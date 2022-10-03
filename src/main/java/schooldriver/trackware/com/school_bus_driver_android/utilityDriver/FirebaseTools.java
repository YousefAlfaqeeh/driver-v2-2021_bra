package schooldriver.trackware.com.school_bus_driver_android.utilityDriver;

import android.content.Context;
import android.os.Bundle;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class FirebaseTools {

//    public static class Analytics {
//        final String analyticsTimeFormat = "yyyy-MM-dd HH:mm:ss a";
//        private static FirebaseAnalytics firebaseAnalytics;
//
//        public static void createInstance(Context c) {
//            try {
//                if (firebaseAnalytics == null)
//                    firebaseAnalytics = FirebaseAnalytics.getInstance(c);
//            } catch (Exception e) {
//                e.printStackTrace();
//
//            }
//
//        }
//
//        Bundle bundle = new Bundle();
//
//        public Analytics addLog(String key, String value) {
//            try {
//                bundle.putString(key, value == null ? "null" : value);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return this;
//        }
//
//        public Analytics addLog(String key, int value) {
//            try {
//                bundle.putString(key, value + "");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return this;
//        }
//
//        public Analytics addLog(String key, Exception exceptionValue) {
//            try {
//                bundle.putString(key + " Exception Message ", exceptionValue.getMessage());
//                bundle.putString(key + " Exception toString ", exceptionValue.toString());
//                bundle.putString(key + " Exception StackTrace ", Arrays.toString(exceptionValue.getStackTrace()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return this;
//        }
//
//        public Analytics addLog(String key, int[] intArray) {
//            try {
//                bundle.putString(key, intArray == null ? "null" : Arrays.toString(intArray));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return this;
//        }
//
//        public Analytics addLog(String key, String[] stringArray) {
//            try {
//                bundle.putString(key, stringArray == null ? "null" : Arrays.toString(stringArray));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return this;
//        }
//
//
//        public void send() {
//            try {
//                if (bundle != null) {
//                    String atTime = new SimpleDateFormat(analyticsTimeFormat, Locale.US).format(new Date());
//                    FirebaseTools.Analytics.firebaseAnalytics.logEvent("Firebase_Analytics_Log", bundle);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }

}
