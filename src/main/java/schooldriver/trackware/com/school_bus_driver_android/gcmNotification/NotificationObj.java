package schooldriver.trackware.com.school_bus_driver_android.gcmNotification;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NotificationObj implements Parcelable {

    private final String actionTypeSchool = "school";
    private final String actionTypeUpdate = "update";
    private final String actionTypeDriver = "driver";
    private final String actionTypeNear = "near";
    private final String actionTypeOther = "other";

    //
    public enum NotificationType {
        SCHOOL,
        UPDATE,
        DRIVER,
        NEAR,
        OTHER
    }


    @SerializedName("action")
    private String action;

//    @SerializedName("avatar")
//    private String avatar;

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;
    /**/
    private String id;

    public NotificationType getType() {
        if (action == null)
            return NotificationType.OTHER;
        /**/
        switch (action) {
            case actionTypeSchool:
                return NotificationType.SCHOOL;
            case actionTypeUpdate:
                return NotificationType.UPDATE;
            case actionTypeDriver:
                return NotificationType.DRIVER;
            case actionTypeNear:
                return NotificationType.NEAR;
            case actionTypeOther:
                return NotificationType.OTHER;

            default:
                return NotificationType.OTHER;
        }
        /**/
    }


//    {"title":null,"message":null,"avatar":null,"action":"update"}
//    {"title":"this is a title","message":"body","avatar":"","action":"school"}
//    {"title":"this is a title","message":"body","avatar":"","action":"driver"}
//    {"title":"this is a title","message":"body","avatar":"","action":"near"}
//    {"title":"this is a title","message":"body","avatar":"","action":"other"}


    public String getAction() {
        return action;
    }

    public NotificationObj setAction(String action) {
        this.action = action;
        return this;
    }

//    public String getAvatar() {
//        return avatar;
//    }

//    public NotificationObj setAvatar(String avatar) {
//        this.avatar = avatar;
//        return this;
//    }

    public String getTitle() {
        return title;
    }

    public NotificationObj setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public NotificationObj setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getId() {
        return id;
    }

    public NotificationObj setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.action);
//        dest.writeString(this.avatar);
        dest.writeString(this.title);
        dest.writeString(this.message);
        dest.writeString(this.id);

    }

    public NotificationObj() {
    }

    protected NotificationObj(Parcel in) {
        this.action = in.readString();
//        this.avatar = in.readString();
        this.title = in.readString();
        this.message = in.readString();
        this.id = in.readString();

    }

    public static final Creator<NotificationObj> CREATOR = new Creator<NotificationObj>() {
        @Override
        public NotificationObj createFromParcel(Parcel source) {
            return new NotificationObj(source);
        }

        @Override
        public NotificationObj[] newArray(int size) {
            return new NotificationObj[size];
        }
    };
}