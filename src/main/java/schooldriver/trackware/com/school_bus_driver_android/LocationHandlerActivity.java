//package schooldriver.trackware.com.school_bus_driver_android;
//
//import android.location.Location;
//import android.os.Bundle;
//import android.os.PersistableBundle;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import io.nlopez.smartlocation.OnLocationUpdatedListener;
//import io.nlopez.smartlocation.SmartLocation;
//import io.nlopez.smartlocation.location.config.LocationParams;
//import schooldriver.trackware.com.school_bus_driver_android.API.ApiFacade;
//import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
//import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
//import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
//import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;
//import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;
//import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseActivity;
//
///**
// * Created by muradtrac on 3/6/17.
// */
//
//public class LocationHandlerActivity extends BaseActivity {
//
//    private final String LOCATION_TAG = "LOCATION_TAG";
//
//    public class LocationData {
//        public double speed = 0.0;
//        public double latitude = 0.0;
//        public double longitude = 0.0;
//    }
//
//    private void initSmartLocation() {
////        if (provider == null) {
////            provider = new LocationGooglePlayServicesProvider();
////            provider.setCheckLocationSettings(true);
////        }
//        if (isLocationAvailable()) {
//            getSmartLocation().location().continuous().config(LocationParams.NAVIGATION)//.location(provider)
//                    .start(new OnLocationUpdatedListener() {
//                        @Override
//                        public void onLocationUpdated(Location location) {
//                            getLocationData().getValue().latitude = location.getLatitude();
//                            getLocationData().getValue().longitude = location.getLongitude();
//                            getLocationData().getValue().speed = location.getSpeed() * 3.6 < 5 ? 0.0 : (location.getSpeed() * 3.6);
//                            /**/
//                            getLocationData().setValue(getLocationData().getValue());
//                            /**/
//                            Log.v(LOCATION_TAG, "---onLocationUpdated--- ");
//                            Log.v(LOCATION_TAG, "Latitude = " + getLocationData().getValue().latitude);
//                            Log.v(LOCATION_TAG, "Longitude = " + getLocationData().getValue().longitude);
//                            Log.v(LOCATION_TAG, "Speed = " + getLocationData().getValue().speed);
//                            Log.v(LOCATION_TAG, "------ ");
//                            /**/
//                        }
//                    });
//        } else {
//            Log.e(LOCATION_TAG, "Location Provider Not Available");
//
//        }
//    }
//
//
//    public boolean isLocationAvailable() {
//        try {
////            if (!getSmartLocation().location().state().isNetworkAvailable()) {
////                Log.v(LOCATION_TAG, "isNetworkAvailable = false");
////                return false;
////            }
//            if (!getSmartLocation().location().state().locationServicesEnabled()) {
//                Log.v(LOCATION_TAG, "locationServicesEnabled = false");
//                return false;
//            }
//            if (!getSmartLocation().location().state().isGpsAvailable()) {
//                Log.v(LOCATION_TAG, "isGpsAvailable = false");
//                return false;
//            }
//            return true;
//            /**/
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//
//        }
//        /**/
//
//    }
//
//    public SmartLocation getSmartLocation() {
//        return SmartLocation.with(this);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//
//    }
//
//}
