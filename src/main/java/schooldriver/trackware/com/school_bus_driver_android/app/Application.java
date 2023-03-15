package schooldriver.trackware.com.school_bus_driver_android.app;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import androidx.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import database.OpenHelper;
import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.DateTools;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.PathUrl;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;


/**
 * Created by muradtrac on 3/6/17.
 */

public class Application extends MultiDexApplication {

    private static Application mInstance;
    private SharedPreferences preferences;
    private final static String LOG_TAG = Application.class.getSimpleName();
    public static boolean shouldRefresh = true;
    public static SQLiteDatabase database;
//    public static PinpointManager pinpointManager;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.v("shouldRefresh", "App onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        String lang = UtilityDriver.getStringShared(UtilityDriver.LANGUAGE).equals("ar") ? "ar" : "en";
        setLocale(new Locale(lang));
        VolleyLog.DEBUG = true;
        VolleyLog.TAG = "VolleyLog_VolleyLog";
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
    public void onCreate() {
        super.onCreate();
        Log.v("shouldRefresh", "App onCreate");
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath(getString(R.string.font))
//                .setFontAttrId(R.attr.fontPath)
//                .build());


//        FirebaseCrash.report(new Exception("Test error"));

//        FirebaseTools.Analytics.createInstance(this);

//        IBeaconScanner.initialize(IBeaconScanner.newInitializer(this).build());
//        IBeaconScanner.getInstance().startMonitoring(new Beacon());
//        IBeaconScanner.getInstance().setCallback(new IBeaconScanner.Callback() {
//            @Override
//            public void didEnterBeacon(Beacon beacon) {
//
//            }
//
//            @Override
//            public void didExitBeacon(Beacon beacon) {
//
//            }
//
//            @Override
//            public void monitoringDidFail(Error error) {
//
//            }
//        });


//       new FontsContractCompat.
        mInstance = this;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(mInstance);
        if (UtilityDriver.isEmptyString(UtilityDriver.getStringShared(UtilityDriver.LANGUAGE))) {
            UtilityDriver.setStringShared(UtilityDriver.LANGUAGE, Locale.getDefault().getLanguage().toLowerCase());
        }
        UtilityDriver.setLocale(UtilityDriver.getStringShared(UtilityDriver.LANGUAGE), this);

        database = OpenHelper.getDatabase(getApplicationContext());
//        FL.init(new FLConfig.Builder(this)
//                .logToFile(true)
//                .dir(new File(Environment.getExternalStorageDirectory(), "driver_file_logger_demo"))
//                .retentionPolicy(FLConst.RetentionPolicy.FILE_COUNT)
//                .build());
//        FL.setEnabled(true);


//        AWSMobileClient.getInstance().initialize(this).execute();
//
//        PinpointConfiguration pinpointConfig = new PinpointConfiguration(
//                getApplicationContext(),
//                AWSMobileClient.getInstance().getCredentialsProvider(),
//                AWSMobileClient.getInstance().getConfiguration());
//
//        pinpointManager = new PinpointManager(pinpointConfig);
//
//        // Start a session with Pinpoint
//        pinpointManager.getSessionClient().startSession();
//
//        // Stop the session and submit the default app started event
//        pinpointManager.getSessionClient().stopSession();
//        pinpointManager.getAnalyticsClient().submitEvents();


//        new FirebaseTools.Analytics().addLog("key1","value1").addLog("key2","value2").send();
    }


    public static synchronized Application getInstance() {
        return mInstance;
    }

    public SharedPreferences getPreferences() {
        return this.preferences;
    }

    public static synchronized Application getInstanceVolly() {
        return getInstance();
    }

    private RequestQueue mRequestQueue;

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? LOG_TAG : tag);
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    //    @Override
//    public void () {
//        super.onDestroy();
//        beaconManager.unbind(this);
//    }
//    private static SentryClient your_dsn;


    private static String volleyErrorToString(VolleyError volleyError) {
        String volleyError_StackTrace;
        try {
            volleyError_StackTrace = Arrays.toString(volleyError.getStackTrace());
        } catch (Exception e) {
            volleyError_StackTrace = "volleyError";
        }
        return volleyError_StackTrace;
    }

    private static String exceptionToString(Exception exception) {
        String exception_StackTrace;
        try {
            exception_StackTrace = Arrays.toString(exception.getStackTrace());
        } catch (Exception e) {
            exception_StackTrace = "volleyError";
        }
        return exception_StackTrace;
    }

    private static String apiRequestToString(ApiRequest apiRequest) {
        String apiRequest_api_name;
        try {
            apiRequest_api_name = "api_url is :" + apiRequest.getUrl_path();
            apiRequest_api_name += "api_name is :" + apiRequest.getEnumNameApi();
        } catch (Exception e) {
            apiRequest_api_name = "nodata";
        }
        return apiRequest_api_name;
    }


    public static void logEvents(Object api_object, String class_and_method, Object... exceptions) {
//        try {
//
//            String api_name = "no data";
//            if (api_object != null) {
//                if (api_object instanceof String)
//                    api_name = api_object.toString();
//                if (api_object instanceof ApiRequest)
//                    api_name = apiRequestToString((ApiRequest) api_object);
//            }
//            class_and_method = class_and_method == null ? "null" : class_and_method;
//
//            try {
//
//                if (your_dsn == null) {
//                    your_dsn = Sentry.init(
//                            PathUrl.LOG_URL,
//                            new AndroidSentryClientFactory(mInstance)
//                    );
//                }
//
//                JsonObject jsonLog = new JsonObject();
//                jsonLog.addProperty("TIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a", Locale.US).format(new Date()));
//                jsonLog.addProperty("AUTH", UtilityDriver.getStringSharedForFireBase(UtilityDriver.AUTH));
//                jsonLog.addProperty("SCHOOL_NAME", UtilityDriver.getStringSharedForFireBase(UtilityDriver.SCHOOL_NAME));
//                jsonLog.addProperty("SCHOOL_ID", UtilityDriver.getStringSharedForFireBase(UtilityDriver.SCHOOL_ID));
//                jsonLog.addProperty("BUS_ID", UtilityDriver.getStringSharedForFireBase(UtilityDriver.BUS_ID));
//                jsonLog.addProperty("DRIVER_ID", UtilityDriver.getStringSharedForFireBase(UtilityDriver.DRIVER_ID));
//                jsonLog.addProperty("BUS_NUMBER", UtilityDriver.getStringSharedForFireBase(UtilityDriver.BUS_NUMBER));
//                jsonLog.addProperty("class_and_method", class_and_method);
//                jsonLog.addProperty("api_name", api_name);
//                jsonLog.add("Device Info", getDeviceinfo());
//
////                if (exceptions != null) {
////                    for (int i = 0; i < exceptions.length; i++) {
////                        if (exceptions[i] != null) {
////                            if (exceptions[i] instanceof String)
////                                jsonLog.addProperty("exception_data_" + i, (String) exceptions[i]);
////                            if (exceptions[i] instanceof VolleyError)
////                                jsonLog.addProperty("exception_data_" + i, volleyErrorToString((VolleyError) exceptions[i]));
////                            if (exceptions[i] instanceof Exception)
////                                jsonLog.addProperty("exception_data_" + i, exceptionToString((Exception) exceptions[i]));
////                            if (api_object instanceof ApiRequest)
////                                jsonLog.addProperty("exception_data_" + i, apiRequestToString((ApiRequest) exceptions[i]));
////                        }
////                    }
////                }
//
//
//                Sentry.capture(new Exception(jsonLog.toString()));
//            } catch (Exception e) {
//                e.printStackTrace();
//                Sentry.capture(e);
//
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


//    private static JsonObject getDeviceinfo() {
//        JsonObject jsonObject = new JsonObject();
//
//        try {
//            jsonObject.addProperty("os_version", System.getProperty("os.version") + " " + android.os.Build.VERSION.INCREMENTAL);
//            jsonObject.addProperty("os_api_level", android.os.Build.VERSION.RELEASE + " " + android.os.Build.VERSION.SDK_INT);
//            jsonObject.addProperty("device", android.os.Build.DEVICE);
//            jsonObject.addProperty("model", android.os.Build.MODEL);
//            jsonObject.addProperty("product", android.os.Build.MODEL);
//
//        } catch (Exception e) {
//            jsonObject.addProperty("exception ", e.getMessage());
//
//        }
//        return jsonObject;
//    }

    public static void addLocation(String roundId, double lng, double lat) {

        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
//        String root = "field-service-management-da2ca";
        String school_db = UtilityDriver.getStringShared(UtilityDriver.SCHOOL_DB);
        String fullRound ;
        if(PathUrl.DEV_PROD.equals("dev")){
             fullRound = school_db + "-stg-round-" + roundId;
        }
        else {
             fullRound = school_db + "-round-" + roundId;
        }
        String date = DateTools.Formats.DATEONLY_FORMAT_GMT.format(new Date());
        /**/
        Map location = new HashMap<>();
        location.put("0", lat);
        location.put("1", lng);
        /**/
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://trackware-sms-default-rtdb.firebaseio.com/").getReference();
        databaseReference.child(fullRound).child(date).child(timeStamp).setValue(location);
    }

    private static FirebaseDatabase firebaseDatabase;

    private static FirebaseDatabase getFirebase() {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
            FirebaseAuth.getInstance().signInAnonymously();
        }
        return firebaseDatabase;

    }



}

