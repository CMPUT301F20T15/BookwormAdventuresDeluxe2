package com.example.bookwormadventuresdeluxe2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

// https://stackoverflow.com/questions/15249409/getting-error-in-barcode-scanner-in-android
// Better way for later: https://www.youtube.com/watch?v=drH63NpSWyk
public class IsbnScanActivity extends AppCompatActivity implements View.OnClickListener
{
    Button scanButton;
    TextView barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isbn_scan);

        scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(this);
    }

    /**
     * Clicked the "Scan" button
     *
     * @param v
     */
    @Override
    public void onClick(View v)
    {
        IntentIntegrator integrator = new IntentIntegrator(
                IsbnScanActivity.this);
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    public void openDialogue(View v)
    {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // I added this so the back button in the app bar would just end the activity
        // Not start a new MyBooksActivity
        // https://stackoverflow.com/questions/14437745/how-to-override-action-bar-back-button-in-android
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("isbn", "4206669");
                setResult(1, intent); // You can also send result without any data using setResult(int resultCode)
                finish();
                break;
        }
        return true;
    }

    /**
     * This method gets called when returning from the barcode scanner function
     * Process the data here then return it to the calling activity with finis()
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, intent);
        if (scanResult != null)
        {
            Toast.makeText(getApplicationContext(), "scan   " + scanResult.getContents(), Toast.LENGTH_LONG).show();
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }
}