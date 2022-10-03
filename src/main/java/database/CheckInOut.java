package database;


public class CheckInOut {

    private int checkid;

    private String message;

    private String data;

    private String date;

    public CheckInOut(String message, String date,String data) {
        this.message = message;
        this.data = data;
        this.date = date;
    }

    public int getCheckid() {
        return checkid;
    }


    public String getMessage() {
        return message;
    }

    public CheckInOut setMessage(String message) {
        this.message = message;
        return CheckInOut.this;

    }

    public String getData() {
        return data;
    }

    public CheckInOut setData(String data) {
        this.data = data;
        return CheckInOut.this;

    }

    public String getDate() {
        return date;
    }

    public CheckInOut setDate(String date) {
        this.date = date;
        return CheckInOut.this;

    }


}