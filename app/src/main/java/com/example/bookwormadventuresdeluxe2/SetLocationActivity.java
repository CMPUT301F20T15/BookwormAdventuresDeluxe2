package com.example.bookwormadventuresdeluxe2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, LocationListener
{
    public static final String TAG = "SetLocationActivityTag";
    private static final int LOCATION_REQUEST_CODE = 1001;
    public static final long UPDATE_INTERVAL = 5000; // 5 seconds
    public static final long FASTEST_INTERVAL = 5000;
    private static final int MAX_ADDRESS_RESULT = 4;
    private EditText editTextAddress;
    private Button setLocationButton;

    private ArrayList<String> locationPermissions = new ArrayList<>();
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    private GoogleMap map;
    private Geocoder geocoder;
    private SupportMapFragment mapFragment;

    LocationCallback locationCallback = new LocationCallback()
    {
        @Override
        public void onLocationResult(LocationResult locationResult)
        {
            if (locationResult == null)
            {
                return;
            }
            for (Location location : locationResult.getLocations())
            {
                Log.d("new tag", location.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTitle("Set Location");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        geocoder = new Geocoder(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        /* Add required location permissions to a list*/
        locationPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        locationPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);


        editTextAddress = findViewById(R.id.set_address_text);
        setLocationButton = findViewById(R.id.set_location_button);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        // Configure location request
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
    }

    private void checkSettingsAndStartLocationUpdates()
    {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);

        /* start location updates*/
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>()
        {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse)
            {
                startLocationUpdates();
            }
        });

        /* attempt to resolve exception eg: user has turned of GPS  */
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                if (e instanceof ResolvableApiException)
                {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try
                    {
                        apiException.startResolutionForResult(SetLocationActivity.this, LOCATION_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void startLocationUpdates()
    {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.myLooper());
    }

    private void stopLocationUpdates()
    {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    /**
     * Ask for location permissions
     * G
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            checkSettingsAndStartLocationUpdates();
        }
        else
        {
            /* Ask only for the requests not granted */
            permissionsToRequest = permissionsToRequest(locationPermissions);
            ActivityCompat.requestPermissions(SetLocationActivity.this,
                    permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                    LOCATION_REQUEST_CODE);

        }

    }

    /**
     * Stop requesting location updates
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        stopLocationUpdates();
    }

    private void getLastLocation()
    {
        /* Check location permission*/
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        System.out.println("on Entry");

        locationTask.addOnSuccessListener(new OnSuccessListener<Location>()
        {
            @Override
            public void onSuccess(Location location)
            {
                Log.d(TAG, "on Success: " + location.toString());
            }
        });

    }

    /**
     * Gets last location if permissions have been granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE)
        {
            for (String perm : permissionsToRequest)
            {
                if (!hasPermission(perm))
                {
                    permissionsRejected.add(perm);
                }
            }

            if (permissionsRejected.size() > 0)
            {

                if (ActivityCompat.shouldShowRequestPermissionRationale(SetLocationActivity.this, permissionsRejected.get(0)))
                {
                    new AlertDialog.Builder(SetLocationActivity.this)
                            .setMessage("Location Permissions are required.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {

                                    ActivityCompat.requestPermissions(SetLocationActivity.this, permissionsRejected.toArray(
                                            new String[permissionsRejected.size()]), LOCATION_REQUEST_CODE);

                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .create()
                            .show();
                }


            }
            else
            {
                checkSettingsAndStartLocationUpdates();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.setOnMapLongClickListener(this);

        try
        {
            List<Address> addresses = geocoder.getFromLocationName("london", MAX_ADDRESS_RESULT);
            if (addresses.size() > 0)
            {
                Address address = addresses.get(0); // Get Best match
                LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(location)
                        .title("Pickup Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                map.addMarker(markerOptions);

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
            }

        } catch (IOException e)
        {
            Log.d(TAG, e.getMessage());
        }


        // remove
    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        //  remove
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions)
    {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions)
        {
            if (!hasPermission(perm))
            {
                result.add(perm);
            }
        }

        return result;
    }

    /* Can check if permisson was previously accepted on SDK > M*/
    private boolean hasPermission(String perm)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            return checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @Override
    public void onMapLongClick(LatLng latLng)
    {
//        
    }
}
