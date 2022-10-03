package schooldriver.trackware.com.school_bus_driver_android.API;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IVolleySendApi;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.app.Application;
import schooldriver.trackware.com.school_bus_driver_android.locationListener.LocationListener;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;


/**
 * Created by muradtrac on 1/7/17.
 */

public class ApiClient implements IVolleySendApi {

    //    public static final String ERROR_VOLLEY = "ERROR_VOLLEY";
        public static final int TIMEOUT = 60000;
    ApiRequest volleyBeans;
    Button view;
    //    ProgressDialog progressDialog;
    Activity activity;

    public ApiClient(ApiRequest volleyBean) {
        this.volleyBeans = volleyBean;
        activity = volleyBean.getActivity();
//        progressDialog = new ProgressDialog(activity);
        view = (Button) volleyBean.getView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVolley(volleyBeans);
            }
        });
        sendVolley(volleyBeans);
    }

    public ApiClient(ApiRequest volleyBeans, EnumTypeHeader enumTypeHeader) {

        if (StaticValue.mActivity != null) {
            if (!UtilityDriver.isNetworkAvailable(StaticValue.mActivity)) {
                if (StaticValue.progressDialog != null) {
                    if (StaticValue.progressDialog.isShowing())
                        StaticValue.progressDialog.dismiss();

                }
                UtilityDriver.showMessageDialog(StaticValue.mActivity, StaticValue.mActivity.getString(R.string.internet_connection), StaticValue.mActivity.getString(R.string.missing_internet_error));
                return;
            }
        }
        this.volleyBeans = volleyBeans;
        if (enumTypeHeader == EnumTypeHeader.JSON) {
            sendVolleyJson(volleyBeans);
        } else {
            sendVolley(volleyBeans, true);
        }
    }

    @Override
    public void sendVolley(final ApiRequest volleyBean) {
//        showProgressDialog();
        int typeMethod = Request.Method.POST;
        if (volleyBean.getMethod().equals(EnumMethodApi.GET)) {
            typeMethod = Request.Method.GET;
        }
        StringRequest jsonObjReq = new StringRequest(typeMethod,
                volleyBean.getUrl_path(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressDialog.dismiss();
                        view.setVisibility(View.INVISIBLE);
                        volleyBeans.restCallBack.onRestCallBack(response, volleyBean.getEnumNameApi(), volleyBean);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Application.logEvents(volleyBean, "ApiClient - sendVolley",volleyBean.toString(),error);
//                progressDialog.dismiss();
                view.setVisibility(View.VISIBLE);
                view.setText(activity.getString(R.string.retry));
                volleyBeans.restCallBack.onRestCallBack(error, volleyBean.getEnumNameApi(), volleyBean);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return volleyBean.getMap();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                volleyBeans.restCallBack.onRestCallBack(response, volleyBean.getEnumNameApi(), volleyBean);
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return volleyBean.getMapHeader();
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Application.getInstanceVolly().addToRequestQueue(jsonObjReq, volleyBean.getUrl_path());
    }


    @Override
    public void sendVolley(final ApiRequest volleyBean, boolean value) {
        int typeMethod = Request.Method.POST;
        if (volleyBean.getMethod().equals(EnumMethodApi.GET)) {
            typeMethod = Request.Method.GET;
        }
        if (volleyBean.enumNameApi == EnumNameApi.ROUND_LIST){
            System.err.println(""+ "1SSSSSS");
        }
        StringRequest jsonObjReq = new StringRequest(typeMethod,
                volleyBean.getUrl_path(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        volleyBeans.restCallBack.onRestCallBack(response, volleyBean.getEnumNameApi(), volleyBean);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Application.logEvents(volleyBean, "ApiClient - sendVolley",volleyBean,error);
                volleyBeans.restCallBack.onRestCallBack(error, volleyBean.getEnumNameApi(), volleyBean);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return volleyBean.getMap();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                volleyBeans.restCallBack.onRestCallBack(response, volleyBean.getEnumNameApi(), volleyBean);
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return volleyBean.getMapHeader();
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Application.getInstanceVolly().addToRequestQueue(jsonObjReq, volleyBean.getUrl_path());
    }

    @Override
    public void sendVolleyJson(final ApiRequest volleyBean) {
        try {


            int typeMethod = Request.Method.POST;
            if (volleyBean.getMethod().equals(EnumMethodApi.GET)) {
                typeMethod = Request.Method.GET;
            }
            JSONObject jsonObject = new JSONObject(volleyBean.getMap());

//            if (volleyBean.getEnumNameApi() == EnumNameApi.STUDENT_BUS_CHECK) {
//                System.err.println("");
//            }

            JsonObjectRequest myRequest = new JsonObjectRequest(
                    typeMethod,
                    volleyBean.getUrl_path(),
                    jsonObject,

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (StaticValue.progressDialog != null) {
                                if (StaticValue.progressDialog.isShowing())
                                    StaticValue.progressDialog.dismiss();
                            }
                            System.err.println(volleyBean.getEnumNameApi() + " API::: " + response);
                            volleyBeans.restCallBack.onRestCallBack(response, volleyBean.getEnumNameApi(), volleyBean);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Application.logEvents(volleyBean, "ApiClient - sendVolleyJson - onErrorResponse",volleyBean,error);
                            if (StaticValue.progressDialog != null) {
                                if (StaticValue.progressDialog.isShowing())
                                    StaticValue.progressDialog.dismiss();
                            }
                            try {
                                volleyBeans.restCallBack.onRestCallBack(error, volleyBean.getEnumNameApi(), volleyBean);
                            } catch (NullPointerException e) {
                                Application.logEvents(volleyBean, "ApiClient - sendVolleyJson - NullPointerException",volleyBean,error);

                            }
                        }
                    }) {
//            @Override
//            protected Map<String, String> getParams() {
//                return volleyBean.getMap();
//            }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    volleyBeans.restCallBack.onRestCallBack(response, volleyBean.getEnumNameApi(), volleyBean);
                    return super.parseNetworkResponse(response);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return volleyBean.getMapHeader();
                }
            };
//        Application.getInstance().addToRequestQueue(myRequest, "tag");
            myRequest.setRetryPolicy(new DefaultRetryPolicy(
                    TIMEOUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Application.getInstanceVolly().addToRequestQueue(myRequest, volleyBean.getUrl_path());
        } catch (NullPointerException e) {
            Application.logEvents(volleyBean, "ApiClient - sendVolleyJson  - NullPointerException",volleyBean,e);

        }
    }

//    public void showProgressDialog() {
//
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                progressDialog.setMessage(activity.getString(R.string.waiting));
//                progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, activity.getString(R.string.cancel),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int whichButton) {
//                                view.setVisibility(View.INVISIBLE);
//                                progressDialog.dismiss();
//                            }
//                        });
//
//                progressDialog.show();
//
//            }
//        });
//
//
//    }


}
