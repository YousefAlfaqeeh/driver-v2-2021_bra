package schooldriver.trackware.com.school_bus_driver_android.basePage;

import android.os.Bundle;
import android.os.PersistableBundle;

import java.util.HashMap;
import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.API.ApiFacade;
import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;
import schooldriver.trackware.com.school_bus_driver_android.AppUpdateCheckerActivity;

/**
 * Created by muradtrac on 3/6/17.
 */

public class BaseActivity extends AppUpdateCheckerActivity {


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
//        FirebaseApp.initializeApp(this);
//        lockOrientation();
    }

    protected void callRestAPI(
            String PATH_URL,
            HashMap params,
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


//    public  void lockOrientation() {
//        if (getResources().getConfiguration().orientation == Configuration. ORIENTATION_LANDSCAPE)
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        else
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//
//
////        Activity activity = this;
////        Display display = ((WindowManager) activity
////                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
////        int rotation = display.getRotation();
////        int tempOrientation = activity.getResources().getConfiguration().orientation;
////        int orientation = 0;
////        switch (tempOrientation) {
////            case Configuration.ORIENTATION_LANDSCAPE:
////                if (rotation == Surface.ROTATION_0
////                        || rotation == Surface.ROTATION_90)
////                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
////                else
////                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
////                break;
////            case Configuration.ORIENTATION_PORTRAIT:
////                if (rotation == Surface.ROTATION_0
////                        || rotation == Surface.ROTATION_270)
////                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
////                else
////                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
////        }
////        activity.setRequestedOrientation(orientation);
//    }

//    private void initBluetoothState() {
////        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
////        registerReceiver(mReceiver, filter);
//        checkBluetoothState();
//    }


//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            try {
//                final String action = intent.getAction();
//                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
//                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
//                            BluetoothAdapter.ERROR);
//                    switch (state) {
//                        case BluetoothAdapter.STATE_OFF:
//                            checkBluetoothState();
//                            break;
//                        case BluetoothAdapter.STATE_TURNING_OFF:
//                            break;
//                        case BluetoothAdapter.STATE_ON:
//                            checkBluetoothState();
//                            break;
//                        case BluetoothAdapter.STATE_TURNING_ON:
//                            break;
//                    }
//                }
//            } catch (Exception e) {
//                checkBluetoothState();
//            }
//
//
//        }
//    };

    @Override
    protected void onStart() {
        super.onStart();
//        initBluetoothState();
    }


}
