package schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi;

import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;

/**
 * Created by Murad.Ibrahim on 12/5/2016.
 */

public interface IVolleySendApi {


    void sendVolley(ApiRequest volleyBean);
    void sendVolley(ApiRequest volleyBean, boolean value);
    void sendVolleyJson(ApiRequest volleyBean);

}
