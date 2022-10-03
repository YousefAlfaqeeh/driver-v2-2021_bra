package database;


public class LonLat {

    private int LonLatId;

    private String longitude;

    private String latitude;

    private String date;

//    private String speed;


    public LonLat(String longitude, String latitude, String date/*,String speed*/) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
//        this.speed=speed;
    }

//    public LonLat(String longitude, String latitude, String date/*,double dSpeed*/) {
//        this.longitude = longitude;
//        this.latitude = latitude;
//        this.date = date;
////        this.speed=dSpeed+"";
//    }


    public int getLonLatId() {
        return LonLatId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    public String getSpeed() {
//        return speed;
//    }
//
//    public LonLat setSpeed(String speed) {
//        this.speed = speed;
//        return this;
//    }
//
//    public LonLat setSpeed(double dSpeed) {
//        this.speed = dSpeed+"";
//        return this;
//    }

}