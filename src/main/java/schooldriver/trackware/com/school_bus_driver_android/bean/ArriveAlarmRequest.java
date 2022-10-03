package schooldriver.trackware.com.school_bus_driver_android.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArriveAlarmRequest implements Parcelable {

    @SerializedName("notification_type")
    @Expose
    private String notificationType = "arrive_alarm";
    @SerializedName("student_id")
    @Expose
    private Integer studentId;
    @SerializedName("round_type")
    @Expose
    private String round_type = "";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.notificationType);
        dest.writeValue(this.studentId);
        dest.writeString(this.round_type);
    }

    public ArriveAlarmRequest() {
    }

    protected ArriveAlarmRequest(Parcel in) {
        this.notificationType = in.readString();
        this.studentId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.round_type = in.readString();
    }

    public static final Creator<ArriveAlarmRequest> CREATOR = new Creator<ArriveAlarmRequest>() {
        @Override
        public ArriveAlarmRequest createFromParcel(Parcel source) {
            return new ArriveAlarmRequest(source);
        }

        @Override
        public ArriveAlarmRequest[] newArray(int size) {
            return new ArriveAlarmRequest[size];
        }
    };

    public String getNotificationType() {
        return notificationType;
    }

    public ArriveAlarmRequest setNotificationType(String notificationType) {
        this.notificationType = notificationType;
        return this;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public ArriveAlarmRequest setStudentId(Integer studentId) {
        this.studentId = studentId;
        return this;
    }

    public String getRound_type() {
        return round_type;
    }

    public ArriveAlarmRequest setRound_type(String round_type) {
        this.round_type = round_type;
        return this;
    }
}
