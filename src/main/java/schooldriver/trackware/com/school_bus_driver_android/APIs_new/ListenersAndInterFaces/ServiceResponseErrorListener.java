package schooldriver.trackware.com.school_bus_driver_android.APIs_new.ListenersAndInterFaces;


import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.lang.reflect.Array;
import java.net.UnknownHostException;
import java.util.Arrays;

import schooldriver.trackware.com.school_bus_driver_android.app.Application;

public class ServiceResponseErrorListener implements Response.ErrorListener {

    public static final int NO_ERROR = 0;
    public static final int NETWORK_IS_UNREACHABLE = 1;
    public static final int CONNECTION_REFUSED = 2;
    public static final int UNKNOWN_HOST = 3;
    public static final int SERVER_ERROR = 4;
    public static final int TIME_OUT = 5;
    public static final int ERROR = 6;
    public static final int AUTH_FAILURE_ERROR = 7;
    public static final int NETWORK_ERROR = 8;
    public static final int PARES_ERROR = 9;

    private String MSG_NO_INTERNET_CONNECTION;
    private String MSG_NO_CONNECTION_WITH_SERVER;
    private String MSG_CONNECTION_TIME_OUT;
    private String MSG_UNKNOWN_HOST;

    private OnApiComplete mOnComplete;
    private Context context;

    public ServiceResponseErrorListener(Context context, OnApiComplete mOnComplete) {
        this.context = context;
        if (mOnComplete == null) {
            this.mOnComplete = new OnApiComplete() {
                @Override
                public void onSuccess(Object data) {

                }

                @Override
                public void onError(int errorCode, String errorMessage) {
                    Application.logEvents("VolleyError", "ServiceResponseErrorListener - onErrorResponse ", errorMessage);

                }
            };
        }
        this.mOnComplete = mOnComplete;
        MSG_NO_INTERNET_CONNECTION = "No internet connection";
        MSG_NO_CONNECTION_WITH_SERVER = "No Connection with server";
        MSG_CONNECTION_TIME_OUT = "connection time out";
        MSG_UNKNOWN_HOST = "unknown host";
    }

    public ServiceResponseErrorListener(Context context) {
        this(context, null);
    }

    @Override
    public final void onErrorResponse(VolleyError error) {
        Application.logEvents("VolleyError", "ServiceResponseErrorListener - onErrorResponse ", error);


        if (error instanceof NoConnectionError) {
            if (error.getMessage().contains("Network is unreachable")) {
                networkIsUnreachable(error);
                mOnComplete.onError(NETWORK_IS_UNREACHABLE, MSG_NO_INTERNET_CONNECTION);
            } else if (error.getMessage().contains("Connection refused")) {
                connectionRefused(error);
                mOnComplete.onError(CONNECTION_REFUSED, MSG_NO_CONNECTION_WITH_SERVER);
            } else if (error.getCause() instanceof UnknownHostException) {
                unknownHost(error);
                mOnComplete.onError(UNKNOWN_HOST, MSG_UNKNOWN_HOST);
            } else {
                networkIsUnreachable(error);
                mOnComplete.onError(NETWORK_IS_UNREACHABLE, MSG_NO_INTERNET_CONNECTION);
            }
        } else if (error instanceof TimeoutError) {
            timeout(error);
            mOnComplete.onError(TIME_OUT, MSG_CONNECTION_TIME_OUT);
        } else if (error instanceof AuthFailureError) {
            authFailureError(error);
            mOnComplete.onError(AUTH_FAILURE_ERROR, error.getMessage());
        } else if (error instanceof ServerError) {
            serverError(error);
            mOnComplete.onError(UNKNOWN_HOST, MSG_UNKNOWN_HOST);
        } else if (error instanceof NetworkError) {
            networkError(error);
            mOnComplete.onError(NETWORK_ERROR, error.getMessage());
        } else if (error instanceof ParseError) {
            paresError(error);
            mOnComplete.onError(PARES_ERROR, error.getMessage());
        } else {
            error(error);
            mOnComplete.onError(ERROR, error.getMessage());
        }

    }

    protected void networkIsUnreachable(VolleyError error) {

    }

    protected void connectionRefused(VolleyError error) {
    }

    protected void unknownHost(VolleyError error) {
    }

    protected void timeout(VolleyError error) {
    }

    protected void authFailureError(VolleyError error) {

    }

    protected void serverError(VolleyError error) {
    }

    protected void networkError(VolleyError error) {
    }

    protected void paresError(VolleyError error) {
    }

    protected void error(VolleyError error) {

    }

}