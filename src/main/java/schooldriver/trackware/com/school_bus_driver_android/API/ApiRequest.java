package schooldriver.trackware.com.school_bus_driver_android.API;

import android.app.Activity;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;

/**
 */

public class ApiRequest {



    public ApiRequest(String url_path, HashMap map, EnumMethodApi method, View view, Activity activity, IRestCallBack restCallBack, EnumNameApi enumNameApi, Map<String, String> mapHeader) {
        this.url_path = url_path;
        this.map = map;
        this.method = method;
        this.view = view;
        this.activity = activity;
        this.restCallBack= restCallBack;
        this.mapHeader = mapHeader;
        this.enumNameApi = enumNameApi;
    }


    String url_path;

    HashMap map;

    IRestCallBack restCallBack;

    EnumMethodApi method;

    View view;

    Activity activity;
    EnumNameApi enumNameApi;

    public ApiRequest(String url_path, HashMap<String, String> map, EnumMethodApi method, IRestCallBack restCallBack, EnumNameApi enumNameApi, Map<String, String> mapHeader) {

        this.url_path = url_path;
        this.map = map;
        this.method = method;
        this.restCallBack= restCallBack;
        this.mapHeader = mapHeader;
        this.enumNameApi = enumNameApi;
    }

    public EnumNameApi getEnumNameApi() {
        return enumNameApi;
    }

    public void setEnumNameApi(EnumNameApi enumNameApi) {
        this.enumNameApi = enumNameApi;
    }

    Map<String,String> mapHeader;

    public Map<String, String> getMapHeader() {
        return mapHeader;
    }

    public void setMapHeader(Map<String, String> mapHeader) {
        this.mapHeader = mapHeader;
    }

    public IRestCallBack getRestCallBack() {
        return restCallBack;
    }

    public void setRestCallBack(IRestCallBack restCallBack) {
        this.restCallBack = restCallBack;
    }



    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getUrl_path() {
        return url_path;
    }

    public void setUrl_path(String url_path) {
        this.url_path = url_path;
    }


    public EnumMethodApi getMethod() {
        return method;
    }

    public void setMethod(EnumMethodApi method) {
        this.method = method;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public HashMap getMap() {
        return map;
    }

    public void setMap(HashMap map) {
        this.map = map;
    }
}
