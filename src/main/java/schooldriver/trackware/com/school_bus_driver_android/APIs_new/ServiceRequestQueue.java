package schooldriver.trackware.com.school_bus_driver_android.APIs_new;

import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;


public class ServiceRequestQueue {

    public static final String TAG = ServiceRequestQueue.class.getSimpleName();

    private static ServiceRequestQueue ourInstance;
    private RequestQueue queue;

    private ServiceRequestQueue(Context context) {
        Network network = new BasicNetwork(new HurlStack());
        Cache cache = new DiskBasedCache(context.getCacheDir(), 2048 * 1024);
        queue = new RequestQueue(cache, network);
        queue.start();
    }

    public static ServiceRequestQueue getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new ServiceRequestQueue(context);
        }
        return ourInstance;
    }

    public void start() {
        queue.start();
    }

    public void cancelAll(Object tag) {
        queue.cancelAll(tag);
    }

    public Request add(Request request) {
        Log.e(TAG, "URL: " + request.getUrl());
        return queue.add(request);
    }

    public void cancelAll(RequestQueue.RequestFilter filter) {
        queue.cancelAll(filter);
    }

    public Cache getCache() {
        return queue.getCache();
    }

    public void stop() {
        queue.stop();
    }

    public int getSequenceNumber() {
        return queue.getSequenceNumber();
    }
}
