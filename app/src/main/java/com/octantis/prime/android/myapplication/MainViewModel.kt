package com.octantis.prime.android.myapplication

import android.app.Activity
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.LogUtils
import com.octantis.prime.android.deviceutils.DeviceInfo
import com.octantis.prime.android.deviceutils.DeviceMain

class MainViewModel : ViewModel() {
    var name = ObservableField("show")
    var appCount = ObservableField("show app")
    var smsCount = ObservableField("show sms")
    var callLogCount = ObservableField("show call")
    fun showData(activity: Activity) {
        LogUtils.json(DeviceInfo.getDevice(activity))
        name.set(DeviceMain.batteryStatusData.battery_pct.toString())
    }

    fun showSMSList(activity: Activity) {
        val smsInfo = DeviceInfo.getSmsList(activity)
        LogUtils.json(smsInfo)
        smsCount.set("SmsCount : ${smsInfo.size}")
    }

    fun showAppList(activity: Activity) {
        val appInfo = DeviceInfo.getAppList()
        LogUtils.json(appInfo)
        appCount.set("AppCount : ${appInfo.size}")
    }

    fun showCallList(activity: Activity) {
        val callInfo = DeviceInfo.getCallList(activity)
        LogUtils.json(callInfo)
        callLogCount.set("CallCount : ${callInfo.size}")
    }

}