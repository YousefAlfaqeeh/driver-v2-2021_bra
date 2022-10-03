package schooldriver.trackware.com.school_bus_driver_android.utilityDriver;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.OnActionDoneListener;

public final class UtilDialogs implements DriverConstants {


    private static MaterialDialog processDialog;
    //    private static MaterialDialog messageYesNoDialog;
    private static MaterialDialog sendMessageDialog;
    private static MaterialDialog callFatherMotherDialog, studentToolDialog;
    private static MaterialDialog reasonDialog;


    /**/
    private static ArrayList<MaterialDialog> toastMessage = new ArrayList<>();

    /**/
    public static class ProcessingDialog {


        public ProcessingDialog show(Activity context, Integer stringId) {
            try {


                if (processDialog != null) {
                    processDialog.cancel();
                    processDialog.dismiss();
                    processDialog = null;
                }
            } catch (Exception e) {

            }
//            if (processDialog == null) {
            if (stringId == null)
                stringId = R.string.waiting;
            processDialog = new MaterialDialog.Builder(context)
                    .content(stringId)
                    .progress(true, 0)
                    .canceledOnTouchOutside(false)
                    .cancelable(false)
                    .build();
//            }

            processDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            processDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dialog_round_shape));
            processDialog.show();

            return this;
        }

        public void dismiss() {
            try {
                if (processDialog != null && processDialog.isShowing()) {
                    processDialog.dismiss();
                }
            } catch (Exception e) {

            }

        }
    }


    public static class MessageDialog {
        private MaterialDialog messageDialog;
        /**/
        public AppCompatButton dialoge_button;
        public TextView dialoge_message, dialoge_title;
        public AppCompatImageView dialoge_image;
        public Activity context;

        public MessageDialog show(Activity context) {

            this.context = context;
            try {


                if (messageDialog != null) {
                    messageDialog.cancel();
                    messageDialog.dismiss();
                    messageDialog = null;
                }
            } catch (Exception e) {

            }


            messageDialog = new MaterialDialog.Builder(context).
                    canceledOnTouchOutside(false).
                    cancelable(false).
                    customView(R.layout.dialoge_message, true).
                    autoDismiss(true).build();

            dialoge_button = (AppCompatButton) messageDialog.getView().findViewById(R.id.dialoge_button);
            dialoge_message = (TextView) messageDialog.getView().findViewById(R.id.dialoge_message);
            dialoge_title = (TextView) messageDialog.getView().findViewById(R.id.dialoge_title);
            dialoge_image = (AppCompatImageView) messageDialog.getView().findViewById(R.id.dialoge_image);
            /**/
            dialoge_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            /**/
            messageDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dialog_round_shape));
            messageDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            messageDialog.show();
            return this;
        }

        public MessageDialog setImage(int imageId) {
            dialoge_image.setImageDrawable(ContextCompat.getDrawable(context, imageId));
            return this;
        }

        public boolean isShowing() {
            return messageDialog.isShowing();
        }

        public MessageDialog setDialogeMessage(int messageId) {
            setDialogeMessage(context.getString(messageId));
            return this;
        }

        public MessageDialog setDialogeMessage(String messageid) {
            dialoge_message.setVisibility(View.VISIBLE);
            dialoge_message.setText(messageid);
            return this;
        }

        public MessageDialog setDialogeTitle(int messageId) {
            dialoge_title.setVisibility(View.VISIBLE);
            dialoge_title.setText(messageId);
            return this;
        }

        public MessageDialog setButtonText(int messageId) {
            dialoge_button.setText(messageId);
            return this;
        }

        public MessageDialog setButtonClickListener(final OnActionDoneListener onActionDoneListener) {
            dialoge_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onActionDoneListener.OnActionDone(MessageDialog.this);
                }
            });
            return this;
        }


        public void dismiss() {
            try {
                if (messageDialog != null && messageDialog.isShowing()) {
                    messageDialog.dismiss();
                }
            } catch (Exception e) {

            }

        }

    }


    public static class MessageYesNoDialog {
        MaterialDialog messageYesNoDialog;
        /**/
        public Button dialoge_button_yes/*, dialoge_button_no*/, dialoge_button_outher;
        public TextView dialoge_message, dialoge_title;
        public AppCompatImageView dialoge_image;
        public Activity context;
        public View dialoge_image_close;

        public MessageYesNoDialog show(Activity context) {
            if (context == null)
                return null;

            this.context = context;
            try {


                if (messageYesNoDialog != null) {
                    messageYesNoDialog.cancel();
                    messageYesNoDialog.dismiss();
                    messageYesNoDialog = null;
                }
            } catch (Exception e) {

            }


            messageYesNoDialog = new MaterialDialog.Builder(context).
                    canceledOnTouchOutside(false).
                    cancelable(false).theme(Theme.DARK).
                    customView(R.layout.dialog_yes_no_message, true).
                    autoDismiss(true).build();

            dialoge_button_yes = messageYesNoDialog.getView().findViewById(R.id.dialoge_button_yes);
            dialoge_button_outher = messageYesNoDialog.getView().findViewById(R.id.dialoge_button_outher);
            dialoge_image_close = messageYesNoDialog.getView().findViewById(R.id.dialoge_image_close);
            dialoge_message = (TextView) messageYesNoDialog.getView().findViewById(R.id.dialoge_message);
            dialoge_title = (TextView) messageYesNoDialog.getView().findViewById(R.id.dialoge_title);
            dialoge_image = (AppCompatImageView) messageYesNoDialog.getView().findViewById(R.id.dialoge_image);
            /**/
            View.OnClickListener clickListenerDismiss = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            };
            dialoge_image_close.setOnClickListener(clickListenerDismiss);
            dialoge_button_yes.setOnClickListener(clickListenerDismiss);
            /**/
            messageYesNoDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            messageYesNoDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            messageYesNoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            messageYesNoDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dialog_round_shape));

//            messageYesNoDialog.getWindow().setDimAmount(0);

            try {
                messageYesNoDialog.show();
            } catch (Exception e) {
                e.printStackTrace();

            }


            return this;
        }

        public MessageYesNoDialog setImageWithColor(int imageId, int color) {
            if (color == 0) {
                dialoge_image.setImageDrawable(ContextCompat.getDrawable(context, imageId));
            } else {
                dialoge_image.setImageDrawable(UtilityDriver.getTintDrawable(context, imageId, ContextCompat.getColor(context, color)));
            }

            return this;
        }

        public MessageYesNoDialog removeImageBackground() {
            dialoge_image.setBackgroundResource(0);
            dialoge_image.setPadding(0, 0, 0, 0);
            return this;
        }


        public MessageYesNoDialog hideImage() {
            dialoge_image.setVisibility(View.GONE);
            return this;
        }

        public MessageYesNoDialog setDialogeMessage(String message) {
            if (message.contains("unknown host")) {
                message = context.getString(R.string.missing_internet_error);
            }
            dialoge_message.setVisibility(View.VISIBLE);
            dialoge_message.setText(message);
            return this;
        }

        public MessageYesNoDialog setDialogeMessage(int messageid) {
            dialoge_message.setVisibility(View.VISIBLE);
            dialoge_message.setText(messageid);
            return this;
        }

        public MessageYesNoDialog setDialogeMessage(Spanned spannedText) {
            dialoge_message.setVisibility(View.VISIBLE);
            dialoge_message.setText(spannedText);
            return this;
        }


        public MessageYesNoDialog loadImageFromURL(String imageUrl) {
//            Glide.with(context).load(imageUrl).fitCenter().into(dialoge_image);
            DrawableToolsV2.loadDrawable(dialoge_image, imageUrl);

            return this;
        }


        public MessageYesNoDialog setDialogeTitle(String messageId) {
            dialoge_title.setVisibility(View.VISIBLE);
            dialoge_title.setText(messageId);
            return this;
        }

        public MessageYesNoDialog setDialogeTitle(Spanned spannedText) {
            dialoge_title.setVisibility(View.VISIBLE);
            dialoge_title.setText(spannedText);
            return this;
        }


        public MessageYesNoDialog setDialogeTitleTextColor(int textColor) {
            dialoge_title.setVisibility(View.VISIBLE);
            dialoge_title.setTextColor(ContextCompat.getColor(context, textColor));
            return this;
        }

        public MessageYesNoDialog setDialogeTitle(int messageId) {
            dialoge_title.setVisibility(View.VISIBLE);
            dialoge_title.setText(messageId);
            return this;
        }

        public MessageYesNoDialog hideCloseButton() {
            dialoge_image_close.setVisibility(View.GONE);
            return this;
        }


        public MessageYesNoDialog closeButtonVisible(boolean visible) {
            if (visible)
                dialoge_image_close.setVisibility(View.VISIBLE);
            else
                dialoge_image_close.setVisibility(View.GONE);

            return this;
        }

        public MessageYesNoDialog setYesButtonText(int messageId) {
            dialoge_button_yes.setText(messageId);
            return this;
        }

        public MessageYesNoDialog setOutherButtonText(int messageId) {
            dialoge_button_outher.setText(messageId);
            return this;
        }

        public MessageYesNoDialog showOutherButton() {
            dialoge_button_outher.setVisibility(View.VISIBLE);
            return this;
        }


        public MessageYesNoDialog setOutherButtonClickListener(final OnActionDoneListener<UtilDialogs.MessageYesNoDialog> onActionDoneListener) {
            dialoge_button_outher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onActionDoneListener.OnActionDone(MessageYesNoDialog.this);
                }
            });
            return this;
        }

        public MessageYesNoDialog setYesButtonClickListener(final OnActionDoneListener<UtilDialogs.MessageYesNoDialog> onActionDoneListener) {
            dialoge_button_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onActionDoneListener.OnActionDone(MessageYesNoDialog.this);
                }
            });
            return this;
        }

        public MessageYesNoDialog setCloseButtonClickListener(final OnActionDoneListener<UtilDialogs.MessageYesNoDialog> onActionDoneListener) {
            dialoge_image_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onActionDoneListener.OnActionDone(MessageYesNoDialog.this);
                }
            });
            return this;
        }


        public void dismiss() {
            try {
                if (messageYesNoDialog != null && messageYesNoDialog.isShowing()) {
                    messageYesNoDialog.dismiss();
                    messageYesNoDialog = null;
                }
            } catch (Exception e) {

            }

        }

        public boolean isShowing() {
            if (messageYesNoDialog != null)
                return messageYesNoDialog.isShowing();
            return false;
        }
    }


    public static class CallFatherMotherDialog {

        public Activity context;
        public View father_number_view, mother_number_view;
        public StudentBean studentBean;

        public CallFatherMotherDialog show(Activity context, final StudentBean studentBean) {
            this.context = context;
            try {
                if (callFatherMotherDialog != null) {
                    callFatherMotherDialog.cancel();
                    callFatherMotherDialog.dismiss();
                    callFatherMotherDialog = null;
                }
            } catch (Exception e) {

            }


            callFatherMotherDialog = new MaterialDialog.Builder(context).
                    canceledOnTouchOutside(false).
                    cancelable(false).theme(Theme.DARK).
                    customView(R.layout.dialog_call_new, true).
                    autoDismiss(true).build();

            father_number_view = callFatherMotherDialog.getView().findViewById(R.id.father_number_view);
            mother_number_view = callFatherMotherDialog.getView().findViewById(R.id.mother_number_view);


            /**/
//            callFatherMotherDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//            callFatherMotherDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
//            callFatherMotherDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.trans_to_dark_selector));

            callFatherMotherDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            callFatherMotherDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            callFatherMotherDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            callFatherMotherDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    callFatherMotherDialog.getView().findViewById(R.id.dialoge_image_close).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismiss();
                        }
                    });
                    initFatherNumberView(studentBean);
                    initMotherNumberView(studentBean);
                }
            });
            callFatherMotherDialog.show();
            callFatherMotherDialog.getView().findViewById(R.id.dialoge_image_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callFatherMotherDialog != null)
                        callFatherMotherDialog.dismiss();
                }
            });


            return this;
        }


        public void dismiss() {
            try {
                if (callFatherMotherDialog != null && callFatherMotherDialog.isShowing()) {
                    callFatherMotherDialog.dismiss();
                }
            } catch (Exception e) {

            }

        }

        public void initFatherNumberView(final StudentBean studentBean) {
            try {
                if (UtilityDriver.isEmptyString(studentBean.getMobileStudentBean().getFatherNumber())) {
                    father_number_view.setVisibility(View.GONE);
                } else {
                    father_number_view.setVisibility(View.VISIBLE);
                    father_number_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dismiss();
                                    callNumber(studentBean.getMobileStudentBean().getFatherNumber());
                                }
                            }, 1000);

                        }
                    });
                }
            } catch (Exception e) {
                father_number_view.setVisibility(View.GONE);
            }

        }

        public void initMotherNumberView(final StudentBean studentBean) {
            try {


                if (UtilityDriver.isEmptyString(studentBean.getMobileStudentBean().getMotherNumber())) {
                    mother_number_view.setVisibility(View.GONE);
                } else {
                    mother_number_view.setVisibility(View.VISIBLE);
                    mother_number_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dismiss();
                                    callNumber(studentBean.getMobileStudentBean().getMotherNumber());
                                }
                            }, 1000);

                        }
                    });

                }
            } catch (Exception e) {

            }
        }

        private void callNumber(String number) {
            String uri = "tel:" + number;
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                context.startActivity(intent);
            } catch (Exception e) {
            }
        }
    }


    public static class SendMessageDialog {

        /**/

        /**/
        public Activity context;
        public AppCompatEditText message_EditText;
        public View dialoge_image_close, dialoge_button_send;


        public SendMessageDialog show(Activity context) {

            this.context = context;
            try {


                if (sendMessageDialog != null) {
                    sendMessageDialog.cancel();
                    sendMessageDialog.dismiss();
                    sendMessageDialog = null;
                }
            } catch (Exception e) {

            }


            sendMessageDialog = new MaterialDialog.Builder(context).
                    canceledOnTouchOutside(false).
                    cancelable(false).theme(Theme.DARK).
                    customView(R.layout.dialog_send_message, true).
                    autoDismiss(true).build();

            message_EditText = (AppCompatEditText) sendMessageDialog.getView().findViewById(R.id.message_EditText);
            dialoge_image_close = sendMessageDialog.getView().findViewById(R.id.dialoge_image_close);
            dialoge_button_send = sendMessageDialog.getView().findViewById(R.id.dialoge_button_send);
            /**/
            View.OnClickListener clickListenerDismiss = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            };
            dialoge_image_close.setOnClickListener(clickListenerDismiss);
            /**/
//            sendMessageDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//            messageYesNoDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
//            sendMessageDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.trans_to_dark_selector));


            sendMessageDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            sendMessageDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            sendMessageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            sendMessageDialog.show();

            return this;
        }

        public SendMessageDialog setOnSendClickListener(final OnActionDoneListener<String> onActionDoneListener) {
            dialoge_button_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!message_EditText.getText().toString().trim().equals("")) {
                        dismiss();
                        onActionDoneListener.OnActionDone(message_EditText.getText().toString().trim());
                    } else {
                        UtilViews.shakeViews(message_EditText);
                    }

                }
            });
            return this;
        }

        public void dismiss() {
            try {
                if (sendMessageDialog != null && sendMessageDialog.isShowing()) {
                    sendMessageDialog.dismiss();
                }
            } catch (Exception e) {

            }

        }

    }


    public static class ToastMessageDialog {

        /**/
        private MaterialDialog toast_message_dialog;
        public CircleImageView dialoge_image;
        public TextView dialoge_title;
        public MainActivity mainActivity;
        public String messageText = "";
        public String imageUrl = "";
        public DialogInterface.OnDismissListener onDismissListener;
        public Handler dismissHandler = new Handler(Looper.getMainLooper());

        public boolean isShowing() {
            return toast_message_dialog.isShowing();
        }

        public UtilDialogs.ToastMessageDialog show(final MainActivity mainActivity) {

            this.mainActivity = mainActivity;

            try {


                if (toast_message_dialog != null) {
                    toast_message_dialog.cancel();
                    toast_message_dialog.dismiss();
                    toast_message_dialog = null;
                }
            } catch (Exception e) {

            }

            toast_message_dialog = new MaterialDialog.Builder(mainActivity).
                    canceledOnTouchOutside(false).
                    cancelable(false).theme(Theme.DARK).
                    customView(R.layout.dialog_toast_message, true).
                    autoDismiss(true).build();

            dialoge_image = (CircleImageView) toast_message_dialog.getView().findViewById(R.id.dialoge_image);
            dialoge_title = (TextView) toast_message_dialog.getView().findViewById(R.id.dialoge_title);
//            dialoge_title.setText(message);
//            setImageWithColor(imageId, imageColor);
            /**/
            toast_message_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_right_to_left;
            toast_message_dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.trans_to_dark_selector));
            toast_message_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    /**/
                    Vibrator v = (Vibrator) mainActivity.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(200);
                    /**/

                    DrawableToolsV2.loadDrawable(dialoge_image, imageUrl);
//                    Glide.with(mainActivity).load(imageUrl).fitCenter().into(dialoge_image);
                    dialoge_title.setText(messageText);
                    toast_message_dialog.setOnDismissListener(onDismissListener);
                    dismissHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    }, 3000);


                }
            });

            toast_message_dialog.show();

            return this;
        }


        public ToastMessageDialog setDialogeTitle(String messageText) {
            this.messageText = messageText;
//            dialoge_title.setText(message);
            return this;
        }


        public ToastMessageDialog loadImageFromURL(String imageUrl) {
            this.imageUrl = imageUrl;
//            Glide.with(mainActivity).load(imageUrl).fitCenter().error(R.drawable.img_user).placeholder(R.drawable.img_user).into(dialoge_image);
            return this;
        }


        public ToastMessageDialog setOnDismiss(DialogInterface.OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }


        public void dismiss() {
            try {
                if (toast_message_dialog != null && toast_message_dialog.isShowing()) {
                    toast_message_dialog.dismiss();
                }
            } catch (Exception e) {

            }

        }

    }


    public static class ReasonDialog {

        /**/
        public static final String REASON_ABSENT = "absent";
        public static final String REASON_NO_SHOW = "no-show";
        public static final String REASON_CANCEL = "cancel";

        public boolean mustEnterEditText;
        public Button dialoge_button_yes, dialoge_button_other;
        public TextView dialoge_title, dialoge_message, dialoge_additional_message;
        public AppCompatImageView dialoge_image;
        private Activity context;
        public View dialoge_image_close;

        private LinearLayout linResult;
        private ArrayList<View> choisesList = new ArrayList<>();
//        private int selectedChoise = -1;

        public ReasonDialog show(Activity context) {

            this.context = context;
            try {


                if (reasonDialog != null) {
                    reasonDialog.cancel();
                    reasonDialog.dismiss();
                    reasonDialog = null;
                }
            } catch (Exception e) {

            }


            reasonDialog = new MaterialDialog.Builder(context).
                    canceledOnTouchOutside(false).
                    cancelable(false).theme(Theme.DARK).
                    customView(R.layout.dialog_reason, true).
                    autoDismiss(true).build();
            dialoge_button_other = reasonDialog.getView().findViewById(R.id.dialoge_button_other);
            dialoge_button_yes = reasonDialog.getView().findViewById(R.id.dialoge_button_yes);
            dialoge_image_close = reasonDialog.getView().findViewById(R.id.dialoge_image_close);
            linResult = (LinearLayout) reasonDialog.getView().findViewById(R.id.linResult);
            dialoge_title = (TextView) reasonDialog.getView().findViewById(R.id.dialoge_title);
            dialoge_message = (TextView) reasonDialog.getView().findViewById(R.id.dialoge_title);
            dialoge_additional_message = (TextView) reasonDialog.getView().findViewById(R.id.dialoge_additional_message);
            dialoge_image = (AppCompatImageView) reasonDialog.getView().findViewById(R.id.dialoge_image);
            /**/

            dialoge_image_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            /**/
//            reasonDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//            reasonDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
//            reasonDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.trans_to_dark_selector));

            reasonDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            reasonDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            reasonDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            reasonDialog.show();


            return this;
        }

        private String getSelectedRason() {
            boolean nothingChecked = true;
            for (int i = 0; i < choisesList.size(); i++) {
                RadioButton radioButton = (RadioButton) choisesList.get(i).findViewById(R.id.rbtext);
                EditText busNumber = (EditText) choisesList.get(i).findViewById(R.id.numtext);
                if (radioButton.isChecked()) {
                    nothingChecked = true;
                    if (busNumber.getVisibility() == View.VISIBLE && busNumber.getText().toString().trim().equals("")) {
                        UtilViews.shakeViews(busNumber);
                        return null;
                    } else {
                        return radioButton.getText().toString() + busNumber.getText().toString();

                    }

                }
            }
            return null;
        }

        public ReasonDialog setAfterResonSelectedLisiner(final OnActionDoneListener<String> after_reson_selected_lisiner, final OnActionDoneListener<String> after_reson_selected_lisiner_other) {
            dialoge_button_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedReason = getSelectedRason();
                    if (selectedReason != null) {
                        after_reson_selected_lisiner.OnActionDone(selectedReason);
                        dismiss();
                    }


                }
            });

            dialoge_button_other.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedReason = getSelectedRason();
                    if (selectedReason != null) {
                        after_reson_selected_lisiner_other.OnActionDone(selectedReason);
                        dismiss();
                    }

                    dismiss();


                }
            });

            return this;
        }


        public ReasonDialog showOtherButton() {
            dialoge_button_other.setVisibility(View.VISIBLE);
            return this;
        }

        public ReasonDialog setOtherButtonText(String otherButtonText) {
            dialoge_button_other.setText(otherButtonText);
            return this;
        }

        public ReasonDialog setYesButtonText(String otherButtonText) {
            dialoge_button_yes.setText(otherButtonText);
            return this;
        }

        public ReasonDialog setOtherButtonText(int otherButtonText) {
            dialoge_button_other.setText(otherButtonText);
            return this;
        }

        public ReasonDialog setYesButtonText(int otherButtonText) {
            dialoge_button_yes.setText(otherButtonText);
            return this;
        }

        public ReasonDialog hideYesButton() {
            dialoge_button_yes.setVisibility(View.GONE);
            return this;
        }

        public ReasonDialog initReasonsViews(String reasons_list_from) {
            List<String> list = UtilityDriver.getArrayMessage(reasons_list_from);
            createRadioButton(list);
            return this;
        }


        public ReasonDialog setDialogeTitle(String messageId) {
            dialoge_title.setText(messageId);
            return this;
        }

        public ReasonDialog setDialogeTitleTextColor(int textColor) {
            dialoge_title.setTextColor(ContextCompat.getColor(context, textColor));
            return this;
        }

        public ReasonDialog setDialogeTitle(int messageId) {
            dialoge_title.setText(messageId);
            return this;
        }

        public ReasonDialog setDialogeMessage(String message) {
            dialoge_message.setVisibility(View.VISIBLE);
            dialoge_message.setText(message);
            return this;
        }

        public ReasonDialog setDialogeMessage(int messageid) {
            dialoge_message.setVisibility(View.VISIBLE);
            dialoge_message.setText(messageid);
            return this;
        }


        public ReasonDialog setDialogeAdditionalMessage(String message) {
            dialoge_additional_message.setVisibility(View.VISIBLE);
            dialoge_additional_message.setText(message);
            return this;
        }

        public ReasonDialog setDialogeAdditionalMessage(int messageid) {
            dialoge_additional_message.setVisibility(View.VISIBLE);
            dialoge_additional_message.setText(messageid);
            return this;
        }

        public ReasonDialog setImageWithColor(int imageId, int color) {
            if (color == 0) {
                dialoge_image.setImageDrawable(ContextCompat.getDrawable(context, imageId));
            } else {
                dialoge_image.setImageDrawable(UtilityDriver.getTintDrawable(context, imageId, ContextCompat.getColor(context, color)));
            }

            return this;
        }

        public ReasonDialog createRadioButton(List<String> list) {
//            editText = new EditText(context);
//            editText.setTypeface(ResourcesCompat.getFont(context, R.font.custom_font));
//            editText.setMinimumWidth(context.getResources().getDimensionPixelSize(R.dimen.edit_text_radio_width));
//            editText.setGravity(R.integer.gravity_left_center_vertical);
//            final View[] rb = new View[list.size()];
            for (int i = 0; i < list.size(); i++) {
                View inflate = context.getLayoutInflater().inflate(R.layout.dialog_radio_button_with_edittext, linResult, false);
//                View inflate =  context.getLayoutInflater().inflate(R.layout.dialog_radio_button, linearLayout);
                RadioButton rb = (RadioButton) inflate.findViewById(R.id.rbtext);
                EditText bn = ((EditText) inflate.findViewById(R.id.numtext));
                inflate.setTag(i + "");
//                rb[i] = (AppCompatRadioButton) context.getLayoutInflater().inflate(R.layout.dialog_radio_button, linearLayout);

//                rb[i] = new RadioButton(context,null,R.style.RadioButtonStyle);
                if (list.get(i) != null)
                    rb.setText(list.get(i).replaceAll(DriverConstants.BUS_NUMBER_API_FORMAT, ""));
//                rb[i].setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
//                rb[i].setId(i + 100);
//                rb[i].setTypeface(ResourcesCompat.getFont(context, R.font.custom_font));
//                rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                    }
//                });
                rb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int j = 0; j < choisesList.size(); j++) {
                            ((RadioButton) choisesList.get(j).findViewById(R.id.rbtext)).setChecked(false);
                        }
                        ((RadioButton) view.findViewById(R.id.rbtext)).setChecked(true);
                        dialoge_button_yes.setEnabled(true);
                        dialoge_button_other.setEnabled(true);
//                        try {
////                            selectedChoise = (int) view.getTag();
//                        } catch (Exception e) {
//
//                        }

//                        selectedReason = ((RadioButton) view.findViewById(R.id.rbtext)).toString() + ((EditText) view.findViewById(R.id.numtext)).getText().toString();
//                        if ((view.getTag()) != null && (view.getTag()).equals("EditText")) {
//                            mustEnterEditText = true;
//                        } else {
//                            mustEnterEditText = false;
//                        }
//                        dialoge_button_yes.setEnabled(true);
//                        if (dialoge_button_other.getVisibility() == View.VISIBLE)
//                            dialoge_button_other.setEnabled(true);

                    }
                });

                if (list.get(i) != null && list.get(i).contains(DriverConstants.BUS_NUMBER_API_FORMAT)) {
                    bn.setVisibility(View.VISIBLE);
//                    LinearLayout linearLayout1 = new LinearLayout(context);
//                    linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
//                    linearLayout1.setGravity(R.integer.gravity_left_center_vertical);
//                    linearLayout1.setHorizontalGravity(R.integer.gravity_left_center_vertical);

//                    if (UtilityDriver.getStringShared(UtilityDriver.LANGUAGE).equals("ar")){
//                        ViewCompat.setLayoutDirection(linearLayout1, ViewCompat.LAYOUT_DIRECTION_RTL);
//
//                        linearLayout1.addView(editText);
//                        linearLayout1.addView(rb[i]);
//                    }
//                    else{
//                    rb[i].setTag("EditText");
//                    linearLayout1.addView(rb[i]);
//                    linearLayout1.addView(editText);
//                    }
//                    linearLayout.addView(linearLayout1);
                } else {
                    bn.setVisibility(View.GONE);
                }
                choisesList.add(inflate);


            }
            for (int i = 0; i < choisesList.size(); i++) {
                linResult.addView(choisesList.get(i));
            }

//            rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                    RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
//                    boolean isChecked = checkedRadioButton.isChecked();
//                    /**/
//                    for (int j = 0; j < rb.length; j++) {
//                        if (rb[j].getId()==checkedId);
//                        rb[j].setChecked(false);
//                    }
//
//
//
//                    if (isChecked) {
//                        selectedReason = checkedRadioButton.getText().toString() + editText.getText().toString();
//                        dialoge_button_yes.setEnabled(true);
//                    }
//                }
//            });
//            linearLayout.addView(rGroup);//you add the whole RadioGroup to the layout
            return this;
        }

        public void dismiss() {
            try {
                if (reasonDialog != null && reasonDialog.isShowing()) {
                    reasonDialog.dismiss();
                }
            } catch (Exception e) {

            }

        }
    }


    public static class StudentToolDialog {

        public Activity context;
        public View imgCall, imgChangeLocation, send_arrive_alarm, tools_bg_d;
        public StudentBean studentBean;
        private int type = 1;

        public StudentToolDialog show(Activity context, int type) {
            this.context = context;
            this.type = type;
            try {
                if (studentToolDialog != null) {
                    studentToolDialog.cancel();
                    studentToolDialog.dismiss();
                    studentToolDialog = null;
                }
            } catch (Exception e) {

            }


            studentToolDialog = new MaterialDialog.Builder(context).
                    canceledOnTouchOutside(false).
                    cancelable(false).theme(Theme.DARK).
                    customView(type == 1 ? R.layout.dialog_student_tool_pick : R.layout.dialog_student_tool, true).
                    autoDismiss(true).build();


            studentToolDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            studentToolDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            studentToolDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            studentToolDialog.show();

            studentToolDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    studentToolDialog.getView().findViewById(R.id.dialoge_image_close).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismiss();
                        }
                    });
                }
            });

            studentToolDialog.getView().findViewById(R.id.dialoge_image_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (studentToolDialog != null)
                        studentToolDialog.dismiss();
                }
            });
            imgCall = studentToolDialog.getView().findViewById(R.id.imgCall);
            imgChangeLocation = studentToolDialog.getView().findViewById(R.id.imgChangeLocation);
            send_arrive_alarm = studentToolDialog.getView().findViewById(R.id.send_arrive_alarm);
            tools_bg_d = studentToolDialog.getView().findViewById(R.id.tools_bg_d);
            return this;
        }


        public void dismiss() {
            try {
                if (studentToolDialog != null && studentToolDialog.isShowing()) {
                    studentToolDialog.dismiss();
                }
            } catch (Exception e) {

            }

        }

        public void changeLocationEnabled(boolean enabled) {
            imgChangeLocation.setVisibility(enabled ? View.VISIBLE : View.GONE);
        }

        public void callEnabled(StudentBean studentBean) {
            try {
                imgCall.setEnabled(studentBean.getMobileStudentBean().hasFatherOrMotherNumber());
            } catch (Exception e) {
                imgCall.setEnabled(false);
            }
        }

        public void changeColor(@ColorRes int colorId) {
            try {
                tools_bg_d.setBackgroundResource(colorId);
            } catch (Exception e) {
                imgCall.setEnabled(false);
            }
        }

    }


}
