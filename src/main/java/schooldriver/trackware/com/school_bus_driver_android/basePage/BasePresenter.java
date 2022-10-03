package schooldriver.trackware.com.school_bus_driver_android.basePage;

import android.app.Activity;


import java.util.HashMap;
import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.API.ApiFacade;
import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created by muradtrac on 3/6/17.
 */

public class BasePresenter<T> {

    protected Activity mActivity = null;

    protected void callRestAPI(
            String PATH_URL,
            HashMap params,
            EnumMethodApi verb,
            IRestCallBack restCallBack,
            EnumNameApi enumNameApi,
            Map<String, String> mapHeader,
            EnumTypeHeader enumTypeHeader
    ) {
//        FL.d("==========================");
//        FL.d("==========================");
//        FL.d("api Calling");
//        FL.d("URL : ");
//        FL.d(PATH_URL);
//        FL.d("_____________");
//        FL.d("AUTH : ");
//        FL.d(UtilityDriver.getStringShared(UtilityDriver.AUTH));
//        FL.d("_____________");
//        FL.d("params : ");
//        FL.d(params != null ? params.toString() : "");
//        FL.d("_____________");
//        FL.d("Header : ");
//        FL.d(mapHeader != null ? mapHeader.toString() : "");
//        FL.d("==========================");
//        FL.d("==========================");
//        /**/
//        FL.d("==========================");
//        FL.d("==========================");
//        FL.d("api Calling");
//        FL.d("URL : ");
//        FL.d(PATH_URL);
//        FL.d("_____________");
//        FL.d("AUTH : ");
//        FL.d(UtilityDriver.getStringShared(UtilityDriver.AUTH));
//        FL.d("_____________");
//        FL.d("params : ");
//        FL.d(params != null ? params.toString() : "");
//        FL.d("_____________");
//        FL.d("Header : ");
//        FL.d(mapHeader != null ? mapHeader.toString() : "");
//        FL.d("==========================");
//        FL.d("==========================");
        /**/
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
