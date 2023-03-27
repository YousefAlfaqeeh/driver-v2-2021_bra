package schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.dialogRoundInfo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseDialog;
import schooldriver.trackware.com.school_bus_driver_android.bean.RoundBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.enums.CheckEnum;
import schooldriver.trackware.com.school_bus_driver_android.enums.StatusRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.enums.TypeRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.RoundInfoFragment;
import schooldriver.trackware.com.school_bus_driver_android.gcmNotification.senderNotification.SendNotificationGCM;
import schooldriver.trackware.com.school_bus_driver_android.geofence.mock.Constants;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.IActionDialog;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created by   3/11/17.
 */

public class ConfirmMessageDialog extends BaseDialog {
    TextView labType, /*labName,*/
            labTittle;
    Button btnOk, btnCancel;
    ImageView imgInfo;
    ConfirmMessagePresenter mPresenter;








    public ConfirmMessageDialog(Activity context) {
        super(context);
    }


    /**
     * Create Dialog Status round ( START || END)
     */



    public void showDialogLogout(Activity context, String title, String body) {
        try {
            this.mActivity = context;
            setCancelable(false);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_message);
            getWindow().setBackgroundDrawableResource(R.drawable.round_dialog_message);
//            int width, height;
//            if (StaticValue.diagonalInches >= 6.5) {
//                width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.45);
//                height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.45);
//            } else {
//                width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.7);
//                height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.37);
//            }
//            getWindow().setLayout(width, height);
            labType = (TextView) findViewById(R.id.labType);
            labTittle = (TextView) findViewById(R.id.labTittle);
            btnOk = (Button) findViewById(R.id.btnOk);
            btnCancel = (Button) findViewById(R.id.btnCancel);
            imgInfo = (ImageView) findViewById(R.id.imgInfo);
            imgInfo.setImageResource(R.drawable.img_worning);
            mPresenter = new ConfirmMessagePresenter(mActivity);
//        labName.setText(mActivity.getString(R.string.alarm));
//        labType.setText(mActivity.getString(R.string.cancel_round));
            labTittle.setText(title);
            labType.setText(body);
            btnCancel.setVisibility(View.VISIBLE);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dismiss();
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StaticValue.progressDialog != null) {
                        StaticValue.progressDialog.dismiss();
                    }
                    MainActivity.showFragmentLogin();

                    dismiss();
                }
            });
        } catch (Exception e) {

        }
    }




    public void showMessageCheck(Activity context, final IActionDialog statusDialog, final StudentBean studentBean, final String status, final int roundId) {
        try {
            this.mActivity = context;
//        super(mActivity);
//        this.mActivity = mActivity;
            mPresenter = new ConfirmMessagePresenter(mActivity, statusDialog);

//        startDialog();
            setCancelable(false);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_message);
            getWindow().setBackgroundDrawableResource(R.drawable.round_dialog_message);
//            int width, height;
//            if (StaticValue.diagonalInches >= 6.5) {
//                width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.45);
//                height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.45);
//            } else {
//                width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.7);
//                height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.37);
//            }
//            getWindow().setLayout(width, height);
            labType = (TextView) findViewById(R.id.labType);
            labTittle = (TextView) findViewById(R.id.labTittle);
            btnOk = (Button) findViewById(R.id.btnOk);
            btnCancel = (Button) findViewById(R.id.btnCancel);
            imgInfo = (ImageView) findViewById(R.id.imgInfo);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StaticValue.progressDialog != null) {
                        StaticValue.progressDialog.dismiss();
                    }
                    dismiss();
                }
            });

//        if (status.equals("out") || status.equals("in")) {
            if (studentBean.getCheckEnum() == CheckEnum.EMPTY) {
                labTittle.setText(mActivity.getString(R.string.conf_student_check_in));
                labType.setText(mActivity.getString(R.string.sure_check_in).replace("@@@@@@", studentBean.getFirstNameStudent()));
            } else if (studentBean.getCheckEnum() == CheckEnum.CHECK_IN) {
                labTittle.setText(mActivity.getString(R.string.conf_student_check_out));
                labType.setText(mActivity.getString(R.string.sure_check_out).replace("@@@@@@", studentBean.getFirstNameStudent()));
            }
//        } else {
//            if (studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
//                labType.setText(mActivity.getString(R.string.absence));
//                statusValue = "absent";
//            } else {
//                labType.setText(mActivity.getString(R.string.no_show));
//                statusValue = "no-show";
//            }
//        }
//        labName.setText(studentBean.getNameStudent());
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.callApiSetCheckShow((int) studentBean.getId(), status, roundId, studentBean.isNoShow(), reason);
                    String sendMessage = null;
                    if (!UtilityDriver.isEmptyString(statusValue)) {

//                        String messageType = "";
//                        if (studentBean.getMobileStudentBean().getPlatform().contains("android")) {
//                            messageType = new JSONObject(mapBodyMessage).toString();
//                        } else {
//                            messageType = UtilityDriver.MESSAGE_IPHONE.replaceAll("MESSAGEMESSAGE", sendMessage);
//                        }
                        if (studentBean.getMobileStudentBean().isCheckInFather()) {
                            sendMessage = UtilityDriver.getMessageNotification(statusValue, studentBean.getTypeRoundEnum(),studentBean.getMobileStudentBean().getFatherLocal());
                            sendMessage = sendMessage.replaceAll(STUDENT_NAME_API_FORMAT, studentBean.getFirstNameStudent());
                            sendMessage = sendMessage.replaceAll(ROUND_NAME_API_FORMAT, RoundInfoFragment.roundBean.getNameRound());
//                        sendMessage = sendMessage.replaceAll("@", "");


                            Map<String, String> mapBodyMessage = new HashMap<>();
                            mapBodyMessage.put("message", sendMessage);
//                            mapBodyMessage.put("avatar", studentBean.getAvatar());
                            mapBodyMessage.put("title", SendNotificationGCM.createNotificationTitle(studentBean.getMobileStudentBean().getFatherLocal()));
                            mapBodyMessage.put("action", Constants.NOTIFICATION_ACTION_Other);


                            SendNotificationGCM sendNotificationGCM = new SendNotificationGCM();
                            StaticValue.listNearby.add(studentBean.getFirstNameStudent());
                            sendNotificationGCM.sendNotification(false,studentBean.getMobileStudentBean().getFatherToken(), studentBean.getMobileStudentBean().getFatherMessageType(mapBodyMessage,sendMessage), (Integer) studentBean.getId(), roundId, studentBean.getMobileStudentBean().getFatherPlatform(), studentBean.getAvatar(),studentBean.getMobileStudentBean().getFatherLocal(),studentBean.getNameStudent(),studentBean.getMobileStudentBean().getFatherId());
//                            sendNotificationGCM.sendNotification(studentBean.getMobileStudentBean().getFatherToken(), messageType, (Integer) studentBean.getId(), roundId, studentBean.getMobileStudentBean().getPlatform(), studentBean.getAvatar());
                        }
                        if (studentBean.getMobileStudentBean().isCheckInMother()) {
                            sendMessage = UtilityDriver.getMessageNotification(statusValue, studentBean.getTypeRoundEnum(),studentBean.getMobileStudentBean().getMotherLocal());
                            sendMessage = sendMessage.replaceAll(STUDENT_NAME_API_FORMAT, studentBean.getFirstNameStudent());
                            sendMessage = sendMessage.replaceAll(ROUND_NAME_API_FORMAT, RoundInfoFragment.roundBean.getNameRound());
//                        sendMessage = sendMessage.replaceAll("@", "");


                            Map<String, String> mapBodyMessage = new HashMap<>();
                            mapBodyMessage.put("message", sendMessage);
//                            mapBodyMessage.put("avatar", studentBean.getAvatar());
                            mapBodyMessage.put("title", SendNotificationGCM.createNotificationTitle(studentBean.getMobileStudentBean().getMotherLocal()));
                            mapBodyMessage.put("action", Constants.NOTIFICATION_ACTION_Other);


                            SendNotificationGCM sendNotificationGCM = new SendNotificationGCM();
                            StaticValue.listNearby.add(studentBean.getFirstNameStudent());
                            sendNotificationGCM.sendNotification(false,studentBean.getMobileStudentBean().getMotherToken(), studentBean.getMobileStudentBean().getMotherMessageType(mapBodyMessage,sendMessage), (Integer) studentBean.getId(), roundId, studentBean.getMobileStudentBean().getMotherPlatform(), studentBean.getAvatar(),studentBean.getMobileStudentBean().getMotherPlatform(),studentBean.getNameStudent(),studentBean.getMobileStudentBean().getMotherId());
//                            sendNotificationGCM.sendNotification(studentBean.getMobileStudentBean().getMotherToken(), messageType, (Integer) studentBean.getId(), roundId, studentBean.getMobileStudentBean().getPlatform(), studentBean.getAvatar());
                        }
                    }
                    dismiss();
                }
            });
            show();
        } catch (Exception e) {

        }
    }


    public void showDialogCancel(Activity context, final Integer id, final RoundInfoFragment roundInfoFragment) {
        try {
            this.mActivity = context;
            setCancelable(false);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_message_cancel);
            getWindow().setBackgroundDrawableResource(R.drawable.round_dialog_message);
//            int width, height;
//            if (StaticValue.diagonalInches >= 6.5) {
//                width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.45);
//                height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.45);
//            } else {
//                width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.7);
//                height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.37);
//            }
//            getWindow().setLayout(width, height);
//            labType = (TextView) findViewById(R.id.labType);
            List<String> list = UtilityDriver.getArrayMessage("cancel");
            LinearLayout linResult = (LinearLayout) findViewById(R.id.linResult);
            createRadioButton(list,mActivity,linResult);
            labTittle = (TextView) findViewById(R.id.labTittle);
            btnOk = (Button) findViewById(R.id.btnOk);
            btnCancel = (Button) findViewById(R.id.btnCancel);
            imgInfo = (ImageView) findViewById(R.id.imgInfo);
            imgInfo.setImageResource(R.drawable.img_worning);
            mPresenter = new ConfirmMessagePresenter(mActivity);
//        labName.setText(mActivity.getString(R.string.alarm));
//        labType.setText(mActivity.getString(R.string.cancel_round));
            labTittle.setText(mActivity.getString(R.string.cancel_the_round));
//            labType.setText(mActivity.getString(R.string.cancel_round));
            btnCancel.setVisibility(View.VISIBLE);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dismiss();
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StaticValue.progressDialog != null) {
                        StaticValue.progressDialog.dismiss();
                    }
                    mPresenter.callApiCancelRound(id, roundInfoFragment,reason);
                    dismiss();
                }
            });
        } catch (Exception e) {

        }
    }

    //
    public void showMessageStatus(Activity context, final IActionDialog statusDialog, final StatusRoundEnum statusRoundEnum, final RoundBean roundBean) {
        try {
            this.mActivity = context;
//        startDialogStatus();

            setCancelable(false);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_message_status);
            getWindow().setBackgroundDrawableResource(R.drawable.round_dialog_message);

//            int width, height;
//            if (StaticValue.diagonalInches >= 6.5) {
//                width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.45);
//                height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.45);
//            } else {
//                width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.7);
//                height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.37);
//            }
//            getWindow().setLayout(width, height);

            labType = (TextView) findViewById(R.id.labType);
//        labName = (TextView) findViewById(R.id.labName);
            btnOk = (Button) findViewById(R.id.btnOk);
            btnCancel = (Button) findViewById(R.id.btnCancel);
            imgInfo = (ImageView) findViewById(R.id.imgInfo);
            labTittle = (TextView) findViewById(R.id.labTittle);

//        labName.setVisibility(View.INVISIBLE);
//        labType.setText(statusDialog);
            mPresenter = new ConfirmMessagePresenter(mActivity, statusDialog);

            if (statusRoundEnum == StatusRoundEnum.START) {
                labType.setText(mActivity.getString(R.string.start_the_round));
                labTittle.setText(mActivity.getString(R.string.start_round));
            } else {
                labType.setText(mActivity.getString(R.string.end_the_round));
                labTittle.setText(mActivity.getString(R.string.end_round));
            }
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StaticValue.progressDialog != null) {
                        StaticValue.progressDialog.dismiss();
                    }
                    dismiss();
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                  
                @Override
                public void onClick(View v) {

                    mPresenter.callApiStatusRound(statusRoundEnum, (int) roundBean.getId());
//                statusDialog.done(statusRoundEnum);
                    dismiss();
                }
            });

            show();
        } catch (Exception e) {

        }
    }


    public void changeLocationHome(Activity context, EnumNameApi enumNameApi, final StudentBean studentBean) {
        try {
            this.mActivity = context;
//        startDialog();
            setCancelable(false);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_message);
            getWindow().setBackgroundDrawableResource(R.drawable.round_dialog_message);
//            int width, height;
//            if (StaticValue.diagonalInches >= 6.5) {
//                width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.45);
//                height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.45);
//            } else {
//                width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.7);
//                height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.37);
//            }
//            getWindow().setLayout(width, height);
            labType = (TextView) findViewById(R.id.labType);
            labTittle = (TextView) findViewById(R.id.labTittle);
            btnOk = (Button) findViewById(R.id.btnOk);
            btnCancel = (Button) findViewById(R.id.btnCancel);
            imgInfo = (ImageView) findViewById(R.id.imgInfo);

//        labName.setText(mActivity.getString(R.string.are_you_sure));
//        labType.setText(mActivity.getString(R.string.change_location) + studentBean.getNameStudent() + "?");
            labType.setText(mActivity.getString(R.string.change_location_message).replace("@@@@@@", studentBean.getFirstNameStudent()));
            labTittle.setText(mActivity.getString(R.string.change_location));
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StaticValue.progressDialog != null) {
                        StaticValue.progressDialog.dismiss();
                    }
                    dismiss();
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.showFragmentChangeLocation((Integer) studentBean.getId());
                    dismiss();
                }
            });
            show();
        } catch (Exception e) {

        }
    }


    public void showDialogTittle(Activity context, String tittle, String body) {
        try {
            this.mActivity = context;
//        startDialogError();

            setCancelable(false);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_error);
            getWindow().setBackgroundDrawableResource(R.drawable.round_dialog_message);
//            int width, height;
//            if (StaticValue.diagonalInches >= 6.5) {
//                width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.45);
//                height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.45);
//            } else {
//                width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.7);
//                height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.37);
//            }
//            getWindow().setLayout(width, height);
            labType = (TextView) findViewById(R.id.labType);
            labTittle = (TextView) findViewById(R.id.labTittle);
            btnOk = (Button) findViewById(R.id.btnOk);
            btnCancel = (Button) findViewById(R.id.btnCancel);

            if (tittle.equals("")){
                labTittle.setVisibility(View.GONE);
            }

            if (body.equals("")){
                labType.setVisibility(View.GONE);
            }
            labTittle.setText(tittle);
            labType.setText(body);
            btnCancel.setVisibility(View.GONE);

            if (mActivity.getString(R.string.please_charger).equals(body)){
                ((ImageView) findViewById(R.id.imgInfo)).setImageDrawable(ContextCompat.getDrawable(mActivity,R.drawable.buttery_low));
            }

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StaticValue.progressDialog != null) {
                        StaticValue.progressDialog.dismiss();
                    }
                    dismiss();
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dismiss();
                }
            });
        } catch (Exception e) {

        }
    }
//
//    /**
//     * Create Dialog Check out all
//     */
//    public ConfirmMessageDialog(final Activity mActivity, final IActionDialog statusDialog, final int roundId) {
//        super(mActivity);
//        this.mActivity = mActivity;
//        mPresenter = new ConfirmMessagePresenter(mActivity, statusDialog);
//        startDialog();
//        labName.setVisibility(View.INVISIBLE);
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (StaticValue.progressDialog != null) {
//                    StaticValue.progressDialog.dismiss();
//                }
//                dismiss();
//            }
//        });
//        labType.setText(mActivity.getString(R.string.check_out_all));
//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPresenter.callApiCheckOutAll(roundId);
//                dismiss();
//            }
//        });
//    }
    /**
     * Create Dialog check , absent and show
     */
    String statusValue = "";



    String reason = "";
    private void createRadioButton(List<String> list,Activity mActivity,LinearLayout linearLayout) {
        final EditText textView = new EditText(mActivity);
        final RadioButton[] rb = new RadioButton[list.size()];
        RadioGroup rGroup = new RadioGroup(mActivity); //create the RadioGroup
        rGroup.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for(int i=0; i<rb.length; i++){
            rb[i]  = new RadioButton(mActivity);
            rb[i].setText(list.get(i).replaceAll(BUS_NUMBER_API_FORMAT,""));
            rb[i].setId(i + 100);
            if (list.get(i).contains(BUS_NUMBER_API_FORMAT)) {

                LinearLayout linearLayout1 = new LinearLayout(mActivity);
                linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout1.addView(rb[i]);
                linearLayout1.addView(textView);
                rGroup.addView(linearLayout1);
            }else{
                rGroup.addView(rb[i]);
            }
        }
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    // Changes the textview's text to "Checked: example radiobutton text"
                    reason = checkedRadioButton.getText().toString()+textView.getText().toString();
                }
            }
        });
        linearLayout.addView(rGroup);//you add the whole RadioGroup to the layout

    }
    public void showMessageAbsent(Activity context, final IActionDialog statusDialog, final StudentBean studentBean, final String status, final int roundId) {
        try {


            this.mActivity = context;
            mPresenter = new ConfirmMessagePresenter(mActivity, statusDialog);

//        startDialog();
            setCancelable(false);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_message_cancel);
            getWindow().setBackgroundDrawableResource(R.drawable.round_dialog_message);
//            int width, height;
//            if (StaticValue.diagonalInches >= 6.5) {
//                width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.45);
//                height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.45);
//            } else {
//                width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.7);
//                height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.37);
//            }
//            getWindow().setLayout(width, height);
            labType = (TextView) findViewById(R.id.labType);

            labTittle = (TextView) findViewById(R.id.labTittle);
            btnOk = (Button) findViewById(R.id.btnOk);
            btnCancel = (Button) findViewById(R.id.btnCancel);
            imgInfo = (ImageView) findViewById(R.id.imgInfo);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StaticValue.progressDialog != null) {
                        StaticValue.progressDialog.dismiss();
                    }
                    dismiss();
                }
            });
            List<String> list = UtilityDriver.getArrayMessage(status);
            LinearLayout linResult = (LinearLayout) findViewById(R.id.linResult);
            createRadioButton(list,mActivity,linResult);
//        if (status.equals("out") || status.equals("in")) {
//            if (studentBean.getCheckEnum() == CheckEnum.EMPTY) {
//                labType.setText(mActivity.getString(R.string.check_in));
//            } else if (studentBean.getCheckEnum() == CheckEnum.CHECK_IN) {
//                labType.setText(mActivity.getString(R.string.check_out));
//            }
//        } else {


        /*
        *  if (studentBean.getCheckEnum() == CheckEnum.EMPTY) {
                labTittle.setText(mActivity.getString(R.string.conf_student_check_in));
                labType.setText(mActivity.getString(R.string.sure_check_in).replace("@@@@@@",studentBean.getNameStudent()));
            } else if (studentBean.getCheckEnum() == CheckEnum.CHECK_IN) {
                labTittle.setText(mActivity.getString(R.string.conf_student_check_out));
                labType.setText(mActivity.getString(R.string.sure_check_out).replace("@@@@@@",studentBean.getNameStudent()));
            }*/
            if (studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
                labTittle.setText(mActivity.getString(R.string.conf_student_absent));
//                labType.setText(mActivity.getString(R.string.sure_absent).replace("@@@@@@", studentBean.getNameStudent()));
                statusValue = "absent";
            } else {
                labTittle.setText(mActivity.getString(R.string.conf_student_no_show));
//                labType.setText(mActivity.getString(R.string.sure_no_show).replace("@@@@@@", studentBean.getNameStudent()));
                statusValue = "no-show";
            }
//        }
//        labName.setText(studentBean.getNameStudent());
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UtilityDriver.isEmptyString(reason)){
                        UtilityDriver.showMessage(mActivity,"Please check");
                        return;
                    }
                    mPresenter.callApiSetCheckShow((int) studentBean.getId(), status, roundId, studentBean.isNoShow(),reason);
                    String sendMessage = null;

                    if (!UtilityDriver.isEmptyString(statusValue)) {


                        if (studentBean.getMobileStudentBean().isCheckInFather()) {
                            sendMessage = UtilityDriver.getMessageNotification(statusValue, studentBean.getTypeRoundEnum(),studentBean.getMobileStudentBean().getFatherLocal());
                            sendMessage = sendMessage.replaceAll(STUDENT_NAME_API_FORMAT, studentBean.getFirstNameStudent());
                            sendMessage = sendMessage.replaceAll(ROUND_NAME_API_FORMAT, RoundInfoFragment.roundBean.getNameRound());
//                        sendMessage = sendMessage.replaceAll("@", "");
                            Map<String, String> mapBodyMessage = new HashMap<>();
                            mapBodyMessage.put("message", sendMessage);
//                            mapBodyMessage.put("avatar", studentBean.getAvatar());
                            mapBodyMessage.put("title", SendNotificationGCM.createNotificationTitle(studentBean.getMobileStudentBean().getFatherLocal()));
                            mapBodyMessage.put("action", Constants.NOTIFICATION_ACTION_Other);


                            SendNotificationGCM sendNotificationGCM = new SendNotificationGCM();

                            StaticValue.listNearby.add(studentBean.getFirstNameStudent());
                            sendNotificationGCM.sendNotification(false,studentBean.getMobileStudentBean().getFatherToken(), studentBean.getMobileStudentBean().getFatherMessageType(mapBodyMessage,sendMessage), (Integer) studentBean.getId(), roundId, studentBean.getMobileStudentBean().getFatherPlatform(), studentBean.getAvatar(),studentBean.getMobileStudentBean().getFatherLocal(),studentBean.getNameStudent(),studentBean.getMobileStudentBean().getFatherId());
//                            sendNotificationGCM.sendNotification(studentBean.getMobileStudentBean().getFatherToken(), messageType, (Integer) studentBean.getId(), roundId, studentBean.getMobileStudentBean().getPlatform(), studentBean.getAvatar());
                        }
                        if (studentBean.getMobileStudentBean().isCheckInMother()) {
                            sendMessage = UtilityDriver.getMessageNotification(statusValue, studentBean.getTypeRoundEnum(),studentBean.getMobileStudentBean().getMotherLocal());
                            sendMessage = sendMessage.replaceAll(STUDENT_NAME_API_FORMAT, studentBean.getFirstNameStudent());
                            sendMessage = sendMessage.replaceAll(ROUND_NAME_API_FORMAT, RoundInfoFragment.roundBean.getNameRound());
//                        sendMessage = sendMessage.replaceAll("@", "");
                            Map<String, String> mapBodyMessage = new HashMap<>();
                            mapBodyMessage.put("message", sendMessage);
//                            mapBodyMessage.put("avatar", studentBean.getAvatar());
                            mapBodyMessage.put("title", SendNotificationGCM.createNotificationTitle(studentBean.getMobileStudentBean().getMotherLocal()));
                            mapBodyMessage.put("action", Constants.NOTIFICATION_ACTION_Other);


                            SendNotificationGCM sendNotificationGCM = new SendNotificationGCM();

                            StaticValue.listNearby.add(studentBean.getFirstNameStudent());
                            sendNotificationGCM.sendNotification(false,studentBean.getMobileStudentBean().getMotherToken(), studentBean.getMobileStudentBean().getMotherMessageType(mapBodyMessage,sendMessage), (Integer) studentBean.getId(), roundId, studentBean.getMobileStudentBean().getMotherPlatform(), studentBean.getAvatar(),studentBean.getMobileStudentBean().getMotherLocal(),studentBean.getNameStudent(),studentBean.getMobileStudentBean().getMotherId());
//                            sendNotificationGCM.sendNotification(studentBean.getMobileStudentBean().getMotherToken(), messageType, (Integer) studentBean.getId(), roundId, studentBean.getMobileStudentBean().getPlatform(), studentBean.getAvatar());
                        }
                    }
                    dismiss();
                }
            });
            show();
        } catch (NullPointerException e) {

        }
    }


//    public void startDialogStatus() {
////        requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//
//    }


//    public void startDialog() {
////        requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//
//    }

//    public void startDialogError() {
////        requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//    }
}

