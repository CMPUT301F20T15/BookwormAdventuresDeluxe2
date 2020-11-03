package com.example.bookwormadventuresdeluxe2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

// Todo: Rename Class to ProfileFragment or rename other fragments

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment implements View.OnClickListener
{
    private static final String TAG = "MyProfileFragment";
    public static final Integer SetLocationActivityResultCode = 1;
    Button edit;
    Button signOutButton;
    MaterialTextView appHeaderText;
    View view;

    private FirebaseAuth firebaseAuth;

    public MyProfileFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        signOutButton = view.findViewById(R.id.profile_logout);
        signOutButton.setOnClickListener(this);

        edit = view.findViewById(R.id.profile_edit);
        edit.setOnClickListener(this);

        /* Set title */
        appHeaderText = view.findViewById(R.id.app_header_title);
        appHeaderText.setText(R.string.my_profile_title);

        /* Set Location Demo*/
        Button setLocationButton = view.findViewById(R.id.setLocationDemobutton);
        setLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent setLocationActivityIntent = new Intent(getActivity(), SetLocationActivity.class);
                startActivityForResult(setLocationActivityIntent, SetLocationActivityResultCode);
            }
        });

        /* Set View Location Demo*/
        Button viewLocationDemo = view.findViewById(R.id.viewLocationDemoButton);
        viewLocationDemo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent viewLocationIntent = new Intent(getActivity(), ViewLocationActivity.class);
                viewLocationIntent.putExtra("location", "53.510787,-113.5140128");
                startActivity(viewLocationIntent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        return view;
    }

    /**
     * Handle click on Profile Edit and SignOut button
     *
     * @param view View containing layout resources
     */
    @Override
    public void onClick(View view)
    {
        try
        {
            switch (view.getId())
            {
                case R.id.profile_edit:
                    final View editInfo = LayoutInflater.from(this.getContext()).inflate(R.layout.edit_profile, null);

                    // Set up the input
                    final EditText inputEmail = editInfo.findViewById(R.id.edit_email);
                    final EditText inputPhone = editInfo.findViewById(R.id.edit_phone);
                    // Specify the type of input expected
                    inputEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    inputPhone.setInputType(InputType.TYPE_CLASS_PHONE);

                    final AlertDialog builder = new AlertDialog.Builder(this.getContext()).create();
                    builder.setView(editInfo);

                    // Set up the buttons
                    editInfo.findViewById(R.id.edit_confirm).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            //TODO: test input, get input, update user
                            builder.dismiss();
                        }
                    });

                    editInfo.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            builder.dismiss();
                        }
                    });

                    builder.show();

                    break;
                case R.id.profile_logout:
                    /*
                     * Listener for signOut button to sign user out of firebase account
                     * Source : https://stackoverflow.com/questions/53334017/back-button-will-bring-to-home-page-after-firebase-logout-on-app
                     * */
                    if (firebaseAuth != null)
                    {
                        firebaseAuth.signOut();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        /* Take User back to Login Page */
                        startActivity(intent);
                    }
                default:
                    /* Unexpected resource id*/
                    throw new Exception("Unexpected resource Id inside click listener."
                            + "Expected: R.id.login_button Or R.idcreate_account_button");
            }
        } catch (Exception e)
        {
            /* Log message to debug*/
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SetLocationActivityResultCode)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String pickUpLocation = data.getStringExtra("pickUpLocation");
                Toast.makeText(getContext(), pickUpLocation, Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                //Write your code if there's no result
            }
        }
    }
}