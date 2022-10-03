package schooldriver.trackware.com.school_bus_driver_android.APIs_new;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ListenersAndInterFaces.DefaultOnComplete;
import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ListenersAndInterFaces.OnApiComplete;
import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ListenersAndInterFaces.RequestHeader;
import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ListenersAndInterFaces.ServiceResponseErrorListener;
import schooldriver.trackware.com.school_bus_driver_android.app.Application;


public class VolleyRequest<RespType> extends Request<RespType> implements RequestHeader {

    public static final String TAG = VolleyRequest.class.getSimpleName();
    public static final int TIMEOUT = 60000*5;

    private Map<String, String> mapParams = new HashMap<String, String>();
    private Map<String, String> mapHeaders = new HashMap<String, String>();
    private byte[] data;
    private Gson gson = GsonInstance.getGson();
    private OnApiComplete<RespType> onComplete;
    private Type respType;
    private Context context;
    private String authorization;
    private boolean saveAuthorization = false;

    public VolleyRequest(Context context,
                         HttpMethod method,
                         String url,
                         Type respType,
                         OnApiComplete<RespType> onComplete,
                         int timeOutInMillis) {
        super(method.getValue(), url, new ServiceResponseErrorListener(context, onComplete));
        this.onComplete = onComplete;
        this.respType = respType;
        this.context = context;
        setRetryPolicy(new DefaultRetryPolicy(timeOutInMillis, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.saveAuthorization = false;
    }


    public VolleyRequest(Context context,
                         HttpMethod method,
                         String url,
                         Type respType,
                         OnApiComplete<RespType> onComplete) {
        this(context, method, url, respType, onComplete, TIMEOUT);
        this.saveAuthorization = false;
    }

    public VolleyRequest(Context context,
                         HttpMethod method,
                         String url,
                         Type respType,
                         OnApiComplete<RespType> onComplete,
                         boolean saveAuthorization) {
        this(context, method, url, respType, onComplete, TIMEOUT);
        this.saveAuthorization = saveAuthorization;
    }

    @Override
    public void addHeader(String key, Object o) {

        if (o == null || key == null) {
            return;
        }
        mapHeaders.put(key, o.toString());
    }

    @Override
    public void addParam(String key, Object o) {
        if (o == null || key == null) {
            return;
        }
        mapParams.put(key, o.toString());
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mapHeaders;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mapParams;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Response<RespType> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, "UTF-8");
            Log.v("response", "" + response.toString());
            RespType resp = gson.fromJson(json, this.respType);
            authorization = response.headers.get("Authorization");
            if (saveAuthorization) {
//                SavedDataUtil.setShared(SavedDataUtil.AUTHORIZATION, authorization, context);


            }
            return Response.success(resp, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            Application.logEvents(response, "VolleyRequest - parseNetworkResponse",e);
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            Application.logEvents(response, "VolleyRequest - parseNetworkResponse",e);
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(RespType response) {
        if (response != null) {
            try {
                onComplete.onSuccess(response);
            } catch (Exception e) {
                Application.logEvents(response, "VolleyRequest - deliverResponse",e);
                onComplete.onError(DefaultOnComplete.NULL_RESPONSE, "Error : "+e.toString());
            }
        } else {
            Application.logEvents(response, "VolleyRequest - deliverResponse","response is null");
            onComplete.onError(DefaultOnComplete.NULL_RESPONSE, "Error");
        }
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }

    @Override
    protected String getParamsEncoding() {
        return "UTF-8";
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return data;
    }

    public void setData(Object data) {
        if (data instanceof byte[]) {
            this.data = (byte[]) data;
        } else if (data instanceof String) {
            this.data = ((String) data).getBytes();
        } else {
            String str = gson.toJson(data);
            Log.e(TAG, "data: " + str);
            this.data = str.getBytes();
        }
    }


}