package schooldriver.trackware.com.school_bus_driver_android.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.app.Application;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * Created by muradtrac on 3/26/17.
 */

public class ParentStudentBean implements Serializable, Parcelable {

    private String fatherNumber;
    private String fatherToken;
    private String motherNumber;
    private String motherToken;
    private boolean checkInFather;
    private boolean checkOutFather;
    private boolean checkInMother;
    private boolean checkOutMother;
    private boolean nearbyFather;
    private boolean nearbyMother;
    private String fatherPlatform;
    private String motherPlatform;
    private String fatherLocal;
    private String motherLocal;

    private int fatherId;
    private int motherId;

    public int getFatherId() {
        return fatherId;
    }

    public ParentStudentBean setFatherId(int fatherId) {
        this.fatherId = fatherId;
        return this;
    }

    public int getMotherId() {
        return motherId;
    }

    public ParentStudentBean setMotherId(int motherId) {
        this.motherId = motherId;
        return this;
    }

    public String getMotherNumber() {
        if (motherNumber==null)
            return "";

        return motherNumber;
    }

    public void setMotherNumber(String motherNumber) {
        this.motherNumber = motherNumber;
    }

    public String getMotherToken() {
        return motherToken;
    }

    public void setMotherToken(String motherToken) {
        this.motherToken = motherToken;
    }

    public String getFatherNumber() {
        if (fatherNumber==null)
            return "";

        return fatherNumber;
    }

    public boolean hasFatherOrMotherNumber(){
        try{
            return !(getFatherNumber().trim().isEmpty() && getMotherNumber().trim().isEmpty());
        }catch (Exception e){
            return false;
        }

    }

    public void setFatherNumber(String fatherNumber) {
        this.fatherNumber = fatherNumber;
    }

    public String getFatherToken() {
        return fatherToken;
    }

    public void setFatherToken(String fatherToken) {
        this.fatherToken = fatherToken;
    }


    public boolean isCheckOutFather() {
        return checkOutFather;
    }

    public boolean isCheckInFather() {
        return checkInFather;
    }

    public void setCheckInFather(boolean checkInFather) {
        this.checkInFather = checkInFather;
    }

    public void setCheckOutFather(boolean checkOutFather) {
        this.checkOutFather = checkOutFather;
    }

    public boolean isCheckInMother() {
        return checkInMother;
    }

    public void setCheckInMother(boolean checkInMother) {
        this.checkInMother = checkInMother;
    }

    public boolean isCheckOutMother() {
        return checkOutMother;
    }

    public void setCheckOutMother(boolean checkOutMother) {
        this.checkOutMother = checkOutMother;
    }

    public boolean isNearbyFather() {
        return nearbyFather;
    }

    public void setNearbyFather(boolean nearbyFather) {
        this.nearbyFather = nearbyFather;
    }

    public boolean isNearbyMother() {
        return nearbyMother;
    }

    public void setNearbyMother(boolean nearbyMother) {
        this.nearbyMother = nearbyMother;
    }

//    public String getPlatform() {
//        return platform;
//    }

    public void setFatherPlatform(String fatherPlatform) {
        this.fatherPlatform = fatherPlatform;
    }

    public void setMotherPlatform(String motherPlatform) {
        this.motherPlatform = motherPlatform;
    }

    public String getFatherLocal() {
        return fatherLocal;
    }

    public ParentStudentBean setFatherLocal(String fatherLocal) {
        this.fatherLocal = fatherLocal;
        return this;
    }

    public String getMotherLocal() {
        return motherLocal;
    }

    public ParentStudentBean setMotherLocal(String motherLocal) {
        this.motherLocal = motherLocal;
        return this;
    }

    public String getFatherPlatform() {
        return fatherPlatform;
    }

    public String getMotherPlatform() {
        return motherPlatform;
    }



    public String getFatherMessageType(Map<String, String> mapBodyMessage, String msg) {
        try {
            if (!UtilityDriver.isEmptyString(getFatherPlatform()) && getFatherPlatform().contains("android")) {
                return new JSONObject(mapBodyMessage).toString();
            } else {
                return UtilityDriver.MESSAGE_IPHONE.replaceAll("MESSAGEMESSAGE", msg);
            }
        } catch (Exception e) {
            return "";
        }

    }



//    public String getFatherMessageTitle() {
//        return createMessageTitle(getFatherLocal());
//    }
//
//    public String getMotherMessageTitle() {
//        return createMessageTitle(getMotherLocal());
//    }



//    public String getFatherPreDescription(String studentName) {
//        return createMessagePreDescription(getFatherLocal(),studentName);
//    }
//
//    public String getMotherPreDescription(String studentName) {
//        return createMessagePreDescription(getMotherLocal(),studentName);
//    }




    public String getMotherMessageType(Map<String, String> mapBodyMessage, String msg) {

        try {
            if (!UtilityDriver.isEmptyString(getMotherPlatform()) && getMotherPlatform().contains("android")) {
                return new JSONObject(mapBodyMessage).toString();
            } else {
                return UtilityDriver.MESSAGE_IPHONE.replaceAll("MESSAGEMESSAGE", msg);
            }
        } catch (Exception e) {
            return "";
        }

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fatherNumber);
        dest.writeString(this.fatherToken);
        dest.writeString(this.motherNumber);
        dest.writeString(this.motherToken);
        dest.writeByte(this.checkInFather ? (byte) 1 : (byte) 0);
        dest.writeByte(this.checkOutFather ? (byte) 1 : (byte) 0);
        dest.writeByte(this.checkInMother ? (byte) 1 : (byte) 0);
        dest.writeByte(this.checkOutMother ? (byte) 1 : (byte) 0);
        dest.writeByte(this.nearbyFather ? (byte) 1 : (byte) 0);
        dest.writeByte(this.nearbyMother ? (byte) 1 : (byte) 0);
        dest.writeString(this.fatherPlatform);
        dest.writeString(this.motherPlatform);
        dest.writeString(this.fatherLocal);
        dest.writeString(this.motherLocal);
        dest.writeInt(this.fatherId);
        dest.writeInt(this.motherId);
    }

    public ParentStudentBean() {
    }

    protected ParentStudentBean(Parcel in) {
        this.fatherNumber = in.readString();
        this.fatherToken = in.readString();
        this.motherNumber = in.readString();
        this.motherToken = in.readString();
        this.checkInFather = in.readByte() != 0;
        this.checkOutFather = in.readByte() != 0;
        this.checkInMother = in.readByte() != 0;
        this.checkOutMother = in.readByte() != 0;
        this.nearbyFather = in.readByte() != 0;
        this.nearbyMother = in.readByte() != 0;
        this.fatherPlatform = in.readString();
        this.motherPlatform = in.readString();
        this.fatherLocal = in.readString();
        this.motherLocal = in.readString();
        this.fatherId = in.readInt();
        this.motherId = in.readInt();
    }

    public static final Creator<ParentStudentBean> CREATOR = new Creator<ParentStudentBean>() {
        @Override
        public ParentStudentBean createFromParcel(Parcel source) {
            return new ParentStudentBean(source);
        }

        @Override
        public ParentStudentBean[] newArray(int size) {
            return new ParentStudentBean[size];
        }
    };
}
