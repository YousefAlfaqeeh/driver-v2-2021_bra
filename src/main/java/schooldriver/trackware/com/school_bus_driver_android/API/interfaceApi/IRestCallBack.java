package schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.DriverConstants;

/**
 */

public interface IRestCallBack extends DriverConstants {

    void onRestCallBack(String response, EnumNameApi nameApiEnum,ApiRequest volleyBean);
    void onRestCallBack(JSONObject response, EnumNameApi nameApiEnum,ApiRequest volleyBean);
    void onRestCallBack(VolleyError volleyError, EnumNameApi nameApiEnum,ApiRequest volleyBean);
    void onRestCallBack(NetworkResponse networkResponse, EnumNameApi nameApiEnum,ApiRequest volleyBean);
}
