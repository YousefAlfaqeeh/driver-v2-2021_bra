package schooldriver.trackware.com.school_bus_driver_android.interfaceDriver;

import schooldriver.trackware.com.school_bus_driver_android.enums.StatusRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.DriverConstants;

/**
 * Created by   3/11/17.
 */

public interface IActionDialog extends DriverConstants {

    void done(StatusRoundEnum statusRoundEnum);
    void done(String message);
    void cancel();

}
