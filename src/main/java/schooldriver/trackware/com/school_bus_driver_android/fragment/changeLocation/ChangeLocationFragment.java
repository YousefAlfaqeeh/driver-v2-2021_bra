package schooldriver.trackware.com.school_bus_driver_android.fragment.changeLocation;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseFragment;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


/**
 * Created by mohbader on 4/2/2017.
 */

public class ChangeLocationFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final int MY_LOCATION_REQUEST_CODE = 100;
    Activity mActivity;
    private GoogleMap mMap;
    MapView mMapView;
    GoogleApiClient mGoogleApiClient;

    Location mLastLocation;
    LocationRequest mLocationRequest;
    static double lat, lng;
    static double latEnd, lngEnd;
//    LinearLayout mainBar;

    Marker mCurrLocationMarker;
    Button btnSaveLocation;
    Button btnCancelChangeLocation;
    ChangeLocationPresenter mapPresenter;
    String roundType = "";
    String dropOff = null;
    String pickUp = null;
    LatLng oldLocation;
    private MarkerOptions marker;
    private int studentID;
    //    private int studentID;
    CheckBox checkDropOff;
    CheckBox checkPickUp;
    // ChangeLocationDialog changeLocationDialog;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_change_location, container, false);
        view.findViewById(R.id.linBack).setOnClickListener(backClickListener);
        mActivity = getActivity();
        Bundle extras = getArguments();
        this.studentID = extras.getInt("studentID");
        checkPickUp = (CheckBox) view.findViewById(R.id.checkPickUp);
        checkDropOff = (CheckBox) view.findViewById(R.id.checkDropOff);
        btnCancelChangeLocation = (Button) view.findViewById(R.id.btnCancleLocattion);
        btnSaveLocation = (Button) view.findViewById(R.id.btnSaveLocattion);
//        if (UtilityDriver.getStringShared(UtilityDriver.LANGUAGE).equals("ar")) {
//            checkDropOff.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//            checkPickUp.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//            btnCancelChangeLocation.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//            btnSaveLocation.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        }


        checkDropOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dropOff = mActivity.getString(R.string.drop_off);
                    roundType = "drop-off";
                } else {
                    dropOff = null;
                }
            }
        });

        checkPickUp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pickUp = mActivity.getString(R.string.pic_up);
                    roundType = "pick-up";
                } else {
                    pickUp = null;
                }
            }
        });


        btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    latEnd = marker.getPosition().latitude;
                    lngEnd = marker.getPosition().longitude;

                    if (checkPickUp.isChecked() || checkDropOff.isChecked()) {
                        if (checkPickUp.isChecked() && checkDropOff.isChecked()) {
                            roundType = "both";
                            if (StaticValue.progressDialog != null) {
                                StaticValue.progressDialog.show();
                            }
                            mapPresenter = new ChangeLocationPresenter(ChangeLocationFragment.this.getActivity(), roundType);
                            mapPresenter.callChangeLocation(roundType, latEnd, lngEnd, studentID);

                        } else {
                            if (StaticValue.progressDialog != null) {
                                StaticValue.progressDialog.show();
                            }
                            mapPresenter = new ChangeLocationPresenter(ChangeLocationFragment.this.getActivity(), roundType);
                            mapPresenter.callChangeLocation(roundType, latEnd, lngEnd, studentID);
                        }
                    } else {
                        UtilityDriver.showMessage(mActivity, mActivity.getString(R.string.select_round_));
                    }

/*
                 changeLocationDialog=new ChangeLocationDialog(mActivity);
                changeLocationDialog.show();*/
                } catch (NullPointerException e) {
                    UtilityDriver.showMessageDialog(mActivity, mActivity.getString(R.string.error), mActivity.getString(R.string.missing_internet_error));
                }
            }
        });

        btnCancelChangeLocation.setOnClickListener(backClickListener);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getMainActivity().checkLocationPermission(true);
        }

        mMapView = (MapView) view.findViewById(R.id.mapChangeLocation);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            mMapView.setLayerType(View.LAYER_TYPE_NONE,null);
        }
//        mMapView.getOverlay().add(new my);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(ChangeLocationFragment.this.getActivity());

        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);


        return view;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ChangeLocationFragment.this.getActivity(),
                    ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                buildGoogleApiClient();
                //  mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            //mMap.setMyLocationEnabled(true);
        }


        if (ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        } else {
//            ActivityCompat.requestPermissions(mActivity, new String[]{
//                            android.Manifest.permission.ACCESS_FINE_LOCATION,
//                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                    MY_LOCATION_REQUEST_CODE);
        }

//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        mMap.getUiSettings().setMapToolbarEnabled(true);
//        mMap.getUiSettings().setZoomControlsEnabled(true);


        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {


                try {
                    LatLng latlng = new LatLng(lat, lng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(oldLocation));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(oldLocation, 15.0f));
                    initMarker(oldLocation);

                } catch (Exception e) {
                    e.printStackTrace();
                }

//
                return true;
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(ChangeLocationFragment.this.getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        mGoogleApiClient.connect();
    }


    public void onStart() {
        super.onStart();
        //  callGetStudentList();
    }

    @Override
    public void onResume() {
        super.onResume();
//        startTimerSpeed();
        //  callGetStudentList();

        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();

//        stopTimerSpeed();
    }

    @Override
    public void onStop() {
        try {
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

        } catch (Exception e) {

        }
        super.onStop();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null)
            mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(ChangeLocationFragment.this.getActivity(),
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {


            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            if (StaticValue.progressDialog != null) {
                StaticValue.progressDialog.show();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    Marker addedMKarker;

    public void initMarker(LatLng latLng) {
        mMap.clear();
//        if (marker == null) {
        marker = new MarkerOptions();
        marker.position(latLng);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        marker.title("Current Position");
        marker.draggable(true);
        addedMKarker = mMap.addMarker(marker);
        lat = marker.getPosition().latitude;
        lng = marker.getPosition().longitude;
//        }
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                marker.getPosition();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                marker.getPosition();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                marker.getPosition();
                latEnd = marker.getPosition().latitude;
                lngEnd = marker.getPosition().longitude;
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;


//        if (location.getAccuracy() <= 50.0f) {
            // use your location data here
            oldLocation = new LatLng(location.getLatitude(), location.getLongitude());

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            initMarker(latLng);
//
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15.0f));

            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
            try {
                StaticValue.progressDialog.dismiss();
            } catch (Exception e) {

            }
//        }


    }

//    public static boolean checkLocationPermission(Activity Activity) {
//        if (ContextCompat.checkSelfPermission(Activity,
//                ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Asking user if explanation is needed
//            if (ActivityCompat.shouldShowRequestPermissionRationale(Activity,
//                    ACCESS_FINE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//                //Prompt the user once explanation has been shown
//                ActivityCompat.requestPermissions(Activity,
//                        new String[]{ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(Activity,
//                        new String[]{ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }
}

