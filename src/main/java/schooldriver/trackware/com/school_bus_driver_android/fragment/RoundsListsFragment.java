package schooldriver.trackware.com.school_bus_driver_android.fragment;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ApiController;
import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ListenersAndInterFaces.OnApiComplete;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.adapters.RecyclerViewAdapter;
import schooldriver.trackware.com.school_bus_driver_android.app.Application;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseFragment;
import schooldriver.trackware.com.school_bus_driver_android.bean.AttendantBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.ParentStudentBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.RoundBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.enums.CheckEnum;
import schooldriver.trackware.com.school_bus_driver_android.enums.StatusRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.enums.TypeRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.fragment.round.RoundFragment;
import schooldriver.trackware.com.school_bus_driver_android.fragment.round.RoundHolderNew;
import schooldriver.trackware.com.school_bus_driver_android.fragment.round.RoundPresenter;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.RoundInfoFragment;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.RoundInfoFragmentDropOff;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.RoundInfoFragmentPickUp;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.OnActionDoneListener;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilDialogs;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilViews;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created   on 2/28/2017.
 */
  
public class RoundsListsFragment extends BaseFragment {

    private RecyclerViewAdapter<RoundBean, RoundHolderNew> rListAdapter;
    private RecyclerView rs_Round;
    private String rType = "";
    private List<RoundBean> roundBeans = new ArrayList<>();
    private OnActionDoneListener<Object> onFragmentReady;
    private OnActionDoneListener<RoundBean> onMustEndedRoundDetected = null;
    private OnActionDoneListener<RoundsListsFragment> onSwipeRefresh = null;
    private SwipeRefreshLayout rs_Round_SwipeRefresh;
    private RoundFragment roundFragment;
    private View linNotification;
    private View loading_view_for_all_screen;

    public static RoundsListsFragment newInstance(String type, OnActionDoneListener<Object> onFragmentReady, RoundFragment roundFragment) {

        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        RoundsListsFragment fragment = new RoundsListsFragment();
        fragment.onFragmentReady = onFragmentReady;
        fragment.roundFragment = roundFragment;
        fragment.setArguments(bundle);
        return fragment;
    }

    public RoundsListsFragment setOnMustEndedRoundDetected(OnActionDoneListener<RoundBean> onMustEndedRoundDetected) {
        this.onMustEndedRoundDetected = onMustEndedRoundDetected;
        return this;
    }

    public RoundsListsFragment setOnSwipeRefresh(OnActionDoneListener<RoundsListsFragment> onSwipeRefresh) {
        this.onSwipeRefresh = onSwipeRefresh;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rounds_list_for_tabs, container, false);
        rs_Round =  view.findViewById(R.id.rs_Round);
        loading_view_for_all_screen = view.findViewById(R.id.loading_view_for_all_screen);
        linNotification = view.findViewById(R.id.linNotification);
        rs_Round_SwipeRefresh = view.findViewById(R.id.rs_Round_SwipeRefresh);
        rs_Round.setLayoutManager(new LinearLayoutManager(getActivity()));
        /**/
        rType = getArguments().getString("type");
        /**/
        initAdapter();

//        rListAdapter.addAll(getListFromDatabBase());


        initVars();
        doRefresh();

        /**/

//        getMainActivity().startAndSendDataToService("startService");
        /**/
        rs_Round_SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (rs_Round_SwipeRefresh.isRefreshing()) {
                    onSwipeRefresh.OnActionDone(RoundsListsFragment.this);
                }

            }
        });
//        try{
//            if (!UtilityDriver.getStringShared(UtilityDriver.PIN).equalsIgnoreCase("")){
//                new LoginPresenter(getMainActivity(), new IActionLogin() {
//                    @Override
//                    public void done(String message) {
//
//                    }
//
//                    @Override
//                    public void error(String message) {
//
//                    }
//                }).callApiLogin(UtilityDriver.getStringShared(UtilityDriver.PIN), "LOGIN_DRIVER_REFRESH");;
//            }
//        }catch (Exception e){
//
//        }

        return view;
    }

    public void doRefresh() {
//        getMainActivity().startAndSendDataToService(BeaconService.REST_SCAN);
        if (rs_Round_SwipeRefresh != null) {
            rs_Round_SwipeRefresh.setRefreshing(true);
        }
        roundBeans.clear();
        rListAdapter.clear();
        callAPI();
    }

    private void callAPI() {

        ApiController.getRoundList(getActivity(), rType, new OnApiComplete<Object>() {
            @Override
            public void onSuccess(Object o) {
                try {
                    roundBeans.clear();
                    rListAdapter.clear();
                    roundBeans.addAll(parceRoundJson(new JSONObject(o.toString())));
                    rListAdapter.addAll(roundBeans);
                    onFragmentReady.OnActionDone(rType);//fragment is ready
                    if (!checkIfWasRoundStartedOrThereIsMustEndedRound(roundBeans)) {
                        rs_Round_SwipeRefresh.setRefreshing(false);
                    }


                } catch (Exception e) {
                    StringBuilder log = new StringBuilder(o == null ? "response is null" : "response : " + o.toString());
                    log.append("\n ");
                    log.append("StackTrace : ");
                    log.append(e.getStackTrace().toString());
                    log.append("\n ");
                    log.append("Error Message : ");
                    log.append(e.getMessage());
                    onError(0, log.toString());
                }

            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                try {
                    Application.logEvents("getRoundList", "RoundsListsFragment - callAPI - onError ", errorMessage);

                    rs_Round_SwipeRefresh.setRefreshing(false);
                    new UtilDialogs.MessageYesNoDialog().show(getActivity())
                            .setYesButtonText(R.string.ok)
                            .hideCloseButton()
                            .setDialogeTitle(getActivity().getString(R.string.error_api))
                            .setDialogeTitleTextColor(R.color.red_tabs);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
//            getMainActivity().checkLocationPermission(true);
            linNotification.setOnClickListener(notificationIconClickListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    UtilDialogs.ReasonDialog reasonDialog;

    private void initAdapter() {
        rListAdapter = new RecyclerViewAdapter<RoundBean, RoundHolderNew>() {
            @Override
            public RoundHolderNew cViewHolder(ViewGroup viewGroup, int i, LayoutInflater layoutInflater) {
                return new RoundHolderNew(layoutInflater.inflate(R.layout.list_rounds_new, viewGroup, false));

            }

            @Override
            public void bViewHolder(RoundHolderNew viewHolder, int i, final RoundBean item) {
                viewHolder.timeRound_tv.setText(item.getDateTime());
                viewHolder.nameRound_tv.setText(item.getNameRound());
                viewHolder.roundNew();
                /**/

//                if (item.getListStudentBean() != null && item.getListStudentBean().size() == 0) {//empty round
////                    viewHolder.roundEmpty();
////                } else {
                if (item.isRoundEndedForEver()) {
                    viewHolder.roundEnded();
                } else if (item.isRoundPaused()) {
                    viewHolder.roundResume();
                }
                /**/
                View.OnClickListener showRoundClickLisiner = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UtilViews.handleClickDelay(view);

                        callStudentsListAPI(item, null);


                    }
                };
                /**/
                viewHolder.startRound_view.setOnClickListener(showRoundClickLisiner);
                viewHolder.showRound_view.setOnClickListener(showRoundClickLisiner);
                viewHolder.resumeRound_view.setOnClickListener(showRoundClickLisiner);
//                }

            }
        };
        rs_Round.setAdapter(rListAdapter);

    }


    private void goToRoundPage(RoundBean item) {
        if (roundFragment.mustEndedRoundm != null && roundFragment.mustEndedRoundm.getId() != (item.getId())) {
            if (reasonDialog != null)
                reasonDialog.dismiss();

            reasonDialog = new UtilDialogs.ReasonDialog().show(getActivity()).
                    setDialogeTitle(getString(R.string.there_is_must_ended_round).replaceAll("@@@@", roundFragment.mustEndedRoundm.getNameRound()))
//                                    .setDialogeTitleTextColor(R.color.red_tabs)
                    .setYesButtonText(R.string.completely_stop_round)
                    .setImageWithColor(R.drawable.stop, 0)
                    .initReasonsViews(UtilDialogs.ReasonDialog.REASON_CANCEL)
                    .setDialogeAdditionalMessage(R.string.end_the_round_additional)
                    .setAfterResonSelectedLisiner(new OnActionDoneListener<String>() {
                        @Override
                        public void OnActionDone(String reason) {
                            if (reason != null) {
                                forceEndRound(reason, roundFragment.mustEndedRoundm, item);
                            }


                        }
                    }, null);

            return;
        }
        BaseFragment roundInfoFragment;

        if (rType.equalsIgnoreCase("pick")) {
            roundInfoFragment = new RoundInfoFragmentPickUp();
        } else {
            roundInfoFragment = new RoundInfoFragmentDropOff();
        }

        if (getMainActivity().checkGPS(false, true)) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(UtilityDriver.ROUND, item);
            setFragment(roundInfoFragment, bundle, false);
        }


    }

    private boolean checkIfWasRoundStartedOrThereIsMustEndedRound(List<RoundBean> roundBeans) {
        /**/
        for (int i = 0; i < roundBeans.size(); i++) {
            RoundBean roundBean = roundBeans.get(i);
            if (roundBean.isRoundStartedNow()) {
                loading_view_for_all_screen.setVisibility(View.VISIBLE);

                callStudentsListAPI(roundBean, new OnApiComplete<Object>() {
                    @Override
                    public void onSuccess(Object o) {


                        try {
                            onFragmentReady.OnActionDone(addStudentsListToRound(roundBean, new JSONObject(o.toString())));
                            stopAllLoading();
                        } catch (Exception e) {
                            stopAllLoading();
                        }

                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        stopAllLoading();
                    }
                });

                return true;
            }

//            else if (onMustEndedRoundDetected != null && roundBean.isRound_canceled() && !roundBean.isRoundsEnd()) {
//                loading_view_for_all_screen.setVisibility(View.VISIBLE);
//                callStudentsListAPI(roundBean, new OnApiComplete<Object>() {
//                    @Override
//                    public void onSuccess(Object o) {
//
//                        try {
//                            onMustEndedRoundDetected.OnActionDone(addStudentsListToRound(roundBean, new JSONObject(o.toString())));
//                            stopAllLoading();
//                        } catch (Exception e) {
//                            stopAllLoading();
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(int errorCode, String errorMessage) {
//                        stopAllLoading();
//                    }
//                });
//
//                return true;
//            }
        }

        return false;
    }


    private void stopAllLoading() {
        if (StaticValue.progressDialog != null)
            StaticValue.progressDialog.dismiss();

        if (rs_Round_SwipeRefresh != null)
            rs_Round_SwipeRefresh.setRefreshing(false);

        loading_view_for_all_screen.setVisibility(View.GONE);
    }

    private void forceEndRound(String reason, RoundBean mustCanceldRound, final RoundBean newRound) {
        showProcessingDialog(0);
        ApiController.forceEndRound(getActivity(), mustCanceldRound.getId(), reason, new OnApiComplete<Object>() {
            @Override
            public void onSuccess(Object repons) {
                hideProcessingDialog();
                Log.v("cancelRound ", "" + repons.toString());
                if (repons.toString().toLowerCase().contains("ok")) {

                    BaseFragment roundInfoFragment;
                    if (rType.equalsIgnoreCase("pick")) {
                        roundInfoFragment = new RoundInfoFragmentPickUp();
                    } else {
                        roundInfoFragment = new RoundInfoFragmentDropOff();
                    }

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(UtilityDriver.ROUND, newRound);
                    setFragment(roundInfoFragment, bundle, false);


                } else {
                    onError(0, "Error : " + repons);
                    Application.logEvents("forceEndRound", "RoundsListsFragment - forceEndRound - onError ", repons);

                }
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                hideProcessingDialog();
                Application.logEvents("forceEndRound", "RoundsListsFragment - forceEndRound - onError ", errorMessage);
                showAPIErrorDialog(errorMessage.toString());
            }
        });

    }

    private List<RoundBean> parceRoundJson(JSONObject response) {

        if (StaticValue.progressDialog != null) {
            StaticValue.progressDialog.dismiss();
        }

        RoundBean roundBean = null;
//        StudentBean studentBean = null;
//        List<StudentBean> listStudentBean = null;
        List<RoundBean> listRoundBean = new ArrayList<>();
//        ParentStudentBean mobileStudentBean = null;
//        ArrayList<String> listString = DAO.getAllRoundList(Application.database);
//        if (UtilityDriver.isNetworkAvailable(StaticValue.mActivity)) {
//            if (listString.size() > 0) {
//                DAO.deleteAll(Application.database, DAO.ROUND_LIST_TBL);
//        DAO.addRoundList(Application.database, response.toString());
//            }

//        }
//        List<StudentBean> listStudentBeanAbsent = new ArrayList<>();
        try {
            boolean joSettings = true;
            if (response.has("school_settings")) {
                joSettings = response.getJSONObject("school_settings").getBoolean("change_student_location");
                UtilityDriver.setBooleanShared(UtilityDriver.ALLOW_DRIVER_TO_USE_BEACON, response.getJSONObject("school_settings").optBoolean("allow_driver_to_use_beacon", false));

            }
            JSONArray jaRounds = response.getJSONArray("rounds");
            for (int i = 0; i < jaRounds.length(); i++) {


//                listStudentBean = new ArrayList<>();
//                StudentBean studentBean = null;
                roundBean = new RoundBean();
                roundBean.setChangeStudentLocation(joSettings);
                JSONObject joRound = jaRounds.getJSONObject(i);

                roundBean.setId(joRound.getInt("round_id"));
                if (joRound.has("name"))
                    roundBean.setNameRound(joRound.getString("name"));

                if (joRound.has("attendant")) {
                    try {
                        AttendantBean attendantBean = new AttendantBean();
                        /**/
                        attendantBean.setName(joRound.getJSONObject("attendant").optString("name"));
                        attendantBean.setMobile_number(joRound.getJSONObject("attendant").optString("mobile_number"));
                        attendantBean.setPhoto(joRound.getJSONObject("attendant").optString("photo"));
                        attendantBean.setPick_lat(joRound.getJSONObject("attendant").getJSONObject("pickup_location").optString("pick_lat"));
                        attendantBean.setPick_lng(joRound.getJSONObject("attendant").getJSONObject("pickup_location").optString("pick_lng"));
                        attendantBean.setDrop_lat(joRound.getJSONObject("attendant").getJSONObject("drop_off_location").optString("drop_lat"));
                        attendantBean.setDrop_lng(joRound.getJSONObject("attendant").getJSONObject("drop_off_location").optString("drop_lng"));
                        /**/
                        roundBean.setAttendantBean(attendantBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                roundBean.setRoundsStart(joRound.getBoolean("round_started"));
                roundBean.setRoundsEnd(joRound.getBoolean("round_ended"));
                roundBean.setRound_canceled(joRound.optBoolean("round_canceled", false));

                try {
                    JSONArray moved_students = joRound.optJSONArray("moved_students");
                    ArrayList<String> movedStudentsList = new ArrayList<>();
                    for (int j = 0; j < moved_students.length(); j++) {
                        movedStudentsList.add(moved_students.getString(j));
                    }
                    roundBean.setMovedStudentsList(movedStudentsList);
                } catch (Exception e) {
                    e.printStackTrace();

                }


                roundBean.setGeofence(joRound.optString("geofenses"));
                roundBean.setDateTime(UtilityDriver.convertUTCDateString(joRound.optString("round_time")));
                roundBean.setStatusRoundEnum(StatusRoundEnum.START);


                ///////
//                        JSONArray jaStudents = joRound.getJSONArray("students_list");

//                addStudentsListToRound(roundBean,jaStudents);
                ///////


                if (rType.equalsIgnoreCase("pick")) {
                    roundBean.setTypeRoundEnum(TypeRoundEnum.PICK_ROUND);
                } else {
                    roundBean.setTypeRoundEnum(TypeRoundEnum.DROP_ROUND);
                }
                listRoundBean.add(roundBean);
            }
//            if (roundFragment != null) {
            listRoundBean = updateGroup(listRoundBean);
//                roundFragment.setListAdapter(listRoundBean);
//            } else {
//                RoundFragment.listRoundBean = new ArrayList<>();
//                listRoundBean =  updateGroup(listRoundBean);
//                RoundFragment.listRoundBean = listRoundBean;
//            }

        } catch (JSONException e) {
            e.printStackTrace();
            Application.logEvents("getRoundList", "RoundsListsFragment - parceRoundJson ", e);
        }
//        if (listRoundBean.size() > 0){
//            DAO.deleteAll(Application.database, DAO.ROUND_LIST_TBL);
//            DAO.addRoundList(Application.database, response.toString());
//        }


        return listRoundBean;
    }

    public RoundBean addStudentsListToRound(RoundBean roundBean, JSONObject jobj) {
//        Log.v("jobjjobj",jobj.toString());

        JSONArray jaStudents = jobj.optJSONArray("students_list");

        List<StudentBean> listStudentBean = new ArrayList<>();

        for (int j = 0; j < jaStudents.length(); j++) {
            try {

            } catch (Exception e) {

            }


            StudentBean studentBean = new StudentBean();

            JSONObject joStudent = jaStudents.optJSONObject(j);
            studentBean.setId(joStudent.optInt("id"));
            studentBean.setNameStudent(joStudent.optString("name"));

            studentBean.setAvatar(joStudent.optString("avatar"));
            Log.v("sssssssssssssssssssssssssssssssss",joStudent.optString("grade"));
            studentBean.setGrade(joStudent.optString("grade"));
            studentBean.setCheckEnum(CheckEnum.EMPTY);
            try {
                if (joStudent.optBoolean("check_in", false)) {
                    studentBean.setCheckEnum(CheckEnum.CHECK_IN);
                }

            } catch (Exception e) {
                Application.logEvents("getRoundList", "RoundsListsFragment - parceRoundJson ", e);

            }

            try {


                if (joStudent.optBoolean("check_out", false)) {
                    studentBean.setCheckEnum(CheckEnum.CHECK_OUT);
                }
            } catch (Exception e) {
                Application.logEvents("getRoundList", "RoundsListsFragment - parceRoundJson ", e);

            }

            if (joStudent.toString().contains("absent"))
                studentBean.setAbsent(joStudent.optBoolean("absent"));
            if (joStudent.toString().contains("mac_address"))
                studentBean.setMacAdress(joStudent.optString("mac_address"));
            if (joStudent.toString().contains("no_show"))
                studentBean.setIsNoShow(joStudent.optBoolean("no_show"));
            if (!UtilityDriver.isEmptyString(joStudent.optString("lat")) && !UtilityDriver.isEmptyString(joStudent.optString("lng"))) {
                studentBean.setLatitude(joStudent.optDouble("lat"));
                studentBean.setLongitude(joStudent.optDouble("lng"));
            } else {
                studentBean.setLatitude(0);
                studentBean.setLongitude(0);
            }

            studentBean.setTypeRoundEnum(rType.equalsIgnoreCase("pick") ? TypeRoundEnum.PICK_ROUND : TypeRoundEnum.DROP_ROUND);

            JSONArray jaMobileStudent = joStudent.optJSONArray("parents_info");
            ParentStudentBean mobileStudentBean = new ParentStudentBean();
            try {
                for (int f = 0; f < jaMobileStudent.length(); f++) {
                    String settings;
                    JSONObject joSetting;
                    if (jaMobileStudent.getJSONObject(f).has("mother")) {
                        Log.d("jaMobileStudent", String.valueOf(jaMobileStudent.getJSONObject(f)));
                        JSONObject joMobileStudent = jaMobileStudent.getJSONObject(f).getJSONObject("mother");
                        if (joMobileStudent.has("number")) {
                            mobileStudentBean.setMotherNumber(joMobileStudent.optString("number"));
                            mobileStudentBean.setMotherToken(joMobileStudent.optString("firebas"));
                            mobileStudentBean.setMotherId(joMobileStudent.optInt("id", -1));
                            if (joMobileStudent.has("platform"))
                                mobileStudentBean.setMotherPlatform(joMobileStudent.optString("platform"));
                            settings = joMobileStudent.getString("settings");

                            joSetting = new JSONObject(settings);
                            if (joSetting.has("notifications")) {
                                String notifications = joSetting.getString("notifications");
                                JSONObject joNotifications = new JSONObject(notifications);
                                mobileStudentBean.setMotherLocal(joNotifications.optString("locale", "en"));
                                mobileStudentBean.setCheckInMother(joNotifications.getBoolean("check_in"));
                                mobileStudentBean.setCheckOutMother(joNotifications.getBoolean("check_out"));
                                mobileStudentBean.setCheckOutMother(joNotifications.getBoolean("nearby"));
                            } else {
                                mobileStudentBean.setCheckInMother(true);
                                mobileStudentBean.setCheckOutMother(true);
                                mobileStudentBean.setCheckOutMother(true);
                            }
                        }
                    }
                    if (jaMobileStudent.getJSONObject(f).has("father")) {
                        Log.d("jaMobileStudent.getJSONObject(f).getJSONObject(father)", String.valueOf(jaMobileStudent.getJSONObject(f)));

                        if (jaMobileStudent.getJSONObject(f).getJSONObject("father").has("number")) {
                            JSONObject joMobileStudent = jaMobileStudent.getJSONObject(f).getJSONObject("father");
                            mobileStudentBean.setFatherNumber(joMobileStudent.optString("number"));
                            mobileStudentBean.setFatherToken(joMobileStudent.optString("mobile_token"));
                            mobileStudentBean.setFatherId(joMobileStudent.optInt("id", -1));
                            if (joMobileStudent.has("platform"))
                                mobileStudentBean.setFatherPlatform(joMobileStudent.getString("platform"));


                            settings = joMobileStudent.getString("settings");
                            joSetting = new JSONObject(settings);
                            if (joSetting.has("notifications")) {
                                String notifications = joSetting.getString("notifications");
                                JSONObject joNotifications = new JSONObject(notifications);
                                mobileStudentBean.setFatherLocal(joNotifications.optString("locale", "en"));
                                mobileStudentBean.setCheckInFather(joNotifications.getBoolean("check_in"));
                                mobileStudentBean.setCheckOutFather(joNotifications.getBoolean("check_out"));
                                mobileStudentBean.setCheckOutFather(joNotifications.getBoolean("nearby"));
                            } else {
                                mobileStudentBean.setCheckInFather(true);
                                mobileStudentBean.setCheckOutFather(true);
                                mobileStudentBean.setCheckOutFather(true);
                            }
                        }
//                        }
                    }
                }
            } catch (JSONException e) {
                Application.logEvents("getRoundList", "RoundsListsFragment - parceRoundJson ", e);
                e.printStackTrace();
            }
            studentBean.setMobileStudentBean(mobileStudentBean);

            listStudentBean.add(studentBean);


        }
        listStudentBean = swapListAll(listStudentBean);
        roundBean.setListStudentBean(listStudentBean);
        return roundBean;
    }

    private void callStudentsListAPI(RoundBean roundBean, OnApiComplete<Object> callback) {
        if (StaticValue.progressDialog != null) {
            StaticValue.progressDialog.show();
        }

        if (callback == null) {
            callback = new OnApiComplete<Object>() {
                @Override
                public void onSuccess(Object o) {
                    try {
                        if (StaticValue.progressDialog != null) {
                            StaticValue.progressDialog.dismiss();
                        }
                        JSONObject jsonObject = new JSONObject(o.toString());
                        RoundBean roundBean1 = addStudentsListToRound(roundBean, jsonObject);
                        goToRoundPage(roundBean1);
                    } catch (Exception e) {

                    }

                }

                @Override
                public void onError(int errorCode, String errorMessage) {
                    if (StaticValue.progressDialog != null) {
                        StaticValue.progressDialog.dismiss();
                    }
                }
            };
        }
        int roundId = roundBean.getId();
        ApiController.getStudents_List(getActivity(), roundId, callback);
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

    private List<RoundBean> updateGroup(List<RoundBean> listRoundBean) {
        List<RoundBean> listRoundBeanNew = new ArrayList<>();
        for (RoundBean roundBean : listRoundBean) {
            Map<Integer, List<StudentBean>> mapStudentBean = new HashMap<>();
            List<StudentBean> listStudentBeen = roundBean.getListStudentBean();
            List<StudentBean> listStudentGroup = new ArrayList<>();
//            StudentBean studentBeanMain = null;
            int noGroup = 1;
            for (StudentBean studentBean : listStudentBeen) {
//                if (studentBeanMain == null) {
////                    studentBeanMain = studentBean;
//                }
                double distanceBetweenStudent = UtilityDriver.distance(studentBean.getLatitude(), studentBean.getLongitude(), studentBean.getLatitude(), studentBean.getLongitude(), "M");
                if (distanceBetweenStudent <= 100) {
                    studentBean.setGroup(noGroup);
                    listStudentGroup.add(studentBean);
                } else {
                    listStudentGroup = new ArrayList<>();
                    listStudentGroup.add(studentBean);
                    studentBean = studentBean;
                    ++noGroup;
                    studentBean.setGroup(noGroup);
                }
                mapStudentBean.put(noGroup, listStudentGroup);
                RoundPresenter.MyComparator comp = new RoundPresenter.MyComparator(mapStudentBean);

                Map<Integer, List<StudentBean>> newMap = new TreeMap(comp);
                newMap.putAll(mapStudentBean);
                roundBean.setMapStudentBean(newMap);
            }
            listRoundBeanNew.add(roundBean);
            System.err.print(mapStudentBean + "");
        }
        return listRoundBeanNew;
    }


    private void initVars() {
        StaticValue.latitudeSpeed = 0;
        StaticValue.longitudeSpeed = 0;
        UtilityDriver.setBooleanShared(UtilityDriver.TYPE_ROUND, false);
        RoundInfoFragment.CHECK_OUT_ALL = false;
        RoundInfoFragment.listGeofenceBean = null;

    }

//    private ArrayList<RoundBean> getListFromDatabBase() {
//        ArrayList<RoundBean> storedList = new ArrayList<>();
//        try {
//            storedList.addAll(parceRoundJson(new JSONObject(DAO.getAllRoundList(Application.database).get(0))));
//        } catch (Exception e) {
//
//        }
//
//        return storedList;
//    }

}
