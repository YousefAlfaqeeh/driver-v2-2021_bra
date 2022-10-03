package schooldriver.trackware.com.school_bus_driver_android.APIs_new.ListenersAndInterFaces;

import android.util.Log;
import android.view.View;

import schooldriver.trackware.com.school_bus_driver_android.app.Application;


public abstract class DefaultOnComplete<Type> implements OnApiComplete<Type> {

    public static final int SHOW_ERROR_MESSAGE_CODE = 500;
    public static final int NULL_RESPONSE = 501;
    private View view;

    public DefaultOnComplete(View view) {
        this.view = view;
    }

    @Override
    public void onError(int errorCode, String errorMessage) {
        Application.logEvents("DefaultOnComplete", "DefaultOnComplete - onError",errorMessage,"error code is "+ errorCode);
        Log.v("onError", "onError");
        Log.v("errorMessage", errorMessage);

        if (view == null) {
            return;
        }
        switch (errorCode) {
            case ServiceResponseErrorListener.NETWORK_IS_UNREACHABLE:
            case ServiceResponseErrorListener.CONNECTION_REFUSED:
            case ServiceResponseErrorListener.UNKNOWN_HOST:
//                UtilityParent.showMessageDialog(view.context(), view.context().getString(R.string.internet_connection), errorMessage);
                break;
            case 404:

//                UtilityParent.showMessageDialog(view.context(), view.context().getString(R.string.api_send_error), errorMessage);

                break;
            case SHOW_ERROR_MESSAGE_CODE:
//                UtilityParent.showMessageDialog(view.context(), view.context().getString(R.string.api_send_error), errorMessage);

                break;
            case NULL_RESPONSE:
//                UtilityParent.showMessageDialog(view.context(), view.context().getString(R.string.api_send_error), errorMessage);

                break;
        }
    }
}
