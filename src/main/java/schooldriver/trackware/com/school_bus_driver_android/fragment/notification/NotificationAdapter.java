package schooldriver.trackware.com.school_bus_driver_android.fragment.notification;

import android.app.Activity;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.ParseException;
import java.util.List;

import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.bean.NotificationBean;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.DateTools;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created by Ibrahem Al-Betar on 3/2/2017.
 */


public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {

    List<NotificationBean> listNotificationBean;
    Activity mActivity;
    ImageLoader imageLoader;
    DisplayImageOptions defaultOptions;
    ImageLoaderConfiguration config;


    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notification, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {
        NotificationBean notificationBean = listNotificationBean.get(position);
        holder.txtDetails.setText(notificationBean.getDetails());
//        holder.btnTime.setText(notificationBean.getTime());
        try {
            long dateLong = DateTools.Formats.DATE_FORMAT_GMT.parse(notificationBean.getTime()).getTime();

            holder.btnTime.setText("");
            if (DateUtils.isToday(dateLong)) {
                holder.btnTime.append(mActivity.getString(R.string.today));
                holder.btnTime.append(" ");
                holder.btnTime.append(mActivity.getString(R.string.at_time));
                holder.btnTime.append(" ");
                holder.btnTime.append(DateTools.Formats.TIMEONLY_FORMAT_12H_LOCAL.format(dateLong));
            } else {
                holder.btnTime.append(DateTools.Formats.DATEONLY_FORMAT_LOCAL.format(dateLong));
                holder.btnTime.append(" ");
                holder.btnTime.append(mActivity.getString(R.string.at_time));
                holder.btnTime.append(" ");
                holder.btnTime.append(DateTools.Formats.TIMEONLY_FORMAT_12H_LOCAL.format(dateLong));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            holder.btnTime.setVisibility(View.INVISIBLE);
        }

        imageLoader.displayImage(notificationBean.getAvatar(), holder.imgStudent);
        if (UtilityDriver.getStringShared(UtilityDriver.LANGUAGE).equals("ar")) {
            holder.btnTime.setGravity(Gravity.CENTER);
        }
//        holder.imgStudent.setImageDrawable(UtilityDriver.getDrawableImage(mActivity,RoundFragment.arrayNameEN[position]));
    }

    @Override
    public int getItemCount() {
        return listNotificationBean.size();
    }
}
