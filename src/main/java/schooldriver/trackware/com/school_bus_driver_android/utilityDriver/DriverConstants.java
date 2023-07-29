package schooldriver.trackware.com.school_bus_driver_android.utilityDriver;


public interface DriverConstants {
    String STUDENT_NAME_API_FORMAT = "@student_name";
    String ROUND_NAME_API_FORMAT = "@round_name";
    String BUS_NUMBER_API_FORMAT = "@bus_number";
    /**/


    String IN_BUS = "in";
    String REACHED_HOME = "out";
    String REACHED_SCHOOL = "out-school";     // new
    String NO_SHOW_PICK = "no-show";        // by driver
    String NO_SHOW_DROP = "no-show-drop";   // by driver // new
    String ABSENT_MORNING = "absent";         // by parent
    String ABSENT_ALLDAY = "absent-all";     // by parent // new
    String EMERGENCY = "emergency";


     int check_undo_timer = 10;

}
