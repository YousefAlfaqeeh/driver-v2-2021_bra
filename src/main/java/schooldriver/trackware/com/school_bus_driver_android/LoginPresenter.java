package schooldriver.trackware.com.school_bus_driver_android;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;
import schooldriver.trackware.com.school_bus_driver_android.app.Application;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BasePresenter;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.IActionLogin;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.PathUrl;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilDialogs;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 */

public class LoginPresenter extends BasePresenter implements IRestCallBack {

    private final IActionLogin iActionLogin;
    MainActivity mActivity;
    private String pin;

    public LoginPresenter(MainActivity mActivity, IActionLogin iActionLogin) {
        this.mActivity = mActivity;
        this.iActionLogin = iActionLogin;
    }

    public void callApiLogin(final String pin, final String param) {


        EnumNameApi enumNameApi = EnumNameApi.LOGIN_BUS;
        if (param.equals("bus_pin")) {
            enumNameApi = EnumNameApi.LOGIN_DRIVER;
            this.pin = pin;
        } else if (param.equals("LOGIN_DRIVER_REFRESH")) {
            enumNameApi = EnumNameApi.LOGIN_DRIVER_REFRESH;
            this.pin = pin;
        }
        Log.v("PathUrl.MAIN_URL + PathUrl.LOGIN",PathUrl.MAIN_URL + PathUrl.LOGIN);
        callRestAPI(PathUrl.MAIN_URL + PathUrl.LOGIN
                ,
                new HashMap<String, String>() {{
//                    put("driver_code",driverCode);
                    put(param, pin);
//                    put("login_type","driver");
//                    put("mobile_platform","android");


                    Log.v("FirebaseInstanceId", mActivity.fireBaseToken);
//                    UtilityDriver.setStringShared(token,UtilityDriver.TOKEN_GCM);
                    put("mobile_token", mActivity.fireBaseToken);
//                    put("datetime",UtilityDriver.getDateFormat("dd/MM/yyyy hh:mm:ss"));
                }}
                ,
                EnumMethodApi.POST
                ,
                LoginPresenter.this
                ,
                enumNameApi
                ,
                UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, false)
                ,
                EnumTypeHeader.JSON
        );
    }

    @Override
    public void onRestCallBack(String response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

    }

      
    @Override
    public void onRestCallBack(JSONObject response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

        if (EnumNameApi.LOGIN_BUS == nameApiEnum) {
            try {
                String status = response.getString("status");
                if (status.contains("ok")) {
                    iActionLogin.done("done");
                } else {
                    iActionLogin.error("error");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (EnumNameApi.LOGIN_DRIVER == nameApiEnum || EnumNameApi.LOGIN_DRIVER_REFRESH == nameApiEnum) {

            System.err.println(response);
            try {
                String status = response.getString("status");
                if (status.contains("ok")) {
                    UtilityDriver.setStringShared(UtilityDriver.PIN, pin);
                    UtilityDriver.setStringShared(UtilityDriver.SCHOOL_PHONE, response.optString("school_phone", ""));
                    UtilityDriver.setStringShared(UtilityDriver.SCHOOL_NAME, response.optString("school_name"));
                    UtilityDriver.setStringShared(UtilityDriver.SCHOOL_ID, response.optString("school_id"));
                    UtilityDriver.setStringShared(UtilityDriver.BUS_ID, response.optString("bus_id"));
                    UtilityDriver.setStringShared(UtilityDriver.DRIVER_ID, response.optString("driver_id"));
                    UtilityDriver.setStringShared(UtilityDriver.UTC, response.optString("utc_offset"));
                    UtilityDriver.setStringShared(UtilityDriver.SCHOOL_DB, response.optString("school_db", ""));
//                    yousef {iks1TGZOU
                    UtilityDriver.setStringShared(UtilityDriver.SCHOOL_ORDER, response.optString("use_round_order"));
                    UtilityDriver.setStringShared(UtilityDriver.ENABLE_TRACK_LINK, response.optString("enable_track_link"));
                    UtilityDriver.setStringShared(UtilityDriver.SERIAL_ID,response.optString("platte_no"));
                    Log.d("ssssssssssssssss1452",response.optString("platte_no"));
//                    UtilityDriver.setStringShared(UtilityDriver.SERIAL_ID,"20-19394");
//                    UtilityDriver.setStringShared(UtilityDriver.SID_TRACK_LINK, "35b801894b909b05a6f520980b2bdf37");
//                    end

                    /**/
                    boolean auto_round_ending = response.optBoolean("auto_round_ending",true);
                    UtilityDriver.setBooleanShared(UtilityDriver.AUTO_ROUND_ENDING, auto_round_ending);
                    /**/
                    /**/
                    String latitude = response.getString("school_lat");
                    String longitude = response.getString("school_lng");
                    /**/
                    if (!UtilityDriver.isEmptyString(latitude)) {
                        UtilityDriver.setStringShared(UtilityDriver.SCHOOL_LATITUDE, latitude);
                    } else {
                        UtilityDriver.setStringShared(UtilityDriver.SCHOOL_LATITUDE, "0");
                    }
                    if (!UtilityDriver.isEmptyString(longitude)) {
                        UtilityDriver.setStringShared(UtilityDriver.SCHOOL_LONGITUDE, longitude);
                    } else {
                        UtilityDriver.setStringShared(UtilityDriver.SCHOOL_LONGITUDE, "0");
                    }
                    /**/
                    /**/
                    String lat_end = response.optString("lat_end",UtilityDriver.getStringShared(UtilityDriver.SCHOOL_LATITUDE));
                    String lng_end = response.optString("lng_end",UtilityDriver.getStringShared(UtilityDriver.SCHOOL_LONGITUDE));
                    UtilityDriver.setStringShared(UtilityDriver.LAT_END, lat_end);
                    UtilityDriver.setStringShared(UtilityDriver.LNG_END, lng_end);
                    /**/
                    UtilityDriver.setStringShared(UtilityDriver.SCHOOL_NEARBY_DISTANCE, response.getString("nearby_distance"));
                    UtilityDriver.setStringShared(UtilityDriver.BUS_NUMBER, response.getString("bus_number"));

                    if (response.toString().contains("location_refresh_rate")) {
                        UtilityDriver.setIntShared(UtilityDriver.CHANEL_REFRESH_RATE, response.getInt("location_refresh_rate"));
                    } else {
                        UtilityDriver.setIntShared(UtilityDriver.CHANEL_REFRESH_RATE, 5);
                    }
                    System.err.println(response);
                    UtilityDriver.setStringShared(UtilityDriver.MESSAGE_NOTIFICATION, response.getString("notifications_text"));
                    if (response.toString().contains("round_cancellation_messages"))
                        UtilityDriver.setStringShared(UtilityDriver.ROUND_MESSAGE_CANCEL, response.getString("round_cancellation_messages"));
                    if (response.toString().contains("Authorization"))
                        UtilityDriver.setStringShared(UtilityDriver.AUTH, response.getString("Authorization"));


                    if (response.toString().contains("notifications_settings")) {
                        JSONArray jaNotificationSetting = response.getJSONArray("notifications_settings");
                        for (int i = 0; i < jaNotificationSetting.length(); i++) {
                            JSONObject joNotificationSetting = jaNotificationSetting.getJSONObject(i);
                            if (joNotificationSetting.toString().contains("speed_limit_watch")) {
                                UtilityDriver.setBooleanShared(UtilityDriver.SPEED_LIMIT_WATCH, joNotificationSetting.getBoolean("speed_limit_watch"));
                            }
                            if (joNotificationSetting.toString().contains("standstill_watch")) {
                                UtilityDriver.setBooleanShared(UtilityDriver.STAND_STILL_WATCH, joNotificationSetting.getBoolean("standstill_watch"));
                            }
                        }
                    }


                    JSONArray jsonArray = response.getJSONArray("notifications_thresholds");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    UtilityDriver.setIntShared(UtilityDriver.BATTERY_LOW, jsonObject.getInt("battery_low"));
                    UtilityDriver.setIntShared(UtilityDriver.SPEED, jsonObject.getInt("user_speed_exceeded"));
                    UtilityDriver.setIntShared(UtilityDriver.STAND_STILL, jsonObject.optInt("user_no_move_time_exceeded", 0));
//                    UtilityDriver.setStringShared(UtilityDriver.GEOFENSES, response.getString("geofenses"));
                    UtilityDriver.setBooleanShared(UtilityDriver.LOGIN, true);

                    if (EnumNameApi.LOGIN_DRIVER_REFRESH != nameApiEnum)
                        MainActivity.showFragmentRound();

//                    MainActivity.mainActivity.openConnect("done");
                } else if (status.equalsIgnoreCase("Bus not found")) {
//                    UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.error_api), status);

                    new UtilDialogs.MessageYesNoDialog().show(mActivity)
                            .setYesButtonText(R.string.ok)
                            .hideCloseButton()
                            .setDialogeTitle(mActivity.getString(R.string.pin_code_error))
                            .setDialogeTitleTextColor(R.color.red_tabs);


                } else {
                    throw new Exception("");
                }
            } catch (Exception e) {
                String r = response == null ? "null" : response.toString();
                String v = volleyBean == null ? "null" : volleyBean.toString();
                Application.logEvents(nameApiEnum, "LoginPresenter - onRestCallBack", e, r, v);

                e.printStackTrace();
                new UtilDialogs.MessageYesNoDialog().show(mActivity)
                        .setYesButtonText(R.string.ok)
                        .hideCloseButton()
                        .setDialogeTitle(mActivity.getString(R.string.error_api))
                        .setDialogeTitleTextColor(R.color.red_tabs);
            }

        }
    }

    @Override
    public void onRestCallBack(VolleyError volleyError, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
        if (volleyError.getMessage().toString().contains("java.net.UnknownHost")) {
            UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.internet_connection), mActivity.getString(R.string.missing_internet_error));
            return;
        }


        new UtilDialogs.MessageYesNoDialog().show(mActivity)
                .setYesButtonText(R.string.ok)
                .hideCloseButton()
                .setDialogeTitle(mActivity.getString(R.string.error_api))
                .setDialogeTitleTextColor(R.color.red_tabs);


        String r = volleyError == null ? "null" : volleyError.toString();
        String v = volleyBean == null ? "null" : volleyBean.toString();
        Application.logEvents(nameApiEnum.name(), "LoginPresenter - onRestCallBack", r, v);

//        UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.error_api), volleyError.getMessage());


    }

    @Override
    public void onRestCallBack(NetworkResponse networkResponse, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
        String authorization = networkResponse.headers.get("x-amzn-remapped-authorization");
        if (!UtilityDriver.isEmptyString(authorization))
            UtilityDriver.setStringShared(UtilityDriver.AUTH, authorization);
    }
}
