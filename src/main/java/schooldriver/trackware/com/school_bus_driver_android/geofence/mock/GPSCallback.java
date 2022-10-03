package schooldriver.trackware.com.school_bus_driver_android.geofence.mock;

import android.location.Location;

/**
 * Created by Ibrahem Al-Betar on 12/5/2016.
 */

public interface GPSCallback {
    public abstract void onGPSUpdate(Location location);
}
