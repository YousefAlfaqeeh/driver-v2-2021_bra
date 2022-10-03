package schooldriver.trackware.com.school_bus_driver_android.fragment.changeLocation;

/**
 * Created by mohbader on 3/8/2017.
 */

import android.app.Activity;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BasePresenter;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.RoundInfoFragment;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.OnActionDoneListener;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.PathUrl;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilDialogs;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;


public class ChangeLocationPresenter extends BasePresenter implements IRestCallBack {
    private final String roundType;
    Activity mActivity;
    int studentId;
    private double lat, lng;


    public ChangeLocationPresenter(Activity mActivity, String roundType) {
        this.mActivity = mActivity;
        this.roundType = roundType;
    }

    public void callChangeLocation(final String roundType, final double lat, final double lng, final int studentId) {
        this.studentId = studentId;
        this.lat = lat;
        this.lng = lng;
        callRestAPI(PathUrl.MAIN_URL + PathUrl.NOTIFY,
                new HashMap() {{
                    put("name", "changed_location");
                    put("location_type", roundType);
                    put("long", lng);
                    put("lat", lat);
                    put("mobile", "");
                    put("student_id", studentId);
                }},
                EnumMethodApi.POST,
                ChangeLocationPresenter.this,
                EnumNameApi.CHANGE_LOCATION,
                UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true),
                EnumTypeHeader.JSON

        );
    }


    @Override
    public void onRestCallBack(String response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

    }

    @Override
    public void onRestCallBack(JSONObject response, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
        if (nameApiEnum == EnumNameApi.CHANGE_LOCATION) {

            try {
                if (response.getString("status").equals("ok")) {
                    List<StudentBean> listStudentBean = new ArrayList<>();
                    for (StudentBean studentBean : RoundInfoFragment.roundBean.getListStudentBean()) {
                        if (studentId == (int) studentBean.getId()) {
                            studentBean.setLatitude(lat);
                            studentBean.setLongitude(lng);
                        }
                        listStudentBean.add(studentBean);
                    }
                    RoundInfoFragment.roundBean.setListStudentBean(listStudentBean);


                    String dialogeText = "";
                    if (roundType.equalsIgnoreCase("both")) {
                        dialogeText = mActivity.getString(R.string.change_Location_both);
                    } else if (roundType.equalsIgnoreCase("pick-up")) {
                        dialogeText = mActivity.getString(R.string.change_Location_pick);
                    } else if (roundType.equalsIgnoreCase("drop-off")) {
                        dialogeText = mActivity.getString(R.string.change_Location_drop);
                    }

                    new UtilDialogs.MessageYesNoDialog().show(mActivity)
                            .setYesButtonText(R.string.ok)
                            .hideCloseButton()
                            .setDialogeMessage(dialogeText)
                            .setDialogeTitle(R.string.done).setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                        @Override
                        public void OnActionDone(UtilDialogs.MessageYesNoDialog dialoge) {
                            if (dialoge != null)
                            dialoge.dismiss();
                            mActivity.onBackPressed();
                        }
                    });

//                    new SendDialog(mActivity, mActivity.getString(R.string.changed_location_successfully) + " " + roundType, EnumNameApi.CHANGE_LOCATION).show();
//                    UtilityDriver.showMessageDialog(mActivity,mActivity.getString(R.string.success),mActivity.getString(R.string.change_location));
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(UtilityDriver.ROUND, RoundInfoFragment.roundBean);
//                    MainActivity.showFragmentRoundInfo(bundle);
                } else {

                    new UtilDialogs.MessageYesNoDialog().show(mActivity)
                            .setYesButtonText(R.string.ok)
                            .hideCloseButton()
                            .setDialogeMessage(mActivity.getString(R.string.changed_location_failed))
                            .setDialogeTitle(R.string.done).setYesButtonClickListener(new OnActionDoneListener<UtilDialogs.MessageYesNoDialog>() {
                        @Override
                        public void OnActionDone(UtilDialogs.MessageYesNoDialog dialoge) {
                            if (dialoge !=null)
                            dialoge.dismiss();
                            mActivity.onBackPressed();
                        }
                    });


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRestCallBack(VolleyError volleyError, EnumNameApi nameApiEnum, ApiRequest volleyBean) {
        if (nameApiEnum == EnumNameApi.CHANGE_LOCATION) {
            UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.error_api), volleyError.getMessage());
        }
    }

    @Override
    public void onRestCallBack(NetworkResponse networkResponse, EnumNameApi nameApiEnum, ApiRequest volleyBean) {

    }
}