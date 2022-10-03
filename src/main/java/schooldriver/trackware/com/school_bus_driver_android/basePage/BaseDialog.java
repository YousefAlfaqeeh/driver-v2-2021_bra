package schooldriver.trackware.com.school_bus_driver_android.basePage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.API.ApiFacade;
import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.DriverConstants;

/**
 * Created by muradtrac on 3/8/17.
 */

public class BaseDialog extends Dialog implements DriverConstants{
    protected Activity mActivity;


    public BaseDialog(Context context) {
        super(context, R.style.Theme_Dialog);
    }

    protected void callRestAPI(
            String PATH_URL,
            HashMap<String, String> params,
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
}
