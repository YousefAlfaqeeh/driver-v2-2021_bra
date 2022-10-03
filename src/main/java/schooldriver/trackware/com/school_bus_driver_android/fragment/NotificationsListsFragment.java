package schooldriver.trackware.com.school_bus_driver_android.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ApiController;
import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ListenersAndInterFaces.OnApiComplete;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.adapters.RecyclerViewAdapter;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseFragment;
import schooldriver.trackware.com.school_bus_driver_android.bean.NotificationBean;
import schooldriver.trackware.com.school_bus_driver_android.fragment.notification.NotificationHolder_new;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.DateTools;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.DrawableToolsV2;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilDialogs;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created by Ibrahem Al-Betar on 2/28/2017.
 */
public class NotificationsListsFragment extends BaseFragment {

    private RecyclerViewAdapter notificationListAdapter;
    private ArrayList<NotificationBean> notificationsList = new ArrayList<>();
    private RecyclerView notification_list;

    public static Fragment newInstance() {
        NotificationsListsFragment fragment = new NotificationsListsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_new, container, false);
        notification_list = (RecyclerView) view.findViewById(R.id.notification_list);
        view.findViewById(R.id.linNotification).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.linBack).setOnClickListener(backClickListener);
        /**/
        initAdapter();
        /**/
        notification_list.setAdapter(notificationListAdapter);


        ApiController.getNotification(getActivity(), new OnApiComplete<Object>() {
            @Override
            public void onSuccess(Object o) {
                      /**/
                try {
                    ArrayList<NotificationBean> notificationBeans = parceNotifications(new JSONObject(o.toString()));
                    if (notificationBeans != null && notificationBeans.size() > 0) {
                        notificationsList.clear();
                        notificationsList.addAll(notificationBeans);

                        notificationListAdapter.addAll(notificationsList);
                    } else {
                        throw new Exception("error");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int errorCode, String errorMessage) {

            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private RecyclerViewAdapter initAdapter() {
        notification_list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).drawable(R.drawable.list_divider).margin(30, 30).build());
        notification_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        /**/
        notificationListAdapter = new RecyclerViewAdapter<NotificationBean, NotificationHolder_new>() {
            @Override
            public NotificationHolder_new cViewHolder(ViewGroup viewGroup, int i, LayoutInflater layoutInflater) {
                return new NotificationHolder_new(layoutInflater.inflate(R.layout.list_notification_new, viewGroup, false));
            }

            @Override
            public void bViewHolder(final NotificationHolder_new viewHolder, int i, final NotificationBean item) {
                viewHolder.notification_text.setText(item.getDetails());
//                viewHolder.notification_time.setText(item.getTime());
                try {
                    long dateLong = DateTools.Formats.DATE_FORMAT_GMT.parse(item.getTime()).getTime();

                    viewHolder.notification_time.setText("");
                    if (DateUtils.isToday(dateLong)) {
                        viewHolder.notification_time.append(mActivity.getString(R.string.today));
                        viewHolder.notification_time.append(" ");
                        viewHolder.notification_time.append(mActivity.getString(R.string.at_time));
                        viewHolder.notification_time.append(" ");
                        viewHolder.notification_time.append(DateTools.Formats.TIMEONLY_FORMAT_12H_LOCAL.format(dateLong));
                    } else {
                        viewHolder.notification_time.append(DateTools.Formats.DATEONLY_FORMAT_LOCAL.format(dateLong));
                        viewHolder.notification_time.append(" ");
                        viewHolder.notification_time.append(mActivity.getString(R.string.at_time));
                        viewHolder.notification_time.append(" ");
                        viewHolder.notification_time.append(DateTools.Formats.TIMEONLY_FORMAT_12H_LOCAL.format(dateLong));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    viewHolder.notification_time.setVisibility(View.INVISIBLE);
                }
//                Glide.with(getActivity()).load(item.getAvatar()).fitCenter().into(viewHolder.notification_image);
                DrawableToolsV2.loadDrawable(viewHolder.notification_image, item.getAvatar());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        new UtilDialogs.MessageYesNoDialog().show(getActivity())
                                .setYesButtonText(R.string.ok)
                                .hideCloseButton()
//                                .setDialogeMessage(item.getDetails())
                                .setImageWithColor(R.drawable.img_notification, R.color.color_header)
                                .setDialogeTitle(item.getDetails())
                                .setDialogeMessage("\n"+viewHolder.notification_time.getText().toString());
//                                .loadImageFromURL(item.getAvatar());


                    }
                });
            }
        };
        return notificationListAdapter;


    }


    private ArrayList<NotificationBean> parceNotifications(JSONObject response) {
        ArrayList<NotificationBean> listNotificationBean = new ArrayList<>();
        try {
            JSONArray jaNotifications = response.getJSONArray("notifications");
            NotificationBean notificationBean = null;
            for (int i = 0; i < jaNotifications.length(); i++) {
                JSONObject joNotification = jaNotifications.getJSONObject(i);
                notificationBean = new NotificationBean();
                if (!UtilityDriver.isEmptyString(joNotification.getString("date")))
                    notificationBean.setTime(joNotification.getString("date"));
                notificationBean.setAvatar(joNotification.getString("image"));
                notificationBean.setDetails(joNotification.getString("message"));
                listNotificationBean.add(notificationBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listNotificationBean;

    }

}
