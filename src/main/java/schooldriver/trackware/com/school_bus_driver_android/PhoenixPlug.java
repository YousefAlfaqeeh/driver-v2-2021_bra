//package schooldriver.trackware.com.school_bus_driver_android;
//
//import android.net.Uri;
//
//import com.fasterxml.jackson.databind.node.ObjectNode;
//
//import org.phoenixframework.channels.Channel;
//import org.phoenixframework.channels.ChannelEvent;
//import org.phoenixframework.channels.ChannelState;
//import org.phoenixframework.channels.Envelope;
//import org.phoenixframework.channels.IErrorCallback;
//import org.phoenixframework.channels.IMessageCallback;
//import org.phoenixframework.channels.ISocketCloseCallback;
//import org.phoenixframework.channels.ISocketOpenCallback;
//import org.phoenixframework.channels.Socket;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.Callable;
//
//import schooldriver.trackware.com.school_bus_driver_android.API.interfaceApi.IRestCallPhoenix;
//import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.PathUrl;
//
//
///**
// * Created by samir on 8/18/16.
// */
//public class PhoenixPlug {
//
//
//    public Socket socket;
//    public Channel channel;
////    private IRestCallPhoenix iRestCallPhoenix;
////    private String currntTopic;
////    private int TIMER = 0;
//    /*public static boolean JOINED;*/
//
//    private static PhoenixPlug singleton;
//
//    public static PhoenixPlug getInstance() {
//        if (singleton == null)
//            singleton = new PhoenixPlug();
//
//        return singleton;
//    }
//
////    private int busID;
//
////    private String client_type;
////    private String token;
//
////    public PhoenixPlug(String client_type, String token) {
////
////        this.client_type = client_type;
////        this.token = token;
////
////    }
//
//    public PhoenixPlug() {
//    }
//
////    Timer timer;
////    public void startTimer() {
////        if ( timer ==null) {
////            timer = new Timer();
////            timer.schedule(new TimerTask() {
////
////                @Override
////                public void run() {
////                    maintianConnection();
////                }
////
////            }, 0, 1000);//Update text every second
////        }
////    }
//
//    //    private static long TIMER = 0;
////    {
////        if(TIMER == 6){
////
////        }
////        TIMER++;
////    }
//
//
////    void maintianConnection() {
////        long current_time = System.currentTimeMillis();
////
////        if (TIMER > 7 && channel.state != ChannelState.JOINING) {
////            socket.vertualFail();
////            TIMER =-5;
////        }
////        TIMER ++;
////
////    }
//
//
////    void maintianConnection() {
////        long current_time = System.currentTimeMillis();
////
////        if (current_time - last_recieved_message_time > 7000 )
////            {
////                socket.vertualFail();
////                last_recieved_message_time = System.currentTimeMillis();
////            }
////        }
////        long last_recieved_message_time = 0;
//
////    public void receivedMessage(final IRestCallPhoenix iRestCallPhoenix) {
////        startTimer();
////        // UtilityParent.sentryLog("receivedMessage","",MainActivity.mActivity);
////        channel.off("new:bus_location");
////        channel.on("new:bus_location", new IMessageCallback() {
////                    @Override
////                    public void onMessage(Envelope envelope) {
////                        System.out.println("NEW MESSAGE: " + envelope.toString());
////                        try {
////                            Integer user_id = (Integer) envelope.getPayload().findValue("bus_id").asInt();
////                            double lat = (double) envelope.getPayload().findValue("lat").asDouble();
////                            double lng = (double) envelope.getPayload().findValue("long").asDouble();
////                            iRestCallPhoenix.newLocation(lat, lng, user_id);
////                            TIMER = 0;
//////                            last_recieved_message_time = System.currentTimeMillis();
////
////                            // UtilityParent.sentryLog("receivedMessage","new:bus_location",MainActivity.mActivity);
////                        } catch (NullPointerException e) {
////                            e.printStackTrace();
////                        }
////                    }
////                }
////        );
////    }
//
//
//
////    public void pushPhoenix(ObjectNode map) {
////
////        try {
////            // UtilityParent.sentryLog("pushPhoenix",("absent_by_parent"+" : "+channel.state+" : "+map.toString()+" ,busId"+busID),MainActivity.mActivity);
//////            channel.push("absent_by_parent:" + busID, map);
////        } catch (IOException e) {
////            e.printStackTrace();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////    }
//
////    private ArrayList<ObjectNode> buffered_messages = new ArrayList<>();
//
////    private void buffer_message(ObjectNode map) {
////        if (buffered_messages == null)
////            buffered_messages = new ArrayList<>();
////
////        /*
////        TODO
////        start calling push_buffered_messages
////         */
////        buffered_messages.add(map);
////    }
//
//
////    private void push_buffered_messages() {
////        if (channel.state == ChannelState.JOINED) {
////            if (buffered_messages.size() > 0) {
////                ObjectNode map = buffered_messages.remove(0);
////                pushPhoenix(map);
////            } else {
////                /*
////                stop calling push_buffered_messages
////                 */
////            }
////        }
////    }
//
////    public void closeConnection() {
////        try {
////            if (socket != null && socket.isConnected())
////                socket.disconnect();
////        } catch (IOException e) {
////            e.printStackTrace();
////        } catch (RuntimeException e) {
////            e.printStackTrace();
////        }
////    }
//
////    public void joinBus(final IRestCallPhoenix iRestCallPhoenix, int USERiD) throws Exception {
////        busID = USERiD;
////        String topicName = "buses:" + busID;
////        currntTopic = topicName;
////
////        if (socket == null || !socket.isConnected()) {
////
////
////            initializeSocket(
////                    new Callable<Void>() {
////                        public Void call() throws Exception {
////                            // socket is opened now:
////                            toJoinChannel(iRestCallPhoenix);
////                            //set point color for text
////
////                            iRestCallPhoenix.socketConnected();
////                            return null;
////                        }
////                    },
////                    iRestCallPhoenix
////
////            );
////        } else {
////
////            toJoinChannel(iRestCallPhoenix);
////
////        }
////    }
//
//
////    private void toJoinChannel(final IRestCallPhoenix iRestCallPhoenix) throws Exception {
////
////
////        if (channel == null)
////            channel = socket.chan(currntTopic, null);
////
////
////        if (channel.state == ChannelState.ERRORED) {
////            joinChannel(iRestCallPhoenix);
////        } else {
////            if (channel.getTopic().equals(currntTopic)) {
////
////                    /*iRestCallPhoenix.onReadyToPushMessage();*/
////                joinChannel(iRestCallPhoenix);
////
////            } else {
////                boolean shouldWait = false;
////                if (shouldWait) {
////                    channel.on(ChannelEvent.CLOSE.getPhxEvent(), new IMessageCallback() {
////                        @Override
////                        public void onMessage(Envelope envelope) throws Exception {
////                            channel = socket.chan(currntTopic, null);
////                            joinChannel(iRestCallPhoenix);
////                        }
////                    });
////
////                    channel.leave();
////                } else {
////                    channel.leave();
////                    channel = socket.chan(currntTopic, null);
////                    joinChannel(iRestCallPhoenix);
////                }
////            }
////        }
////
////
////    }
//
////    private void joinChannel(final IRestCallPhoenix iRestCallPhoenix) throws Exception {
////        boolean shouldJoin = channel.state == ChannelState.CLOSED;
////
////        // UtilityParent.sentryLog("joinChannel","shouldJoin:"+shouldJoin+", "+channel.state,MainActivity.mActivity);
////        if (channel.state == ChannelState.ERRORED) return;
////        if (channel.state == ChannelState.JOINING) return;
////
////        if (shouldJoin) channel.join()
////                .receive("error", new IMessageCallback() {
////                    @Override
////                    public void onMessage(Envelope envelope) {
////                        System.out.println("error");
////
////                        // UtilityParent.sentryLog("joinChannel"," ,error"+" ,"+envelope.toString(),MainActivity.mActivity);
////                        iRestCallPhoenix.errorConnect(envelope.toString());
////                    }
////                })
////                .receive("ignore", new IMessageCallback() {
////                    @Override
////                    public void onMessage(Envelope envelope) {
////
////                        System.out.println("IGNORE");
////                        // UtilityParent.sentryLog("joinChannel"," ,ignore"+" ,"+envelope.toString(),MainActivity.mActivity);
////                        iRestCallPhoenix.ignoreConnect(envelope.toString());
////                    }
////                })
////                .receive("ok", new IMessageCallback() {
////                    @Override
////                    public void onMessage(Envelope envelope) throws Exception {
////                        System.out.println("JOINED with " + envelope.toString());
////                        // UtilityParent.sentryLog("joinChannel"," ,ok"+" ,"+envelope.toString(),MainActivity.mActivity);
////
////                        iRestCallPhoenix.onReadyToPushMessage();
////
////                    }
////                });
////        else {
////            iRestCallPhoenix.onReadyToPushMessage();
////        }
////    }
//
//
////    public void initializeSocket(final Callable<Void> onOpen, final IRestCallPhoenix iRestCallPhoenix) {
////        try {
////            Uri.Builder url = Uri.parse(PathUrl.PHOENIX_SOCKET).buildUpon();
////
////            url.appendQueryParameter("token", token);
////            url.appendQueryParameter("client_type", client_type);
////
////            // UtilityParent.sentryLog("initializeSocket"," ,"+url.toString(),MainActivity.mActivity);
////
////            try {
////                if (socket == null) {
////
////                    socket = new Socket(url.build().toString());
////
////                    socket.onOpen(new ISocketOpenCallback() {
////                        @Override
////                        public void onOpen() throws Exception {
////
////                            onOpen.call();
////                        }
////                    });
////                    socket.onClose(new ISocketCloseCallback() {
////                        @Override
////                        public void onClose() {
////
////                            boolean isOpened = true;
////                        }
////                    });
////                    socket.onError(new IErrorCallback() {
////                        @Override
////                        public void onError(String reason) {
////                            iRestCallPhoenix.errorConnect(reason);
////                        }
////                    });
////
////                }
////
////                if (!socket.isConnected()) {
////                    socket.connect();
////                }
////
////            } catch (IOException e) {
////                // UtilityParent.sentryLog("initializeSocket"," ,IOException : "+e.getMessage(),MainActivity.mActivity);
////                e.printStackTrace();
////            }
////        } catch (Exception e) {
////            // UtilityParent.sentryLog("initializeSocket"," ,Exception : "+e.getMessage(),MainActivity.mActivity);
////            e.printStackTrace();
////        }
////    }
//}
//
//
