package schooldriver.trackware.com.school_bus_driver_android.utilityDriver;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.Normalizer;

import schooldriver.trackware.com.school_bus_driver_android.R;

/**
 * Created by USER on 7/6/2017.
 */

public class UtilViews {

    public static void shakeView(int messageId, View... v) {
        for (int i = 0; i < v.length; i++) {
            v[i].startAnimation(AnimationUtils.loadAnimation(v[i].getContext(), R.anim.shake_shake));
            if (messageId != 0) {
                Toast.makeText(v[i].getContext(), messageId, Toast.LENGTH_SHORT).show();
            }
        }

    }


    public static void shakeViews(View... v) {
        Log.v("iiiiiiiiiiiishakeViews", String.valueOf(v));
        for (int i = 0; i < v.length; i++) {
            v[i].startAnimation(AnimationUtils.loadAnimation(v[i].getContext(), R.anim.shake_shake));
        }
    }


    public static boolean checkViewsIsStillEmpty(View... v) {
        for (int i = 0; i < v.length; i++) {
            if (!v[i].isShown()) {
                continue;
            }
            if (v[i] instanceof EditText &&
                    ((EditText) v[i]).getText().toString().trim().equals("") &&
                    ((EditText) v[i]).getHint().toString().trim().contains("*")) {
                shakeView(R.string.please_fill_all_required_fields, ((EditText) v[i]));
                return true;

            } else if (v[i] instanceof Button &&
                    ((Button) v[i]).getText().toString().trim().equals("") &&
                    ((Button) v[i]).getHint().toString().trim().contains("*")) {
                shakeView(R.string.please_fill_all_required_fields, ((Button) v[i]));
                return true;
            }
        }
        return false;
    }

    public static void addRedTag(View... v) {
        for (int i = 0; i < v.length; i++) {
            if (v[i] instanceof EditText) {
                if (!((EditText) v[i]).getHint().toString().trim().contains("*")) {
                    ((EditText) v[i]).setHint(Html.fromHtml(((EditText) v[i]).getHint() + " <font color='#B01500'>*</font>")); //((EditText) v[i]).getText().toString().trim().equals("")  &&
                }
            } else if (v[i] instanceof Button) {
                if (!((Button) v[i]).getHint().toString().trim().contains("*")) { //((Button) v[i]).getText().toString().trim().equals("") &&
                    ((Button) v[i]).setHint(Html.fromHtml(((Button) v[i]).getHint() + " <font color='#B01500'>*</font>"));

                }
            }


        }
    }

    public static CharSequence highlightText(String search, String originalText) {
        if (search != null && !search.equalsIgnoreCase("")) {
            String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
            int start = normalizedText.indexOf(search);
            if (start < 0) {
                return originalText;
            } else {
                Spannable highlighted = new SpannableString(originalText);
                while (start >= 0) {
                    int spanStart = Math.min(start, originalText.length());
                    int spanEnd = Math.min(start + search.length(), originalText.length());
                    /**/
                    ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.RED});
                    TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                    highlighted.setSpan(highlightSpan, spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    /**/
//                    highlighted.setSpan(new ForegroundColorSpan(Color.BLUE), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = normalizedText.indexOf(search, spanEnd);
                }
                return highlighted;
            }
        }
        return originalText;
    }

    public static View handleClickDelay(View view) {
        view.setEnabled(false);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 500);
        return view;
    }





}
