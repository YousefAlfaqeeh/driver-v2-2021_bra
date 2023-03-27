package schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BasePresenter;
import schooldriver.trackware.com.school_bus_driver_android.bean.RoundBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.enums.CheckEnum;
import schooldriver.trackware.com.school_bus_driver_android.enums.EnumConfigNotify;
import schooldriver.trackware.com.school_bus_driver_android.enums.TypeRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.dialogRoundInfo.ConfirmMessageDialog;
import schooldriver.trackware.com.school_bus_driver_android.locationListener.ConfigNotify;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;

/**
 * Created by   3/8/17.
 */

public class RoundInfoPresenter extends BasePresenter {

    private RoundInfoFragment roundInfoFragment;
    Context context;

    public RoundInfoPresenter(Context context, RoundInfoFragment roundInfoFragment) {
        this.context = context;
        this.roundInfoFragment = roundInfoFragment;

    }


//    public boolean checkRound(StudentBean studentBean) {
//        boolean value = true;
//        if (studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
//            if (studentBean.isAbsent()) {
//                value = false;
//            }
//        } else {
//            if (studentBean.isNoShow()) {
//                value = false;
//            }
//        }
//        if (studentBean.getCheckEnum() == CheckEnum.CHECK_IN) {
//            value = false;
//        }
//        return value;
//    }


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

    public List<StudentBean> swapListCheckout(List<StudentBean> listStudentBean) {

        List<StudentBean> newListStudentBean = new ArrayList<>();
        List<StudentBean> firstListStudentBean = new ArrayList<>();
        List<StudentBean> lastListStudentBean = new ArrayList<>();

        for (StudentBean studentBean : listStudentBean) {
            if (studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
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
                firstListStudentBean.add(studentBean);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StudentBean showAbsence(RoundInfoHolder holder, StudentBean studentBean) {
        holder.labDone.setVisibility(View.VISIBLE);
        holder.imgDone.setVisibility(View.VISIBLE);
        if (studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
            holder.labDone.setText(mActivity.getString(R.string.absence));
            holder.imgDone.setImageResource(R.drawable.img_none);
            holder.imgShow.setBackgroundResource(R.drawable.button_drop_off);
            holder.labDone.setTextColor(Color.RED);
            holder.imgDone.setColorFilter(mActivity.getResources().getColor(R.color.color_red));
            holder.imgShow.setBackgroundResource(R.drawable.button_drop_off);
            studentBean.setAbsent(true);
        } else {
            holder.labDone.setText(mActivity.getString(R.string.no_show));
            holder.imgDone.setImageResource(R.drawable.img_none);
            holder.imgShow.setBackgroundResource(R.drawable.button_drop_off);
            holder.labDone.setTextColor(Color.RED);
            holder.imgDone.setColorFilter(mActivity.getResources().getColor(R.color.color_red));
            holder.imgShow.setBackgroundResource(R.drawable.button_drop_off);
            studentBean.setIsNoShow(true);
        }

        return studentBean;
    }


    public StudentBean showAbsenceUpdate(RoundInfoHolder holder, StudentBean studentBean) {
        holder.labDone.setVisibility(View.VISIBLE);
        holder.imgDone.setVisibility(View.VISIBLE);
        if (studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
            holder.labDone.setText(mActivity.getString(R.string.absence));
            holder.imgDone.setImageResource(R.drawable.img_none);
            holder.labDone.setTextColor(Color.RED);
            holder.imgDone.setColorFilter(context.getResources().getColor(R.color.color_red));
            studentBean.setAbsent(true);
            holder.imgShow.setBackgroundResource(R.drawable.button_drop_off);
        } else {
            holder.labDone.setText(mActivity.getString(R.string.no_show));
            holder.imgDone.setImageResource(R.drawable.img_none);
            holder.labDone.setTextColor(Color.RED);
            holder.imgDone.setColorFilter(context.getResources().getColor(R.color.color_red));
            holder.imgShow.setBackgroundResource(R.drawable.button_drop_off);
            studentBean.setIsNoShow(!studentBean.isNoShow());
            if (studentBean.isNoShow() == false) {
                holder.labDone.setVisibility(View.GONE);
                holder.imgDone.setVisibility(View.GONE);
            }
        }

        return studentBean;
    }

    public StudentBean showCheck(RoundInfoHolder holder, StudentBean studentBean, CheckEnum checkEnum) {

        if (studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND && checkEnum == CheckEnum.CHECK_OUT) {
            holder.labDone.setVisibility(View.VISIBLE);
            holder.imgDone.setVisibility(View.VISIBLE);
            holder.labDone.setText(mActivity.getString(R.string.done));
            holder.imgDone.setImageResource(R.drawable.img_check_in);
            holder.labDone.setTextColor(Color.GREEN);
            holder.imgDone.setColorFilter(context.getResources().getColor(R.color.color_green));
        }
//        holder.imgCheck.setColorFilter();
        studentBean.setCheckEnum(checkEnum);
        if (studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND && checkEnum == CheckEnum.CHECK_IN) {
//            holder.imgCheck.setColorFilter(context.getResources().getColor(R.color.color_enable));
            holder.labDone.setVisibility(View.VISIBLE);
            holder.imgDone.setVisibility(View.VISIBLE);
            holder.labDone.setText(mActivity.getString(R.string.done));
            holder.imgDone.setImageResource(R.drawable.img_check_in);
            holder.labDone.setTextColor(Color.GREEN);
            holder.imgDone.setColorFilter(context.getResources().getColor(R.color.color_green));
        }
        if (studentBean.getTypeRoundEnum() == TypeRoundEnum.DROP_ROUND) {
            holder.labDone.setVisibility(View.GONE);
            holder.imgDone.setVisibility(View.GONE);
        }
        return studentBean;
    }

      
    public void checkChangeRoute(int roundId) {
        new ConfigNotify(
                StaticValue.latitudeMain + "",
                StaticValue.longitudeMain + "",
                roundId + "",
                EnumConfigNotify.ROUTE_CHANGED, mActivity);
        StaticValue.SUM_NOTIFICATION++;
    }

    public void checkStatusRound(RoundBean roundBean, ConfirmMessageDialog confirmMessageDialog) {

//        int noCheck = 0;
//        for (StudentBean studentBean : roundBean.getListStudentBean()) {
//            if (studentBean.getCheckEnum() == CheckEnum.CHECK_IN)
//                noCheck++;
//            if (studentBean.isAbsent() || studentBean.isNoShow())
//                noCheck++;
//
//        }
//        if (
//                noCheck == roundBean.getListStudentBean().size()
//                        ||
//                        labTypeRound.getText().toString().equals(context.getString(R.string.end_round))
//                ) {
        confirmMessageDialog = new ConfirmMessageDialog(mActivity);
        confirmMessageDialog.showMessageStatus(mActivity,roundInfoFragment, roundBean.getStatusRoundEnum(), roundBean);
//        } else {
//            UtilityDriver.showMessage(context, context.getString(R.string.please_end_check));
//        }
    }


}
