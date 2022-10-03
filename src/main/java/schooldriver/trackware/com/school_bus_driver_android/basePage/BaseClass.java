package schooldriver.trackware.com.school_bus_driver_android.basePage;

import java.util.HashMap;
import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.API.ApiFacade;
import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;

/**
 * Created by muradtrac on 4/3/17.
 */

public class BaseClass {

        protected void callRestAPI(
                String PATH_URL,
                HashMap<String, String> params,
                EnumMethodApi verb,
                IRestCallBack restCallBack,
                EnumNameApi enumNameApi,
                Map<String,String> mapHeader,
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
}
