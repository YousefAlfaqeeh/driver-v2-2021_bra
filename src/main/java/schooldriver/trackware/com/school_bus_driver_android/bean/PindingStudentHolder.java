package schooldriver.trackware.com.school_bus_driver_android.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by M.Bader on 1/3/2018.
 */

public class PindingStudentHolder implements Parcelable {

    private StudentBean studentBean;
    private String reson = "";
    private boolean byBeacon = false;

    public StudentBean getStudentBean() {
        return studentBean;
    }

    public PindingStudentHolder setStudentBean(StudentBean studentBean) {
        this.studentBean = studentBean;
        return this;
    }

    public String getReson() {
        return reson;
    }

    public PindingStudentHolder setReson(String reson) {
        this.reson = reson;
        return this;
    }

    public boolean isByBeacon() {
        return byBeacon;
    }

    public PindingStudentHolder setByBeacon(boolean byBeacon) {
        this.byBeacon = byBeacon;
        return this;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.studentBean, flags);
        dest.writeString(this.reson);
        dest.writeByte(this.byBeacon ? (byte) 1 : (byte) 0);
    }

    public PindingStudentHolder() {
    }

    protected PindingStudentHolder(Parcel in) {
        this.studentBean = in.readParcelable(StudentBean.class.getClassLoader());
        this.reson = in.readString();
        this.byBeacon = in.readByte() != 0;
    }

    public static final Creator<PindingStudentHolder> CREATOR = new Creator<PindingStudentHolder>() {
        @Override
        public PindingStudentHolder createFromParcel(Parcel source) {
            return new PindingStudentHolder(source);
        }

        @Override
        public PindingStudentHolder[] newArray(int size) {
            return new PindingStudentHolder[size];
        }
    };
}
