package schooldriver.trackware.com.school_bus_driver_android.fragment.notification;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.bean.NotificationBean;

/**
 * Created by Ibrahem Al-Betar on 3/2/2017.
 */

public class NotifiactionFragment extends Fragment {


    Activity mActivity;
    RecyclerView rsDrower;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notification, container, false);

        rsDrower=(RecyclerView) view.findViewById(R.id.rsleft_drawer);

        List<NotificationBean> listNotificationBeen=new ArrayList<NotificationBean>();
//        listNotificationBeen.add(new NotificationBean(1,"Murad checkin","","7:00am ago"));
//        listNotificationBeen.add(new NotificationBean(2,"Ahmad checkin","","7:50am ago"));
//        listNotificationBeen.add(new NotificationBean(3,"Bader checkin","","8:30am ago"));
//        listNotificationBeen.add(new NotificationBean(4,"Mickel checkin","","8:30am ago"));

        setListAdapter(listNotificationBeen);
        return view;
    }

    public void setListAdapter(List<NotificationBean> notificationBeanList) {

        // use a linear layout manager
      /*  RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        rsDrower.setLayoutManager(mLayoutManager);
         NotificationAdapter notificationAdapter = new NotificationAdapter(notificationBeanList, mActivity);
        rsDrower.setAdapter(notificationAdapter);*/
    }
}
