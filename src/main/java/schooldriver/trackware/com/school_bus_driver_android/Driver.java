package schooldriver.trackware.com.school_bus_driver_android;

import android.widget.TextView;

import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created by M.Bader on 6/14/2017.
 */

public class Driver extends User {
//    int sentCount= 0;
    public Driver() {
        super("bus",
                UtilityDriver.getStringShared(UtilityDriver.AUTH),
                new TextView(StaticValue.mActivity),
                new TextView(StaticValue.mActivity),
                new TextView(StaticValue.mActivity)
                );
    }

//    @Override
//    public void refresh(int busId)  {
//
//        try {
//
//            super.refresh(busId);
//            if (phoenixPlug.socket!= null )
//            phoenixPlug.joinBus(this, busId);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void onReadyToPushMessage() {
//
//        try {
//
//
//            if (StaticValue.latitudeMain > 0 || StaticValue.longitudeMain > 0){
//                ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
//                node.put("lat", StaticValue.latitudeMain);
//                node.put("long", StaticValue.longitudeMain);
//                node.put("school_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.SCHOOL_ID)));
//                node.put("round_id", RoundInfoFragment.ROUND_ID_SOCKET);
//                node.put("bus_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.BUS_ID)));
//                node.put("at", DateTools.Formats.DATE_FORMAT_GMT.format(new Date()));
//
//                phoenixPlug.channel.push("new:bus_location", node).receive("ok", new IMessageCallback() {
//                    @Override
//                    public void onMessage(Envelope envelope) throws Exception {
//                        sentCount++;
//                    }
//                });
//
//            }else{
////                ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
////                node.put("lat", "31.947054");
////                node.put("long", "35.928953");
////                node.put("school_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.SCHOOL_ID)));
////                node.put("round_id", RoundInfoFragment.ROUND_ID_SOCKET);
////                node.put("bus_id", Integer.parseInt(UtilityDriver.getStringShared(UtilityDriver.BUS_ID)));
////                node.put("at", UtilityDriver.getDateFormat("mm/DD/yyyy hh:mm:ss"));
////
////                phoenixPlug.channel.push("new:bus_location", node).receive("ok", new IMessageCallback() {
////                    @Override
////                    public void onMessage(Envelope envelope) throws Exception {
////                        sentCount++;
////                    }
////                });
//            }
//
////            if (MainActivity.lab_driverSentCount!=null){
////                new Handler(Looper.getMainLooper()).post(new Runnable() {
////                    @Override
////                    public void run() {
////                        MainActivity.lab_driverSentCount.setText(sentCount+"");
////
////                    }
////                });
////            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
