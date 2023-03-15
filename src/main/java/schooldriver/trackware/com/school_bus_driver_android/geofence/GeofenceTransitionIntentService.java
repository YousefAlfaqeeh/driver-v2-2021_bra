package schooldriver.trackware.com.school_bus_driver_android.geofence;

/**
 * Created by muradtrac on 4/3/17.
 */


import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.API.ApiFacade;
import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.bean.GeofenceBean;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.PathUrl;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;


public class GeofenceTransitionIntentService extends IntentService implements IRestCallBack{
    private static final String TAG = GeofenceTransitionIntentService.class.getSimpleName();

    public GeofenceTransitionIntentService() {
        super(TAG);
    }

    public static PendingIntent newPendingIntent(Context context, List<LatLng> points) {
        Intent intent = new Intent(context, GeofenceTransitionIntentService.class);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    String typeCheck = "";
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            Log.d(TAG, "intent is null.");
            return;
        }

        GeofencingEvent event = GeofencingEvent.fromIntent(intent);


        if (event.hasError()) {
            Log.e(TAG, "Error code: " + event.getErrorCode());
            return;
        }

//        intent.setClass(this, LocationUpdateService.class);
        int transition = event.getGeofenceTransition();
        GeofenceBean geofenceBean = getGeofenceDetails(transition, event);

        switch (transition) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Log.d(TAG, "Entered the geofence.");
//                startService(intent);
                typeCheck = "in";
                break;

            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Log.d(TAG, "Dwelling on the geofence.");
//                startService(intent);
                break;

            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Log.d(TAG, "Exited the geofence.");
//                stopService(intent);
                typeCheck = "out";
                break;

            default:
                Log.d(TAG, "Invalid transition: " + transition);
                break;
        }
        final String details[] = geofenceBean.getDetais().split(":")[0].split("@@");
//        yousef
//        callRestAPI(PathUrl.MAIN_URL + PathUrl.NOTIFY
//                ,
//                new HashMap<String, String>() {{
//                    put("name", "check");
//                    put("move", typeCheck);
//                    put("geo_type", "place");
//                    put("geo_id", details[0]);
//                    put("geo_name", details[1]);
//                    put("lat", ""+ StaticValue.latitudeMain);
//                    put("long", ""+ StaticValue.longitudeMain);
//                }}
//                ,
//                EnumMethodApi.POST
//                ,
//                GeofenceTransitionIntentService.this
//                ,
//                EnumNameApi.ROUND_LIST
//                ,
//                UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
//                ,
//                EnumTypeHeader.JSON
//        );
    }

    private GeofenceBean getGeofenceDetails(int geofenceTransition, GeofencingEvent geofencingEvent) {
        // Get the geofences that were triggered. A single event can trigger multiple geofences.
        List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

        // Get the transition details as a String.
        GeofenceBean geofenceTransitionDetails = getGeofenceTransitionDetails(
                this,
                geofenceTransition,
                triggeringGeofences
        );
//        triggeringGeofences.get(0).getRequestId();
        // Send notification and log the transition details.
        return geofenceTransitionDetails;
    }

    String geofenceTransitionString;

    private GeofenceBean getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList triggeringGeofencesIdsList = new ArrayList();
        String id = null;
        for (Geofence geofence : triggeringGeofences) {
            id = geofence.getRequestId();
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);
        String details = geofenceTransitionString + ": " + triggeringGeofencesIdsString;


        return new GeofenceBean(id, details);
    }

    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }

    @Override
    public void onRestCallBack(String response, EnumNameApi nameApiEnum,ApiRequest volleyBean) {

    }

    @Override
    public void onRestCallBack(JSONObject response, EnumNameApi nameApiEnum,ApiRequest volleyBean) {

    }

    @Override
    public void onRestCallBack(VolleyError volleyError, EnumNameApi nameApiEnum,ApiRequest volleyBean) {
//        if (volleyError.getMessage().toString().contains("java.net.UnknownHost")){                 UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.internet_connection), mActivity.getString(R.string.missing_internet_error));             return;         }

    }

    @Override
    public void onRestCallBack(NetworkResponse networkResponse, EnumNameApi nameApiEnum,ApiRequest volleyBean) {

    }

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