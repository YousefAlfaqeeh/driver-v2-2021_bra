//package beacon;
//
//import android.app.Service;
//import android.bluetooth.BluetoothAdapter;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.IBinder;
//import android.util.Log;
//
//import com.minew.beaconplus.sdk.MTCentralManager;
//import com.minew.beaconplus.sdk.MTFrameHandler;
//import com.minew.beaconplus.sdk.MTPeripheral;
//import com.minew.beaconplus.sdk.enums.BluetoothState;
//import com.minew.beaconplus.sdk.interfaces.MTCentralManagerListener;
//import com.minew.beaconplus.sdk.interfaces.OnBluetoothStateChangedListener;
//
//import org.altbeacon.beacon.BeaconManager;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
//import schooldriver.trackware.com.school_bus_driver_android.bean.RoundBean;
//import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
//
//
//public class BeaconService extends Service {
//    /**/
//    public static String BEACON_SERVICE_SIGNAL = "BEACON_SERVICE_SIGNAL";
//    /**/
//    public static String CLEAR_LIST = "CLEAR_LIST";
//    public static String ADD_THIS_ROUNDE = "ADD_THIS_ROUNDE";
//    //    public static String REST_SCAN = "REST_SCAN";
//    /**/
//    public static String STUDENT_IN = "STUDENT_IN";
//    public static String STUDENT_OUT = "STUDENT_OUT";
//    /**/
//    private BeaconManager beaconManager;
//    /**/
//    private ArrayList<MTPeripheral> beaconsList = new ArrayList<>();
//    private MTCentralManager mtCentralManager;
//    private RoundBean roundBean;
//    private HashSet<StudentBean> inStudentList = new HashSet<>();
//    private HashSet<StudentBean> outStudentList = new HashSet<>();
//    private Handler studentsq = new Handler();
//    private boolean hold = true;
//
//    /**/
//    @Override
//    public void onCreate() {
//        super.onCreate();
////        startBeaconManager();
//
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
////        try {
////            hold=true;
////            if (intent.hasExtra(ADD_THIS_ROUNDE)) {
////                beaconsList.clear();
////                roundBean = intent.getParcelableExtra(ADD_THIS_ROUNDE);
////
////                startBeaconManager();
////            } else if (intent.hasExtra(CLEAR_LIST)) {      //clear round list
////                beaconsList.clear();
////                roundBean = null;
////            }
////
////
//////            mtCentralManager.stopScan();
//////            mtCentralManager.stopService();
//////            else if (intent.hasExtra(REST_SCAN)) {
//////                beaconsList.clear();
//////                inStudentList.clear();
//////                outStudentList.clear();
//////                mtCentralManager.stopScan();
//////                mtCentralManager.stopService();
//////            }
////
////        try {
////
////            new Handler().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    hold=false;
////                }
////            },6000);
////        } catch (Exception e) {
////
////        }
////        inStudentList.clear();
////        outStudentList.clear();
////        mtCentralManager.clear();
////        } catch (Exception e) {
//
////        }
//        return Service.START_STICKY;
//    }
//
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
////    boolean finishAddingIn = true;
//
//    private void sendStudentIn(StudentBean studentBean) {
//        if (hold)
//            return;
//
//
//        try {
//            Intent intent = new Intent(BEACON_SERVICE_SIGNAL);
//            intent.putExtra(STUDENT_IN, studentBean.getId());
//            sendBroadcast(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
////        new Handler().postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                finishAddingIn=true;
////            }
////        }, 500);
//    }
//
//    //    boolean finishAddingOut = true;
//    private void sendStudentOut(StudentBean studentBean) {
//        if (hold)
//            return;
//
//        try {
//            Intent intent = new Intent(BEACON_SERVICE_SIGNAL);
//            intent.putExtra(STUDENT_OUT, studentBean.getId());
//            sendBroadcast(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        new Handler().postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                finishAddingOut=true;
////            }
////        }, 500);
//    }
//
//    private void startBeaconManager() {
//        try {
////            if (mtCentralManager!=null){
////
////                mtCentralManager=null;
////            }
//
//        } catch (Exception e) {
//
//        }
//
//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        if (!mBluetoothAdapter.isEnabled()) {
//            return;
//        }
//
//        mtCentralManager = MTCentralManager.getInstance(this);
//        mtCentralManager.startService();
//        mtCentralManager.setBluetoothChangedListener(new OnBluetoothStateChangedListener() {
//            @Override
//            public void onStateChanged(BluetoothState state) {
//                Boolean b = true;
//            }
//        });
//        mtCentralManager.startScan();
//
//        mtCentralManager.setMTCentralManagerListener(new MTCentralManagerListener() {
//            @Override
//            public void onScanedPeripheral(final List<MTPeripheral> peripherals) {
//                beaconsList.clear();
////                HashSet<String> inRangeMacList = new HashSet<>();
////                HashSet<StudentBean> studentsNotInRange = new HashSet<>();
////                HashSet<StudentBean> studentsInRange = new HashSet<>();
//                Log.v("-------------", "-------------");
//                for (MTPeripheral mtPeripheral : peripherals) {
////                    studentsNotInRange.clear();
////                    studentsInRange.clear();
//                    // get FrameHandler of a device.
//                    beaconsList.add(mtPeripheral);
//                    MTFrameHandler mtFrameHandler = mtPeripheral.mMTFrameHandler;
//                    String mac = mtFrameHandler.getMac();       //mac address of device
//                    int battery = mtFrameHandler.getBattery();  //battery
//                    int distance = mtFrameHandler.getRssi();
//                    StringBuilder sb = new StringBuilder();
//                    sb.append("Mac : " + mac);
//                    sb.append("-");
//                    sb.append("Battery : " + battery);
//                    sb.append("-");
//                    sb.append("distance: " + distance);
//
//
////                    if (distance > -65) {
//                    if (roundBean != null && roundBean.getListStudentBean() != null) {
//                        inStudentList.add(findStudentByMac(mac));
//                    }
////                    }
//
//
//                    /**/
//                    Log.v("Beacon", "" + sb.toString());
//
//
//                    // To Do check student if hase same mac then
//                    // send student
//
//
//                }
//                /**/
////                outStudentList.clear();
////                inStudentList.clear();
//                if (roundBean != null && roundBean.getListStudentBean() != null) {
//                    outStudentList.addAll(roundBean.getListStudentBean());
//                    outStudentList.removeAll(inStudentList);
//                /**/
//
//                    for (StudentBean studentBean : inStudentList) {
//                        sendStudentIn(studentBean);
//                    }
//                    for (StudentBean studentBean : outStudentList) {
//                        sendStudentOut(studentBean);
//                    }
//
//                    outStudentList.clear();
//                    inStudentList.clear();
//
//
//                }
//
//
//            }
//        });
//    }
//
//
//    private StudentBean findStudentByMac(String mac) {
//        if (roundBean != null && roundBean.getListStudentBean() != null) {
//            for (int i = 0; i < roundBean.getListStudentBean().size(); i++) {
//                if (mac.equals(roundBean.getListStudentBean().get(i).getMacAdress())) {
//                    return roundBean.getListStudentBean().get(i);
//                }
//
//            }
//        }
//        return null;
//    }
////    private void stopBeaconManager() {
////        try {
////            mtCentralManager.stopScan();
////            mtCentralManager.stopService();
////            mtCentralManager.clear();
////            mtCentralManager = null;
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////
////    }
//
////    @Override
////    public boolean stopService(Intent name) {
////        stopBeaconManager();
////        return super.stopService(name);
////    }
//}
//
