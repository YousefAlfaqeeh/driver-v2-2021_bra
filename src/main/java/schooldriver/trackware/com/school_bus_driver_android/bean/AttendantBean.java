package schooldriver.trackware.com.school_bus_driver_android.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AttendantBean implements Parcelable {

    String name;
    String mobile_number;
    String photo;
    String pick_lat;
    String pick_lng;
    String drop_lat;
    String drop_lng;


    public String getName() {
        return name;
    }

    public AttendantBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public AttendantBean setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public AttendantBean setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getPick_lat() {
        return pick_lat;
    }

    public AttendantBean setPick_lat(String pick_lat) {
        this.pick_lat = pick_lat;
        return this;
    }

    public String getPick_lng() {
        return pick_lng;
    }

    public AttendantBean setPick_lng(String pick_lng) {
        this.pick_lng = pick_lng;
        return this;
    }

    public String getDrop_lat() {
        return drop_lat;
    }

    public AttendantBean setDrop_lat(String drop_lat) {
        this.drop_lat = drop_lat;
        return this;
    }

    public String getDrop_lng() {
        return drop_lng;
    }

    public AttendantBean setDrop_lng(String drop_lng) {
        this.drop_lng = drop_lng;
        return this;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.mobile_number);
        dest.writeString(this.photo);
        dest.writeString(this.pick_lat);
        dest.writeString(this.pick_lng);
        dest.writeString(this.drop_lat);
        dest.writeString(this.drop_lng);
    }

    public AttendantBean() {
    }

    protected AttendantBean(Parcel in) {
        this.name = in.readString();
        this.mobile_number = in.readString();
        this.photo = in.readString();
        this.pick_lat = in.readString();
        this.pick_lng = in.readString();
        this.drop_lat = in.readString();
        this.drop_lng = in.readString();
    }

    public static final Parcelable.Creator<AttendantBean> CREATOR = new Parcelable.Creator<AttendantBean>() {
        @Override
        public AttendantBean createFromParcel(Parcel source) {
            return new AttendantBean(source);
        }

        @Override
        public AttendantBean[] newArray(int size) {
            return new AttendantBean[size];
        }
    };
}
