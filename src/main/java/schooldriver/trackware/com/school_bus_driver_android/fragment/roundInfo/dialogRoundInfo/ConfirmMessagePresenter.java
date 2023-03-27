package schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.dialogRoundInfo;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import database.CheckInOut;
import database.DAO;
import schooldriver.trackware.com.school_bus_driver_android.API.ApiRequest;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumMethodApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumTypeHeader;
import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallBack;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.app.Application;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BasePresenter;
import schooldriver.trackware.com.school_bus_driver_android.enums.StatusRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.RoundInfoFragment;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.IActionDialog;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.DateTools;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.PathUrl;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created by   3/27/17.
 */

public class ConfirmMessagePresenter extends BasePresenter implements IRestCallBack {


    private  IActionDialog statusDialog;
    private StatusRoundEnum statusRoundEnum;
    private String status;

    public ConfirmMessagePresenter(Activity mActivity, IActionDialog statusDialog) {
        this.mActivity = mActivity;
        this.statusDialog = statusDialog;
    }
    public ConfirmMessagePresenter(Activity mActivity) {
        this.mActivity = mActivity;
    }
      
    public void callApiStatusRound(final StatusRoundEnum statusRoundEnum, final int roundID) {
        final int distance = (int) UtilityDriver.distance(StaticValue.latitudeMain,StaticValue.longitudeMain,StaticValue.latitudeStartRound,StaticValue.longitudeStartRound,"M");

        HashMap map =  new HashMap() {{
            put("round_id", Integer.parseInt(String.valueOf(roundID)));
            put("datetime", DateTools.Formats.DATE_FORMAT_GMT.format(new Date()));
            put("status", (statusRoundEnum == StatusRoundEnum.START) ? "start" : "end");
            put("lat",  StaticValue.latitudeMain+"");
            put("long",  StaticValue.longitudeMain+"");
            put("distance", distance);
        }};
        if (statusRoundEnum == StatusRoundEnum.END) {
            if (!UtilityDriver.isNetworkAvailable(StaticValue.mActivity)) {
                CheckInOut checkInOut = new CheckInOut("", "", new JSONObject(map).toString());
                DAO.addCheck(Application.database, checkInOut);
                statusDialog.done(statusRoundEnum);
                return;
            }
        }
        if (statusRoundEnum == StatusRoundEnum.START){
            StaticValue.latitudeDistance = StaticValue.latitudeMain+0;
            StaticValue.longitudeDistance = StaticValue.longitudeMain+0;
        }else{
            StaticValue.latitudeDistance = 0;
            StaticValue.longitudeDistance = 0;
        }
        this.statusRoundEnum = statusRoundEnum;

        callRestAPI(PathUrl.MAIN_URL + PathUrl.SET_ROUND_STATUS
                ,
               map
                ,
                EnumMethodApi.POST
                ,
                ConfirmMessagePresenter.this
                ,
                EnumNameApi.SET_ROUND_STATUS
                ,
                UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
                ,
                EnumTypeHeader.JSON
        );
    }

//    public void callApiCheckOutAll(final int roundId) {
//        callRestAPI(PathUrl.MAIN_URL + PathUrl.CHECK_OUT_ALL
//                ,
//                new HashMap<String, String>() {{
//                    put("bus_id", UtilityDriver.getStringShared(UtilityDriver.SCHOOL_ID));
//                    put("round_id", String.valueOf(roundId));
//                    put("datetime", DateTools.Formats.DATE_FORMAT_GMT.format(new Date()));
//                    put("lat", StaticValue.latitudeMain + "");
//                    put("long", StaticValue.longitudeMain + "");
//                }}
//                ,
//                EnumMethodApi.POST
//                ,
//                ConfirmMessagePresenter.this
//                ,
//                EnumNameApi.CHECK_OUT_ALL
//                ,
//                UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
//                ,
//                EnumTypeHeader.JSON
//        );
//    }


    public void callApiSetCheckShow(final int studentID, final String status, final int roundId, final boolean overwrite, final String reason) {
        this.status = status;

        HashMap map =    new HashMap() {{
//                    put("bus_id", UtilityDriver.getStringShared(UtilityDriver.SCHOOL_ID));
//                    put("round_id", String.valueOf(roundBean.getId()));
//                    put("datetime", UtilityDriver.getDateFormat("mm/dd/yyyy hh:mm:ss"));
            put("lat", StaticValue.latitudeMain );
            put("long", StaticValue.longitudeMain);
            put("student_id", studentID);
            put("round_id", roundId);
            put("status", status);
            put("datetime", DateTools.Formats.DATE_FORMAT_GMT.format(new Date()));
            put("overwrite", overwrite);
            put("reason", reason);
        }};
//        if (!UtilityDriver.isNetworkAvailable(StaticValue.mActivity)) {
            CheckInOut checkInOut = new CheckInOut("", "", new JSONObject(map).toString());
            DAO.addCheck(Application.database, checkInOut);
            statusDialog.done(status);
//            return;
//        }
//        callRestAPI(PathUrl.MAIN_URL + PathUrl.STUDENT_BUS_CHECK
//                ,
//             map
//                ,
//                EnumMethodApi.POST
//                ,
//                ConfirmMessagePresenter.this
//                ,
//                EnumNameApi.STUDENT_BUS_CHECK
//                ,
//                UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
//                ,
//                EnumTypeHeader.JSON
//        );
    }

    @Override
    public void onRestCallBack(String response, EnumNameApi nameApiEnum,ApiRequest volleyBean) {

    }

    @Override
    public void onRestCallBack(JSONObject response, EnumNameApi nameApiEnum,ApiRequest volleyBean) {
        try {
            String outPut = null;
            if (EnumNameApi.SET_ROUND_STATUS == nameApiEnum) {
                 outPut = response.getString("status");
                if (outPut.toLowerCase().equals("ok")) {
                    statusDialog.done(statusRoundEnum);

                } else {
                    UtilityDriver.showMessageDialog(mActivity,mActivity.getString(R.string.failed),outPut);
                }

            } else if (nameApiEnum == EnumNameApi.CHECK_OUT_ALL) {

                 outPut = response.getString("status");
                if (outPut.toLowerCase().equals("ok")) {
                    statusDialog.done(mActivity.getString(R.string.check_out_all));
                } else {
                    UtilityDriver.showMessageDialog(mActivity,mActivity.getString(R.string.failed),outPut);
                }

            }
//            else if (nameApiEnum == EnumNameApi.STUDENT_BUS_CHECK) {
//                outPut = response.getString("status");
//                if (outPut.toLowerCase().equals("ok")) {
//                    statusDialog.done(status);
//                } else {
//                    UtilityDriver.showMessageDialog(mActivity,mActivity.getString(R.string.failed),outPut);
//                }
//
//            }
        } catch (JSONException | NullPointerException e) {
            UtilityDriver.showMessageDialog(mActivity,mActivity.getString(R.string.failed),e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onRestCallBack(VolleyError volleyError, EnumNameApi nameApiEnum,ApiRequest volleyBean) {  if (volleyError.getMessage().toString().contains("java.net.UnknownHost")){                 UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.internet_connection), mActivity.getString(R.string.missing_internet_error));             return;         }
        UtilityDriver.showMessageDialog(mActivity,mActivity.getString(R.string.failed),volleyError.getMessage());
    }

    @Override
    public void onRestCallBack(NetworkResponse networkResponse, EnumNameApi nameApiEnum,ApiRequest volleyBean) {

    }


    public void callApiCancelRound(final int roundID, RoundInfoFragment roundInfoFragment, final String reason) {



        callRestAPI(PathUrl.MAIN_URL + PathUrl.SET_ROUND_STATUS
                ,
                new HashMap() {{
                    put("round_id", roundID);
                    put("datetime", DateTools.Formats.DATE_FORMAT_GMT.format(new Date()));
                    put("status", "cancel");
                    put("lat",  StaticValue.latitudeMain+"");
                    put("long",  StaticValue.longitudeMain+"");
                    put("distance", 0);
                    put("reason", reason);
                }}
                ,
                EnumMethodApi.POST
                ,
                roundInfoFragment
                ,
                EnumNameApi.CANCEL_ROUND
                ,
                UtilityDriver.typeHeaderMap(EnumTypeHeader.JSON, true)
                ,
                EnumTypeHeader.JSON
        );

    }
}
