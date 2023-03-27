package schooldriver.trackware.com.school_bus_driver_android.basePage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import database.DAO;
import database.OpenHelper;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import schooldriver.trackware.com.school_bus_driver_android.API.ApiFacade;
import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;
import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.bean.RoundBean;
import schooldriver.trackware.com.school_bus_driver_android.fragment.NotificationsListsFragment;
import schooldriver.trackware.com.school_bus_driver_android.gcmNotification.NotificationObj;
import schooldriver.trackware.com.school_bus_driver_android.gcmNotification.senderNotification.SendNotificationGCM;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.OnActionDoneListener;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.DriverConstants;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilDialogs;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

  
public class BaseFragment<T> extends Fragment implements  DriverConstants {


    private UtilDialogs.ProcessingDialog processDialog;
    public static MainActivity mActivity;
    protected int distanceLimit = 500;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mActivity = getMainActivity();
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


  

    protected void callRestAPI(
            String PATH_URL,
            HashMap params,
            EnumMethodApi verb,
            IRestCallBack restCallBack,
            EnumNameApi enumNameApi,
            Map<String, String> mapHeader,
            EnumTypeHeader enumTypeHeader
    ) {
        ApiFacade callApi = new ApiFacade();
        callApi.onStartVolley(new ApiRequest(PATH_URL,
                        params,
                        verb,
                        restCallBack,
                        enumNameApi,
                        mapHeader
                )
                ,
                enumTypeHeader
        );
    }


    protected void showSendMessageDialog(final RoundBean roundBean) {
        new UtilDialogs.SendMessageDialog().show(getMainActivity()).
                setOnSendClickListener(new OnActionDoneListener<String>() {
                    @Override
                    public void OnActionDone(String message) {
                        Log.v("iiiiiiiiiiiiiiiiiiiiiiiiii","ooooooooooooo");
                        new SendNotificationGCM(roundBean.getListStudentBean(), message, roundBean.getId());
                    }
                });


    }

    public void setFragment(Fragment fragment, Bundle bundle, boolean clearStack) {
        ((MainActivity) getActivity()).setFragment(fragment, bundle, clearStack);
    }

    protected View.OnClickListener backClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    protected View.OnClickListener notificationIconClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            setFragment(NotificationsListsFragment.newInstance(), null, false);

        }
    };

    protected String getTimerText() {
        long savedTime = Long.parseLong(UtilityDriver.getStringShared(UtilityDriver.START_ROUND_TIME).trim());
        long nowTime = System.currentTimeMillis();
        long timerUntillNow = nowTime - savedTime;
        StringBuilder timeFormat = new StringBuilder();
        String hours = String.format("%02d", ((timerUntillNow / (1000 * 60 * 60)) % 24));
        String minutes = String.format("%02d", ((timerUntillNow / (1000 * 60)) % 60));
        String seconds = String.format("%02d", (timerUntillNow / 1000) % 60);
        /**/
        timeFormat.append(hours);
        timeFormat.append(":");
        timeFormat.append(minutes);
        timeFormat.append(":");
        timeFormat.append(seconds);
        /**/
        return timeFormat.toString();
    }


    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    public void showProcessingDialog(int stringId) {
        stringId = stringId == 0 ? R.string.loading : stringId;
        processDialog = new UtilDialogs.ProcessingDialog().show(getMainActivity(), stringId);
    }

    public void hideProcessingDialog() {
        if (processDialog != null)
            processDialog.dismiss();
    }


    protected void showAPIErrorDialog(String errorMessage) {
        new UtilDialogs.MessageYesNoDialog().show(getActivity())
                .setDialogeTitle(R.string.error)
                .setDialogeTitleTextColor(R.color.red_tabs)
                .setDialogeMessage(errorMessage)
                .hideCloseButton()
                .setYesButtonText(R.string.ok)
                .setImageWithColor(R.drawable.img_error, 0);

    }

    @Override
    public void onResume() {
        super.onResume();
        checkMessagesFromNotification();
//        getMainActivity().setOnBackButtonPressed(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @SuppressLint("ResourceAsColor")
    public void showTip(View view,
                        String message) {
        new SimpleTooltip.Builder(getActivity())
                .anchorView(view)
                .text(message)
                .gravity(Gravity.CENTER)
                .animated(false)
//                .highlightShape()
                .modal(true)
                .contentView(R.layout.tooltip_custom, R.id.tv_text)
//                .backgroundColor(Color.parseColor("#FFFFFF"))
                .showArrow(false)
                .transparentOverlay(true)
                .build()
                .show();


    }

    /**/
    UtilDialogs.MessageYesNoDialog messageYesNoDialog;

    public void checkMessagesFromNotification() {
        try {
            if (messageYesNoDialog != null)
                return;

            NotificationObj notificationObj = DAO.ImportantNotificationTable.getOne(OpenHelper.getDatabase(getMainActivity()));
            if (notificationObj == null)
                return;
            messageYesNoDialog = new UtilDialogs.MessageYesNoDialog().show(getActivity())
                    .setDialogeTitle(notificationObj.getTitle())
                    .setDialogeTitleTextColor(R.color.red_tabs)
                    .setDialogeMessage(notificationObj.getMessage())
                    .hideCloseButton()
                    .setYesButtonText(R.string.ok)
                    .removeImageBackground()
                    .setImageWithColor(R.drawable.admin_msg, 0).setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                        @Override
                        public void OnActionDone(UtilDialogs.MessageYesNoDialog Action) {
                            boolean deleteed = DAO.ImportantNotificationTable.delete(OpenHelper.getDatabase(getMainActivity()), notificationObj.getId());
                            if (messageYesNoDialog != null)
                            messageYesNoDialog.dismiss();
                            messageYesNoDialog = null;
                            checkMessagesFromNotification();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

}
