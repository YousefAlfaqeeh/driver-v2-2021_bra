package schooldriver.trackware.com.school_bus_driver_android.interfaceDriver;

import java.util.List;

/**
 * Created by muradtrac on 4/6/17.
 */

public interface IBaseFragment<T>{

     void setListAdapter(List<T> listBean);
}
