package schooldriver.trackware.com.school_bus_driver_android.locationListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.RequiresApi;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.API.ApiFacade;
import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;
import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.enums.EnumConfigNotify;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.PathUrl;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created by muradtrac on 3/29/17.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class ConfigNotify implements IRestCallBack {

    public EnumConfigNotify enumConfigNotify;
    Activity mActivity;

    public ConfigNotify() {

    }


    public ConfigNotify(final String latitude, final String longitude, final String value, final EnumConfigNotify enumConfigNotify, final Activity mActivity) {
        this(latitude, longitude, value, enumConfigNotify, mActivity, null, null);
    }

    public ConfigNotify(final String latitude, final String longitude, final String value, final EnumConfigNotify enumConfigNotify, final Activity mActivity, Integer original_student, Integer picked_student) {
        this.mActivity = mActivity;
        this.enumConfigNotify = enumConfigNotify;
        callRestAPI(PathUrl.MAIN_URL + PathUrl.NOTIFY
                ,
                new HashMap() {
                    {

                        if (EnumConfigNotify.BATTERY == enumConfigNotify) {
                            put("name", "battery_low");
                            put("battery_power", value);
                        } else if (EnumConfigNotify.NETWORK == enumConfigNotify) {
                            put("name", "network");
                            put("network", value);
                        } else if (EnumConfigNotify.GPS_OFF == enumConfigNotify) {
                            put("name", "gps_off");
                            put("gps_off", value);
                        } else if (EnumConfigNotify.SPEED == enumConfigNotify) {
                            put("name", "user_speed_exceeded");
                            put("speed", value);
//                            put("address", StaticValue.ADDRESS);
                            put("address", StaticValue.getCurrentAreaName(mActivity));
                        } else if (EnumConfigNotify.STAND_STILL == enumConfigNotify) {
                            put("name", "user_no_move_time_exceeded");
//                            put("address", StaticValue.ADDRESS);
                            put("address", StaticValue.getCurrentAreaName(mActivity));
//                            put("stand_still", value);
                        } else if (EnumConfigNotify.ROUTE_CHANGED == enumConfigNotify) {


                            put("original_student_id", original_student);
                            put("picked_student_id", picked_student);
                            put("name", "route_changed");

                        }
                        if (MainActivity.CURRENT_SELECTED_ROUND != null)
                            put("round_id", MainActivity.CURRENT_SELECTED_ROUND.getId());

//                        put("battery_power", value);
                        put("lat", latitude);
                        put("long", longitude);

                    }

                }
                ,
                EnumMethodApi.POST
                ,
                ConfigNotify.this
                ,
                EnumNameApi.NOTIFY
                ,
                UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
                , EnumTypeHeader.JSON
        );

    }

    public void sendMessageAdmin(final List<Integer> listInteger, final String message, final int id) {
        callRestAPI(PathUrl.MAIN_URL + PathUrl.NOTIFY
                ,
                new HashMap() {
                    {

                        put("name", "emergency");
                        put("round_id", id);
                        put("students_ids", listInteger.toArray());
                        put("long", StaticValue.latitudeMain);
                        put("lat", StaticValue.longitudeMain);
                        put("emergency_text", message);


                    }

                }
                ,
                EnumMethodApi.POST
                ,
                ConfigNotify.this
                ,
                EnumNameApi.NOTIFY
                ,
                UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
                , EnumTypeHeader.JSON
        );

    }

    @Override
    public void onRestCallBack(String response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

    }

    @Override
    public void onRestCallBack(JSONObject response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
//        if (EnumConfigNotify.NETWORK == enumConfigNotify) {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
//            mActivity.startActivity(intent);
//        } else
        if (EnumConfigNotify.GPS_OFF == enumConfigNotify) {
            final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mActivity.startActivity(intent);
        } else if (EnumConfigNotify.BATTERY == enumConfigNotify) {
            UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.failed), mActivity.getString(R.string.please_charger));
        } else if (nameApiEnum == EnumNameApi.NOTIFY) {
//            initNotificationList();
//            new RoundPresenter().callSelectNotification();
        }
    }

    @Override
    public void onRestCallBack(VolleyError volleyError, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
        if (volleyError.getMessage().toString().contains("java.net.UnknownHost")) {
            UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.internet_connection), mActivity.getString(R.string.missing_internet_error));
            return;
        }
//        UtilityDriver.showMessageDialog(mActivity,mActivity.getString(R.string.error_api),volleyError.getMessage());
    }

    @Override
    public void onRestCallBack(NetworkResponse networkResponse, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

    }

    protected void callRestAPI(
            String PATH_URL,
            HashMap params,
            EnumMethodApi verb,
            IRestCallBack restCallBack,
            EnumNameApi enumNameApi,
            Map<String, String> mapHeader,
            EnumTypeHeader form
    ) {
        ApiFacade callApi = new ApiFacade();
        callApi.onStartVolley(new ApiRequest(PATH_URL,
                        params,
                        verb,
                        restCallBack,
                        enumNameApi,
                        mapHeader
                ),
                form
        );
    }

}
