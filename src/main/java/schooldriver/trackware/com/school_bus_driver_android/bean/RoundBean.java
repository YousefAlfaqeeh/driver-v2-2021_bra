package schooldriver.trackware.com.school_bus_driver_android.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import schooldriver.trackware.com.school_bus_driver_android.enums.StatusRoundEnum;
import schooldriver.trackware.com.school_bus_driver_android.enums.TypeRoundEnum;

/**
 * Created by Ibrahem Al-Betar on 3/1/2017.
 */

public class RoundBean /* extends  BaseBean<Integer> */ implements Parcelable, Serializable {
    private String nameRound;
    private String dateTime;
    private StatusRoundEnum statusRoundEnum;
    private List<StudentBean> listStudentBean = new ArrayList<>();
    private TypeRoundEnum typeRoundEnum;
    private boolean roundsStart;
    private  boolean roundsEnd;
    private boolean round_canceled;
     private String geofence;
    private boolean changeStudentLocation;
    Map<Integer, List<StudentBean>> mapStudentBean = new HashMap<>();
    private int id;
    private ArrayList<String> movedStudentsList;

    private AttendantBean attendantBean;

    public AttendantBean getAttendantBean() {
        return attendantBean;
    }

    public RoundBean setAttendantBean(AttendantBean attendantBean) {
        this.attendantBean = attendantBean;
        return this;
    }

    public int getId() {
        return id;
    }
    public String getIdAsString() {
        return id+"";
    }
    public void setId(int id) {
        this.id = id;
    }


    public Map<Integer, List<StudentBean>> getMapStudentBean() {
        if (mapStudentBean==null)
            return new HashMap<>();

        return mapStudentBean;
    }

    public void setMapStudentBean(Map<Integer, List<StudentBean>> mapStudentBean) {
        this.mapStudentBean = mapStudentBean;
    }

    public RoundBean() {


    }

    public ArrayList<String> getMovedStudentsList() {
        return movedStudentsList;
    }

    public RoundBean setMovedStudentsList(ArrayList<String> movedStudentsList) {
        this.movedStudentsList = movedStudentsList;
        return this;
    }

    public String getGeofence() {
        return geofence;
    }

    public void setGeofence(String geofence) {
        this.geofence = geofence;
    }

    public String getNameRound() {
        return nameRound;
    }

    public void setNameRound(String nameRound) {
        this.nameRound = nameRound;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<StudentBean> getListStudentBean() {
        return listStudentBean;
    }

//    public List<Integer> getListStudentBeanIds() {
//        ArrayList<Integer> ids= new ArrayList<>();
//        for (int i = 0; i < listStudentBean.size(); i++) {
//            ids.add(listStudentBean.get(i).getId());
//        }
//        return ids;
//    }


    public void setListStudentBean(List<StudentBean> listStudentBean) {
        this.listStudentBean = listStudentBean;
    }

//    public boolean isRound_canceled() {
//        return round_canceled;
//    }

    public RoundBean setRound_canceled(boolean round_canceled) {
        this.round_canceled = round_canceled;
        return this;
    }

    public StatusRoundEnum getStatusRoundEnum() {
        return statusRoundEnum;
    }

    public void setStatusRoundEnum(StatusRoundEnum statusRoundEnum) {
        this.statusRoundEnum = statusRoundEnum;
    }


    public TypeRoundEnum getTypeRoundEnum() {
        return typeRoundEnum;
    }

    public void setTypeRoundEnum(TypeRoundEnum typeRoundEnum) {
        this.typeRoundEnum = typeRoundEnum;
    }

//    public boolean isRoundsStart() {
//        return roundsStart;
//    }

    public void setRoundsStart(boolean roundsStart) {
        this.roundsStart = roundsStart;
    }

//    public boolean isRoundsEnd() {
//        return roundsEnd;
//    }

    public void setRoundsEnd(boolean roundsEnd) {
        this.roundsEnd = roundsEnd;
    }

    public boolean isChangeStudentLocation() {
        return changeStudentLocation;
    }

    public void setChangeStudentLocation(boolean changeStudentLocation) {
        this.changeStudentLocation = changeStudentLocation;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nameRound);
        dest.writeString(this.dateTime);
        dest.writeInt(this.statusRoundEnum == null ? -1 : this.statusRoundEnum.ordinal());
        dest.writeTypedList(this.listStudentBean);
        dest.writeInt(this.typeRoundEnum == null ? -1 : this.typeRoundEnum.ordinal());
        dest.writeByte(this.roundsStart ? (byte) 1 : (byte) 0);
        dest.writeByte(this.roundsEnd ? (byte) 1 : (byte) 0);
        dest.writeByte(this.round_canceled ? (byte) 1 : (byte) 0);
        dest.writeString(this.geofence);
        dest.writeByte(this.changeStudentLocation ? (byte) 1 : (byte) 0);
        dest.writeInt(this.getMapStudentBean().size());
        for (Map.Entry<Integer, List<StudentBean>> entry : this.mapStudentBean.entrySet()) {
            dest.writeValue(entry.getKey());
            dest.writeTypedList(entry.getValue());
        }
        dest.writeInt(this.id);
        dest.writeStringList(this.movedStudentsList);
        dest.writeParcelable(this.attendantBean, flags);
    }

    protected RoundBean(Parcel in) {
        this.nameRound = in.readString();
        this.dateTime = in.readString();
        int tmpStatusRoundEnum = in.readInt();
        this.statusRoundEnum = tmpStatusRoundEnum == -1 ? null : StatusRoundEnum.values()[tmpStatusRoundEnum];
        this.listStudentBean = in.createTypedArrayList(StudentBean.CREATOR);
        int tmpTypeRoundEnum = in.readInt();
        this.typeRoundEnum = tmpTypeRoundEnum == -1 ? null : TypeRoundEnum.values()[tmpTypeRoundEnum];
        this.roundsStart = in.readByte() != 0;
        this.roundsEnd = in.readByte() != 0;
        this.round_canceled = in.readByte() != 0;
        this.geofence = in.readString();
        this.changeStudentLocation = in.readByte() != 0;
        int mapStudentBeanSize = in.readInt();
        this.mapStudentBean = new HashMap<Integer, List<StudentBean>>(mapStudentBeanSize);
        for (int i = 0; i < mapStudentBeanSize; i++) {
            Integer key = (Integer) in.readValue(Integer.class.getClassLoader());
            List<StudentBean> value = in.createTypedArrayList(StudentBean.CREATOR);
            this.mapStudentBean.put(key, value);
        }
        this.id = in.readInt();
        this.movedStudentsList = in.createStringArrayList();
        this.attendantBean = in.readParcelable(AttendantBean.class.getClassLoader());
    }

    public static final Creator<RoundBean> CREATOR = new Creator<RoundBean>() {
        @Override
        public RoundBean createFromParcel(Parcel source) {
            return new RoundBean(source);
        }

        @Override
        public RoundBean[] newArray(int size) {
            return new RoundBean[size];
        }
    };




    public boolean isRoundStartedNow() {
        return roundsStart && !roundsEnd && !round_canceled;
    }

    public void startRoundNow() {
        roundsStart = true;
        roundsEnd = false;
        round_canceled = false;
    }

    public boolean isRoundFirstTime() {
        return !roundsStart && !roundsEnd && !round_canceled;
    }

    public boolean isRoundEndedForEver() {
        return !roundsStart && roundsEnd && !round_canceled;
    }

    public void endRoundForever() {
        roundsStart = false;
        roundsEnd = true;
        round_canceled = false;
    }

    public boolean isRoundPaused() {
        return !roundsStart && !roundsEnd && round_canceled;
    }

}
