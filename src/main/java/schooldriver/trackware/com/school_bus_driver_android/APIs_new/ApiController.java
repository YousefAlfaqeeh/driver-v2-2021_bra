package schooldriver.trackware.com.school_bus_driver_android.APIs_new;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import database.CheckInOut;
import database.DAO;
import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ListenersAndInterFaces.OnApiComplete;
import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ListenersAndInterFaces.RequestHeader;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.app.Application;
import schooldriver.trackware.com.school_bus_driver_android.bean.ApplicationVersionJson;
import schooldriver.trackware.com.school_bus_driver_android.bean.ArriveAlarmRequest;
import schooldriver.trackware.com.school_bus_driver_android.bean.ReorderedRequest;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.geofence.mock.Constants;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.DateTools;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.PathUrl;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;


public class ApiController {

    public static final String TAG = ApiController.class.getSimpleName();
    private static final int TIME = 1000 * 60 * 30;
    private final static String PLATFORM = "android";
    private final static String USER_TYPE = "teacher";


    //////////////
    private static void sendRequest(Context context, Request request) {
        Log.e(TAG, "url: " + request.getUrl());
        ((RequestHeader) request).addHeader("locale", UtilityDriver.getStringShared(UtilityDriver.LANGUAGE));
        if (UtilityDriver.getStringShared(UtilityDriver.AUTH) == null || UtilityDriver.getStringShared(UtilityDriver.AUTH).equals("")) {
            Log.v("authorization", " authorization = null or empty");
        } else {
            ((RequestHeader) request).addHeader("Authorization", UtilityDriver.getStringShared(UtilityDriver.AUTH));
        }
        ServiceRequestQueue.getInstance(context).add(request);
    }


    //////////////


    //    public static void register(Context context,
//                                String secret_token,
//                                String pin,
//                                String email,
//                                final OnApiComplete<String> onComplete) {
//
//        VolleyRequest<String> request = new VolleyRequest<String>(
//                context,
//                HttpMethod.POST,
//                REGISTER,
//                new TypeToken<String>() {
//                }.getType(),
//                onComplete
//        );
//        RegisterRequest reqObject = new RegisterRequest();
//        reqObject.setSecretToken(secret_token);
//        reqObject.setPin(pin);
//        reqObject.setEmail(email);
//        request.setData(reqObject);
//        /**/
//        UtilDialogs.ProcessingDialog.show(context, null);
//        sendRequest(context, request);
//    }
//
//
//    public static void logIn(Context context,
//                             String secret_token,
//                             String pin,
//                             String mobile_token,
//                             final OnApiComplete<String> onComplete) {
//
//        VolleyRequest<String> request = new VolleyRequest<String>(
//                context,
//                HttpMethod.POST,
//                LOGIN,
//                new TypeToken<String>() {
//                }.getType(),
//                onComplete
//        );
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setPlatform(PLATFORM);
//        loginRequest.setUserType(USER_TYPE);
//        loginRequest.setSecretToken(secret_token);
//        loginRequest.setPin(pin);
//        loginRequest.setMobileToken(mobile_token);
//        request.setData(loginRequest);
//        /**/
//        UtilDialogs.ProcessingDialog.show(context, null);
//        sendRequest(context, request);
//    }
//
//
//    public static void getClassRoomsWithSections(Context context, final OnApiComplete<List<ClassRoomsWithSection>> onComplete) {
//        VolleyRequest<List<ClassRoomsWithSection>> request = new VolleyRequest<>(
//                context,
//                HttpMethod.GET,
//                Urls.GET_CLASS_ROOMS_WITH_SECTIONS,
//                new TypeToken<List<ClassRoomsWithSection>>() {
//                }.getType(),
//                onComplete
//        );
////        UtilDialogs.ProcessingDialog.show(context, null);
//        sendRequest(context, request);
//    }
//
//
//    public static void getStudentsForGrade(Context context,
//                                           ArrayList<Section_Grade> sectionGradeList,
//                                           final OnApiComplete<List<Student>> onComplete) {
//
//        VolleyRequest<List<Student>> request = new VolleyRequest<List<Student>>(
//                context,
//                HttpMethod.POST,
//                GET_STUDENTS_FOR_GRADE,
//                new TypeToken<List<Student>>() {
//                }.getType(),
//                onComplete
//        );
//        request.setData(sectionGradeList);
////        UtilDialogs.ProcessingDialog.show(context, null);
//        sendRequest(context, request);
//    }
//
//
//    public static void createClassRoom(Context context,
//                                       ArrayList<Section_Grade> sectionGradeList,
//                                       final OnApiComplete<List<TeacherClass>> onComplete) {
//
//        VolleyRequest<List<TeacherClass>> request = new VolleyRequest<>(
//                context,
//                HttpMethod.POST,
//                CREATE_CLASS_ROOM,
//                new TypeToken<List<TeacherClass>>() {
//                }.getType(),
//                onComplete
//        );
//        request.setData(sectionGradeList);
////        UtilDialogs.ProcessingDialog.show(context, null);
//        sendRequest(context, request);
//    }
//
//
//    public static void getTeacherClasses(Context context, Integer iD, final OnApiComplete<List<TeacherClass>> onComplete) {
//        String url = Urls.GET_TEACHER_CLASSES;
//        if (iD != null)
//            url = Urls.GET_TEACHER_CLASSES + "?cid=" + iD;
//
//        VolleyRequest<List<TeacherClass>> request = new VolleyRequest<>(
//                context,
//                HttpMethod.GET,
//                url,
//                new TypeToken<List<TeacherClass>>() {
//                }.getType(),
//                onComplete
//        );
////        UtilDialogs.ProcessingDialog.show(context, null);
//        sendRequest(context, request);
//    }
//
//
//    public static void deleteClass(Context context,
//                                   DeleteClassRequest deleteClassRequest,
//                                   final OnApiComplete<Object> onComplete) {
//
//        VolleyRequest<Object> request = new VolleyRequest<>(
//                context,
//                HttpMethod.POST,
//                DELETE_CLASSES,
//                new TypeToken<Object>() {
//                }.getType(),
//                onComplete
//        );
//        request.setData(deleteClassRequest);
////        UtilDialogs.ProcessingDialog.show(context, null);
//        sendRequest(context, request);
//    }
//
//


//    public static void sendGCMForMother(Context context, StudentBean studentBean, int roundId, Map<String, String> mapBodyMessage, String sendMessage, String action, int motherId) {
//        /**/
//        String motherToken = studentBean.getMobileStudentBean().getMotherToken();
//        String motherMessageType = studentBean.getMobileStudentBean().getMotherMessageType(mapBodyMessage, sendMessage);
//        String motherPlatform = studentBean.getMobileStudentBean().getMotherPlatform();
//        String motherlocale = studentBean.getMobileStudentBean().getMotherLocal();
//        /**/
//        Integer studentid = (Integer) studentBean.getId();
//        String avatar = studentBean.getAvatar();
//        /**/
//        sendGCM(context, motherToken, motherMessageType, studentid, roundId, motherPlatform, avatar, motherlocale, action, motherId);
//        /**/
//    }

//    public static void sendGCMForFather(Context context, StudentBean studentBean, int roundId, Map<String, String> mapBodyMessage, String sendMessage, String action, int fatherId) {
//        /**/
//        String fatherToken = studentBean.getMobileStudentBean().getFatherToken();
//        String fatherMessageType = studentBean.getMobileStudentBean().getFatherMessageType(mapBodyMessage, sendMessage);
//        String fatherPlatform = studentBean.getMobileStudentBean().getFatherPlatform();
//        String fatherlocale = studentBean.getMobileStudentBean().getFatherLocal();
//        /**/
//        Integer studentid = (Integer) studentBean.getId();
//        String avatar = studentBean.getAvatar();
//        /**/
//        sendGCM(context, fatherToken, fatherMessageType, studentid, roundId, fatherPlatform, avatar, fatherlocale, action, fatherId);
//        /**/
//    }

//    public static void sendGCM(Context context, final String token, final String msg, final int userID, final int roundID, final String platform, final String avatar, String locale, String action, int parent_id) {
//
//        final OnApiComplete<Object> onComplete = new OnApiComplete<Object>() {
//            @Override
//            public void onSuccess(Object o) {
//                Log.v("sendGCM -- onSuccess", o.toString());
//            }
//
//            @Override
//            public void onError(int errorCode, String errorMessage) {
//                Application.logEvents("sendGCM", "ApiController - sendGCM", errorMessage);
//                Log.v("sendGCM -- onError", errorMessage.toString());
//            }
//        };
//
//        /**/
//        VolleyRequest<Object> request = new VolleyRequest<Object>(
//                context,
//                HttpMethod.POST,
//                PathUrl.MAIN_URL + PathUrl.PUSH_NOTIFICATION,
//                new TypeToken<JsonObject>() {
//                }.getType(),
//                onComplete
//        );
//        if (action == null) {
//            action = Constants.NOTIFICATION_ACTION_Other;
//        }
//        /**/
//        JsonObject jsonObject = new JsonObject();
//
//
//        jsonObject.addProperty("platform", platform);
//        jsonObject.addProperty("message", msg);
//        jsonObject.addProperty("school_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.SCHOOL_ID)));
//        jsonObject.addProperty("round_id", roundID);
//        jsonObject.addProperty("user_id", userID);
//        jsonObject.addProperty("action", action);
//
////        if (platform.contains("ios")) {
////        jsonObject.addProperty("avatar", avatar);
//        jsonObject.addProperty("body", msg);
//
//
////        if (platform.contains("ios")) {
//            jsonObject.addProperty("endpoint_arn", token);
////        } else {
//            jsonObject.addProperty("parent_id", parent_id);
////        }
//
//        /**/
////        if (ios) {
////        jsonObject.addProperty("avatar", avatar);
//        jsonObject.addProperty("body", msg);
//        if (locale.equals("ar"))
//            jsonObject.addProperty("title", Application.getInstance().getString(R.string.driver_notification_ar));
//        else
//            jsonObject.addProperty("title", Application.getInstance().getString(R.string.driver_notification_en));
////    }
//
//
//        request.setData(jsonObject);
//        /**/
//        sendRequest(context, request);
//    }


    public static void getRoundList(Context context,
                                    String type, final OnApiComplete<Object> onComplete) {
        /**/
        VolleyRequest<Object> request = new VolleyRequest<Object>(
                context,
                HttpMethod.POST,
                PathUrl.MAIN_URL + PathUrl.ROUND_LIST,
                new TypeToken<JsonObject>() {
                }.getType(),
                onComplete
        );
        /**/
        /**/
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("round_type", type);
        jsonObject.addProperty("bus_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.BUS_ID)));
        request.setData(jsonObject);
        /**/
        sendRequest(context, request);
    }


    public static void getStudents_List(Context context, int round_id, final OnApiComplete<Object> onComplete) {
        /**/
        VolleyRequest<Object> request = new VolleyRequest<Object>(
                context,
                HttpMethod.GET,
                PathUrl.MAIN_URL + PathUrl.STUDENTS_LIST + round_id,
                new TypeToken<JsonObject>() {
                }.getType(),
                onComplete
        );
        /**/
        /**/
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("round_type", type);
//        jsonObject.addProperty("bus_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.BUS_ID)));
//        request.setData(jsonObject);
        /**/
        sendRequest(context, request);
    }


    public static void getNotification(Context context,
                                       final OnApiComplete<Object> onComplete) {

        /**/
        VolleyRequest<Object> request = new VolleyRequest<Object>(
                context,
                HttpMethod.POST,
                PathUrl.MAIN_URL + PathUrl.RECENT_NOTIFICATION,
                new TypeToken<JsonObject>() {
                }.getType(),
                onComplete
        );
        /**/
        /**/
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("day_count", 2);
        request.setData(jsonObject);
        /**/
        sendRequest(context, request);
    }


//    public static void setStudentBusCheck(Context context, final int studentID, final String status, final int roundId, final boolean overwrite, final String reason, String timeUntilCheckIn) {
//         setStudentBusCheck(context, studentID, status, roundId, overwrite, reason, null);
//    }

    public static void setStudentBusCheck(Context context, final int studentID, final String status, final int roundId, final boolean overwrite, final String reason, String timeUntilCheckIn) {
        try {
            final OnApiComplete<Object> onComplete = new OnApiComplete<Object>() {
                @Override
                public void onSuccess(Object o) {

                }

                @Override
                public void onError(int errorCode, String errorMessage) {

                }
            };
            /**/
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("day_count", 2);
            jsonObject.addProperty("lat", StaticValue.latitudeMain);
            jsonObject.addProperty("long", StaticValue.longitudeMain);
            jsonObject.addProperty("student_id", studentID);
            jsonObject.addProperty("round_id", roundId);
            jsonObject.addProperty("status", status);
            jsonObject.addProperty("datetime", DateTools.Formats.DATE_FORMAT_GMT.format(new Date()));
            jsonObject.addProperty("overwrite", overwrite);
            if (reason != null)
                jsonObject.addProperty("reason", reason);

            jsonObject.addProperty("waiting_minutes", timeUntilCheckIn);

            /**/

//            if (!UtilityDriver.isNetworkAvailable(StaticValue.mActivity)) {
            CheckInOut checkInOut = new CheckInOut("", "", jsonObject.toString());
            DAO.addCheck(Application.database, checkInOut);
//                return false;
//            }

            /**/
//            VolleyRequest<Object> request = new VolleyRequest<Object>(
//                    context,
//                    HttpMethod.POST,
//                    PathUrl.MAIN_URL + PathUrl.STUDENT_BUS_CHECK,
//                    new TypeToken<JsonObject>() {
//                    }.getType(),
//                    onComplete
//            );
            /**/
//            request.setData(jsonObject);
//            /**/
//            sendRequest(context, request);

//            return true;
        } catch (Exception e) {
            e.printStackTrace();
//            return false;
        }


    }


//    public static void busLocation(Context context, int round_id) {
//        /**/
//        int store_order = UtilityDriver.getIntShared(UtilityDriver.STORE_ORDER) + 1;
//        UtilityDriver.setIntShared(UtilityDriver.STORE_ORDER, store_order);
//        /**/
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("long", StaticValue.longitudeMain);
//        jsonObject.addProperty("lat", StaticValue.latitudeMain);
//        jsonObject.addProperty("bus_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.BUS_ID)));
//        jsonObject.addProperty("school_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.SCHOOL_ID)));
//        jsonObject.addProperty("round_id", round_id);
//        jsonObject.addProperty("store_order", store_order);
//        /**/
//        OnApiComplete<Object> onComplete = new OnApiComplete<Object>() {
//            @Override
//            public void onSuccess(Object response) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response.toString());
//                    if (response.toString().contains("\"status\":\"ok\"")) {
//                        StaticValue.BUS_LOCATION_TYPE = false;
//                    } else {
//                        LonLat lonLat = new LonLat("", "", jsonObject.toString()/*,StaticValue.getCurrentSpeed()*/);
//                        DAO.addLonLat(Application.database, lonLat);
//                        StaticValue.BUS_LOCATION_TYPE = false;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    onError(0, "");
//
//                }
//
//
//            }
//
//            @Override
//            public void onError(int errorCode, String errorMessage) {
//
//            }
//        };
//
//        /**/
//        VolleyRequest<Object> request = new VolleyRequest<Object>(
//                context,
//                HttpMethod.POST,
//                PathUrl.MAIN_URL + PathUrl.BUS_LOCATION,
//                new TypeToken<JsonObject>() {
//                }.getType(),
//                onComplete
//        );
//        /**/
//        /**/
//
//        request.setData(jsonObject);
//        /**/
//        sendRequest(context, request);
//    }

    public static void startRound(Context context, int roundId, final OnApiComplete<Object> onComplete) {
        /**/
        final int distance = (int) UtilityDriver.distance(StaticValue.latitudeMain, StaticValue.longitudeMain, StaticValue.latitudeStartRound, StaticValue.longitudeStartRound, "M");
        /**/

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("round_id", roundId);
        jsonObject.addProperty("datetime", DateTools.Formats.DATE_FORMAT_GMT.format(new Date()));
        jsonObject.addProperty("status", "start");
        jsonObject.addProperty("lat", StaticValue.latitudeMain + "");
        jsonObject.addProperty("long", StaticValue.longitudeMain + "");
        jsonObject.addProperty("distance", distance);
        /**/
        StaticValue.latitudeDistance = StaticValue.latitudeMain + 0;
        StaticValue.longitudeDistance = StaticValue.longitudeMain + 0;
        /**/
        VolleyRequest<Object> request = new VolleyRequest<Object>(
                context,
                HttpMethod.POST,
                PathUrl.MAIN_URL + PathUrl.SET_ROUND_STATUS,
                new TypeToken<JsonObject>() {
                }.getType(),
                onComplete
        );
        /**/
        /**/

        request.setData(jsonObject);
        /**/
        sendRequest(context, request);
    }

    public static void endRound(Context context, int roundId, final OnApiComplete<Object> onComplete) {
        /**/
        final int distance = (int) UtilityDriver.distance(StaticValue.latitudeMain, StaticValue.longitudeMain, StaticValue.latitudeStartRound, StaticValue.longitudeStartRound, "M");
        /**/
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("round_id", roundId);
        jsonObject.addProperty("datetime", DateTools.Formats.DATE_FORMAT_GMT.format(new Date()));
        jsonObject.addProperty("status", "end");
        jsonObject.addProperty("lat", StaticValue.latitudeMain + "");
        jsonObject.addProperty("long", StaticValue.longitudeMain + "");
        jsonObject.addProperty("distance", distance);
        /**/
//        if (!UtilityDriver.isNetworkAvailable(StaticValue.mActivity)) {
//            CheckInOut checkInOut = new CheckInOut("", "", jsonObject.toString());
//            DAO.addCheck(Application.database, checkInOut);
//            return;
//        }
        /**/
        VolleyRequest<Object> request = new VolleyRequest<Object>(
                context,
                HttpMethod.POST,
                PathUrl.MAIN_URL + PathUrl.SET_ROUND_STATUS,
                new TypeToken<JsonObject>() {
                }.getType(),
                onComplete
        );
        /**/
        /**/

        request.setData(jsonObject);
        /**/
        sendRequest(context, request);
    }

    public static void cancelRound(Context context, int roundId, String reason, final OnApiComplete<Object> onComplete) {
        /**/
        final int distance = (int) UtilityDriver.distance(StaticValue.latitudeMain, StaticValue.longitudeMain, StaticValue.latitudeStartRound, StaticValue.longitudeStartRound, "M");
        /**/
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("round_id", roundId);
        jsonObject.addProperty("datetime", DateTools.Formats.DATE_FORMAT_GMT.format(new Date()));
        jsonObject.addProperty("status", "cancel");
        jsonObject.addProperty("lat", StaticValue.latitudeMain + "");
        jsonObject.addProperty("long", StaticValue.longitudeMain + "");
        jsonObject.addProperty("distance", 0);
        jsonObject.addProperty("reason", reason);

        /**/
//        if (!UtilityDriver.isNetworkAvailable(StaticValue.mActivity)) {
//            CheckInOut checkInOut = new CheckInOut("", "", jsonObject.toString());
//            DAO.addCheck(Application.database, checkInOut);
//            return;
//        }
        /**/
        VolleyRequest<Object> request = new VolleyRequest<Object>(
                context,
                HttpMethod.POST,
                PathUrl.MAIN_URL + PathUrl.SET_ROUND_STATUS,
                new TypeToken<JsonObject>() {
                }.getType(),
                onComplete
        );
        /**/
        /**/

        request.setData(jsonObject);
        /**/
        sendRequest(context, request);
    }

    public static void forceEndRound(Context context, int roundId, String reason, final OnApiComplete<Object> onComplete) {
        /**/
//        final int distance = (int) UtilityDriver.distance(StaticValue.latitudeMain, StaticValue.longitudeMain, StaticValue.latitudeStartRound, StaticValue.longitudeStartRound, "M");
        /**/
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("round_id", roundId);
        jsonObject.addProperty("datetime", DateTools.Formats.DATE_FORMAT_GMT.format(new Date()));
        jsonObject.addProperty("status", "force_end");
        jsonObject.addProperty("lat", StaticValue.latitudeMain + "");
        jsonObject.addProperty("long", StaticValue.longitudeMain + "");
        jsonObject.addProperty("distance", 0);
        jsonObject.addProperty("reason", reason);

        /**/
//        if (!UtilityDriver.isNetworkAvailable(StaticValue.mActivity)) {
//            CheckInOut checkInOut = new CheckInOut("", "", jsonObject.toString());
//            DAO.addCheck(Application.database, checkInOut);
//            return;
//        }
        /**/
        VolleyRequest<Object> request = new VolleyRequest<Object>(
                context,
                HttpMethod.POST,
                PathUrl.MAIN_URL + PathUrl.SET_ROUND_STATUS,
                new TypeToken<JsonObject>() {
                }.getType(),
                onComplete
        );
        /**/
        /**/

        request.setData(jsonObject);
        /**/
        sendRequest(context, request);
    }


//
//
//    public static void getMessageHistory(Context context,
//                                         MessageHistoryRequest messageHistoryRequest,
//                                         final OnApiComplete<ArrayList<MessageHistoryObject>> onComplete) {
//
//        VolleyRequest<ArrayList<MessageHistoryObject>> request = new VolleyRequest<ArrayList<MessageHistoryObject>>(
//                context,
//                HttpMethod.POST,
//                DriverConstants.Urls.MESSAGE_HISTORY,
//                new TypeToken<ArrayList<MessageHistoryObject>>() {
//                }.getType(),
//                onComplete
//        );
//        request.setData(messageHistoryRequest);
//        sendRequest(context, request);
//    }
//
//    //
////
//    public static void sendMessage(Context context,
//                                   SendMessageRequest sendMessageRequest,
//                                   final OnApiComplete<ArrayList<SendMessageResponse>> onComplete) {
//
//        VolleyRequest<ArrayList<SendMessageResponse>> request = new VolleyRequest<ArrayList<SendMessageResponse>>(
//                context,
//                HttpMethod.POST,
//                DriverConstants.Urls.SEND_MESSAGE,
//                new TypeToken<ArrayList<SendMessageResponse>>() {
//                }.getType(),
//                onComplete
//        );
//        request.setData(sendMessageRequest);
//        sendRequest(context, request);
//    }
//
//
//    public static void isParentPhoneNumberRegistered(Context context,
//                                                     String secret_token,
//                                                     String mobile_number,
//                                                     final OnApiComplete<JsonObject> onComplete) {
//
//        VolleyRequest<JsonObject> request = new VolleyRequest<JsonObject>(
//                context,
//                HttpMethod.POST,
////                "https://boknyyx648.execute-api.eu-central-1.amazonaws.com/dev/api/parents/parent-registered",
//                DriverConstants.Urls.PARENT_IS_REGISTER,
//                new TypeToken<JsonObject>() {
//                }.getType(),
//                onComplete
//        );
//
//        try {
//            JsonObject jsonObject = new JsonObject();
////            jsonObject.addProperty("secret_token", secret_token);
//            jsonObject.addProperty("mobile_number", mobile_number);
//            request.setData(jsonObject);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//
//        }
//        /**/
//        sendRequest(context, request);
//    }
//
//    public static void getNumberFromFaceBook(Context context,
//                                             String access_token,
//                                             final OnApiComplete<JsonObject> onComplete) {
//
//        VolleyRequest<JsonObject> request = new VolleyRequest<JsonObject>(
//                context,
//                HttpMethod.GET,
//                "https://graph.accountkit.com/v1.2/me/?access_token=" + access_token,
//                new TypeToken<JsonObject>() {
//                }.getType(),
//                onComplete
//        );
//        /**/
//        sendRequest(context, request);
//    }

    public static void checkForLatestAppVersion(Context context, final OnApiComplete<ApplicationVersionJson> onComplete) {
        VolleyRequest<ApplicationVersionJson> request = new VolleyRequest<ApplicationVersionJson>(
                context,
                HttpMethod.GET,
                PathUrl.LATEST_APP_VERSION_URL,
                new TypeToken<ApplicationVersionJson>() {
                }.getType(),
                onComplete
        );
        request.addHeader("Cache-Control", "no-cache");
        ServiceRequestQueue.getInstance(context).add(request);
    }

    public static void sendArriveAlarm(Context context, int studentId, String round_type, final OnApiComplete<Object> onComplete) {
        ArrayList<ArriveAlarmRequest> studentIds = new ArrayList<>();
        studentIds.add(new ArriveAlarmRequest().setStudentId(studentId).setRound_type(round_type));
        sendArriveAlarm(context, studentIds, onComplete);
    }

    public static void sendArriveAlarm(Context context, ArrayList<ArriveAlarmRequest> data, final OnApiComplete<Object> onComplete) {
        JsonObject jsonObject = new JsonObject();

        /**/
        VolleyRequest<Object> request = new VolleyRequest<Object>(
                context,
                HttpMethod.POST,
                PathUrl.MAIN_URL + PathUrl.NOTIFY + PathUrl.VERSION_2,
                new TypeToken<JsonObject>() {
                }.getType(),
                onComplete
        );
        /**/
        /**/
        request.setData(data);
        /**/
        sendRequest(context, request);
    }


    public static void sendRoundOrder(Context context, ReorderedRequest reorderedRequest, final OnApiComplete<Object> onComplete) {
        JsonObject jsonObject = new JsonObject();

        /**/
        VolleyRequest<Object> request = new VolleyRequest<Object>(
                context,
                HttpMethod.POST,
                PathUrl.MAIN_URL + PathUrl.REORDERED,
                new TypeToken<JsonObject>() {
                }.getType(),
                onComplete
        );
        /**/
        /**/
        request.setData(reorderedRequest);
        /**/
        sendRequest(context, request);
    }

}
