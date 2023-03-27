package schooldriver.trackware.com.school_bus_driver_android.bean;

import android.os.Parcel;
import android.os.Parcelable;

import schooldriver.trackware.com.school_bus_driver_android.enums.CheckEnum;
import schooldriver.trackware.com.school_bus_driver_android.enums.TypeRoundEnum;

/**
 * Created   on 2/28/2017.
 */

public class StudentBean implements Parcelable, Comparable<StudentBean> /*extends BaseBean<Integer>*/ {


    private String nameStudent;
    private String grade;
    private String avatar;

    private CheckEnum checkEnum;
    private TypeRoundEnum typeRoundEnum;
    private boolean absent;
    private boolean show;
    private double latitude;
    private double longitude;
    private int id;
    private int group;
    private String macAdress = "";
    private boolean checkedByBeacon = false;
    private int beaconBattery = -1;

    public String getMacAdress() {
        return macAdress;
    }

    public void setMacAdress(String macAdress) {
        this.macAdress = macAdress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    private ParentStudentBean mobileStudentBean;

    public StudentBean() {
    }

    public ParentStudentBean getMobileStudentBean() {
        return mobileStudentBean;
    }

    public void setMobileStudentBean(ParentStudentBean mobileStudentBean) {
        this.mobileStudentBean = mobileStudentBean;
    }

    public String getNameStudent() {
        return nameStudent;
    }

    public String getFirstNameStudent() {
        try {
            return getNameStudent().split(" ")[0];
        } catch (Exception e) {
            return getNameStudent();
        }

    }

    public void setNameStudent(String nameStudent) {
        this.nameStudent = nameStudent;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public CheckEnum getCheckEnum() {
        return checkEnum;
    }

    public void setCheckEnum(CheckEnum checkEnum) {
        this.checkEnum = checkEnum;
    }

    public boolean isAbsent() {
        return absent;
    }

    public void setAbsent(boolean absent) {
        this.absent = absent;
    }

    public boolean isNoShow() {
        return show;
    }

    public void setIsNoShow(boolean show) {
        this.show = show;
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

    public TypeRoundEnum getTypeRoundEnum() {
        return typeRoundEnum;
    }

    public void setTypeRoundEnum(TypeRoundEnum typeRoundEnum) {
        this.typeRoundEnum = typeRoundEnum;
    }

    public String getAvatar() {
        if (avatar == null)
            return "";

        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isCheckedByBeacon() {
        return checkedByBeacon;
    }

    public void setCheckedByBeacon(boolean checkedByBeacon) {
        this.checkedByBeacon = checkedByBeacon;
    }


    @Override
    public int compareTo(StudentBean studentBean) {

        if (studentBean.isAbsent() || studentBean.isNoShow()) {
            return 1;
        } else {
            return -1;
        }
//          if (studentBean.getCheckEnum() ==CheckEnum.CHECK_IN || studentBean.getCheckEnum() ==CheckEnum.CHECK_OUT) {
//            return 1;
//        }else if(studentBean.isAbsent() || studentBean.isNoShow()){
//              return 2;
//          }

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nameStudent);
        dest.writeString(this.grade);
        dest.writeString(this.avatar);
        dest.writeInt(this.checkEnum == null ? -1 : this.checkEnum.ordinal());
        dest.writeInt(this.typeRoundEnum == null ? -1 : this.typeRoundEnum.ordinal());
        dest.writeByte(this.absent ? (byte) 1 : (byte) 0);
        dest.writeByte(this.show ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeInt(this.id);
        dest.writeInt(this.group);
        dest.writeString(this.macAdress);
        dest.writeByte(this.checkedByBeacon ? (byte) 1 : (byte) 0);
        dest.writeInt(this.beaconBattery);
        dest.writeParcelable(this.mobileStudentBean, flags);
    }

    protected StudentBean(Parcel in) {
        this.nameStudent = in.readString();
        this.grade = in.readString();
        this.avatar = in.readString();
        int tmpCheckEnum = in.readInt();
        this.checkEnum = tmpCheckEnum == -1 ? null : CheckEnum.values()[tmpCheckEnum];
        int tmpTypeRoundEnum = in.readInt();
        this.typeRoundEnum = tmpTypeRoundEnum == -1 ? null : TypeRoundEnum.values()[tmpTypeRoundEnum];
        this.absent = in.readByte() != 0;
        this.show = in.readByte() != 0;
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.id = in.readInt();
        this.group = in.readInt();
        this.macAdress = in.readString();
        this.checkedByBeacon = in.readByte() != 0;
        this.beaconBattery = in.readInt();
        this.mobileStudentBean = in.readParcelable(ParentStudentBean.class.getClassLoader());
    }

    public static final Creator<StudentBean> CREATOR = new Creator<StudentBean>() {
        @Override
        public StudentBean createFromParcel(Parcel source) {
            return new StudentBean(source);
        }

        @Override
        public StudentBean[] newArray(int size) {
            return new StudentBean[size];
        }
    };
}
