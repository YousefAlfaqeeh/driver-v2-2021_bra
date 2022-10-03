package schooldriver.trackware.com.school_bus_driver_android.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class AbsentKidsBean implements Parcelable {

    private String name;
    private String status;
    private String receivedDate;
    private String roundId;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getRoundId() {
        return roundId;
    }

    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.status);
        dest.writeString(this.receivedDate);
        dest.writeString(this.roundId);
        dest.writeInt(this.id);
    }

    public AbsentKidsBean() {
    }

    protected AbsentKidsBean(Parcel in) {
        this.name = in.readString();
        this.status = in.readString();
        this.receivedDate = in.readString();
        this.roundId = in.readString();
        this.id = in.readInt();
    }

    public static final Creator<AbsentKidsBean> CREATOR = new Creator<AbsentKidsBean>() {
        @Override
        public AbsentKidsBean createFromParcel(Parcel source) {
            return new AbsentKidsBean(source);
        }

        @Override
        public AbsentKidsBean[] newArray(int size) {
            return new AbsentKidsBean[size];
        }
    };
}
