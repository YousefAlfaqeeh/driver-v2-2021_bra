package schooldriver.trackware.com.school_bus_driver_android.geofence.mock;

/**
 * Created by   4/3/17.
 */

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;


/**
 * Created   on 12/15/2016.
 */

public class MockLocation {

    public static final String TAG = "MockLocation";

    private Activity context;

    /**
     * The name of the mock location.
     */
    public static final String MacaMall = "com.trackware.GPS.gps.mocks" + ".MacaMall";
    public static final float MacaMall_LATITUDE = 31.97526f;

    public static final float MacaMall_LONGITUDE = 35.84469f;

    public static final float ACCURACY_IN_METERS = 10.0f;

    public static final int AWAIT_TIMEOUT_IN_MILLISECONDS = 2000;

    private GoogleApiClient googleApiClient;

    private static MockLocation INSTANCE;

    protected LocationRequest mLocationRequest;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private static GPSCallback gpsCallback = null;


    public static MockLocation getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MockLocation();
        }
        return INSTANCE;
    }

    public void init(Activity context, GPSCallback gpsCallback) {
        this.context = context;
        this.gpsCallback = gpsCallback;
        connectWithCallbacks(connectionAddListener);
    }

    private void connectWithCallbacks(GoogleApiClient.ConnectionCallbacks callbacks) {
//        googleApiClient = new GoogleApiClient.Builder(context)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(callbacks)
//                .addOnConnectionFailedListener(connectionFailedListener)
//                .build();
//        googleApiClient.connect();
        googleApiClient = StaticValue.mGoogleApiClient;
        createLocationRequest();
    }


    /**
     * Calls the asynchronous methods
     * {@link com.google.android.gms.location.FusedLocationProviderApi#setMockMode} and
     * {@link com.google.android.gms.location.FusedLocationProviderApi#setMockLocation} to set the
     * mock location. Uses nested callbacks when calling setMockMode() and setMockLocation().
     * Each method returns a {@link Status} through a
     * {@link com.google.android.gms.common.api.PendingResult<>}, and setMockLocation() is called
     * only if setMockMode() is successful. Maintains a
     * {@link CountDownLatch} with a count of 1, which makes the current thread wait. Decrements
     * the CountDownLatch count only if the mock location is successfully set, allowing the set up
     * to complete.
     *
     * Called by mocking tester
     *
     */
    public void setMockLocation(final Location mockLocation) {
        // We use a CountDownLatch to ensure that all asynchronous tasks complete within setUp. We
        // set the CountDownLatch count to 1 and decrement this count only when we are certain that
        // mock location has been set.
        final CountDownLatch lock = new CountDownLatch(1);

        // First, ensure that the location provider is in mock mode. Using setMockMode() ensures
        // that only locations specified in setMockLocation(GoogleApiClient, Location) are used.
        LocationServices.FusedLocationApi.setMockMode(googleApiClient, true)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.v(TAG, "Mock mode set");
                            // Set the mock location to be used for the location provider. This
                            // location is used in place of any actual locations from the underlying
                            // providers (network or gps).
                            LocationServices.FusedLocationApi.setMockLocation(
                                    googleApiClient,
                                    mockLocation
                            ).setResultCallback(new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    if (status.isSuccess()) {
                                        Log.v(TAG, "Mock location set");
                                        // Decrement the count of the latch, releasing the waiting
                                        // thread. This permits lock.await() to return.
                                        Log.v(TAG, "Decrementing latch count");
                                        lock.countDown();
                                    } else {
                                        Log.e(TAG, "Mock location not set");
                                    }
                                }
                            });
                        } else {
                            Log.e(TAG, "Mock mode not set");
                        }
                    }
                });

        try {
            // Make the current thread wait until the latch has counted down to zero.
            Log.v(TAG, "Waiting until the latch has counted down to zero");
            lock.await(AWAIT_TIMEOUT_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException exception) {
            Log.i(TAG, "Waiting thread awakened prematurely", exception);
        }
    }

    public void setFakeLocation(Location fakeLocation) {

        if (ActivityCompat.checkSelfPermission(StaticValue.mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StaticValue.mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.setMockLocation(StaticValue.mGoogleApiClient, fakeLocation);
    }

    private GoogleApiClient.ConnectionCallbacks connectionAddListener = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            Log.e(TAG, "connectionAddListener Connecting to GoogleApiClient Connected.");
            try {
                LocationServices.FusedLocationApi.setMockMode(googleApiClient, true);
            }catch (java.lang.SecurityException e){

            }catch (Exception e){

            }
            //MockLocationTester.getInstance().start(1000 * 60 * 60, 100, new LatLng(31.97504,35.84418),2);
            startLocationUpdates();
            //mecamall: 31.97504,35.84418
            // AL karak: 31.169625784064, 35.695611266947
            //Amman: 31.933224837603, 35.890617789465
            // MockLocationTester.getInstance().start( 100, new LatLng(31.180852,35.701375),0.001);
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.e(TAG, "Connecting to GoogleApiClient suspended.");
            //sendError();
        }
    };

    private GoogleApiClient.ConnectionCallbacks connectionRemoveListener = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            Log.e(TAG, "Connecting to GoogleApiClient onConnected.");

        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.e(TAG, "Connecting to GoogleApiClient suspended.");
            //sendError();
        }
    };

    // endregion

    // region OnConnectionFailedListener
    private GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.e(TAG, "Connecting to GoogleApiClient failed.");
            //sendError();
        }
    };

    // endregion

    // region Interfaces


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(5000L);

        mLocationRequest.setInterval(10000L);

    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    public void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).

        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, locationListener);
    }

    public LocationListener locationListener = new LocationListener(){
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged "+location.getLatitude()+","+location.getLongitude());
            MockLocation.gpsCallback.onGPSUpdate(location);
        }
    };

    public Location latLngToLocation(double newLat, double newLong){
        Location location = new Location("network");
        location.setLatitude(newLat);
        location.setLongitude(newLong);
        location.setTime(new Date().getTime());
        location.setAccuracy(0.001f);
        location.setElapsedRealtimeNanos(System.nanoTime());
        return  location;
    }
}
