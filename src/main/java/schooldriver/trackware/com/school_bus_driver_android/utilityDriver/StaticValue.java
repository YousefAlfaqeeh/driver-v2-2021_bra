package schooldriver.trackware.com.school_bus_driver_android.utilityDriver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;
import java.util.Locale;

import schooldriver.trackware.com.school_bus_driver_android.LoginFragment;
import schooldriver.trackware.com.school_bus_driver_android.dbDriver.DBHandler;
import schooldriver.trackware.com.school_bus_driver_android.enums.TypeRoundEnum;

/**
 * Created by muradtrac on 9/19/17.
 */

public class StaticValue {

    public static final boolean G34 = true;

    public static boolean OPEN_GPS;
//    public static boolean SOCKET_API = false;
    public static int SUM_NOTIFICATION;
    public static String ADDRESS;
    public static double latitudeMain;
    public static double longitudeMain;
    public static double latitudeLast;
    public static double longitudeLast;
    public static double latitudeSpeed;
    public static double longitudeSpeed;
//    public static double latitudeStandStill;
//    public static double longitudeStandStill;
    public static double longitudeDistance;
    public static double latitudeDistance;
    public static double distance;
    public static double latitudeStartRound;
    public static double longitudeStartRound;
    public static List<String> listNearby;
    public static boolean BUS_LOCATION_TYPE;
    public static Location locationFromMap;

    public static double getCurrentSpeed() {
        if (locationFromMap == null)
            return 0.0;
        else if (!locationFromMap.hasSpeed())
            return 0.0;
        else
            return locationFromMap.getSpeed() * 3.6;
    }

    public static String getCurrentAreaName(Context c) {
        String currentAreaName = "";
        try {
            String local = UtilityDriver.getStringShared(UtilityDriver.LANGUAGE).equals("") ? "en" : UtilityDriver.getStringShared(UtilityDriver.LANGUAGE);
            Geocoder geocoder = new Geocoder(c, new Locale(local));
            List<Address> listAddresses = geocoder.getFromLocation(locationFromMap.getLatitude(), locationFromMap.getLongitude(), 1);
            if (null != listAddresses && listAddresses.size() > 0) {
                currentAreaName = listAddresses.get(0).getAddressLine(0);
            }
        } catch (Exception e) {

        }
        return currentAreaName;


    }


    public static DBHandler dbHandler;
    public static Activity mActivity;
    public static GoogleApiClient mGoogleApiClient;
    public static LoginFragment loginFragment;
    public static ProgressDialog progressDialog;

    public static TypeRoundEnum typeRoundEnum;
    public static double diagonalInches;
    public static long milliSeconds = 0;
    public static Handler handler = new Handler();
    public static Runnable runnable;
    public static String TIMER_TEXT;


}
