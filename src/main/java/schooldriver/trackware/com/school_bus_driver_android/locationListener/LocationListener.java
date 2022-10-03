package schooldriver.trackware.com.school_bus_driver_android.locationListener;

/**
 * Created by muradtrac on 3/29/17.
 */

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Date;
import java.util.Locale;

import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
import schooldriver.trackware.com.school_bus_driver_android.dialog.SpeedDialog;
import schooldriver.trackware.com.school_bus_driver_android.enums.EnumConfigNotify;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.OnActionDoneListener;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.PathUrl;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class LocationListener {


    //    private static Activity mActivity;
    //    LocationHelper.LocationResult locationResult;
//    LocationHelper locationHelper;
    final String TAG = "MainActivity.java";
    private static Activity mActivity;
    ConfigNotify configNotify;
    public static int noSpeed, noBattery;
    public static Date dateStandstill;
//    public static boolean firstStop = true;

    protected void init() {

        try {
            if (MainActivity.mMap != null && MainActivity.mMap.getMyLocation() != null) {
//                MainActivity.latitudeMain = MainActivity.mMap.getMyLocation().getLatitude();
//                MainActivity.longitudeMain = MainActivity.mMap.getMyLocation().getLongitude();
                Location location = new Location("123");
                location.setLongitude(StaticValue.longitudeMain);
                location.setLatitude(StaticValue.latitudeMain);
                checkNearbyDistance(location);
//                checkNearbyDistance(location);
                if (UtilityDriver.getBooleanShared(UtilityDriver.SPEED_LIMIT_WATCH))
                    checkSpeed();
                if (UtilityDriver.getBooleanShared(UtilityDriver.STAND_STILL_WATCH))
                    checkStandStill(StaticValue.locationFromMap);
//                    checkStandStill(location);
                if (PathUrl.CHECKIN_TIMER_ON)
                    checkStopUntillCheckIn(StaticValue.locationFromMap);

                if (PathUrl.CHECKIN_TIMER_ON)
                    refreshLocationForNearByChecker(location);

                checkDistanceRound(location);
                checkDistanceRound(location);
                batteryLevel();
            }
        } catch (NullPointerException | OutOfMemoryError e) {

        }

//        this.locationResult = new LocationHelper.LocationResult() {
//            @Override
//            public void gotLocation(Location location) {
//                mActivity.registerReceiver(mBatInfoReceiver, new IntentFilter(
//                        Intent.ACTION_BATTERY_CHANGED));
//                //Got the location!
//                if (location != null) {
//
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
//
//                    checkSpeed(location);
//                    checkStandStill(location);
//                    checkNearbyDistance(location);
//                    checkDistanceRound(location);
//                    if (MainActivity.mMap.getMyLocation() == null) {
//                        MainActivity.latitude = latitude;
//                        MainActivity.longitude = longitude;
//                    }else{
//                        MainActivity.latitude = MainActivity.mMap.getMyLocation().getLatitude();
//                        MainActivity.longitude = MainActivity.mMap.getMyLocation().getLongitude();
//                    }
//
//
//
//                } else {
//                    Log.e(TAG, "Location is null.");
//                }
//
//            }
//
//        };

        // initialize our useful class,
//        this.locationHelper = new LocationHelper();
    }

    public static OnActionDoneListener<Location> nearByChecker = null;

    private void refreshLocationForNearByChecker(Location currentBusLocation) {
        Log.d("refreshLocationForNearByChecker", String.valueOf(currentBusLocation));
        try {
            if (nearByChecker == null)
                return;
            if (currentBusLocation == null)
                return;
            if (currentBusLocation.getLongitude() <= 0.0)
                return;
            if (currentBusLocation.getLatitude() <= 0.0)
                return;

            nearByChecker.OnActionDone(currentBusLocation);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkDistanceRound(Location location) {
        if (StaticValue.longitudeDistance != 0 || StaticValue.latitudeDistance != 0) {
            Location locationDistance = new Location("123");
            locationDistance.setLongitude(StaticValue.longitudeDistance);
            locationDistance.setLatitude(StaticValue.latitudeDistance);
            StaticValue.distance += UtilityDriver.distance(location.getLatitude(), location.getLongitude(), locationDistance.getLatitude(), locationDistance.getLongitude(), "M");

//            MainActivity.distance += Math.abs(location.distanceTo(locationDistance));
            StaticValue.latitudeDistance = location.getLatitude();
            StaticValue.longitudeDistance = location.getLongitude();
        } else {
            StaticValue.distance = 0;
        }
    }


    /*
    * public static void checkNearbyDistance(final Location location) {
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 10 seconds


                    if (RoundInfoFragment.roundBeanCheck != null) {
//                    if (RoundInfoFragment.roundBeanCheck.getListStudentBean().get(0).getTypeRoundEnum() == TypeRoundEnum.DROP_ROUND) {
//                        return;
//                    }
                        List<StudentBean> listStudentBean = RoundInfoFragment.roundBeanCheck.getListStudentBean();
                        Location locationStudent = null;
                        double distanceNearby = Double.parseDouble(UtilityDriver.getStringShared(UtilityDriver.SCHOOL_NEARBY_DISTANCE));
                        StudentBean studentBean = null;
                        for (StudentBean studentBeanNearby : listStudentBean) {
                            if (studentBeanNearby.getCheckEnum() != CheckEnum.CHECK_IN || studentBeanNearby.getTypeRoundEnum() == TypeRoundEnum.DROP_ROUND)
                                studentBean = studentBeanNearby;
                            break;

                        }
//                    UtilityDriver.showMessageDialog(mActivity,"NAME", studentBean.getNameStudent());
                        if (studentBean == null) {
                            return;
                        }
                        String value = "";
                        if (UtilityDriver.isEmptyString("" + studentBean.getId())) {
                            value = "021";
                        }
                        locationStudent = new Location(value);
                        locationStudent.setLatitude(studentBean.getLatitude());
                        locationStudent.setLongitude(studentBean.getLongitude());


                        boolean studentNearedBy = StaticValue.listNearby.contains(studentBean.getNameStudent());
                        if (!studentNearedBy) {

                            double busStudentCurrentDistance = UtilityDriver.distance(location.getLatitude(), location.getLongitude(), locationStudent.getLatitude(), locationStudent.getLongitude(), "M");
//                double distance = Math.abs(location.distanceTo(locationStudent));

                            boolean isNearBy = busStudentCurrentDistance <= distanceNearby;
                            if (isNearBy) {
//                    new SendNotificationGCM(studentBean,mActivity.getString(R.string.nearby_distance) + " "+distanceNearby + " M");
//                    for (String nearby : MainActivity.listNearby.) {
//                        if () {
//                            return;
//                        }
////                    }
                                String sendMessage = null;
                                sendMessage = UtilityDriver.getMessageNotification("near", studentBean.getTypeRoundEnum());
                                sendMessage = sendMessage.replaceAll("@student_name", studentBean.getNameStudent());
                                Map<String, String> mapBodyMessage = new HashMap<>();
                                mapBodyMessage.put("message", sendMessage);
                                mapBodyMessage.put("avatar", studentBean.getAvatar());
                                String messageType = "";
                                if (studentBean.getMobileStudentBean().getPlatform().contains("android")) {
                                    messageType = new JSONObject(mapBodyMessage).toString();
                                } else {
                                    messageType = UtilityDriver.MESSAGE_IPHONE.replaceAll("MESSAGEMESSAGE", sendMessage);
                                }
                                if (sendNotificationGCM == null)
                                    sendNotificationGCM = new SendNotificationGCM();

                                if (studentBean.getMobileStudentBean().isCheckInFather()) {
                                    StaticValue.listNearby.add(studentBean.getNameStudent());
//                                UtilityDriver.showMessageDialog(mActivity,"SEND", studentBean.getNameStudent());
                                    sendNotificationGCM.sendNotification(studentBean.getMobileStudentBean().getFatherToken(), messageType, (Integer) studentBean.getId(), (Integer) RoundInfoFragment.roundBean.getId(), studentBean.getMobileStudentBean().getPlatform(), studentBean.getAvatar());
                                }
                                if (studentBean.getMobileStudentBean().isCheckInMother()) {
                                    StaticValue.listNearby.add(studentBean.getNameStudent());
                                    sendNotificationGCM.sendNotification(studentBean.getMobileStudentBean().getMotherToken(), messageType, (Integer) studentBean.getId(), (Integer) RoundInfoFragment.roundBean.getId(), studentBean.getMobileStudentBean().getPlatform(), studentBean.getAvatar());
                                }
                            }

                        }
//            }
                    }

                }
            }, 200);

        } catch (NullPointerException | OutOfMemoryError e) {

        }
    }*/

    public static void checkNearbyDistance(final Location location) {
        try {
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
                    // Actions to do after 10 seconds


//                    if (RoundInfoFragment.roundBeanCheck != null) {
//                        Map<Integer, List<StudentBean>> map = RoundInfoFragment.roundBeanCheck.getMapStudentBean();
//                        if (map.size() == 0) {
//                            return;
//                        }
//                        List<StudentBean> listStudentBean = map.get((map.keySet().toArray())[0]);
////                        String value = (new ArrayList<StudentBean>(RoundInfoFragment.roundBeanCheck.getMapStudentBean().values())).get(0);
//
//                        Location locationStudent = null;
//                        double distanceNearby = Double.parseDouble(UtilityDriver.getStringShared(UtilityDriver.SCHOOL_NEARBY_DISTANCE));
//                        StudentBean studentBean = listStudentBean.get(0);
////                        for (StudentBean studentBeanNearby : listStudentBean) {
////                            if (studentBeanNearby.getCheckEnum() != CheckEnum.CHECK_IN || studentBeanNearby.getTypeRoundEnum() == TypeRoundEnum.DROP_ROUND)
////                                studentBean = studentBeanNearby;
////                            break;
////
////                        }
//////                    UtilityDriver.showMessageDialog(mActivity,"NAME", studentBean.getNameStudent());
//                        if (studentBean == null) {
//                            return;
//                        }
//                        String value = "";
//                        if (UtilityDriver.isEmptyString("" + studentBean.getId())) {
//                            value = "021";
//                        }
//                        locationStudent = new Location(value);
//                        locationStudent.setLatitude(studentBean.getLatitude());
//                        locationStudent.setLongitude(studentBean.getLongitude());
//
//
//                        boolean studentNearedBy = StaticValue.listNearby.contains(studentBean.getId());
//                        if (!studentNearedBy) {
//                            LocationListenerPres locationListenerPres = new LocationListenerPres(mActivity);
//
//                            double busStudentCurrentDistance = UtilityDriver.distance(location.getLatitude(), location.getLongitude(), locationStudent.getLatitude(), locationStudent.getLongitude(), "M");
//
//                            boolean isNearBy = busStudentCurrentDistance <= distanceNearby;
//
//                            if (isNearBy) {
//                                locationListenerPres.sendNearMessage(studentBean, "");

//                                map.remove((map.keySet().toArray())[0]);
//                                if (map != null)
//                                    RoundInfoFragment.roundBeanCheck.setMapStudentBean(map);
//                                if (map != null)
//                                    RoundInfoFragment.roundBean.setMapStudentBean(map);
//                                for (final StudentBean studentBeanGroup : listStudentBean) {
//                                    locationListenerPres.sendNearMessage( studentBean, "");
////                                    locationListenerPres.getKeyMap(studentBeanGroup, location, studentBean);
//
//
//                                }
//
//                            }
//
//                        }
////            }
//                    }
//
//                }
//            }, 200);

        } catch (Exception e) {

        }
    }


    private void checkStopUntillCheckIn(Location location) {
        if (StaticValue.locationFromMap == null) {
            return;
        }
        if (StaticValue.getCurrentSpeed() > 5) {
            removeCheckInStopTime();
            return;
        }
        saveStopTime();

    }


    private void checkStandStill(Location location) {
        if (StaticValue.locationFromMap == null) { // can't get location
            return;
        }
        if (UtilityDriver.getIntShared(UtilityDriver.STAND_STILL) <= 0) { // in case the admin set limit to 0 or less
            return;
        }
        if (StaticValue.getCurrentSpeed() > 5) { // is driver moving ? (ignore speed  less than 5m)
            removeDriverStoppTime(); // make sure there is no (stopped time) saved
            return;
        }
        /**/
        long difference = getTimeBetweenFirstStopAndNow();
        /**/
        if (difference == -1) { // the driver is not moving -> save the timer for first time
            saveDriverStoppTime();
        } else if (difference >= UtilityDriver.getIntShared(UtilityDriver.STAND_STILL)) { //  is the driver reach the limit time
//            if (firstStop) { // is this first time ?
            sendStandStillNotify(location);
            removeDriverStoppTime();
//                firstStop = false;
//            } else if (difference >= 10) { // he stopped more than 10 min
//                sendStandStillNotify(location);
//            }
        }


    }

    private void saveDriverStoppTime() {
        dateStandstill = new Date();
    }

    private void removeDriverStoppTime() {
//        firstStop = true;
        dateStandstill = null;
    }

    public long getTimeBetweenFirstStopAndNow() {
        if (dateStandstill == null) {
            return -1;
        } else {
            return UtilityDriver.differenceTime(new Date(), dateStandstill);
        }
    }

    private void sendStandStillNotify(Location location) {
        configNotify = new ConfigNotify(
                location.getLatitude() + "",
                location.getLongitude() + "",
                true + "",
                EnumConfigNotify.STAND_STILL,
                mActivity);
        StaticValue.SUM_NOTIFICATION++;
    }
//
//    private void checkStandStill(Location location) {
//
//
//        if (StaticValue.latitudeStandStill == 0 && StaticValue.longitudeStandStill == 0) {
//            StaticValue.longitudeStandStill = location.getLongitude();
//            StaticValue.latitudeStandStill = location.getLatitude();
//            dateStandstill = new Date();
//        } else {
//            Location locationStandStill = new Location("123");
//            locationStandStill.setLatitude(StaticValue.latitudeStandStill);
//            locationStandStill.setLongitude(StaticValue.longitudeStandStill);
//            double distance = UtilityDriver.distance(StaticValue.latitudeMain,
//                    StaticValue.longitudeMain,
//                    StaticValue.latitudeStandStill,
//                    StaticValue.longitudeStandStill, "M");
//            if (distance <= 10) {
//                if (UtilityDriver.differenceTime(new Date(), dateStandstill) >= UtilityDriver.getIntShared(UtilityDriver.STAND_STILL) && noStandStill == 0) {
//                    configNotify = new ConfigNotify(
//                            location.getLatitude() + "",
//                            location.getLongitude() + "",
//                            true + "",
//                            EnumConfigNotify.STAND_STILL,
//                            mActivity);
//                    StaticValue.SUM_NOTIFICATION++;
//                    dateStandstill = new Date();
//                    noStandStill = 1;
//                } else if (UtilityDriver.differenceTime(new Date(), dateStandstill) >= 10) {
//                    configNotify = new ConfigNotify(
//                            location.getLatitude() + "",
//                            location.getLongitude() + "",
//                            true + "",
//                            EnumConfigNotify.STAND_STILL,
//                            mActivity);
//                    StaticValue.SUM_NOTIFICATION++;
//                    dateStandstill = new Date();
//                }
//            }
//        }
//
//    }


    private void checkSpeed() {
//        FL.d("=============");
//        FL.d("checkSpeed() called");
        Location location = new Location("123");
        location.setLongitude(StaticValue.longitudeMain);
        location.setLatitude(StaticValue.latitudeMain);

        if (StaticValue.longitudeSpeed > 0 && StaticValue.latitudeSpeed > 0) { //1
//            FL.d("StaticValue.longitudeSpeed = "+StaticValue.longitudeSpeed);
//            FL.d("StaticValue.latitudeSpeed = "+StaticValue.latitudeSpeed);
//            Toast.makeText(mActivity,"speed count  = "+noSpeed,Toast.LENGTH_SHORT).show();
//            FL.d("noSpeed ="+noSpeed);
            if (noSpeed >= 4) {
                Location locationSecond = new Location("123");
                locationSecond.setLongitude(StaticValue.longitudeSpeed);
                locationSecond.setLatitude(StaticValue.latitudeSpeed);
//                int speedValue = speed(location, locationSecond);
                int speedValue = (int) StaticValue.getCurrentSpeed();
//                int speedValue = 40;

//                Toast.makeText(mActivity,"speed Value  = "+speedValue,Toast.LENGTH_SHORT).show();
//                if (location.hasSpeed()){
//                    Log.v("GNewSpeed",""+location.getCurrentSpeed());
//                    Toast.makeText(mActivity,"G Speed = "+location.getCurrentSpeed(),Toast.LENGTH_SHORT).show();
//                }else {
//                    Log.v("G No Speed","G No Speed");
//                    Toast.makeText(mActivity,"G No Speed",Toast.LENGTH_SHORT).show();
//
//                }
                if (UtilityDriver.getIntShared(UtilityDriver.SPEED) <= speedValue /*&& location.hasSpeed()*/) {
//                    if (UtilityDriver.getIntShared(UtilityDriver.SPEED) <= speedValue && UtilityDriver.getIntShared(UtilityDriver.SPEED) >170 ) {
                    SpeedDialog.getInstanceAndShow(mActivity, UtilityDriver.getIntShared(UtilityDriver.SPEED), speedValue);
                    StaticValue.locationFromMap = null;
//                    }

                    configNotify = new ConfigNotify(
                            location.getLatitude() + "",
                            location.getLongitude() + "",
                            speedValue + "",
                            EnumConfigNotify.SPEED,
                            mActivity);
                    StaticValue.SUM_NOTIFICATION++;
                }
                StaticValue.latitudeSpeed = location.getLatitude();
                StaticValue.longitudeSpeed = location.getLongitude();
                noSpeed = 0;
//                FL.d("=============");
            } else {
                noSpeed++;
//                FL.d("=============");
            }
        } else {
            if (location.getLatitude() != 0 && location.getLongitude() != 0) {
                StaticValue.latitudeSpeed = location.getLatitude();
                StaticValue.longitudeSpeed = location.getLongitude();
            }
        }
    }


    public void onStart(Activity mActivity) {
        this.mActivity = mActivity;
        init();
//        locationHelper.getLocation(mActivity, locationResult);

    }

//    public void onStop() {
//        locationHelper.stopGettingLocationUpdates();
//    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        //When Event is published, onReceive method is called
        public void onReceive(Context c, Intent i) {
            //Get Battery %
            int batteryLow = UtilityDriver.getIntShared(UtilityDriver.BATTERY_LOW);

            int level = i.getIntExtra("level", 0);
            if (level <= batteryLow) {

                configNotify = new ConfigNotify(
                        StaticValue.latitudeMain + "",
                        StaticValue.longitudeMain + "",
                        String.valueOf(level),
                        EnumConfigNotify.BATTERY, mActivity);

            }
        }


    };

    private void batteryLevel() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int rawLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level = -1;
                if (rawLevel >= 0 && scale > 0) {
                    level = (rawLevel * 100) / scale;
                    int batteryLow = UtilityDriver.getIntShared(UtilityDriver.BATTERY_LOW);

                    if (level == batteryLow) {
                        if (noBattery == 0) {
                            configNotify = new ConfigNotify(
                                    StaticValue.latitudeMain + "",
                                    StaticValue.longitudeMain + "",
                                    String.valueOf(level),
                                    EnumConfigNotify.BATTERY, mActivity);
                            noBattery++;
                        } else if (noBattery >= 36) {
                            configNotify = new ConfigNotify(
                                    StaticValue.latitudeMain + "",
                                    StaticValue.longitudeMain + "",
                                    String.valueOf(level),
                                    EnumConfigNotify.BATTERY, mActivity);
                            noBattery = 1;
                        } else {
                            noBattery++;
                        }
                    }

                }
//                batterLevel.setText("Battery Level Remaining: " + level + "%");
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        mActivity.registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    public int speed(Location locationFirst, Location locationSecond) {
        double distance = UtilityDriver.distance
                (locationFirst.getLatitude(),
                        locationFirst.getLongitude(),
                        locationSecond.getLatitude(),
                        locationSecond.getLongitude(),
                        "K"
                );
//        double speed = (distance) / (0.018055556);
//        double speed =   (distance) / (0.004);
        double speed = (distance) / (0.008055556);
        System.err.println(distance + "  location1 : " + locationFirst.getLatitude() + " : " + locationFirst.getLongitude() + "  " +
                "location2 : " + locationSecond.getLatitude() + " : " + locationSecond.getLongitude() + " SPEED::: " + speed);
//        Raven.capture("13.STATUS SPEED ::: "+(int)distance+" SPEED "+(int) speed);
        return (int) speed;
    }


    private Long checkInStopTime = null;


    private void saveStopTime() {
        if (checkInStopTime != null)
            return;

        checkInStopTime = new Date().getTime();
    }

    public void removeCheckInStopTime() {
        checkInStopTime = null;
    }

    public String timeBetweenStopAndNow() {


        if (!PathUrl.CHECKIN_TIMER_ON)
            return null;

        if (checkInStopTime == null)
            return null;


        StringBuilder timeFormat = new StringBuilder();

        try {
            long nowTime = System.currentTimeMillis();
            long timeUntillNow = nowTime - checkInStopTime;

            String hours = String.format(Locale.US, "%02d", ((timeUntillNow / (1000 * 60 * 60)) % 24));
            String minutes = String.format(Locale.US, "%02d", ((timeUntillNow / (1000 * 60)) % 60));
            String seconds = String.format(Locale.US, "%02d", (timeUntillNow / 1000) % 60);
            /**/
            timeFormat.append(hours);
            timeFormat.append(":");
            timeFormat.append(minutes);
            timeFormat.append(":");
            timeFormat.append(seconds);
            /**/
            return timeFormat.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";

        }


//        return timeUntillNow;
    }
}

