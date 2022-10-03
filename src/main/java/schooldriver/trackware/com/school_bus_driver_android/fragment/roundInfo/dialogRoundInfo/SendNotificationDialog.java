package schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.dialogRoundInfo;

import android.app.Activity;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseDialog;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.gcmNotification.senderNotification.SendNotificationGCM;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created by muradtrac on 3/28/17.
 */

public class SendNotificationDialog extends BaseDialog {

    TextView txtMessage;
    Button btnOk, btnCancel;

    public SendNotificationDialog(final Activity mActivity, final List<StudentBean> listStudentBean, final int id) {
        super(mActivity);
        startDialog();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {




                String message = txtMessage.getText().toString();
                if (!UtilityDriver.isEmptyString(message)){
                     new SendNotificationGCM(listStudentBean,message,id);
                    dismiss();
                }else{
                    UtilityDriver.showMessage(mActivity,mActivity.getString(R.string.please_enter_message));
                }




            }
        });
    }
    public void startDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_notification);
        getWindow().setBackgroundDrawableResource(R.drawable.round_dialog_message);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnCancel = (Button) findViewById(R.id.btnCancel);


    }
}
