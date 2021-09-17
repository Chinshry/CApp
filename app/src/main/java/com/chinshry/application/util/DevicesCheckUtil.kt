package com.chinshry.application.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.input.InputManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/**
 * @author         Chinshry
 * @date           2021/9/8 5:11 下午
 * @description    checkIsRoot root检测  checkIsRunningInEmulator 模拟器检测
 */
object DevicesCheckUtil {
    private const val TAG = "DevicesCheckUtil"
    private const val RESULT_UNKNOWN = 0 // 可能是真机
    private const val RESULT_MAYBE_EMULATOR = 1 // 可能是模拟器
    private const val RESULT_EMULATOR = 2 // 模拟器

    private const val SUSPECT_MAX = 3 // 怀疑值大于3 判断为模拟器

    private val SU_PATHS = listOf(
        "/sbin/su",
        "/system/bin/su",
        "/system/xbin/su",
        "/data/local/xbin/su",
        "/data/local/bin/su",
        "/system/sd/xbin/su",
        "/system/bin/failsafe/su",
        "/data/local/su"
    )
    private val EMULATOR_PATHS = listOf(
        "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq",
        "/system/lib/libc_malloc_debug_qemu.so",
        "/sys/qemu_trace",
        "/system/bin/qemu-props",
        "/dev/socket/qemud",
        "/dev/qemu_pipe",
        "/dev/socket/baseband_genyd",
        "/dev/socket/genyd"
    )
    private val EMULATOR_PKG_NAMES = listOf(
        "com.mumu.launcher",
        "com.ami.duosupdater.ui",
        "com.ami.launchmetro",
        "com.ami.syncduosservices",
        "com.bluestacks.home",
        "com.bluestacks.BstCommandProcessor",
        "com.bluestacks.appguidance",
        "com.bluestacks.windowsfilemanager",
        "com.bluestacks.settings",
        "com.bluestacks.bluestackslocationprovider",
        "com.bluestacks.appsettings",
        "com.bluestacks.bstfolder",
        "com.bluestacks.BstCommandProcessor",
        "com.bluestacks.s2p",
        "com.bluestacks.setup",
        "com.bluestacks.appmart",
        "com.kaopu001.tiantianserver",
        "com.kpzs.helpercenter",
        "com.kaopu001.tiantianime",
        "com.android.development_settings",
        "com.android.development",
        "com.android.customlocale2",
        "com.genymotion.superuser",
        "com.genymotion.clipboardproxy",
        "com.uc.xxzs.keyboard",
        "com.uc.xxzs",
        "com.blue.huang17.agent",
        "com.blue.huang17.launcher",
        "com.blue.huang17.ime",
        "com.microvirt.guide",
        "com.microvirt.market",
        "com.microvirt.memuime",
        "cn.itools.vm.launcher",
        "cn.itools.vm.proxy",
        "cn.itools.vm.softkeyboard",
        "cn.itools.avdmarket",
        "com.syd.IME",
        "com.bignox.app.store.hd",
        "com.bignox.launcher",
        "com.bignox.app.phone",
        "com.bignox.app.noxservice",
        "com.android.noxpush",
        "com.haimawan.push",
        "me.haima.helpcenter",
        "com.windroy.launcher",
        "com.windroy.superuser",
        "com.windroy.launcher",
        "com.windroy.ime",
        "com.android.flysilkworm",
        "com.android.emu.inputservice",
        "com.tiantian.ime",
        "com.microvirt.launcher",
        "me.le8.androidassist",
        "com.vphone.helper",
        "com.vphone.launcher",
        "com.duoyi.giftcenter.giftcenter"
    )

    class CheckResult(
        var resultList: MutableList<CheckItemResult> = mutableListOf(),
        var suspectCount: Int = 0,
        var result: Boolean = false
    ) {
        fun checkItem(newResult: CheckItemResult): CheckResult {
            resultList.apply {
                add(newResult)
            }
            when (newResult.result) {
                RESULT_MAYBE_EMULATOR -> {
                    suspectCount++
                    //模拟器基带信息为null的情况概率相当大
                    if (newResult.name == "baseBand") {
                        suspectCount++
                    }
                }
                RESULT_EMULATOR -> result = true
            }
            //嫌疑值大于3，认为是模拟器
            if (suspectCount > SUSPECT_MAX) {
                result = true
            }
            return this
        }
    }

    class CheckItemResult(var name: String, var result: Int = RESULT_UNKNOWN, var value: String? = null)

    /**
     * 检查是否为root或模拟器
     */
    fun checkRootAndEmulator(context: Context) : Boolean {
        val rootResult = checkIsRoot()
        val emulatorResult = checkIsRunningInEmulator(context)
        Log.d(TAG, "checkRootAndEmulator isRoot = $rootResult")
        Log.d(TAG, "checkRootAndEmulator isEmulator = ${emulatorResult.result}")
        if (rootResult) {
            Toast.makeText(context, "您的设备已ROOT，存在安全隐患！", Toast.LENGTH_LONG).show()
        }
        if (emulatorResult.result) {
            Toast.makeText(context, "您正在使用模拟器设备，存在安全隐患！", Toast.LENGTH_LONG).show()
            val brand  = getSimulatorBrand(context, emulatorResult.resultList)
            val simulator = SimulatorDetector.deviceType
            Log.d(TAG, "checkRootAndEmulator brand = $brand")
            Log.d(TAG, "checkRootAndEmulator simulator = $simulator")
        }
        logEmulatorResult(emulatorResult)
        return rootResult || emulatorResult.result
    }

    /**
     * 打印模拟器检测日志
     */
    private fun logEmulatorResult(result: CheckResult) {
        var str = "logEmulatorResult"
        for (eachResult in result.resultList) {
            str += "\n ${eachResult.name} | result = ${eachResult.result} | value = ${eachResult.value}"
        }
        str += "\n suspectCount = ${result.suspectCount}"
        Log.d(TAG, str)
    }

    /**
     * 是否ROOT
     */
    private fun checkIsRoot(): Boolean {
        val secureProp: Int = getRoSecureProp()
        return if (secureProp == 0) //eng/userDebug版本，自带root权限
            true else isSUExist() //user版本，继续查su文件
    }

    private fun getRoSecureProp(): Int {
        val secureProp: Int
        val roSecureObj: String? = CommonUtils.getProperty("ro.secure")
        secureProp = if (roSecureObj == null) 1 else {
            if ("0" == roSecureObj) 0 else 1
        }
        return secureProp
    }

    private fun isSUExist(): Boolean {
        var file: File?
        for (path in SU_PATHS) {
            file = File(path)
            if (file.exists()) return true
        }
        return false
    }

    /**
     * 是否为模拟器设备
     */
    private fun checkIsRunningInEmulator(context: Context): CheckResult {
        val checkResult = CheckResult()

        // 判断是否包含特定路径
        if (checkResult.checkItem(checkEmulatorPath()).result) return checkResult

        // 判断是否包含模拟器应用
        if (checkResult.checkItem(checkInstalledEmulatorPackages(context)).result) return checkResult

        // 检测usb接口的信息
        if (checkResult.checkItem(checkUSBInfo(context)).result) return checkResult

        // 判断cpu是否为电脑
        if (checkResult.checkItem(checkIsPcCpu()).result) return checkResult

        // 判断是否支持x86
        if (checkResult.checkItem(checkIsSupportX86()).result) return checkResult

        // 检测硬件名称
        if (checkResult.checkItem(checkFeaturesByHardware()).result) return checkResult

        // 检测渠道
        if (checkResult.checkItem(checkFeaturesByFlavor()).result) return checkResult

        // 检测设备型号
        if (checkResult.checkItem(checkFeaturesByModel()).result) return checkResult

        // 检测硬件制造商
        if (checkResult.checkItem(checkFeaturesByManufacturer()).result) return checkResult

        // 检测主板名称
        if (checkResult.checkItem(checkFeaturesByBoard()).result) return checkResult

        // 检测主板平台
        if (checkResult.checkItem(checkFeaturesByPlatform()).result) return checkResult

        // 检测基带信息
        if (checkResult.checkItem(checkFeaturesByBaseBand()).result) return checkResult

        // 检测传感器数量
        if (checkResult.checkItem(getSensorNumber(context)).result) return checkResult

        // 检测已安装第三方应用数量
        if (checkResult.checkItem(getUserAppNumber()).result) return checkResult

        // 检测是否支持相机
        if (checkResult.checkItem(supportCamera(context)).result) return checkResult

        // 检测是否支持闪光灯
        if (checkResult.checkItem(supportCameraFlash(context)).result) return checkResult

        // 检测是否支持蓝牙
        if (checkResult.checkItem(supportBluetooth(context)).result) return checkResult

        // 检测光线传感器
        if (checkResult.checkItem(hasLightSensor(context)).result) return checkResult

        // 检测进程组信息
        if (checkResult.checkItem(checkFeaturesByCgroup()).result) return checkResult

        return checkResult
    }

    private fun getProperty(propName: String): String? {
        val property: String? = CommonUtils.getProperty(propName)
        return if (property.isNullOrBlank()) null else property
    }

    /**
     * 特征参数-硬件名称
     */
    private fun checkFeaturesByHardware(): CheckItemResult {
        val checkItemName = "hardware"
        val hardware = getProperty("ro.hardware")?.toLowerCase(Locale.ROOT) ?: return CheckItemResult(checkItemName, RESULT_MAYBE_EMULATOR)
        val emulatorHardwareList = listOf("ttvm", "nox", "cancro", "intel", "vbox", "vbox86", "android_x86")
        val result = when {
            emulatorHardwareList.contains(hardware) -> RESULT_EMULATOR
            emulatorHardwareList.any { hardware.contains(it) } -> RESULT_MAYBE_EMULATOR
            else -> RESULT_UNKNOWN
        }
        return CheckItemResult(checkItemName, result, hardware)
    }

    /**
     * 特征参数-渠道
     */
    private fun checkFeaturesByFlavor(): CheckItemResult {
        val checkItemName = "flavor"
        val flavor = getProperty("ro.build.flavor") ?: return CheckItemResult(checkItemName, RESULT_MAYBE_EMULATOR)
        val emulatorFlavorList = listOf("vbox", "sdk_gphone")
        val result = if (emulatorFlavorList.any { flavor.toLowerCase(Locale.ROOT).contains(it) }) RESULT_EMULATOR else RESULT_UNKNOWN
        return CheckItemResult(checkItemName, result, flavor)
    }

    /**
     * 特征参数-设备型号
     */
    private fun checkFeaturesByModel(): CheckItemResult {
        val checkItemName = "model"
        val model = getProperty("ro.product.model") ?: return CheckItemResult(checkItemName, RESULT_MAYBE_EMULATOR)
        val emulatorModelList = listOf("google_sdk", "emulator", "android sdk built for x86", "mumu")
        val result = if (emulatorModelList.any { model.toLowerCase(Locale.ROOT).contains(it) }) RESULT_EMULATOR else RESULT_UNKNOWN
        return CheckItemResult(checkItemName, result, model)
    }

    /**
     * 特征参数-硬件制造商
     */
    private fun checkFeaturesByManufacturer(): CheckItemResult {
        val checkItemName = "manufacturer"
        val manufacturer = getProperty("ro.product.manufacturer") ?: return CheckItemResult(checkItemName, RESULT_MAYBE_EMULATOR)
        val result =
            when {
                manufacturer.toLowerCase(Locale.ROOT).contains("genymotion") -> RESULT_EMULATOR
                manufacturer.toLowerCase(Locale.ROOT).contains("netease") -> RESULT_EMULATOR //网易MUMU模拟器
                else -> RESULT_UNKNOWN
            }
        return CheckItemResult(checkItemName, result, manufacturer)
    }

    /**
     * 特征参数-主板名称
     */
    private fun checkFeaturesByBoard(): CheckItemResult {
        val checkItemName = "board"
        val board = getProperty("ro.product.board") ?: return CheckItemResult(checkItemName, RESULT_MAYBE_EMULATOR)
        val emulatorBoardList = listOf("android", "goldfish")
        val result = if (emulatorBoardList.any { board.toLowerCase(Locale.ROOT).contains(it) }) RESULT_EMULATOR else RESULT_UNKNOWN
        return CheckItemResult(checkItemName, result, board)
    }

    /**
     * 特征参数-主板平台
     */
    private fun checkFeaturesByPlatform(): CheckItemResult {
        val checkItemName = "platform"
        val platform = getProperty("ro.board.platform") ?: return CheckItemResult(checkItemName, RESULT_MAYBE_EMULATOR)
        val result = if (platform.toLowerCase(Locale.ROOT).contains("android")) RESULT_EMULATOR else RESULT_UNKNOWN
        return CheckItemResult(checkItemName, result, platform)
    }

    /**
     * 特征参数-基带信息
     */
    private fun checkFeaturesByBaseBand(): CheckItemResult {
        val checkItemName = "baseBand"
        val baseBandVersion = getProperty("gsm.version.baseband") ?: return CheckItemResult(checkItemName, RESULT_MAYBE_EMULATOR)
        val result = if (baseBandVersion.contains("1.0.0.0")) RESULT_EMULATOR else RESULT_UNKNOWN
        return CheckItemResult(checkItemName, result, baseBandVersion)
    }

    /**
     * 获取传感器数量
     */
    private fun getSensorNumber(context: Context): CheckItemResult {
        val checkItemName = "sensorNumber"
        val sm = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorNumber = sm.getSensorList(Sensor.TYPE_ALL).size
        val result = if (sensorNumber <= 7) RESULT_MAYBE_EMULATOR else RESULT_UNKNOWN
        return CheckItemResult(checkItemName, result, sensorNumber.toString())
    }

    /**
     * 获取已安装第三方应用数量
     */
    private fun getUserAppNumber(): CheckItemResult {
        val checkItemName = "userAppNum"
        val userApps: String? = CommonUtils.exec("pm list package -3")
        val userAppNum = if (userApps.isNullOrBlank()) 0 else userApps.split("package:").toTypedArray().size
        val result = if (userAppNum <= 5) RESULT_MAYBE_EMULATOR else RESULT_UNKNOWN
        return CheckItemResult(checkItemName, result, userAppNum.toString())
    }

    /**
     * 是否支持相机
     */
    private fun supportCamera(context: Context): CheckItemResult {
        val checkItemName = "supportCamera"
        val supportCamera = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        val result = if (!supportCamera) RESULT_MAYBE_EMULATOR else RESULT_UNKNOWN
        return CheckItemResult(checkItemName, result, supportCamera.toString())
    }

    /**
     * 是否支持闪光灯
     */
    private fun supportCameraFlash(context: Context): CheckItemResult {
        val checkItemName = "supportCameraFlash"
        val supportCameraFlash = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        val result = if (!supportCameraFlash) RESULT_MAYBE_EMULATOR else RESULT_UNKNOWN
        return CheckItemResult(checkItemName, result, supportCameraFlash.toString())
    }

    /**
     * 是否支持蓝牙
     */
    private fun supportBluetooth(context: Context): CheckItemResult {
        val checkItemName = "supportBluetooth"
        val supportBluetooth = context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
        val result = if (!supportBluetooth) RESULT_MAYBE_EMULATOR else RESULT_UNKNOWN
        return CheckItemResult(checkItemName, result, supportBluetooth.toString())
    }

    /**
     * 判断是否存在光传感器来判断是否为模拟器
     * 部分真机也不存在温度和压力传感器。其余传感器模拟器也存在。
     */
    private fun hasLightSensor(context: Context): CheckItemResult {
        val checkItemName = "hasLightSensor"
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) //光线传感器
        val result = if (sensor == null) RESULT_MAYBE_EMULATOR else RESULT_UNKNOWN
        return CheckItemResult(checkItemName, result, (sensor != null).toString())
    }

    /**
     * 特征参数-进程组信息
     */
    private fun checkFeaturesByCgroup(): CheckItemResult {
        val checkItemName = "cgroupResult"
        val filter: String = CommonUtils.exec("cat /proc/self/cgroup")
            ?: return CheckItemResult(checkItemName, RESULT_MAYBE_EMULATOR)
        return CheckItemResult(checkItemName, RESULT_UNKNOWN, filter)
    }

    /**
     * 检测usb接口的信息
     * 测试模拟器 蓝叠、夜神、MUMU、雷电都包含 Android Power Button 这一条信息。
     * 测试真机 不包含
     */
    private fun checkUSBInfo(context: Context): CheckItemResult {
        val checkItemName = "usbInfo"
        val im = context.getSystemService(AppCompatActivity.INPUT_SERVICE) as InputManager
        for (id in im.inputDeviceIds) {
            val value = im.getInputDevice(id).name
            if (value.contains("Power Button")) {
                return CheckItemResult(checkItemName, RESULT_MAYBE_EMULATOR, value)
            }
        }
        return CheckItemResult(checkItemName)
    }

    /**
     * 判断cpu是否为电脑 intel amd
     */
    private fun checkIsPcCpu(): CheckItemResult {
        val checkItemName = "cpuInfo"
        val value = readCpuInfo()
        val result = if (value.contains("intel") || value.contains("amd"))
            RESULT_MAYBE_EMULATOR
        else
            RESULT_UNKNOWN
        return CheckItemResult(checkItemName, result, value)
    }

    /**
     * 判断是否支持x86
     */
    private fun checkIsSupportX86(): CheckItemResult {
        val checkItemName = "supportAbis"
        val supportAbis = Build.SUPPORTED_ABIS
        val result = if (supportAbis.contains("x86")) RESULT_MAYBE_EMULATOR else RESULT_UNKNOWN
        return CheckItemResult(checkItemName, result, Arrays.toString(supportAbis))
    }


    /**
     * 获取cpu信息
     */
    private fun readCpuInfo(): String {
        var result = ""
        try {
            val args = arrayOf("/system/bin/cat", "/proc/cpuinfo")
            val cmd = ProcessBuilder(*args)
            val process = cmd.start()
            val sb = StringBuffer()
            var readLine: String?
            val responseReader = BufferedReader(InputStreamReader(process.inputStream, "utf-8"))
            while (responseReader.readLine().also { readLine = it } != null) {
                sb.append(readLine + "\n")
            }
            responseReader.close()
            result = sb.toString().toLowerCase(Locale.ROOT)
        } catch (ex: IOException) {
        }
        return result
    }

    /**
     * 判断是否包含特定路径
     */
    private fun checkEmulatorPath(): CheckItemResult {
        val checkItemName = "emulatorPath"
        var result = RESULT_UNKNOWN
        var value: String? = null
        for ((index, item) in EMULATOR_PATHS.withIndex()) {
            if (index == 0) {
                // 检测的特定路径
                if (!File(item).exists()) {
                    result = RESULT_MAYBE_EMULATOR
                    value = item
                }
            } else {
                if (File(item).exists()) {
                    result = RESULT_MAYBE_EMULATOR
                    value = item
                }
            }
        }
        return CheckItemResult(checkItemName, result, value)
    }

    /**
     * 判断是否包含模拟器应用
     */
    @SuppressLint("QueryPermissionsNeeded")
    private fun checkInstalledEmulatorPackages(context: Context): CheckItemResult {
        val checkItemName = "installedEmulatorPackages"
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apps: List<ResolveInfo> = context.packageManager.queryIntentActivities(intent, 0)
        for (element in apps) {
            val packageName: String = element.activityInfo.packageName
            if (EMULATOR_PKG_NAMES.contains(packageName)) return CheckItemResult(checkItemName, RESULT_MAYBE_EMULATOR, packageName)
        }
        return CheckItemResult(checkItemName)
    }

    private fun getSimulatorBrand(context: Context, resultPkg: MutableList<CheckItemResult>): String {
        val emulatorPkgName : String = resultPkg.filter { it.name == "installedEmulatorPackages" }[0].value
            ?: checkInstalledEmulatorPackages(context).value ?: "UNKNOWN"
        val knownSimulator = SimulatorDetector.SimulatorList.filter { emulatorPkgName.contains(it.code)}
        if (knownSimulator.isNotEmpty()) return knownSimulator[0].name

        when {
            // emulatorPkgName.contains("mumu") -> {
            //     return "mumu"
            // }
            // emulatorPkgName.contains("bluestacks") -> {
            //     return "蓝叠"
            // }
            // emulatorPkgName.contains("kaopu001") || emulatorPkgName.contains("tiantian") -> {
            //     return "天天"
            // }
            // emulatorPkgName.contains("microvirt") -> {
            //     return "逍遥"
            // }
            // emulatorPkgName.contains("bignox") -> {
            //     return "夜神"
            // }
            // emulatorPkgName.contains("haimawan") -> {
            //     return "海马玩"
            // }
            // emulatorPkgName.contains("flysilkworm") -> {
            //     return "雷电"
            // }
            emulatorPkgName.contains("ami") -> {
                return "AMIDuOS"
            }
            emulatorPkgName.contains("kpzs") -> {
                return "靠谱助手"
            }
            emulatorPkgName.contains("genymotion") -> {
                return when {
                    Build.MODEL.contains("iTools") -> {
                        "iTools"
                    }
                    Build.MODEL.contains("ChangWan") -> {
                        "畅玩"
                    }
                    else -> {
                        "genymotion"
                    }
                }
            }
            emulatorPkgName.contains("uc") -> {
                return "uc"
            }
            emulatorPkgName.contains("blue") -> {
                return "blue"
            }
            emulatorPkgName.contains("itools") -> {
                return "itools"
            }
            emulatorPkgName.contains("syd") -> {
                return "手游岛"
            }
            emulatorPkgName.contains("windroy") -> {
                return "windroy"
            }
            emulatorPkgName.contains("emu") -> {
                return "emu"
            }
            emulatorPkgName.contains("le8") -> {
                return "le8"
            }
            emulatorPkgName.contains("vphone") -> {
                return "vphone"
            }
            emulatorPkgName.contains("duoyi") -> {
                return "多益"
            }
            else -> {
                return SimulatorDetector.UNKNOWN_SIMULATOR.name
            }
        }
    }

}