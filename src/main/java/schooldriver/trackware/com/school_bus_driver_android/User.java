package schooldriver.trackware.com.school_bus_driver_android;

import android.graphics.Color;
import android.widget.TextView;

import java.util.concurrent.Callable;

//import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallPhoenix;

/**
 * Created by samir on 6/14/17.
 */

class User   {

//    PhoenixPlug phoenixPlug;
//    private TextView socket_state_lamp;
//    private TextView channel_state_lamp;
//    private TextView channel_state;
//    public static int id= (int) System.currentTimeMillis();

    User(String client_type, String token, TextView socket_state_lamp, TextView channel_state_lamp, TextView channel_state){

//        this.socket_state_lamp= socket_state_lamp;
//        this.channel_state_lamp= channel_state_lamp;
//
//        this.channel_state= channel_state;

//        phoenixPlug=new PhoenixPlug(client_type, token);
//
//        if (phoenixPlug.socket== null) {
//
//            phoenixPlug.initializeSocket(new Callable<Void>() {
//
//                public Void call() throws Exception {
//                    // socket is opened now:
//
//                    try {
//                        socketConnected();
//                    } catch (Exception e) {
//
//                    }
//                    return null;
//                }
//            }, this);
//
//        }
    }



//    @Override
//    public void sendSocketMessage(String response, int usedId) {
//
//    }
//
//    @Override
//    public void receivedSocketMessage(String response, int usedId) {
//
//    }
//
//    @Override
//    public void newLocation(double lat, double lng, int usedId) {
//
//    }
//
//    @Override
//    public void errorConnect(String response) {
////        if (socket_state_lamp!=null){
////            new Handler(Looper.getMainLooper()).post(new Runnable() {
////                @Override
////                public void run() {
////                    if (socket_state_lamp!=null){
////                        socket_state_lamp.setTextColor(Color.RED);
////                    }
////
////                }
////            });
////        }
//    }

//    @Override
//    public void ignoreConnect(String response) {
//
//    }
//
//    @Override
//    public void closeConnect(String response) {
//
//    }

//    public void refresh(int busId){
////        if (phoenixPlug.socket!= null)
////        {
////
////            new Handler(Looper.getMainLooper()).post(new Runnable() {
////                @Override
////                public void run() {
////                    if (socket_state_lamp!=null){
////
////                        socket_state_lamp.setTextColor(phoenixPlug.socket.isConnected() ? Color.GREEN : Color.RED);
////                    }
////
////                }
////            });
//
//            setChannelState();
////        }
//    }
//
//    public void socketConnected() {
////        if (socket_state_lamp!=null){
////            new Handler(Looper.getMainLooper()).post(new Runnable() {
////                @Override
////                public void run() {
////                    if (socket_state_lamp!=null){
////                        socket_state_lamp.setTextColor(Color.GREEN);
////                    }
////
////                }
////            });
////        }
//    }
//
//    @Override
//    public void onReadyToPushMessage() {
//
//    }
//
//
//
//    void setChannelState() {
//
////        if (channel_state!=null){
////            new Handler(Looper.getMainLooper()).post(new Runnable() {
////                @Override
////                public void run() {
////                    if ( phoenixPlug.channel== null)
////                        channel_state.setText("Channel: null");
////                        else
////
////                    channel_state.setText("Channel:" + phoenixPlug.channel.state+"");
////
////                }
////            });
////        }
//
//
//        if ( phoenixPlug.channel== null)
//        {
//            setChannelLamp(Color.GRAY);
//        }
//        else
//        switch (phoenixPlug.channel.state){
//
//            case CLOSED:
//                setChannelLamp(Color.BLACK);
//                break;
//            case ERRORED:
//                setChannelLamp(Color.RED);
//                break;
//            case JOINED:
//                setChannelLamp(Color.GREEN);
//                break;
//            case JOINING:
//                setChannelLamp(Color.YELLOW);
//                break;
//        }
//    }
//
//    void setChannelLamp(final int color){
////        if (channel_state_lamp!=null){
////            new Handler(Looper.getMainLooper()).post(new Runnable() {
////                @Override
////                public void run() {
////                    if (channel_state_lamp!=null) {
////                        channel_state_lamp.setTextColor(color);
////                    }
////
////                }
////            });
////        }
//    }
}
