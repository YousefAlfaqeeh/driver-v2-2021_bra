package schooldriver.trackware.com.school_bus_driver_android.fragment.round;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.bean.RoundBean;
import schooldriver.trackware.com.school_bus_driver_android.enums.TypeRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created   on 3/1/2017.
 */

public class RoundAdapter extends RecyclerView.Adapter<RoundHolder> {

    List<RoundBean> listRoundBean;
    Activity mActivity;

    public RoundAdapter(List<RoundBean> listRoundBean, Activity mActivity) {
        this.listRoundBean = listRoundBean;
        this.mActivity = mActivity;
    }

    @Override
    public RoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_rounds, parent, false);
        return new RoundHolder(view);
    }

      
    @Override
    public void onBindViewHolder(RoundHolder holder, int position) {

        final RoundBean roundBean = listRoundBean.get(position);
        holder.txtTimeRound.setText(roundBean.getDateTime());
        if (UtilityDriver.getStringShared(UtilityDriver.LANGUAGE).equals("ar")) {
                holder.txtTimeRound.setGravity(Gravity.RIGHT);
        }
        holder.txtNameRound.setText(roundBean.getNameRound());
        holder.imgStartRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();
                bundle.putSerializable(UtilityDriver.ROUND, roundBean);
                String type = mActivity.getString(R.string.drop);
                if(roundBean.getListStudentBean()==null || roundBean.getListStudentBean().size() == 0){
                    UtilityDriver.showMessageDialog(mActivity,"","No students");
                    return;
                }
                if (roundBean.getListStudentBean().get(0).getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND){
                    type = mActivity.getString(R.string.pick);
                }
                bundle.putString(UtilityDriver.TYPE_ROUND, type);
                MainActivity.showFragmentRoundInfo(bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listRoundBean.size();
    }
}
