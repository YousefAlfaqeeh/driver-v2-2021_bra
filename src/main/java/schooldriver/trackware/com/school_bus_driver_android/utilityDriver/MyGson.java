package schooldriver.trackware.com.school_bus_driver_android.utilityDriver;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/*mohbader*/

/**
 * Created by Mohammad Al-Najjar.
 * date: 8/27/16
 *
 * @version 1.0
 * @since 2.0
 */
public class MyGson {

    private static Gson gson;

    private MyGson() {
    }

    public static Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder()
                    .disableHtmlEscaping()
                    .serializeNulls()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
            gson = builder.create();
        }
        return gson;
    }

}
