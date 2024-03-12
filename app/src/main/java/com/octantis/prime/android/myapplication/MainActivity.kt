package com.octantis.prime.android.myapplication

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.octantis.prime.android.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var m: MainViewModel
    private lateinit var d: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        d = ActivityMainBinding.inflate(layoutInflater)
        m = ViewModelProvider(this)[MainViewModel::class.java]
        d.mainModel = m
        setContentView(d.root)
        initView()
        initClick()
    }

    private fun initClick() {
        d.show.setOnClickListener {
            m.showData(this)
        }

        d.showAPPList.setOnClickListener {
            m.showAppList(this)
        }
        d.showSMS.setOnClickListener {
            m.showSMSList(this)
        }
        d.showCallLog.setOnClickListener {
            m.showCallList(this)
        }
    }

    private fun initView() {
        // get Permissions
        PermissionUtils.permission(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CALENDAR
        ).callback(object : PermissionUtils.SimpleCallback {
            override fun onGranted() {
                ToastUtils.showShort("Has all permission")
            }

            override fun onDenied() {
                PermissionUtils.launchAppDetailsSettings()
            }
        }).request()

    }
}