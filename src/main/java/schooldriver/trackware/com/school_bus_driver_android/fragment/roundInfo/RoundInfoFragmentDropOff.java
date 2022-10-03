package schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.adapters.DragDropHelper;
import schooldriver.trackware.com.school_bus_driver_android.adapters.RecyclerViewAdapterWithDragDrop;
import schooldriver.trackware.com.school_bus_driver_android.bean.PindingStudentHolder;
import schooldriver.trackware.com.school_bus_driver_android.bean.RoundBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.enums.CheckEnum;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.OnActionDoneListener;
import schooldriver.trackware.com.school_bus_driver_android.locationListener.LocationListenerPres;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilDialogs;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilViews;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class RoundInfoFragmentDropOff extends RoundInfoFragment_NEW {

    private ArrayList<PindingStudentHolder> pinding_NoShow_Requists = new ArrayList<>();
    private ArrayList<PindingStudentHolder> pinding_Pickup_Requists = new ArrayList<>();
    private ItemTouchHelper itemTouchHelper;


//    private RecyclerViewAdapter.AdapterFilter<StudentBean> filter = new RecyclerViewAdapter.AdapterFilter<StudentBean>() {
//        @Override
//        public boolean filter(StudentBean type) {
//            if (studentHaseBeaconsButStillInBus.contains(type.getId()) && endingRound && type.getCheckEnum().equals(CheckEnum.CHECK_IN) && type.isCheckedByBeacon() && !type.isAbsent()) {
//                return true;
//            } else {
//                return false;
//            }
//
//
//        }
//    };

    /**/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_round_info_new, container, false);
        view.findViewById(R.id.lliTittle).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red_tabs));
        roundBean = (RoundBean) getArguments().getParcelable(UtilityDriver.ROUND);
        RoundInfoFragment.roundBean = (RoundBean) getArguments().getParcelable(UtilityDriver.ROUND);
        RoundInfoFragment.ROUND_ID_SOCKET = (int) roundBean.getId();
        MainActivity.CURRENT_SELECTED_ROUND = roundBean;
        /**/
        findViews(view);
        getMainActivity().checkGPS();
        setTopBarOnClickListeners();
        setOnClickListeners();
        initAdapter();
        /**/
        if (roundBean.isRoundEndedForEver()) {
            handleWasEndedRound();
            oldActionsOnEnd();
            hideEditMode();
        } else if (roundBean.isRoundPaused()) {
            handleToResumeRound();
            showConfirmResumeRoundDialogOnPageStart();
            oldActionsOnEnd();
            hideEditMode();
        } else if (roundBean.isRoundStartedNow()) {
            RoundInfoFragment.roundBeanCheck = getArguments().getParcelable(UtilityDriver.ROUND);
            handlePausedRound();
            oldActionsOnStart();
            initFirstTimeEditMode();
            /**/
        } else {
            oldActionsOnEnd();
            initFirstTimeEditMode();
        }

        labNameRound.setText(roundBean.getNameRound());
        /**/
        roundAdapter.addAll(roundBean.getListStudentBean());
//        roundAdapter.sort(new Comparator<StudentBean>() {
//            @Override
//            public int compare(StudentBean t1, StudentBean t2) {
//                return t1.compareTo(t1);
//            }
//        });
        /**/
//        getMainActivity().clearBeaconScanner();
//        getMainActivity().addMacAddressToScanner(roundBean.getListStudentBean());
        getMainActivity().addNotifiers(roundBean.getListStudentBean());
        getMainActivity().onScannerFind_in = new OnActionDoneListener<StudentBean>() {
            @Override
            public void OnActionDone(StudentBean studentFromBeacon) {
                try {

                    /**/
                    int studentId = studentFromBeacon.getId();
                    boolean wasNotInTheList = studentHaseBeaconsButStillInBus.add(studentId); //already contains
                    if (!wasNotInTheList)
                        return;
                    /**/
                    final int sPosition = getPositionOfThisStudentInAdapter(studentId);
                    if (sPosition == -1)
                        return;
                    /**/
                    StudentBean studentFromList = roundAdapter.getValues().get(sPosition);
                    studentFromList.setCheckedByBeacon(true);
                    if (roundBean.isRoundStartedNow())
                        return;
                    /**/
                    if (studentFromList.isNoShow())
                        return;
                    /**/
                    if (studentFromList.getCheckEnum() == CheckEnum.CHECK_IN)
                        return;

                    if (studentFromList.isNoShow())
                        return;
//                    if ((studentFromList.getCheckEnum() != CheckEnum.CHECK_IN && !studentFromList.isNoShow() /*&& finishedAdding*/)) {
                    doCheckIn_OnList(studentFromList, sPosition, false);
                    pinding_Pickup_Requists.add(new PindingStudentHolder().setStudentBean(studentFromList).setByBeacon(true));
                    showCheckToast(studentFromList.getAvatar(), studentFromList.getNameStudent(), getString(R.string.check_in));

//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };
        getMainActivity().onScannerFind_out = new OnActionDoneListener<StudentBean>() {
            @Override
            public void OnActionDone(StudentBean student) {
                try {
                    /**/
                    int studentId = student.getId();
                    boolean removed = studentHaseBeaconsButStillInBus.remove(studentId);
                    if (!removed)
                        return;
                    /**/
                    final int sPosition = getPositionOfThisStudentInAdapter(studentId);
                    if (sPosition == -1)
                        return;
                    /**/
                    student = roundAdapter.getValues().get(sPosition);
                    if (student.isNoShow())
                        return;
                    /**/
                    boolean isCheckedInByBeacon = removed && student.getCheckEnum() == CheckEnum.CHECK_IN && student.isCheckedByBeacon();
                    if (roundBean.isRoundStartedNow() && isCheckedInByBeacon) {
                        doCheckOut(student, sPosition);
                        checkChangeRoot(student);//order
                        showCheckToast(student.getAvatar(), student.getNameStudent(), getString(R.string.check_out));
                        return;
                    }
                    if (!roundBean.isRoundStartedNow() && isCheckedInByBeacon) {
                        removeCheck_OnList(student, sPosition);
                        for (int i = 0; i < pinding_Pickup_Requists.size(); i++) {
                            if (pinding_Pickup_Requists.get(i).getStudentBean().getId() == studentId) {
                                pinding_Pickup_Requists.remove(i);
                                break;
                            }

                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };

//        getMainActivity().registerBeaconReceiver(new BroadcastReceiver() {
//            @Override
//            public synchronized void onReceive(Context context, final Intent intent) {
////                new Handler().postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
//                try {
//
//
//                    if (intent.hasExtra(BeaconService.STUDENT_IN) && intent.getIntExtra(BeaconService.STUDENT_IN, -1) != -1 && !studentHaseBeaconsButStillInBus.contains(intent.getIntExtra(BeaconService.STUDENT_IN, -1))) {
//                    /**/
//                        int studentId = intent.getIntExtra(BeaconService.STUDENT_IN, -1);
//                        final int sPosition = getPositionOfThisStudentInAdapter(studentId);
//                        StudentBean student = roundAdapter.getValues().get(sPosition);
//                    /**/
//                        boolean isNewStudent = studentHaseBeaconsButStillInBus.add(studentId);
//                    /**/
//                        if (!roundStarted && (student.getCheckEnum() != CheckEnum.CHECK_IN && !student.isNoShow() /*&& finishedAdding*/)) {
//
////                                    finishedAdding = false;
//                            student.setCheckedByBeacon(true);
//                            doCheckIn_OnList(student, sPosition);
//                            pinding_Pickup_Requists.add(new PindingStudentHolder().setStudentBean(student).setByBeacon(true));
//                            showCheckToast(student.getAvatar(), student.getNameStudent(), getString(R.string.check_in));
////                                doCheckIn_OnAPI(roundAdapter.getValues().get(sPosition));
////                                    new Handler().postDelayed(new Runnable() {
////                                        @Override
////                                        public void run() {
////                                            finishedAdding = true;
////                                        }
////                                    },3000);
//
//
//                        }
//                    } else if (intent.hasExtra(BeaconService.STUDENT_OUT) && intent.getIntExtra(BeaconService.STUDENT_OUT, -1) != -1) {
//                    /**/
//                        int studentId = intent.getIntExtra(BeaconService.STUDENT_OUT, -1);
//                        final int sPosition = getPositionOfThisStudentInAdapter(studentId);
//                        final StudentBean student = roundAdapter.getValues().get(sPosition);
//                    /**/
//                        studentHaseBeaconsButStillInBus.remove(studentId);
//                    /**/
//                        if (roundStarted) {
//                            if (student.getCheckEnum() == CheckEnum.CHECK_IN && !student.isNoShow() && student.isCheckedByBeacon() /*&& finishedAdding*/) {
////                                        finishedAdding = false;
//                                doCheckOut(student, sPosition);
//                                checkChangeRoot(student);//order
//                                showCheckToast(student.getAvatar(), student.getNameStudent(), getString(R.string.check_out));
////                                        new Handler().postDelayed(new Runnable() {
////                                            @Override
////                                            public void run() {
////                                                finishedAdding = true;
////                                            }
////                                        },3000);
//
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
////                    }
////                }, 500);
//            }
//
//        });
        /**/


        /**/
        checkAbsentToday();
        initForNearBy();
        checkMovedStudentsList(roundBean.getMovedStudentsList());

        return view;
    }


    @Override
    protected void sendNearByNotification(StudentBean studentBean,String roundID) {
        try {
            if (sentNearby.contains(studentBean.getId()))
                return;
            if (studentBean.getCheckEnum() == CheckEnum.CHECK_OUT)
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

    }

    protected void handleToResumeRound() {
//        roundStarted = true;
//        toResumeRound = true;
        start_round_view.setVisibility(View.VISIBLE);
        end_round_view.setVisibility(View.GONE);

    }


    public void initAdapter() {
        rsStudent.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).drawable(R.drawable.list_divider).margin(30, 30).build());
        rsStudent.setLayoutManager(new LinearLayoutManager(getActivity()));
        /**/
//        final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
        roundAdapter = new RecyclerViewAdapterWithDragDrop<StudentBean, RoundInfoHolderDropOff>() {
            @Override
            public RoundInfoHolderDropOff cViewHolder(ViewGroup viewGroup, int i, LayoutInflater layoutInflater) {

                return new RoundInfoHolderDropOff(layoutInflater.inflate(R.layout.list_student_drop_off, viewGroup, false));
            }

            @Override
            public void bViewHolder(final RoundInfoHolderDropOff viewHolder, final int position, final StudentBean item) {
//                viewBinderHelper.bind(viewHolder.swipeRevealLayout, item.getId() + "");

                viewHolder.initNoChange();
                viewHolder.labStudentName.setText(item.getNameStudent());
                viewHolder.labGrade.setText(new StringBuilder().append(getString(R.string.grade)).append(" : ").append(item.getGrade()));
                viewHolder.setStudentImage(item.getAvatar());
                viewHolder.setRoundEnded(roundBean.isRoundEndedForEver());

                viewHolder.editMode(isInEditMode());
//                viewHolder.send_arrive_alarm.setEnabled(roundStarted && !item.isAbsent() && !item.isNoShow() && !item.getCheckEnum().equals(CheckEnum.CHECK_OUT));


                /**/
                viewHolder.labStudentName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTip(v, item.getNameStudent());
                    }
                });
                /**/
                viewHolder.labGrade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTip(v, item.getGrade());
                    }
                });
                /**/


//                if (item.isNoShow() && (roundStarted)) {
//                    viewHolder.noShowDone();
//                    viewHolder.undo_no_show.setVisibility(View.INVISIBLE);
//                } else if (item.isNoShow()) {
//                    viewHolder.noShowDone();
//                } else if (item.getCheckEnum().equals(CheckEnum.CHECK_OUT)) {
//                    viewHolder.dropOffDone();
//                } else if (item.getCheckEnum().equals(CheckEnum.CHECK_IN) && (!roundStarted)) {
//                    viewHolder.checkInDone();
//                } else if (item.getCheckEnum().equals(CheckEnum.CHECK_IN) && (roundStarted)) {
//                    viewHolder.showDropOffButton();
//                }
//
//                if (roundBean.isRoundsEnd()) {
//                    viewHolder.undo_no_show.setVisibility(View.INVISIBLE);
//                }
//                /**/
//                if (toResumeRound) {
//                    viewHolder.disableDropOff();
//                }


                ////////////////////
                ///////////////////////


                if (item.isNoShow()) {
                    viewHolder.noShowDone();
                    if (roundBean.isRoundStartedNow() || roundBean.isRoundEndedForEver()) {
                        viewHolder.undo_no_show.setVisibility(View.INVISIBLE);
                    } else {
                        viewHolder.undo_no_show.setVisibility(View.VISIBLE);
                    }
                } else if (item.getCheckEnum().equals(CheckEnum.CHECK_OUT)) {
                    viewHolder.dropOffDone();
                } else if (item.getCheckEnum().equals(CheckEnum.CHECK_IN) && !roundBean.isRoundStartedNow()) {
                    viewHolder.checkInDone();
                } else if (item.getCheckEnum().equals(CheckEnum.CHECK_IN) && roundBean.isRoundStartedNow()) {
                    viewHolder.showDropOffButton();
                }
                if (roundBean.isRoundPaused()) {
                    viewHolder.disableDropOff();
                }

                ////////////////////


                /**/
//                viewHolder.callEnabled(item);
//                viewHolder.imgCall.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        new UtilDialogs.CallFatherMotherDialog().show(getMainActivity(), item);
//                    }
//                });
//                viewHolder.imgChangeLocation.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showChangeLocationDialog(item);
//                    }
//                });
                viewHolder.check_in_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showConfirmCheckInDialog(item, position);
                    }
                });
                viewHolder.no_show_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showConfirmNoShowDialog(item, position);
                    }
                });

                viewHolder.undo_no_show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItemFromPindingRequists(item.getId());
                        item.setIsNoShow(false);
//                        viewHolder.initNoChange();
                        roundAdapter.notifyDataSetChanged();


//                        if (viewHolder.isNoShowProgressWorking()) {
//                            viewHolder.cancelNoShowProgress();
//                            viewHolder.initNoChange();
//                        } else {
//                            showConfirmCheckInDialog(item, position);
//                        }


                    }
                });

                viewHolder.no_show_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showConfirmNoShowDialog(item, position);
                        /////////////////////
                        ////////Testing////////
                        ////////////////////////
//                        new UtilDialogs.ReasonDialog().show(getActivity()).
//                                setDialogeTitle(getString(R.string.conf_student_no_show))
//                                .setYesButtonText(R.string.yes_value)
//                                .setImageWithColor(R.drawable.no_show_student, 0)
//                                .initReasonsViews(UtilDialogs.ReasonDialog.REASON_NO_SHOW)
//                                .setAfterResonSelectedLisiner(new OnActionDoneListener<String>() {
//                                    @Override
//                                    public void OnActionDone(String reason) {
//                                        viewHolder.noShowDone();
//                                        viewHolder.startNoShowProgressBar(new OnActionDoneListener() {
//                                            @Override
//                                            public void OnActionDone(Object Action) {
//                                                item.setIsNoShow(true);
//                                                pinding_NoShow_Requists.add(new PindingStudentHolder().setStudentBean(item).setReson(reason));
//
//                                            }
//                                        });
//
//
//                                    }
//                                }, null);


//                        new UtilDialogs.ReasonDialog().show(getActivity()).
//                                setDialogeTitle(R.string.conf_student_absent)
//                                .setYesButtonText(R.string.yes_value)
//                                .setImageWithColor(R.drawable.absent_msg_icon, 0)
//                                .initReasonsViews(UtilDialogs.ReasonDialog.REASON_ABSENT)
//                                .setAfterResonSelectedLisiner(new OnActionDoneListener<String>() {
//                                    @Override
//                                    public void OnActionDone(String reason) {
//                                        viewHolder.absenceDone();
//                                        viewHolder.startAbsentProgressBar(new OnActionDoneListener() {
//                                            @Override
//                                            public void OnActionDone(Object Action) {
//                                                item.setAbsent(true);
//                                                moveToLastWithAnimation(item, position);
//                                            }
//                                        });
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

                viewHolder.swap_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UtilDialogs.StudentToolDialog studentToolDialog = viewHolder.showToolsDialog(getActivity(), 2);
                        studentToolDialog.changeLocationEnabled(roundBean.isChangeStudentLocation());
                        studentToolDialog.callEnabled(item);
                        studentToolDialog.send_arrive_alarm.setEnabled(roundBean.isRoundStartedNow() && !item.isAbsent() && !item.isNoShow() && !item.getCheckEnum().equals(CheckEnum.CHECK_OUT));
//                        studentToolDialog.changeColor(R.color.red_tabs);
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
                                showArriveAlarmConfirmDialog(item, "dropoff");

                            }
                        });

                    }
                });


                viewHolder.drop_off_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UtilViews.handleClickDelay(view);
                        showConfirmCheckOutDialog(item, position);

                    }
                });
//                viewHolder.send_arrive_alarm.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        UtilViews.handleClickDelay(view);
//                        showArriveAlarmConfirmDialog(item, "dropoff");
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
//        setSwipeForRecyclerView();
        itemTouchHelper = new DragDropHelper(rsStudent).attach();
        rsStudent.setAdapter(roundAdapter);
        /**/

    }

    private void setOnClickListeners() {

        /**/
        start_round_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilViews.handleClickDelay(view);
                if (checkEveryOneInTheBus()) {
                    showConfirmStartRoundDialog();
                } else {
                    showPleaseCheckInDialog();
                }

            }
        });
        end_round_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilViews.handleClickDelay(view);
                if (checkBeforeEndRound()) {
                    showConfirmEndRoundDialog();
                } else {
                    showPleaseCheckOutDialog();
                }
            }
        });


    }


//    private void setSwipeForRecyclerView() {
//
//        SwipeUtil swipeHelper = new SwipeUtil(0, ItemTouchHelper.LEFT, getActivity()) {
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//
//            }
//
//            @Override
//            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//
//
//                return super.getSwipeDirs(recyclerView, viewHolder);
//            }
//        };
//
//        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(swipeHelper);
//        mItemTouchHelper.attachToRecyclerView(rsStudent);
//
//        //set swipe label
//        swipeHelper.setLeftSwipeLable("Archive");
//        //set swipe background-Color
//        swipeHelper.setLeftcolorCode(ContextCompat.getColor(getActivity(), R.color.red_tabs));
//
//    }


    private boolean checkEveryOneInTheBus() {

        for (int i = 0; i < roundAdapter.getValues().size(); i++) { //is all student checked-In or NoShow Or Absent?
            boolean isNoShow = roundAdapter.getValues().get(i).isNoShow();
            boolean isAbsent = roundAdapter.getValues().get(i).isAbsent();
            boolean isCheckedIn = roundAdapter.getValues().get(i).getCheckEnum() == CheckEnum.CHECK_IN;
            if (!(isNoShow || isCheckedIn || isAbsent)) {
                return false;
            }
        }

        return true;


//        for (int i = 0; i < roundAdapter.getValues().size(); i++) { //  make sure there is at least one student checked-in ?
////            boolean isNoShow = roundAdapter.getValues().get(i).isNoShow();
//            boolean somoneNotInTheBus = roundAdapter.getValues().get(i).getCheckEnum() != CheckEnum.CHECK_IN;
//            if (somoneNotInTheBus) {
//                return false;
//            }
//        }
//
//        return true;
    }

    private boolean checkBeforeEndRound() {

        for (int i = 0; i < roundAdapter.getValues().size(); i++) { //is all student checked-Out or NoShow?
            boolean isNoShow = roundAdapter.getValues().get(i).isNoShow();
            boolean isAbsent = roundAdapter.getValues().get(i).isAbsent();
            boolean isCheckedOut = roundAdapter.getValues().get(i).getCheckEnum() == CheckEnum.CHECK_OUT;
            if (!(isNoShow || isCheckedOut || isAbsent)) {
                return false;
            }
        }

        return true;
    }

    private UtilDialogs.MessageYesNoDialog confirmCheckInDialog;

    private void showConfirmCheckInDialog(final StudentBean item, final int position) {
        if (confirmCheckInDialog != null)
            confirmCheckInDialog.dismiss();
        confirmCheckInDialog = new UtilDialogs.MessageYesNoDialog().show(getActivity()).
                setDialogeTitle(R.string.conf_student_check_in)
//                .setDialogeTitleTextColor(R.color.color_green)
                .setDialogeMessage(getString(R.string.sure_check_in).replace("@@@@@@", item.getFirstNameStudent()))
                .setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                    @Override
                    public void OnActionDone(UtilDialogs.MessageYesNoDialog dialog) {
                        /**/

                        doCheckIn_OnList(item, position, false);
                        pinding_Pickup_Requists.add(new PindingStudentHolder().setStudentBean(item));
//                        doCheckIn_OnAPI(item);
                        if (dialog != null)
                            dialog.dismiss();
                        /**/
                    }
                }).setImageWithColor(R.drawable.img_check_in, R.color.color_green);
    }

    private UtilDialogs.ReasonDialog confirmNoShowDialog;

    private void showConfirmNoShowDialog(final StudentBean item, final int position) {
        if (confirmNoShowDialog != null)
            confirmNoShowDialog.dismiss();
        confirmNoShowDialog = new UtilDialogs.ReasonDialog().show(getActivity()).
//                setDialogeTitle(getString(R.string.sure_no_show).replace("@@@@@@", item.getNameStudent()))
        setDialogeTitle(getString(R.string.conf_student_no_show))
//                .setDialogeTitleTextColor(R.color.red_tabs)
                .setYesButtonText(R.string.yes_value)
                .setImageWithColor(R.drawable.no_show_student, 0)
                .initReasonsViews(UtilDialogs.ReasonDialog.REASON_NO_SHOW)
                .setAfterResonSelectedLisiner(new OnActionDoneListener<String>() {
                    @Override
                    public void OnActionDone(String reason) {

                        doNoShow_OnList(item, position, reason);
//                        doNoShow_OnList(item, position, reason);
                        pinding_NoShow_Requists.add(new PindingStudentHolder().setStudentBean(item).setReson(reason));
//                        doNoShow_OnAPI(item, reason);
                        refreshList();

                    }
                }, null);

        return;


    }

    private UtilDialogs.MessageYesNoDialog confirmCheckOutDialog;

    private void showConfirmCheckOutDialog(final StudentBean item, final int position) {
        if (confirmCheckOutDialog != null)
            confirmCheckOutDialog.dismiss();
        confirmCheckOutDialog = new UtilDialogs.MessageYesNoDialog().show(getActivity()).
                setDialogeTitle(R.string.conf_student_check_out)
//                .setDialogeTitleTextColor(R.color.color_green)
                .setDialogeMessage(getString(R.string.sure_check_out).replace("@@@@@@", item.getFirstNameStudent()))
                .setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                    @Override
                    public void OnActionDone(UtilDialogs.MessageYesNoDialog dialog) {
                        /**/
                        checkChangeRoot(item);//order
                        doCheckOut(item, position);
                        if (dialog != null)
                            dialog.dismiss();
                        /**/

                    }
                }).setImageWithColor(R.drawable.img_check_in, R.color.color_green);
    }

    @Override
    protected void afterEndRound(boolean isCanceled) {
//        getMainActivity().unRegisterBeaconReceiver();
        getMainActivity().clearBeaconScanner();
        /**/
        end_round_view.setVisibility(View.GONE);
        start_round_view.setVisibility(View.GONE);
//        roundStarted = false;
        roundBean.isRoundEndedForEver();
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


    private UtilDialogs.MessageYesNoDialog confirmStartRoundDialog;

    @Override
    protected void showConfirmStartRoundDialog() {
        if (confirmStartRoundDialog != null)
            confirmStartRoundDialog.dismiss();
        confirmStartRoundDialog = new UtilDialogs.MessageYesNoDialog().show(getActivity())
//                setDialogeTitle(R.string.start_round)
//                .setDialogeTitleTextColor(R.color.color_green)
                .setDialogeTitle(getString(R.string.start_the_round))
                .setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                    @Override
                    public void OnActionDone(UtilDialogs.MessageYesNoDialog dialog) {

                        doPindingRequists();
                        startRound(roundBean.getId(), null);
                        roundBean.startRoundNow();
                        if (dialog != null)
                            dialog.dismiss();

                    }
                }).setImageWithColor(R.drawable.start_arrow, R.color.color_green);

    }


    private void doPindingRequists() {
        for (int i = 0; i < pinding_NoShow_Requists.size(); i++) {
            doNoShow_OnAPI(pinding_NoShow_Requists.get(i).getStudentBean(), pinding_NoShow_Requists.get(i).getReson());
        }
        for (int i = 0; i < pinding_Pickup_Requists.size(); i++) {
            doCheckIn_OnAPI(pinding_Pickup_Requists.get(i).getStudentBean());
        }
    }


    private void removeItemFromPindingRequists(int studentId) {
        for (int i = 0; i < pinding_Pickup_Requists.size(); i++) {
            if (pinding_Pickup_Requists.get(i).getStudentBean().getId() == studentId) {
                pinding_Pickup_Requists.remove(i);
                return;
            }


        }

        for (int i = 0; i < pinding_NoShow_Requists.size(); i++) {
            if (pinding_NoShow_Requists.get(i).getStudentBean().getId() == studentId) {
                pinding_NoShow_Requists.remove(i);
                return;
            }


        }

    }


}
