package schooldriver.trackware.com.school_bus_driver_android.fragment.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseFragment;
import schooldriver.trackware.com.school_bus_driver_android.bean.AttendantBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.RoundBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.enums.CheckEnum;
import schooldriver.trackware.com.school_bus_driver_android.enums.TypeRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created by Ibrahem Al-Betar on 3/2/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MapFragment extends BaseFragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks
        ,
        GoogleApiClient.OnConnectionFailedListener
        ,
        LocationListener


{
    private RoundBean roundBean;
    private GoogleMap mMap;
    private MapView mMapView;
    private MapPresenter mMapPresenter;
    private Marker markerBus;
    private float currentZoom = 20;
    private View btnTypeButton, imgSendMessage, imgTypeRound, btnReCenter;
    private View map_header;
    private TextView labTimer;
    private AppCompatImageView imgSize;
    private boolean trackBus = true;
    private static double latest_bus_lat = 0;
    private static double latest_bus_lon = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_map, container, false);
        imgTypeRound = view.findViewById(R.id.imgTypeRound);
        imgSize = (AppCompatImageView) view.findViewById(R.id.imgSize);
        map_header = view.findViewById(R.id.map_header);
        btnTypeButton = view.findViewById(R.id.btnTypeButton);
        imgSendMessage = view.findViewById(R.id.imgSendMessage);
        labTimer = view.findViewById(R.id.labTimer);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        btnReCenter = view.findViewById(R.id.btnReCenter);


        /**/
        Bundle extras = getArguments();
        roundBean = (RoundBean) extras.getSerializable(UtilityDriver.ROUND);
        if (roundBean.isRoundStartedNow()) {
            startMapTimer();
        }
        ((TextView) view.findViewById(R.id.labNameRound)).setText(roundBean.getNameRound());
        mMapPresenter = new MapPresenter(getMainActivity());
        /**/
        view.findViewById(R.id.linNotification).setOnClickListener(notificationIconClickListener);
        view.findViewById(R.id.linBack).setOnClickListener(backClickListener);
        view.findViewById(R.id.tool_back).setOnClickListener(backClickListener);

        /**/
        imgSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSize.setImageResource(map_header.getVisibility() == View.GONE ? R.drawable.img_full : R.drawable.img_minm);
                map_header.setVisibility(map_header.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                view.findViewById(R.id.tool_back).setVisibility(view.findViewById(R.id.tool_back).getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
            }
        });
        btnReCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnReCenter.setVisibility(View.GONE);
                trackBus = true;
                doRecenterCamera();
                updateBusLocation();
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            mMapView.setLayerType(View.LAYER_TYPE_NONE, null);
        }
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        btnTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(mMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID ? GoogleMap.MAP_TYPE_NORMAL : GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        imgSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSendMessageDialog(roundBean);
//                roundAdapter.notifyDataSetChanged();
            }
        });


        view.findViewById(R.id.map_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btnReCenter.setVisibility(View.VISIBLE);
                trackBus = false;
                return false;
            }
        });
        return view;
    }

//    @Override
//    protected boolean onTap(int index) {
//        return true;
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng arg0) {
//                btnReCenter.setVisibility(View.VISIBLE);
//                trackBus = false;
//            }
//        });


        LatLng mLatLng = null;
        mLatLng = new LatLng(StaticValue.latitudeMain, StaticValue.longitudeMain);

        if (StaticValue.latitudeMain == 0 || StaticValue.longitudeMain == 0) {
            mLatLng = new LatLng(
                    roundBean.getListStudentBean().get(0).getLatitude()
                    ,
                    roundBean.getListStudentBean().get(0).getLongitude()
            )
            ;

        }
        latest_bus_lon = mLatLng.longitude;
        latest_bus_lat = mLatLng.latitude;

        int no = 1;
        for (StudentBean studentBean : roundBean.getListStudentBean()) {
            if (roundBean.getListStudentBean().get(0).getTypeRoundEnum() == TypeRoundEnum.PICK_ROUND) {
                if (!studentBean.isAbsent()) {
                    if (studentBean.getCheckEnum() == CheckEnum.EMPTY) {
                        addPoint(new LatLng(studentBean.getLatitude(), studentBean.getLongitude()), studentBean, no);
                        no++;
                    }
                }
            } else {
                if (!studentBean.isNoShow()) {
                    if (studentBean.getCheckEnum() == CheckEnum.CHECK_IN) {
                        addPoint(new LatLng(studentBean.getLatitude(), studentBean.getLongitude()), studentBean, no);
                        no++;
                    }
                }
            }
        }
        addAttendantPoint(roundBean.getAttendantBean(), roundBean.getTypeRoundEnum());
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

//        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition) {
//                if (cameraPosition.zoom != currentZoom) {
//                    currentZoom = cameraPosition.zoom;  // here you get zoom level
//                }
//            }
//        });

//        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
//            @Override
//            public void onCameraMoveStarted(int i) {
//                center_enabled = false;
//                if (cameraPosition.zoom != currentZoom) {
//                    currentZoom = cameraPosition.zoom;  // here you get zoom level
//                }
//            }
//        });


//        mMapView.setOnTouchListener(new MapView.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                trackBus = false;
//                return false;
//            }
//        });

        currentZoom = 20;
        System.err.println(mLatLng + "    77777777");
        markerBus = mMap.addMarker(new MarkerOptions().position(mLatLng).icon(BitmapDescriptorFactory.fromBitmap(mMapPresenter.MarkerWithOptionsBitmap())));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mLatLng)      // Sets the center of the map to Mountain View
                .zoom(currentZoom)                   // Sets the zoom
//                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(70)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        marker = mMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromBitmap(mMapPresenter.MarkerWithOptionsBitmap()))
//                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
//                .position(mLatLng));
//        CameraPosition INIT =
//                new CameraPosition.Builder()
//                        .target(mLatLng)
//                        .zoom(currentZoom)
//                        .build();
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));

//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, currentZoom));


        double dpPerdegree = 256.0 * Math.pow(2, currentZoom) / 170.0;
        double screen_height = (double) mMapView.getHeight();
        double screen_height_30p = 30.0 * screen_height / 100.0;
        double degree_30p = screen_height_30p / dpPerdegree;
        LatLng centerlatlng = new LatLng(mLatLng.latitude + degree_30p, mLatLng.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerlatlng, currentZoom), 1000, null);
//        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
//        mMap.setOnInfoWindowClickListener(this);


    }

//    @Override
//    public void onInfoWindowClick(Marker marker) {
//
//    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = mActivity.getLayoutInflater().inflate(R.layout.view_marker_student, null);
//            myContentsView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));

        }

        @Override
        public View getInfoContents(final Marker marker) {
            ImageView imageView = ((ImageView) myContentsView.findViewById(R.id.imgCall));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mActivity,
//                            "Info Window clicked@" + marker.getId(),
//                            Toast.LENGTH_SHORT).show();
                }
            });
//

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    boolean value = true;

    private void addPoint(final LatLng point, final StudentBean studentBean, final int no) {

        if (value) {


            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    URL url;
                    Bitmap bmp = null;
                    try {
                        if (StaticValue.G34) {
                            url = new URL(studentBean.getAvatar());
                            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final Bitmap finalBmp = bmp;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMap.addMarker(new MarkerOptions()
                                    .title(roundBean.getNameRound())
                                    .icon(BitmapDescriptorFactory.fromBitmap(mMapPresenter.MarkerWithStudentsBitmapSmall(studentBean, finalBmp, no)))
                                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                                    .position(point));


                        }
                    });
                }
            });
            thread.start();
//            value = false;
        }

//        }

    }


    private void addAttendantPoint(final AttendantBean attendantBean, TypeRoundEnum typeRoundEnum) {
        try {

            if (attendantBean == null)
                return;

            /**/
            String lat = "", lng = "";
            /**/
            switch (typeRoundEnum) {
                case PICK_ROUND:
                    lat = attendantBean.getPick_lat();
                    lng = attendantBean.getPick_lng();
                    break;
                case DROP_ROUND:
                    lat = attendantBean.getDrop_lat();
                    lng = attendantBean.getDrop_lng();
                    break;
            }

            if (UtilityDriver.isEmptyString(lat) || UtilityDriver.isEmptyString(lng))
                return;

            final LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            /**/
            /**/
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    URL url;
                    Bitmap bmp = null;
                    try {
                        if (StaticValue.G34) {
                            url = new URL(attendantBean.getPhoto());
                            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final Bitmap finalBmp = bmp;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMap.addMarker(new MarkerOptions()
                                    .title(roundBean.getNameRound())
                                    .icon(BitmapDescriptorFactory.fromBitmap(mMapPresenter.MarkerWithAttendantBeanBitmapSmall(attendantBean, finalBmp, -1)))
                                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                                    .position(point));


                        }
                    });
                }
            });
            thread.start();
        } catch (Exception e) {

        }


    }


    @Override
    public void onResume() {
        super.onResume();
        startTimerSpeed();


//        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        mMapView.onPause();

        stopTimerSpeed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mMapView.onDestroy();
        } catch (Exception e) {


        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    Timer timerSpeed;
    TimerTask timerTaskSpeed;

    final Handler handlerSpeed = new Handler();

    public void startTimerSpeed() {
        //set a new Timer
        timerSpeed = new Timer();

        //initialize the TimerTask's job
        timerSpeed();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timerSpeed.schedule(timerTaskSpeed, 20 * 100, 15 * 100); //
    }


    private float bearing(LatLng latLng1, LatLng latLng2) {
        double lat1 = latLng1.latitude;
        double lng1 = latLng1.longitude;
        double lat2 = latLng2.latitude;
        double lng2 = latLng2.longitude;

        double dLon = (lng2 - lng1);
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        double brng = Math.toDegrees((Math.atan2(y, x)));
        brng = (360 - ((brng + 360) % 360));
        return (float) brng;
    }

//    LatLng mainLatLng;

    public void timerSpeed() {

        timerTaskSpeed = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handlerSpeed.post(new Runnable() {
                    public void run() {
//                        if (listTestMarker.size() > 0) {
                        if (StaticValue.latitudeMain == 0 || StaticValue.longitudeMain == 0) {
                            return;
                        }
                        if (trackBus) {
                            btnReCenter.setVisibility(View.GONE);
                            doRecenterCamera();
                        } else {
                            btnReCenter.setVisibility(View.VISIBLE);
                        }

                        updateBusLocation();
                    }
                });
            }
        };
    }


    private void updateBusLocation() {
        LatLng mainLatLng;

        if (StaticValue.latitudeMain != 0 && StaticValue.longitudeMain != 0) {
            mainLatLng = new LatLng(StaticValue.latitudeMain, StaticValue.longitudeMain);
            latest_bus_lat = StaticValue.latitudeMain + 0;
            latest_bus_lon = StaticValue.longitudeMain + 0;
        } else if (latest_bus_lat != 0 && latest_bus_lon != 0) {
            mainLatLng = new LatLng(latest_bus_lat, latest_bus_lon);
        } else
            return;


        if (markerBus != null) {
            markerBus.setPosition(mainLatLng);
        } else {
            markerBus.setPosition(mainLatLng);
        }


    }

    private void doRecenterCamera() {
        LatLng mainLatLng;

        if (StaticValue.latitudeMain != 0 && StaticValue.longitudeMain != 0) {
            mainLatLng = new LatLng(StaticValue.latitudeMain, StaticValue.longitudeMain);
            latest_bus_lat = StaticValue.latitudeMain + 0;
            latest_bus_lon = StaticValue.longitudeMain + 0;
        } else if (latest_bus_lat != 0 && latest_bus_lon != 0) {
            mainLatLng = new LatLng(latest_bus_lat, latest_bus_lon);
        } else
            return;


        System.err.println(mainLatLng + "    77777777");
//                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20.5f), 1000, null);

        if (markerBus != null) {
//            markerBus.setPosition(mainLatLng);
            CameraPosition INIT =
                    new CameraPosition.Builder()
                            .target(mainLatLng)
                            .zoom(currentZoom)
                            .bearing(0F) // orientation
                            .tilt(50F) // viewing angle
                            .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mainLatLng, currentZoom));
            double dpPerdegree = 256.0 * Math.pow(2, currentZoom) / 170.0;
            double screen_height = (double) mMapView.getHeight();
            double screen_height_30p = 30.0 * screen_height / 100.0;
            double degree_30p = screen_height_30p / dpPerdegree;
//                            LatLng centerlatlng = new LatLng( mainLatLng.latitude, mainLatLng.longitude );
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mainLatLng, currentZoom), 1000, null);
//                            CameraPosition cameraPosition = new CameraPosition.Builder()
//                                    .target(mainLatLng)      // Sets the center of the map to Mountain View
//                                    .zoom(currentZoom)                   // Sets the zoom
////                                    .bearing(0)                // Sets the orientation of the camera to east
//                                    .tilt(70)                   // Sets the tilt of the camera to 30 degrees
//                                    .build();                   // Creates a CameraPosition from the builder
//                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
//            markerBus.setPosition(mainLatLng);
            CameraPosition INIT =
                    new CameraPosition.Builder()
                            .target(mainLatLng)
                            .zoom(currentZoom)
                            .bearing(bearing(mainLatLng, mainLatLng)) // orientation
                            .tilt(50F) // viewing angle
                            .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mainLatLng, currentZoom));
            double dpPerdegree = 256.0 * Math.pow(2, currentZoom) / 170.0;
            double screen_height = (double) mMapView.getHeight();
            double screen_height_30p = 30.0 * screen_height / 100.0;
            double degree_30p = screen_height_30p / dpPerdegree;
//                            LatLng centerlatlng = new LatLng( mainLatLng.latitude, mainLatLng.longitude );
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mainLatLng, currentZoom), 1000, null);


        }


    }

    public void stopTimerSpeed() {
        //stop the timer, if it's not already null
        if (timerSpeed != null) {
            timerSpeed.cancel();
            timerSpeed = null;
        }
    }


    Handler timerHandler = new Handler(Looper.getMainLooper());

    private void startMapTimer() {
        try {
            if (UtilityDriver.getStringShared(UtilityDriver.START_ROUND_TIME).trim().equals("")) {
                labTimer.setText("00:00:00");
                return;
            }
            /**/
            if (labTimer.getText().toString().trim().equals("00:00:00")) { // on first time
                String timeFormat = getTimerText();
                labTimer.setText(timeFormat.toString());
            }

            /**/

            Runnable timerRunnable = new Runnable() {
                @Override
                public void run() {


                    try {
                        String timeFormat = getTimerText();
                        labTimer.setText(timeFormat.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        timerHandler.removeCallbacksAndMessages(null);
                        timerHandler = null;
                    }


                    /**/
                    startMapTimer();
                    /**/

                }
            };


            timerHandler.postDelayed(timerRunnable, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
