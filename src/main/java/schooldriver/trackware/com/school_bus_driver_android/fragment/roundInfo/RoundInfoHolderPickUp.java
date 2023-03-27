package schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo;

import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.adapters.DraggableHolder;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.DrawableToolsV2;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilViews;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created   on 2/28/2017.
 */

public class RoundInfoHolderPickUp extends DraggableHolder {


    public CircleImageView imgStudent;
    public TextView labStudentName, labGrade;
    public View student_error_img, check_in_view, absent_view, check_in_done_view, absence_done_view, checkin_image, checkin_text, absent_image, still_in_bus_view, acbsent_text, drop_off_view, drop_off_done_view, call_and_changelocation_view_container, undo_absent, absence_by_parent_done_view;
    //    public SwipeRevealLayout swipeRevealLayout;
    public AppCompatImageView swap_image_view;
//    SwipeLayout swipeLayout;

    //    private MaterialProgressBar absent_MaterialProgressBar;
    public View real_item_view, checkin_and_absent_includeview;

    public RoundInfoHolderPickUp(View itemView) {
        super(itemView);
        imgStudent = (CircleImageView) itemView.findViewById(R.id.imgStudent);
        /**/
        labStudentName = (TextView) itemView.findViewById(R.id.labStudentName);
        labGrade = (TextView) itemView.findViewById(R.id.labGrade);
        /**/
        call_and_changelocation_view_container = itemView.findViewById(R.id.call_and_changelocation_view_container);
//        imgCall = itemView.findViewById(R.id.imgCall);
//        imgChangeLocation = itemView.findViewById(R.id.imgChangeLocation);
        /**/
        check_in_view = itemView.findViewById(R.id.check_in_view);
        absent_view = itemView.findViewById(R.id.absent_view);
        drop_off_view = itemView.findViewById(R.id.drop_off_view);
        /**/
        check_in_done_view = itemView.findViewById(R.id.check_in_done_view);
        absence_done_view = itemView.findViewById(R.id.absence_done_view);
        drop_off_done_view = itemView.findViewById(R.id.drop_off_done_view);
        still_in_bus_view = itemView.findViewById(R.id.still_in_bus_view);
        /**/
        checkin_image = itemView.findViewById(R.id.checkin_image);
        checkin_text = itemView.findViewById(R.id.checkin_text);
        absent_image = itemView.findViewById(R.id.absent_image);
        acbsent_text = itemView.findViewById(R.id.acbsent_text);
        /**/
        student_error_img = itemView.findViewById(R.id.student_error_img);
//        send_arrive_alarm = itemView.findViewById(R.id.send_arrive_alarm);
        undo_absent = itemView.findViewById(R.id.undo_absent);
        absence_by_parent_done_view = itemView.findViewById(R.id.absence_by_parent_done_view);
        checkin_and_absent_includeview = itemView.findViewById(R.id.checkin_and_absent_includeview);
        /**/
        swap_image_view = itemView.findViewById(R.id.swap_image_view);
//        swipeRevealLayout = itemView.findViewById(R.id.swipeRevealLayout);
//        absent_MaterialProgressBar = itemView.findViewById(R.id.absent_MaterialProgressBar);
        real_item_view = itemView.findViewById(R.id.real_item_view);
//        swipeLayout =  (SwipeLayout)itemView.findViewById(R.id.swipeRevealLayout);

//set show mode.
//        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
//        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, itemView.findViewById(R.id.bottom_wrapper));
        /**/
//        swap_image_view.setScaleX(1);
//        swipe.setSwipeListener(new SwipeRevealLayout.SwipeListener() {
//            @Override
//            public void onClosed(SwipeRevealLayout view) {
//                swap_image_view.setScaleX(1);
//                swap_image_view.setBackgroundColor(Color.parseColor("#FFFFFF"));
//            }
//
//            @Override
//            public void onOpened(SwipeRevealLayout view) {
//                swap_image_view.setScaleX(-1);
//                swap_image_view.setBackgroundColor(Color.parseColor("#F2F2F2"));
//            }
//
//            @Override
//            public void onSlide(SwipeRevealLayout view, float slideOffset) {
//
//            }
//        });
//        swipe.addSwipeListener(new SwipeLayout.SwipeListener() {
//            @Override
//            public void onStartOpen(SwipeLayout layout) {
//                swap_image_view.setScaleX(-1);
//                swap_image_view.setBackgroundColor(Color.parseColor("#F2F2F2"));
//            }
//
//            @Override
//            public void onOpen(SwipeLayout layout) {
//                swap_image_view.setScaleX(-1);
//                swap_image_view.setBackgroundColor(Color.parseColor("#F2F2F2"));
//            }
//
//            @Override
//            public void onStartClose(SwipeLayout layout) {
//                swap_image_view.setScaleX(1);
//                swap_image_view.setBackgroundColor(Color.parseColor("#FFFFFF"));
//            }
//
//            @Override
//            public void onClose(SwipeLayout layout) {
//                swap_image_view.setScaleX(1);
//                swap_image_view.setBackgroundColor(Color.parseColor("#FFFFFF"));
//
//            }
//
//            @Override
//            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
//
//            }
//
//            @Override
//            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
//
//            }
//        });

    }

    protected void checkedInDone() {
        hideAllActions();
        UtilityDriver.showAllViews(check_in_done_view);
        checkInOutBackground();

    }

    protected void initNoChange() {
        hideAllActions();
        UtilityDriver.showAllViews(check_in_view, absent_view);
//        changeLocationEnabled(true);
        normalBackground();
//        swipeLayout.close(false);
//        swipe.close(false);
    }


    protected void checkedOutDone() {
        hideAllActions();
        UtilityDriver.showAllViews(drop_off_done_view);
        checkInOutBackground();

    }

    protected void absenceDone() {
        UtilityDriver.goneAllViews(check_in_view, absent_view, check_in_done_view);
        UtilityDriver.showAllViews(absence_done_view, undo_absent);
        absentDropOffBackground();
    }

    protected void absenceByParentDone() {
        hideAllActions();
        UtilityDriver.showAllViews(absence_by_parent_done_view);
        absentDropOffBackground();
    }


//    protected void callEnabled(boolean enabled) {
//        imgCall.setVisibility(enabled ? View.VISIBLE : View.GONE);
//    }

//    protected void changeLocationEnabled(boolean enabled) {
//        labGrade.setVisibility(enabled ? View.VISIBLE : View.GONE);
//    }


    public RoundInfoHolderPickUp normalBackground() {
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.studentslist_normal_bg));
        return this;
    }

    public RoundInfoHolderPickUp absentDropOffBackground() {
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.studentslist_absent_dropoff_bg));
        return this;
    }

    public RoundInfoHolderPickUp checkInOutBackground() {
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.studentslist_check_in_out_bg));
        return this;
    }

    public RoundInfoHolderPickUp studentErrorBackground() {
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.studentslist_still_in_bus_bg));
        return this;
    }

    public RoundInfoHolderPickUp studentStillInBus() {
        studentErrorBackground();
        studentError();
        still_in_bus_view.setVisibility(View.VISIBLE);
        return this;
    }


    public RoundInfoHolderPickUp setEnabled(boolean enabled) {


        check_in_view.setEnabled(enabled);
        absent_view.setEnabled(enabled);
        check_in_done_view.setEnabled(enabled);
        absence_done_view.setEnabled(enabled);
        undo_absent.setEnabled(enabled);
        absence_by_parent_done_view.setEnabled(enabled);
        /**/
        checkin_image.setEnabled(enabled);
        checkin_text.setEnabled(enabled);
        absent_image.setEnabled(enabled);
        acbsent_text.setEnabled(enabled);


        return this;

    }


    public RoundInfoHolderPickUp setRoundEnded(boolean roundEnded) {
        hideAllActions();
        if (roundEnded)
            undo_absent.setVisibility(View.GONE);

        call_and_changelocation_view_container.setVisibility(roundEnded ? View.GONE : View.VISIBLE);
        if (!roundEnded) {
            initNoChange();
        }
        return this;

    }

    public RoundInfoHolderPickUp setEndedRoundState() {

        check_in_view.setEnabled(false);
        absent_view.setEnabled(false);
        check_in_done_view.setEnabled(false);
        absence_done_view.setEnabled(false);
        absence_by_parent_done_view.setEnabled(false);
        undo_absent.setEnabled(false);
        /**/
        checkin_image.setEnabled(false);
        checkin_text.setEnabled(false);
        absent_image.setEnabled(false);
        acbsent_text.setEnabled(false);

        return this;

    }


    private RoundInfoHolderPickUp hideAllActions() {
        UtilityDriver.goneAllViews(check_in_view, absent_view, check_in_done_view, absence_done_view, undo_absent);
//        absent_MaterialProgressBar.setVisibility(View.INVISIBLE);
        return this;
    }

    public RoundInfoHolderPickUp studentError() {
        student_error_img.setVisibility(View.VISIBLE);
        UtilViews.shakeViews(student_error_img);
        UtilViews.shakeViews(imgStudent);

        return this;
    }

    public RoundInfoHolderPickUp mustManualCheckOut() {
        showDropOff();
        return this;
    }

    protected RoundInfoHolderPickUp showDropOff() {
        hideAllActions();
        UtilityDriver.showAllViews(drop_off_view);
        return this;

    }

    protected RoundInfoHolderPickUp dropOffDone() {
        hideAllActions();
        UtilityDriver.showAllViews(drop_off_done_view);
        return this;

    }

    public RoundInfoHolderPickUp setStudentImage(String url) {
//        Glide.with(itemView.getContext()).load(url).fitCenter().into(imgStudent);
        DrawableToolsV2.loadDrawable(imgStudent, url);

        return this;
    }

//    public RoundInfoHolderPickUp changeLocationEnabled(boolean enabled) {
//        imgChangeLocation.setVisibility(enabled ? View.VISIBLE : View.GONE);
//        return this;
//    }

//    public RoundInfoHolderPickUp callEnabled(StudentBean studentBean) {
//        try {
//            imgCall.setEnabled(studentBean.getMobileStudentBean().hasFatherOrMotherNumber());
//        } catch (Exception e) {
//            imgCall.setEnabled(false);
//        }
//        return this;
//    }
//    ObjectAnimator absentProgressAnimation;

//    public RoundInfoHolderPickUp startAbsentProgressBar(OnActionDoneListener onFinish) {
//        absent_MaterialProgressBar.setVisibility(View.VISIBLE);
//        absent_MaterialProgressBar.setProgress(100);
//        absentProgressAnimation = ObjectAnimator.ofInt(absent_MaterialProgressBar, "progress", 100, 0);
//        absentProgressAnimation.setDuration(5000);
//        absentProgressAnimation.setInterpolator(new DecelerateInterpolator());
//        absentProgressAnimation.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                if (onFinish != null)
//                    onFinish.OnActionDone(absent_MaterialProgressBar);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//            }
//        });
//        absentProgressAnimation.start();
//
//        return this;
//    }


//    public RoundInfoHolderPickUp forceFinishProgressBar(){
//        if (absentProgressAnimation!=null)
//            absentProgressAnimation.end();
//
//        return this;
//    }

//    public RoundInfoHolderPickUp cancelAbsentProgress() {
//        if (absentProgressAnimation != null) {
//            absentProgressAnimation.removeAllListeners();
//            absentProgressAnimation.cancel();
//        }
//
//        /**/
//        if (absent_MaterialProgressBar != null)
//            absent_MaterialProgressBar.setVisibility(View.INVISIBLE);
//
//        /**/
//        return this;
//
//    }

//    public boolean isAbsentProgressWorking() {
//        boolean wasProssing = absent_MaterialProgressBar.getVisibility() == View.VISIBLE;
//        return wasProssing;
//    }

    public RoundInfoHolderPickUp editMode(boolean inEditMode) {
        checkin_and_absent_includeview.setVisibility(inEditMode ? View.INVISIBLE : View.VISIBLE);
        call_and_changelocation_view_container.setVisibility(inEditMode ? View.VISIBLE : View.INVISIBLE);
//        swipeRevealLayout.setLockDrag(inEditMode);
        real_item_view.setBackgroundColor(inEditMode ? ContextCompat.getColor(itemView.getContext(), R.color.md_blue_grey_100) : ContextCompat.getColor(itemView.getContext(), R.color.studentslist_normal_bg));
//        real_item_view.setBackground(inEditMode ?ContextCompat.getDrawable( itemView.getContext(), R.drawable.green_to_white_text_selector): ContextCompat.getDrawable( itemView.getContext(), android.R.drawable.screen_background_dark_transparent));
        return this;
    }


    @Override
    public void onItemSelected() {
        super.onItemSelected();
        real_item_view.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.md_blue_grey_400));

    }

    @Override
    public void onItemClear() {
        super.onItemClear();
        real_item_view.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.md_blue_grey_100));
    }
}
