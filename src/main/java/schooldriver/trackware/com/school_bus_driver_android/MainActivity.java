package schooldriver.trackware.com.school_bus_driver_android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
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
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
        OnMapReadyCallback, NfcAdapter.ReaderCallback {

    public String fireBaseToken = "null";
    private NfcAdapter nfcAdapter;
    private OnActionDoneListener<String> onNFCActionDone = null;

    public MainActivity setOnNFCActionDone(OnActionDoneListener<String> onNFCActionDone) {
        this.onNFCActionDone = onNFCActionDone;
        return this;
    }

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


    @Override
    public void onTagDiscovered(Tag tag) {
        try {


            if (onNFCActionDone == null)
                return;

            Ndef ndef = Ndef.get(tag);
            NdefMessage ndefMessage = ndef.getCachedNdefMessage();
            runOnUiThread(() -> {
                try {
                    if (ndefMessage != null && ndefMessage.getRecords().length > 0) {
                        byte[] payload = ndefMessage.getRecords()[0].getPayload();
                        final String conteudoTag = new String(payload);
                        if (onNFCActionDone == null)
                            return;


                        if (conteudoTag.length() != 0) {
                                onNFCActionDone.OnActionDone(conteudoTag);
//                            Toast.makeText(this, conteudoTag, Toast.LENGTH_SHORT)
//                                    .show();
                        } else {
//                            Toast.makeText(this, getString(R.string.sem_conteudo), Toast.LENGTH_SHORT)
//                                    .show();
                        }
                    } else {
//                        Toast.makeText(this, getString(R.string.erro_ler), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ignore) {
                }
            });
        } catch (Exception ignore) {
        }
    }


    public void habilitarModoLeitor() {
        try {


            int flags = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_B;
            Bundle extras = new Bundle();
            nfcAdapter.enableReaderMode(this, this, flags, extras);
        } catch (Exception ignore) {
        }
    }

    public void desabilitarModoLeitor() {
        try {


            nfcAdapter.disableReaderMode(this);
        } catch (Exception ignore) {
        }
    }

    private String startNFC() {
        try {

            nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        } catch (Exception ignore) {

        }
        return null;
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
                                            Log.v("oooooo", "ppppp");
                                            offlineModelStatus();
                                        } catch (OutOfMemoryError |
                                                 NullPointerException outOfMemoryError) {

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
                                            Log.v("round_ST___", round_ST);


                                            if (round_ST.equals("true")) {
                                                Log.v("round_ST___1111111111111111111111111111111111111", round_ST);
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


    @Override
    protected void onResume() {
        super.onResume();
        habilitarModoLeitor();

        try {
            startTimerPhoenix();
            startTimerLocationListener();
            StaticValue.OPEN_GPS = false;

        } catch (Exception e) {
            e.printStackTrace();

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
            desabilitarModoLeitor();
            StaticValue.OPEN_GPS = true;
            System.err.println(StaticValue.latitudeMain + " " + StaticValue.longitudeMain + "       5555555555");
            super.onPause();
        } catch (Exception e) {

        }
    }

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


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            }
        });
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
                    String tracklink = UtilityDriver.getStringShared(UtilityDriver.ENABLE_TRACK_LINK);
                    String round_ST = UtilityDriver.getStringShared(UtilityDriver.ROUND_ST);
                    Log.v("round_ST", "" + round_ST);

                    if (tracklink.equals("true") && round_ST.equals("true")) {
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
                                " http://tamapps.trak-link.net/checker/Details2", null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    JSONArray jsonArray = new JSONArray(response.getString("Result"));
                                    JSONObject object = jsonArray.getJSONObject(0);
                                    StaticValue.latitudeMain = Double.parseDouble(object.getString("y"));
                                    StaticValue.longitudeMain = Double.parseDouble(object.getString("x"));
                                    Log.v("Response1111111111111111Details2", object.getString("x"));
//                                    UtilityDriver.setStringShared(UtilityDriver.ROUND_ST, "true");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Error: ", error.getMessage());
//                Log.d("VOLLEY", Objects.requireNonNull(error.getMessage()));

                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json");
                                return headers;
                            }

                            @Override
                            public byte[] getBody() {
                                try {
                                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    Log.e("Unsupported Encoding while trying to get the bytes of %s using %s",
                                            requestBody.toString());
                                    return null;
                                }
                            }
                        };
                        Application.getInstanceVolly().addToRequestQueue(jsonObjectRequest1, "http://tamapps.trak-link.net/checker/Details2");
//
                    } else {
                        StaticValue.latitudeMain = location.getLatitude();
                        StaticValue.longitudeMain = location.getLongitude();
                    }
                }
            });
        }
    }

    public static Driver driver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startNFC();
        initFireBase();
        try {
            registerReceiver(new NetworkChangeReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        } catch (Exception e) {
            e.printStackTrace();

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
        StaticValue.listNearby = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                MainActivity.this.startActivityForResult(intent, 1234);
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
//      
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


    public static int beaconTicker = 0;
    ArrayList<StudentBean> mustScanStudents = new ArrayList<>(); // all
    HashSet<StudentBean> foundedStudents = new HashSet<>();


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
    UtilDialogs.MessageYesNoDialog messageDialog;


    private static final int REQ_PERMISSION = 1233;


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

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
