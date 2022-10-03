package database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import schooldriver.trackware.com.school_bus_driver_android.app.Application;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.RoundInfoFragment;
import schooldriver.trackware.com.school_bus_driver_android.gcmNotification.NotificationObj;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.MyGson;


public class DAO {

    public static final String TAG = DAO.class.getSimpleName();

    public static final String CheckInOut_TBL = "CheckInOut";
    //    public static final String CheckInOut_checkid = "checkid";
    public static final String CheckInOut_message = "message";
    public static final String CheckInOut_data = "data";
    public static final String CheckInOut_date = "date";
    /**/
//    public static final String ROUND_LIST_TBL = "tb_round_list";
//    public static final String ROUND_LIST_DATA = "data";

    /**/
//    public static final String StudentsOfStartedRound_TBL = "StudentsOfStartedRound";

//    public static final String studentID_ = "studentID";
//    public static final String roundID_ = "roundID";
//    public static final String roundType_ = "roundType";
//    public static final String orderInList_ = "orderInList";
//    public static final String nameStudent_ = "nameStudent";
//    public static final String grade_ = "grade";
//    public static final String avatar_ = "avatar";
//    public static final String checkEnum_ = "checkEnum";
//    public static final String typeRoundEnum_ = "typeRoundEnum";
//    public static final String isAbsent_ = "isAbsent";
//    public static final String isNoShow_ = "isNoShow";
//    public static final String latitude_ = "latitude";
//    public static final String longitude_ = "longitude";

    /**/


//    private interface NotificationTable {
//        String TABLE_NAME = "Notifications";//table name
//        /**/
//        String Notifications_TBL_Notification_ID = "Notification_ID";
//        String Student_id = "Student_id";
//        String Round_id = "Round_id";
//        String StudentName = "Name";
//        String Status = "Status";
//        String DateTime = "DateTime";
//
//
//    }


    //    CREATE TABLE `tbl_notifications` ( `notification_id` TEXT NOT NULL, `notification_data` TEXT NOT NULL, `notification_type` TEXT NOT NULL )
    public static class ImportantNotificationTable {
        private static final String table_name = "tbl_notifications";
        private static final String notification_id = "notification_id";
        private static final String notification_data = "notification_data";
        private static final String notification_type = "notification_type";


        public static NotificationObj getOne(SQLiteDatabase database) {
            try {
                Cursor cursor = database.rawQuery("select * FROM " + table_name, null);
                if (cursor.moveToFirst()) {
                    do {
                        String id = cursor.getString(cursor.getColumnIndex(notification_id));
                        String type = cursor.getString(cursor.getColumnIndex(notification_type));
                        String data = cursor.getString(cursor.getColumnIndex(notification_data));
                        NotificationObj notificationObj = MyGson.getGson().fromJson(data, NotificationObj.class);
                        notificationObj.setId(id);
                        cursor.close();
                        return notificationObj;


                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {

            }

            return null;

        }


//        public static ArrayList<NotificationObj> getAll(SQLiteDatabase database) {
//            Cursor cursor = database.rawQuery("select * FROM " + table_name, null);
//            ArrayList<NotificationObj> dataList = new ArrayList<NotificationObj>();
//            if (cursor.moveToFirst()) {
//                do {
//                    try {
//                        String id = cursor.getString(cursor.getColumnIndex(notification_id));
//                        String type = cursor.getString(cursor.getColumnIndex(notification_type));
//                        String data = cursor.getString(cursor.getColumnIndex(notification_data));
//                        NotificationObj resp = MyGson.getGson().fromJson(data, NotificationObj.class);
//                        dataList.add(resp);
//                    } catch (Exception e) {
//
//                    }
//
//
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//            return dataList;
//
//        }

        public static boolean delete(SQLiteDatabase database, String id) {
            try {
                if (id == null) {
                    return false;
                }
                database.beginTransaction();
                database.delete(table_name, notification_id + " = ?", new String[]{id});
                database.setTransactionSuccessful();
                database.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                return false;

            }

            return true;
        }


        public static void deleteAll(SQLiteDatabase database) {
            database.beginTransaction();
            database.delete(table_name, null, null);
            database.setTransactionSuccessful();
            database.endTransaction();
        }

        public static boolean add(SQLiteDatabase database, NotificationObj notificationObj) {
            try {
                String data = MyGson.getGson().toJson(notificationObj).toString();
                database.beginTransaction();

                ContentValues values = new ContentValues();
                /**/
                /**/
                CursorUtil.putObject(values, notification_id, new Random().nextInt(9999 - 1000) + 1000);
                CursorUtil.putObject(values, notification_data, data);
                CursorUtil.putObject(values, notification_type, notificationObj.getAction());

                database.insert(table_name, null, values);


                database.setTransactionSuccessful();
                database.endTransaction();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;

            }

        }


    }


//    public static ArrayList<AbsentKidsBean> getNotificationsFor(SQLiteDatabase database, int roundId) {
//        Cursor cursor = database.rawQuery("SELECT * FROM " + NotificationTable.TABLE_NAME + " where "+NotificationTable.Round_id+" ="+roundId, null);
//        ArrayList<AbsentKidsBean> absentKidsBeanList = new ArrayList<>();
//        try {
//            if (cursor.moveToFirst()) {
//                do {
//
//
//                    Integer student_id = cursor.getInt(cursor.getColumnIndex(NotificationTable.Student_id));
//                    Integer round_id = cursor.getInt(cursor.getColumnIndex(NotificationTable.Round_id));
//                    String studentName = cursor.getString(cursor.getColumnIndex(NotificationTable.StudentName));
//                    String status = cursor.getString(cursor.getColumnIndex(NotificationTable.Status));
//                    String dateTime = cursor.getString(cursor.getColumnIndex(NotificationTable.DateTime));
//
//
//
//                    started_round_students.add(studentID);
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("sortStudentUsingSaved", "sortStudentUsingSaved");
//        }
//
//        return started_round_students;
//
//    }

    /**/


    /**/

//
//    public static final String LonLat_TBL = "LonLat";
//    //    public static final String LonLat_LonLatId = "LonLatId";
//    public static final String LonLat_longitude = "longitude";
//    public static final String LonLat_latitude = "latitude";
//    public static final String LonLat_date = "date";
//    public static final String SPEED = "speed";

//    public static ArrayList<Integer> getStartedRoundStudents(SQLiteDatabase database) {
//        Cursor cursor = database.rawQuery("select * FROM " + StudentsOfStartedRound_TBL, null);
//        ArrayList<Integer> started_round_students = new ArrayList<>();
//        try {
//
//
//            if (cursor.moveToFirst()) {
//                do {
//
//                    Integer studentID = cursor.getInt(cursor.getColumnIndex(studentID_));
////                    String roundID = cursor.getString(cursor.getColumnIndex(roundID_)).trim();
////                    String roundType = cursor.getString(cursor.getColumnIndex(roundType_)).trim();
////                    String orderInList = cursor.getString(cursor.getColumnIndex(orderInList_)).trim();
////                    String nameStudent = cursor.getString(cursor.getColumnIndex(nameStudent_)).trim();
////                    String grade = cursor.getString(cursor.getColumnIndex(grade_)).trim();
////                    String avatar = cursor.getString(cursor.getColumnIndex(avatar_)).trim();
////                    String checkEnum = cursor.getString(cursor.getColumnIndex(checkEnum_)).trim();
////                    String typeRoundEnum = cursor.getString(cursor.getColumnIndex(typeRoundEnum_)).trim();
////                    String isAbsent = cursor.getString(cursor.getColumnIndex(isAbsent_)).trim();
////                    String isNoShow = cursor.getString(cursor.getColumnIndex(isNoShow_)).trim();
////                    String latitude = cursor.getString(cursor.getColumnIndex(latitude_)).trim();
////                    String longitude = cursor.getString(cursor.getColumnIndex(longitude_)).trim();
//
//                    /**/
////                    StudentBean studentBean = new StudentBean();
////                    studentBean.setId( Integer.parseInt(studentID));
////                    studentBean.setNameStudent(nameStudent);
////                    studentBean.setGrade(grade);
////                    studentBean.setAvatar(avatar);
////                    studentBean.setCheckEnum(checkEnum.equals("CHECK_IN") ? CheckEnum.CHECK_IN : CheckEnum.CHECK_OUT);
////                    studentBean.setTypeRoundEnum(typeRoundEnum.equals("PICK_ROUND") ? TypeRoundEnum.PICK_ROUND : TypeRoundEnum.DROP_ROUND);
////                    studentBean.setAbsent(isAbsent.equals("true"));
////                    studentBean.setIsNoShow(isNoShow.equals("true"));
////                    studentBean.setLatitude(Double.parseDouble(latitude));
////                    studentBean.setLongitude(Double.parseDouble(longitude));
//
//
//                    started_round_students.add(studentID);
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("sortStudentUsingSaved", "sortStudentUsingSaved");
//        }
//
//        return started_round_students;
//
//    }


//    public static void deleteAllNotifications(SQLiteDatabase database, int... roundId) {
//        database.beginTransaction();
//        if (roundId.length == 0)
//            database.delete(NotificationTable.TABLE_NAME, null, null);
//        else
//            for (int i = 0; i < roundId.length; i++) {
//                database.delete(NotificationTable.TABLE_NAME, NotificationTable.Round_id + " = ?", new String[]{Integer.toString(roundId[i])});
//            }
//        database.setTransactionSuccessful();
//        database.endTransaction();
//    }
//
//    public static void deleteAllStartedRoundStudents(SQLiteDatabase database) {
//        database.beginTransaction();
//        database.delete(StudentsOfStartedRound_TBL, null, null);
//        database.setTransactionSuccessful();
//        database.endTransaction();
//    }
//
//    public static void addStartedRoundStudents(SQLiteDatabase database, List<StudentBean> started_round_students) {
//        try {
//            database.beginTransaction();
//
//            for (int i = 0; i < started_round_students.size(); i++) {
//                ContentValues values = new ContentValues();
//                CursorUtil.putObject(values, studentID_, started_round_students.get(i).getId());
////                CursorUtil.putObject(values, roundID_, roundid.toString());
////                CursorUtil.putObject(values, roundType_, TypeRoundEnum.PICK_ROUND == started_round_students.get(i).getTypeRoundEnum() ? "PICK_ROUND" : "DROP_ROUND");
////                CursorUtil.putObject(values, orderInList_, i+"");
////                CursorUtil.putObject(values, nameStudent_, started_round_students.get(i).getNameStudent());
////                CursorUtil.putObject(values, grade_, started_round_students.get(i).getGrade());
////                CursorUtil.putObject(values, avatar_, started_round_students.get(i).getAvatar());
////                CursorUtil.putObject(values, checkEnum_, CheckEnum.CHECK_IN == started_round_students.get(i).getCheckEnum() ? "CHECK_IN" : "CHECK_OUT");
////
////                CursorUtil.putObject(values, typeRoundEnum_, TypeRoundEnum.PICK_ROUND == started_round_students.get(i).getTypeRoundEnum() ? "PICK_ROUND" : "DROP_ROUND");
////                CursorUtil.putObject(values, isAbsent_, started_round_students.get(i).isAbsent() ? "true" : "false");
////                CursorUtil.putObject(values, isNoShow_, started_round_students.get(i).isNoShow() ? "true" : "false");
////                CursorUtil.putObject(values, latitude_, started_round_students.get(i).getLatitude() +"");
////                CursorUtil.putObject(values, longitude_, started_round_students.get(i).getLongitude()  +"");
//
//                database.insert(StudentsOfStartedRound_TBL, null, values);
//            }
//
//            database.setTransactionSuccessful();
//            database.endTransaction();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("addStartedRoundStudents", "addStartedRoundStudents");
//        }
//
//    }
//
//    public static void addRoundList(SQLiteDatabase database, String value) {
//
//        database.beginTransaction();
//        ContentValues values = new ContentValues();
//        CursorUtil.putObject(values, ROUND_LIST_DATA, value);
//
//        database.insert(ROUND_LIST_TBL, null, values);
//
//
//        database.setTransactionSuccessful();
//        database.endTransaction();
//    }

//    public static ArrayList<String> getAllRoundList(SQLiteDatabase database) {
//        Cursor cursor = database.rawQuery("select * FROM " + ROUND_LIST_TBL, null);
//        ArrayList<String> reminderObjectList = new ArrayList<String>();
//        if (cursor.moveToFirst()) {
//            do {
////                Integer reminder_id = cursor.getInt(cursor.getColumnIndex(CheckInOut_checkid));
////                String longitude = cursor.getString(cursor.getColumnIndex(LonLat_longitude));
////                String latitude = cursor.getString(cursor.getColumnIndex(LonLat_latitude));
////                String date = cursor.getString(cursor.getColumnIndex(LonLat_date));
////                LonLat LonLat_object = new LonLat(longitude, latitude, date);
//
//                reminderObjectList.add(cursor.getString(cursor.getColumnIndex(ROUND_LIST_DATA)));
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return reminderObjectList;
//    }


    /**/
//    public static void addCheck(SQLiteDatabase database, ArrayList<CheckInOut> checkInOutList) {
//
//        database.beginTransaction();
//        for (int i = 0; i < checkInOutList.size(); i++) {
//            ContentValues values = new ContentValues();
//            CursorUtil.putObject(values, CheckInOut_message, checkInOutList.get(i).getMessage());
//            CursorUtil.putObject(values, CheckInOut_data, checkInOutList.get(i).getData());
//            CursorUtil.putObject(values, CheckInOut_date, checkInOutList.get(i).getDate());
//
//            database.insert(CheckInOut_TBL, null, values);
//        }
//
//        database.setTransactionSuccessful();
//        database.endTransaction();
//    }

    public static void addCheck(SQLiteDatabase database, CheckInOut checkInOutList) {

        database.beginTransaction();
        ContentValues values = new ContentValues();
        CursorUtil.putObject(values, CheckInOut_message, checkInOutList.getMessage());
        CursorUtil.putObject(values, CheckInOut_data, checkInOutList.getData());
        CursorUtil.putObject(values, CheckInOut_date, checkInOutList.getDate());

        database.insert(CheckInOut_TBL, null, values);


        database.setTransactionSuccessful();
        database.endTransaction();
    }


    public static ArrayList<CheckInOut> getAllRemindersObj(SQLiteDatabase database) {
        Cursor cursor = database.rawQuery("select * FROM " + CheckInOut_TBL, null);
        ArrayList<CheckInOut> reminderObjectList = new ArrayList<CheckInOut>();
        if (cursor.moveToFirst()) {
            do {
//                Integer reminder_id = cursor.getInt(cursor.getColumnIndex(CheckInOut_checkid));
                String message = cursor.getString(cursor.getColumnIndex(CheckInOut_message));
                String data = cursor.getString(cursor.getColumnIndex(CheckInOut_data));
                String date = cursor.getString(cursor.getColumnIndex(CheckInOut_date));
                CheckInOut CheckInOut_object = new CheckInOut(message, data, date);

                reminderObjectList.add(CheckInOut_object);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reminderObjectList;

    }


    public static void deleteAll(SQLiteDatabase database, String tableName) {
        try {
            database.beginTransaction();
            database.delete(tableName, null, null);
            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}