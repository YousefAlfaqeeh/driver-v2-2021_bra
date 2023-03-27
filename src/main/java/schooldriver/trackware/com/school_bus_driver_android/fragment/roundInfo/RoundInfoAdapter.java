package schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.app.Application;
import schooldriver.trackware.com.school_bus_driver_android.bean.ParentStudentBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.RoundBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.enums.CheckEnum;
import schooldriver.trackware.com.school_bus_driver_android.enums.StatusRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.enums.TypeRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.dialogRoundInfo.ConfirmMessageDialog;
import schooldriver.trackware.com.school_bus_driver_android.gcmNotification.senderNotification.SendNotificationGCM;
import schooldriver.trackware.com.school_bus_driver_android.geofence.mock.Constants;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.IActionDialog;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.DriverConstants;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**

 */
  
public class RoundInfoAdapter extends RecyclerView.Adapter<RoundInfoHolder> implements IActionDialog {
    private final RoundBean roundBean;
    
    public static List<StudentBean> listStudentBean;
    static Activity mActivity;
    RoundInfoPresenter mPresenter;
    ConfirmMessageDialog confirmMessageDialog;
    private RoundInfoHolder holderCheck;
    private int positionCheck;
    private StudentBean studentBeanCheck;
    private CheckEnum checkEnumCheck;
    ImageLoader imageLoader;
    DisplayImageOptions defaultOptions;
    ImageLoaderConfiguration config;
    int routeOrderId;
    SendNotificationGCM sendNotificationGCM;

    public RoundInfoAdapter(List<StudentBean> listStudentBean, Activity mActivity, RoundInfoPresenter mPresenter, RoundBean roundBean) {
        this.listStudentBean = listStudentBean;
        this.mActivity = mActivity;
        this.mPresenter = mPresenter;
        this.roundBean = roundBean;
        if (StaticValue.G34) {
            imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
            defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true).cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .displayer(new FadeInBitmapDisplayer(300)).build();

            config = new ImageLoaderConfiguration.Builder(Application.getInstance().getApplicationContext())
                    .defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(new WeakMemoryCache())
                    .discCacheSize(100 * 1024 * 1024).build();
            imageLoader.init(config);
        }
        if (sendNotificationGCM == null) {
            sendNotificationGCM = new SendNotificationGCM();
        }
    }


    @Override
    public RoundInfoHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_student , parent, false);
        return new RoundInfoHolder (view);
    }

    public static void setEnableFragment(RoundInfoHolder  holder) {
        if (RoundInfoFragment.statusRoundEnum == StatusRoundEnum.START) {
            holder.imgShow.setEnabled(false);
            holder.imgCheck.setEnabled(false);
            holder.imgCall.setEnabled(false);
        } else if (RoundInfoFragment.statusRoundEnum == StatusRoundEnum.END) {
            holder.imgShow.setEnabled(true);
            holder.imgCheck.setEnabled(true);
            holder.imgCall.setEnabled(true);
        } else if (RoundInfoFragment.statusRoundEnum == StatusRoundEnum.FULL) {
            holder.imgShow.setEnabled(false);
            holder.imgCheck.setEnabled(false);
            holder.imgCall.setEnabled(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final RoundInfoHolder  holder, final int position) {

        final StudentBean studentBean = listStudentBean.get(position);
        holder.labStudentName.setText(studentBean.getNameStudent());
        holder.labGrade.setText(studentBean.getGrade());
        /**/

//        holder.imgStudent.setImageDrawable(UtilityDriver.getDrawableImage(mActivity, studentBean.getId()));
//        holder.imgShow.setColorFilter(mActivity.getResources().getColor(R.color.white));

        if (StaticValue.G34) {
            imageLoader.displayImage(studentBean.getAvatar(), holder.imgStudent);
        }
        if (studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
            setEnableFragment(holder);
        }
        if (studentBean.getCheckEnum() == CheckEnum.EMPTY) {
            holder.imgCheck.setImageResource(R.drawable.img_check_in);
//            holder.imgCheck.setColorFilter(mActivity.getResources().getColor(R.color.white));
            holder.labDone.setVisibility(View.INVISIBLE);
            holder.imgDone.setVisibility(View.INVISIBLE);
//            holder.imgCheck.setBackgroundResource(R.drawable.button_pick_up);//.setColorFilter(mActivity.getResources().getColor(R.color.white));
//            holder.imgShow.setBackgroundResource(R.drawable.button_drop_off);//.setColorFilter(mActivity.getResources().getColor(R.color.color_enable));
        } else if (studentBean.getCheckEnum() == CheckEnum.CHECK_IN) {
//            holder.imgCheck.setImageResource(R.drawable.img_round_info_check_out_white);
            holder.imgShow.setEnabled(false);
//            holder.imgCheck.setBackgroundResource(R.drawable.button_pick_up_trans);//.setColorFilter(mActivity.getResources().getColor(R.color.white));
//            holder.imgShow.setBackgroundResource(R.drawable.button_drop_off_trans);//.setColorFilter(mActivity.getResources().getColor(R.color.color_enable));
            mPresenter.showCheck(holder, studentBean, CheckEnum.CHECK_IN);
            holder.imgCheck.setPadding(25, 25, 25, 25);
            holder.imgShow.setPadding(25, 25, 25, 25);
            holder.imgChangeLocation.setPadding(25, 25, 25, 25);
            holder.imgCall.setPadding(25, 25, 25, 25);
            holder.imgCheck.setBackgroundResource(R.drawable.button_pick_up);
//            holder.imgCheck.setColorFilter(mActivity.getResources().getColor(R.color.white));

        } else if (studentBean.getCheckEnum() == CheckEnum.CHECK_OUT) {
//            holder.imgCheck.setImageResource(R.drawable.img_round_info_check_out_white);
//            holder.imgCheck.setColorFilter(mActivity.getResources().getColor(R.color.white));
            holder.imgShow.setEnabled(false);
            mPresenter.showCheck(holder, studentBean, CheckEnum.CHECK_OUT);
            holder.labDone.setVisibility(View.VISIBLE);
            holder.imgDone.setVisibility(View.VISIBLE);
            holder.labDone.setText(mActivity.getString(R.string.done));

            holder.imgCheck.setEnabled(false);
//            holder.imgCheck.setColorFilter(mActivity.getResources().getColor(R.color.color_enable));
            holder.imgShow.setEnabled(false);
//            holder.imgShow.setColorFilter(mActivity.getResources().getColor(R.color.color_enable));
            holder.imgCheck.setBackgroundResource(R.drawable.button_pick_up);
        }

        if (studentBean.isAbsent() || studentBean.isNoShow()) {
            if (studentBean.getTypeRoundEnum() == TypeRoundEnum.DROP_ROUND) {
                if (RoundInfoFragment.roundBean.isRoundStartedNow()) {
                    holder.imgShow.setEnabled(false);
                } else {
                    RoundInfoFragment.labCheckIn.setText(R.string.check_out);
                    holder.imgShow.setEnabled(true);
                }
            } else {
                holder.imgShow.setEnabled(false);
            }
            holder.imgCheck.setEnabled(false);
//            holder.imgShow.setColorFilter(mActivity.getResources().getColor(R.color.color_enable));
//            holder.imgCheck.setColorFilter(mActivity.getResources().getColor(R.color.color_enable));
//            holder.imgCheck.setBackgroundResource(R.drawable.button_pick_up_trans);//.setColorFilter(mActivity.getResources().getColor(R.color.white));

            if (StaticValue.diagonalInches >= 6.5) {
                // 6.5inch device or bigger
                holder.imgCheck.setPadding(25, 25, 25, 25);
                holder.imgShow.setPadding(25, 25, 25, 25);
                holder.imgChangeLocation.setPadding(25, 25, 25, 25);
                holder.imgCall.setPadding(25, 25, 25, 25);
            } else {
                holder.imgCheck.setPadding(25, 25, 25, 25);
                holder.imgShow.setPadding(25, 25, 25, 25);
                holder.imgChangeLocation.setPadding(25, 25, 25, 25);
                holder.imgCall.setPadding(25, 25, 25, 25);
            }

//            holder.imgShow.setBackgroundResource(R.drawable.button_drop_off_trans);//.setColorFilter(mActivity.getResources().getColor(R.color.color_enable));
            mPresenter.showAbsence(holder, studentBean);
        } else {
            if (studentBean.getCheckEnum() == CheckEnum.EMPTY) {
                if (position <= listStudentBean.size() - 1) {
                    routeOrderId = (int) studentBean.getId();
                }
            }
        }
        if (StaticValue.diagonalInches >= 6.5) {
            // 6.5inch device or bigger
            holder.imgCheck.setPadding(25, 25, 25, 25);
            holder.imgShow.setPadding(25, 25, 25, 25);
            holder.imgChangeLocation.setPadding(25, 25, 25, 25);
            holder.imgCall.setPadding(25, 25, 25, 25);
        } else {
            holder.imgCheck.setPadding(25, 25, 25, 25);
            holder.imgShow.setPadding(25, 25, 25, 25);
            holder.imgChangeLocation.setPadding(25, 25, 25, 25);
            holder.imgCall.setPadding(25, 25, 25, 25);
        }
//        if (position >= 0 && position < 3) {
//            final int sdk = android.os.Build.VERSION.SDK_INT;
//            if (mPresenter.checkRound(studentBean)) {
//                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    holder.lliMain.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.liner_first_location));
//                } else {
//                    holder.lliMain.setBackground(mActivity.getResources().getDrawable(R.drawable.liner_first_location));
//                }
//            }
//        }


        holder.imgCall.setOnClickListener(new View.OnClickListener() {
              
            @Override
            public void onClick(View v) {

                showDialog(mActivity, studentBean.getMobileStudentBean());


            }
        });
        if (roundBean.isChangeStudentLocation()) {

            if (studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
                holder.imgChangeLocation.setVisibility(View.VISIBLE);
            } else {
                if (RoundInfoFragment.statusRoundEnum == StatusRoundEnum.END) {
                    holder.imgChangeLocation.setVisibility(View.VISIBLE);
                }
            }

            holder.imgChangeLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StaticValue.progressDialog != null) {
                        StaticValue.progressDialog.show();
                    }
                    ConfirmMessageDialog confirmMessageDialog = new ConfirmMessageDialog(mActivity);
                    confirmMessageDialog.changeLocationHome(mActivity, EnumNameApi.CHANGE_LOCATION, studentBean);
                }
            });
        }
        holder.imgCheck.setOnClickListener(new View.OnClickListener() {


              
            @Override
            public void onClick(View v) {
                if (StaticValue.progressDialog != null) {
                    StaticValue.progressDialog.show();
                }
                double distance = UtilityDriver.distance(StaticValue.latitudeMain, StaticValue.longitudeMain, studentBean.getLatitude(), studentBean.getLongitude(), "M");
                if (studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {


                    if (distance >= 100) {

                        UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.failed), mActivity.getString(R.string.allow_distance));
                        return;
                    }
                }
                /* ----- */

                RoundInfoFragment.COUNT = 0;
                for (StudentBean studentBean : listStudentBean) {
                    if (studentBean.getCheckEnum() == CheckEnum.CHECK_IN) {
                        RoundInfoFragment.COUNT++;
                    } else if (studentBean.getCheckEnum() == CheckEnum.CHECK_OUT) {
                        RoundInfoFragment.COUNT++;
//                        RoundInfoFragment.COUNT_DROP_OFF++;
                    } else if (studentBean.isAbsent() || studentBean.isNoShow()) {
                        RoundInfoFragment.COUNT++;
                    }
                }
                if (studentBean.getTypeRoundEnum() == TypeRoundEnum.DROP_ROUND) {
                    if (RoundInfoFragment.COUNT < listStudentBean.size()) {
                        if (studentBean.getCheckEnum() == CheckEnum.CHECK_IN) {
                            UtilityDriver.showMessageDialog(mActivity, "", mActivity.getString(R.string.please_finish_round));
                            return;
                        }
                    } else if (!RoundInfoFragment.roundBean.isRoundStartedNow()) {
                        UtilityDriver.showMessageDialog(mActivity, "", mActivity.getString(R.string.start_round));
                        return;
                    }
                }


                if (StaticValue.progressDialog != null) {
                    StaticValue.progressDialog.show();
                }

                holderCheck = holder;
                positionCheck = position;
                studentBeanCheck = studentBean;

                if (studentBean.getCheckEnum() == CheckEnum.EMPTY) {
                    if (routeOrderId != (int) studentBean.getId()) {
                        mPresenter.checkChangeRoute((Integer) roundBean.getId());
                    }

                    checkEnumCheck = CheckEnum.CHECK_IN;
                    confirmMessageDialog = new ConfirmMessageDialog(mActivity);
                    confirmMessageDialog.showMessageCheck(mActivity, RoundInfoAdapter.this, studentBean, "in", (Integer) roundBean.getId());
                } else if (studentBean.getCheckEnum() == CheckEnum.CHECK_IN) {
                    distance = UtilityDriver.distance(StaticValue.latitudeMain, StaticValue.longitudeMain, studentBean.getLatitude(), studentBean.getLongitude(), "M");
                    if (distance >= 100) {
                        UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.failed), mActivity.getString(R.string.allow_distance));
                        return;
                    }
                    checkEnumCheck = CheckEnum.CHECK_OUT;
                    confirmMessageDialog = new ConfirmMessageDialog(mActivity);
                    confirmMessageDialog.showMessageCheck(mActivity, RoundInfoAdapter.this, studentBean, "out", (Integer) roundBean.getId());
                }

            }
        });

        holder.imgShow.setOnClickListener(new View.OnClickListener() {

              
            @Override
            public void onClick(View v) {
                double distance = UtilityDriver.distance(StaticValue.latitudeMain, StaticValue.longitudeMain, studentBean.getLatitude(), studentBean.getLongitude(), "M");
                if (studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
//                    if (distance >= 100) {
//
//                        UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.failed), mActivity.getString(R.string.allow_distance));
//                        return;
//                    }
                }
                if (StaticValue.progressDialog != null) {
                    StaticValue.progressDialog.show();
                }
                holderCheck = holder;
                positionCheck = position;
                studentBeanCheck = studentBean;
                String status = "";
                if (studentBean.getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
                    status = "absent";
                } else {
                    status = "no-show";
                }
                confirmMessageDialog = new ConfirmMessageDialog(mActivity);
                confirmMessageDialog.showMessageAbsent(mActivity, RoundInfoAdapter.this, studentBean, status, (Integer) roundBean.getId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return listStudentBean.size();
    }


    public void showDialog(Activity activity, final ParentStudentBean mobileStudentBean) {
        final Dialog dialog = new Dialog(activity, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_call);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_dialog_message);
        LinearLayout lliFather = (LinearLayout) dialog.findViewById(R.id.lliFather);
        LinearLayout lliMother = (LinearLayout) dialog.findViewById(R.id.lliMother);
        TextView labFather = (TextView) dialog.findViewById(R.id.labFather);
        TextView labMother = (TextView) dialog.findViewById(R.id.labMother);

        if (!UtilityDriver.isEmptyString(mobileStudentBean.getFatherNumber()))
            labFather.append("\n" + mobileStudentBean.getFatherNumber() + " ");
        else
            labFather.setVisibility(View.GONE);

        if (!UtilityDriver.isEmptyString(mobileStudentBean.getMotherNumber()))
            labMother.append("\n" + mobileStudentBean.getMotherNumber() + " ");
        else
            lliMother.setVisibility(View.GONE);


        lliFather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + mobileStudentBean.getFatherNumber();
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                    mActivity.startActivity(intent);
                } catch (Exception e) {
                }
//
            }
        });
        lliMother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + mobileStudentBean.getMotherNumber();
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                    mActivity.startActivity(intent);
                } catch (Exception e) {
                }
//
            }
        });
        dialog.show();
    }

    @Override
    public void done(StatusRoundEnum statusRoundEnum) {

    }


    @Override
    public void done(String message) {

        if (UtilityDriver.isStringEqualsAnyOfOther(message ,DriverConstants.ABSENT_ALLDAY,DriverConstants.ABSENT_MORNING, DriverConstants.NO_SHOW_DROP,DriverConstants.NO_SHOW_PICK)) {
            StudentBean bean = mPresenter.showAbsenceUpdate(holderCheck, studentBeanCheck);
            listStudentBean.set(positionCheck, bean);
            List<StudentBean> newListStudentBean = mPresenter.swapListAll(listStudentBean);
            RoundInfoFragment.setListAdapters(newListStudentBean);
//            if (StaticValue.SOCKET_API) {
//                ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
//                node.put("bus_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.BUS_ID)));
//                node.put("round_id", (Integer) roundBean.getId());
//                node.put("student_id", studentBeanCheck.getId() + "");
//                node.put("lat", StaticValue.latitudeMain);
//                node.put("long", StaticValue.longitudeMain);
////                PhoenixPlug.getInstance().pushPhoenix("student_served", node);
//            }
        } else if (message.equals("in") || message.equals("out")) {
            StudentBean bean = null;
            if (message.equals("in")) {
                bean = mPresenter.showCheck(holderCheck, studentBeanCheck, checkEnumCheck);
                listStudentBean.set(positionCheck, bean);
                List<StudentBean> newListStudentBean = mPresenter.swapListAll(listStudentBean);
                RoundInfoFragment.setListAdapters(newListStudentBean);

            } else {
                bean = mPresenter.showCheck(holderCheck, studentBeanCheck, checkEnumCheck);
                listStudentBean.set(positionCheck, bean);
                List<StudentBean> newListStudentBean = mPresenter.swapListCheckout(listStudentBean);
                RoundInfoFragment.setListAdapters(newListStudentBean);
            }
            boolean valueCheckSendPhoenix = false;
            if (listStudentBean.get(0).getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
                if (message.equals("in")) {
                    valueCheckSendPhoenix = true;
                }
            } else {
                if (message.equals("out")) {
                    valueCheckSendPhoenix = true;
                }
            }
//            if (StaticValue.SOCKET_API) {
//                if (valueCheckSendPhoenix) {
////                if (MainActivity.phoenixPlug != null) {
//                    Thread thread = new Thread(new Runnable() {
//
//                        @Override
//                        public void run() {
//
//                            ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
//                            node.put("bus_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.BUS_ID)));
//                            node.put("round_id", (Integer) roundBean.getId());
//                            node.put("student_id", studentBeanCheck.getId() + "");
//                            node.put("lat", StaticValue.latitudeMain);
//                            node.put("long", StaticValue.longitudeMain);
////                            PhoenixPlug.getInstance().pushPhoenix("student_served", node);
//                        }
//                    });
//
//                    thread.start();
////                }
//                }
//            }

//
            try {

                if (bean.getMobileStudentBean().isCheckInFather()) {
                    String sendMessage = null;
                    if (message.equals("in")) {
                        sendMessage = UtilityDriver.getMessageNotification("in", bean.getTypeRoundEnum(),bean.getMobileStudentBean().getMotherLocal());
                    } else {
                        sendMessage = UtilityDriver.getMessageNotification("out", bean.getTypeRoundEnum(),bean.getMobileStudentBean().getFatherLocal());
                    }
                    sendMessage = sendMessage.replaceAll(STUDENT_NAME_API_FORMAT, bean.getFirstNameStudent());
                    Map<String, String> mapBodyMessage = new HashMap<>();
                    mapBodyMessage.put("message", sendMessage);
//                    mapBodyMessage.put("avatar", bean.getAvatar());
                    mapBodyMessage.put("title", SendNotificationGCM.createNotificationTitle(bean.getMobileStudentBean().getFatherLocal()));
                    mapBodyMessage.put("action", Constants.NOTIFICATION_ACTION_Other);


//                    String messageTypeFather = "";
//                    if (bean.getMobileStudentBean().getFatherPlatform().contains("android")) {
//                        messageTypeFather = new JSONObject(mapBodyMessage).toString();
//                    } else {
//                        messageTypeFather = UtilityDriver.MESSAGE_IPHONE.replaceAll("MESSAGEMESSAGE", sendMessage);
//                    }

                    sendNotificationGCM.sendNotification(false,bean.getMobileStudentBean().getFatherToken(), bean.getMobileStudentBean().getFatherMessageType(mapBodyMessage,sendMessage), (Integer) studentBeanCheck.getId(), (Integer) roundBean.getId(), bean.getMobileStudentBean().getFatherPlatform(), bean.getAvatar(),bean.getMobileStudentBean().getFatherLocal(),bean.getNameStudent(),bean.getMobileStudentBean().getFatherId());
                }
                if (bean.getMobileStudentBean().isCheckInMother()) {
                    String sendMessage = null;
                    if (message.equals("in")) {
                        sendMessage = UtilityDriver.getMessageNotification("in", bean.getTypeRoundEnum(),bean.getMobileStudentBean().getMotherLocal());
                    } else {
                        sendMessage = UtilityDriver.getMessageNotification("out", bean.getTypeRoundEnum(),bean.getMobileStudentBean().getFatherLocal());
                    }
                    sendMessage = sendMessage.replaceAll(STUDENT_NAME_API_FORMAT, bean.getFirstNameStudent());
                    Map<String, String> mapBodyMessage = new HashMap<>();
                    mapBodyMessage.put("message", sendMessage);
//                    mapBodyMessage.put("avatar", bean.getAvatar());
                    mapBodyMessage.put("title", SendNotificationGCM.createNotificationTitle(bean.getMobileStudentBean().getMotherLocal()));
                    mapBodyMessage.put("action", Constants.NOTIFICATION_ACTION_Other);

//                    String messageTypeMother = "";
//                    if (bean.getMobileStudentBean().getMotherPlatform().contains("android")) {
//                        messageTypeMother = new JSONObject(mapBodyMessage).toString();
//                    } else {
//                        messageTypeMother = UtilityDriver.MESSAGE_IPHONE.replaceAll("MESSAGEMESSAGE", sendMessage);
//                    }
                    sendNotificationGCM.sendNotification(false,bean.getMobileStudentBean().getMotherToken(), bean.getMobileStudentBean().getMotherMessageType(mapBodyMessage,sendMessage), (Integer) studentBeanCheck.getId(), (Integer) roundBean.getId(), bean.getMobileStudentBean().getMotherPlatform(), bean.getAvatar(), bean.getMobileStudentBean().getMotherLocal(),bean.getNameStudent(),bean.getMobileStudentBean().getMotherId());
                }
            } catch (NullPointerException e) {

            }
        }
        if (StaticValue.progressDialog != null) {
            StaticValue.progressDialog.dismiss();
        }
    }

    @Override
    public void cancel() {

    }

    public List<StudentBean> getListStudentBean() {
        return listStudentBean;
    }
}
