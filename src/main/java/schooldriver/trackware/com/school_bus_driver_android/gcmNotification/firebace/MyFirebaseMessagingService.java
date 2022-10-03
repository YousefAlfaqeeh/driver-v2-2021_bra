///**
// * Copyright 2016 Google Inc. All Rights Reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package schooldriver.trackware.com.school_bus_driver_android.gcmNotification.firebace;
//
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Build;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//import com.google.gson.annotations.SerializedName;
//import com.google.gson.reflect.TypeToken;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
//import schooldriver.trackware.com.school_bus_driver_android.R;
//import schooldriver.trackware.com.school_bus_driver_android.bean.AbsentKidsBean;
//import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
//import schooldriver.trackware.com.school_bus_driver_android.enums.CheckEnum;
//import schooldriver.trackware.com.school_bus_driver_android.enums.TypeRoundEnum;
//import schooldriver.trackware.com.school_bus_driver_android.fragment.round.RoundPresenter;
//import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.RoundInfoFragment;
//import schooldriver.trackware.com.school_bus_driver_android.gcmNotification.NotificationObj;
//import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.MyGson;
//import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
//import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;
//
////import com.firebase.jobdispatcher.FirebaseJobDispatcher;
////import com.firebase.jobdispatcher.GooglePlayDriver;
////import com.firebase.jobdispatcher.Job;
////import com.getsentry.raven.android.Raven;
//
//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//
//    private static final String TAG = "MyFirebaseMsgService";
//    public static String ABSENT_RECEIVER = "ABSENT_RECEIVER";
//    /**
//     * Called when message is received.
//     *
//     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
//     */
//    // [START receive_message]
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//
//        // Check if message contains a data payload.
////        if (remoteMessage.getData().size() > 0) {
////            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
////
////            if (/* Check if data needs to be processed by long running job */ true) {
////                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
////                scheduleJob();
////            } else {
////                // Handle message within 10 seconds
////                handleNow();
////            }
////
////        }
//
////        // Check if message contains a notification payload.
////        if (remoteMessage.getNotification() != null) {
////            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
////        }
////
////        String message = remoteMessage.getData().get("message");
////        Log.d(TAG, "Message: " + message);
//        String message = null;
//        final Map<String, String> mapArray = remoteMessage.getData();
//        for (Map.Entry<String, String> entry : mapArray.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            System.err.println(key+"   "+value);
//            message = value;
//
//        }
//        //remoteMessage.getData().get("message");
//
////        new UtilityParent().sentryLog("onMessageReceived  @@@@@@",message+" :: "+remoteMessage.toString());
////            new Handler(getMainLooper()).postDelayed(new Runnable() {
////                @Override
////                public void run() {
//        if (message == null){
//            return;
//        }
//
//
//        if (message.contains("\"status\":\"absent\"")) {
//            try {
//                new UtilityDriver().logProject("onMessageReceived","14.RECEIVED NOTI ::: "+message);
//                // TODO: 4/20/17  Test
//                JSONObject jsonObject = new JSONObject(message);
//                final AbsentKidsBean absentKidsBean = new AbsentKidsBean();
//                absentKidsBean.setId(jsonObject.getInt("student_id"));
//                absentKidsBean.setName(jsonObject.getString("student_name"));
//                absentKidsBean.setStatus(jsonObject.getString("status"));
//                absentKidsBean.setRoundId(jsonObject.getString("round_id"));
//                absentKidsBean.setReceivedDate(jsonObject.getString("date_time"));
////                Log.d(TAG, "MainActivity.mActivity: " + StaticValue.mActivity);
//                sendStudentAbsent(absentKidsBean);
////                if (StaticValue.mActivity == null){
////                    sendNotification(absentKidsBean.getName(), absentKidsBean.getStatus(), getPendingIntent());
////                }else {
////                    if (StaticValue.mActivity!=null)
////                        StaticValue.mActivity.runOnUiThread(new Runnable() {
////                            public void run() {
////                                UtilityDriver.showMessageDialog(StaticValue.mActivity,absentKidsBean.getStatus(),absentKidsBean.getName());
////                                if (RoundInfoFragment.roundInfoStart!=null){
////                                    List<StudentBean> listStudentBean = new ArrayList<>();
////                                    for (StudentBean studentBean :RoundInfoFragment.roundBean.getListStudentBean()){
////                                        boolean value =(studentBean.getId()+"").contains(""+absentKidsBean.getId());
////                                        if (value) {
////                                            studentBean.setAbsent(true);
////                                            StaticValue.SUM_NOTIFICATION++;
////                                        }
////                                        listStudentBean.add(studentBean);
////                                    }
////                                    listStudentBean = swapListAll(listStudentBean);
////                                    RoundInfoFragment.roundInfoStart.setListAdapters(listStudentBean);
////                                }
////                            }
////                        });
////                    new RoundPresenter().callApiGetRoundList(StaticValue.typeRoundEnum);
////                }
//            }catch(JSONException e){
//                e.printStackTrace();
//            }
//        }else if (message.contains("action") && message.contains("avatar") &&message.contains("title") &&message.contains("message")){
//
//
//            NotificationObj notificationObj = MyGson
//                    .getGson().fromJson(message, new TypeToken<NotificationObj>() {
//            }.getType());
//
//        }
//
//        else{
//            new UtilityDriver().logProject("onMessageReceived","EMER ::: "+message);
//            sendNotification(message,"",getPendingIntent());
//        }
//        // [START_EXCLUDE]
//        /**
//         * Production applications would usually process the message here.
//         * Eg: - Syncing with server.
//         *     - Store message in local database.
//         *     - Update UI.
//         */
//
//        /**
//         * In some cases it may be useful to show a notification indicating to the user
//         * that a message was received.
//         */
//
//        // [END_EXCLUDE]
//    }
//    // [END receive_message]
//
//    /**
//     * Create and show a simple notification containing the received GCM message.
//     *
//     */
//    public static List<StudentBean> swapListAll(List<StudentBean> listStudentBean) {
//
//        List<StudentBean> newListStudentBean = new ArrayList<>();
//        List<StudentBean> firstListStudentBean = new ArrayList<>();
//        List<StudentBean> lastListStudentBean = new ArrayList<>();
//
//        for (StudentBean studentBean : listStudentBean) {
//            if (studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) { //////
//                if (studentBean.isAbsent() == true) {
//                    lastListStudentBean.add(studentBean);
//                }
//            } else {
//                if (studentBean.isNoShow() == true) {
//                    lastListStudentBean.add(studentBean);
//                }
//            }
//            if (studentBean.getCheckEnum() == CheckEnum.CHECK_OUT) {
//                lastListStudentBean.add(studentBean);
//            } else  if (studentBean.getCheckEnum() == CheckEnum.CHECK_IN) {
//                lastListStudentBean.add(studentBean);
//            } else {
//                if (studentBean.isAbsent() == false)
//                    if (studentBean.isNoShow() == false)
//                        firstListStudentBean.add(studentBean);
//            }
//        }
//
//
//
//
//        newListStudentBean.addAll(firstListStudentBean);
//        newListStudentBean.addAll(lastListStudentBean);
//
//        return newListStudentBean;
//    }
//    private PendingIntent getPendingIntent(){
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//        return pendingIntent;
//    }
//    private void sendNotification(String message,String title,PendingIntent pendingIntent) {
//
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                ;
//        if (pendingIntent!=null){
//            notificationBuilder.setContentIntent(pendingIntent);
//        }
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }
//    // [END receive_message]
//
//
//
//
//    /**
//     * Handle time allotted to BroadcastReceivers.
//     */
//    private void handleNow() {
//        Log.d(TAG, "Short lived task is done.");
//    }
//
//    private void sendStudentAbsent(AbsentKidsBean absentKidsBean) {
//        try {
//            Intent intent = new Intent(ABSENT_RECEIVER);
//            intent.putExtra(ABSENT_RECEIVER, absentKidsBean);
//            sendBroadcast(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//}
