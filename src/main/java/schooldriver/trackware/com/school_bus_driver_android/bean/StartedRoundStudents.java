package schooldriver.trackware.com.school_bus_driver_android.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class StartedRoundStudents implements Parcelable {


    private Integer orderInList;
    private Integer studentID;
    private Integer roundID;

    public StartedRoundStudents(Integer orderInList, Integer studentID, Integer roundID) {
        this.orderInList = orderInList;
        this.studentID = studentID;
        this.roundID = roundID;
    }

    public Integer getOrderInList() {
        return orderInList;
    }

    public void setOrderInList(Integer orderInList) {
        this.orderInList = orderInList;
    }

    public Integer getStudentID() {
        return studentID;
    }

    public void setStudentID(Integer studentID) {
        this.studentID = studentID;
    }

    public Integer getRoundID() {
        return roundID;
    }

    public void setRoundID(Integer roundID) {
        this.roundID = roundID;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.orderInList);
        dest.writeValue(this.studentID);
        dest.writeValue(this.roundID);
    }

    protected StartedRoundStudents(Parcel in) {
        this.orderInList = (Integer) in.readValue(Integer.class.getClassLoader());
        this.studentID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.roundID = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<StartedRoundStudents> CREATOR = new Parcelable.Creator<StartedRoundStudents>() {
        @Override
        public StartedRoundStudents createFromParcel(Parcel source) {
            return new StartedRoundStudents(source);
        }

        @Override
        public StartedRoundStudents[] newArray(int size) {
            return new StartedRoundStudents[size];
        }
    };
}
