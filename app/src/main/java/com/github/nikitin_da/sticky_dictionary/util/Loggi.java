package com.github.nikitin_da.sticky_dictionary.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * Simple android Log proxy, includes default TAG and provides ability to turn logs on/off
 *
 * Call it "Loggi, come here!", you can think that Loggi is your little dog :)
 *
 * @author Artem Zinnatullin [artem.zinnatullin@gmail.com]
 */
public class Loggi {

    private static final String TAG = "StickyDictionary";
    private static volatile boolean sIsEnabled = true;

    private Loggi() {}

    public static boolean isEnabled() {
        return sIsEnabled;
    }

    public static void setIsEnabled(boolean isEnabled) {
        sIsEnabled = isEnabled;
    }

    private static String getTag() {
        return TAG;
    }

    private static String getTag(String subTag) {
        return TAG + "/" + subTag;
    }

    public static void v(String message) {
        if (isEnabled()) {
            Log.v(getTag(), message);
        }
    }

    public static void v(String subTag, String message) {
        if (isEnabled()) {
            Log.v(getTag(subTag), message);
        }
    }

    public static void d(String message) {
        if (isEnabled()) {
            Log.d(getTag(), message);
        }
    }

    public static void d(String subTag, String message) {
        if (isEnabled()) {
            Log.d(getTag(subTag), message);
        }
    }

    public static void i(String message) {
        if (isEnabled()) {
            Log.i(getTag(), message);
        }
    }

    public static void i(String subTag, String message) {
        if (isEnabled()) {
            Log.i(getTag(subTag), message);
        }
    }

    public static void w(String message) {
        if (isEnabled()) {
            Log.w(getTag(), message);
        }
    }

    public static void w(String subTag, String message) {
        if (isEnabled()) {
            Log.w(getTag(subTag), message);
        }
    }

    public static void w(String message, Throwable e) {
        if (isEnabled()) {
            Log.w(getTag(), message + ", ex: \n" + exceptionToString(e));
        }
    }

    public static void w(String subTag, String message, Throwable e) {
        if (isEnabled()) {
            Log.w(getTag(subTag), message + ", ex: \n" + exceptionToString(e));
        }
    }

    public static void e(String message) {
        if (isEnabled()) {
            Log.e(getTag(), message);
        }
    }

    public static void e(String subTag, String message) {
        if (isEnabled()) {
            Log.e(getTag(subTag), message);
        }
    }

    public static void e(String message, Throwable e) {
        if (isEnabled()) {
            Log.w(getTag(), message + ", ex: \n" + exceptionToString(e));
        }
    }

    public static void e(String subTag, String message, Throwable e) {
        if (isEnabled()) {
            Log.w(getTag(subTag), message + ", ex: \n" + exceptionToString(e));
        }
    }

    private static String exceptionToString(Throwable e) {
        if (e == null) return "exception ref == null";
        String message = e.getMessage();
        String stackTrace = Log.getStackTraceString(e);

        StringBuilder stringBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(message)) {
            stringBuilder.append(message);
        } else {
            stringBuilder.append("ex message null or empty");
        }

        if (!TextUtils.isEmpty(stackTrace)) {
            stringBuilder.append(", stack trace:");
            stringBuilder.append(stackTrace);
        } else {
            stringBuilder.append(", stack trace is empty");
        }

        return stringBuilder.toString();
    }
}
