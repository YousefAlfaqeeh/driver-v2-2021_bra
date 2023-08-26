package schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.adapters.DragDropHelper;
import schooldriver.trackware.com.school_bus_driver_android.adapters.RecyclerViewAdapter;
import schooldriver.trackware.com.school_bus_driver_android.adapters.RecyclerViewAdapterWithDragDrop;
import schooldriver.trackware.com.school_bus_driver_android.bean.AbsentKidsBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.RoundBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.enums.CheckEnum;
import schooldriver.trackware.com.school_bus_driver_android.gcmNotification.firebace.MyFirebaseMessagingService_new;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.OnActionDoneListener;
import schooldriver.trackware.com.school_bus_driver_android.locationListener.LocationListenerPres;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilDialogs;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;


public class RoundInfoFragmentPickUp extends RoundInfoFragment_NEW {

    private ItemTouchHelper itemTouchHelper;
    private RecyclerViewAdapter.AdapterFilter<StudentBean> filter = new RecyclerViewAdapter.AdapterFilter<StudentBean>() {
        @Override
        public boolean filter(StudentBean type) {
            if (studentHaseBeaconsButStillInBus.contains(type.getId()) && roundBean.isRoundEndedForEver() && type.isCheckedIn() && type.isCheckedByBeacon() && !type.isAbsent()) {
                return true;
            } else {
                return false;
            }
            /*&& studentHaseBeaconsButStillInBus.contains(type.getId()) */


        }
    };


    @Override
    protected void sendNearByNotification(StudentBean studentBean, String roundID) {
        try {
            if (sentNearby.contains(studentBean.getId()))
                return;
            if (studentBean.isCheckedIn())
                return;
            if (studentBean.isCheckedOut())
                return;
            if (studentBean.isAbsent())
                return;
            if (studentBean.isNoShow())
                return;
            /**/
            sentNearby.add(studentBean.getId());
            new LocationListenerPres(getActivity()).sendNearMessage(studentBean, roundID);
        } catch (Exception e) {
            e.printStackTrace();

        }

        //sendNearGSM
    }

//    protected void getFirstStudentForNearBy() {
//        if (isCorrectCheckOrder(currentCheckedStudent.getId())) {
//            Integer original_student = currentCheckedStudent.getId();
//            Integer picked_student = getMustTakenStudent().getId();
//            new ConfigNotify(
//                    StaticValue.latitudeMain + "",
//                    StaticValue.longitudeMain + "",
//                    roundBean.getId() + "",
//                    EnumConfigNotify.ROUTE_CHANGED,
//                    mActivity,
//                    original_student,
//                    picked_student);
//        }
//
//    }

    /**/

    /**/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_round_info_new, container, false);
        view.findViewById(R.id.lliTittle).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green_tabs));
        roundBean = (RoundBean) getArguments().getParcelable(UtilityDriver.ROUND);
        RoundInfoFragment.roundBean = (RoundBean) getArguments().getParcelable(UtilityDriver.ROUND);
        RoundInfoFragment.ROUND_ID_SOCKET = (int) roundBean.getId();
//        roundBean.getListStudentBean().get(1).setMacAdress("AC:23:3F:22:B7:EB");
        studentHaseBeaconsButStillInBus.clear();
        MainActivity.CURRENT_SELECTED_ROUND = roundBean;

        /**/
        getMainActivity().checkGPS();
        findViews(view);
        setTopBarOnClickListeners();
        setOnClickListeners();
        initAdapter();
        /**/
        labNameRound.setText(roundBean.getNameRound());
        /**/
        if (roundBean.isRoundEndedForEver()) {
            handleWasEndedRound();
            oldActionsOnEnd();
            hideEditMode();
        } else if (roundBean.isRoundPaused()) {
            showConfirmResumeRoundDialogOnPageStart();
            oldActionsOnEnd();
            hideEditMode();

        } else if (roundBean.isRoundStartedNow()) {
            handlePausedRound();
            RoundInfoFragment.roundBeanCheck = (RoundBean) getArguments().getParcelable(UtilityDriver.ROUND);
            oldActionsOnStart();
            setBeaconLisinersAndAddMacs();
            initFirstTimeEditMode();
//            getMainActivity().startAndSendDataToService(roundBean);.
//            getMainActivity().clearBeaconScanner();
//            getMainActivity().addMacAddressToScanner(roundBean.getListStudentBean());
        } else {
            showConfirmStartRoundDialog();
            initFirstTimeEditMode();
//            oldActionsOnEnd();
        }

        roundAdapter.addAll(roundBean.getListStudentBean());


        getMainActivity().setOnNFCActionDone(nfc_data -> {
            try {
                /**/
                if (!roundBean.isRoundStartedNow()) {
                    return;
                }
                /**/
                final int sPosition = getPositionOfThisStudentInAdapter_NFC(nfc_data);
                if (sPosition == -1) {
                    return;
                }
                /**/
                StudentBean nfc_student = roundAdapter.getValues().get(sPosition);
                if (nfc_student.isAbsent() || nfc_student.isNoShow()) {
                    return;
                }
                if (nfc_student.isCheckedIn()) {
                    return;
                }

                try {
                    showCheckToast(nfc_student.getAvatar(), nfc_student.getNameStudent(), getString(R.string.nfc_check_in));

                } catch (Exception ignore) {

                }


                checkChangeRoot(nfc_student);//order
                doCheckIn_OnList(nfc_student, sPosition, true);
                doCheckIn_OnAPI(nfc_student);
                refreshList();


            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        getMainActivity().registerAbsentReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    try {
                        checkMessagesFromNotification();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (intent.hasExtra(MyFirebaseMessagingService_new.ABSENT_RECEIVER)) {
                        /**/
                        AbsentKidsBean absentKidsBean = intent.getParcelableExtra(MyFirebaseMessagingService_new.ABSENT_RECEIVER);
                        /**/
                        if (Integer.parseInt(absentKidsBean.getRoundId().trim()) == roundBean.getId()) {
                            /**/
                            int positionOfThisStudentInAdapter = getPositionOfThisStudentInAdapter(absentKidsBean.getId());
                            roundAdapter.getValues().get(positionOfThisStudentInAdapter).setAbsent(true);
                            moveToLast(roundAdapter.getValues().get(positionOfThisStudentInAdapter), positionOfThisStudentInAdapter);

                            if (getMainActivity() != null) {
                                new UtilDialogs.MessageYesNoDialog().show(getMainActivity())
                                        .setYesButtonText(R.string.ok)
                                        .hideCloseButton()
                                        .setImageWithColor(R.drawable.absent_msg_icon, 0)
                                        .setDialogeMessage(absentKidsBean.getName())
                                        .setDialogeTitle(R.string.absence);
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        checkAbsentToday();
        initForNearBy();
        checkMovedStudentsList(roundBean.getMovedStudentsList());
        return view;
    }

    @Override
    protected void whenEndRoundLocationInRange() {
        try {
            if (canEndRound() && end_round_view.getVisibility() == View.VISIBLE && end_round_view.isEnabled()) {
                if (UtilityDriver.getBooleanShared(UtilityDriver.AUTO_ROUND_ENDING)){
                    end_round_view.performClick();
                }

            }
        } catch (Exception ignore) {
        }

    }

    private void setBeaconLisinersAndAddMacs() {
        try {

            getMainActivity().onScannerFind_in = new OnActionDoneListener<StudentBean>() {
                @Override
                public void OnActionDone(StudentBean student) {
                    try {
                        /**/
                        int studentId = student.getId();
                        /**/
                        if (!roundBean.isRoundStartedNow()) {
                            return;
                        }
                        /**/
                        final int sPosition = getPositionOfThisStudentInAdapter(studentId);
                        if (sPosition == -1) {
                            return;
                        }
                        /**/
                        boolean wasNotInTheList = studentHaseBeaconsButStillInBus.add(studentId);
                        if (wasNotInTheList) {
                            student = roundAdapter.getValues().get(sPosition);
                            if (student.isAbsent()) {
                                return;
                            }
                            student.setCheckedByBeacon(true);
                            if (!student.isCheckedIn()) {
                                /**/
                                checkChangeRoot(student);//order
                                doCheckIn_OnList(student, sPosition, true);
                                doCheckIn_OnAPI(student);
                                /**/
                                showCheckToast(student.getAvatar(), student.getNameStudent(), getString(R.string.check_in));

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            };

            getMainActivity().onScannerFind_out = new OnActionDoneListener<StudentBean>() {
                @Override
                public void OnActionDone(StudentBean student) {
                    try {
                        int studentId = student.getId();
                        if (!roundBean.isRoundStartedNow()) {
                            return;
                        }


                        studentHaseBeaconsButStillInBus.remove(studentId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


//                }

                }
            };


        } catch (Exception e) {

        }

    }


    public void initAdapter() {
        rsStudent.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).drawable(R.drawable.list_divider).margin(30, 30).build());
        rsStudent.setLayoutManager(new LinearLayoutManager(getActivity()));

        /**/
//        final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

        roundAdapter = new RecyclerViewAdapterWithDragDrop<StudentBean, RoundInfoHolderPickUp>() {
            @Override
            public RoundInfoHolderPickUp cViewHolder(ViewGroup viewGroup, int i, LayoutInflater layoutInflater) {
                return new RoundInfoHolderPickUp(layoutInflater.inflate(R.layout.list_student_pickup, viewGroup, false));
            }

            @Override
            public void bViewHolder(final RoundInfoHolderPickUp viewHolder, final int position, final StudentBean item) {
                /**/
//                viewBinderHelper.bind(viewHolder.swipeRevealLayout, item.getId()+"");

                viewHolder.initNoChange();
                viewHolder.setEnabled(roundBean.isRoundStartedNow());
                viewHolder.setRoundEnded(roundBean.isRoundEndedForEver());
                viewHolder.setStudentImage(item.getAvatar());

                viewHolder.editMode(isInEditMode());
                /**/
                viewHolder.labStudentName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTip(v, item.getNameStudent());
                    }
                });
                /**/

                /**/
//                viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
//                            getDragDropListener().onStartDrag(viewHolder);
//                        }
//                        return false;
//                    }
//                });

                viewHolder.call_and_changelocation_view_container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                            if (itemTouchHelper != null)
                                itemTouchHelper.startDrag(viewHolder);
                        }
                        return false;
                    }
                });


                if (roundBean.isRoundEndedForEver() && item.isCheckedIn() && item.isCheckedByBeacon() && studentHaseBeaconsButStillInBus.contains(item.getId())) { // ending Mood
                    viewHolder.studentStillInBus();
//                        viewHolder.mustManualCheckOut();
                } else {
                    if (item.isAbsent()) {
                        viewHolder.absenceDone();
                    }
                    if (roundBean.isRoundEndedForEver())
                        viewHolder.undo_absent.setVisibility(View.GONE);

//                    else if (item.isAbsent()) {
//                        viewHolder.absenceByParentDone();
//                    }
                    else if (item.isCheckedIn()) {
                        viewHolder.checkedInDone();
                    }

                }

                /**/
                viewHolder.labStudentName.setText(item.getNameStudent());
                viewHolder.labGrade.setText(new StringBuilder().append(getString(R.string.grade)).append(" : ").append(item.getGrade()));
                /**/
                viewHolder.swap_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UtilDialogs.StudentToolDialog studentToolDialog = viewHolder.showToolsDialog(getActivity(), 1);
                        studentToolDialog.changeLocationEnabled(roundBean.isChangeStudentLocation());
                        studentToolDialog.callEnabled(item);
//                        studentToolDialog.changeColor(R.color.green_tabs);
                        studentToolDialog.send_arrive_alarm.setEnabled(roundBean.isRoundStartedNow() && !item.isAbsent() && !item.isNoShow() && !item.isCheckedIn() && !item.getCheckEnum().equals(CheckEnum.CHECK_OUT));
                        studentToolDialog.imgCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new UtilDialogs.CallFatherMotherDialog().show(getMainActivity(), item);

                            }
                        });
                        studentToolDialog.imgChangeLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showChangeLocationDialog(item);
                            }
                        });

                        studentToolDialog.send_arrive_alarm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                showArriveAlarmConfirmDialog(item, "pickup");

                            }
                        });

                    }
                });

//                viewHolder.imgChangeLocation.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showChangeLocationDialog(item);
//                    }
//                });
                viewHolder.check_in_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showConfirmCheckInDialog(item, position, viewHolder);
                    }
                });
                viewHolder.undo_absent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (viewHolder.isAbsentProgressWorking()) {
//                            viewHolder.cancelAbsentProgress();
//                            viewHolder.initNoChange();
//                        } else {
                        showConfirmCheckInDialog(item, position, viewHolder);
//                        }


                    }
                });
                viewHolder.undo_check_in.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.cancelTimer();
                    }
                });

                viewHolder.absent_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showConfirmAbsentDialog(item, position);
                        /////////////////////
                        ////////Testing////////
                        ////////////////////////

//                        if (progressingHolder!=null)
//                            progressingHolder.forceFinishProgressBar();

//                        new UtilDialogs.ReasonDialog().show(getActivity()).
//                                setDialogeTitle(R.string.conf_student_absent)
//                                .setYesButtonText(R.string.yes_value)
//                                .setImageWithColor(R.drawable.absent_msg_icon, 0)
//                                .initReasonsViews(UtilDialogs.ReasonDialog.REASON_ABSENT)
//                                .setAfterResonSelectedLisiner(new OnActionDoneListener<String>() {
//                                    @Override
//                                    public void OnActionDone(final String reason) {
////                                        viewHolder.absenceDone();
////                                         progressingHolder = viewHolder;
////                                        viewHolder.startAbsentProgressBar(new OnActionDoneListener() {
////                                            @Override
////                                            public void OnActionDone(Object Action) {
////                                                item.setAbsent(true);
////                                                moveToLastWithAnimation(item, position);
//                                                doAbsent(item, position, reason);
////                                                progressingHolder=null;
//
////                                            }
////                                        });
//
//
//                                    }
//                                }, null);


                        ///////////////////////
                        ///////////////////////
                        ////////////////////////


//                        showConfirmAbsentDialog(item, position);
                    }
                });


            }
        };
        /**/
//        roundAdapter.setOnListChangedListener(new DragDropHelper.OnListChangedListener() {
//            @Override
//            public void onListChanged(List items) {
////                sendStudentsOrderToAPI((ArrayList<StudentBean>) roundAdapter.getValues());
//            }
//        });
        /**/
        itemTouchHelper = new DragDropHelper(rsStudent).attach();
        /**/
        roundAdapter.filter(filter);
        /**/
        rsStudent.setAdapter(roundAdapter);
    }


//    private void setOnClickListeners() {
//
//        start_round_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (roundBean.isRound_canceled())
//                    showConfirmResumeRoundDialogOnPageStart();
//                else
//                    showConfirmStartRoundDialog();
//            }
//        });
//        end_round_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (canEndRound()) {
//                    if (studentHaseBeaconsButStillInBus.size() == 0) {
//                        showConfirmEndRoundDialog();
//                    } else {
//                        endingRound = true;
//                        roundAdapter.filter(filter);
//                    }
//                } else {
//                    showCancelRoundDialog();
////                    new UtilDialogs.MessageYesNoDialog().show(getActivity()).
////                            setDialogeTitle(R.string.alarm)
////                            .setDialogeTitleTextColor(R.color.toolbar_bg_color)
////                            .hideCloseButton()
////                            .setYesButtonText(R.string.ok)
////                            .setDialogeMessage(getString(R.string.pick_up_round_end_round_students_not_check_in));
//
//                }
//
//
//            }
//        });
//
//
//    }


    private boolean canEndRound() {
        for (int i = 0; i < roundAdapter.getValues().size(); i++) { //is all student checked-Out or Absent ?
            boolean isAbsent = roundAdapter.getValues().get(i).isAbsent();
            boolean isCheckedIn = roundAdapter.getValues().get(i).isCheckedIn();
            if (!(isAbsent || isCheckedIn)) {
                return false;
            }
        }

        return true;
    }

    UtilDialogs.MessageYesNoDialog confirmCheckInDialog;

    private UtilDialogs.MessageYesNoDialog showConfirmCheckInDialog(final StudentBean item, final int position, RoundInfoHolderPickUp roundInfoHolderPickUp) {
        if (confirmCheckInDialog != null)
            confirmCheckInDialog.dismiss();
        confirmCheckInDialog = new UtilDialogs.MessageYesNoDialog().show(getActivity()).
//                setDialogeTitle(R.string.conf_student_check_in)
//        setDialogeTitleTextColor(R.color.color_green)
        setDialogeTitle(getString(R.string.sure_check_in).replace("@@@@@@", item.getFirstNameStudent()))
                .setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                    @Override
                    public void OnActionDone(UtilDialogs.MessageYesNoDialog dialog) {
                        ///////////////
                        if (dialog != null) {
                            dialog.dismiss();
                        }

                        roundInfoHolderPickUp.addTimer(new OnActionDoneListener<StudentBean>() {
                            @Override
                            public void OnActionDone(StudentBean studentBean_) {
                                checkChangeRoot(studentBean_);//order
                                doCheckIn_OnList(studentBean_, getPositionOfThisStudentInAdapter(studentBean_.getId()), true);
                                doCheckIn_OnAPI(studentBean_);
                                refreshList();

                            }
                        }, item);


                    }
                }).setImageWithColor(R.drawable.img_check_in, R.color.color_green);
        return confirmCheckInDialog;
    }

//    private UtilDialogs.MessageYesNoDialog showUnDoAbsentDialog(final StudentBean item, final int position) {
//        return new UtilDialogs.MessageYesNoDialog().show(getActivity()).
////                setDialogeTitle(R.string.conf_student_check_in)
////        setDialogeTitleTextColor(R.color.color_green)
//        setDialogeTitle(getString(R.string.sure_check_in).replace("@@@@@@", item.getFirstNameStudent()))
//                .setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
//                    @Override
//                    public void OnActionDone(UtilDialogs.MessageYesNoDialog dialog) {
//                        checkChangeRoot(item);//order
//                        doCheckIn_OnList(item, position, true);
//                        doCheckIn_OnAPI(item);
//                        dialog.dismiss();
//                    }
//                }).setImageWithColor(R.drawable.undo_absent, R.color.color_blue);
//    }

    private UtilDialogs.ReasonDialog confirmAbsentDialog;

    private void showConfirmAbsentDialog(final StudentBean item, final int position) {
        if (confirmAbsentDialog != null)
            confirmAbsentDialog.dismiss();
        confirmAbsentDialog = new UtilDialogs.ReasonDialog().show(getActivity()).
                setDialogeTitle(R.string.conf_student_absent)
//                .setDialogeTitleTextColor(R.color.red_tabs)
                .setYesButtonText(R.string.yes_value)
                .setImageWithColor(R.drawable.absent_msg_icon, 0)
                .initReasonsViews(UtilDialogs.ReasonDialog.REASON_ABSENT)
                .setAfterResonSelectedLisiner(new OnActionDoneListener<String>() {
                    @Override
                    public void OnActionDone(String reason) {
                        doAbsent(item, position, reason);
                        refreshList();
                    }
                }, null);


//        new UtilDialogs.MessageYesNoDialog().show(getActivity()).
//                setDialogeTitle(R.string.conf_student_absent)
//                .setDialogeTitleTextColor(R.color.red_tabs)
//                .setDialogeMessage(getString(R.string.sure_absent).replace("@@@@@@", item.getNameStudent()))
//                .setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
//                    @Override
//                    public void OnActionDone(UtilDialogs.MessageYesNoDialog dialog) {
//                        dialog.dismiss();
//                        doAbsent(item, position, reason);
//                        /**/
//                    }
//                }).setImageWithColor(R.drawable.img_none, R.color.red_tabs);
    }


    @Override
    protected void afterEndRound(boolean isCanceled) {
//        getMainActivity().unRegisterBeaconReceiver();
        getMainActivity().clearBeaconScanner();
        /**/
//        if (!isCanceled) // don't send notification if the round is canceled
//            sendEndRoundNotificationToAllParents();
        /**/
        end_round_view.setVisibility(View.GONE);
        start_round_view.setVisibility(View.GONE);
        roundBean.endRoundForever();
        endTimer();
        showBackButton();
        getMainActivity().setOnBackButtonPressed(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getMainActivity().onBackPressed();
            }
        }, 200);

    }


//    private void sendEndRoundNotificationToAllParents() {
//        if (PathUrl.send_EndRound_Notification) {
//            try {
//                for (int i = 0; i < roundAdapter.getValues().size(); i++) {
//                    /**/
////                    if (!roundAdapter.getValues().get(i).isAbsent()) // don't send (end round) notification if the student is Absent
////                        sendNotificationsForMotherAndFather(roundAdapter.getValues().get(i), roundBean, statusCheckOut, roundBean.getTypeRoundEnum());
//                    /**/
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    protected UtilDialogs.MessageYesNoDialog confirmStartRoundDialog;

    @Override
    protected void showConfirmStartRoundDialog() {
        if (confirmStartRoundDialog != null)
            confirmStartRoundDialog.dismiss();
        confirmStartRoundDialog = new UtilDialogs.MessageYesNoDialog().show(getActivity()).
//                setDialogeTitle(R.string.start_round)
        setDialogeMessage(getString(R.string.start_the_round))
//                .setDialogeTitleTextColor(R.color.color_green)
                .setDialogeMessage(getString(R.string.start_the_round))
                .setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                    @Override
                    public void OnActionDone(UtilDialogs.MessageYesNoDialog dialog) {

                        startRound(roundBean.getId(), new OnActionDoneListener() {
                            @Override
                            public void OnActionDone(Object Action) {
                                setBeaconLisinersAndAddMacs();
                            }
                        });
                        if (dialog != null)
                            dialog.dismiss();


                    }
                }).setImageWithColor(R.drawable.start_arrow, R.color.color_green);

    }

    private UtilDialogs.MessageYesNoDialog confirmResumeRoundDialog;

    private void showConfirmResumeRoundDialog() {
        if (confirmResumeRoundDialog != null)
            confirmResumeRoundDialog.dismiss();
        confirmResumeRoundDialog = new UtilDialogs.MessageYesNoDialog().show(getActivity()).
                setDialogeTitle(R.string.start_round)
//                .setDialogeTitleTextColor(R.color.color_green)
                .setDialogeMessage(getString(R.string.start_the_round))
                .setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                    @Override
                    public void OnActionDone(UtilDialogs.MessageYesNoDialog dialog) {

                        startRound(roundBean.getId(), new OnActionDoneListener() {
                            @Override
                            public void OnActionDone(Object Action) {
                                setBeaconLisinersAndAddMacs();
                            }
                        });
                        if (dialog != null)
                            dialog.dismiss();

                    }
                }).setImageWithColor(R.drawable.start_arrow, R.color.color_green);

    }


    @Override
    public void onDestroy() {
        getMainActivity().unRegisterAbsentReceiver();
        super.onDestroy();
    }

    private UtilDialogs.MessageYesNoDialog endRoundDialog;

    private void setOnClickListeners() {

        start_round_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roundBean.isRoundPaused()) { // paused
                    showConfirmResumeRoundDialogOnPageStart();
                } else {
                    showConfirmStartRoundDialog();
                }

            }
        });
        end_round_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canEndRound()) {
                    if (studentHaseBeaconsButStillInBus.size() == 0) {
                        showConfirmEndRoundDialog();
                    } else {
                        roundBean.endRoundForever();
                        roundAdapter.filter(filter);
                    }
                } else {
                    if (endRoundDialog != null)
                        endRoundDialog.dismiss();
                    endRoundDialog = new UtilDialogs.MessageYesNoDialog().show(getActivity()).
                            setDialogeTitle(R.string.alarm)
                            .setDialogeTitleTextColor(R.color.toolbar_bg_color)
                            .hideCloseButton()
                            .setYesButtonText(R.string.ok)
                            .setDialogeMessage(getString(R.string.pick_up_round_end_round_students_not_check_in));

                }


            }
        });


    }
}
