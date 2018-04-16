package demo.vend.wifidistancecalculator.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import demo.vend.wifidistancecalculator.ApplicationState;
import demo.vend.wifidistancecalculator.R;
import demo.vend.wifidistancecalculator.storage.AppPreference;
import demo.vend.wifidistancecalculator.utils.Constants;

public class MainActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final int INTERVAL = 10000;
    private static final int FASTEST_INTERVAL = 5000;

    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private boolean waitForPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String username = AppPreference.getString(mContext, Constants.KEY_USER_NAME, Constants.EMPTY_STRING);
        if(username==null)
            getUserDetails();
        else if (username.isEmpty())
            getUserDetails();
        else
            initMainActivity();

    }

    private void initMainActivity() {
        initComponents();
        checkForPermissions();
        if (!waitForPermissions && isLocationEnabled()) {
            openDashboardActivity();
        }
    }

    private void getUserDetails() {

        final EditText etUserName = new EditText(this);

        etUserName.setHint("Your name..");
        new AlertDialog.Builder(this)
                .setTitle("Please Register Yourself")
                .setView(etUserName)
                .setCancelable(false)
                .setPositiveButton("Start Tracking", (dialog, whichButton) -> {
                    String name = etUserName.getText().toString();
                    AppPreference.saveString(mContext,name,Constants.KEY_USER_NAME);
                    initMainActivity();
                })
                .setNegativeButton("Quit", (dialog, which) -> MainActivity.this.finish())
                .show();
    }

    private void initComponents() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }
    private ResultCallback<LocationSettingsResult> locationCallBack = new ResultCallback<LocationSettingsResult>() {
        @Override
        public void onResult(LocationSettingsResult result) {
            final Status status = result.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can initialize location requests here.
                   openDashboardActivity();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    showEnableLocationDialog(status);
                    break;
                default:
                    break;
            }
        }

        private void showEnableLocationDialog(Status status) {
            try {// Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
            } catch (IntentSender.SendIntentException e) {
                Logger.getAnonymousLogger().log(Logger.getGlobal().getLevel(),e.getMessage());
            }
        }
    };

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void enableLocation() {

        if (!isLocationEnabled()) {
            LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setInterval(INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                    builder.build());
            result.setResultCallback(locationCallBack);
        }
    }
    private void checkForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> listPermissionsNeeded = new ArrayList<>();
            int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int phonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            int storagePermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (locationPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (phonePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                requestPermissions(listPermissionsNeeded.toArray(new String[0]),
                        PERMISSIONS_MULTIPLE_REQUEST);
                waitForPermissions = true;
            } else {
                waitForPermissions = false;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_MULTIPLE_REQUEST) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.permission_error_message), Toast.LENGTH_SHORT).show();
            }
            onPermissionResult();
        }
    }

    private void onPermissionResult() {
        waitForPermissions = false;
        if (!isLocationEnabled()) {
            mGoogleApiClient.connect();
        } else {
            openDashboardActivity();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (!waitForPermissions) {
            enableLocation();
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        //on connection suspended
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //on connection failed
    }
    private void openDashboardActivity() {
        Intent i = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(i);
        finish();
    }
}
