package schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;
import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseFragment;
import schooldriver.trackware.com.school_bus_driver_android.bean.RoundBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.enums.CheckEnum;
import schooldriver.trackware.com.school_bus_driver_android.enums.StatusRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.enums.TypeRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.fragment.NotificationsListsFragment;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.dialogRoundInfo.ConfirmMessageDialog;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.dialogRoundInfo.SendNotificationDialog;
import schooldriver.trackware.com.school_bus_driver_android.gcmNotification.senderNotification.SendNotificationGCM;
import schooldriver.trackware.com.school_bus_driver_android.geofence.mock.Constants;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.IActionDialog;
import schooldriver.trackware.com.school_bus_driver_android.locationListener.LocationListener;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class RoundInfoFragment extends BaseFragment implements IActionDialog, IRestCallBack {


    public static String TYPE_CHECK ="";
    static RecyclerView rsStudent;
    public static RoundBean roundBean;
    public static RoundBean roundBeanCheck;
    static RoundInfoAdapter roundInfoAdapter;
    static RoundInfoPresenter mPresenter;
    public static ImageView imgLocation;
    public static TextView labTimer,labCheckIn;
            //,
//            labNotification
//            ;
    public static ArrayList<LatLng> listGeofenceBean;
    public static View imgLogout, imgSendMessage;
    static Button imgTypeRound;
    View linNotification,linBack;
    public static int ROUND_ID_SOCKET;
    public static StatusRoundEnum statusRoundEnum;

    public static int COUNT = 0;
    //    public static  int COUNT_DROP_OFF = 0;
    public static boolean CHECK_OUT_ALL;
    public static RoundInfoFragment roundInfoStart;
    LinearLayout lliTittle;
    TextView labNameRound;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_round_info, container, false);
        mPresenter = new RoundInfoPresenter(getActivity(), RoundInfoFragment.this);
        Bundle extras = getArguments();
        roundBean = (RoundBean) extras.getSerializable(UtilityDriver.ROUND);
        ROUND_ID_SOCKET = (int) roundBean.getId();
        statusRoundEnum = roundBean.getStatusRoundEnum();
        rsStudent = (RecyclerView) view.findViewById(R.id.rsStudent);
        lliTittle = (LinearLayout) view.findViewById(R.id.lliTittle);
        linBack = (LinearLayout) view.findViewById(R.id.linBack);
        labNameRound = (TextView) view.findViewById(R.id.labNameRound);
        labNameRound.setText(roundBean.getNameRound());
        if (roundBean.getListStudentBean().get(0).getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
            lliTittle.setBackgroundColor(getActivity().getResources().getColor(R.color.color_green));
        }else  if (roundBean.getListStudentBean().get(0).getTypeRoundEnum() == TypeRoundEnum.DROP_ROUND) {
            lliTittle.setBackgroundColor(getActivity().getResources().getColor(R.color.color_red));
        }else {
            lliTittle.setBackgroundColor(getActivity().getResources().getColor(R.color.color_blue));
        }

//        labNumberBus = (TextView) view.findViewById(R.id.labNumberBus);
//        labNameRoundTime = (TextView) view.findViewById(R.id.labNameRoundTime);

        linNotification =  view.findViewById(R.id.linNotification);
//        labNotification = (TextView) view.findViewById(R.id.labNotification);
        imgLogout =  view.findViewById(R.id.imgLogout);
//        imgCheckOutAll = (ImageView) view.findViewById(R.id.imgCheckOutAll);
//        labCheckOutAll = (TextView) view.findViewById(R.id.labCheckOutAll);
        imgTypeRound = (Button) view.findViewById(R.id.imgTypeRound);
//        labTypeRound = (TextView) view.findViewById(R.id.labTypeRound);
        imgLocation = (ImageView) view.findViewById(R.id.imgLocation);
        imgSendMessage = (ImageView) view.findViewById(R.id.imgSendMessage);
        labTimer = (TextView) view.findViewById(R.id.labTimer);
        labCheckIn = (TextView) view.findViewById(R.id.labCheckIn);

        if (roundBean.isRoundStartedNow()) {
//            labTypeRound.setText(mActivity.getString(R.string.end_round));
            imgTypeRound.setBackgroundResource(R.drawable.img_end_round);
            imgTypeRound.setText(R.string.end);
            UtilityDriver.setBooleanShared(UtilityDriver.TYPE_ROUND, true);
            roundBean.setStatusRoundEnum(StatusRoundEnum.END);
//            roundBean.setRoundsStart(true);
            setListAdapters(roundBean.getListStudentBean());
            roundBeanCheck = roundBean;
        }
        if (roundBean.isRoundEndedForEver()) {
//            labTypeRound.setText(mActivity.getString(R.string.start_round));
            imgTypeRound.setBackgroundResource(R.drawable.img_start_round);
            imgTypeRound.setText(R.string.start);
            UtilityDriver.setBooleanShared(UtilityDriver.TYPE_ROUND, false);
            roundBean.setStatusRoundEnum(StatusRoundEnum.FULL);
//            roundBean.setRoundsStart(true);
            roundBeanCheck = null;
        }
        RoundInfoFragment.statusRoundEnum = roundBean.getStatusRoundEnum();
        checkRoundView();
//        if (roundBean.isRoundsEnd() && roundBean.isRoundsStart()) {
//            imgTypeRound.setEnabled(false);
//        }
        clickListenerFragment();
//        labNumberBus.setText(roundBean.getId() + "");
//        labNameRoundTime.setText(roundBean.getNameRound() + " " + roundBean.getDateTime() + " \n" + extras.getString(UtilityDriver.TYPE_ROUND));
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL);
        rsStudent.setLayoutManager(gridLayoutManager);


        setListAdapters(roundBean.getListStudentBean());
        String response = roundBean.getGeofence();
        TYPE_CHECK = "";
        listGeofenceBean = new ArrayList<>();
        if (response.contains("id")) {
            try {
                JSONArray joGeofence = new JSONArray(response);
                JSONArray jaGeofence = joGeofence.getJSONObject(0).getJSONObject("shape_attrs").getJSONArray("vertices");
                for (int i = 0; i < jaGeofence.length(); i++) {
                    JSONObject joLatLang = jaGeofence.getJSONObject(i);
                    listGeofenceBean.add(new LatLng(joLatLang.getDouble("lat"), joLatLang.getDouble("lng")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    public static void setEnableFragment() {

        if (roundBean.getListStudentBean().get(0).getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
            if (statusRoundEnum == StatusRoundEnum.START) {
//                imgCheckOutAll.setEnabled(false);
                imgLocation.setEnabled(false);
                imgLocation.setEnabled(false);
                imgSendMessage.setEnabled(false);
            } else if (statusRoundEnum == StatusRoundEnum.END) {
//                imgCheckOutAll.setEnabled(true);
                imgLocation.setEnabled(true);
                imgLocation.setEnabled(true);
                imgSendMessage.setEnabled(true);
                imgTypeRound.setEnabled(true);
            } else if (statusRoundEnum == StatusRoundEnum.FULL) {
//                imgCheckOutAll.setEnabled(false);
                imgLocation.setEnabled(false);
                imgLocation.setEnabled(false);
                imgSendMessage.setEnabled(false);
                imgTypeRound.setEnabled(false);
            }
        }
    }

    private void checkRoundView() {
        if (roundBean.getListStudentBean() != null || roundBean.getListStudentBean().size() != 0) {
            if (roundBean.getListStudentBean().get(0).getTypeRoundEnum() == TypeRoundEnum.DROP_ROUND) {
//                imgCheckOutAll.setVisibility(View.INVISIBLE);
//                labCheckOutAll.setVisibility(View.INVISIBLE);
            }
        }
    }


    private void clickListenerFragment() {

        linBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean value = businessCancel();
                if (!value){
                    MainActivity.showFragmentRound();
                }
            }
        });

        if (roundBean.getListStudentBean().get(0).getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
            if (!roundBean.isRoundStartedNow())
                mPresenter.checkStatusRound(roundBean, confirmMessageDialog);
        }
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilityDriver.showMessageLogout(getActivity());
            }
        });
        linNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticValue.SUM_NOTIFICATION = 0;
//                labNotification.setVisibility(View.INVISIBLE);
                setFragment(NotificationsListsFragment.newInstance(),null,false);


            }
        });

        imgTypeRound.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if (StaticValue.progressDialog != null) {
                                                    StaticValue.progressDialog.show();
                                                }
                                                RoundInfoFragment.COUNT = 0;
                                                for (StudentBean studentBean : roundBean.getListStudentBean()) {
                                                    if (studentBean.getCheckEnum() == CheckEnum.CHECK_IN) {
                                                        RoundInfoFragment.COUNT++;
                                                    } else if (studentBean.getCheckEnum() == CheckEnum.CHECK_OUT) {
                                                        RoundInfoFragment.COUNT++;
                                                    } else if (studentBean.isAbsent() || studentBean.isNoShow()) {
                                                        RoundInfoFragment.COUNT++;
                                                    }
                                                }
                                                if (roundBean.getListStudentBean().get(0).getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {

                                                    if (roundBean.getStatusRoundEnum() == StatusRoundEnum.END) {

                                                        if (RoundInfoFragment.COUNT == roundBean.getListStudentBean().size()) {

                                                            mPresenter.checkStatusRound(roundBean, confirmMessageDialog);
                                                        } else {
                                                            if (RoundInfoFragment.COUNT == 0) {
                                                                businessCancel();
                                                            } else {
                                                                UtilityDriver.showMessageDialog(getActivity(), "", getActivity().getString(R.string.please_finish_round));
                                                            }
                                                        }
                                                    } else {
                                                        StaticValue.listNearby = new ArrayList<>();
                                                        mPresenter.checkStatusRound(roundBean, confirmMessageDialog);
                                                    }
                                                } else {


                                                    if (roundBean.getStatusRoundEnum() == StatusRoundEnum.END) {
                                                        int countCheckOut = 0;
                                                        if (RoundInfoFragment.COUNT >= roundBean.getListStudentBean().size()) {
                                                            for (StudentBean studentBean : roundBean.getListStudentBean()) {
                                                                if (studentBean.getCheckEnum() == CheckEnum.CHECK_OUT) {
                                                                    countCheckOut++;
                                                                } else if (!studentBean.isNoShow()) {
                                                                    countCheckOut++;
                                                                }
                                                            }
                                                            if (countCheckOut <= 0) {
                                                                businessCancel();
                                                            } else {
                                                                mPresenter.checkStatusRound(roundBean, confirmMessageDialog);
                                                            }


                                                        } else {
                                                            UtilityDriver.showMessageDialog(getActivity(), "", getActivity().getString(R.string.please_finish_round));

                                                        }
                                                    } else {
                                                        if (RoundInfoFragment.COUNT >= roundBean.getListStudentBean().size()) {
                                                            mPresenter.checkStatusRound(roundBean, confirmMessageDialog);
                                                        } else {
                                                            if (RoundInfoFragment.COUNT == 0) {
                                                                UtilityDriver.showMessageDialog(getActivity(), "", getActivity().getString(R.string.pelase_check_in_student));
                                                            } else {
                                                                businessCancel();
//                                                                UtilityDriver.showMessageDialog(getActivity(), "", getActivity().getString(R.string.please_finish_round));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

        );
        imgLocation.setVisibility(View.VISIBLE);
        imgTypeRound.setVisibility(View.VISIBLE);
        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable(UtilityDriver.ROUND, roundBean);
                MainActivity.showFragmentMap(bundle);
            }
        });

        imgSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roundInfoAdapter != null)
                    new SendNotificationDialog(getActivity(), roundBean.getListStudentBean(), (Integer) roundBean.getId()).show();
            }
        });
    }

    public static void setListAdapters(List<StudentBean> listStudentBean) {
        try {


//        RoundInfoFragment.COUNT = 0;
//        setEnableFragment();
//        roundBean.setListStudentBean(listStudentBean);
//        if (UtilityDriver.getStringShared(UtilityDriver.LANGUAGE).equals("ar")) {
//            rsStudent.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        }
//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        rsStudent.setLayoutManager(mLayoutManager);
//        roundInfoAdapter = new RoundInfoAdapter(listStudentBean, getActivity(), mPresenter, roundBean);
//        rsStudent.setAdapter(roundInfoAdapter);

            if (listStudentBean!=null) {
                if (UtilityDriver.getStringShared(UtilityDriver.LANGUAGE).equals("ar")) {
                    rsStudent.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                }
                // use a linear layout manager
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
                rsStudent.setLayoutManager(mLayoutManager);
                roundInfoAdapter = new RoundInfoAdapter(listStudentBean, mActivity, mPresenter, roundBean);
                rsStudent.setAdapter(roundInfoAdapter);
            }
        }catch (NullPointerException e){
            UtilityDriver.showMessageDialog(mActivity,mActivity.getString(R.string.failed)+": SAMSOMSAMSOM",e.getMessage());
        }
    }


    @Override
    public void done(StatusRoundEnum statusRoundEnum) {
        try {
//            final int store_order = UtilityDriver.getIntShared(UtilityDriver.STORE_ORDER) + 1;
//            UtilityDriver.setIntShared(UtilityDriver.STORE_ORDER, store_order);
//            callRestAPI(PathUrl.MAIN_URL + PathUrl.BUS_LOCATION
//                    ,
//                    new HashMap() {{
//                        put("long", StaticValue.longitudeMain);
//                        put("lat", StaticValue.latitudeMain);
//                        put("bus_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.BUS_ID)));
//                        put("school_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.SCHOOL_ID)));
//                        put("round_id", RoundInfoFragment.ROUND_ID_SOCKET);
//                        put("store_order", store_order);
//                    }}
//                    ,
//                    EnumMethodApi.POST
//                    ,
//                    RoundInfoFragment.this
//                    ,
//                    EnumNameApi.BUS_LOCATION
//                    ,
//                    UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
//                    ,
//                    EnumTypeHeader.JSON
//            );
            if (statusRoundEnum == StatusRoundEnum.START) {

//            if (MainActivity.locationListener!=null){
                Location location = new Location("123");
                location.setLongitude(StaticValue.longitudeMain);
                location.setLatitude(StaticValue.latitudeMain);
                LocationListener.checkNearbyDistance(location);
                MainActivity.TIMER_START();
//            }
//                labTypeRound.setText(getActivity().getString(R.string.end_round));
                imgTypeRound.setBackgroundResource(R.drawable.img_end_round);
                imgTypeRound.setVisibility(View.VISIBLE);
                imgTypeRound.setText(R.string.end);
                UtilityDriver.setBooleanShared(UtilityDriver.TYPE_ROUND, true);
                roundBean.setStatusRoundEnum(StatusRoundEnum.END);
//                roundBean.setRoundsStart(true);
                if (roundBean.getListStudentBean().get(0).getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
                    setListAdapters(roundBean.getListStudentBean());
                    roundBeanCheck = roundBean;
                }else {
                    List<StudentBean> listStudentBean = roundBean.getListStudentBean();
                    Collections.reverse(roundBean.getListStudentBean());
                    setListAdapters(listStudentBean);
                    roundBean.setListStudentBean(listStudentBean);
                    roundBeanCheck = roundBean;
                }
//            if (roundBean.getListStudentBean().get(0).getTypeRoundEnum()==TypeRoundEnum.DROP_ROUND) {
//                RoundInfoFragment.COUNT_DROP_OFF = 0;
//            }
                try {
                    if (MainActivity.mMap != null && MainActivity.mMap.getMyLocation() != null) {
                        StaticValue.latitudeStartRound = MainActivity.mMap.getMyLocation().getLatitude();
                        StaticValue.longitudeStartRound = MainActivity.mMap.getMyLocation().getLongitude();
                    } else {
                        StaticValue.latitudeStartRound = StaticValue.latitudeMain;
                        StaticValue.longitudeStartRound = StaticValue.longitudeMain;
                    }
                } catch (IllegalStateException e) {
                    if (ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        MainActivity.mMap.setMyLocationEnabled(true);
                    } else {

                    }
//                done(statusRoundEnum);
                }
            } else {
                RoundInfoFragment.COUNT = 0;
                StaticValue.handler.removeCallbacks(StaticValue.runnable);
                if (roundBean.getListStudentBean().get(0).getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
                    for (StudentBean studentBean : roundBean.getListStudentBean()) {
                        if (studentBean.isAbsent()) {
                            break;
                        }

                        String statusValue = "out";
                        if (!UtilityDriver.isEmptyString(statusValue)) {

//                            String messageType = "";
//                            if (studentBean.getMobileStudentBean().getPlatform().contains("android")) {
//                                messageType = new JSONObject(mapBodyMessage).toString();
//                            } else {
//                                messageType = UtilityDriver.MESSAGE_IPHONE.replaceAll("MESSAGEMESSAGE", sendMessage);
//                            }
                            if (studentBean.getMobileStudentBean().isCheckInFather()) {
                                String sendMessage  = UtilityDriver.getMessageNotification(statusValue, studentBean.getTypeRoundEnum(),studentBean.getMobileStudentBean().getFatherLocal());
                                sendMessage = sendMessage.replaceAll(STUDENT_NAME_API_FORMAT, studentBean.getFirstNameStudent());
                                SendNotificationGCM sendNotificationGCM = new SendNotificationGCM();
                                Map<String, String> mapBodyMessage = new HashMap<>();
                                mapBodyMessage.put("message", sendMessage);
//                                mapBodyMessage.put("avatar", studentBean.getAvatar());
                                mapBodyMessage.put("title", SendNotificationGCM.createNotificationTitle(studentBean.getMobileStudentBean().getFatherLocal()));
                                mapBodyMessage.put("action", Constants.NOTIFICATION_ACTION_Other);


                                sendNotificationGCM.sendNotification(false,studentBean.getMobileStudentBean().getFatherToken(), studentBean.getMobileStudentBean().getFatherMessageType(mapBodyMessage,sendMessage), (Integer) studentBean.getId(), (Integer) roundBean.getId(), studentBean.getMobileStudentBean().getFatherPlatform(), studentBean.getAvatar(),studentBean.getMobileStudentBean().getFatherLocal(),studentBean.getNameStudent(),studentBean.getMobileStudentBean().getFatherId());
                            }
                            if (studentBean.getMobileStudentBean().isCheckInMother()) {
                                String sendMessage = UtilityDriver.getMessageNotification(statusValue, studentBean.getTypeRoundEnum(),studentBean.getMobileStudentBean().getMotherPlatform());
                                sendMessage = sendMessage.replaceAll(STUDENT_NAME_API_FORMAT, studentBean.getFirstNameStudent());
                                SendNotificationGCM sendNotificationGCM = new SendNotificationGCM();
                                Map<String, String> mapBodyMessage = new HashMap<>();
                                mapBodyMessage.put("message", sendMessage);
//                                mapBodyMessage.put("avatar", studentBean.getAvatar());
                                mapBodyMessage.put("title", SendNotificationGCM.createNotificationTitle(studentBean.getMobileStudentBean().getMotherLocal()));
                                mapBodyMessage.put("action", Constants.NOTIFICATION_ACTION_Other);


                                sendNotificationGCM.sendNotification(false,studentBean.getMobileStudentBean().getMotherToken(), studentBean.getMobileStudentBean().getMotherMessageType(mapBodyMessage,sendMessage), (Integer) studentBean.getId(), (Integer) roundBean.getId(), studentBean.getMobileStudentBean().getMotherPlatform(), studentBean.getAvatar(),studentBean.getMobileStudentBean().getMotherLocal(),studentBean.getNameStudent(),studentBean.getMobileStudentBean().getMotherId());
                            }
                        }
                    }
                }
//                labTypeRound.setText(getActivity().getString(R.string.start_round));
                imgTypeRound.setBackgroundResource(R.drawable.img_start_round);
                imgTypeRound.setText(R.string.start);
                UtilityDriver.setBooleanShared(UtilityDriver.TYPE_ROUND, false);
                roundBean.setStatusRoundEnum(StatusRoundEnum.FULL);
//                roundBean.setRoundsStart(true);
                MainActivity.showFragmentRound();
                roundBeanCheck = null;
//            MainActivity.showFragmentRound();
                StaticValue.latitudeStartRound = 0;
                StaticValue.longitudeStartRound = 0;

//                deleteCache(getActivity());
            }

            RoundInfoFragment.statusRoundEnum = roundBean.getStatusRoundEnum();
            setListAdapters(roundBean.getListStudentBean());
        } catch (NullPointerException e) {
            UtilityDriver.showMessageDialog(getActivity(),mActivity.getString(R.string.failed)+": OSAMAOSAMA",e.getMessage());
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void done(String message) {
        if (mActivity.getString(R.string.check_out_all).equals(message)) {
            CHECK_OUT_ALL = true;

        }
    }

    @Override
    public void cancel() {

    }

    //    AlertDialog.Builder adb;
    @Override
    public void onResume() {
        super.onResume();
        roundInfoStart = RoundInfoFragment.this;


//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    businessCancel();
//
//                    return false;
//                }
//                return false;
//            }
//        });

    }

    private boolean businessCancel() {

        boolean value = false;
        if (roundBean.getListStudentBean().get(0).getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
            if (UtilityDriver.getBooleanShared(UtilityDriver.TYPE_ROUND)) {
//                    showDialogCancel();
                if (RoundInfoFragment.COUNT == 0) {
                    value = true;
                    showDialogCancel();
                } else {
                    UtilityDriver.showMessageDialog(getActivity(), "", mActivity.getString(R.string.please_finish_round));
                }
            }
        } else if (roundBean.getListStudentBean().get(0).getTypeRoundEnum() == TypeRoundEnum.DROP_ROUND) {
            for (StudentBean studentBean : roundBean.getListStudentBean()) {
                if (studentBean.getCheckEnum() == CheckEnum.CHECK_IN) {
                    RoundInfoFragment.COUNT++;
                } else if (studentBean.getCheckEnum() == CheckEnum.CHECK_OUT) {
                    RoundInfoFragment.COUNT++;
                } else if (studentBean.isAbsent() || studentBean.isNoShow()) {
                    RoundInfoFragment.COUNT++;
                }
            }
            if (!UtilityDriver.getBooleanShared(UtilityDriver.TYPE_ROUND) && RoundInfoFragment.COUNT > 0 && roundBean.getStatusRoundEnum() != StatusRoundEnum.FULL) {
                value = true;
                showDialogCancel();
            } else if (UtilityDriver.getBooleanShared(UtilityDriver.TYPE_ROUND) && RoundInfoFragment.COUNT > 0 && roundBean.getStatusRoundEnum() == StatusRoundEnum.END) {
                UtilityDriver.showMessageDialog(getActivity(), mActivity.getString(R.string.error), mActivity.getString(R.string.error_cancel));
            }
        }
        return value;
    }

    ConfirmMessageDialog confirmMessageDialog;

    private void showDialogCancel() {


        if (confirmMessageDialog == null)
            confirmMessageDialog = new ConfirmMessageDialog(getActivity());
        confirmMessageDialog.showDialogCancel(getActivity(),(Integer) roundBean.getId(), RoundInfoFragment.this);
        if (!confirmMessageDialog.isShowing()) {
            confirmMessageDialog.show();
        }
//        if (adb==null) {
//            adb = new AlertDialog.Builder(getActivity());
//
//
//            adb.setTitle(getActivity().getString(R.string.alarm));
//            adb.setMessage(getActivity().getString(R.string.cancel_round));
//            adb.setIcon(android.R.drawable.ic_dialog_alert);
//
//
//            adb.setPositiveButton(getActivity().getString(R.string.ok), new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    adb =null;
////                                    MainActivity.showFragmentRound();

//                    dialog.dismiss();
//                }
//            });
//
//
//            adb.setNegativeButton(mActivity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    adb =null;
//                    dialog.dismiss();
//                }
//            });
//
//            adb.show();
//        }
    }

//    @Override
//    public void onPause() {
//        super.onPause();
////        roundInfoStart = null;
//    }

    @Override
    public void onRestCallBack(String response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

    }

    @Override
    public void onRestCallBack(JSONObject response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

        if (EnumNameApi.CANCEL_ROUND == nameApiEnum) {
            try {
                String outPut = response.getString("status");
                if (outPut.toLowerCase().equals("ok")) {
                    StaticValue.handler.removeCallbacks(StaticValue.runnable);
                    StaticValue.listNearby = new ArrayList<>();
                    MainActivity.showFragmentRound();

                } else {
                    UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.failed), outPut);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRestCallBack(VolleyError volleyError, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
        if (volleyError.getMessage().toString().contains("java.net.UnknownHost")) {
            UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.internet_connection), mActivity.getString(R.string.missing_internet_error));
            return;
        }
        if (EnumNameApi.CANCEL_ROUND == nameApiEnum) {

        }
    }

    @Override
    public void onRestCallBack(NetworkResponse networkResponse, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

    }
}
