package schooldriver.trackware.com.school_bus_driver_android.locationListener;

import android.app.Activity;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import database.CheckInOut;
import database.DAO;
import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;
import schooldriver.trackware.com.school_bus_driver_android.app.Application;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BasePresenter;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.enums.StatusRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.RoundInfoFragment;
import schooldriver.trackware.com.school_bus_driver_android.gcmNotification.senderNotification.SendNotificationGCM;
import schooldriver.trackware.com.school_bus_driver_android.geofence.mock.Constants;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.DateTools;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;


/**
 * Created by muradtrac on 11/7/17.
 */

public class LocationListenerPres extends BasePresenter implements IRestCallBack{
    SendNotificationGCM sendNotificationGCM = null;
    public LocationListenerPres(Activity mActivity) {
        this.mActivity = mActivity;
        sendNotificationGCM = new SendNotificationGCM();
    }

//    public void getKeyMap(final StudentBean studentBeanGroup, Location location, StudentBean studentBean) {
//
//        String PATH_URL = "http://maps.googleapis.com/maps/api/directions/json?origin="+location.getLatitude()+","+location.getLongitude()+"&destination="+studentBean.getLatitude()+","+studentBean.getLongitude()+"&sensor=false";
//        callRestAPI(PATH_URL
//                ,
//                new HashMap() {{
//                    put("name", studentBeanGroup);
//                }
//                }
//                ,
//                EnumMethodApi.GET
//                ,
//                LocationListenerPres.this
//                ,
//                EnumNameApi.LOCATION_TIME
//                ,
//                UtilityDriver.typeHeaderMap(EnumTypeHeader.EMPTY, true)
//                ,
//                EnumTypeHeader.JSON
//        );
//    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onRestCallBack(String response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
//        String time = "";
//        if (nameApiEnum == EnumNameApi.LOCATION_TIME) {
//            if (response.contains("legs")) {
//                try {
//
//                    JSONArray jsonArray = new JSONObject(response).getJSONArray("routes");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//                        JSONObject jsonObject2 = jsonObject1.getJSONArray("legs").getJSONObject(0);
//                        time = jsonObject2.getJSONObject("duration").getString("text");
//                    }
//                } catch (JSONException e) {
//                    System.err.println(e.getMessage().toString()+ "2SSSSSS");
//                    e.printStackTrace();
//                }
//            }
//
//            sendNearMessage((StudentBean) volleyBean.getMap().get("name"), ""/*time*/);
//        }
    }

    public void sendNearMessage(StudentBean studentBeanGroup, String roundID) {
        if (studentBeanGroup.isAbsent()) {
            return;
        }
        if (studentBeanGroup.isNoShow()) {
            return;
        }
//        if (studentBeanGroup.getMobileStudentBean().isCheckInFather()){
//            return;
//        }
//        if (studentBeanGroup.getMobileStudentBean().isCheckInMother()){
//            return;
//        }

        HashMap map =  new HashMap() {{
            put("round_id", roundID );
            put("student_id", studentBeanGroup.getId());
            put("datetime", DateTools.Formats.DATE_FORMAT_GMT.format(new Date()));
            put("status", Constants.NOTIFICATION_ACTION_Near);
            put("lat",  StaticValue.latitudeMain+"");
            put("long",  StaticValue.longitudeMain+"");
        }};

        CheckInOut checkInOut = new CheckInOut("", "", new JSONObject(map).toString());
        DAO.addCheck(Application.database, checkInOut);

        }
//        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    public void sendNearMessage(StudentBean studentBeanGroup, String time) {
//
//
//
////        String messageType = "";
////        if (studentBeanGroup.getMobileStudentBean().getPlatform().contains("android")) {
////            messageType = new JSONObject(mapBodyMessage).toString();
////        } else {
//
////            messageType = UtilityDriver.MESSAGE_IPHONE.replaceAll("MESSAGEMESSAGE", sendMessage);
////        }
//        if (sendNotificationGCM == null)
//            sendNotificationGCM = new SendNotificationGCM();
//        if (studentBeanGroup.isAbsent()) {
//            return;
//        }
//        if (studentBeanGroup.isNoShow()) {
//            return;
//        }
//
//        if (studentBeanGroup.getMobileStudentBean().isCheckInFather()  && studentBeanGroup.getMobileStudentBean().getFatherId()!=-1 ) {
//            String sendMessage = UtilityDriver.getMessageNotification("near", studentBeanGroup.getTypeRoundEnum(),studentBeanGroup.getMobileStudentBean().getFatherLocal());
//
//            String splitValue = ".";
////        sendMessage = sendMessage.replaceAll("@student_name", studentBeanGroup.getNameStudent());
//            sendMessage = sendMessage.replaceAll(STUDENT_NAME_API_FORMAT, studentBeanGroup.getFirstNameStudent());
////        sendMessage = sendMessage.replaceAll("@", "");
////                                    sendMessage = sendMessage.replace(splitValue, splitValue + "\n");
//            Map<String, String> mapBodyMessage = new HashMap<>();
//            mapBodyMessage.put("message", sendMessage + " " + time);
////            mapBodyMessage.put("type", "near");
////            mapBodyMessage.put("avatar", studentBeanGroup.getAvatar());
//            mapBodyMessage.put("title", SendNotificationGCM.createNotificationTitle(studentBeanGroup.getMobileStudentBean().getFatherLocal()));
//            mapBodyMessage.put("action", Constants.NOTIFICATION_ACTION_Near);
//            StaticValue.listNearby.add(studentBeanGroup.getFirstNameStudent());
////                                UtilityDriver.showMessageDialog(mActivity,"SEND", studentBeanGroup.getNameStudent());
////            sendNotificationGCM.sendNotification(studentBeanGroup.getMobileStudentBean().getFatherToken(), messageType, (Integer) studentBeanGroup.getId(), (Integer) RoundInfoFragment.roundBean.getId(), studentBeanGroup.getMobileStudentBean().getPlatform(), studentBeanGroup.getAvatar());
//            sendNotificationGCM.sendNotification(false,studentBeanGroup.getMobileStudentBean().getFatherToken(), studentBeanGroup.getMobileStudentBean().getFatherMessageType(mapBodyMessage,sendMessage), (Integer) studentBeanGroup.getId(), (Integer) RoundInfoFragment.roundBean.getId(), studentBeanGroup.getMobileStudentBean().getFatherPlatform(), studentBeanGroup.getAvatar(), studentBeanGroup.getMobileStudentBean().getFatherLocal(),studentBeanGroup.getNameStudent(),studentBeanGroup.getMobileStudentBean().getFatherId());
//
//        }
//        if (studentBeanGroup.getMobileStudentBean().isCheckInMother() && studentBeanGroup.getMobileStudentBean().getMotherId()!=-1) {
//
//            String sendMessage = null;
//            sendMessage = UtilityDriver.getMessageNotification("near", studentBeanGroup.getTypeRoundEnum(),studentBeanGroup.getMobileStudentBean().getMotherLocal());
//
//            String splitValue = ".";
////        sendMessage = sendMessage.replaceAll("@student_name", studentBeanGroup.getNameStudent());
//            sendMessage = sendMessage.replaceAll(STUDENT_NAME_API_FORMAT, studentBeanGroup.getFirstNameStudent());
////        sendMessage = sendMessage.replaceAll("@", "");
////                                    sendMessage = sendMessage.replace(splitValue, splitValue + "\n");
//            Map<String, String> mapBodyMessage = new HashMap<>();
//            mapBodyMessage.put("message", sendMessage + " " + time);
////            mapBodyMessage.put("type", "near");
////            mapBodyMessage.put("avatar", studentBeanGroup.getAvatar());
//            mapBodyMessage.put("title", SendNotificationGCM.createNotificationTitle(studentBeanGroup.getMobileStudentBean().getMotherLocal()));
//            mapBodyMessage.put("action", Constants.NOTIFICATION_ACTION_Near);
//
//
//            StaticValue.listNearby.add(studentBeanGroup.getFirstNameStudent());
//            sendNotificationGCM.sendNotification(false,studentBeanGroup.getMobileStudentBean().getMotherToken(), studentBeanGroup.getMobileStudentBean().getMotherMessageType(mapBodyMessage,sendMessage), (Integer) studentBeanGroup.getId(), (Integer) RoundInfoFragment.roundBean.getId(), studentBeanGroup.getMobileStudentBean().getMotherPlatform(), studentBeanGroup.getAvatar(), studentBeanGroup.getMobileStudentBean().getMotherLocal(),studentBeanGroup.getNameStudent(),studentBeanGroup.getMobileStudentBean().getMotherId());
//
////            sendNotificationGCM.sendNotification(studentBeanGroup.getMobileStudentBean().getMotherToken(), messageType, (Integer) studentBeanGroup.getId(), (Integer) RoundInfoFragment.roundBean.getId(), studentBeanGroup.getMobileStudentBean().getPlatform(), studentBeanGroup.getAvatar());
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onRestCallBack(JSONObject response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
//        String time = "";

//        if (nameApiEnum == EnumNameApi.LOCATION_TIME) {
//            if (response.toString().contains("legs")) {
//                try {
//
//                    JSONArray jsonArray = response.getJSONArray("routes");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//                        JSONObject jsonObject2 = jsonObject1.getJSONArray("legs").getJSONObject(0);
//                        time = jsonObject2.getJSONObject("duration").getString("text");
//                    }
//                } catch (JSONException e) {
//                    System.err.println(e.getMessage().toString()+ "2SSSSSS");
//                    e.printStackTrace();
//                }
//            }
//
//            sendNearMessage((StudentBean) volleyBean.getMap().get("name"), ""/* time*/);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onRestCallBack(VolleyError volleyError, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
//        if (nameApiEnum == EnumNameApi.LOCATION_TIME) {
//            System.err.println(""+ "3SSSSSS");
//            sendNearMessage((StudentBean) volleyBean.getMap().get("name"), "");
//        }
    }

    @Override
    public void onRestCallBack(NetworkResponse networkResponse, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

    }
}
