package schooldriver.trackware.com.school_bus_driver_android.APIs_new.ListenersAndInterFaces;


public interface OnApiComplete<Type> {

    void onSuccess(Type type);

    void onError(int errorCode, String errorMessage);

}
