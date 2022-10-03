package schooldriver.trackware.com.school_bus_driver_android.APIs_new;


public enum HttpMethod {

    GET(0),
    POST(1),
    PUT(2),
    DELETE(3),
    HEAD(4),
    OPTIONS(5),
    TRACE(6),
    PATCH(7);

    int value;

    HttpMethod(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}