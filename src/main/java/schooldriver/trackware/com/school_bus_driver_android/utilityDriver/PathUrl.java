package schooldriver.trackware.com.school_bus_driver_android.utilityDriver;

/**
 * Created by   3/20/17.
 */

public class PathUrl {

    public static boolean send_EndRound_Notification = true;
    public static boolean send_CheckIn_Notification = true;
    public static boolean send_CheckOut_Notification = true;
    public static boolean send_NoShow_Notification = true;
    public static boolean send_Absent_Notification = true;

//    public static String DEV_PROD = "prod";
    public static   String DEV_PROD = "dev";
//    public static String URL = "https://boknyyx648.execute-api.eu-central-1.amazonaws.com/" + DEV_PROD;
//    public static String URL = "http://apipy-stg.trackware.com/" ;//ddddddddddddddddddddddddd
    public static String URL = "http://192.168.1.150:8000/" ;
//    public static String URL = "http://192.168.1.19:8888"; // local url
    public static String MAIN_URL = URL;
//    public static String XDEBUG_SESSION_START = "?XDEBUG_SESSION_START=SCHOOL_API";
//    public static boolean BEACON_Enabled = true;
//    public static String LOGIN = "/api/login";
    public static String LOGIN = "api/login/";// yousef 1
    public static String ROUND_LIST = "api/drivers/round_list/";//yousef 2
//    public static String ROUND_LIST = "/api/drivers/round_list";
    public static String STUDENTS_LIST = "api/drivers/round-students-list/";//yousef 4
//    public static String STUDENTS_LIST = "/api/drivers/round-students-list?round_id=";
//    public static String RECENT_NOTIFICATION = "/api/recent-notifications";
public static String RECENT_NOTIFICATION = "api/recent-notifications/";//yousef 6
    public static String SET_ROUND_STATUS = "api/drivers/set_round_status/";//yousef 3
//    public static String SET_ROUND_STATUS = "/api/drivers/set_round_status";
    public static String CHECK_OUT_ALL = "/api/drivers/check_out_all";
//    public static String STUDENT_BUS_CHECK = "/api/drivers/student_bus_check";
    public static final String NOTIFY = "api/notify";//yousef 8
//    public static final String NOTIFY = "/api/notify";
    public static final String REORDERED = "api/drivers/reordered-students";//yousef 7
//    public static final String REORDERED = "/api/drivers/reordered-students";
    public static final String OFFLINE_STATUS = "api/drivers/students_bus_checks/";//yousef 5
//    public static final String OFFLINE_STATUS = "/api/drivers/students_bus_checks";
    public static final boolean CHECKIN_TIMER_ON = true;
//    public static final boolean CHECKIN_TIMER_TEST_ON = true;
    public static String VERSION_2 = "?version=2";
//    public static String LOG_URL = "https://4a5c513c9c754ab9921732a5087b7663:3bc05e02b9464a0e8a3f3e6bf35fe5e4@sentry.io/174447";
    public static String LATEST_APP_VERSION_URL = "https://raw.githubusercontent.com/Trackware/versions/master/schools_driver.json".trim();
//    public static final String PHOENIX_SOCKET = "ws://192.168.1.32:4011/socket/websocket";
//    public static final String PHOENIX_SOCKET = "wss://rt.trackware.com:81/socket/websocket";
//    public static final String NOTIFY_URL = "https://boknyyx648.execute-api.eu-central-1.amazonaws.com/dev";
//    public static String BUS_LOCATION = "/rt/api/schools/bus_location";
    public static String PUSH_NOTIFICATION = "rt/api/schools/push-notification";
//    public static String OFFLINE_LOCATION = "/rt/api/schools/bus_locations";


}
