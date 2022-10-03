package schooldriver.trackware.com.school_bus_driver_android.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ibrahem Al-Betar on 3/2/2017.
 */

public class NotificationBean implements Parcelable {

    private String details;
    private String avatar;
    private String time;
    private String fromDate;
    private int id;

    public NotificationBean(int id, String details, String avatar, String time, String fromDate) {
        this.details = details;
        this.avatar = avatar;
        this.time = time;
        this.fromDate = fromDate;
    }

    public NotificationBean() {
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.details);
        dest.writeString(this.avatar);
        dest.writeString(this.time);
        dest.writeString(this.fromDate);
        dest.writeInt(this.id);
    }

    protected NotificationBean(Parcel in) {
        this.details = in.readString();
        this.avatar = in.readString();
        this.time = in.readString();
        this.fromDate = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<NotificationBean> CREATOR = new Parcelable.Creator<NotificationBean>() {
        @Override
        public NotificationBean createFromParcel(Parcel source) {
            return new NotificationBean(source);
        }

        @Override
        public NotificationBean[] newArray(int size) {
            return new NotificationBean[size];
        }
    };
}
