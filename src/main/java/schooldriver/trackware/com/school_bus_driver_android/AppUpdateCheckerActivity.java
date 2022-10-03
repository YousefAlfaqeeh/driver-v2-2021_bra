package schooldriver.trackware.com.school_bus_driver_android;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;

import java.util.Arrays;

import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ApiController;
import schooldriver.trackware.com.school_bus_driver_android.APIs_new.ListenersAndInterFaces.OnApiComplete;
import schooldriver.trackware.com.school_bus_driver_android.app.Application;
import schooldriver.trackware.com.school_bus_driver_android.bean.ApplicationVersionJson;
import schooldriver.trackware.com.school_bus_driver_android.geofence.mock.Constants;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.OnActionDoneListener;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilDialogs;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

public class AppUpdateCheckerActivity extends AppCompatActivity implements Constants {


    @Override
    protected void onStart() {
        super.onStart();
        checkForUpdate();
    }

    UtilDialogs.MessageYesNoDialog messageYesNoDialog;

    private void checkForUpdate() {

        ApiController.checkForLatestAppVersion(this, new OnApiComplete<ApplicationVersionJson>() {
            @Override
            public void onSuccess(ApplicationVersionJson applicationVersionJson) {
                try {
                    /**/
                    String googlePlayLink = applicationVersionJson.getGooglePlayLink();
                    String updateMessageAr = applicationVersionJson.getUpdateMessageAr();
                    String updateMessageEn = applicationVersionJson.getUpdateMessageEn();
                    String updateMessage = getString(R.string.update_available);
                    /**/
                    if (UtilityDriver.isEmptyString(googlePlayLink))
                        return;

                    if (!applicationVersionJson.checkIfAvailableUpdate())
                        return;


                    /**/
                    try {
                        if (!TextUtils.isEmpty(updateMessageAr) || !TextUtils.isEmpty(updateMessageEn)) {
                            if (UtilityDriver.getStringShared(UtilityDriver.LANGUAGE).equalsIgnoreCase("ar")) {
                                updateMessage = updateMessageAr;
                            } else {
                                updateMessage = updateMessageEn;
                            }
                        }


                    } catch (Exception e) {
                        updateMessage = getString(R.string.update_available);
                    }

                    if (messageYesNoDialog == null || !messageYesNoDialog.isShowing()) {
                        messageYesNoDialog = new UtilDialogs.MessageYesNoDialog().show(AppUpdateCheckerActivity.this)
                                .setYesButtonText(R.string.update_now)
                                .closeButtonVisible(!applicationVersionJson.checkIfShouldUpdate())
                                .setImageWithColor(R.drawable.ic_system_update_black_36dp, R.color.toolbar_bg_color)
                                .setDialogeTitle(updateMessage).setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                                    @Override
                                    public void OnActionDone(UtilDialogs.MessageYesNoDialog dialoge) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(googlePlayLink.trim())));
                                        if (dialoge != null)
                                            dialoge.dismiss();
                                        messageYesNoDialog = null;


                                    }
                                });

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    onError(0, Arrays.toString(e.getStackTrace()));
                }

            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                errorMessage = errorMessage == null ? "onError" : errorMessage;
                Application.logEvents("checkForLatestAppVersion", "Error on Update Check ", errorMessage);

            }
        });
    }
}
