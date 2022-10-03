package schooldriver.trackware.com.school_bus_driver_android.utilityDriver;

import android.content.Context;
import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.core.view.GravityCompat;

import com.afollestad.materialdialogs.GravityEnum;
import com.google.android.material.snackbar.Snackbar;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import schooldriver.trackware.com.school_bus_driver_android.R;


public class SnackbarTool {

    @ColorRes
    static int text_color = R.color.color_white;

    public static void show(View view, @StringRes int stringRes) {
        if (view != null) {
            String message = view.getContext().getString(stringRes);
            if (!message.isEmpty()) {
                message = message.substring(0, 1).toUpperCase() + message.substring(1).toLowerCase();
            } else {
                message = "";
            }
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            TextView textView = getTextView(snackbar);
            textView.setTextColor(view.getContext().getResources().getColor(text_color));
            textView.setScaleX(view.getContext().getResources().getInteger(R.integer.scale_x));
            textView.setGravity(Gravity.START | Gravity. CENTER_VERTICAL);
            snackbar.show();
        }
    }

    public static void show(Context context, View view, String message, @ColorRes int colorRes) {
        if (message != null && !message.isEmpty()) {
            message = message.substring(0, 1).toUpperCase() + message.substring(1).toLowerCase();
        } else {
            message = "";
        }
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            TextView textView = getTextView(snackbar);
            textView.setTextColor(context.getResources().getColor(colorRes));
            textView.setScaleX(context.getResources().getInteger(R.integer.scale_x));
            textView.setGravity(Gravity.START | Gravity. CENTER_VERTICAL);
            snackbar.show();
        }
    }

    public static void show(Context context, View view, String message) {
        show(context, view, message, R.color.color_white);
    }

    public static void show(Context context, View view, @StringRes int stringRes) {
        show(view, stringRes);
    }

    public static void show(Context context, View view, String message, String actionStr, @ColorRes int actionColorRes, View.OnClickListener onClickListener) {
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            TextView textView = getTextView(snackbar);
            textView.setScaleX(context.getResources().getInteger(R.integer.scale_x));
            textView.setTextColor(context.getResources().getColor(text_color));
            textView.setGravity(Gravity.START | Gravity. CENTER_VERTICAL);
            snackbar.setAction(actionStr, onClickListener);
            snackbar.setActionTextColor(context.getResources().getColor(actionColorRes));
            snackbar.show();
        }
    }

    private static TextView getTextView(Snackbar snackbar) {
        return ((TextView) snackbar.getView().findViewById(R.id.snackbar_text));
    }

    public static void show(Context context, View view, @StringRes int messageRes, @StringRes int actionStr, @ColorRes int actionColorRes, View.OnClickListener onClickListener) {
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, messageRes, Snackbar.LENGTH_LONG);
            TextView textView = getTextView(snackbar);
            textView.setTextColor(context.getResources().getColor(text_color));
            textView.setScaleX(context.getResources().getInteger(R.integer.scale_x));
            textView.setGravity(Gravity.START | Gravity. CENTER_VERTICAL);
            snackbar.setAction(actionStr, onClickListener);
            snackbar.setActionTextColor(context.getResources().getColor(actionColorRes));
            snackbar.show();
        }
    }

    public static void show(Context context, View view, @StringRes int messageRes, @StringRes int actionStr, View.OnClickListener onClickListener) {
        show(context, view, messageRes, actionStr, R.color.color_white, onClickListener);
    }

}
