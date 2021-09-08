package com.chinshry.application

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.chinshry.application.bean.Router
import kotlinx.android.synthetic.main.activity_test.*
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import android.hardware.input.InputManager
import android.text.method.ScrollingMovementMethod
import android.util.Log
import com.chinshry.application.SimulatorDetector.SimulatorList
import com.chinshry.application.SimulatorDetector.UNKNOWN_SIMULATOR
import com.chinshry.application.bean.CheckResult
import com.chinshry.application.bean.CheckResult.Companion.RESULT_EMULATOR
import com.chinshry.application.bean.CheckResult.Companion.RESULT_MAYBE_EMULATOR
import com.chinshry.application.bean.CheckResult.Companion.RESULT_UNKNOWN


@Route(path = Router.ACTIVITY_TEST)
class TestActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TestActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initView()
    }

    private fun initView() {
        tv_msg.movementMethod = ScrollingMovementMethod.getInstance()
        btn_test.setOnClickListener {
            tv_msg.text = ""
            val isEmulator = checkEmulator()
            println("isEmulator = ${isEmulator}")
            if (isEmulator) {
                val brand  = getSimulatorBrand(this)
                val simulator = SimulatorDetector.deviceType
                println("brand = ${brand}")
                println("simulator = ${simulator}")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun logAndPost(msg: String) {
        Log.d(TAG, msg)
        tv_msg.text = tv_msg.text.toString() + msg
    }

    fun checkEmulator():Boolean {
        var checkResult = "checkEmulator"
        var suspectCount = 0

        // 判断是否包含模拟器应用
        val emulatorPathResult: CheckResult = checkEmulatorPath()
        checkResult += "\n emulatorPath = ${emulatorPathResult.value}"
        when (emulatorPathResult.result) {
            RESULT_MAYBE_EMULATOR -> suspectCount ++
            RESULT_EMULATOR -> {
                logAndPost(checkResult)
                return true
            }
        }

        // 判断是否包含特定路径
        val installedEmulatorPackagesResult: CheckResult = checkInstalledEmulatorPackages(this)
        checkResult += "\n installedEmulatorPackages = ${installedEmulatorPackagesResult.value}"
        when (installedEmulatorPackagesResult.result) {
            RESULT_MAYBE_EMULATOR -> suspectCount ++
            RESULT_EMULATOR -> {
                logAndPost(checkResult)
                return true
            }
        }

        // 检测usb接口的信息
        val usbResult: CheckResult = checkUSB()
        checkResult += "\n usb = ${usbResult.value}"
        when (usbResult.result) {
            RESULT_MAYBE_EMULATOR -> suspectCount ++
            RESULT_EMULATOR -> {
                logAndPost(checkResult)
                return true
            }
        }

        // 判断cpu是否为电脑
        val cpuResult: CheckResult = checkIsPcCpu()
        checkResult += "\n cpu = ${cpuResult.value}"
        when (cpuResult.result) {
            RESULT_MAYBE_EMULATOR -> suspectCount ++
            RESULT_EMULATOR -> {
                logAndPost(checkResult)
                return true
            }
        }

        // 判断是否支持x86
        val supportAbisResult: CheckResult = checkIsSupportX86()
        checkResult += "\n supportAbis = ${supportAbisResult.value}"
        when (supportAbisResult.result) {
            RESULT_MAYBE_EMULATOR -> suspectCount ++
            RESULT_EMULATOR -> {
                logAndPost(checkResult)
                return true
            }
        }

        checkResult += "\n suspectCount = ${suspectCount}"

        logAndPost(checkResult)
        return suspectCount > 0
    }

    /**
     * 检测usb接口的信息
     * 测试模拟器 蓝叠、夜神、MUMU、雷电都包含 Android Power Button 这一条信息。
     * 测试真机 不包含
     */
    fun checkUSB(): CheckResult {
        val im = getSystemService(INPUT_SERVICE) as InputManager
        for (id in im.inputDeviceIds) {
            val value = im.getInputDevice(id).name
            if (value.contains("Power Button")) {
                return CheckResult(RESULT_MAYBE_EMULATOR, value)
            }
        }
        return CheckResult(RESULT_UNKNOWN, null)
    }

    /**
     * 判断cpu是否为电脑 intel amd
     */
    fun checkIsPcCpu(): CheckResult {
        val value = readCpuInfo()
        return if (value.contains("intel") || value.contains("amd"))
            CheckResult(RESULT_MAYBE_EMULATOR, value)
        else
            CheckResult(RESULT_UNKNOWN, value)
    }

    /**
     * 判断是否支持x86
     */
    fun checkIsSupportX86(): CheckResult {
        val supportAbis = Build.SUPPORTED_ABIS
        return if (supportAbis.contains("x86"))
            CheckResult(RESULT_MAYBE_EMULATOR, Arrays.toString(supportAbis))
        else
            CheckResult(RESULT_UNKNOWN, Arrays.toString(supportAbis))
    }


    /**
     * 获取cpu信息
     *
     * @return true 为模拟器
     */
    fun readCpuInfo(): String {
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

    private val PKG_NAMES = listOf(
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

    private val PATHS = listOf(
        "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq",
        "/system/lib/libc_malloc_debug_qemu.so",
        "/sys/qemu_trace",
        "/system/bin/qemu-props",
        "/dev/socket/qemud",
        "/dev/qemu_pipe",
        "/dev/socket/baseband_genyd",
        "/dev/socket/genyd"
    )

    private fun checkEmulatorPath(): CheckResult {
        for ((index, item) in PATHS.withIndex()) {
            if (index == 0) {
                // 检测的特定路径
                if (!File(item).exists()) {
                    return CheckResult(RESULT_MAYBE_EMULATOR, item)
                }
            } else {
                if (File(item).exists()) {
                    return CheckResult(RESULT_MAYBE_EMULATOR, item)
                }
            }
        }
        return CheckResult(RESULT_UNKNOWN, null)
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun checkInstalledEmulatorPackages(context: Context): CheckResult {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apps: List<ResolveInfo> = context.packageManager.queryIntentActivities(intent, 0)
        for (element in apps) {
            val packageName: String = element.activityInfo.packageName
            if (PKG_NAMES.contains(packageName)) return CheckResult(RESULT_MAYBE_EMULATOR, packageName)

        }
        return CheckResult(RESULT_UNKNOWN, null)
    }


    private fun getSimulatorBrand(context: Context): String {
        val pkgName = checkInstalledEmulatorPackages(context).value ?: "UNKNOWN"
        val knownSimulator = SimulatorList.filter { pkgName.contains(it.code)}
        if (knownSimulator.isNotEmpty()) return knownSimulator[0].name

        when {
            // pkgName.contains("mumu") -> {
            //     return "mumu"
            // }
            // pkgName.contains("bluestacks") -> {
            //     return "蓝叠"
            // }
            // pkgName.contains("kaopu001") || pkgName.contains("tiantian") -> {
            //     return "天天"
            // }
            // pkgName.contains("microvirt") -> {
            //     return "逍遥"
            // }
            // pkgName.contains("bignox") -> {
            //     return "夜神"
            // }
            // pkgName.contains("haimawan") -> {
            //     return "海马玩"
            // }
            // pkgName.contains("flysilkworm") -> {
            //     return "雷电"
            // }
            pkgName.contains("ami") -> {
                return "AMIDuOS"
            }
            pkgName.contains("kpzs") -> {
                return "靠谱助手"
            }
            pkgName.contains("genymotion") -> {
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
            pkgName.contains("uc") -> {
                return "uc"
            }
            pkgName.contains("blue") -> {
                return "blue"
            }
            pkgName.contains("itools") -> {
                return "itools"
            }
            pkgName.contains("syd") -> {
                return "手游岛"
            }
            pkgName.contains("windroy") -> {
                return "windroy"
            }
            pkgName.contains("emu") -> {
                return "emu"
            }
            pkgName.contains("le8") -> {
                return "le8"
            }
            pkgName.contains("vphone") -> {
                return "vphone"
            }
            pkgName.contains("duoyi") -> {
                return "多益"
            }
            else -> {
                return UNKNOWN_SIMULATOR.name
            }
        }
    }

}