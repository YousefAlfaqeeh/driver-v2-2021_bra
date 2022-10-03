package schooldriver.trackware.com.school_bus_driver_android.APIs_new;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GsonInstance {

    private static Gson gson;

    private GsonInstance() {
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
