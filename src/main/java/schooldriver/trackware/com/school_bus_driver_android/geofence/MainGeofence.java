package schooldriver.trackware.com.school_bus_driver_android.geofence;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import schooldriver.trackware.com.school_bus_driver_android.bean.GeofenceBean;

/**
 * Created by muradtrac on 4/3/17.
 */

public class MainGeofence implements ResultCallback {

    private static final String TAG = MainGeofence.class.getSimpleName();
    private final Activity mActivity;
    private final GoogleApiClient mGoogleApiClient;
    private final List<GeofenceBean> listGeofenceBean;
    private static final int REQUEST_PERMISSION_LOCATION = 34839;


    public MainGeofence(Activity mActivity, GoogleApiClient mGoogleApiClient, List<GeofenceBean> listGeofenceBean) {
        this.mActivity = mActivity;
        this.mGoogleApiClient = mGoogleApiClient;
        this.listGeofenceBean = listGeofenceBean;
    }





    public void refreshGeofences() {
//        removeGeofences();
        List<Geofence> geofences = new ArrayList<>();
        for (GeofenceBean geofenceBean:listGeofenceBean){
            Geofence geofence = new Geofence.Builder()
                    .setRequestId(geofenceBean.getId()+"@@"+geofenceBean.getName())
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setCircularRegion(geofenceBean.getLatitude(), geofenceBean.getLongitude(),
                            (float) 50)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                            | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .setLoiteringDelay(1000)
                    .build();
            geofences.add(geofence);
        }

        addGeofences(geofences);
    }

    private void removeGeofences() {
        Log.d(TAG, "removeGeofences()");
        if (mGoogleApiClient == null) {
            Log.d(TAG, "Failed to remove geofence because Google API client is null!");
            return;
        }
        List<String> geofences = new ArrayList<>();
        for (GeofenceBean geofenceBean:listGeofenceBean)
        geofences.add(geofenceBean.getId()+"@@"+geofenceBean.getName());
//        LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient, geofences);
        LocationServices.getGeofencingClient(mActivity).removeGeofences(geofences);
    }

    private void addGeofences(List<Geofence> geofenceList) {
        if (geofenceList == null) {
            throw new IllegalArgumentException("Argument 'geofenceList' cannot be null.");
        }

        if (!mGoogleApiClient.isConnected()) {
            Log.d(TAG, "Google API client is not connected!");
            return;
        }

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    createGeofencingRequest(geofenceList),
                    GeofenceTransitionIntentService.newPendingIntent(mActivity, convertMarkersToLatlng(listGeofenceBean))
            ).setResultCallback(this);
        } else {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION_LOCATION);
        }
    }

    private static GeofencingRequest createGeofencingRequest(List<Geofence> geofenceList) {
        if (geofenceList == null) {
            throw new IllegalArgumentException("Argument 'geofenceList' cannot be null.");
        }
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
                .addGeofences(geofenceList)
                .build();
    }

    private static ArrayList<LatLng> convertMarkersToLatlng(List<GeofenceBean> markers) {
        ArrayList<LatLng> points = new ArrayList<>();
        for (GeofenceBean marker : markers) {
            points.add(new LatLng(marker.getLatitude(),marker.getLongitude()));
        }
        return points;
    }

    @Override
    public void onResult(@NonNull Result result) {
        Log.d(TAG, "onResult() "+result);
    }
}
