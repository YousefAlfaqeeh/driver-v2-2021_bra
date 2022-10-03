package schooldriver.trackware.com.school_bus_driver_android.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import schooldriver.trackware.com.school_bus_driver_android.BuildConfig;

public class ApplicationVersionJson implements Parcelable {


    @SerializedName("latest_version_code")
    private int latestVersionCode;
    @SerializedName("latest_version_name")
    private String latestVersionName;
    @SerializedName("google_play_link")
    private String googlePlayLink;
    @SerializedName("update_message_ar")
    private String updateMessageAr;
    @SerializedName("update_message_en")
    private String updateMessageEn;
    @SerializedName("minimumAllowedVersion")
    private int minimumAllowedVersion;

    public int getLatestVersionCode() {
        return latestVersionCode;
    }

    public String getLatestVersionName() {
        return latestVersionName;
    }

    public String getGooglePlayLink() {
        return googlePlayLink;
    }

    public String getUpdateMessageAr() {
        return updateMessageAr;
    }

    public String getUpdateMessageEn() {
        return updateMessageEn;
    }

    public int getForceUpdateForCode() {
        return minimumAllowedVersion;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.latestVersionCode);
        dest.writeString(this.latestVersionName);
        dest.writeString(this.googlePlayLink);
        dest.writeString(this.updateMessageAr);
        dest.writeString(this.updateMessageEn);
        dest.writeInt(this.minimumAllowedVersion);
    }

    public ApplicationVersionJson() {
    }

    protected ApplicationVersionJson(Parcel in) {
        this.latestVersionCode = in.readInt();
        this.latestVersionName = in.readString();
        this.googlePlayLink = in.readString();
        this.updateMessageAr = in.readString();
        this.updateMessageEn = in.readString();
        this.minimumAllowedVersion = in.readInt();
    }

    public static final Parcelable.Creator<ApplicationVersionJson> CREATOR = new Parcelable.Creator<ApplicationVersionJson>() {
        @Override
        public ApplicationVersionJson createFromParcel(Parcel source) {
            return new ApplicationVersionJson(source);
        }

        @Override
        public ApplicationVersionJson[] newArray(int size) {
            return new ApplicationVersionJson[size];
        }
    };


    public boolean checkIfShouldUpdate() {
        int currentVersionCode = BuildConfig.VERSION_CODE;
        return currentVersionCode < minimumAllowedVersion;
    }

    public boolean checkIfAvailableUpdate() {
        int currentVersionCode = BuildConfig.VERSION_CODE;
        return currentVersionCode < latestVersionCode;
    }
}
