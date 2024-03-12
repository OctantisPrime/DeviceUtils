package com.octantis.prime.android.myapplication

import android.app.Application
import com.octantis.prime.android.deviceutils.DeviceMain

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        DeviceMain.init(this, true)
    }
}