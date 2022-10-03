package database;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.Html;
import android.text.Spanned;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CursorUtil {

    public static Spanned getValue(Cursor cursor, String columnName) {
        String value = getColumnValue(cursor, columnName, String.class);
        if (value == null) {
            return null;
        }
        value = value.replaceAll("\\s{2,}", " ").trim();
        /*value = value.replaceAll("(\\r\\n|\\n)", "<br/>");*/
        value = value.replaceAll("(\r?\n){2,}", "$1");
        value = value.replaceAll("(?m)(^ *| +(?= |$))", "").replaceAll("(?m)^$([\r\n]+?)(^$[\r\n]+?^)+", "$1");
        value = keepOneWS(value);
        return Html.fromHtml("<div style=\"direction: rtl; text-align: right\" dir=\"rtl\">" + value + "</div>");
    }

    public static Spanned getHtmlValueAr(Cursor cursor, String columnName) {
        String value = getColumnValue(cursor, columnName, String.class);
        if (value == null) {
            return null;
        }
        value = value.replaceAll("\\s{2,}", " ").trim();
        /*value = value.replaceAll("(\\r\\n|\\n)", "<br/>");*/
        value = value.replaceAll("(\r?\n){2,}", "$1");
        value = value.replaceAll("(?m)(^ *| +(?= |$))", "").replaceAll("(?m)^$([\r\n]+?)(^$[\r\n]+?^)+", "$1");
        value = keepOneWS(value);
        return Html.fromHtml("<div style=\"direction: rtl; text-align: right\" dir=\"rtl\">" + value + "</div>");
    }

    public static Spanned getHtmlValueEn(Cursor cursor, String columnName) {
        String value = getColumnValue(cursor, columnName, String.class);
        if (value == null) {
            return null;
        }
        value = value.replaceAll("\\s{2,}", " ").trim();
        value = value.replaceAll("(\r?\n){2,}", "$1");
        value = value.replaceAll("(?m)(^ *| +(?= |$))", "").replaceAll("(?m)^$([\r\n]+?)(^$[\r\n]+?^)+", "$1");
        /*value = value.replaceAll("(\\r\\n|\\n)", "<br/>");*/
        value = keepOneWS(value);
        return Html.fromHtml("<div style=\"direction: ltr; text-align: left\" dir=\"ltr\">" + value + "</div>");
    }

    public static Spanned getHtmlValue(Cursor cursor, String columnName) {
        String value = getColumnValue(cursor, columnName, String.class);
        if (value == null) {
            return null;
        }
        /*value = value.replaceAll("(\\r\\n|\\n)", "<br/>");*/
        return Html.fromHtml(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getColumnValue(Cursor cursor, String columnName, Class c) {
        int index = cursor.getColumnIndex(columnName);
        if (index == -1 || cursor.isNull(index)) {
            return null;
        }
        if (c == String.class) {
            return (T) cursor.getString(index);
        }
        if (c == Integer.class) {
            return (T) Integer.valueOf(cursor.getInt(index));
        }
        if (c == Double.class) {
            return (T) Double.valueOf(cursor.getDouble(index));
        }
        if (c == Long.class) {
            return (T) Long.valueOf(cursor.getLong(index));
        }
        if (c == Calendar.class) {
            long timeInMillis = cursor.getLong(index);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeInMillis);
            return (T) calendar;
        }

        return null;
    }

    public static void putObject(ContentValues values, String key, Object o) {
        if (o == null) {
            values.putNull(key);
            return;
        }
        if (o instanceof String) {
            values.put(key, (String) o);
        } else if (o instanceof Integer) {
            values.put(key, (Integer) o);
        } else if (o instanceof Long) {
            values.put(key, (Long) o);
        } else if (o instanceof Boolean) {
            values.put(key, ((Boolean) o) ? 1 : 0);
        } else if (o instanceof Calendar) {
            values.put(key, ((Calendar) o).getTimeInMillis());
        } else {
            values.putNull(key);
        }
    }

    public static String keepOneWS(String str) {
        Pattern p = Pattern.compile("(\\s+)");
        Matcher m = p.matcher(str);

        Pattern pBlank = Pattern.compile("[ \t]+");
        String newLineReplacement = System.getProperty("line.separator") +
                System.getProperty("line.separator");

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            if (pBlank.matcher(m.group(1)).matches()) {
                m.appendReplacement(sb, " ");
            } else {
                m.appendReplacement(sb, newLineReplacement);
            }
        }
        m.appendTail(sb);

        return sb.toString().trim();
    }

}
