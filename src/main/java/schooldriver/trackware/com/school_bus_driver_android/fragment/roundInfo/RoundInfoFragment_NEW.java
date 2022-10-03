package schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ApiController;
import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ListenersAndInterFaces.OnApiComplete;
import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.adapters.RecyclerViewAdapterWithDragDrop;
import schooldriver.trackware.com.school_bus_driver_android.app.Application;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseFragment;
import schooldriver.trackware.com.school_bus_driver_android.bean.ReorderedRequest;
import schooldriver.trackware.com.school_bus_driver_android.bean.RoundBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.enums.CheckEnum;
import schooldriver.trackware.com.school_bus_driver_android.enums.EnumConfigNotify;
import schooldriver.trackware.com.school_bus_driver_android.fragment.changeLocation.ChangeLocationFragment;
import schooldriver.trackware.com.school_bus_driver_android.fragment.map.MapFragment;
import schooldriver.trackware.com.school_bus_driver_android.gcmNotification.senderNotification.SendNotificationGCM;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.OnActionDoneListener;
import schooldriver.trackware.com.school_bus_driver_android.locationListener.ConfigNotify;
import schooldriver.trackware.com.school_bus_driver_android.locationListener.LocationListener;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.DrawableToolsV2;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilDialogs;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilTools;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilViews;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.VolleySingleton;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public abstract class RoundInfoFragment_NEW extends BaseFragment {

    protected RecyclerView rsStudent;
    protected RoundBean roundBean;
    protected TextView labTimer, start_end_round_text, labNameRound;
    protected AppCompatImageView start_end_round_image;
    //    protected boolean toResumeRound = false;
//    protected boolean roundStarted = false;
//    protected boolean endingRound = false;
    //    protected boolean finishedAdding = true;
    protected View imgLocation, imgSendMessage, linNotification, linBack, start_round_view, end_round_view, imgLocation_imgSendMessage_view_container;
    protected SendNotificationGCM sendNotificationGCM;
    protected String statusCheckIn = "in";
    protected String statusCheckOut = "out";
    protected String statusAbsent = "absent";
    protected String statusNo_Show = "no-show";
    protected RecyclerViewAdapterWithDragDrop<StudentBean, ?> roundAdapter;
    protected HashSet<Integer> studentHaseBeaconsButStillInBus = new HashSet<>();
    //    protected ArrayList<String[]> toastMessagesList = new ArrayList<>();
//    private Handler dialogeHandler = new Handler();
    private int currentRepeat = 0;
    private MediaPlayer mp;
    private View edit_mode_img, edit_mode_view, save_edit_mode, cancel_edit_mode, start_end_round_button_view;
    private OnActionDoneListener<Boolean> onEditModeChangedListener = null;
    /**/


    protected void findViews(View view) {
        labTimer = view.findViewById(R.id.labTimer);
        labNameRound = view.findViewById(R.id.labNameRound);
        start_end_round_text = view.findViewById(R.id.start_end_round_text);
        start_end_round_image = view.findViewById(R.id.start_end_round_image);
        rsStudent = view.findViewById(R.id.rsStudent);
        linNotification = view.findViewById(R.id.linNotification);
        linBack = view.findViewById(R.id.linBack);
        start_round_view = view.findViewById(R.id.start_round_view);
        end_round_view = view.findViewById(R.id.end_round_view);
        imgLocation = view.findViewById(R.id.imgLocation);
        imgSendMessage = view.findViewById(R.id.imgSendMessage);
        imgLocation_imgSendMessage_view_container = view.findViewById(R.id.imgLocation_imgSendMessage_view_container);
        edit_mode_img = view.findViewById(R.id.edit_mode_img);
        edit_mode_view = view.findViewById(R.id.edit_mode_view);
        save_edit_mode = view.findViewById(R.id.save_edit_mode);
        cancel_edit_mode = view.findViewById(R.id.cancel_edit_mode);
        start_end_round_button_view = view.findViewById(R.id.start_end_round_button_view);
//        ((SwitchCompat) view.findViewById(R.id.with_amazon)).setChecked(SendNotificationGCM.WITH_AMAZON);
//        ((SwitchCompat) view.findViewById(R.id.with_amazon)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                SendNotificationGCM.WITH_AMAZON = isChecked;
//            }
//        });
    }


    protected void hideEditMode() {
        edit_mode_img.setVisibility(View.INVISIBLE);
        edit_mode_view.setVisibility(View.GONE);
    }

    protected void initFirstTimeEditMode() {
        edit_mode_img.setVisibility(View.VISIBLE);
        edit_mode_view.setVisibility(View.GONE);
        /**/
        edit_mode_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditMode();
            }
        });


        setOnEditModeChangedListener(new OnActionDoneListener<Boolean>() {
            @Override
            public void OnActionDone(Boolean Action) {
                if (roundAdapter != null)
                    roundAdapter.notifyDataSetChanged();
            }
        });
        onEditModeChangedListener.OnActionDone(isInEditMode());
        start_end_round_button_view.setVisibility(View.VISIBLE);
    }

    public RoundInfoFragment_NEW setOnEditModeChangedListener(OnActionDoneListener<Boolean> onEditModeChangedListener) {
        this.onEditModeChangedListener = onEditModeChangedListener;
        return this;
    }

    List<StudentBean> valuesBeforeEdit = new ArrayList<>();

    protected void startEditMode() {
        valuesBeforeEdit.clear();
        valuesBeforeEdit.addAll(roundAdapter.getValues());

        edit_mode_view.setVisibility(View.VISIBLE);
        /**/
        save_edit_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundBean.setListStudentBean(roundAdapter.getValues());
                roundAdapter.setRootList(roundAdapter.getValues());
                initFirstTimeEditMode();
                sendStudentsOrderToAPI((ArrayList<StudentBean>) roundAdapter.getValues());

            }
        });
        cancel_edit_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundAdapter.newList(valuesBeforeEdit);

                initFirstTimeEditMode();
            }
        });
        onEditModeChangedListener.OnActionDone(isInEditMode());
        start_end_round_button_view.setVisibility(View.GONE);

    }

    protected boolean isInEditMode() {
        return edit_mode_view.getVisibility() == View.VISIBLE;
    }

    protected void setTopBarOnClickListeners() {
        linBack.setOnClickListener(backClickListener);
        linNotification.setOnClickListener(notificationIconClickListener);
        /**/
        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                roundBean.setListStudentBean(roundAdapter.getValues());
                bundle.putSerializable(UtilityDriver.ROUND, roundBean);
                setFragment(new MapFragment(), bundle, false);

            }
        });
        imgSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSendMessageDialog(roundBean);
//                roundAdapter.notifyDataSetChanged();
            }
        });
        initPlayer();
    }

    @Override
    public void onStop() {
        MainActivity.CURRENT_SELECTED_ROUND = null;
        super.onStop();

    }

    protected boolean isIn_100M_Range(double longitude_from, double latitude_from, double longitude_to, double latitude_to) {

        try {
            float[] distancearray = new float[2];
            Location.distanceBetween(longitude_from, latitude_from, longitude_to, latitude_to, distancearray);
            return distancearray[0] < 100;
        } catch (Exception e) {
            return false;
        }

    }

    protected boolean isIn_500M_Range(double longitude_from, double latitude_from, double longitude_to, double latitude_to) {
        try {
            float[] distancearray = new float[2];
            Location.distanceBetween(longitude_from, latitude_from, longitude_to, latitude_to, distancearray);
            return distancearray[0] < 500;
        } catch (Exception e) {
            return false;
        }

    }

    protected void sendNotificationForNextStudent(StudentBean studentBean) {
        try {
            Log.d("sendNotificationForNextStudent", String.valueOf(studentBean));
            int nextStudentToTake = getPositionOfThisStudentInAdapter(studentBean.getId()) + 1;
            StudentBean nextStudent = roundAdapter.getValues().get(nextStudentToTake);
//            List<StudentBean> mustListTakenStudent = getMustTakenStudentList();
//            String school_order = UtilityDriver.getStringShared(UtilityDriver.SCHOOL_ORDER);
//                    Log.d("mustListTakenStudent", String.valueOf(mustListTakenStudent.size()));
//            if (school_order.equals("false")) {

//                for (int i = 0; i <= mustListTakenStudent.size(); i++) {
////                            Log.d("mustListTakenStudent.size()1111ss", String.valueOf(mustListTakenStudent.get(i).getLongitude()));
//                    if (isIn_500M_Range(currentLongitude, currentLatitude, mustListTakenStudent.get(i).getLongitude(), mustListTakenStudent.get(i).getLatitude())) {
////                            Log.d("isIn_500M_Range1111ss", String.valueOf(mustListTakenStudent.get(i).getNameStudent()));
//                        sendNearByNotification(mustListTakenStudent.get(i), roundBean.getIdAsString());
//                        sendNotificationForNextStudent(mustListTakenStudent.get(i));
//                        for (int j = 0; j <= mustListTakenStudent.size(); j++) {
//                            if (isIn_500M_Range(mustListTakenStudent.get(i).getLongitude(), mustListTakenStudent.get(i).getLatitude(), mustListTakenStudent.get(j).getLongitude(), mustListTakenStudent.get(j).getLatitude())) {
////                                    Log.d("isIn_500M_Range1111ssffffffffffffffffff", String.valueOf(mustListTakenStudent.get(i).getNameStudent()));
//                                sendNearByNotification(mustListTakenStudent.get(j), roundBean.getIdAsString());
//                                sendNotificationForNextStudent(mustListTakenStudent.get(j));
//                            }
//                        }
//                    }
//                }
//            }
//            else {
            if (isIn_100M_Range(studentBean.getLongitude(), studentBean.getLatitude(), nextStudent.getLongitude(), nextStudent.getLatitude())) {
                sendNearByNotification(nextStudent, roundBean.getIdAsString());
                sendNotificationForNextStudent(nextStudent);
            }
//            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    protected abstract void sendNearByNotification(StudentBean studentBean, String roundId);

    protected HashSet<Integer> sentNearby = new HashSet<>();


    protected void clearNearByListeners() {
        try {
            sentNearby.clear();
            LocationListener.nearByChecker = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void initForNearBy() {
        sentNearby.clear();
        LocationListener.nearByChecker = new OnActionDoneListener<Location>() {
            @Override
            public void OnActionDone(Location currentBusLocation) {
                try {
                    /**/
                    if (!roundBean.isRoundStartedNow()) {
                        return;
                    }
                    /**/
                    double currentLongitude = currentBusLocation.getLongitude();
                    double currentLatitude = currentBusLocation.getLatitude();
                    /**/
                    StudentBean mustTakenStudent = getMustTakenStudent();
                    List<StudentBean> mustListTakenStudent = getMustTakenStudentList();
//                    yousef
                    String school_order = UtilityDriver.getStringShared(UtilityDriver.SCHOOL_ORDER);
//                    Log.d("mustListTakenStudent", String.valueOf(school_order));
                    if (school_order.equals("false")) {
                        for (int i = 0; i <= mustListTakenStudent.size(); i++) {
//                            Log.d("mustListTakenStudent.size()1111ss", String.valueOf(mustListTakenStudent.get(i).getLongitude()));
                            if (isIn_500M_Range(currentLongitude, currentLatitude, mustListTakenStudent.get(i).getLongitude(), mustListTakenStudent.get(i).getLatitude())) {
//                            Log.d("isIn_500M_Range1111ss", String.valueOf(mustListTakenStudent.get(i).getNameStudent()));
                                sendNearByNotification(mustListTakenStudent.get(i), roundBean.getIdAsString());
                                sendNotificationForNextStudent(mustListTakenStudent.get(i));
                                for (int j = 0; j <= mustListTakenStudent.size(); j++) {
                                    if (isIn_500M_Range(mustListTakenStudent.get(i).getLongitude(), mustListTakenStudent.get(i).getLatitude(), mustListTakenStudent.get(j).getLongitude(), mustListTakenStudent.get(j).getLatitude())) {
//                                    Log.d("isIn_500M_Range1111ssffffffffffffffffff", String.valueOf(mustListTakenStudent.get(i).getNameStudent()));
                                        sendNearByNotification(mustListTakenStudent.get(j), roundBean.getIdAsString());
                                        sendNotificationForNextStudent(mustListTakenStudent.get(j));
                                    }
                                }
                            }
                        }
                    }
                    /**/
                    else {
//                        Log.d("isIn_500M_Range_else", String.valueOf(mustTakenStudent.getLongitude()));
                        if (isIn_500M_Range(currentLongitude, currentLatitude, mustTakenStudent.getLongitude(), mustTakenStudent.getLatitude())) {
//                            Log.d("isIn_500M_Range_else111111111111222222d", String.valueOf(mustTakenStudent.getLongitude()));
                            sendNearByNotification(mustTakenStudent, roundBean.getIdAsString());
                            sendNotificationForNextStudent(mustTakenStudent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        };
    }


    private void initGeofence() {
        String response = roundBean.getGeofence();
        RoundInfoFragment.listGeofenceBean = new ArrayList<>();
        if (response.contains("id")) {
            try {
                JSONArray joGeofence = new JSONArray(response);
                JSONArray jaGeofence = joGeofence.getJSONObject(0).getJSONObject("shape_attrs").getJSONArray("vertices");
                for (int i = 0; i < jaGeofence.length(); i++) {
                    JSONObject joLatLang = jaGeofence.getJSONObject(i);
                    RoundInfoFragment.listGeofenceBean.add(new LatLng(joLatLang.getDouble("lat"), joLatLang.getDouble("lng")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void oldActionsOnStart() {
        try {
            RoundInfoFragment.ROUND_ID_SOCKET = roundBean.getId();
            RoundInfoFragment.statusRoundEnum = roundBean.getStatusRoundEnum();
            initGeofence();
            UtilityDriver.setBooleanShared(UtilityDriver.TYPE_ROUND, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void oldActionsOnEnd() {
        try {
            RoundInfoFragment.ROUND_ID_SOCKET = 0;
            RoundInfoFragment.statusRoundEnum = roundBean.getStatusRoundEnum();
            initGeofence();
            UtilityDriver.setBooleanShared(UtilityDriver.TYPE_ROUND, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected UtilDialogs.MessageYesNoDialog pleaseCheckInDialog;

    protected void showPleaseCheckInDialog() {
        if (pleaseCheckInDialog != null)
            pleaseCheckInDialog.dismiss();
        pleaseCheckInDialog = new UtilDialogs.MessageYesNoDialog().show(getActivity()).
                setDialogeTitle(getString(R.string.drop_off_round_start_round_students_not_check_in))
                .hideCloseButton()
                .setYesButtonText(R.string.ok)
                .setImageWithColor(R.drawable.img_error, 0);

    }


    protected void sendGCM() {
        if (sendNotificationGCM == null) {
            sendNotificationGCM = new SendNotificationGCM();
        }

    }

    protected UtilDialogs.MessageYesNoDialog changeLocationDialog;

    protected void showChangeLocationDialog(final StudentBean student) {
        String message = getString(R.string.change_location_message).replace("@@@@@@", student.getFirstNameStudent());
        if (changeLocationDialog != null)
            changeLocationDialog.dismiss();
        changeLocationDialog = new UtilDialogs.MessageYesNoDialog().show(getMainActivity()).
//                setDialogeTitle(R.string.change_location)
        setDialogeTitle(message)
//                .setDialogeTitleTextColor(R.color.toolbar_bg_color)
                .setYesButtonText(R.string.yes_value).
                setImageWithColor(R.drawable.img_change_location, R.color.toolbar_bg_color)
//                setDialogeMessage(message)
                .setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                    @Override
                    public void OnActionDone(UtilDialogs.MessageYesNoDialog dialog) {

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("studentID", (int) student.getId());
                        setFragment(new ChangeLocationFragment(), bundle, false);
                        if (dialog != null)
                            dialog.dismiss();
                    }
                });

    }

    protected UtilDialogs.MessageYesNoDialog notOnDistance;

    protected boolean isNotOnDistance(StudentBean studentBean) {
        double distance = UtilityDriver.distance(StaticValue.latitudeMain, StaticValue.longitudeMain, studentBean.getLatitude(), studentBean.getLongitude(), "M");
        if (distance >= distanceLimit) {
            if (notOnDistance != null)
                notOnDistance.dismiss();
            notOnDistance = new UtilDialogs.MessageYesNoDialog().show(getActivity())
                    .setDialogeTitleTextColor(R.color.red_tabs)
                    .setDialogeTitle(getString(R.string.failed))
                    .hideCloseButton()
                    .setYesButtonText(R.string.ok)
                    .setDialogeMessage(getString(R.string.allow_distance));


            return true;
        }
        return false;

    }


    public void endTimer() {
        try {
            UtilityDriver.setStringShared(UtilityDriver.START_ROUND_TIME, "");
            timerHandler.removeCallbacksAndMessages(null);
            timerHandler = null;
//        UtilViews.shakeViews(labTimer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    Handler timerHandler;


    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {


            try {
                String timeFormat = getTimerText();
                Log.v("format : ", timeFormat.toString());
                labTimer.setText(timeFormat.toString());
            } catch (Exception e) {
                e.printStackTrace();
                endTimer();
            }


            /**/
            startTimer();
            /**/

        }
    };

    public void startTimer() {
        if (UtilityDriver.getStringShared(UtilityDriver.START_ROUND_TIME).trim().equals("")) {
            labTimer.setText("00:00:00");
            return;
        }
        /**/
        if (labTimer.getText().toString().trim().equals("00:00:00")) { // on first time
            String timeFormat = getTimerText();
            UtilViews.shakeViews(labTimer);
            labTimer.setText(timeFormat.toString());
        }

        /**/
        timerHandler = new Handler(Looper.getMainLooper());

        /**/
        timerHandler.postDelayed(timerRunnable, 1000);

    }


    @Override
    public void onPause() {
        super.onPause();
        getMainActivity().clearBeaconScanner();
        getMainActivity().clearScannerLisiners();
    }

    @Override
    public void onResume() {
        super.onResume();
//        getMainActivity().checkLocationPermission(true);
        getMainActivity().checkBluetoothState();
        getMainActivity().setOnBlueToothStatusChanged(new OnActionDoneListener<Boolean>() {
            @Override
            public void OnActionDone(Boolean blueToothOn) {
                try {
                    if (roundBean != null)
//                    getMainActivity().clearBeaconScanner();
//                    getMainActivity().initBeaconScanner();
                        if (blueToothOn) {
                            getMainActivity().initBeaconScanner();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getMainActivity().addNotifiers(roundBean.getListStudentBean());
                                }
                            }, 1000);


                        }

//                getMainActivity().addMacAddressToScanner(roundBean.getListStudentBean());
                } catch (Exception e) {

                }
            }
        });

        if (roundBean != null)
            MainActivity.CURRENT_SELECTED_ROUND = roundBean;
//        getMainActivity().startAndSendDataToService("onResume");
    }


//    protected void saveStudentListForStartedRound(List<StudentBean> started_round_students) {
//        DAO.deleteAllStartedRoundStudents(Application.database);
//        DAO.addStartedRoundStudents(Application.database, started_round_students);
//    }

//    protected ArrayList<StudentBean> sortStudentUsingSaved(List<StudentBean> started_round_students) {
//        ArrayList<StudentBean> afterSort = new ArrayList<>();
//        ArrayList<Integer> startedRoundStudents = DAO.getStartedRoundStudents(Application.database);
//
//        for (int i = 0; i < startedRoundStudents.size(); i++) {
//            StudentBean studentById = findStudentById(started_round_students, startedRoundStudents.get(i));
//            if (studentById!=null)
//            afterSort.add(studentById);
//        }
//
//        return afterSort;
//    }


//    private StudentBean findStudentById(List<StudentBean> studentsList, Integer id) {
//        for (int i = 0; i < studentsList.size(); i++) {
//            if (id == studentsList.get(i).getId()) {
//                return studentsList.get(i);
//            }
//        }
//        return null;
//    }


    protected void hideBackButton() {
        linBack.setVisibility(View.INVISIBLE);
    }

    protected void showBackButton() {
        linBack.setVisibility(View.VISIBLE);
    }


    protected void changeBackButtonListener(OnActionDoneListener<Object> onBackButtonClicked) {
        getMainActivity().setOnBackButtonPressed(onBackButtonClicked);
    }


    protected abstract void afterEndRound(boolean isCanceled);

    protected void handleWasEndedRound() {
//        roundStarted = false;
        start_round_view.setVisibility(View.GONE);
        end_round_view.setVisibility(View.GONE);
        labTimer.setVisibility(View.INVISIBLE);
        imgLocation_imgSendMessage_view_container.setVisibility(View.INVISIBLE);
        StaticValue.latitudeStartRound = 0.0;
        StaticValue.longitudeStartRound = 0.0;
    }


    protected void handlePausedRound() {
//        roundStarted = true;
        start_round_view.setVisibility(View.GONE);
        end_round_view.setVisibility(View.VISIBLE);
        enableCancelRoundOnBackClick();
        startTimer();
    }

    protected void enableCancelRoundOnBackClick() {
        showBackButton();
        changeBackButtonListener(new OnActionDoneListener<Object>() {
            @Override
            public void OnActionDone(Object Action) {
                showCancelRoundDialog();
            }
        });
    }

    protected void enableAlertOnBackClick() {
        showBackButton();
        changeBackButtonListener(new OnActionDoneListener<Object>() {
            @Override
            public void OnActionDone(Object Action) {
                showConfirmOnBackDialog();
            }
        });
    }

    protected UtilDialogs.MessageYesNoDialog confirmEndRoundDialog;

    protected void showConfirmEndRoundDialog() {
        if (confirmEndRoundDialog != null)
            confirmEndRoundDialog.dismiss();
        confirmEndRoundDialog = new UtilDialogs.MessageYesNoDialog().show(getActivity())
//                setDialogeTitle(R.string.end_round).setDialogeTitleTextColor(R.color.red_tabs)
                .setDialogeMessage(getString(R.string.end_the_round))
                .setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                    @Override
                    public void OnActionDone(UtilDialogs.MessageYesNoDialog dialog) {
                        /**/

                        endRound(roundBean.getId());
                        if (dialog != null)
                            dialog.dismiss();
                        /**/
                    }
                }).setImageWithColor(R.drawable.end_arrow, R.color.color_header);
    }

    protected void startRound(final int roundId, OnActionDoneListener onStartRoundonSuccess) {
        showProcessingDialog(0);
        StaticValue.listNearby = new ArrayList<>();

        ApiController.startRound(getActivity(), roundId, new OnApiComplete<Object>() {
            @Override
            public void onSuccess(Object repons) {
                /**/
                try {
                    getMainActivity().locationListener.removeCheckInStopTime();
                } catch (Exception e) {
                }

                checkNearby();//old
                oldActionsOnStart();//old
                /**/
                hideProcessingDialog();
                Log.v("startRound ", "" + repons.toString());
                if (repons.toString().toLowerCase().contains("ok")) {

                    start_round_view.setVisibility(View.GONE);
                    end_round_view.setVisibility(View.VISIBLE);
                    roundBean.startRoundNow();
//                    Collections.sort(roundAdapter.getValues());
                    roundAdapter.notifyDataSetChanged();
                    /**/
                    UtilityDriver.setStringShared(UtilityDriver.START_ROUND_TIME, System.currentTimeMillis() + " ");
                    startTimer();
//                    yousef ahmad
                    String url1 = "https://tam.trak-link.net/wialon/ajax.html?svc=token/login&params={\"token\":\"006e88fb7d566f322840883846cf56ca8A422F31FF5C2296B6DEE6240E34FEC26F12BB07\"}";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, url1, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String url = "";
                                        UtilityDriver.setStringShared(UtilityDriver.SID_TRACK_LINK, response.getString("eid"));
                                        url = "https://tam.trak-link.net/wialon/ajax.html?svc=core/search_items&params={\"spec\":{\"itemsType\":\"avl_unit\",\"propName\":\"sys_name\",\"propValueMask\":\"*" + UtilityDriver.getStringShared(UtilityDriver.SERIAL_ID) + "*\",\"sortType\":\"sys_name\"},\"force\":1,\"flags\":1,\"from\":0,\"to\":0}&sid=" + UtilityDriver.getStringShared(UtilityDriver.SID_TRACK_LINK);
                                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                                                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try {

                                                            JSONArray jsonArray = new JSONArray(response.getString("items"));
                                                            JSONObject object = jsonArray.getJSONObject(0);
                                                            UtilityDriver.setStringShared(UtilityDriver.TRACKLINK_ID, object.getString("id"));
                                                            UtilityDriver.setStringShared(UtilityDriver.ROUND_ST, "true");
                                                        } catch (JSONException e) {
                                                            Log.d("response1111111", e.getMessage());
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                }, new Response.ErrorListener() {

                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.d("response1111111", error.getMessage());
                                                    }
                                                });
                                        Application.getInstanceVolly().addToRequestQueue(jsonObjectRequest1, url);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d("response1111111", e.getMessage());
                                    }

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("response1111111", error.getMessage());
                                }
                            });
                    Application.getInstanceVolly().addToRequestQueue(jsonObjectRequest, url1);

                    /**/
                    hideBackButton();
                    enableCancelRoundOnBackClick();
//                    getMainActivity().startAndSendDataToService(BeaconService.REST_SCAN);
//                    getMainActivity().startAndSendDataToService(roundBean);
//                    getMainActivity().clearBeaconScanner();
                    RoundInfoFragment.roundBeanCheck = (RoundBean) getArguments().getParcelable(UtilityDriver.ROUND);
//                    getMainActivity().addNotifiers(roundBean.getListStudentBean());
//                    getMainActivity().addMacAddressToScanner(roundBean.getListStudentBean());
                    /**/
                    StaticValue.latitudeStartRound = StaticValue.latitudeMain;
                    StaticValue.longitudeStartRound = StaticValue.longitudeMain;
                    /**/
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (onStartRoundonSuccess != null)
                                onStartRoundonSuccess.OnActionDone(null);
                        }
                    }, 300);

                } else {
                    onError(0, "startRound Error");

                }

            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                hideProcessingDialog();
                showAPIErrorDialog(errorMessage.toString());
            }
        });
    }

    protected void endRound(final int roundId) {

        showProcessingDialog(0);
        ApiController.endRound(getActivity(), roundId, new OnApiComplete<Object>() {
            @Override
            public void onSuccess(Object repons) {
                hideProcessingDialog();
                oldActionsOnEnd();//old
                Log.v("endRound ", "" + repons.toString());
                if (repons.toString().toLowerCase().contains("ok")) {
                    afterEndRound(false);
//                    yousef
                    UtilityDriver.setStringShared(UtilityDriver.ROUND_ST, "false");
//                    roundAdapter.notifyDataSetChanged();
                } else {
                    onError(0, "endRound Error");

                }

            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                hideProcessingDialog();
                showAPIErrorDialog(errorMessage.toString());
            }
        });


    }

    protected void moveToLast(final StudentBean student_bean, final int oldLocation) {

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        roundAdapter.add(student_bean, false);
        roundAdapter.remove(oldLocation, false);
//                Collections.sort(roundAdapter.getValues());
        roundAdapter.notifyDataSetChanged();
//                finishedAdding = true;
//            }
//        }, 200);
    }

    protected void moveToLastWithAnimation(final StudentBean student_bean, final int oldLocation) {
        roundAdapter.add(student_bean, false);
        roundAdapter.remove(oldLocation, true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                roundAdapter.notifyDataSetChanged();
            }
        }, 300);
    }


    protected void refreshList() {
        roundAdapter.notifyDataSetChanged();
    }


    protected void moveToFirst(final StudentBean student_bean, final int oldLocation) {

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        roundAdapter.add(student_bean, false);
        roundAdapter.remove(oldLocation, false);
//                Collections.sort(roundAdapter.getValues());
        roundAdapter.notifyDataSetChanged();
//                finishedAdding = true;
//            }
//        }, 400);
    }


    UtilDialogs.ReasonDialog reasonDialog;

    protected UtilDialogs.ReasonDialog showCancelRoundDialog() {
        if (reasonDialog != null)
            reasonDialog.dismiss();
        reasonDialog = new UtilDialogs.ReasonDialog().show(getActivity()).
//                setDialogeTitle(R.string.cancel_round)
        setDialogeTitle(R.string.cancel_the_round)
                .setYesButtonText(R.string.temporary_stop_round)
                .showOtherButton()
                .setOtherButtonText(R.string.completely_stop_round)
                .setDialogeAdditionalMessage(R.string.end_the_round_additional)
                .setImageWithColor(R.drawable.stop, 0)
                .initReasonsViews(UtilDialogs.ReasonDialog.REASON_CANCEL)
                .setAfterResonSelectedLisiner(new OnActionDoneListener<String>() {
                    @Override
                    public void OnActionDone(String reason) {
                        cancelRound(reason);
                    }
                }, new OnActionDoneListener<String>() {
                    @Override
                    public void OnActionDone(String reason) {
                        forceEndRound(reason);
                    }
                });
        return reasonDialog;

    }

    UtilDialogs.MessageYesNoDialog confirmOnBackDialog;

    private void showConfirmOnBackDialog() {
        if (confirmOnBackDialog != null)
            confirmOnBackDialog.dismiss();
        confirmOnBackDialog = new UtilDialogs.MessageYesNoDialog().show(getActivity()).
                setDialogeTitle(R.string.cancel_round)
//                .setDialogeTitleTextColor(R.color.red_tabs)
                .setImageWithColor(R.drawable.stop, 0)
                .setYesButtonText(R.string.yes_value)
                .setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                    @Override
                    public void OnActionDone(UtilDialogs.MessageYesNoDialog dialog) {
                        /**/

                        showBackButton();
                        getMainActivity().setOnBackButtonPressed(null);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getMainActivity().onBackPressed();
                            }
                        }, 200);
                        if (dialog != null)
                            dialog.dismiss();

                        /**/
                    }
                }).setImageWithColor(R.drawable.end_arrow, R.color.red_tabs);


    }

    protected abstract void showConfirmStartRoundDialog();

    protected UtilDialogs.ReasonDialog confirmResumeRoundDialogOnPageStart;

    protected void showConfirmResumeRoundDialogOnPageStart() {
        final ArrayList<String> reasonsList = new ArrayList<>();
        reasonsList.add(getString(R.string.resume_round_confirmation_yes));
        reasonsList.add(getString(R.string.resume_round_confirmation_no));
        /**/
        if (confirmResumeRoundDialogOnPageStart != null)
            confirmResumeRoundDialogOnPageStart.dismiss();
        confirmResumeRoundDialogOnPageStart = new UtilDialogs.ReasonDialog().show(getActivity()).
                setDialogeTitle(R.string.resume_round_confirmation)
                .setYesButtonText(R.string.ok)
                .setImageWithColor(R.drawable.stop, 0)
                .setAfterResonSelectedLisiner(new OnActionDoneListener<String>() {
                    @Override
                    public void OnActionDone(String reason) {
                        if (reason.equals(reasonsList.get(0))) {
                            showConfirmStartRoundDialog();
                        } else if (reason.equals(reasonsList.get(1))) {
                            showCancelRoundDialog().hideYesButton();
                            forceEndRound(reason);
                        }

                    }
                }, null).createRadioButton(reasonsList);
        ;


//        new UtilDialogs.MessageYesNoDialog().show(getActivity()).
//                setDialogeTitle(R.string.resume_round_confirmation)
//                .setDialogeTitleTextColor(R.color.color_green)
//                .setYesButtonText(R.string.resume_round_confirmation_yes_)
//                .showOutherButton()
//                .setOutherButtonText(R.string.resume_round_confirmation_no_)
////                .setDialogeMessage(getString(R.string.start_the_round))
//                .setImageWithColor(R.drawable.stop, 0)
//                .setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
//                    @Override
//                    public void OnActionDone(UtilDialogs.MessageYesNoDialog dialog) {
//
//                        dialog.dismiss();
//                        showConfirmStartRoundDialog();
//
//                    }
//                }).setOutherButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
//            @Override
//            public void OnActionDone(UtilDialogs.MessageYesNoDialog action) {
//                action.dismiss();
//                showCancelRoundDialog().hideYesButton();
//
//            }
//        });

    }


    private void cancelRound(String reason) {
        showProcessingDialog(0);
        ApiController.cancelRound(getActivity(), roundBean.getId(), reason, new OnApiComplete<Object>() {
            @Override
            public void onSuccess(Object repons) {
                hideProcessingDialog();
                Log.v("cancelRound ", "" + repons.toString());
                if (repons.toString().toLowerCase().contains("ok")) {
                    afterEndRound(true);
//                    Collections.sort(roundAdapter.getValues());
//                    yousef
                    UtilityDriver.setStringShared(UtilityDriver.ROUND_ST, "false");
                    roundAdapter.notifyDataSetChanged();

                } else {
                    onError(0, "Error : " + repons.toString());

                }
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                hideProcessingDialog();
                showAPIErrorDialog(errorMessage.toString());
            }
        });

    }

    protected void forceEndRound(String reason) {
        showProcessingDialog(0);
        ApiController.forceEndRound(getActivity(), roundBean.getId(), reason, new OnApiComplete<Object>() {
            @Override
            public void onSuccess(Object repons) {
                hideProcessingDialog();
                Log.v("cancelRound ", "" + repons.toString());
                if (repons.toString().toLowerCase().contains("ok")) {
                    afterEndRound(true);
//                    Collections.sort(roundAdapter.getValues());
                    roundAdapter.notifyDataSetChanged();

                } else {
                    onError(0, "Error : " + repons.toString());

                }
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                hideProcessingDialog();
                showAPIErrorDialog(errorMessage.toString());
            }
        });

    }

    protected UtilDialogs.MessageYesNoDialog pleaseCheckOutDialog;

    protected void showPleaseCheckOutDialog() {
        if (pleaseCheckOutDialog != null)
            pleaseCheckOutDialog.dismiss();
        pleaseCheckOutDialog = new UtilDialogs.MessageYesNoDialog().show(getActivity()).
                setDialogeTitle(getString(R.string.drop_off_round_end_round_students_not_check_out))
                .hideCloseButton()
                .setYesButtonText(R.string.ok)
                .setImageWithColor(R.drawable.img_error, 0);

    }

    private void checkNearby() {
        try {
            RoundInfoFragment.roundBeanCheck = roundBean;
            RoundInfoFragment.roundBean = roundBean;
//            RoundInfoFragment.roundBean = (RoundBean) getArguments().getParcelable(UtilityDriver.ROUND);
//            RoundInfoFragment.roundBeanCheck = roundBean;
            Location location = new Location("123");
            location.setLongitude(StaticValue.longitudeMain);
            location.setLatitude(StaticValue.latitudeMain);
            LocationListener.checkNearbyDistance(location);
        } catch (Exception e) {
            e.printStackTrace();

        }


    }


    @Override
    public void onDestroy() {
        RoundInfoFragment.roundBeanCheck = null;
        RoundInfoFragment.roundBean = null;
        getMainActivity().setOnBlueToothStatusChanged(null);
        super.onDestroy();
//        Application.shouldRefresh = true;

    }


    protected void doNoShow_OnList(StudentBean studentBean, int position, String reason) {
        studentBean.setIsNoShow(true);
//        moveToLast(studentBean, position);
        roundAdapter.notifyDataSetChanged();
        enableAlertOnBackClick();
    }

    protected void doNoShow_OnAPI(StudentBean studentBean, String reason) {
        studentBean.setIsNoShow(true);
        String time_between_stop_and_now = getMainActivity().timeBetweenStopAndNow();
        ApiController.setStudentBusCheck(getActivity(), studentBean.getId(), statusNo_Show, roundBean.getId(), studentBean.isNoShow(), reason, time_between_stop_and_now);
//        if (PathUrl.send_NoShow_Notification) {
//            sendNotificationsForMotherAndFather(studentBean, roundBean, statusNo_Show, roundBean.getTypeRoundEnum());
//        }
        enableCancelRoundOnBackClick();
    }

    protected void doCheckIn_OnList(StudentBean studentBean, int position, boolean moveToLast) {
        studentBean.setAbsent(false);
        studentBean.setCheckEnum(CheckEnum.CHECK_IN);
        if (moveToLast)
            moveToLast(studentBean, position);
        else
            refreshList();

        enableAlertOnBackClick();
    }


    protected void doCheckIn_OnAPI(StudentBean studentBean) {
        String time_between_stop_and_now = getMainActivity().timeBetweenStopAndNow();
        studentBean.setCheckEnum(CheckEnum.CHECK_IN);
        ApiController.setStudentBusCheck(getActivity(), studentBean.getId(), statusCheckIn, roundBean.getId(), studentBean.isNoShow(), null, time_between_stop_and_now);
//        if (PathUrl.send_CheckIn_Notification) {
//            sendNotificationsForMotherAndFather(studentBean, roundBean, statusCheckIn, roundBean.getTypeRoundEnum());
//        }
        enableCancelRoundOnBackClick();

//        if (PathUrl.CHECKIN_TIMER_ON && PathUrl.CHECKIN_TIMER_TEST_ON)
//            new UtilDialogs.MessageYesNoDialog().show(getMainActivity())
//                    .setYesButtonText(R.string.ok)
//                    .hideCloseButton()
//                    .hideImage()
//                    .setDialogeTitle("(Testing Message) \n Time Until Check In is : \n " + time_between_stop_and_now);
    }


    protected void doCheckOut(StudentBean studentBean, int position) {
        studentBean.setCheckEnum(CheckEnum.CHECK_OUT);
        String time_between_stop_and_now = getMainActivity().timeBetweenStopAndNow();
        ApiController.setStudentBusCheck(getActivity(), (int) studentBean.getId(), statusCheckOut, (int) roundBean.getId(), studentBean.isNoShow(), null, time_between_stop_and_now);
//        if (PathUrl.send_CheckOut_Notification) {
//            sendNotificationsForMotherAndFather(studentBean, roundBean, statusCheckOut, roundBean.getTypeRoundEnum());
//        }
        moveToLast(studentBean, position);
        enableCancelRoundOnBackClick();
    }

    protected void removeCheck_OnList(StudentBean studentBean, int position) {
        studentBean.setCheckEnum(CheckEnum.EMPTY);
        moveToFirst(studentBean, position);
//        enableAlertOnBackClick();
    }

    protected void doAbsent(StudentBean studentBean, int position, String reason) {
        studentBean.setAbsent(true);
        moveToLast(studentBean, position);
        String time_between_stop_and_now = getMainActivity().timeBetweenStopAndNow();
        ApiController.setStudentBusCheck(getActivity(), studentBean.getId(), statusAbsent, roundBean.getId(), studentBean.isNoShow(), reason, time_between_stop_and_now);
//        if (PathUrl.send_Absent_Notification) {
//            sendNotificationsForMotherAndFather(studentBean, roundBean, statusAbsent, roundBean.getTypeRoundEnum());
//        }
        enableCancelRoundOnBackClick();

    }


    protected int getPositionOfThisStudentInAdapter(int studentId) {
        for (int i = 0; i < roundAdapter.getValues().size(); i++) {
            if (roundAdapter.getValues().get(i).getId() == studentId) {
                return i;
            }
        }
        return -1;
    }


    protected void showCheckToast(final String url, String studentName, String text) {

        currentRepeat++;
        if (!mp.isPlaying()) {
            mp.start();
//            ttobj.speak("Student Check In", TextToSpeech.QUEUE_FLUSH, null);
        }

        final View layout = getLayoutInflater().inflate(R.layout.dialog_toast_message, null);
        Drawable tintDrawable = UtilityDriver.getTintDrawable(getActivity(), R.drawable.img_user, R.color.color_white);
        if (url != null && url != "") {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Glide.with(getActivity()).load(url).fitCenter().into((CircleImageView) layout.findViewById(R.id.dialoge_image));
                    DrawableToolsV2.loadDrawable((CircleImageView) layout.findViewById(R.id.dialoge_image), url);

                }
            });
        } else {
            ((CircleImageView) layout.findViewById(R.id.dialoge_image)).setImageDrawable(tintDrawable);
        }
//        Glide.with(getActivity()).load(url).fitCenter().into((CircleImageView) layout.findViewById(R.id.dialoge_image)).onLoadCleared();
        ((TextView) layout.findViewById(R.id.dialoge_title)).setText(text);
        ((TextView) layout.findViewById(R.id.student_name)).setText(studentName);

        Toast toast = new Toast(getActivity());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    //    TextToSpeech  ttobj;
    private void initPlayer() {
        try {
//            if (PathUrl.BEACON_Enabled) {
            AudioManager audioManager = (AudioManager) getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(audioManager.STREAM_MUSIC), 0);
//            }


            mp = MediaPlayer.create(getActivity().getApplicationContext(), Uri.parse("android.resource://" + getActivity().getApplicationContext().getPackageName() + "/" + R.raw.correct));
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    currentRepeat--;
//                mp.stop();
                    if (currentRepeat > 0) {
                        mp.start();
                    }
                }
            });
            mp.prepare();

            /**/
//              ttobj=new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
//                @Override
//                public void onInit(int status) {
//                    ttobj.setLanguage(Locale.ENGLISH);
//                }
//            });

        } catch (Exception e) {

        }

    }

//    protected void showCheckToast(String url, String text) {
//        if (url != null && text != null) {
//            toastMessagesList.add(new String[]{url, text});
//        }
//
//        if (toastMessagesList.size() == 1) {
//            showdialoge();
//        }
//
//
//    }

//    protected void showdialoge() {
//        String[] url_and_image = toastMessagesList.remove(0);
//        new UtilDialogs
//                .ToastMessageDialog()
//                .loadImageFromURL(url_and_image[0])
//                .setDialogeTitle(url_and_image[1])
//                .show(getMainActivity()).setOnDismiss(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                showdialoge();
//            }
//        });
//    }

//    protected  void addToastMessageToShow(final UtilDialogs.ToastMessageDialog dialog) {
//
//        toastMessagesList.add(dialog);
//        showToastDialog();
//
//
//    }

//    protected  void showToastDialog() {
//
////        toastMessagesHandler.postDelayed(new Runnable() {
////            @Override
////            public void run() {
//                try {
//                    if (toastMessagesList.size() > 0) {
//                        final UtilDialogs.ToastMessageDialog toastMessageDialog = toastMessagesList.remove(0);
//                        toastMessageDialog.setOnDismiss(new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogInterface dialogInterface) {
////                                new Handler().postDelayed(new Runnable() {
////                                    @Override
////                                    public void run() {
//                                        showToastDialog();
////                                    }
////                                },3000);
//
//                            }
//                        });
//                        toastMessageDialog.show(getMainActivity());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
////            }
////        },3000);
//
//
//    }


    public StudentBean getMustTakenStudent() {
        return roundAdapter.getValues().get(0);
    }

    //yousef
    public List<StudentBean> getMustTakenStudentList() {
        return roundAdapter.getValues();
    }

    protected void checkChangeRoot(StudentBean currentCheckedStudent) {
        if (isCorrectCheckOrder(currentCheckedStudent.getId())) {
            Integer picked_student = currentCheckedStudent.getId();
            Integer original_student = getMustTakenStudent().getId();
            if (getMustTakenStudent().getCheckEnum().equals(CheckEnum.CHECK_OUT) || getMustTakenStudent().isNoShow() || getMustTakenStudent().isAbsent())
                return;

            new ConfigNotify(
                    StaticValue.latitudeMain + "",
                    StaticValue.longitudeMain + "",
                    roundBean.getId() + "",
                    EnumConfigNotify.ROUTE_CHANGED,
                    mActivity,
                    original_student,
                    picked_student);
        }

    }

    protected boolean isCorrectCheckOrder(int currentCheckedStudentId) {
        try {
            return getMustTakenStudent().getId() != currentCheckedStudentId;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    protected UtilDialogs.MessageYesNoDialog arriveAlarmConfirmDialog;

    protected void showArriveAlarmConfirmDialog(final StudentBean item, String round_type) {
        if (arriveAlarmConfirmDialog != null)
            arriveAlarmConfirmDialog.dismiss();
        arriveAlarmConfirmDialog = new UtilDialogs.MessageYesNoDialog().show(getActivity())
                .setDialogeMessage(getString(R.string.arrive_alarm_text))
                .setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                    @Override
                    public void OnActionDone(UtilDialogs.MessageYesNoDialog dialog) {
                        if (dialog != null)
                            dialog.dismiss();
                        showProcessingDialog(0);
                        ApiController.sendArriveAlarm(getMainActivity(), item.getId(), round_type, new OnApiComplete<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                hideProcessingDialog();
                            }

                            @Override
                            public void onError(int errorCode, String errorMessage) {
                                hideProcessingDialog();
                                showAPIErrorDialog(errorMessage.toString());
                            }
                        });
                    }
                }).setImageWithColor(R.drawable.bell, R.color.color_student_name);
    }


    protected void checkAbsentToday() {
        try {
            for (int i = 0; i < roundBean.getListStudentBean().size(); i++) {
                if (roundBean.getListStudentBean().get(i).isAbsent()) {
                    StringBuilder title = new StringBuilder();
                    title.append(roundBean.getListStudentBean().get(i).getNameStudent());
                    title.append(" ");
                    title.append(getString(R.string.is_absent_today));
                    if (getMainActivity() != null) {
                        new UtilDialogs.MessageYesNoDialog().show(getMainActivity())
                                .setYesButtonText(R.string.ok)
                                .hideCloseButton()
                                .setImageWithColor(R.drawable.absent_msg_icon, 0)
                                .setDialogeTitle(title.toString());
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RoundInfoFragment.ROUND_ID_SOCKET = 0;
        RoundInfoFragment.roundBeanCheck = null;
        clearNearByListeners();
    }


    protected void sendStudentsOrderToAPI(ArrayList<StudentBean> studentBeans) {
        try {
            ReorderedRequest reorderedRequest = new ReorderedRequest();
            /**/
            reorderedRequest.roundId = roundBean.getId();
            for (StudentBean studentBean : studentBeans) {
                reorderedRequest.orderedStudentsIds.add(studentBean.getId());
            }
            /**/
            ApiController.sendRoundOrder(getMainActivity(), reorderedRequest, new OnApiComplete<Object>() {
                @Override
                public void onSuccess(Object o) {
//                    Toast.makeText(getMainActivity(), "++ Success 'Send Order'  ++", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(int errorCode, String errorMessage) {
//                    Toast.makeText(getMainActivity(), "-- Error 'Send Order'  --", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
//            Toast.makeText(getMainActivity(), "-- Error 'Send Order'  --", Toast.LENGTH_SHORT).show();

        }

    }


    protected void checkMovedStudentsList(ArrayList<String> movedStudentsList) {
        try {

            if (movedStudentsList == null)
                return;

            if (movedStudentsList.size() <= 0)
                return;
            if (movedStudentsList.get(0).toString().trim().equals(""))
                return;

//            StringBuilder messagesBuilder=new StringBuilder();
//            for (String message:roundBean.getMovedStudentsList()) {
//                messagesBuilder.append(message);
//                messagesBuilder.append("\n");
//            }


            UtilDialogs.MessageYesNoDialog movedStudentsDialog = new UtilDialogs.MessageYesNoDialog().show(getMainActivity())
                    .setYesButtonText(R.string.ok)
                    .hideCloseButton()
                    .setImageWithColor(R.drawable.stop, 0)
                    .setDialogeTitle(UtilTools.fromHTML(movedStudentsList.get(0)))
//                    .setDialogeTitle(UtilTools.fromHTML("The student <br> <img src='https://png.icons8.com/metro/1600/image-file.png'/> <br><b><font color='#CE3337'>أدم داود</font></b><br>has been added to this round. Before you start the round, please check the availability of the student in the bus and the pickup location of the student"))

                    .setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                        @Override
                        public void OnActionDone(UtilDialogs.MessageYesNoDialog messageYesNoDialog) {
                            movedStudentsList.remove(0);
                            if (messageYesNoDialog != null)
                                messageYesNoDialog.dismiss();
                            checkMovedStudentsList(movedStudentsList);

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
