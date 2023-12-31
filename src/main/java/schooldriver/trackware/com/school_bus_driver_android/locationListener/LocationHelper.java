package schooldriver.trackware.com.school_bus_driver_android.locationListener;

/**
 * Created by   3/29/17.
 */


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import java.util.TimerTask;

import schooldriver.trackware.com.school_bus_driver_android.enums.EnumConfigNotify;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;

public class LocationHelper {

    final String TAG = "LocationHelper.java";
//    LocationHelper locationHelper;
//    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    long valueTimer = 5000;
    ConfigNotify configNotify;
     Activity mActivity;


      
    public boolean getLocation(Activity mActivity, LocationResult result) {
 
        this.mActivity = mActivity;
        // I use LocationResult callback class to pass location value from
        // LocationHelper to user code.
        locationResult = result;


        if (lm == null) {
            lm = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        }

        // exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // don't start listeners if no provider is enabled
        if (!gps_enabled) {
            configNotify = new ConfigNotify(
                    StaticValue.latitudeMain + "",
                    StaticValue.longitudeMain + "",
                    "false",
                    EnumConfigNotify.GPS_OFF, mActivity);
            return false;
        }
//        if (!network_enabled) {
//            configNotify = new ConfigNotify(
//                    MainActivity.latitude + "",
//                    MainActivity.longitude + "",
//                    "false",
//                    EnumConfigNotify.NETWORK,mActivity);
//            return false;
//        }


        // if gps is enabled, get location updates
        if (gps_enabled) {
            Log.e(TAG, "gps_enabled, requesting updates.");
//            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return false;
//            }
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return false;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
        }

        // if network is enabled, get location updates
//        if (network_enabled) {
//            Log.e(TAG, "network_enabled, requesting updates.");
//            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
//        }

        // the timer
//        timer1 = new Timer();
//        timer1.schedule(new GetLastLocation(), valueTimer);

        return false;
    }

    LocationListener locationListenerGps = new LocationListener() {

        public void onLocationChanged(Location location) {

            // gave a location, cancel the timer
//            timer1.cancel();

            // put the location value
            locationResult.gotLocation(location);

            // if you want to stop listening to gps location updates, un-comment the code below

            // lm.removeUpdates(mActivity);
            // lm.removeUpdates(locationListenerNetwork);

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    LocationListener locationListenerNetwork = new LocationListener() {

        public void onLocationChanged(Location location) {

            // gave a location, cancel the timer
//            timer1.cancel();

            // put the location value
            locationResult.gotLocation(location);

            // if you want to stop listening to network location updates, un-comment the code below

            // lm.removeUpdates(mActivity);
            // lm.removeUpdates(locationListenerGps);

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    // stop listening to location updates
    public void stopGettingLocationUpdates() {

        try {


            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.removeUpdates(locationListenerGps);
            lm.removeUpdates(locationListenerNetwork);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class GetLastLocation extends TimerTask {

        @Override
        public void run() {

            // In my case, I do not return the last known location, so I DO NOT remove the updates, just return a location value of null
            // or else, if you need the opposite un-comment the comment below

            /*
            lm.removeUpdates(locationListenerGps);
            lm.removeUpdates(locationListenerNetwork);

            Location net_loc = null, gps_loc = null;
            if (gps_enabled){
                gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (network_enabled){
                net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            // if there are both values use the latest one
            if (gps_loc != null && net_loc != null) {

                if (gps_loc.getTime() > net_loc.getTime()){
                    locationResult.gotLocation(gps_loc);
                }else{
                    locationResult.gotLocation(net_loc);
                }
                return;
            }

            if (gps_loc != null) {
                locationResult.gotLocation(gps_loc);
                return;
            }
            if (net_loc != null) {
                locationResult.gotLocation(net_loc);
                return;
            }
            */

            locationResult.gotLocation(null);
        }
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }

}