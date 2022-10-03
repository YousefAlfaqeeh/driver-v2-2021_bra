package schooldriver.trackware.com.school_bus_driver_android.utilityDriver;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by M.Bader on 8/27/2017.
 */

public class UtilTools {
//    @TargetApi(value = 23)
//    public static void gainPermission(Activity c, final String... permissions) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return;
//        }
//        try {
//            List<String> permissionList = new ArrayList<String>();
//            for (String permission : permissions) {
//                if (ContextCompat.checkSelfPermission(c, permission) != PackageManager.PERMISSION_GRANTED) {
//                    permissionList.add(permission);
//                }
//            }
//            ActivityCompat.requestPermissions(c, permissionList.toArray(new String[permissionList.size()]), 30);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }



//    public static boolean checkPermission(AppCompatActivity c, String... permissions) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        try {
//            List<String> permissionList = new ArrayList<String>();
//            for (String permission : permissions) {
//                if (ContextCompat.checkSelfPermission(c, permission) != PackageManager.PERMISSION_GRANTED) {
//                    return false;
//
//                }
//            }
//            return true;
//        } catch (Exception e) {
//            return true;
//        }
//
//    }


    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static Spanned fromHTML(String htmlString) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(htmlString);
        }
    }


}
