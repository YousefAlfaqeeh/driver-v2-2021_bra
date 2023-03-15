package schooldriver.trackware.com.school_bus_driver_android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import database.CheckInOut;
import database.DAO;
import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;
import schooldriver.trackware.com.school_bus_driver_android.app.Application;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseActivity;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseFragment;
import schooldriver.trackware.com.school_bus_driver_android.bean.NotificationBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.RoundBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.dbDriver.DBHandler;
import schooldriver.trackware.com.school_bus_driver_android.fragment.changeLocation.ChangeLocationFragment;
import schooldriver.trackware.com.school_bus_driver_android.fragment.map.MapFragment;
import schooldriver.trackware.com.school_bus_driver_android.fragment.round.RoundFragment;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.RoundInfoFragment;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.RoundInfoFragment_NEW;
import schooldriver.trackware.com.school_bus_driver_android.gcmNotification.firebace.MyFirebaseMessagingService_new;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.OnActionDoneListener;
import schooldriver.trackware.com.school_bus_driver_android.kalman.KalmanLocationManager;
import schooldriver.trackware.com.school_bus_driver_android.locationListener.LocationListener;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.PathUrl;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilDialogs;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

//import com.bosphere.filelogger.FL;

public class MainActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, /*IRestCallPhoenix,*/
        OnMapReadyCallback, BeaconConsumer {

    public String fireBaseToken = "null";

    public void hideSoftKeyboard() {
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {

        }


    }

    private void initFireBase() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                try {
                    String newToken = instanceIdResult.getToken();
                    fireBaseToken = newToken + "";
                    Log.v("FirebaseInstanceId", fireBaseToken);
                } catch (Exception e) {
                    Log.v("FirebaseInstanceId", e.getMessage());
                    fireBaseToken = "";
                }


            }
        });
    }

    private boolean isBluetoothDialogClosedBefore = false;
    private OnActionDoneListener<Object> onBackButtonPressed = null;
    LocationManager locationManager;
    static FragmentManager fragmentManager;
    static FragmentTransaction fragmentTransaction;
    public static GoogleMap mMap;
    public static int count = 0;
    static View root_view;
    private final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final String TAG = "MainActivity";
    public LocationListener locationListener = new LocationListener();
    public static RoundBean CURRENT_SELECTED_ROUND = null;

    private IRestCallBack doNothingCallBack = new IRestCallBack() {
        @Override
        public void onRestCallBack(String response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

        }

        @Override
        public void onRestCallBack(JSONObject response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

        }

        @Override
        public void onRestCallBack(VolleyError volleyError, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

        }

        @Override
        public void onRestCallBack(NetworkResponse networkResponse, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

        }
    };
    Timer timerPhoenix;
    TimerTask timerTaskPhoenix;

    final Handler handlerPhoenix = new Handler();


    Timer timerLocationListener;
    TimerTask timerTaskLocationListener;

    final Handler handlerLocationListener = new Handler();
    private int MY_LOCATION_REQUEST_CODE = 200;
//    public boolean backButtonEnabled = true;

    private KalmanLocationManager mKalmanLocationManager;

    public String timeBetweenStopAndNow() {
        if (locationListener == null)
            return null;
        return locationListener.timeBetweenStopAndNow();

    }

    public void setOnBackButtonPressed(OnActionDoneListener<Object> onBackButtonPressed) {
        this.onBackButtonPressed = onBackButtonPressed;
    }

    private void initKarml(final Context context) {
        final long GPS_TIME = 200;
        final long NET_TIME = 200;
        final long FILTER_TIME = 200;

        /**/

        if (mKalmanLocationManager == null) {
            mKalmanLocationManager = new KalmanLocationManager(context);
            mKalmanLocationManager.requestLocationUpdates(
                    KalmanLocationManager.UseProvider.GPS_AND_NET, FILTER_TIME, GPS_TIME, NET_TIME, new android.location.LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            checkGPS();

//                            if (location.hasSpeed()){
//                                Log.v("NewSpeed",""+location.getCurrentSpeed());
//                                Toast.makeText(MainActivity.this,"Speed = "+location.getCurrentSpeed(),Toast.LENGTH_SHORT).show();
//                            }else {
//                                Log.v("No Speed","No Speed");
//                                Toast.makeText(MainActivity.this,"No Speed",Toast.LENGTH_SHORT).show();
//
//                            }


                            if (location.getProvider().equals(KalmanLocationManager.KALMAN_PROVIDER)) {
//                                Log.d("#######", "AAAonLocationChanged " + MainActivity.latitudeMain  + "," + MainActivity.longitudeMain+" ,getAccuracy:"+location.getAccuracy());
                                int acc = 0;
                                try {
                                    acc = Integer.parseInt(String.valueOf(location.getAccuracy()));
                                } catch (NumberFormatException e) {
                                    //Log it if needed
                                    acc = (int) location.getAccuracy();
                                }

                                if (acc <= 13) {
                                    StaticValue.locationFromMap = location;
                                    StaticValue.latitudeMain = location.getLatitude();
                                    StaticValue.longitudeMain = location.getLongitude();
//                                    Log.d("#######", "BBBonLocationChanged " + StaticValue.latitudeMain + "," + StaticValue.longitudeMain + " ,getAccuracy:" + location.getAccuracy());
//                                    accuracyCount=0;
                                }
// else {
//                                    accuracyCount++;
//                                }

                            }
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {
                            checkGPS();
//                            Toast.makeText(context, "onStatusChanged", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onProviderEnabled(String s) {
//                            checkGPS(true);
//                            Toast.makeText(context, "onProviderEnabled", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onProviderDisabled(String s) {
                            checkGPS(false, false);
//                            Toast.makeText(context, "onProviderDisabled", Toast.LENGTH_SHORT).show();
                        }
                    }, true);
        }
    }


    public void startTimerPhoenix() {
        //set a new Timer
        timerPhoenix = new Timer();
//        Log.v("ooooooooooooooooooooooooooooooooooooo","ooooooooooooooo");
        //initialize the TimerTask's job
        try {
            timerOpenPhoenix();
        } catch (OutOfMemoryError outOfMemoryError) {
//            Log.v("ooooooooooooooooooooooooooooooooooooo1111", String.valueOf(outOfMemoryError));
        }

        if (UtilityDriver.getIntShared(UtilityDriver.CHANEL_REFRESH_RATE) == 0) {
            UtilityDriver.setIntShared(UtilityDriver.CHANEL_REFRESH_RATE, 5);
        }

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
//        timerPhoenix.schedule(timerTaskPhoenix, 5 * 1000, 5 * 1000);
//        Log.v("yousef ahmad timerPhoenix", String.valueOf(UtilityDriver.getIntShared(UtilityDriver.CHANEL_REFRESH_RATE)));
        timerPhoenix.schedule(timerTaskPhoenix, UtilityDriver.getIntShared(UtilityDriver.CHANEL_REFRESH_RATE) * 1000, UtilityDriver.getIntShared(UtilityDriver.CHANEL_REFRESH_RATE) * 1000); //
    }


    public void timerOpenPhoenix() {
        try {

            timerTaskPhoenix = new TimerTask() {
                public void run() {
                    checkGPS();
                    //use a handler to run a toast that shows the current timestamp
                    handlerPhoenix.post(new Runnable() {
                        public void run() {


                            Thread thread = new Thread(new Runnable() {

                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                                @Override
                                public void run() {
//                                if (mActivity == null){
//                                    return;
//                                }
//                                if(!UtilityDriver.getBooleanShared(UtilityDriver.LOGIN)){
//                                    return;
//                                }
                                    Thread thread = new Thread(new Runnable() {

                                        @Override
                                        public void run() {
                                            try {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        if (StaticValue.SUM_NOTIFICATION > 0) {
//                                                            if (RoundFragment.labNotification != null) {
//                                                                RoundFragment.labNotification.setVisibility(View.VISIBLE);
//                                                                RoundFragment.labNotification.setText(StaticValue.SUM_NOTIFICATION + "");
//                                                            }
//                                                            if (RoundInfoFragment.labNotification != null) {
//                                                                RoundInfoFragment.labNotification.setVisibility(View.VISIBLE);
//                                                                RoundInfoFragment.labNotification.setText(MainActivity.SUM_NOTIFICATION + "");
//                                                            }
                                                        }

                                                    }
                                                });

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    thread.start();

                                    if (!UtilityDriver.getBooleanShared(UtilityDriver.LOGIN)) {
                                        return;
                                    }
                                    if (UtilityDriver.isNetworkAvailable(StaticValue.mActivity)) {
//                                        try {
//                                            offlineModelLocation();
//                                        } catch (OutOfMemoryError | NullPointerException outOfMemoryError) {
//
//                                        }
                                        try {
                                            Log.v("oooooo","ppppp");
                                            offlineModelStatus();
                                        } catch (OutOfMemoryError | NullPointerException outOfMemoryError) {

                                        }
                                    }

                                    if (RoundInfoFragment.ROUND_ID_SOCKET == 0) {
                                        return;
                                    }


                                    try {


                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (StaticValue.OPEN_GPS)
                                                    initKarml(StaticValue.mActivity);

                                            }
                                        });
//                                        System.err.println(StaticValue.latitudeMain + " " + StaticValue.longitudeMain + "       22222222222");
                                        if (RoundInfoFragment.listGeofenceBean != null && RoundInfoFragment.listGeofenceBean.size() > 0) {

                                            if (RoundInfoFragment.TYPE_CHECK.equals("")) {
                                                if (UtilityDriver.isPointInPolygon(new LatLng(StaticValue.latitudeMain, StaticValue.longitudeMain), RoundInfoFragment.listGeofenceBean)) {
                                                    RoundInfoFragment.TYPE_CHECK = "in";
//                                                    UtilityDriver.showMessageDialog(mActivity,"GEOFENCE", RoundInfoFragment.TYPE_CHECK);
                                                    System.err.println(RoundInfoFragment.TYPE_CHECK + "  CHECKCHECK");
//                                                    yousef
//                                                    callRestAPI(PathUrl.MAIN_URL + PathUrl.NOTIFY
//                                                            ,
//                                                            new HashMap<String, String>() {{
//                                                                put("name", "check");
//                                                                put("move", RoundInfoFragment.TYPE_CHECK);
//                                                                put("geo_type", "place");
//                                                                put("geo_id", RoundInfoFragment.roundBean.getId() + "");
//                                                                put("geo_name", RoundInfoFragment.roundBean.getNameRound());
//                                                                put("lat", "" + StaticValue.latitudeMain);
//                                                                put("long", "" + StaticValue.longitudeMain);
//                                                            }}
//                                                            ,
//                                                            EnumMethodApi.POST
//                                                            ,
//                                                            doNothingCallBack
//                                                            ,
//                                                            EnumNameApi.GEOFENCE
//                                                            ,
//                                                            UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
//                                                            ,
//                                                            EnumTypeHeader.JSON
//                                                    );
                                                }
                                            } else if (RoundInfoFragment.TYPE_CHECK.equals("in")) {
                                                if (!UtilityDriver.isPointInPolygon(new LatLng(StaticValue.latitudeMain, StaticValue.longitudeMain), RoundInfoFragment.listGeofenceBean)) {
                                                    RoundInfoFragment.TYPE_CHECK = "out";
//                                                    UtilityDriver.showMessageDialog(mActivity,"GEOFENCE", RoundInfoFragment.TYPE_CHECK);
                                                    System.err.println(RoundInfoFragment.TYPE_CHECK + "  CHECKCHECK");
//yousef
//                                                    callRestAPI(PathUrl.MAIN_URL + PathUrl.NOTIFY
//                                                            ,
//                                                            new HashMap<String, String>() {{
//                                                                put("name", "check");
//                                                                put("move", RoundInfoFragment.TYPE_CHECK);
//                                                                put("geo_type", "place");
//                                                                put("geo_id", RoundInfoFragment.roundBean.getId() + "");
//                                                                put("geo_name", RoundInfoFragment.roundBean.getNameRound());
//                                                                put("lat", "" + StaticValue.latitudeMain);
//                                                                put("long", "" + StaticValue.longitudeMain);
//                                                            }}
//                                                            ,
//                                                            EnumMethodApi.POST
//                                                            ,
//                                                            doNothingCallBack
//                                                            ,
//                                                            EnumNameApi.GEOFENCE
//                                                            ,
//                                                            UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
//                                                            ,
//                                                            EnumTypeHeader.JSON
//                                                    );
                                                    RoundInfoFragment.TYPE_CHECK = "";
                                                }
                                            }
                                        }

//                                        if (StaticValue.SOCKET_API) {
//
////                                            valueSend = false;
//                                            driver.refresh(Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.BUS_ID)));
////                                                PhoenixPlug.getInstance().joinBus(MainActivity.this,Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.BUS_ID)),SEND_LOCATION);
//                                        }
//                                        else {

//                                            System.err.println(StaticValue.latitudeMain + " " + StaticValue.longitudeMain + "       444444444");
//                                                latitude = 31.975273;
//                                                longitude =  35.843926;
                                        if (StaticValue.latitudeMain > 0 && StaticValue.longitudeMain > 0) {


                                            boolean changeLocation = false;
                                            if (UtilityDriver.distance(StaticValue.latitudeMain, StaticValue.longitudeMain, StaticValue.latitudeLast, StaticValue.longitudeLast, "M") > 4) {
                                                changeLocation = true;
                                            }
//                                                        UtilityDriver.showMessageDialog(mActivity,latitude+"   "+UtilityDriver.getStringShared(UtilityDriver.BUS_ID)+" "+longitude,latitudeLast+" "+longitudeLast);
                                            StaticValue.latitudeLast = StaticValue.latitudeMain + 0;
                                            StaticValue.longitudeLast = StaticValue.longitudeMain + 0;
//                                                final boolean finalChangeLocation = changeLocation;
                                            final int store_order = UtilityDriver.getIntShared(UtilityDriver.STORE_ORDER) + 1;
                                            UtilityDriver.setIntShared(UtilityDriver.STORE_ORDER, store_order);
                                            String round_ST = UtilityDriver.getStringShared(UtilityDriver.ROUND_ST);
                                            Log.v("round_ST___",round_ST);


                                            if( round_ST.equals("true")) {
                                                Log.v("round_ST___1111111111111111111111111111111111111",round_ST);
                                                Application.addLocation(RoundInfoFragment.ROUND_ID_SOCKET + "", StaticValue.longitudeMain, StaticValue.latitudeMain);
                                            }
//                                                HashMap map = new HashMap() {{
//                                                    put("long", StaticValue.longitudeMain);
//                                                    put("lat", StaticValue.latitudeMain);
//                                                    put("bus_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.BUS_ID)));
//                                                    put("school_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.SCHOOL_ID)));
//                                                    put("round_id", RoundInfoFragment.ROUND_ID_SOCKET);
//                                                    put("store_order", store_order);
////                                                    put("speed", StaticValue.getCurrentSpeed());
//                                                }};
//                                                DAO.addLonLat( lonLat);
//                                                if (!UtilityDriver.isNetworkAvailable(StaticValue.mActivity)) {
//                                                    LonLat lonLat = new LonLat("", "", new JSONObject(map).toString()/*,StaticValue.getCurrentSpeed()*/);
//                                                    DAO.addLonLat( lonLat);
//                                                } else {
//                                                    if (!StaticValue.BUS_LOCATION_TYPE) {
//                                                        map.put("location_changed", finalChangeLocation);
//                                                        callRestAPI(PathUrl.MAIN_URL/*"http://192.168.1.17:4011"*/ + PathUrl.BUS_LOCATION
//                                                                ,
//                                                                map
//                                                                ,
//                                                                EnumMethodApi.POST
//                                                                ,
//                                                                MainActivity.this
//                                                                ,
//                                                                EnumNameApi.BUS_LOCATION
//                                                                ,
//                                                                UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
//                                                                ,
//                                                                EnumTypeHeader.JSON
//                                                        );
//                                                        System.err.println(new JSONObject(map) + "......TESTROUND.....");
//                                                        StaticValue.BUS_LOCATION_TYPE = true;
//                                                    } else {
//                                                        LonLat lonLat = new LonLat("", "", new JSONObject(map).toString()/*,StaticValue.getCurrentSpeed()*/);
//                                                        DAO.addLonLat( lonLat);
//                                                    }
//                                                }
//                                                valueSend = false;
                                        }

//                                        }

// NetworkUtil.getNetworkPhone(StaticValue.mActivity);
                                    } catch (Exception e) {
                                        if (StaticValue.mActivity != null)
//                                            UtilityDriver.showMessage(mActivity,e.getMessage());
                                        {
                                            new UtilityDriver().logProject("timerOpenPhoenix", "PhoenixPlug :: " + e.getMessage());
                                        }
                                        e.printStackTrace();
                                    }


                                }
                            });

                            thread.start();
                        }
                    });
                }
            };


        } catch (OutOfMemoryError | NumberFormatException outOfMemoryError) {

        }
    }

    private void offlineModelStatus() throws OutOfMemoryError {

        String jsonStatusEnd = "[JSONJSON]";

        String jsonAbsent = "";
        String jsonEnd = "{}";
//        String statusEnd = "";
        List<CheckInOut> allRemindersObj = DAO.getAllRemindersObj(Application.database);
        if (allRemindersObj.size() == 0) {
            return;
        }
        for (int i = 0; i < allRemindersObj.size(); i++) {
            CheckInOut checkInOut = allRemindersObj.get(i);
            if (checkInOut.getDate().contains("\"status\":\"end\"") || checkInOut.getDate().contains("\"status\":\"cancel\"")) {
                jsonEnd = checkInOut.getDate();
                break;
            } else {
//            if (i == allRemindersObj.size() - 1) {
//                jsonAbsent += checkInOut.getDate();
//            } else {
                jsonAbsent += checkInOut.getDate() + ",";
//            }
            }
        }

        jsonStatusEnd = jsonStatusEnd.replaceAll("JSONJSON", jsonAbsent);
        jsonStatusEnd = jsonStatusEnd.replaceAll(",]", "]");


        JSONArray finalJsonAbsent = null;
        try {
            finalJsonAbsent = new JSONArray(jsonStatusEnd.equals("") ? "{}" : jsonStatusEnd);
            final JSONObject jsonObjectEnd = new JSONObject(jsonEnd.equals("") ? "{}" : jsonEnd);

            final JSONArray finalJsonAbsent1 = finalJsonAbsent;
//            Log.v("ddddddddddddddddddddddddd","ttttttttttttttttttttttttttttt");
            callRestAPI(PathUrl.MAIN_URL + PathUrl.OFFLINE_STATUS
                    ,
                    new HashMap() {{
                        put("students", finalJsonAbsent1);
                        put("round", jsonObjectEnd);
                    }}
                    ,
                    EnumMethodApi.POST
                    ,
                    new IRestCallBack() {
                        @Override
                        public void onRestCallBack(String response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

                        }

                        @Override
                        public void onRestCallBack(JSONObject response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
                            try {
                                if (response.getString("status").contains("ok")) {
//                                    DAO.deleteAll(Application.database, DAO.CheckInOut_TBL);
                                }
                            } catch (Exception e) {
                                try {
//                                    DAO.deleteAll(Application.database, DAO.CheckInOut_TBL);
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }

                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onRestCallBack(VolleyError volleyError, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

                        }

                        @Override
                        public void onRestCallBack(NetworkResponse networkResponse, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

                        }
                    }
                    ,
                    EnumNameApi.OFFLINE_STATUS
                    ,
                    UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
                    ,
                    EnumTypeHeader.JSON
            );
            DAO.deleteAll(Application.database, DAO.CheckInOut_TBL);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private void offlineModelLocation() throws OutOfMemoryError {
//
//
//        String jsonLOcation = "[JSONJSON]";
//
//        String arrayJson = "";
//
//        List<LonLat> lonLatList = DAO.getAllLonLat(Application.database);
//        if (lonLatList.size() == 0) {
//            return;
//        }
//        for (int i = 0; i < lonLatList.size(); i++) {
//            LonLat lonLat = lonLatList.get(i);
//            if (i == lonLatList.size() - 1) {
//                arrayJson += lonLat.getDate();
//            } else {
//                arrayJson += lonLat.getDate() + ",";
//            }
//        }
//        String replaceOne = "";
//        jsonLOcation = jsonLOcation.replaceAll("JSONJSON", arrayJson);
//
//
//        JSONArray finalJsonLOcation = null;
//        try {
//            finalJsonLOcation = new JSONArray(jsonLOcation);
//
//            final JSONArray finalJsonLOcation1 = finalJsonLOcation;
////            System.err.println(finalJsonLOcation1.toString()+"   finalJsonLOcation1");
//            callRestAPI(PathUrl.MAIN_URL/*"http://192.168.1.17:4011"*/ + PathUrl.OFFLINE_LOCATION
//                    ,
//                    new HashMap() {{
//                        put("date", DateTools.Formats.DATEONLY_FORMAT_GMT.format(new Date()));/*UtilityDriver.convertUTCDate(UtilityDriver.getDateFormat("yyyy-MM-dd")*/
//                        put("locations", finalJsonLOcation1);
//                    }}
//                    ,
//                    EnumMethodApi.POST
//                    ,
//                    MainActivity.this
//                    ,
//                    EnumNameApi.OFFLINE_LOCATION
//                    ,
//                    UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
//                    ,
//                    EnumTypeHeader.JSON
//            );
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    public void stopTimerPhoenix() {
//        //stop the timer, if it's not already null
//        if (timerPhoenix != null) {
//            timerPhoenix.cancel();
//            timerPhoenix = null;
//        }
//    }


    public void startTimerLocationListener() {
        //set a new Timer
        timerLocationListener = new Timer();

        //initialize the TimerTask's job
        try {
            timerOpenLocationListener();
        } catch (OutOfMemoryError outOfMemoryError) {

        }

        timerLocationListener.schedule(timerTaskLocationListener, 5 * 1000, 5 * 1000); //
    }

    private void timerOpenLocationListener() {


        timerTaskLocationListener = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (StaticValue.mActivity == null) {
                            return;
                        }
                        StaticValue.mActivity.runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                            public void run() {
                                if (!UtilityDriver.getBooleanShared(UtilityDriver.LOGIN)) {
                                    return;
                                }

                                try {
                                    count++;
                                    if (RoundInfoFragment.roundBean != null) {

                                        locationListener.onStart(StaticValue.mActivity);
                                    }
                                } catch (Exception e) {
                                    Log.d("TTTTTTT", e.getMessage() + "");
                                }
//                                if (RoundInfoFragment.txtTimer != null) {
//                                    RoundInfoFragment.txtTimer.setText("01:00");
//                                }

                            }
                        });


                    }
                });
            }
        };
    }

    BroadcastReceiver bluetoothBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        checkBluetoothState();
                        if (onBlueToothStatusChanged != null)
                            onBlueToothStatusChanged.OnActionDone(false);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        checkBluetoothState();
                        if (onBlueToothStatusChanged != null)
                            onBlueToothStatusChanged.OnActionDone(true);
                        break;
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();


        startTimerPhoenix();
        startTimerLocationListener();
        StaticValue.OPEN_GPS = false;
        try {
            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(bluetoothBroadcastReceiver, filter);
            /**/
            if (checkBluetoothState()) {
                initBeaconScanner();
            }

        } catch (Exception e) {

        }
        /**/


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String lang = UtilityDriver.getStringShared(UtilityDriver.LANGUAGE).equals("ar") ? "ar" : "en";
        setLocale(new Locale(lang));
    }

    private void setLocale(Locale locale) {
        Configuration conf = getBaseContext().getResources().getConfiguration();
        conf.locale = locale;
        getBaseContext().getResources().updateConfiguration(conf, getResources().getDisplayMetrics());
        Configuration systemConf = Resources.getSystem().getConfiguration();
        systemConf.locale = locale;
        Resources.getSystem().updateConfiguration(systemConf, Resources.getSystem().getDisplayMetrics());
        Locale.setDefault(locale);
    }

    @Override
    protected void onPause() {
        try {
            StaticValue.OPEN_GPS = true;
            System.err.println(StaticValue.latitudeMain + " " + StaticValue.longitudeMain + "       5555555555");
            super.onPause();
            unregisterReceiver(bluetoothBroadcastReceiver);
        } catch (Exception e) {

        }
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//        isReceiverRegistered = false;


//        if (RoundInfoFragment.ROUND_ID_SOCKET != 0) {
//            addNewNotification();
//        }

//        stopTimerPhoenix();
//        try {
//            phoenixPlug.closeConnection();
////            locationListener.onStop();
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        }
//        mActivity = null;
    }

//    PopupWindow popupWindow;

//    private void callPopup() {
//
//        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
//                .getSystemService(LAYOUT_INFLATER_SERVICE);
//
//        View popupView = layoutInflater.inflate(R.layout.dialog_message, null);
//
//        popupWindow = new PopupWindow(popupView,
//                RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.MATCH_PARENT,
//                true);
//
//        popupWindow.setTouchable(true);
//        popupWindow.setFocusable(true);
//
//        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
////        EditText Name = (EditText) popupView.findViewById(R.id.edtimageName);
//
//        ((Button) popupView.findViewById(R.id.btnOk))
//                .setOnClickListener(new View.OnClickListener() {
//
//                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
//                    public void onClick(View arg0) {
////                        Toast.makeText(getApplicationContext(),
////                                Name.getText().toString(),Toast.LENGTH_LONG).show();
//
//                        popupWindow.dismiss();
//
//                    }
//
//                });
////
//        ((Button) popupView.findViewById(R.id.btnCancel))
//                .setOnClickListener(new View.OnClickListener() {
//
//                    public void onClick(View arg0) {
//
//                        popupWindow.dismiss();
//                    }
//                });
//
//    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    private GoogleApiClient createGoogleApiClient() {
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (requestCode == REQ_PERMISSION) {
            if (grantResults.length > 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                    StaticValue.locationFromMap = location;
                    StaticValue.latitudeMain = location.getLatitude();
                    StaticValue.longitudeMain = location.getLongitude();
//                UtilityDriver.showMessage(StaticValue.mActivity,StaticValue.latitudeMain + " " + StaticValue.longitudeMain + "       22222222222");
//                    Toast.makeText(getApplicationContext(),latitude+"  "+longitude,Toast.LENGTH_LONG).show();
            }
        });

//        } else {
//            //Show snackbar
//        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    StaticValue.locationFromMap = location;
                    Log.v("speed__", "" + location.getSpeed() * 3.6);
//                    yousef
                    String  tracklink = UtilityDriver.getStringShared(UtilityDriver.ENABLE_TRACK_LINK);
                    String round_ST = UtilityDriver.getStringShared(UtilityDriver.ROUND_ST);
                    Log.v("round_ST", "" + round_ST);

                    if (tracklink.equals("true")&& round_ST.equals("true")) {
                        JSONObject jsonBody = new JSONObject();
                        try {
//9531008228
                            jsonBody.put("veh", "20-19394");
                            jsonBody.put("ser", "1");
                            jsonBody.put("sid", "3a5ddbd597f94aaab0742777d774b3c4");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final String requestBody = jsonBody.toString();
                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST,
                                " http://tamapps.trak-link.net/checker/Details2", null, new Response.Listener<JSONObject>(){
                            @Override    public void onResponse(JSONObject response) {

                                try {
                                    JSONArray jsonArray = new JSONArray(response.getString("Result"));
                                    JSONObject object = jsonArray.getJSONObject(0);
                                    StaticValue.latitudeMain = Double.parseDouble(object.getString("y"));
                                     StaticValue.longitudeMain = Double.parseDouble(object.getString("x"));
                                     Log.v("Response1111111111111111Details2",object.getString("x"));
//                                    UtilityDriver.setStringShared(UtilityDriver.ROUND_ST, "true");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override    public void onErrorResponse(VolleyError error) {
                                VolleyLog.e("Error: ", error.getMessage());
//                Log.d("VOLLEY", Objects.requireNonNull(error.getMessage()));

                            }
                        }){
                            @Override    public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json");
                                return headers;
                            }
                            @Override    public byte[] getBody() {
                                try {
                                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                            requestBody, "utf-8");
                                    return null;
                                }
                            }
                        };
                        Application.getInstanceVolly().addToRequestQueue(jsonObjectRequest1, "http://tamapps.trak-link.net/checker/Details2");
//                        String TRACKLINK_ID = UtilityDriver.getStringShared(UtilityDriver.TRACKLINK_ID);
////
//                        String url1 = "https://tam.trak-link.net/wialon/ajax.html?svc=token/login&params={\"token\":\"006e88fb7d566f322840883846cf56caD8CAA24F9DDD9BF5EAF96B56AAD325102805F610\n" +
//                                "\n\"}";
//                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                                (Request.Method.POST, url1, null, new Response.Listener<JSONObject>() {
//
//                                    @Override
//                                    public void onResponse(JSONObject response) {
//                                        try {
//                                            String url = "https://tam.trak-link.net/wialon/ajax.html?svc=core/search_item&params={\"id\":" + TRACKLINK_ID + "," +
//                                                    "\"flags\":1025}&sid="+UtilityDriver.getStringShared(UtilityDriver.SID_TRACK_LINK);
//                                            Log.v("round_ST", "" + url);
////
//                                            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
//                                                    (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//
//                                                        @Override
//                                                        public void onResponse(JSONObject response) {
//                                                            try {
//                                                                Log.v("ssssssssssssss", String.valueOf(response));
//                                                                JSONObject obj = new JSONObject(response.getString("item"));
//                                                                JSONObject object = obj.getJSONObject("pos");
//                                                                StaticValue.latitudeMain = Double.parseDouble(object.getString("y"));
//                                                                StaticValue.longitudeMain = Double.parseDouble(object.getString("x"));
//                                                                Log.v("ssssssssssssss852",object.getString("y"));
//                                                            } catch (JSONException e) {
//                                                                e.printStackTrace();
//                                                            }
//                                                        }
//                                                    }, new Response.ErrorListener() {
//
//                                                        @Override
//                                                        public void onErrorResponse(VolleyError error) {
//
//                                                            Log.d("llssslllllldddddddlllllllll", error.getMessage());
//                                                        }
//                                                    });
//
//                                            Application.getInstanceVolly().addToRequestQueue(jsonObjectRequest1, url);
//
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                            Log.d("response1111111", e.getMessage());
//                                        }
//
//                                    }
//                                }, new Response.ErrorListener() {
//
//                                    @Override
//                                    public void onErrorResponse(VolleyError error) {
//                                        Log.d("response1111111", error.getMessage());
//                                    }
//                                });
//                        Application.getInstanceVolly().addToRequestQueue(jsonObjectRequest, url1);

                    }
                    else
                    {
                        StaticValue.latitudeMain = location.getLatitude();
                        StaticValue.longitudeMain = location.getLongitude();
                    }

//                    UtilityDriver.showMessage(mActivity,MainActivity.latitudeMain + " " + MainActivity.longitudeMain + "       22222222222 "+location.getProvider());
//                    Toast.makeText(getApplicationContext(),latitude+"  "+longitude,Toast.LENGTH_LONG).show();
                }
            });
        } else {
//            ActivityCompat.requestPermissions(this, new String[]{
//                            android.Manifest.permission.ACCESS_FINE_LOCATION,
//                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                    MY_LOCATION_REQUEST_CODE);
        }
//            mMap.setMyLocationEnabled(true);


    }

    public static Driver driver;


    //    private BubblesManager bubblesManager;
//    private void initializeBubbleManager() {
//        bubblesManager = new BubblesManager.Builder(this)
//                .setTrashLayout(R.layout.notification_trash_layout)
//                .build();
//        bubblesManager.initialize();
//    }
//    private void addNewNotification() {
//        BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(MainActivity.this)
//                .inflate(R.layout.notification_layout, null);
////        MapView mMapView = (MapView) bubbleView.findViewById(R.id.mapView);
////        ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getView().getMapAsync;
//
//        MapView mMapView = (MapView) bubbleView.findViewById(R.id.mapView);
//
//        mMapView.onCreate(savedInstanceState);
//        mMapView.onResume(); // needed to get the map to display immediately
//        try {
//            MapsInitializer.initialize(mActivity);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mMapView.getMapAsync(this);
//
//        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
//            @Override
//            public void onBubbleRemoved(BubbleLayout bubble) {
//                Toast.makeText(getApplicationContext(), "Bubble removed !",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//        // this methoid call when cuser click on the notification layout( bubble layout)
//        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {
//
//            @Override
//            public void onBubbleClick(BubbleLayout bubble) {
//                Toast.makeText(getApplicationContext(), "Clicked !",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // add bubble view into bubble manager
////        bubblesManager.addBubble(bubbleView, 60, 20);
//    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFireBase();
        try {

//            checkLocationPermission(true);

//            initFileLogger();

//            log("test");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                registerReceiver(new NetworkChangeReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                registerReceiver(new NetworkChangeReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            }

        } catch (Exception e) {

        }
        /**/

        /**/
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_main);
        StaticValue.mActivity = this;
//        new SpeedDialog(StaticValue.mActivity, UtilityDriver.getIntShared(UtilityDriver.SPEED), 10);

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        DisplayMetrics metrics = new DisplayMetrics();
//        StaticValue.mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        float yInches = metrics.heightPixels / metrics.ydpi;
//        float xInches = metrics.widthPixels / metrics.xdpi;
//        StaticValue.diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
//        if (StaticValue.diagonalInches >= 6.5) {
//            // 6.5inch device or bigger
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }


//        this.savedInstanceState = savedInstanceState;
//        valueSend = true;
//        DAO.deleteAll(Application.database, DAO.LonLat_TBL);
//        DAO.deleteAll(Application.database, DAO.CheckInOut_TBL);
        StaticValue.listNearby = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
            }
        } else {
            Intent intent = new Intent(this, Service.class);
            startService(intent);
        }


        if (StaticValue.mGoogleApiClient == null) {
            StaticValue.mGoogleApiClient = createGoogleApiClient();
        }
        if (!StaticValue.mGoogleApiClient.isConnected()) {
            StaticValue.mGoogleApiClient.connect();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();


        StaticValue.progressDialog = ProgressDialog.show(StaticValue.mActivity, "",
                getString(R.string.waiting), true);
        StaticValue.progressDialog.dismiss();
        StaticValue.dbHandler = new DBHandler(this);
        StaticValue.dbHandler.addShop();
        root_view = findViewById(R.id.root_view);
        if (UtilityDriver.getBooleanShared(UtilityDriver.LOGIN)) {
            MainActivity.showFragmentRound();
        } else {
            MainActivity.showFragmentLogin();
        }
//        mNavigationView.setNavigationItemSelectedListener(this);
//        mNavigationView.setItemIconTintList(null);
//        StaticValue.mainActivity = MainActivity.this;

        if (driver == null) {
            driver = new Driver();
        }
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        checkGPS(false, true);
        android.location.LocationListener locationListener = new android.location.LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
//                Toast.makeText(getApplicationContext(),location+"",Toast.LENGTH_LONG).show();
//                MainActivity.latitudeMain = location.getLatitude();
//                MainActivity.longitudeMain = location.getLongitude();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                checkGPS();
            }

            public void onProviderEnabled(String provider) {
                checkGPS();
            }

            public void onProviderDisabled(String provider) {
                checkGPS();
            }
        };

        if (!checkLocationPermission(false)) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }


    public static void TIMER_START() {
        StaticValue.runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                StaticValue.handler.postDelayed(this, 1000);

                StaticValue.milliSeconds = StaticValue.milliSeconds + 1000;

                int hours = (int) ((StaticValue.milliSeconds / (1000 * 60 * 60)) % 24);
                int minutes = (int) ((StaticValue.milliSeconds / (1000 * 60)) % 60);
                int seconds = (int) (StaticValue.milliSeconds / 1000) % 60;
                StaticValue.TIMER_TEXT = String.format("%02d", hours)
                        + ":" + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds);
                if (RoundInfoFragment.labTimer != null) {
                    RoundInfoFragment.labTimer.setText(StaticValue.TIMER_TEXT);
                }
            }
        };
        StaticValue.handler.postDelayed(StaticValue.runnable, 0);
    }


//        StaticValue.runnable = new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//            @Override
//            public void run() {
//                StaticValue.handler.postDelayed(this, 1000);
//
//                StaticValue.milliSeconds = StaticValue.milliSeconds + 1000;
//
//                int hours = (int) ((StaticValue.milliSeconds / (1000 * 60 * 60)) % 24);
//                int minutes = (int) ((StaticValue.milliSeconds / (1000 * 60)) % 60);
//                int seconds = (int) (StaticValue.milliSeconds / 1000) % 60;
//                StaticValue.TIMER_TEXT = String.format("%02d", hours)
//                        + ":" + String.format("%02d", minutes)
//                        + ":" + String.format("%02d", seconds);
//                if (RoundInfoFragment.labTimer != null) {
//                    RoundInfoFragment.labTimer.setText(StaticValue.TIMER_TEXT);
//                }
//            }
//        };
//        StaticValue.handler.postDelayed(StaticValue.runnable, 0);


//}


    @Override
    protected void onDestroy() {
        StaticValue.handler.removeCallbacks(StaticValue.runnable);
        super.onDestroy();
    }

    public boolean isLocationServicesAvailable() {
        int locationMode = 0;
        String locationProviders;
        boolean isAvailable = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(MainActivity.this.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            isAvailable = (locationMode != Settings.Secure.LOCATION_MODE_OFF);
        } else {
            locationProviders = Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            isAvailable = !TextUtils.isEmpty(locationProviders);
        }

//        boolean coarsePermissionCheck = (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
//        boolean finePermissionCheck = (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

        return isAvailable;
    }


    public boolean checkGPS() {
        return checkGPS(false, true);
    }


    public boolean checkGPS(boolean withClose, boolean withMessage) {
        if (!checkLocationPermission(true)) {
            return false;
        }
        if (isLocationServicesAvailable()) {
            hideNoLocationMessage();
            return true;
        } else {
            if (withMessage)
                noLocationMessage(withClose);

            return false;
        }
    }


//    public boolean checkGPSAndNetWorkLocation(boolean withClose) {
//        if (isLocationServiceEnabled()) {
//            noLocationMessage(withClose);
//            return false;
//        }else {
//            return true;
//        }
//    }

    public boolean isLocationServiceEnabled() {
//        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


//        if (!checkLocationPermission(false)){
//            return false;
//        }

//        try {
//            assert manager != null;
//            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
//                return true;
//        } catch (Exception e) {
//        }
//
//
//        try {
//            if (!UtilityDriver.isEmptyString(Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED)))
//                return true;
//        } catch (Exception e) {
//        }

//        try {
//            String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//            if (locationProviders.contains(LocationManager.GPS_PROVIDER) && locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
//                return true;
//            }
//        } catch (Exception e) {
//        }


//        try {
//            if (UtilityDriver.isEmptyString(manager.getBestProvider(new Criteria(), true)) && !LocationManager.PASSIVE_PROVIDER.equals(manager.getBestProvider(new Criteria(), true)))
//                return true;
//        } catch (Exception e) {
//        }


        return false;
    }

    AlertDialog no_location_message = null;

    public void noLocationMessage(boolean withClose) {
        //Ask the user to enable GPS
        if (no_location_message == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.location_manager);
//            builder.setMessage(R.string.would_you_like_to_enable_gps);
            builder.setPositiveButton(getString(R.string.turn_on_gps), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Launch settings, allowing user to make a change
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);
                }
            });
            if (withClose)
                builder.setNegativeButton(R.string.no_value, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //No location service, no Activity

                        finish();
                    }
                });
            builder.setCancelable(false);
            no_location_message = builder.create();
        }

        if (!no_location_message.isShowing()) {
            no_location_message.show();
        }
    }

    public void hideNoLocationMessage() {
        if (no_location_message != null)
            if (no_location_message.isShowing())
                no_location_message.dismiss();
    }


//    private ArrayList<NotificationBean> notificationBeanList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void setListAdapter(ArrayList<NotificationBean> notificationBeanList) {
//        notificationBeanList.clear();
//        notificationBeanList.addAll(notificationBeanList);
    }

//    public ArrayList<NotificationBean> getNotificationsList() {
//        return notificationBeanList;
//    }


    public static void showFragmentRoundInfo(Bundle bundle) {
        BaseFragment roundFragment = new RoundInfoFragment();
        roundFragment.setArguments(bundle);
        replaceFragmentGeneric(roundFragment);
    }


    public static void showFragmentRound() {
        BaseFragment roundFragment = RoundFragment.newInstance();
        replaceFragmentGeneric(roundFragment);
    }

//    public static void showFragmentRound(Bundle bundle) {
//
//        BaseFragment roundFragment = RoundFragment.newInstance();
//        roundFragment.setArguments(bundle);
//        replaceFragmentGeneric(roundFragment);
//
//    }

    public static void showFragmentMap(Bundle bundle) {

        BaseFragment mapFragment = new MapFragment();
        mapFragment.setArguments(bundle);
        replaceFragmentGeneric(mapFragment);

    }

    public static void showFragmentLogin() {
        UtilityDriver.setBooleanShared(UtilityDriver.LOGIN, false);
        BaseFragment mainActivityFragment = new LoginFragment();
        replaceFragmentGeneric(mainActivityFragment);
    }

    public static void showFragmentChangeLocation(int studentID) {
        BaseFragment changeLocationFragment = new ChangeLocationFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("studentID", studentID);
        changeLocationFragment.setArguments(bundle);
        replaceFragmentGeneric(changeLocationFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    static void replaceFragmentGeneric(BaseFragment baseFragment) {

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, baseFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected()");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended()");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed()");
    }


//    @Override
//    public void sendSocketMessage(String response, int usedId) {
//
//    }
//
//    @Override
//    public void receivedSocketMessage(String response, int usedId) {
//
//    }
//
//    @Override
//    public void newLocation(double lat, double lng, int usedId) {
//
//    }
//
//    @Override
//    public void errorConnect(String response) {
//        Log.d(response, response);
//    }
//
//    @Override
//    public void ignoreConnect(String response) {
//
//    }
//
//    @Override
//    public void closeConnect(String response) {
//
//    }
//
//    @Override
//    public void socketConnected() {
//
//    }
//
//    @Override
//    public void onReadyToPushMessage(int referrer) {
//        if (referrer == SEND_LOCATION) {
//            ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
//            node.put("school_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.SCHOOL_ID)));
//            node.put("round_id", RoundInfoFragment.ROUND_ID_SOCKET);
//
//
//            if (false) {
//                double latt = 0, longt = 0;
//                if (RoundFragment.count < RoundFragment.arrayTestPhonix.length) {
//                    latt = Double.parseDouble(RoundFragment.arrayTestPhonix[RoundFragment.count].split(",")[0]);
//                    longt = Double.parseDouble(RoundFragment.arrayTestPhonix[RoundFragment.count].split(",")[1]);
//
//                    RoundFragment.count++;
//                } else {
//                    RoundFragment.count = 0;
//                }
//                node.put("lat", latt);
//                node.put("long", longt);
//            } else {
//                node.put("lat", MainActivity.latitude);
//                node.put("long", MainActivity.longitude);
//            }
//            node.put("bus_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.BUS_ID)));
//            node.put("at", UtilityDriver.getDateFormat("mm/DD/yyyy hh:mm:ss"));
////            PhoenixPlug.getInstance().pushPhoenix( "new:bus_location",node);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    @Override
//    public void openConnect(String response) {
////        if (phoenixPlug == null) {
////            phoenixPlug = new PhoenixPlug();
////                phoenixPlug.init(MainActivity.this);
////        }
//        try {
//            PhoenixPlug.getInstance().joinBus(MainActivity.this,Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.BUS_ID)),STARTING);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //);
//    }


//    @Override
//    public void onRestCallBack(String response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
//
//
//    }
//
//    @Override
//    public void onRestCallBack(JSONObject response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
//        if (EnumNameApi.BUS_LOCATION == nameApiEnum) {
////            valueSend = true;
////            System.err.println(response);
//            JSONObject jsonObject = new JSONObject(volleyBean.getMap());
//
//            if (response.toString().contains("\"status\":\"ok\"")) {
//                System.err.println(response + "......TESTROUND....." + jsonObject);
//                StaticValue.BUS_LOCATION_TYPE = false;
//            } else {
//                LonLat lonLat = new LonLat("", "", new JSONObject(volleyBean.getMap()).toString()/*,StaticValue.getCurrentSpeed()*/);
//                DAO.addLonLat(Application.database, lonLat);
//                StaticValue.BUS_LOCATION_TYPE = false;
//            }
//
//        }
//        else if (EnumNameApi.GEOFENCE == nameApiEnum) {
//        } else if (EnumNameApi.OFFLINE_LOCATION == nameApiEnum) {
//            String status = null;
//            try {
//                JSONObject jsonObject = new JSONObject(volleyBean.getMap());
//
//                status = response.getString("status");
//                if (status.contains("ok")) {
//                    System.err.println(response + ".OFFLINE.....TESTROUND....." + jsonObject);
//                    System.err.println(new JSONObject(volleyBean.getMap()) + "   SUCCESS_LOCATION_MESSI");
//                    DAO.deleteAll(Application.database, DAO.LonLat_TBL);
//                }
//                StaticValue.BUS_LOCATION_TYPE = false;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        } else if (EnumNameApi.OFFLINE_STATUS == nameApiEnum) {
//            String status = null;
//            try {
//                status = response.getString("status");
//                if (status.contains("ok")) {
//                    DAO.deleteAll(Application.database, DAO.CheckInOut_TBL);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }

//    @Override
//    public void onRestCallBack(VolleyError volleyError, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
//        if (volleyError != null)
//            if (volleyError.getMessage().toString().contains("java.net.UnknownHost")) {
//                UtilityDriver.showMessageDialog(StaticValue.mActivity, StaticValue.mActivity.getString(R.string.internet_connection), StaticValue.mActivity.getString(R.string.missing_internet_error));
//                return;
//            }
//        if (EnumNameApi.BUS_LOCATION == nameApiEnum) {
//            LonLat lonLat = new LonLat("", "", new JSONObject(volleyBean.getMap()).toString()/*,StaticValue.getCurrentSpeed()*/);
//            DAO.addLonLat(lonLat);
//            StaticValue.BUS_LOCATION_TYPE = false;
//            System.err.println(volleyBean.getUrl_path() + " @@@ " + "BUS_LOCATION_ERROR" + volleyError.getMessage() + " " + new JSONObject(volleyBean.getMap()) + " " + new JSONObject(volleyBean.getMapHeader()));
////            valueSend = true;
////            if (volleyError !=null)
////            System.err.println(volleyError.getMessage());
//        }
//            else if (EnumNameApi.GEOFENCE == nameApiEnum) {
//                UtilityDriver.showMessage(StaticValue.mActivity, "GEOFENCE error " + volleyError.getMessage());
//            } else if (EnumNameApi.OFFLINE_LOCATION == nameApiEnum) {
//                System.err.println(new JSONObject(volleyBean.getMap()) + "   FAIL_LOCATION_MESSI");
//            }
//    }

//    @Override
//    public void onRestCallBack(NetworkResponse networkResponse, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
//
//    }
    //////////////////////////////////
    ////////////////////////////////////

    public void setFragment(final Fragment fragment, Bundle bundle, boolean clearStack) {
        /**/
        hideSoftKeyboard();
        /**/
        if (fragment == null) {
            return;
        }
        /**/
        if (clearStack) {
            getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        /**/
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        /**/
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .add(R.id.fragment_container, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
//        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//
//            @Override
//            public void onBackStackChanged() {
//                if (fragment.getView()!=null){
//                    fragment.getView().setFocusableInTouchMode(true);
//                    fragment.getView().requestFocus();
//                }
//            }
//        });

        getSupportFragmentManager().executePendingTransactions();
        /**/

    }

    public void removeCurrentFragment() {
        onBackPressed();
    }


    @Override
    public void onBackPressed() {


        if (onBackButtonPressed != null && getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof RoundInfoFragment_NEW) {
            onBackButtonPressed.OnActionDone("");
            return;
        }


        if (getSupportFragmentManager().getBackStackEntryCount() == 0 || (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof RoundFragment)) {
            finish();
            return;

        } else {
            handleOnResume();
            super.onBackPressed();
            if (/*getSupportFragmentManager().getBackStackEntryCount() == 1 &&*/ (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof RoundFragment)) {
//                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                getSupportFragmentManager().executePendingTransactions();
//                showFragmentRound();
                try {
//                    ((RoundFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).mustEndedRoundm=null;
                    ((RoundFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).refreshPages();//refresh round list if the user on round listscreen
                } catch (Exception e) {

                }

            }
//            else if ((getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof RoundFragment)) {
//                ((RoundFragment)(getSupportFragmentManager().findFragmentById(R.id.fragment_container))
////                drawer.setSelectionAtPosition(2, false);
//                setFragment(RoundFragment.newInstance(), null, true);
//            }

        }
    }

    private void handleOnResume() {
        try {
            getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getBackStackEntryCount() - 1).onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //    private BroadcastReceiver beaconReceiver;
    private BroadcastReceiver absentReceiver;

//    public void startBeaconService() {
//        Intent intentBeaconService = new Intent(this, BeaconService.class);
//        this.startService(intentBeaconService);
//    }

//    public void stopBeaconService() {
//        stopService(new Intent(this, BeaconService.class));
//    }

//    public void unRegisterBeaconReceiver() {
//            if (beaconReceiver != null) {
//                this.unregisterReceiver(beaconReceiver);
//                beaconReceiver = null;
//            }
//
//    }

//    public void registerBeaconReceiver(BroadcastReceiver beaconReceiver) {
//        if (PathUrl.BEACON_Enabled) {
//            unRegisterBeaconReceiver();
//            this.beaconReceiver = beaconReceiver;
//            this.registerReceiver(beaconReceiver, new IntentFilter(BeaconService.BEACON_SERVICE_SIGNAL));
//        }
//    }


    public void unRegisterAbsentReceiver() {
        if (absentReceiver != null) {
            this.unregisterReceiver(absentReceiver);
            absentReceiver = null;
        }
    }

    public void registerAbsentReceiver(BroadcastReceiver absentReceiver) {
        unRegisterAbsentReceiver();
        this.absentReceiver = absentReceiver;
        this.registerReceiver(absentReceiver, new IntentFilter(MyFirebaseMessagingService_new.FIREBASE_BROADCAST));

    }


//    public void startAndSendDataToService(Object data) {
//        try {
////            if (PathUrl.BEACON_Enabled) {
//                Intent intent = new Intent(this, BeaconService.class);
//                if (data instanceof String) {
//                    intent.putExtra((String) data, (String) data);
//                } else {
//                    intent.putExtra(BeaconService.ADD_THIS_ROUNDE, (Parcelable) data);
//                }
////                stopService(intent);
//                startService(intent);
////            }
//        } catch (Exception e) {
//
//        }
//
//
//    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }


    private BeaconManager mBeaconManager;
    public static int beaconTicker = 0;
    public static int scanEverySec = 30;
    ArrayList<StudentBean> mustScanStudents = new ArrayList<>(); // all
    HashSet<StudentBean> foundedStudents = new HashSet<>();
//    HashSet<String> notFoundedBeacons = new HashSet<>();

    public void initBeaconScanner() throws Exception {
        try {
            /**/
//        clearBeaconScanner();
            /**/
//        if (mBeaconManager == null) {
            mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
//        }
            /**/
            mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
//        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-1=5900,i:2-2,i:3-4,p:5-5"));
//        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
//        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
//        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
//        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
//        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.URI_BEACON_LAYOUT));
            long exitTimeOut = TimeUnit.SECONDS.toMillis(45);
//        int maxScanTime = Math.round(TimeUnit.SECONDS.toMillis(45));
            ;
            mBeaconManager.setBackgroundBetweenScanPeriod(TimeUnit.SECONDS.toMillis(5));
            mBeaconManager.setBackgroundMode(true);
//        mBeaconManager.setMaxTrackingAge(maxScanTime);
            mBeaconManager.setScannerInSameProcess(false);
            mBeaconManager.setRegionExitPeriod(exitTimeOut);
            mBeaconManager.applySettings();
            mBeaconManager.bind(MainActivity.this);

//        public static final long DEFAULT_FOREGROUND_SCAN_PERIOD = 1100L;
//        public static final long DEFAULT_FOREGROUND_BETWEEN_SCAN_PERIOD = 0L;
//        public static final long DEFAULT_BACKGROUND_SCAN_PERIOD = 10000L;
//        public static final long DEFAULT_BACKGROUND_BETWEEN_SCAN_PERIOD = 300000L;
//        public static final long DEFAULT_EXIT_PERIOD = 10000L;

//        mBeaconManager.setBackgroundScanPeriod(300000L);// 30 secs
//        mBeaconManager.setBackgroundBetweenScanPeriod(300000L);
//        mBeaconManager.setForegroundScanPeriod(300000L);
//        mBeaconManager.setForegroundBetweenScanPeriod(300000L);
//        mBeaconManager.updateScanPeriods();


        } catch (Exception e) {

        }

    }


    public void addMacAddressToScanner() {
        try {
//            clearBeaconScanner();
//            initBeaconScanner();
//            mustScanStudents.addAll(findThis);
        } catch (Exception e) {

        }


//        String macb1 = "AC:23:3F:24:92:C0";
//        String macb2 = "AC:23:3F:24:92:C6";
//        String macb3 = "AC:11:3F:24:92:C6";
    }


    public void clearScannerLisiners() {
        foundedStudents.clear();
        mustScanStudents.clear();
        onScannerFind_out = null;
        onScannerFind_in = null;
    }

    public void clearBeaconScanner() {
        try {
            foundedStudents.clear();
            mustScanStudents.clear();
            beaconTicker = 0;

            if (mBeaconManager != null) {
//                mBeaconManager.getBeaconParsers().clear();
                for (Region region : mBeaconManager.getMonitoredRegions()) {
                    mBeaconManager.stopRangingBeaconsInRegion(region);
                }

                try {
                    mBeaconManager.getMonitoredRegions().clear();
                    mBeaconManager.getMonitoringNotifiers().clear();
                } catch (Exception e) {

                }

                mBeaconManager.removeAllRangeNotifiers();
                mBeaconManager.removeAllMonitorNotifiers();
                mBeaconManager.updateScanPeriods();
                mBeaconManager.applySettings();
//                mBeaconManager.unbind(MainActivity.this);
//                mBeaconManager = null;

            }
        } catch (Exception e) {

        }


    }

    @Override
    public void onBeaconServiceConnect() {
        try {
            initStudentListToNotifier();
//            mBeaconManager.removeAllRangeNotifiers();
//            mBeaconManager.removeAllMonitorNotifiers();
//            Region region = new Region("allbeacons", null, null, null);
//            Region region1 = new Region(macb1, macb1);
//            Region region2 = new Region(macb2, macb2);
//            Region region3 = new Region(macb3, macb3);


//            mBeaconManager.addRangeNotifier(new RangeNotifier() {
//                @Override
//                public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//                    beaconTicker++;
//                    Log.i("didRangeBeaconsInRegion", " region.getBluetoothAddress() " + region.getBluetoothAddress());
//                    Log.i("didRangeBeaconsInRegion", " beacons.size() " + beacons.size());
//                    for (Beacon b : beacons) {
//                        StudentBean foundedS = findStudentByMac(b.getBluetoothAddress());
//                        if (foundedS != null)
//                            foundedStudents.add(foundedS);
//                    }
//                /**/
//                    if (beaconTicker > scanEverySec) {
//                        beaconTicker = 0;
//                        onScannerDone(foundedStudents);
//                    }
//                }
//            });
//
//
//            for (int i = 0; i < mustScanStudents.size(); i++) {
//                if (mustScanStudents.get(i).getMacAdress() != null)
//                    mBeaconManager.startRangingBeaconsInRegion(new Region(mustScanStudents.get(i).getMacAdress(), mustScanStudents.get(i).getMacAdress()));
//            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void initStudentListToNotifier() {
        try {
            if (mustScanStudents == null || mustScanStudents.size() < 0)
                return;

            mBeaconManager.addMonitorNotifier(new MonitorNotifier() {
                @Override
                public void didEnterRegion(final Region region) {
                    try {
                        new Handler(getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (onScannerFind_in != null)
                                    onScannerFind_in.OnActionDone(findStudentByMac(region.getBluetoothAddress()));
                                Log.v("BeaconBeacon Enter", region.getBluetoothAddress());
                            }
                        }, 500);


                    } catch (Exception e) {

                    }
                }

                @Override
                public void didExitRegion(final Region region) {
                    try {
                        new Handler(getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (onScannerFind_out != null)
                                    onScannerFind_out.OnActionDone(findStudentByMac(region.getBluetoothAddress()));
                                Log.v("BeaconBeacon Exit", region.getBluetoothAddress());
                            }

                        }, 500);


                    } catch (Exception e) {

                    }
                }

                @Override
                public void didDetermineStateForRegion(int in_out, Region region) {
                    try {
                        switch (in_out) {
                            case MonitorNotifier.OUTSIDE:
//                                Beacon OUTSIDE
//                                Log.v("BeaconBeacon  OUTSIDE", region.getBluetoothAddress());
                                break;
                            case MonitorNotifier.INSIDE:
//                                Beacon OUTSIDE INSIDE
//                                Log.v("BeaconBeacon  INSIDE", region.getBluetoothAddress());
                                break;
                        }


                    } catch (Exception e) {

                    }

                }
            });
            for (int i = 0; i < mustScanStudents.size(); i++) {
                if (mustScanStudents.get(i).getMacAdress() != null)
                    mBeaconManager.startMonitoringBeaconsInRegion(new Region(mustScanStudents.get(i).getMacAdress(), mustScanStudents.get(i).getMacAdress()));
            }
        } catch (Exception e) {

        }

    }

    public void addNotifiers(List<StudentBean> mustScanStudents) {
        try {


            clearBeaconScanner();

            if (mBeaconManager.isBound(this)) {
                this.mustScanStudents.addAll(mustScanStudents);
                initStudentListToNotifier();
            } else {
                initBeaconScanner();
            }


        } catch (Exception e) {

        }

    }

    public OnActionDoneListener<StudentBean> onScannerFind_out;
    public OnActionDoneListener<StudentBean> onScannerFind_in;


//    public void onScannerDone(final HashSet<StudentBean> founded) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                HashSet<StudentBean> notFounded = new HashSet<>();
//         /**/
//                notFounded.addAll(mustScanStudents);
//                notFounded.removeAll(founded);
//
//                if (onScannerFind_in!=null)
//                    onScannerFind_in.OnActionDone(founded);
//
//                if (onScannerFind_out!=null)
//                    onScannerFind_out.OnActionDone(notFounded);
//
//                foundedStudents.clear();
//            }
//        });
//
//
//    }

    public StudentBean findStudentByMac(String mac) {
        for (int i = 0; i < mustScanStudents.size(); i++) {
            if (mustScanStudents.get(i).getMacAdress() != null && mustScanStudents.get(i).getMacAdress().equals(mac)) {
                return mustScanStudents.get(i);
            }

        }
        return null;
    }


    public static boolean isBluetoothOn = false;
    private OnActionDoneListener<Boolean> onBlueToothStatusChanged = null;
    UtilDialogs.MessageYesNoDialog messageDialog;

    public boolean checkBluetoothState() {
        try {
            if (!UtilityDriver.getBooleanShared(UtilityDriver.ALLOW_DRIVER_TO_USE_BEACON))
                return false;

            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter.isEnabled()) {
                initBeaconScanner();
                isBluetoothOn = true;
            } else {
                isBluetoothOn = false;
            }
            /**/

        } catch (Exception e) {
            isBluetoothOn = false;
        }
        /**/
        if (isBluetoothOn) {
            if (messageDialog != null && messageDialog.isShowing()) {
                if (messageDialog != null)
                    messageDialog.dismiss();
                messageDialog = null;
            }

        } else {
            if (messageDialog == null && !isBluetoothDialogClosedBefore) {
                messageDialog = new UtilDialogs.MessageYesNoDialog().show(this)
                        .setDialogeTitle(R.string.you_should_turn_on_the_bluetooth)
                        .setImageWithColor(R.drawable.ic_bluetooth_disabled_black_48dp, 0)
                        .setYesButtonText(R.string.you_should_turn_on_the_bluetooth_button)
                        .setYesButtonClickListener(new OnActionDoneListener() {
                            @Override
                            public void OnActionDone(Object Action) {

                                enableBlueTooth();
                                if (messageDialog != null)
                                    messageDialog.dismiss();
//                                ComponentName cn = new ComponentName("com.android.settings",
//                                        "com.android.settings.bluetooth.BluetoothSettings");
//                                final Intent intent = new Intent(Intent.ACTION_MAIN, null);
//                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                                intent.setComponent(cn);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);

                            }
                        }).setCloseButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                            @Override
                            public void OnActionDone(UtilDialogs.MessageYesNoDialog action) {
                                if (messageDialog != null)
                                    messageDialog.dismiss();
                                messageDialog = null;
                            }
                        });

                isBluetoothDialogClosedBefore = true;
            }

        }


        return isBluetoothOn;
    }

    public void setOnBlueToothStatusChanged(OnActionDoneListener<Boolean> onBlueToothStatusChanged) {
        this.onBlueToothStatusChanged = onBlueToothStatusChanged;
    }

    public void enableBlueTooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
    }


    private static final int REQ_PERMISSION = 1233;

//    private void initFileLogger() {
//
////        FL.init(new FLConfig.Builder(this)
//////                .minLevel(FLConst.Level.D)
////                .logToFile(true)
////                .defaultTag("CONNECTION_FILE_LOGGER")   // customise default tag
////                .dir(new File(Environment.getExternalStorageDirectory(), "connection_file_logger"))
////                .retentionPolicy(FLConst.RetentionPolicy.FILE_COUNT)
////                .build());
//
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_PERMISSION);
//            return;
//        }
//
//
//    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

//            if (checkInternet(context)) {
//                log("Network Available");
//                //Network Available Do operations
//            } else {
//                log("No Network Available");
//
//            }

        }

        boolean checkInternet(Context context) {
            ServiceManager serviceManager = new ServiceManager(context);
            if (serviceManager.isNetworkAvailable()) {
                return true;
            } else {
                return false;
            }
        }

    }

    public class ServiceManager {

        Context context;

        public ServiceManager(Context base) {
            context = base;
        }

        public boolean isNetworkAvailable() {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }


//    public MainActivity log(String s) {
//        try {
//            FL.d("---------------");
//            FL.d(StaticValue.latitudeMain + "," + StaticValue.longitudeMain);
//            String strUri = "http://maps.google.com/maps?q=loc:" + StaticValue.latitudeMain + "," + StaticValue.longitudeMain + " (" + "Location" + ")";
//            FL.d(strUri);
//            FL.d(DateTools.Formats.TIMEONLY_FORMAT_12H_LOCAL.format(new Date()));
//
//            FL.d(s);
//
//            FL.d("---------------");
//
//        } catch (Exception e) {
//
//        }
//        return this;
//    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
//    public static boolean checkLocationPermission(Activity Activity,boolean showMessage) {
//        if (ContextCompat.checkSelfPermission(Activity,
//                ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Asking user if explanation is needed
//            if (ActivityCompat.shouldShowRequestPermissionRationale(Activity,
//                    ACCESS_FINE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//                //Prompt the user once explanation has been shown
//                ActivityCompat.requestPermissions(Activity,
//                        new String[]{ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(Activity,
//                        new String[]{ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }


    public boolean checkLocationPermission(boolean showd) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (showd) {
                ActivityCompat.requestPermissions(this, new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (showd) {
                ActivityCompat.requestPermissions(this, new String[]{
                                android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//           if (showd) {
//               ActivityCompat.requestPermissions(this, new String[]{
//                               android.Manifest.permission.ACCESS_FINE_LOCATION,
//                               android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                       MY_PERMISSIONS_REQUEST_LOCATION);
//           }
//            return false;
//        }
        return true;
    }
}
