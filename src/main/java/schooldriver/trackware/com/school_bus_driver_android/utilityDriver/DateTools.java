package schooldriver.trackware.com.school_bus_driver_android.utilityDriver;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by M.Bader on 2/13/2018.
 */

public class DateTools {

    public static class Formats {

        public static SimpleDateFormat DATE_FORMAT_GMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        public static SimpleDateFormat DATEONLY_FORMAT_GMT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        public static SimpleDateFormat TIMEONLY_FORMAT_12H_GMT = new SimpleDateFormat("hh:mm a", Locale.US);
        /**/

        public static SimpleDateFormat DATE_FORMAT_LOCAL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        public static SimpleDateFormat DATEONLY_FORMAT_LOCAL = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        public static SimpleDateFormat TIMEONLY_FORMAT_12H_LOCAL = new SimpleDateFormat("hh:mm a", Locale.US);

        static {
            DATE_FORMAT_GMT.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
            DATEONLY_FORMAT_GMT.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
            TIMEONLY_FORMAT_12H_GMT.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));

            DATE_FORMAT_LOCAL.setTimeZone(TimeZone.getDefault());
            DATEONLY_FORMAT_LOCAL.setTimeZone(TimeZone.getDefault());
            TIMEONLY_FORMAT_12H_LOCAL.setTimeZone(TimeZone.getDefault());
        }
    }


}
