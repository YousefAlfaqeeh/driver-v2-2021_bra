package schooldriver.trackware.com.school_bus_driver_android.fragment.round;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;
import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseFragment;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BasePresenter;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.enums.CheckEnum;
import schooldriver.trackware.com.school_bus_driver_android.enums.TypeRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.PathUrl;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created by   3/22/17.
 */
  
public class RoundPresenter extends BasePresenter implements IRestCallBack {
    TypeRoundEnum typeRoundEnum;
    MainActivity mActivity;
    BaseFragment roundFragment;

    public RoundPresenter(MainActivity mActivity, BaseFragment roundFragment) {
        this.mActivity = mActivity;
        this.roundFragment = roundFragment;
//        roundFragment.setListAdapter();
    }

    public RoundPresenter() {

    }

//    public void callApiGetRoundList(final TypeRoundEnum typeRoundEnum) {
//        this.typeRoundEnum = typeRoundEnum;
//        ArrayList<String> listString = DAO.getAllRoundList(Application.database);
//        if (!UtilityDriver.isNetworkAvailable(StaticValue.mActivity)) {
//            JSONObject response = null;
//            try {
//                response = new JSONObject(listString.get(0));
//                selectRound(response);
//                return;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//        String type = "";
//        if (typeRoundEnum == TypeRoundEnum.PICK_ROUND){
//            type = "pick";
//        }else if (typeRoundEnum == TypeRoundEnum.DROP_ROUND){
//            type = "drop";
//        }else if (typeRoundEnum == TypeRoundEnum.OTHER){
//            type = "other";
//        }
//        final String finalType = type;
//        callRestAPI(PathUrl.MAIN_URL + PathUrl.ROUND_LIST
//                ,
//                new HashMap<String, String>() {{
//                    put("round_type", finalType);
//                    put("bus_id", UtilityDriver.getStringShared(UtilityDriver.BUS_ID));
//                }}
//                ,
//                EnumMethodApi.POST
//                ,
//                RoundPresenter.this
//                ,
//                EnumNameApi.ROUND_LIST
//                ,
//                UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
//                ,
//                EnumTypeHeader.JSON
//        );
//    }

//    public void callSelectNotification() {
//
//
//
//        callRestAPI(PathUrl.MAIN_URL + PathUrl.RECENT_NOTIFICATION
//                ,
//                new HashMap() {{
////                    put("bus_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.BUS_ID)));
//                    put("day_count", 2);
//                }}
//                ,
//                EnumMethodApi.POST
//                ,
//                RoundPresenter.this
//                ,
//                EnumNameApi.RECENT_NOTIFICATION
//                ,
//                UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
//                ,
//                EnumTypeHeader.JSON
//        );
//    }

    @Override
    public void onRestCallBack(String response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

    }

    @Override
    public void onRestCallBack(JSONObject response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

//        if (EnumNameApi.ROUND_LIST == nameApiEnum) {
//            selectRound(response);
//
//        }
//        else
        if (EnumNameApi.RECENT_NOTIFICATION == nameApiEnum) {

//            selectNotification(response);

//            MainActivity.listNotificationBeen = new ArrayList<>();

        } else if (EnumNameApi.LOGIN_DRIVER == nameApiEnum || EnumNameApi.LOGIN_DRIVER_REFRESH == nameApiEnum) {

            System.err.println(response);
            try {
                String status = response.getString("status");
                if (!status.contains("ok")) {
                    UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.error_api), status);
                    MainActivity.showFragmentLogin();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


//    private void selectRound(JSONObject response) {
//
//        if (StaticValue.progressDialog != null) {
//            StaticValue.progressDialog.dismiss();
//        }
//        if (roundFragment != null) {
//            roundFragment.setListAdapter(new ArrayList());
//        }
//        RoundBean roundBean = null;
////        StudentBean studentBean = null;
//        List<StudentBean> listStudentBean = null;
//        List<RoundBean> listRoundBean = new ArrayList<>();
//        ParentStudentBean mobileStudentBean = null;
//        ArrayList<String> listString = DAO.getAllRoundList(Application.database);
//        if (UtilityDriver.isNetworkAvailable(StaticValue.mActivity)) {
//
//            if (listString.size() > 0) {
//                DAO.deleteAll(Application.database, DAO.ROUND_LIST_TBL);
//            }
//            DAO.addRoundList(Application.database, response.toString());
//        }
////        List<StudentBean> listStudentBeanAbsent = new ArrayList<>();
//        try {
//            boolean joSettings = true;
//            if (response.has("school_settings")) {
//                joSettings = response.getJSONObject("school_settings").getBoolean("change_student_location");
//            }
//            JSONArray jaRounds = response.getJSONArray("rounds");
//            for (int i = 0; i < jaRounds.length(); i++) {
//
//
//                listStudentBean = new ArrayList<>();
//                StudentBean studentBean = null;
//                roundBean = new RoundBean();
//                roundBean.setChangeStudentLocation(joSettings);
//                JSONObject joRound = jaRounds.getJSONObject(i);
//
//                roundBean.setId(joRound.getInt("round_id"));
//                if (joRound.has("name"))
//                    roundBean.setNameRound(joRound.optString("name"));
//                roundBean.setRoundsStart(joRound.getBoolean("round_started"));
//                roundBean.setRoundsEnd(joRound.getBoolean("round_ended"));
//                roundBean.setRound_canceled(joRound.optBoolean("round_canceled",false));
//                roundBean.setGeofence(joRound.getString("geofenses"));
//                roundBean.setDateTime(UtilityDriver.convertUTCDateString(joRound.getString("round_time")));
//                roundBean.setStatusRoundEnum(StatusRoundEnum.START);
//                JSONArray jaStudents = joRound.getJSONArray("students_list");
//
//                for (int j = 0; j < jaStudents.length(); j++) {
//                    studentBean = new StudentBean();
//
//                    JSONObject joStudent = jaStudents.getJSONObject(j);
//                    studentBean.setId(joStudent.getInt("id"));
//                    studentBean.setNameStudent(joStudent.getString("name"));
//
//                    studentBean.setAvatar(joStudent.getString("avatar"));
//                    studentBean.setGrade(joStudent.getString("grade"));
//                    studentBean.setCheckEnum(CheckEnum.EMPTY);
//                    try {
//                        if (joStudent.getBoolean("check_in")) {
//                            studentBean.setCheckEnum(CheckEnum.CHECK_IN);
//                        }
//
//                    } catch (JSONException e) {
//
//                    }
//
//                    try {
//
//
//                        if (joStudent.getBoolean("check_out")) {
//                            studentBean.setCheckEnum(CheckEnum.CHECK_OUT);
//                        }
//                    } catch (JSONException e) {
//
//                    }
//
//                    if (joStudent.has("absent"))
//                        studentBean.setAbsent(joStudent.getBoolean("absent"));
//                    if (joStudent.has("no_show"))
//                        studentBean.setIsNoShow(joStudent.getBoolean("no_show"));
//                    if (!UtilityDriver.isEmptyString(joStudent.getString("lat")) && !UtilityDriver.isEmptyString(joStudent.getString("lng"))) {
//                        studentBean.setLatitude(joStudent.getDouble("lat"));
//                        studentBean.setLongitude(joStudent.getDouble("lng"));
//                    } else {
//                        studentBean.setLatitude(0);
//                        studentBean.setLongitude(0);
//                    }
//                    studentBean.setTypeRoundEnum(typeRoundEnum);
//
//                    JSONArray jaMobileStudent = joStudent.getJSONArray("parents_info");
//                    try {
//                        for (int f = 0; f < jaMobileStudent.length(); f++) {
//
//
//                            mobileStudentBean = new ParentStudentBean();
//                            String settings;
//                            JSONObject joSetting;
//                            if (jaMobileStudent.getJSONObject(f).has("mother")) {
//                                JSONObject joMobileStudent = jaMobileStudent.getJSONObject(f).getJSONObject("mother");
//                                if (joMobileStudent.has("number")) {
//                                    mobileStudentBean.setMotherNumber(joMobileStudent.optString("number"));
//                                    mobileStudentBean.setMotherToken(joMobileStudent.optString("mobile_token"));
//                                    mobileStudentBean.setMotherId(joMobileStudent.optInt("id",-1));
//                                    if (joMobileStudent.has("platform"))
//                                        mobileStudentBean.setMotherPlatform(joMobileStudent.getString("platform"));
//
//
//                                    settings = joMobileStudent.getString("settings");
//                                    joSetting = new JSONObject(settings);
//                                    if (joSetting.has("notifications")) {
//                                        String notifications = joSetting.getString("notifications");
//                                        JSONObject joNotifications = new JSONObject(notifications);
//                                        mobileStudentBean.setMotherLocal(joNotifications.optString("locale","en"));
//                                        mobileStudentBean.setCheckInMother(joNotifications.getBoolean("check_in"));
//                                        mobileStudentBean.setCheckOutMother(joNotifications.getBoolean("check_out"));
//                                        mobileStudentBean.setCheckOutMother(joNotifications.getBoolean("nearby"));
//                                    } else {
//                                        mobileStudentBean.setCheckInMother(true);
//                                        mobileStudentBean.setCheckOutMother(true);
//                                        mobileStudentBean.setCheckOutMother(true);
//                                    }
//                                }
//                            }
//                            if (jaMobileStudent.getJSONObject(f).has("father")) {
//                                if (jaMobileStudent.getJSONObject(f).has("number")) {
//                                    JSONObject joMobileStudent = jaMobileStudent.getJSONObject(f).getJSONObject("father");
//                                    mobileStudentBean.setFatherNumber(joMobileStudent.optString("number"));
//                                    mobileStudentBean.setFatherToken(joMobileStudent.optString("mobile_token"));
//                                    mobileStudentBean.setFatherId(joMobileStudent.optInt("id",-1));
//                                    if (joMobileStudent.has("platform"))
//                                        mobileStudentBean.setFatherPlatform(joMobileStudent.getString("platform"));
//
//
//
//                                    settings = joMobileStudent.getString("settings");
//                                    joSetting = new JSONObject(settings);
//                                    if (joSetting.has("notifications")) {
//                                        String notifications = joSetting.getString("notifications");
//                                        JSONObject joNotifications = new JSONObject(notifications);
//                                        mobileStudentBean.setFatherLocal(joNotifications.optString("locale","en"));
//                                        mobileStudentBean.setCheckInFather(joNotifications.getBoolean("check_in"));
//                                        mobileStudentBean.setCheckOutFather(joNotifications.getBoolean("check_out"));
//                                        mobileStudentBean.setCheckOutFather(joNotifications.getBoolean("nearby"));
//                                    } else {
//                                        mobileStudentBean.setCheckInFather(true);
//                                        mobileStudentBean.setCheckOutFather(true);
//                                        mobileStudentBean.setCheckOutFather(true);
//                                    }
//                                }
////                        }
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    studentBean.setMobileStudentBean(mobileStudentBean);
//
//                    listStudentBean.add(studentBean);
//
//
//                }
//                listStudentBean = swapListAll(listStudentBean);
//                roundBean.setListStudentBean(listStudentBean);
//                listRoundBean.add(roundBean);
//            }
//            if (roundFragment != null) {
//                listRoundBean = updateGroup(listRoundBean);
//                roundFragment.setListAdapter(listRoundBean);
//            } else {
//                RoundFragment.listRoundBean = new ArrayList<>();
//                listRoundBean =  updateGroup(listRoundBean);
//                RoundFragment.listRoundBean = listRoundBean;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

    //    private List<RoundBean> updateGroup(List<RoundBean> listRoundBean) {
//
//        List<RoundBean> listRoundBeanNew = new ArrayList<>();
//
//        for (RoundBean roundBean : listRoundBean) {
//            Map<Integer,List<StudentBean>> mapStudentBean = new HashMap<>();
//            List<StudentBean> listStudentBeen = roundBean.getListStudentBean();
//            List<StudentBean> listStudentGroup = new ArrayList<>();
//            StudentBean studentBeanMain = null;
//            int noGroup = 1;
//            for (StudentBean studentBean : listStudentBeen) {
//                if (studentBeanMain == null) {
//                    studentBeanMain = studentBean;
//                }
//                double distanceBetweenStudent = UtilityDriver.distance(studentBeanMain.getLatitude(), studentBeanMain.getLongitude(), studentBean.getLatitude(), studentBean.getLongitude(), "M");
//                if (distanceBetweenStudent <= 100) {
//                    studentBean.setGroup(noGroup);
//                    listStudentGroup.add(studentBean);
//                }else{
//                    listStudentGroup = new ArrayList<>();
//                    listStudentGroup.add(studentBean);
//                    studentBeanMain = studentBean;
//                    ++noGroup;
//                    studentBean.setGroup(noGroup);
//                }
//                mapStudentBean.put(noGroup,listStudentGroup);
//                MyComparator comp = new MyComparator(mapStudentBean);
//
//                Map<Integer,List<StudentBean>> newMap = new TreeMap(comp);
//                newMap.putAll(mapStudentBean);
//                roundBean.setMapStudentBean(newMap);
//            }
//            listRoundBeanNew.add(roundBean);
//            System.err.print(mapStudentBean+"");
//        }
//        return listRoundBeanNew;
//    }
    public static class MyComparator implements Comparator {
        Map map;

        public MyComparator(Map map) {
            this.map = map;
        }

        @Override
        public int compare(Object o1, Object o2) {
            return (o1.toString()).compareTo(o2.toString());
        }
    }

    public List<StudentBean> swapListAll(List<StudentBean> listStudentBean) {

        List<StudentBean> newListStudentBean = new ArrayList<>();
        List<StudentBean> firstListStudentBean = new ArrayList<>();
        List<StudentBean> lastListStudentBean = new ArrayList<>();

        for (StudentBean studentBean : listStudentBean) {
            if (studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) { //////
                if (studentBean.isAbsent() == true) {
                    lastListStudentBean.add(studentBean);
                }
            } else {
                if (studentBean.isNoShow() == true) {
                    lastListStudentBean.add(studentBean);
                }
            }
            if (studentBean.getCheckEnum() == CheckEnum.CHECK_OUT) {
                lastListStudentBean.add(studentBean);
            } else if (studentBean.getCheckEnum() == CheckEnum.CHECK_IN) {
                lastListStudentBean.add(studentBean);
            } else {
                if (studentBean.isAbsent() == false)
                    if (studentBean.isNoShow() == false)
                        firstListStudentBean.add(studentBean);
            }
        }


        newListStudentBean.addAll(firstListStudentBean);
        newListStudentBean.addAll(lastListStudentBean);

        return newListStudentBean;
    }

    @Override
    public void onRestCallBack(VolleyError volleyError, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
        try {
            if (volleyError.getMessage().toString().contains("java.net.UnknownHost")) {
                UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.internet_connection), "");
                return;
            }
        } catch (Exception e) {

        }


        UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.error_api), volleyError.getMessage());
    }

    @Override
    public void onRestCallBack(NetworkResponse networkResponse, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

    }


    public void callApiLogin(final String pin, final String param) {


        EnumNameApi enumNameApi = EnumNameApi.LOGIN_BUS;
        if (param.equals("bus_pin")) {
            enumNameApi = EnumNameApi.LOGIN_DRIVER;
        }
        callRestAPI(PathUrl.MAIN_URL + PathUrl.LOGIN
                ,
                new HashMap<String, String>() {{
//                    put("driver_code",driverCode);
                    put(param, pin);
//                    put("login_type","driver");
//                    put("mobile_platform","android");
//                    String token = FirebaseInstanceId.getInstance().getToken();
//                    UtilityDriver.setStringShared(token, UtilityDriver.TOKEN_GCM);

                    Log.v("FirebaseInstanceId", mActivity.fireBaseToken);
                    if (mActivity.fireBaseToken==null || mActivity.fireBaseToken.isEmpty())
                        mActivity.fireBaseToken = "null";

                    put("mobile_token", mActivity.fireBaseToken);
//                    put("datetime",UtilityDriver.getDateFormat("dd/MM/yyyy hh:mm:ss"));
                }}
                ,
                EnumMethodApi.POST
                ,
                RoundPresenter.this
                ,
                enumNameApi
                ,
                UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, false)
                ,
                EnumTypeHeader.JSON
        );
    }
}
