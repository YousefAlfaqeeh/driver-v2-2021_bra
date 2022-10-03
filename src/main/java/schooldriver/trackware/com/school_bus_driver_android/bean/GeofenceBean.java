package schooldriver.trackware.com.school_bus_driver_android.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by muradtrac on 4/3/17.
 */

public class GeofenceBean implements Parcelable {

    private String name;
    private String detais;
    private double latitude;
    private double longitude;
    private String geofenceId;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GeofenceBean(String geofenceId, String details) {

        this.detais = details;
        this.geofenceId = geofenceId;
    }

    public GeofenceBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDetais() {
        return detais;
    }

    public void setDetais(String detais) {
        this.detais = detais;
    }

    public String getGeofenceId() {
        return geofenceId;
    }

    public void setGeofenceId(String geofenceId) {
        this.geofenceId = geofenceId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.detais);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.geofenceId);
        dest.writeInt(this.id);
    }

    protected GeofenceBean(Parcel in) {
        this.name = in.readString();
        this.detais = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.geofenceId = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<GeofenceBean> CREATOR = new Parcelable.Creator<GeofenceBean>() {
        @Override
        public GeofenceBean createFromParcel(Parcel source) {
            return new GeofenceBean(source);
        }

        @Override
        public GeofenceBean[] newArray(int size) {
            return new GeofenceBean[size];
        }
    };
}
