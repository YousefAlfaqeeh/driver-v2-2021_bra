package schooldriver.trackware.com.school_bus_driver_android.gcmNotification.senderNotification;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

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
import schooldriver.trackware.com.school_bus_driver_android.app.Application;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.enums.CheckEnum;
import schooldriver.trackware.com.school_bus_driver_android.enums.TypeRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.geofence.mock.Constants;
import schooldriver.trackware.com.school_bus_driver_android.locationListener.ConfigNotify;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.PathUrl;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created by muradtrac on 3/26/17.
 */

public class SendNotificationGCM implements IRestCallBack, Constants {


//        public static boolean WITH_AMAZON = false;


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


    private Map<String, String> createAndroidNotification_ForDriverMessage(String locale, String nameStudent, String round_type, String msg, String avatar) {
        Map<String, String> androidBodyMessage = new HashMap<>();
        String title = createMessageTitle(locale, nameStudent);
        androidBodyMessage.put("title", title);
        androidBodyMessage.put("student_name", nameStudent);
        androidBodyMessage.put("round_type", round_type);
        androidBodyMessage.put("message", msg);
        androidBodyMessage.put("action", Constants.NOTIFICATION_ACTION_Driver);

        return androidBodyMessage;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public SendNotificationGCM(List<StudentBean> listStudentBean, String msg, int id) {
        List<Integer> listInteger = new ArrayList<>();
        for (StudentBean studentBean : listStudentBean) {
//            if (true) {

            Boolean isDropRound = studentBean.getTypeRoundEnum() == TypeRoundEnum.DROP_ROUND;
            Boolean isCheckOut = studentBean.getCheckEnum() == CheckEnum.CHECK_OUT;
            Boolean isShow = studentBean.isNoShow();
            String nameStudent = studentBean.getFirstNameStudent();
            String round_type = studentBean.getTypeRoundEnum() == TypeRoundEnum.DROP_ROUND ? "DROP_ROUND" : "PICK_ROUND";
            String avatar = studentBean.getAvatar();


            if (!isShow && isDropRound && !isCheckOut) {
                new UtilityDriver().logProject("SendNotificationGCM", "17.emergency :: " + msg);
//                    Map<String, String> androidBodyMessage = new HashMap<>();
//                    androidBodyMessage.put("title", "driver_");
//                    androidBodyMessage.put("student_name", nameStudent);
//                    androidBodyMessage.put("round_type", round_type);
//                    androidBodyMessage.put("message", msg);
//                    androidBodyMessage.put("avatar", avatar);
//                    String messageType = "";
//                    if (studentBean.getMobileStudentBean().getPlatform().contains("android")) {
//                        messageType = new JSONObject(mapBodyMessage).toString();
//                    } else {
//                        messageType = UtilityDriver.MESSAGE_IPHONE.replaceAll("MESSAGEMESSAGE", msg);
//                    }
//                String msgAfterPreDescription_Father =  /*createMessagePreDescription(studentBean.getMobileStudentBean().getFatherLocal(), nameStudent)+"\n"+*/msg;
//                Map<String, String> fatherGCM = createAndroidNotification_ForDriverMessage(studentBean.getMobileStudentBean().getFatherLocal(), nameStudent, round_type, msgAfterPreDescription_Father, avatar);
//                sendNotification(true, studentBean.getMobileStudentBean().getFatherToken(), studentBean.getMobileStudentBean().getFatherMessageType(fatherGCM, msgAfterPreDescription_Father), (Integer) studentBean.getId(), id, studentBean.getMobileStudentBean().getFatherPlatform(), studentBean.getAvatar(), studentBean.getMobileStudentBean().getFatherLocal(), studentBean.getNameStudent(), studentBean.getMobileStudentBean().getFatherId());
                /**/
                String msgAfterPreDescription_Mother =  /*createMessagePreDescription(studentBean.getMobileStudentBean().getMotherLocal(), nameStudent)+"\n"+*/msg;
                Map<String, String> motherGCM = createAndroidNotification_ForDriverMessage(studentBean.getMobileStudentBean().getMotherLocal(), nameStudent, round_type, msgAfterPreDescription_Mother, avatar);
                sendNotification(true, studentBean.getMobileStudentBean().getMotherToken(), studentBean.getMobileStudentBean().getMotherMessageType(motherGCM, msgAfterPreDescription_Mother), (Integer) studentBean.getId(), id, studentBean.getMobileStudentBean().getMotherPlatform(), studentBean.getAvatar(), studentBean.getMobileStudentBean().getMotherLocal(), studentBean.getNameStudent(), studentBean.getMobileStudentBean().getMotherId());
                /**/
                listInteger.add((Integer) studentBean.getId());
            } else if (!studentBean.isAbsent() && studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND && studentBean.getCheckEnum() != CheckEnum.CHECK_IN) {
                new UtilityDriver().logProject("SendNotificationGCM", "17.emergency :: " + msg);
//                    Map<String, String> androidBodyMessage = new HashMap<>();
//                    androidBodyMessage.put("title", "driver_");
//                    androidBodyMessage.put("student_name", nameStudent);
//                    androidBodyMessage.put("round_type", round_type);
//                    androidBodyMessage.put("message", msg);
//                    androidBodyMessage.put("avatar", avatar);

//                    String messageType = "";
//                    if (UtilityDriver.isEmptyString(studentBean.getMobileStudentBean().getPlatform())){
//                        return;
//                    }
//                    if (UtilityDriver.isEmptyString(studentBean.getMobileStudentBean().getPlatform())){
//                        return;
//                    }
//

//                    if (studentBean.getMobileStudentBean().getPlatform().contains("android")) {
//                        messageType = new JSONObject(mapBodyMessage).toString();
//                    } else {
//                        messageType = UtilityDriver.MESSAGE_IPHONE.replaceAll("MESSAGEMESSAGE", msg);
//                    }
//                String msgAfterPreDescription_Father =  /*createMessagePreDescription(studentBean.getMobileStudentBean().getFatherLocal(), nameStudent)+"\n"+*/msg;
//                Map<String, String> fatherGCM = createAndroidNotification_ForDriverMessage(studentBean.getMobileStudentBean().getFatherLocal(), nameStudent, round_type, msgAfterPreDescription_Father, avatar);
//                sendNotification(true, studentBean.getMobileStudentBean().getFatherToken(), studentBean.getMobileStudentBean().getFatherMessageType(fatherGCM, msgAfterPreDescription_Father), (Integer) studentBean.getId(), id, studentBean.getMobileStudentBean().getFatherPlatform(), studentBean.getAvatar(), studentBean.getMobileStudentBean().getFatherLocal(), studentBean.getNameStudent(), studentBean.getMobileStudentBean().getFatherId());
                Log.d("aaaaaaaaaaaaaaaaaaaaasssssssss", "dddddddd");
                /**/
                String msgAfterPreDescription_Mother =  /*createMessagePreDescription(studentBean.getMobileStudentBean().getMotherLocal(), nameStudent)+"\n"+*/msg;
                Map<String, String> motherGCM = createAndroidNotification_ForDriverMessage(studentBean.getMobileStudentBean().getFatherLocal(), nameStudent, round_type, msgAfterPreDescription_Mother, avatar);
                sendNotification(true, studentBean.getMobileStudentBean().getMotherToken(), studentBean.getMobileStudentBean().getMotherMessageType(motherGCM, msgAfterPreDescription_Mother), (Integer) studentBean.getId(), id, studentBean.getMobileStudentBean().getMotherPlatform(), studentBean.getAvatar(), studentBean.getMobileStudentBean().getMotherLocal(), studentBean.getNameStudent(), studentBean.getMobileStudentBean().getFatherId());
                listInteger.add((Integer) studentBean.getId());
            }
//            }
        }
        new ConfigNotify().sendMessageAdmin(listInteger, msg, id);
    }

    //    public SendNotificationGCM(StudentBean studentBean,String msg) {
//            sendNotification(studentBean.getMobileStudentBean().getFatherToken(),msg);
//            sendNotification(studentBean.getMobileStudentBean().getMotherToken(),msg);
//    }
    public SendNotificationGCM() {

    }


//    public void sendNotification(boolean isDriverMessage, final String token, final String msg, final int userID, final int roundID, final String platform, final String avatar, final String local, final String studentName) {
//        sendNotification( isDriverMessage,   token,   msg,   userID,   roundID,   platform,   avatar,   local,   studentName,   NOTIFICATION_ACTION_DRIVER);
//    }

    public void sendNotification(boolean isDriverMessage, final String token, final String msg, final int userID, final int roundID, final String platform, final String avatar, final String local, final String studentName, int parent_id) {

//        if (UtilityDriver.isEmptyString(token)) {
//            return;
//        }
        String title_;
        String action = NOTIFICATION_ACTION_Other;
        if (isDriverMessage) {
            action = NOTIFICATION_ACTION_Driver;
            title_ = createMessageTitle(local, studentName);
        } else {
            title_ = createNotificationTitle(local);
        }

//        if (platform.contains("ios"))
        title_ = title_.replaceAll("driver_", "").trim();

        final String title_withText = title_;
        final String finalAction = action;

//        this.token = token;
//        this.msg = msg;


//        System.err.println(msg+" @@@ "+token+ " userID:"+userID+" roundID:"+roundID+" SCHOOL_ID:"+Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.SCHOOL_ID)));

        Log.v("SEND....NOTIFICATION", new JSONObject(new HashMap() {{

            put("body", msg);
            put("parent_id", parent_id);
            put("action", finalAction);
            put("message", msg);
            put("title", title_withText);
            put("school_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.SCHOOL_ID)));
            put("round_id", roundID);

            put("user_id", userID);


//            put("endpoint_arn", token);

        }}) + " \n ");
//        if (token.equals("arn:aws:sns:eu-central-1:006260994575:endpoint/GCM/schools/60ba363b-9a43-3ede-b499-1ac3f9235d3d")) {
        callRestAPI(PathUrl.MAIN_URL + PathUrl.PUSH_NOTIFICATION
                ,
                new HashMap() {{

                    put("body", msg);
                    put("parent_id", parent_id);
                    put("action", finalAction);
                    put("message", msg);
                    put("title", title_withText);
                    put("school_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.SCHOOL_ID)));
                    put("round_id", roundID);

                    put("user_id", userID);

//                    int[] ids={1,2,3,4};
//                    put("user_ids",ids);

//                    }else {
//
//                        put("platform", platform);
//                        put("avatar", avatar);
//                        put("body", msg);
//                        put("endpoint_arn", token);
//                        put("parent_id", parent_id);
//                        put("action", finalAction);
//                        put("message", msg);
//                        put("title", title_withText);
//                        put("school_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.SCHOOL_ID)));
//                        put("round_id", roundID);
//                        put("user_id", userID);
//                    }


                }}
                ,
                EnumMethodApi.POST
                ,
                SendNotificationGCM.this
                ,
                EnumNameApi.PUSH_NOTIFICATION
                ,
                UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
                ,
                EnumTypeHeader.JSON
        );
//        }
//        if (UtilityDriver.isEmptyString(token)){
//            return;
//        }
//        Thread thread = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try  {
//                    try {
//
//
//
//                        new UtilityDriver().logProject("NOTIFICATION_STRT","10.sendNotification :: "+token+" :: "+msg);
//                        JSONObject jGcmData = new JSONObject();
//                        JSONObject jData = new JSONObject();
//                        jData.put("message", msg);
//                        jGcmData.put("to", token);
//                        jGcmData.put("data", jData);
//
//                        // Create connection to send GCM Message request.
//                        URL url = new URL("https://fcm.googleapis.com/fcm/send");
//                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                        conn.setRequestProperty("Authorization", "key=" + UtilityDriver.WEB_API_KEY_GCM);
//                        conn.setRequestProperty("Content-Type", "application/json");
//                        conn.setRequestMethod("POST");
//                        conn.setDoOutput(true);
//                        OutputStream outputStream = conn.getOutputStream();
//                        outputStream.write(jGcmData.toString().getBytes());
//                        InputStream inputStream = conn.getInputStream();
//                        System.out.println(inputStream.toString());
//                        System.out.println("Check your device/emulator for notification or logcat for " +
//                                "confirmation of the receipt of the GCM message.");
//                    } catch (Exception e) {
//                        System.out.println("Unable to send GCM message.");
//                        System.out.println("Please ensure that API_KEY has been replaced by the server " +
//                                "API key, and that the device's registration token is correct (if specified).");
//                        e.printStackTrace();
//                        new UtilityDriver().logProject("NOTIFICATION_EXEPTION","10.sendNotification :: "+token+" :: "+msg);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        thread.start();
    }

//    public String createMessagePreDescription(final String local, final String studentName) {
//
//        StringBuilder title = new StringBuilder();
//        String bus_number_text;
//        String student_name_text;
//        String stringBusNumber = UtilityDriver.getStringShared(UtilityDriver.BUS_NUMBER);
//        if (local.equals("ar")) {
//            bus_number_text = Application.getInstance().getString(R.string.bus_number_ar);
//            student_name_text = Application.getInstance().getString(R.string.student_name_ar);
//        } else {
//            bus_number_text = Application.getInstance().getString(R.string.bus_number_en);
//            student_name_text = Application.getInstance().getString(R.string.student_name_en);
//        }
//        /**/
//        title.append(bus_number_text);
//        title.append(stringBusNumber);
//        title.append("\n");
//        title.append(student_name_text);
//        title.append(studentName);
//        title.append("\n");
//        /**/
//        return title.toString();
//    }


    private String createMessageTitle(final String local, String studentName) {
        StringBuilder messageFromBus = new StringBuilder();
        String stringBusNumber = UtilityDriver.getStringShared(UtilityDriver.BUS_NUMBER);
        if (local != null && local.equals("ar")) {
            messageFromBus.append(Application.getInstance().getString(R.string.driver_msg_ar));
        } else {
            messageFromBus.append(Application.getInstance().getString(R.string.driver_msg_en));
        }
        messageFromBus.append(stringBusNumber);
        messageFromBus.append(" ,");
        messageFromBus.append(studentName);
        return messageFromBus.toString();
    }

    public static String createNotificationTitle(final String local) {
        String messageFromBus = "";
        if (local.equals("ar")) {
            messageFromBus = Application.getInstance().getString(R.string.driver_notification_ar);
        } else {
            messageFromBus = Application.getInstance().getString(R.string.driver_notification_en);
        }
        return messageFromBus;
    }


    @Override
    public void onRestCallBack(String response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
//        if (nameApiEnum == EnumNameApi.PUSH_NOTIFICATION){
//            System.err.println(new JSONObject(volleyBean.getMap())+" \n SEND....NOTIFICATION");
//            System.err.println(response+" \n SEND....NOTIFICATION");
//        }
    }

    @Override
    public void onRestCallBack(JSONObject response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

        if (nameApiEnum == EnumNameApi.PUSH_NOTIFICATION) {
//            Log.v("SEND....NOTIFICATION",new JSONObject(volleyBean.getMap())+" \n "+response);
//            Log.v("SEND....NOTIFICATION",response+" \n SEND....NOTIFICATION");
        }

    }

    @Override
    public void onRestCallBack(VolleyError volleyError, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
//        if (volleyError.getMessage().toString().contains("java.net.UnknownHost")){                 UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.internet_connection), mActivity.getString(R.string.missing_internet_error));             return;         }
        if (nameApiEnum == EnumNameApi.PUSH_NOTIFICATION) {
            callRestAPI(PathUrl.MAIN_URL + PathUrl.PUSH_NOTIFICATION
                    ,
                    volleyBean.getMap()
                    ,
                    EnumMethodApi.POST
                    ,
                    SendNotificationGCM.this
                    ,
                    EnumNameApi.PUSH_NOTIFICATION
                    ,
                    UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
                    ,
                    EnumTypeHeader.JSON
            );
//            System.err.println("Error" + volleyError.getMessage());
//            System.err.println(volleyBean.getUrl_path()+" @@@ "+"PUSH_NOTIFICATION_ERROR" + volleyError.getMessage()+" "+new JSONObject(volleyBean.getMap())+" "+new JSONObject(volleyBean.getMapHeader()));
        }
    }

    @Override
    public void onRestCallBack(NetworkResponse networkResponse, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

    }


}


