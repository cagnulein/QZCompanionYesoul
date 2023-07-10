package com.yesoulchina.support.serialport;

import android.util.Log;

public class SerialLog {
    private static String TAG = "Serial";
    private static boolean isDebug = false;

    /* renamed from: d */
    public static void m440d(String str) {
        if (isDebug) {
            Log.d(TAG, str);
        }
    }

    /* renamed from: i */
    public static void m441i(String str) {
        if (isDebug) {
            Log.i(TAG, str);
        }
    }

    /* renamed from: v */
    public static void m442v(String str) {
        if (isDebug) {
            Log.v(TAG, str);
        }
    }

    /* renamed from: v */
    public static void m443v(String str, Throwable th) {
        if (isDebug) {
            Log.v(TAG, str, th);
        }
    }
}
