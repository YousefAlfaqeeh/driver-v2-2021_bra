package schooldriver.trackware.com.school_bus_driver_android.utilityDriver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import java.net.URL;
import java.util.Random;

import schooldriver.trackware.com.school_bus_driver_android.R;

public class MyNotificationHelper extends ContextWrapper {
    /**/
    private NotificationManager notifManager;
    private final int lockscreenVisibility = Notification.VISIBILITY_PRIVATE;
    private final int normalDefultIcon = R.mipmap.ic_launcher;
    private final int smallDefultIcon = R.mipmap.ic_launcher;
    ////////////////////////////////////
    ////////////////////////////////////
    /*for android 'Oreo' or plus */
    private final boolean lights = true;
    private final boolean vibration = true;
    private final int lightColor = Color.GREEN;
    private final String channelName = "channelName";
    private final String channelId = "channelId";
    ////////////////////////////////////
    ////////////////////////////////////
    private Uri customSoundUri = null;
    private NotificationCompat.Builder notificationBuilder;
    private int notificationId = 12121;
    private Uri soundUri = null;
    private String customImageUrl=null;
    private Intent clickIntent;
    private String notificationTitle = "";
    private String notificationDescription = "";
    private  int imageId=0;
    ////////////////////////////////////
    ////////////////////////////////////
    public MyNotificationHelper(Context base) {
        super(base);
        notificationBuilder = new NotificationCompat.Builder(context(), channelId);
        notifManager = (NotificationManager) context().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationId = generateRandomNotificationId();

    }

    public Context context() {
        return this;
    }

    public MyNotificationHelper customSound(int rawId) {
        try {
            soundUri = Uri.parse("android.resource://" + this.getPackageName() + "/" + rawId);
        } catch (Exception e) {
            e.printStackTrace();
            soundUri = null;
        }
        return this;
    }


    public MyNotificationHelper title(String notificationTitle) {
        this.notificationTitle = notificationTitle;
        return this;
    }


    public MyNotificationHelper description(String notificationDescription) {
        this.notificationDescription = notificationDescription;
        return this;
    }


    public MyNotificationHelper customImageURL(String customImageUrl) {
        this.customImageUrl = customImageUrl;
        return this;
    }

    public MyNotificationHelper customImage(int imageId) {
        this.imageId = imageId;
        return this;
    }


    public MyNotificationHelper activityOnClick(Class<?> classOpenOnClick) {
        Intent intent = new Intent(context(), classOpenOnClick);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        clickIntent = intent;
        return this;
    }

//    private PendingIntent createOnDismissedIntent(Class<?> broadcastReceiverClass) {
//        Intent intent = new Intent(this, broadcastReceiverClass);
//        intent.putExtra("broadcastReceiverClass", true);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context().getApplicationContext(), 111, intent, 0);
//        return pendingIntent;
//    }


    public void show() {
        /**/
        init_NotificationForOreoOrPlus();
        /**/
        init_Notification();
        /**/
        notifManager.notify(notificationId,  notificationBuilder.build());
    }


    private void init_NotificationForOreoOrPlus() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                /**/
                NotificationChannel mChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                if (soundUri != null)
                    mChannel.setSound(soundUri, new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_ALARM).build());
                mChannel.enableLights(lights);
                mChannel.enableVibration(vibration);
                mChannel.setLightColor(lightColor);
                mChannel.setLockscreenVisibility(lockscreenVisibility);
                ((NotificationManager) context().getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(mChannel);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void init_Notification() {
        try {
            /**/
            if (soundUri != null) {
                notificationBuilder.setSound(soundUri);
            }
            notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            PendingIntent pendingIntent = PendingIntent.getActivity(context(), notificationId, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setPriority(Notification.PRIORITY_MAX);
            notificationBuilder.setTicker(notificationTitle);
            if (imageId!=0){
                notificationBuilder.setSmallIcon(imageId);
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), imageId));
            }else {
                notificationBuilder.setSmallIcon(smallDefultIcon);
                notificationBuilder.setLargeIcon(getNotificationIcon());
            }

            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setContentTitle(notificationTitle).setStyle(new NotificationCompat.BigTextStyle().bigText(notificationTitle));
            notificationBuilder.setContentText(notificationDescription).setStyle(new NotificationCompat.BigTextStyle().bigText(notificationDescription));

        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    private Bitmap getNotificationIcon() {
        try {
            if (customImageUrl == null && customImageUrl.trim().equals(""))
                throw new Exception("");
            else
                return BitmapFactory.decodeStream(new URL(customImageUrl).openConnection().getInputStream());

        } catch (Exception e) {
            return BitmapFactory.decodeResource(context().getResources(), normalDefultIcon);
        }
    }

    private int generateRandomNotificationId() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }


}