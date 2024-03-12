package com.octantis.prime.android.deviceutils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Environment
import android.os.SystemClock
import android.provider.CallLog
import android.util.DisplayMetrics
import com.octantis.prime.android.deviceutils.data.AppListData
import com.octantis.prime.android.deviceutils.data.GeneralData
import com.octantis.prime.android.deviceutils.data.HardwareData
import com.octantis.prime.android.deviceutils.data.MediaFilesData
import com.octantis.prime.android.deviceutils.data.NetWorkData
import com.octantis.prime.android.deviceutils.data.OtherData
import com.octantis.prime.android.deviceutils.data.StorageData
import com.octantis.prime.android.deviceutils.utils.GeneralUtils
import com.octantis.prime.android.deviceutils.utils.NetWorkUtils
import com.octantis.prime.android.deviceutils.utils.StorageQueryUtil
import java.io.File
import java.util.Locale
import java.util.StringTokenizer

class DeviceInfo {
    companion object {
        val G = GeneralData()
        val H = HardwareData()
        val O = OtherData()
        val M = MediaFilesData()
        val S = StorageQueryUtil.queryWithStorageManager(StorageData())
        val B = DeviceMain.batteryStatusData
        val N = NetWorkUtils.getNetWorkInfo(NetWorkData())
        val R = Runtime.getRuntime()
        private const val Mb = 1024 * 1024
        private val SMS_INBOX: Uri = Uri.parse("content://sms/")

        /**
         * 总体设别信息
         */
        fun getDevice(context: Context): MutableMap<String, Any> {

            val deviceInfo = mutableMapOf<String, Any>()
            deviceInfo["generalData"] = getGeneraData()
            deviceInfo["hardware"] = getHardware()
            deviceInfo["publicIp"] = getPublicIp()
            deviceInfo["simCard"] = getSimCard()
            deviceInfo["otherData"] = getOther(context)
            deviceInfo["location"] = getLocation(context)
            deviceInfo["storage"] = getStorage()
            deviceInfo["devFile"] = getDevFile()
            deviceInfo["batteryStatus"] = getBattery()
            deviceInfo["currWifi"] = getCurrWifi()
            deviceInfo["configWifi"] = getConfigWifi()
            return deviceInfo
        }

        /**
         * 通用信息
         */
        fun getGeneraData(): Map<String, Any> {
            val p1 = GeneralUtils.getSimCardInfo().number1
            val p2 = GeneralUtils.getSimCardInfo().number2
            val phoneNumber = if (p1 == "") p2 else p1

            val i1 = GeneralUtils.getSimCardInfo().imsi1
            val i2 = GeneralUtils.getSimCardInfo().imsi2
            val imei = if (i1 == "") i2 else i1

            val generaInfo = mutableMapOf<String, Any>()
            generaInfo["andId"] = G.and_id
            generaInfo["phoneType"] = G.phone_type
            generaInfo["mnc"] = G.mnc
            generaInfo["gaid"] = G.gaid
            generaInfo["dns"] = G.dns
            generaInfo["language"] = G.language
            generaInfo["mcc"] = G.mcc
            generaInfo["localeIso3Language"] = G.locale_iso_3_language
            generaInfo["localeDisplayLanguage"] = G.locale_display_language
            generaInfo["imei"] = imei
            generaInfo["phoneNumber"] = phoneNumber
            generaInfo["networkOperator"] = G.network_operator_name
            generaInfo["networkType"] = G.network_type
            generaInfo["timeZoneId"] = G.time_zone_id
            generaInfo["localeIso3Country"] = G.locale_iso_3_country
            return generaInfo
        }

        /**
         * 硬件信息
         */
        fun getHardware(): Map<String, Any> {
            val sdCardInfo = mutableMapOf<String, Any>()
            sdCardInfo["totalSize"] = S.memory_card_size
            sdCardInfo["freeSize"] = S.memory_card_size - S.memory_card_size_use
            sdCardInfo["useSize"] = S.memory_card_size_use
            val abis = H.abis
            val token = StringTokenizer(abis, ",")
            val abisList = arrayListOf<String>()
            while (token.hasMoreTokens()) {
                abisList.add(token.nextToken())
            }
            val hardware = mutableMapOf<String, Any>()
            hardware["deviceName"] = H.device
            hardware["brand"] = H.brand
            hardware["product"] = H.product
            hardware["cpu_type"] = H.cpu_type
            hardware["model"] = H.model
            hardware["release"] = H.release
            hardware["cpuType"] = H.cpu_type
            hardware["sdkVersion"] = H.sdk_version_code
            hardware["serialNumber"] = H.serial_number
            hardware["physicalSize"] = H.physical_size
            hardware["manufacturer"] = H.manufacturer_name
            hardware["display"] = H.display
            hardware["fingerprint"] = H.finger_print
            hardware["abis"] = abisList
            hardware["board"] = H.board
            hardware["buildId"] = H.id
            hardware["host"] = H.host
            hardware["type"] = H.type
            hardware["buildUser"] = H.user
            hardware["cpuAbi"] = H.cpu_abi
            hardware["cpuAbi2"] = H.cpu_abi2
            hardware["bootloader"] = H.bootloader
            hardware["hardware"] = H.hardware
            hardware["baseOS"] = H.base_os
            hardware["radioVersion"] = H.radio_version
            hardware["sdCardInfo"] = sdCardInfo
            return hardware
        }

        /**
         * IP信息
         */
        fun getPublicIp(): Map<String, Any> {
            val ip = mutableMapOf<String, Any>()
            ip["internetIp"] = ""
            ip["intranetIp"] = if (N.ip == null) {
                ""
            } else {
                N.ip
            }
            ip["internetIp2"] = ""

            return ip
        }

        /**
         * Sim卡信息
         */
        fun getSimCard(): Map<String, Any> {
            val i1 = GeneralUtils.getSimCardInfo().sim_country_iso1
            val i2 = GeneralUtils.getSimCardInfo().sim_country_iso2
            val iso = if (i1 == "") i2 else i1

            val n1 = GeneralUtils.getSimCardInfo().sim_serial_number1
            val n2 = GeneralUtils.getSimCardInfo().sim_serial_number2
            val number = if (n1 == "") n2 else n1

            val e1 = G.network_type == "" || G.network_type == "NETWORK_WIFI"
            val enabled = e1 && GeneralUtils.getSimCardInfo().sim_count > 0

            val simCardReadyS = if (GeneralUtils.getSimCardInfo().sim_count > 0) "true" else "false"
            val simCardReady = GeneralUtils.getSimCardInfo().sim_count > 0

            val simCardInfo = mutableMapOf<String, Any>()
            simCardInfo["countryIso"] = iso
            simCardInfo["serialNumber"] = number
            simCardInfo["simCardReady"] = simCardReadyS
            simCardInfo["operator"] = G.network_operator
            simCardInfo["operatorName"] = G.network_operator_name
            simCardInfo["mobileDataEnabled"] = !enabled
            simCardInfo["mobileData"] = simCardReady
            simCardInfo["dataNetworkType"] = G.network_type

            return simCardInfo
        }

        /**
         * 其他信息
         */
        private fun getOther(context: Context): Map<String, Any> {
            val m = DisplayMetrics()
            val appFree = R.totalMemory() - R.freeMemory()
            val freeM = appFree * 1.00 / Mb
            val availableM = R.totalMemory() * 1.00 / Mb
            val maxM = R.maxMemory() * 1.00 / Mb

            val otherInfo = mutableMapOf<String, Any>()
            otherInfo["hasRoot"] = O.root_jailbreak != 0
            otherInfo["lastBootTime"] = O.last_boot_time
            otherInfo["keyboard"] = O.keyboard.toString()
            otherInfo["simulator"] = O.simulator == 1
            otherInfo["adbEnabled"] = O.is_usb_debug == 1
            otherInfo["dbm"] = O.dbm
            otherInfo["imageNum"] = M.images_internal
            otherInfo["screenWidth"] = m.widthPixels
            otherInfo["screenHeight"] = m.heightPixels
            otherInfo["screenDensity"] = H.screen_density.toFloat()
            otherInfo["screenDensityDpi"] = H.screen_density_dpi.toInt()
            otherInfo["cpuNumber"] = getCpuCore()
            otherInfo["appFreeMemory"] = freeM
            otherInfo["appAvailableMemory"] = availableM
            otherInfo["appMaxMemory"] = maxM
            otherInfo["maxBattery"] = getBatteryInfo(context)[0]
            otherInfo["levelBattery"] = getBatteryInfo(context)[1]
            otherInfo["totalBootTimeWake"] = getRunTime()[0]
            otherInfo["totalBootTime"] = getRunTime()[1]

            return otherInfo
        }

        /**
         * 地址信息
         */
        private fun getLocation(context: Context): Map<String, Any> {
            try {
                val latAndLong = getLocationService(context)
                val geocoder = Geocoder(context, Locale.getDefault())
                val address = getLocalInfo(geocoder, latAndLong[0], latAndLong[1])

                val gps = mutableMapOf<String, Any>()
                gps["latitude"] = latAndLong[0]
                gps["longitude"] = latAndLong[1]

                if (address.isNullOrEmpty() || (latAndLong[0] == 0.0 && latAndLong[1] == 0.0)) {
                    val locationInfo = mutableMapOf<String, Any>()
                    locationInfo["country"] = ""
                    locationInfo["province"] = ""
                    locationInfo["city"] = ""
                    locationInfo["largeDistrict"] = ""
                    locationInfo["smallDistrict"] = ""
                    locationInfo["address"] = ""
                    locationInfo["gps"] = gps
                    return locationInfo
                } else {

                    val locationInfo = mutableMapOf<String, Any>()
                    locationInfo["country"] = address[0].countryName ?: ""
                    locationInfo["province"] = address[0].adminArea ?: ""
                    locationInfo["city"] = address[0].subAdminArea ?: ""
                    locationInfo["largeDistrict"] = latAndLong[0].toString()
                    locationInfo["smallDistrict"] = latAndLong[1].toString()
                    locationInfo["address"] = address[0].getAddressLine(0) ?: ""
                    locationInfo["gps"] = gps
                    return locationInfo
                }
            } catch (e: Exception) {
                val gps = mutableMapOf<String, Any>()
                gps["latitude"] = 0.0
                gps["longitude"] = 0.0
                val locationInfo = mutableMapOf<String, Any>()
                locationInfo["country"] = ""
                locationInfo["province"] = ""
                locationInfo["city"] = ""
                locationInfo["largeDistrict"] = ""
                locationInfo["smallDistrict"] = ""
                locationInfo["address"] = ""
                locationInfo["gps"] = gps
                return locationInfo
            }
        }

        /**
         * 资源信息
         */
        fun getStorage(): Map<String, Any> {
            val storageInfo = mutableMapOf<String, Any>()

            storageInfo["ramTotalSize"] = S.ram_total_size.toString()
            storageInfo["ramUsableSize"] = S.ram_usable_size.toString()
            storageInfo["memoryCardSize"] = S.memory_card_size.toString()
            storageInfo["memoryCardSizeUse"] = S.memory_card_size_use.toString()
            storageInfo["mainStorage"] = Environment.getExternalStorageDirectory().absolutePath
            storageInfo["externalStorage"] = System.getenv("SECONDARY_STORAGE") ?: ""
            storageInfo["internalStorageUsable"] = S.internal_storage_usable.toString()
            storageInfo["internalStorageTotal"] = S.internal_storage_total.toString()

            return storageInfo

//            return Device.Model.Storage(
//                ramTotalSize = S.ram_total_size.toString()
//                ramUsableSize = S.ram_usable_size.toString()
//                memoryCardSize = S.memory_card_size.toString()
//                memoryCardSizeUse = S.memory_card_size_use.toString()
//                mainStorage = Environment.getExternalStorageDirectory().absolutePath
//                externalStorage = System.getenv("SECONDARY_STORAGE") ?: ""
//                internalStorageUsable = S.internal_storage_usable.toString()
//                internalStorageTotal = S.internal_storage_total.toString()
//            )
        }

        /**
         * 媒体文件信息
         */
        private fun getDevFile(): Map<String, Any> {
            val fileInfo = mutableMapOf<String, Any>()
            fileInfo["audioExternal"] = M.audio_external
            fileInfo["audioInternal"] = M.audio_internal
            fileInfo["downloadFiles"] = M.download_files
            fileInfo["imagesExternal"] = M.images_external
            fileInfo["imagesInternal"] = M.images_internal
            fileInfo["videoExternal"] = M.video_external
            fileInfo["videoInternal"] = M.video_internal

            return fileInfo
        }

        /**
         * 电池信息
         */
        private fun getBattery(): Map<String, Any> {
            val batteryInfo = mutableMapOf<String, Any>()
            batteryInfo["batteryPct"] = B?.battery_pct ?: ""
            batteryInfo["isUsbCharge"] = B?.charge_type == 2
            batteryInfo["isAcCharge"] = B?.charge_type == 1
            batteryInfo["isCharging"] = B?.charge_type == 1
            return batteryInfo
        }

        /**
         * Wifi信息
         */
        private fun getCurrWifi(): Map<String, Any> {
            val wifiInfo = mutableMapOf<String, Any>()
            wifiInfo["name"] = N.current_wifi.name
            wifiInfo["bssid"] = N.current_wifi.bssid
            wifiInfo["ssid"] = N.current_wifi.ssid
            wifiInfo["mac"] = N.current_wifi.mac
            wifiInfo["ip"] = N.ip
            return wifiInfo
        }

        /**
         * Wifi列表
         */
        @SuppressLint("MissingPermission")
        private fun getConfigWifi(): List<Map<String, Any>> {
            val networkList = N.configured_wifi
            val configWifi = mutableListOf<MutableMap<String, Any>>()
            if (networkList.size > 0) {
                for (i in 0 until networkList.size) {
                    val netWorkInfo = networkList[i]
                    val wifi = mutableMapOf<String, Any>()
                    wifi["name"] = netWorkInfo.name
                    wifi["bssid"] = netWorkInfo.bssid
                    wifi["ssid"] = netWorkInfo.ssid
                    wifi["mac"] = netWorkInfo.mac
                    wifi["ip"] = N.ip
                    configWifi.add(wifi)
                }
            }
            return configWifi
        }


        /**
         * 获取短信记录
         */
        fun getSmsList(context: Activity): List<Map<String, Any>> {

            val contentResult = context.contentResolver
            val projection = arrayOf(
                "_id",
                "thread_id",
                "address",
                "person",
                "protocol",
                "read",
                "type",
                "status",
                "body",
                "service_center",
                "date"
            )

            val cursor = contentResult.query(
                SMS_INBOX, projection, null, null, "date desc"
            ) as Cursor

            val smsList = mutableListOf<MutableMap<String, Any>>()

            while (cursor.moveToNext()) {
                val msgNo = cursor.getString(cursor.getColumnIndexOrThrow("_id")).toLong()
                val threadNo = cursor.getString(cursor.getColumnIndexOrThrow("thread_id")).toLong()
                val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                val personName = cursor.getString(cursor.getColumnIndexOrThrow("person"))
                val protocol =
                    cursor.getString(cursor.getColumnIndexOrThrow("protocol"))?.toInt() ?: 1
                val read = cursor.getString(cursor.getColumnIndexOrThrow("read")).toInt()
                val type = cursor.getString(cursor.getColumnIndexOrThrow("type")).toInt()
                val status = cursor.getString(cursor.getColumnIndexOrThrow("status")).toInt()
                val bodyT = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                val serviceCenter = cursor.getString(cursor.getColumnIndexOrThrow("service_center"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date")).toLong()

                if (smsList.size < 3000 && smsList.toString().length < 100000) {


                    val item = mutableMapOf<String, Any>()

                    item["msgNO"] = msgNo
                    item["threadNo"] = threadNo
                    item["address"] = address ?: ""

                    item["personName"] = personName ?: ""
                    item["protocol"] = protocol
                    item["read"] = read

                    item["type"] = type
                    item["status"] = status
                    item["body"] = bodyT ?: ""

                    item["serviceCenter"] = serviceCenter ?: ""
                    item["date"] = date

                    smsList.add(item)
                }
            }

            cursor.close()

            if (smsList.isEmpty()) {
                val item = mutableMapOf<String, Any>()
                item["msgNO"] = ""
                item["threadNo"] = ""
                item["address"] = ""
                item["personName"] = ""
                item["protocol"] = ""
                item["read"] = ""
                item["type"] = ""
                item["status"] = ""
                item["body"] = ""
                item["serviceCenter"] = ""
                item["date"] = ""
                smsList.add(item)
            }
            return smsList
        }

        /**
         * 获取 APP 列表
         */
        fun getAppList(): MutableList<MutableMap<String, Any>> {
            val resultAppList = AppListData.getAppListData(AppListData()).list
            val appList = mutableListOf<MutableMap<String, Any>>()
            for (i in 0 until resultAppList.size) {
                val appInfo = mutableMapOf<String, Any>()
                appInfo["appName"] = resultAppList[i].app_name ?: ""
                appInfo["packageName"] = resultAppList[i].package_name ?: ""
                appInfo["version"] = resultAppList[i].version_name ?: ""
                appInfo["versionCode"] = resultAppList[i].version_code.toString()
                appInfo["appType"] =
                    if (resultAppList[i].app_type == 1) "SYSTEM" else "NON_SYSTEM"
                appInfo["flags"] = resultAppList[i].flags.toString()
                appInfo["installTime"] = resultAppList[i].in_time
                appInfo["updateTime"] = resultAppList[i].up_time
                appList.add(appInfo)
            }
            return appList
        }

        /**
         * 获取通话记录
         */
        fun getCallList(context: Activity): MutableList<MutableMap<String, Any>> {
            val list = mutableListOf<MutableMap<String, Any>>()
            try {
                val callUri = CallLog.Calls.CONTENT_URI
                // 按照时间逆序排列，最近打的最先显示
                val cursor = context.contentResolver.query(/* uri = */ callUri,/* projection = */
                    null,/* selection = */
                    null,/* selectionArgs = */
                    null,/* sortOrder = */
                    CallLog.Calls.DEFAULT_SORT_ORDER
                )
                var i = 0
                if (cursor == null) {
                    return list
                }
                while (cursor.moveToNext() && i < 3000) {
                    i++
                    val number =
                        cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                    val dateLong = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))
                    val duration =
                        cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION))
                    val type = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE))
                    val map = mutableMapOf<String, Any>()
                    map["number"] = number
                    map["dateLong"] = dateLong
                    map["duration"] = duration
                    map["type"] = type
                    list.add(map)
                }
                if (!cursor.isClosed) {
                    cursor.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return list
        }

        /**
         * 解析地址信息
         */
        @Suppress("DEPRECATION")
        private fun getLocalInfo(
            geocoder: Geocoder, lat: Double, long: Double
        ): MutableList<Address>? {
            return geocoder.getFromLocation(lat, long, 1)
        }

        /**
         * 获取地址信息
         */
        @SuppressLint("MissingPermission")
        private fun getLocationService(context: Context): List<Double> {
            if (GeneralUtils.isChekSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                return listOf(0.0, 0.0)
            }
            val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val provider = manager.getProviders(true)
            var batterLocal: Location? = manager.getLastKnownLocation(provider[0])
            for (pro in provider) {
                val location = manager.getLastKnownLocation(pro)
                if (location != null) {
                    if (batterLocal == null) {
                        batterLocal = location
                    }
                    if (location.accuracy < batterLocal.accuracy) {
                        batterLocal = location
                    }
                }
            }
            return listOf(batterLocal?.latitude ?: 0.0, batterLocal?.longitude ?: 0.0)
        }


        /**
         * 获取CPU核心数
         */
        private fun getCpuCore(): Int {
            return try {
                val dir = File("/sys/devices/system/cpu/")
                val files = dir.listFiles()?.filter {
                    java.util.regex.Pattern.matches("cpu[0-9]+", it.name)
                }
                files?.size ?: 1
            } catch (e: Exception) {
                1
            }
        }

        /**
         * 获取电量信息
         */
        private fun getBatteryInfo(context: Context): List<Int> {
            val status = context.applicationContext.registerReceiver(
                null, IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            )
            val max = status?.getIntExtra("scale", 0) ?: 0
            val stt = status?.getIntExtra("level", 0) ?: 0
            return listOf(max, stt)
        }

        /**
         * 运行时间
         */
        private fun getRunTime(): List<Long> {
            return listOf(SystemClock.elapsedRealtime(), SystemClock.uptimeMillis())
        }

        interface MMLInf {
            fun data(info: MutableList<MutableMap<String, Any>>)
        }

        interface MMMInf {
            fun data(info: MutableMap<String, Any>)
        }
    }
}