/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package schooldriver.trackware.com.school_bus_driver_android.gcmNotification.firebace;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import database.DAO;
import database.OpenHelper;
import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.bean.AbsentKidsBean;
import schooldriver.trackware.com.school_bus_driver_android.gcmNotification.NotificationObj;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.MyGson;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.MyNotificationHelper;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

//import com.firebase.jobdispatcher.FirebaseJobDispatcher;
//import com.firebase.jobdispatcher.GooglePlayDriver;
//import com.firebase.jobdispatcher.Job;
//import com.getsentry.raven.android.Raven;

public class MyFirebaseMessagingService_new extends FirebaseMessagingService {

    public static String FIREBASE_BROADCAST = "FIREBASE_BROADCAST";
    public static String ABSENT_RECEIVER = "ABSENT_RECEIVER";
    public static String SCHOOL_MESSAGE = "SCHOOL_MESSAGE";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


//        String message = null;
//        final Map<String, String> mapArray = remoteMessage.getData();
//        for (Map.Entry<String, String> entry : mapArray.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            System.err.println(key + "   " + value);
//            message = value;

//        }
        String message = "";
        try {
            message = remoteMessage.getData().get("json_data");
        } catch (Exception e) {
            message = "";
        }
        if (message == null) {
            return;
        }


        if (message.contains("absent")) {
            try {
                new UtilityDriver().logProject("onMessageReceived", "14.RECEIVED NOTI ::: " + message);
                // TODO: 4/20/17  Test
                JSONObject jsonObject = new JSONObject(message);
                final AbsentKidsBean absentKidsBean = new AbsentKidsBean();
                absentKidsBean.setId(jsonObject.getInt("student_id"));
                absentKidsBean.setName(jsonObject.getString("student_name"));
                absentKidsBean.setStatus(jsonObject.getString("status"));
                absentKidsBean.setRoundId(jsonObject.getString("round_id"));
                absentKidsBean.setReceivedDate(jsonObject.getString("date_time"));
                sendStudentAbsent(absentKidsBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (message.contains("action") && message.contains("title") && message.contains("message")) {


            NotificationObj notificationObj = MyGson.getGson().fromJson(message, new TypeToken<NotificationObj>() {
            }.getType());

            new MyNotificationHelper(this)
                    .activityOnClick(MainActivity.class)
                    .title(notificationObj.getTitle())
                    .description(notificationObj.getMessage())
                    .customImage(R.drawable.admin_msg)
//                    .customImageURL(notificationObj.getAvatar())
                    .show();
            boolean added = DAO.ImportantNotificationTable.add(OpenHelper.getDatabase(this), notificationObj);
            sendSchoolMessageBroadcast(notificationObj);
//            new Handler(getMainLooper()).postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    sendSchoolMessageBroadcast(notificationObj);
//                }
//            }, 400);

        } else {
            new UtilityDriver().logProject("onMessageReceived", "EMER ::: " + message);
            new MyNotificationHelper(this)
                    .activityOnClick(MainActivity.class)
                    .title("")
                    .description(message)
                    .show();


//            sendNotification(message, "", getPendingIntent());
        }
        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */

        // [END_EXCLUDE]
    }

//    private PendingIntent getPendingIntent() {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//        return pendingIntent;
//    }

//    private void sendNotification(String message, String title, PendingIntent pendingIntent) {
//
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri);
//        if (pendingIntent != null) {
//            notificationBuilder.setContentIntent(pendingIntent);
//        }
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }

    private void sendStudentAbsent(AbsentKidsBean absentKidsBean) {
        try {
            Intent intent = new Intent(FIREBASE_BROADCAST);
            intent.putExtra(ABSENT_RECEIVER, absentKidsBean);
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void sendSchoolMessageBroadcast(NotificationObj notificationObj) {
        try {
            Intent intent = new Intent(FIREBASE_BROADCAST);
            intent.putExtra(SCHOOL_MESSAGE, notificationObj);
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
