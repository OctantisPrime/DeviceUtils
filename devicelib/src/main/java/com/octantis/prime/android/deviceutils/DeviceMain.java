package com.octantis.prime.android.deviceutils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.octantis.prime.android.deviceutils.data.BatteryStatusData;
import com.octantis.prime.android.deviceutils.receiver.BatteryBroadcastReceiver;
import com.octantis.prime.android.deviceutils.utils.GeneralUtils;


public class DeviceMain {
    @SuppressLint("StaticFieldLeak")
    private static Application sApp;
    /**
     * 全局设备Id
     */
    public static String deviceId = "";
    public static Boolean onlyBat = false;
    /**
     * 电池信息
     */
    public static BatteryStatusData batteryStatusData;
    /**
     * 通过广播获取电池信息
     */
    public static BatteryBroadcastReceiver batteryBroadcastReceiver;

    public static boolean mRegisterTag = false;

    public static void init(final Application app) {
        if (app == null) {
            Log.e("DeviceUtils", "app is null.");
            return;
        }
        if (sApp == null) {
            sApp = app;
            GeneralUtils.getGaid();
            initBoadcast();
        }
    }
    public static void init(final Application app,boolean setOnlyBat) {
        if (app == null) {
            Log.e("DeviceUtils", "app is null.");
            return;
        }
        if (sApp == null) {
            sApp = app;
            GeneralUtils.getGaid();
            initBoadcast();
            onlyBat = setOnlyBat;
        }
    }


    public static Application getApp() {
        if (sApp != null) return sApp;
        throw new NullPointerException("reflect failed.");
    }

    public static void initBoadcast() {
        batteryBroadcastReceiver = new BatteryBroadcastReceiver();
        IntentFilter intent = new IntentFilter();
        intent.addAction(Intent.ACTION_BATTERY_CHANGED);
        DeviceMain.getApp().registerReceiver(batteryBroadcastReceiver, intent);
        mRegisterTag = true;
    }

    public static void removeBoadcast() {
        if (mRegisterTag) {
            DeviceMain.getApp().unregisterReceiver(batteryBroadcastReceiver);
            mRegisterTag = false;
        }
    }

}
