package schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.adapters.DraggableHolder;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.DrawableToolsV2;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created   on 2/28/2017.
 */

public class RoundInfoHolderDropOff extends DraggableHolder {


    public TextView check_in_done_view;
    public TextView drop_off_done_view;
    public TextView no_show_done_view;
    public View drop_off_view;
    public View check_in_view;
    public View no_show_view;
    public ProgressBar undo_progressBar;
    public View call_and_changelocation_view_container;
    /**/
//    public View imgCall;
//    public View imgChangeLocation;
//    public View send_arrive_alarm;
    public View undo_no_show;
    /**/
    public TextView labStudentName;
    public TextView labGrade;
    public CircleImageView imgStudent;
    public View actions_view_container,swap_image_view;
    //        private SwipeLayout swipe;
//    public SwipeRevealLayout swipeRevealLayout;
//    SwipeLayout swipeLayout;
    public View real_item_view;
//    private MaterialProgressBar absent_MaterialProgressBar;

    public RoundInfoHolderDropOff(View itemView) {
        super(itemView);

        check_in_done_view = itemView.findViewById(R.id.check_in_done_view);
        drop_off_done_view = itemView.findViewById(R.id.drop_off_done_view);
        no_show_done_view = itemView.findViewById(R.id.no_show_done_view);
        drop_off_view = itemView.findViewById(R.id.drop_off_view);
        check_in_view = itemView.findViewById(R.id.check_in_view);
        no_show_view = itemView.findViewById(R.id.no_show_view);
        /**/
//        imgCall = itemView.findViewById(R.id.imgCall);
//        imgChangeLocation = itemView.findViewById(R.id.imgChangeLocation);
        call_and_changelocation_view_container = itemView.findViewById(R.id.call_and_changelocation_view_container);
        /**/
        imgStudent = itemView.findViewById(R.id.imgStudent);
        labStudentName = itemView.findViewById(R.id.labStudentName);
        labGrade = itemView.findViewById(R.id.labGrade);
//        send_arrive_alarm = itemView.findViewById(R.id.send_arrive_alarm);
        swap_image_view = itemView.findViewById(R.id.swap_image_view);
        undo_no_show = itemView.findViewById(R.id.undo_no_show);
        actions_view_container = itemView.findViewById(R.id.actions_view_container);
        /**/

//set show mode.
//        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
//        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, itemView.findViewById(R.id.bottom_wrapper));
        real_item_view = itemView.findViewById(R.id.real_item_view);
//        swipe.setShowMode(SwipeLayout.ShowMode.LayDown);

//        absent_MaterialProgressBar = itemView.findViewById(R.id.absent_MaterialProgressBar);

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

    public RoundInfoHolderDropOff setRoundEnded(boolean roundEnded) {
        hideAllActions();
        call_and_changelocation_view_container.setVisibility(roundEnded ? View.GONE : View.VISIBLE);
        if (!roundEnded) {
            initNoChange();
        }
        return this;

    }


    protected RoundInfoHolderDropOff showCheckIn() {
        hideAllActions();
        UtilityDriver.showAllViews(check_in_view);
        return this;

    }

    protected RoundInfoHolderDropOff checkInDone() {
        hideAllActions();
        UtilityDriver.showAllViews(check_in_done_view);
        absentDropOffBackground();
        return this;

    }

    protected RoundInfoHolderDropOff noShowDone() {
        hideAllActions();
//        UtilityDriver.goneAllViews(drop_off_view, check_in_done_view, drop_off_done_view,  drop_off_view, check_in_view, no_show_view,undo_no_show);

//        no_show_done_view.setVisibility(View.VISIBLE);
//        undo_no_show.setVisibility(View.VISIBLE);

        UtilityDriver.showAllViews(no_show_done_view, undo_no_show);
        absentDropOffBackground();
        return this;

    }


    protected RoundInfoHolderDropOff showDropOffButton() {
        hideAllActions();
        UtilityDriver.showAllViews(drop_off_view);
        drop_off_view.setEnabled(true);
        return this;

    }

    protected RoundInfoHolderDropOff disableDropOff() {
        drop_off_view.setEnabled(false);
        return this;
    }

    protected void initNoChange() {
//        if (swipeRevealLayout.isOpened())
//        swipeLayout.close(false);
        hideAllActions();
        UtilityDriver.showAllViews(check_in_view, no_show_view);
//        changeLocationEnabled(true);
        normalBackground();
//        swipe.close(false);


    }


    protected RoundInfoHolderDropOff dropOffDone() {
        hideAllActions();
        UtilityDriver.showAllViews(drop_off_done_view);
        return this;

    }


//    public RoundInfoHolderDropOff callEnabled(StudentBean studentBean) {
//        try {
//            imgCall.setEnabled(studentBean.getMobileStudentBean().hasFatherOrMotherNumber());
//        } catch (Exception e) {
//            imgCall.setEnabled(false);
//        }
//        return this;
//    }

//    protected RoundInfoHolderDropOff changeLocationEnabled(boolean enabled) {
//        labGrade.setVisibility(enabled ? View.VISIBLE : View.GONE);
//        return this;
//    }

    private RoundInfoHolderDropOff hideAllActions() {
        UtilityDriver.goneAllViews(drop_off_view, drop_off_done_view, check_in_view, check_in_done_view, no_show_done_view, no_show_view, undo_no_show);
//        absent_MaterialProgressBar.setVisibility(View.INVISIBLE);
        return this;
    }


    public RoundInfoHolderDropOff normalBackground() {
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.studentslist_normal_bg));
        return this;
    }

    public RoundInfoHolderDropOff absentDropOffBackground() {
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.studentslist_absent_dropoff_bg));
        return this;
    }

    public RoundInfoHolderDropOff checkInOutBackground() {
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.studentslist_check_in_out_bg));
        return this;

    }

    public RoundInfoHolderDropOff setStudentImage(String url) {
//        Glide.with(itemView.getContext()).load(url).fitCenter().into(imgStudent);
        DrawableToolsV2.loadDrawable(imgStudent, url);

        return this;
    }


//    public RoundInfoHolderDropOff changeLocationEnabled(boolean enabled) {
//        imgChangeLocation.setVisibility(enabled ? View.VISIBLE : View.GONE);
//        return this;
//    }


    public RoundInfoHolderDropOff editMode(boolean inEditMode) {
        actions_view_container.setVisibility(inEditMode ? View.INVISIBLE : View.VISIBLE);
        call_and_changelocation_view_container.setVisibility(inEditMode ? View.VISIBLE : View.INVISIBLE);
//        swipeLayout.setLockDrag(inEditMode);
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


//    ObjectAnimator absentProgressAnimation;
//    public RoundInfoHolderDropOff startNoShowProgressBar(OnActionDoneListener onFinish) {
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

//    public RoundInfoHolderDropOff cancelNoShowProgress() {
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

//    public boolean isNoShowProgressWorking() {
//        boolean wasProssing = absent_MaterialProgressBar.getVisibility() == View.VISIBLE;
//        return wasProssing;
//    }


}
