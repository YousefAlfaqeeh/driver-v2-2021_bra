package schooldriver.trackware.com.school_bus_driver_android.API;

import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;

/**
 */

public class ApiFacade {
    ApiClient thread;
    public void onStringRequest(ApiRequest volleyBean) {
        if (thread == null) {
            thread = new ApiClient(volleyBean);
        }

    }

    public void onStartVolley(ApiRequest volleyBean, EnumTypeHeader enumTypeHeader) {
        if (thread == null) {
            thread = new ApiClient(volleyBean,enumTypeHeader);
        }
    }
//dddd
}
