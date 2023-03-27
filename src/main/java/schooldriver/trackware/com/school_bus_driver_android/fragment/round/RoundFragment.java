package schooldriver.trackware.com.school_bus_driver_android.fragment.round;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ApiController;
import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ListenersAndInterFaces.OnApiComplete;
import schooldriver.trackware.com.school_bus_driver_android.LoginFragment;
import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.adapters.ViewPagerAdapter;
import schooldriver.trackware.com.school_bus_driver_android.app.Application;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseFragment;
import schooldriver.trackware.com.school_bus_driver_android.bean.NotificationBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.RoundBean;
import schooldriver.trackware.com.school_bus_driver_android.custoom_views.NonSwipeableViewPager;
import schooldriver.trackware.com.school_bus_driver_android.enums.TypeRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.fragment.NotificationsListsFragment;
import schooldriver.trackware.com.school_bus_driver_android.fragment.RoundsListsFragment;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.RoundInfoFragment;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.RoundInfoFragmentDropOff;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.RoundInfoFragmentPickUp;
import schooldriver.trackware.com.school_bus_driver_android.gcmNotification.firebace.MyFirebaseMessagingService_new;
import schooldriver.trackware.com.school_bus_driver_android.geofence.mock.GPSCallback;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.OnActionDoneListener;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilDialogs;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;


public class RoundFragment extends BaseFragment implements GPSCallback {


    private static TextView labNotification;
    private RecyclerView rsRounds;
    MainActivity mActivity;
    private View imgLogout, linNotification, loading_view;
    private RoundPresenter mPresenter;
    public static List<RoundBean> listRoundBean;
    private NonSwipeableViewPager main_ViewPager;
    private TabLayout main_TabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private int readyScreensCount = 0;
    public RoundBean mustEndedRoundm = null;

    /**/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getMainActivity();
        RoundInfoFragment.COUNT = 0;
        RoundInfoFragment.ROUND_ID_SOCKET = 0;
        RoundInfoFragment.roundBeanCheck = null;
//        MainActivity.latitude = 0;
//        MainActivity.longitude = 0;
        StaticValue.latitudeSpeed = 0;
        StaticValue.longitudeSpeed = 0;
        /**/
        mPresenter = new RoundPresenter(mActivity, this);
        UtilityDriver.setLocale(UtilityDriver.getStringShared(UtilityDriver.LANGUAGE), mActivity);
        mPresenter.callApiLogin(UtilityDriver.getStringShared(UtilityDriver.PIN), "bus_pin");
        UtilityDriver.setBooleanShared(UtilityDriver.TYPE_ROUND, false);
        final View view = inflater.inflate(R.layout.fragment_round_with_tabs, container, false);
        linNotification = view.findViewById(R.id.linNotification);
        loading_view = view.findViewById(R.id.loading_view);
        imgLogout = view.findViewById(R.id.imgLogout);


        rsRounds = (RecyclerView) view.findViewById(R.id.rsRound);
        actionListenerPage();
        if (StaticValue.typeRoundEnum == null)
            StaticValue.typeRoundEnum = TypeRoundEnum.PICK_ROUND;
//        UpdateButton(StaticValue.typeRoundEnum);
        RoundInfoFragment.CHECK_OUT_ALL = false;
        RoundInfoFragment.listGeofenceBean = null;

        /**/
        main_ViewPager = (NonSwipeableViewPager) view.findViewById(R.id.main_ViewPager);
        main_ViewPager.setOffscreenPageLimit(2);
        main_TabLayout = (TabLayout) view.findViewById(R.id.main_TabLayout);
        initPager();
        /**/

        return view;
    }


    public static RoundFragment newInstance() {
        return new RoundFragment();
    }

    UtilDialogs.MessageYesNoDialog logout;

    public void actionListenerPage() {
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (logout != null)
                    logout.dismiss();

                logout = new UtilDialogs.MessageYesNoDialog().show(getActivity())
                        .setYesButtonText(R.string.yes_value)
//                        .setDialogeTitle(R.string.logout)
                        .setDialogeTitle(R.string.do_you_log_out)
                        .setImageWithColor(R.drawable.img_logout, R.color.toolbar_bg_color)
                        .setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                            @Override
                            public void OnActionDone(UtilDialogs.MessageYesNoDialog dialog) {
                                if (dialog != null)
                                    dialog.dismiss();
                                UtilityDriver.setBooleanShared(UtilityDriver.LOGIN, false);
                                setFragment(new LoginFragment(), null, false);
                            }
                        });


            }
        });


    }


    private void initTabLayout(NonSwipeableViewPager main_ViewPager, TabLayout main_TabLayout) {
        if (main_TabLayout != null) {
            main_TabLayout.setupWithViewPager(main_ViewPager);
            for (int i = 0; i < main_TabLayout.getTabCount(); i++) {
                View tabview = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.tab_item_view, null, false);
                if (i == 0) {
                    ((AppCompatImageView) tabview.findViewById(R.id.tab_icon)).setImageResource(R.drawable.img_pick_up);
                    tabview.setBackgroundResource(R.drawable.tab_bg_selector_green);
                } else if (i == 1) {
                    ((AppCompatImageView) tabview.findViewById(R.id.tab_icon)).setImageResource(R.drawable.img_drop_off);
                    tabview.setBackgroundResource(R.drawable.tab_bg_selector_red);
                }


                main_TabLayout.getTabAt(i).setCustomView(tabview);
            }
        }
    }


    private OnActionDoneListener<Object> onFragmentReady = new OnActionDoneListener<Object>() {
        @Override
        public void OnActionDone(Object callBack) { // there is Started Round

            if (callBack instanceof String) {//name of ready fragment
                if (++readyScreensCount == 2) {
                    readyScreensCount = 0;
                    loading_view.setVisibility(View.GONE);
                }
            }

            if (callBack instanceof RoundBean) { // started Round
                RoundBean startedRound = (RoundBean) callBack;
                BaseFragment roundInfoFragment;
                if (startedRound.getTypeRoundEnum().equals(TypeRoundEnum.PICK_ROUND)) {
                    roundInfoFragment = new RoundInfoFragmentPickUp();
                } else {
                    roundInfoFragment = new RoundInfoFragmentDropOff();
                }

                Bundle bundle = new Bundle();
                bundle.putParcelable(UtilityDriver.ROUND, startedRound);
                setFragment(roundInfoFragment, bundle, false);

            } else if (callBack instanceof Integer) {

            }


        }
    };


    RoundsListsFragment pickFragment;
    RoundsListsFragment dropFragment;

    public void refreshPages() {
        mustEndedRoundm = null;
        if (pickFragment != null)
            pickFragment.doRefresh();
        if (dropFragment != null)
            dropFragment.doRefresh();
    }


    public void initPager() {
        /**/
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        main_ViewPager.setAdapter(viewPagerAdapter);
        mustEndedRoundm = null;
        /**/
        main_ViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                UtilityDriver.setIntShared(UtilityDriver.SELECTED_TAB, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /**/
        OnActionDoneListener onMustEndedRoundDetected = new OnActionDoneListener<RoundBean>() {
            @Override
            public void OnActionDone(RoundBean mustEndedRound) {
                mustEndedRoundm = mustEndedRound;
            }
        };
        /**/
        OnActionDoneListener<RoundsListsFragment> onSwipeRefresh = new OnActionDoneListener<RoundsListsFragment>() {
            @Override
            public void OnActionDone(RoundsListsFragment roundsListsFragment) {
                mustEndedRoundm = null;
                pickFragment.doRefresh();
                dropFragment.doRefresh();
            }
        };
        /**/

        pickFragment = RoundsListsFragment.newInstance("pick", onFragmentReady, this).setOnMustEndedRoundDetected(onMustEndedRoundDetected).setOnSwipeRefresh(onSwipeRefresh);
        dropFragment = RoundsListsFragment.newInstance("drop", onFragmentReady, this).setOnMustEndedRoundDetected(onMustEndedRoundDetected).setOnSwipeRefresh(onSwipeRefresh);
        /**/
        viewPagerAdapter.addFragment(pickFragment, getString(R.string.pick), "fpick");
        viewPagerAdapter.addFragment(dropFragment, getString(R.string.drop), "fdrop");
        initTabLayout(main_ViewPager, main_TabLayout);
        main_ViewPager.setCurrentItem(UtilityDriver.getIntShared(UtilityDriver.SELECTED_TAB));

    }

    private void refreshReciver() {
        getMainActivity().registerAbsentReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    checkMessagesFromNotification();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (intent.hasExtra(MyFirebaseMessagingService_new.ABSENT_RECEIVER)) {
                        refreshPages();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    public void onGPSUpdate(Location location) {
        Log.d("Location", "onGPSUpdate " + location.getLatitude() + "," + location.getLongitude());

    }


    @Override
    public void onResume() {
        super.onResume();

        Log.v("shouldRefresh", "" + Application.shouldRefresh);
//        if (Application.shouldRefresh) {
        try {
            mPresenter = new RoundPresenter(mActivity, this);
            initNotificationList();

            linNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StaticValue.SUM_NOTIFICATION = 0;
                    setFragment(NotificationsListsFragment.newInstance(), null, false);

                }
            });

            refreshReciver();

//        viewPagerAdapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
        Application.shouldRefresh = false;
//        }


    }


    ArrayList<NotificationBean> listNotificationBean = new ArrayList<>();

    private void initNotificationList() {
        ApiController.getNotification(getActivity(), new OnApiComplete<Object>() {
            @Override
            public void onSuccess(Object o) {
                try {
                    JSONObject jsonObject = new JSONObject(o.toString());
                    listNotificationBean.clear();
                    JSONArray jaNotifications = jsonObject.getJSONArray("notifications");
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

            }

            @Override
            public void onError(int errorCode, String errorMessage) {

            }
        });
    }
}
