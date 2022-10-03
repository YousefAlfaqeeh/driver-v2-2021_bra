package schooldriver.trackware.com.school_bus_driver_android.utilityDriver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import androidx.annotation.DrawableRes;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.widget.AppCompatDrawableManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.API.ApiFacade;
import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;
import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.app.Application;
import schooldriver.trackware.com.school_bus_driver_android.enums.TypeRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.dialogRoundInfo.ConfirmMessageDialog;


public class UtilityDriver implements IRestCallBack {
    public static final String ROUND = "ROUND";
    public static final String TYPE_ROUND = "TYPE_ROUND";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String WEB_API_KEY_GCM = "AIzaSyBdQkWLXMhfCdqyJD946uC7OXF-YU1ZG_0";
    public static final String SCHOOL_PHONE = "SCHOOL_PHONE";
    public static final String SCHOOL_NAME = "SCHOOL_NAME";
    public static final String SCHOOL_ID = "SCHOOL_ID";
    public static final String BATTERY_LOW = "BATTERY_LOW";
    public static final String SCHOOL_NEARBY_DISTANCE = "SCHOOL_NEARBY_DISTANCE";
    //    public static final String TOKEN_GCM = "TOKEN_GCM";
    public static final String AUTH = "AUTH";
    public static final String PIN = "PIN";
    public static final String SPEED = "SPEED";
    public static final String STAND_STILL = "STAND_STILL";
    public static final String BUS_ID = "BUS_ID";
    public static final String DRIVER_ID = "DRIVER_ID";
    public static final String UTC = "UTC";
    public static final String SCHOOL_DB = "SCHOOL_DB";
    public static final String MESSAGE_NOTIFICATION = "MESSAGE_NOTIFICATION";
    public static final String BUS_NUMBER = "BUS_NUMBER";
    public static final String LOGIN = "LOGIN";
    public static final String ALLOW_DRIVER_TO_USE_BEACON = "allow_driver_to_use_beacon";
    public static final String SPEED_LIMIT_WATCH = "SPEED_LIMIT_WATCH";
    public static final String STAND_STILL_WATCH = "STAND_STILL_WATCH";
    public static final String CHANEL_REFRESH_RATE = "CHANEL_REFRESH_RATE";
    public static final String SCHOOL_LATITUDE = "SCHOOL_LATITUDE";
    public static final String SCHOOL_LONGITUDE = "SCHOOL_LONGITUDE";
    public static final String NETWORK_PHONE = "NETWORK_PHONE";
    public static final String MESSAGE_IPHONE = "MESSAGEMESSAGE";
    public static final String STORE_ORDER = "store_order";
    public static final String SPEED_NEW = "SPEED_NEW";
    public static final String ROUND_MESSAGE_CANCEL = "ROUND_MESSAGE_CANCEL";
    public static final String SELECTED_TAB = "SELECTED_TAB";
//    yousef
    public static final String SERIAL_ID = "SERIAL_ID";
    public static final String TRACKLINK_ID = "TRACKLINK_ID";
    public static final String ROUND_ST = "ROUND_ST";
    public static final String SCHOOL_ORDER = "SCHOOL_ORDER";
    public static final String ENABLE_TRACK_LINK = "ENABLE_TRACK_LINK ";
    public static final String SID_TRACK_LINK = "SID_TRACK_LINK ";
    /**/

    /**/
    //    public static final String MESSAGE_IPHONE =  "{\n" +
//            "    \"aps\": {\n" +
//            "        \"alert\": \"MESSAGEMESSAGE\",\n" +
//            "        \"sound\": \"default\"\n" +
//            "    }\n" +
//            "}";
    public static final String START_ROUND_TIME = "START_ROUND_TIME";
    public static final SimpleDateFormat dateFormat_timerformat = new SimpleDateFormat("HH:mm:ss");


    public static Drawable getTintDrawable(Context context, @DrawableRes int drawableResId, int color) {
        @SuppressLint("RestrictedApi")
        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableResId);
        drawable = DrawableCompat.wrap(drawable).getConstantState().newDrawable();
        DrawableCompat.setTint(drawable, color);
        return drawable;
    }

    public static Drawable getTintListDrawable(Context context, @DrawableRes int drawableResId, ColorStateList colorStateList) {
        @SuppressLint("RestrictedApi")
        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableResId);
        drawable = DrawableCompat.wrap(drawable).getConstantState().newDrawable();
        DrawableCompat.setTintList(drawable, colorStateList);
        return drawable;
    }

    public static void showMessage(Activity applicationContext, String message) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show();
    }


    //region Shared Preference
    public static String getStringShared(String key) {
        return Application.getInstance().getPreferences().getString(key, "");
    }

    public static String getStringSharedForFireBase(String key) {
        try {
            return Application.getInstance().getPreferences().getString(key, "No Data");

        } catch (Exception e) {
            return "No Data";
        }

    }

    public static void setStringShared(String key, String value) {
        Application.getInstance().getPreferences().edit().putString(key, value).commit();
    }

    public static int getIntShared(String key) {
//        if (UtilityDriver.STAND_STILL.equals(key)){
//            return 1;
//        }
        return Application.getInstance().getPreferences().getInt(key, 0);
    }

    public static void setIntShared(String key, int value) {
        Application.getInstance().getPreferences().edit().putInt(key, value).commit();
    }

    public static boolean getBooleanShared(String key) {
        return Application.getInstance().getPreferences().getBoolean(key, false);
    }

    public static void setBooleanShared(String key, boolean value) {
        Application.getInstance().getPreferences().edit().putBoolean(key, value).commit();
    }


    public static boolean isEmptyString(String value) {
        boolean check = false;
        try {
            if (value == null || value.toLowerCase().equals("null") || value.trim().isEmpty()) {
                check = true;
            }
        } catch (Exception e) {
            check = true;
        }

        return check;

    }

    public static boolean isEmptyString(String... value) {
        boolean check = false;
        try {
            for (String str : value) {
                if (isEmptyString(str))
                    return true;
            }
        } catch (Exception e) {
            check = true;
        }

        return false;

    }


    public static String getDateFormat(String format) {
        return new SimpleDateFormat(format)
                .format(new Date());
    }

//    public static String getDateFormat(String format, Date date) {
//        return new SimpleDateFormat(format)
//                .format(date);
//    }

//    public static boolean afterDate(String toDate, String dateSearch) {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        Date strDate = null;
//        boolean value = false;
//        try {
//            strDate = sdf.parse(dateSearch);
//
//            if (sdf.parse(toDate).after(strDate)) {
//                value = true;
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return value;
//    }

//    public static boolean beforaDate(String fromDate, String dateSearch) {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        Date strDate = null;
//        boolean value = false;
//        try {
//            strDate = sdf.parse(dateSearch);
//
//            if (sdf.parse(fromDate).before(strDate)) {
//                value = true;
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return value;
//    }


    public static void setLocale(String lang, Activity activity, Class<MainActivity> mainActivity) {
        Locale myLocale = new Locale(lang);
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(activity, mainActivity);
        activity.startActivity(refresh);
        activity.finish();
        UtilityDriver.setStringShared(UtilityDriver.LANGUAGE, lang);
    }

    public static void setLocale(String lang, Context activity) {
        Locale myLocale = new Locale(lang);
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        UtilityDriver.setStringShared(UtilityDriver.LANGUAGE, lang);
    }


    public static Drawable getDrawableImage(Activity activity, String name) {
        name = name.replaceAll(" ", "_").toLowerCase();
        Resources res = activity.getResources();
        int resID = res.getIdentifier(name, "drawable", activity.getPackageName());
        Drawable drawable = res.getDrawable(resID);
        return drawable;
    }

//    public static Drawable getDrawableImage(Activity activity, int posotion) {
//        String name = RoundFragment.arrayNameEN[posotion].replaceAll(" ", "_").toLowerCase();
//        Resources res = activity.getResources();
//        int resID = res.getIdentifier(name, "drawable", activity.getPackageName());
//        Drawable drawable = res.getDrawable(resID);
//        return drawable;
//    }

    public static Map<String, String> typeHeaderMap(EnumTypeHeader type, boolean auth) {

        Map<String, String> headers = new HashMap<String, String>();


        if (type != EnumTypeHeader.EMPTY) {
            if (type == EnumTypeHeader.FORM) {
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            } else {
                headers.put("Content-Type", "application/json; charset=utf-8");
            }
        }
        if (auth) {
            headers.put("Authorization", UtilityDriver.getStringShared(UtilityDriver.AUTH));
        }
        headers.put("locale", UtilityDriver.getStringShared(UtilityDriver.LANGUAGE));

        return headers;

    }

    public static final int getColor(Context context, int color) {
//        final int version = Build.VERSION.SDK_INT;
//        if (version >= 23) {
//            return ContextCompatApi23.getColor(context, id);
//        } else {
        return context.getResources().getColor(color);
//        }
    }

    public static ConfirmMessageDialog confirmMessageDialog;

    public static void showMessageDialog(Activity mActivity, String tittle, String message) {

        if (StaticValue.progressDialog != null) {
            if (StaticValue.progressDialog.isShowing())
                StaticValue.progressDialog.dismiss();
        }

        if (confirmMessageDialog == null)
            confirmMessageDialog = new ConfirmMessageDialog(mActivity);
        confirmMessageDialog.showDialogTittle(mActivity, tittle, message);
        if (!confirmMessageDialog.isShowing()) {
            confirmMessageDialog.show();
        }

    }

    public static void showMessageLogout(Activity mActivity) {
        AlertDialog.Builder adb = new AlertDialog.Builder(mActivity);


        adb.setTitle(mActivity.getString(R.string.alarm));
        adb.setMessage(mActivity.getString(R.string.are_you_sure));

        ;
        adb.setIcon(android.R.drawable.ic_dialog_alert);


        adb.setPositiveButton(mActivity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.showFragmentLogin();
                dialog.dismiss();
            }
        });


        adb.setNegativeButton(mActivity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        adb.show();
    }


    public static long differenceTime(Date dateFirst, Date dateSecond) {

        long diff = dateSecond.getTime() - dateFirst.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
//        System.out.println("Time in seconds: " + diffSeconds + " seconds.");
//        System.out.println("Time in minutes: " + diffMinutes + " minutes.");
//        System.out.println("Time in hours: " + diffHours + " hours.");
        return Math.abs(diffMinutes);
    }

    public static String convertUTCDateString(String dateInString) {


//        if (UtilityDriver.isEmptyString(dateInString)){
//            return dateInString;
//        }
//        String time = null;
//        try {
//            String value = UtilityDriver.getStringShared(UtilityDriver.UTC);
//            int parcelInt = 0;
//            if (value.contains("+") ){
//                value = value.replaceAll("\\+","").replaceAll("\\-","");
//                parcelInt = +Integer.parseInt(value)/60;
//            }else if (value.contains("-") ){
//                value = value.replaceAll("\\+","").replaceAll("\\-","");
//                parcelInt = -Integer.parseInt(value)/60;
//            }else{
//                parcelInt = Integer.parseInt(value)/60;
//            }
//            String array[] = dateInString.split(":");
//            int addTime = Integer.parseInt(array[0])+parcelInt;
//            time =addTime+":"+array[1];
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return dateInString;

    }

//    public static String convertUTCDate(String dateInString) throws NullPointerException {
//
//
//        if (UtilityDriver.isEmptyString(dateInString)) {
//            return null;
//        }
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        Date inaptDate = null;
//        try {
//            String value = UtilityDriver.getStringShared(UtilityDriver.UTC);
//            if (UtilityDriver.isEmptyString(value)) {
//
//                return dateInString;
//            }
//            int parcelInt = 0;
//            if (value.contains("+")) {
//                value = value.replaceAll("\\+", "").replaceAll("\\-", "");
//                parcelInt = +Integer.parseInt(value) / 60;
//            } else if (value.contains("-")) {
//                value = value.replaceAll("\\+", "").replaceAll("\\-", "");
//                parcelInt = -Integer.parseInt(value) / 60;
//            } else {
//                parcelInt = Integer.parseInt(value) / 60;
//            }
//            inaptDate = simpleDateFormat.parse(addHour(dateInString, parcelInt));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return getDateFormat("HH:mm a", inaptDate);
//
//    }

    public static String addHour(String myTime, int number) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date d = df.parse(myTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.HOUR, number);
            String newTime = df.format(cal.getTime());
            return newTime;
        } catch (ParseException e) {
            System.out.println(" Parsing Exception");
        }
        return null;

    }

    public static String getMessageNotification(String value, TypeRoundEnum typeRoundEnum, String locale) {
        String message = "";
        /**/
        locale = locale.equals("ar") ? "_ar" : "_en";
        /**/
        String check_in = "check_in" + locale;
        String check_out = "check_out" + locale;
        String near_by = "near_by" + locale;
        String no_show = "no-show" + locale;
        String absent = "absent" + locale;
        /**/

        try {
            JSONArray jaNotificationText = new JSONArray(UtilityDriver.getStringShared(MESSAGE_NOTIFICATION));
            for (int i = 0; i < jaNotificationText.length(); i++) {
                JSONObject joMessage = jaNotificationText.getJSONObject(i);
                if (typeRoundEnum == TypeRoundEnum.DROP_ROUND) {
                    if (joMessage.toString().contains("drop-off")) {
                        if (value.equals(DriverConstants.IN_BUS)) {
                            message = joMessage.getJSONObject("actions").getString(check_in);
                        } else if (value.equals(DriverConstants.REACHED_HOME)) {
                            message = joMessage.getJSONObject("actions").getString(check_out);
                        } else if (value.equals("near")) {
                            message = joMessage.getJSONObject("actions").getString(near_by);
                        } else if (value.equals(DriverConstants.NO_SHOW_PICK) || value.equals(DriverConstants.NO_SHOW_DROP)) {
                            message = joMessage.getJSONObject("actions").getString(no_show);
                        }
                    }
                } else {
                    if (joMessage.toString().contains("pick-up")) {
                        if (value.equals(DriverConstants.IN_BUS)) {
                            message = joMessage.getJSONObject("actions").getString(check_in);
                        } else if (value.equals("out")) {
                            message = joMessage.getJSONObject("actions").getString(check_out);
                        } else if (value.equals("near")) {
                            message = joMessage.getJSONObject("actions").getString(near_by);
                        } else if (value.equals(DriverConstants.ABSENT_ALLDAY)|| value.equals(DriverConstants.ABSENT_MORNING)) {
                            message = joMessage.getJSONObject("actions").getString(absent);
                        }
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }


//    public static String getMessageNotification(String value) {
//        String message = "";
//        try {
//            JSONArray jaNotificationText = new JSONArray(UtilityDriver.getStringShared(MESSAGE_NOTIFICATION));
//            for (int i = 0; i < jaNotificationText.length(); i++) {
//                JSONObject joMessage = jaNotificationText.getJSONObject(i);
//                switch (value) {
//                    case "in":
//                        message = joMessage.getJSONArray("actions").getJSONObject(0).getString("check_in");
//                        break;
//                    case "out":
//                        message = joMessage.getJSONArray("actions").getJSONObject(0).getString("check_out");
//                        break;
//                    case "near":
//                        message = joMessage.getJSONArray("actions").getJSONObject(0).getString("near_by");
//                        break;
//                    case "no-show":
//                        message = joMessage.getJSONArray("actions").getJSONObject(0).getString("no-show");
//                        break;
//                    case "absent":
//                        message = joMessage.getJSONArray("actions").getJSONObject(0).getString("absent");
//                        break;
//                }
//
//            }
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return message;
//    }


//    public static  double distance(double lat1, double lon1, double lat2, double lon2) {
//        double Rad = 6372.8; //Earth's Radius In kilometers
//        // TODO Auto-generated method stub
//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLon = Math.toRadians(lon2 - lon1);
//        lat1 = Math.toRadians(lat1);
//        lat2 = Math.toRadians(lat2);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
//        double c = 2 * Math.asin(Math.sqrt(a));
//        double distance =  Rad * c /1000;
//return distance;
//    }

    //    private double deg2rad(double deg) {
//        return (deg * Math.PI / 180.0);
//    }
//
//    private double rad2deg(double rad) {
//        return (rad * 180.0 / Math.PI);
//    }
    public static <K, V extends Comparable<? super V>>
    List<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {

        List<Map.Entry<K, V>> sortedEntries = new ArrayList<Map.Entry<K, V>>(map.entrySet());

        Collections.sort(sortedEntries,
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );

        return sortedEntries;
    }

    //    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
//        double theta = lon1 - lon2;
//        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
//        dist = Math.acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60 * 1.1515;
//        if (unit == "K") {
//            dist = dist * 1.609344;
//        } else if (unit == "N") {
//            dist = dist * 0.8684;
//        }
//
//        return (dist);
//    }
//    private static double rad2deg(double rad) {
//        return (rad * 180 / Math.PI);
//    }
//    private static double deg2rad(double deg) {
//        return (deg * Math.PI / 180.0);
//    }
    public static double distanceOLD(double lat1, double lon1, double lat2, double lon2, String unit) {
        double rlat1 = Math.PI * lat1 / 180.0f;
        double rlat2 = Math.PI * lat2 / 180.0f;
        double rlon1 = Math.PI * lon1 / 180.0f;
        double rlon2 = Math.PI * lon2 / 180.0f;

        double theta = lon1 - lon2;
        double rtheta = Math.PI * theta / 180.0f;

        double dist = Math.sin(rlat1) * Math.sin(rlat2) + Math.cos(rlat1) * Math.cos(rlat2) * Math.cos(rtheta);
        dist = Math.acos(dist);
        dist = dist * 180.0f / Math.PI;
        dist = dist * 60.0f * 1.1515f;

        if (unit == "K") {
            dist = dist * 1.609344f;
        }
        if (unit == "M") {
            dist = dist * 1.609344 * 1000f;
        }
        if (unit == "N") {
            dist = dist * 0.8684f;
        }
        return dist;
    }


    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        /**/
        Location selected_location = new Location("locationA");
        selected_location.setLatitude(lat1);
        selected_location.setLongitude(lon1);
        /**/
        Location near_locations = new Location("locationB");
        near_locations.setLatitude(lat2);
        near_locations.setLongitude(lon2);

        double distance = selected_location.distanceTo(near_locations); //  distance in meters


        Log.v("-----------------------", "-----------------");
        Log.v("-----------------------", "-----------------");
//        Log.v("distance_OLD",distanceOLD(lat1,lon1 ,lat2 , lon2, "M")+"");
        Log.v("distance_NEW", distance + "");
        float[] distancearray = new float[2];
        Location.distanceBetween(lat1, lon1, lat2, lon2, distancearray);
        Log.v("distance_NEW2", distancearray[0] + "");

        Log.v("-----------------------", "-----------------");
        Log.v("-----------------------", "-----------------");

        return distance;



        /**/
//        double startLatitude;
//        double startLongitude;
//        double endLatitude;
//        double endLongitude;
//        float[] results;
        /**/
//        Location.distanceBetween( lat1,  lon1,  lat2,  lon2,  results);

//        double theta = lon1 - lon2;
//        double rtheta = Math.PI * theta / 180.0f;
//
//        double dist = distance;
////        dist = Math.acos(dist);
////        dist = dist * 180.0f / Math.PI;
////        dist = dist * 60.0f * 1.1515f;
////
//        if (unit == "K") {
//            dist = dist * 1.609344f;
//        }
//        if (unit == "M") {
//            dist = dist * 1.609344 * 1000f;
//        }
//        if (unit == "N") {
//            dist = dist * 0.8684f;
//        }
//        return dist;
    }


    public static boolean isNetworkAvailable(Activity mActivity) {


        ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
//            Log.v(TAG, "Internet Connection Not Present");
            return false;
        }

//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void logProject(String methodName, String message) {


//        final String messagess = "DATE :"+UtilityDriver.getDateFormat("dd hh:mm")+" ,PIN : "+ UtilityDriver.getStringShared(UtilityDriver.PIN)+",busNo : "+UtilityDriver.getStringShared(UtilityDriver.BUS_NUMBER)+" busId : "+UtilityDriver.getStringShared(UtilityDriver.BUS_ID)+", "+methodName+", MESSAGE::"+message;

//        callRestAPI("http://tikramservices.com/API/insert_test.php"
//                ,
//                new HashMap() {{
//                    put("message", messagess);
//                    put("app", "DRIVER");
//
//                }}
//                ,
//                EnumMethodApi.POST
//                ,
//                UtilityDriver.this
//                ,
//                EnumNameApi.SET_ROUND_STATUS
//                ,
//                UtilityDriver.typeHeaderMap(EnumTypeHeader.FORM, true)
//                ,
//                EnumTypeHeader.FORM
//        );
//        Raven.capture("DATE :"+UtilityDriver.getDateFormat("dd hh:mm")+" ,PIN : "+ UtilityDriver.getStringShared(UtilityDriver.PIN)+",busNo : "+UtilityDriver.getStringShared(UtilityDriver.BUS_NUMBER)+" busId : "+UtilityDriver.getStringShared(UtilityDriver.BUS_ID)+", "+methodName+", "+message);
//        Log.d("LOGG","PIN : "+ UtilityDriver.getStringShared(UtilityDriver.PIN)+",busNo : " + UtilityDriver.getStringShared(UtilityDriver.BUS_NUMBER) + " busId : " + UtilityDriver.getStringShared(UtilityDriver.BUS_ID) + ", " + methodName + ", " + message);
    }

    public static void callRestAPI(
            String PATH_URL,
            HashMap params,
            EnumMethodApi verb,
            IRestCallBack restCallBack,
            EnumNameApi enumNameApi,
            Map<String, String> mapHeader,
            EnumTypeHeader enumTypeHeader
    ) {
        ApiFacade callApi = new ApiFacade();
        callApi.onStartVolley(new ApiRequest(PATH_URL,
                        params,
                        verb,
                        restCallBack,
                        enumNameApi,
                        mapHeader
                )
                ,
                enumTypeHeader
        );
    }

    @Override
    public void onRestCallBack(String response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

    }

    @Override
    public void onRestCallBack(JSONObject response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

    }

    @Override
    public void onRestCallBack(VolleyError volleyError, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
//        if (volleyError.getMessage().toString().contains("java.net.UnknownHost")){                 UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.internet_connection), mActivity.getString(R.string.missing_internet_error));             return;         }

    }

    @Override
    public void onRestCallBack(NetworkResponse networkResponse, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

    }

    public static boolean isPointInPolygon(LatLng point, List<LatLng> polygonPoints) {
        boolean isPointInPolygon;
        if (PolyUtil.containsLocation(point, polygonPoints, true)) {
            isPointInPolygon = true;
        } else {
            isPointInPolygon = false;
        }
        return isPointInPolygon;
    }


    public static List getArrayMessage(String statusValue) {
        List list = new ArrayList();
        try {
            if (statusValue.contains("no-show")) {
                statusValue = "no_show";
            }
            JSONArray jsonArray = new JSONArray(UtilityDriver.getStringShared(UtilityDriver.ROUND_MESSAGE_CANCEL));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.toString().contains(statusValue)) {
                    JSONArray array = jsonObject.getJSONArray("actions");
                    for (int j = 0; j < array.length(); j++) {
                        list.add(array.getString(j));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void showAllViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void goneAllViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    public static void invisibleAllViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.INVISIBLE);
        }
    }


    public static boolean isStringEqualsAnyOfOther(String string, String... views) {
        try {
            for (String s : views) {
                if (string.equalsIgnoreCase(s))
                    return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
