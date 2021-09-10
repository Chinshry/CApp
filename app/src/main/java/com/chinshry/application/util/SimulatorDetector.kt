package com.chinshry.application.util

import android.annotation.SuppressLint
import java.io.*

/**
 * Simulator Detector
 */
object SimulatorDetector {
    class SimulatorBean (val code: String, val name: String)

    val UNKNOWN_SIMULATOR = SimulatorBean("other", "其他模拟器")

    val SYZS_SIMULATOR = SimulatorBean("syzs", "腾讯手游助手")
    val LDMNQ_SIMULATOR = SimulatorBean("flysilkworm", "雷电")
    val YESHEN_SIMULATOR = SimulatorBean("bignox", "夜神")
    val MUMU_SIMULATOR = SimulatorBean("mumu", "MuMu")
    val HAIMAWAN_SIMULATOR = SimulatorBean("haimawan", "海马玩")
    val TIANTIAN_SIMULATOR = SimulatorBean("tiantian", "天天")
    val BLUESTACKS_SIMULATOR = SimulatorBean("bluestacks", "蓝叠")
    val XYAZ_SIMULATOR = SimulatorBean("microvirt", "逍遥")

    val SimulatorList = listOf(
        SYZS_SIMULATOR,
        LDMNQ_SIMULATOR,
        YESHEN_SIMULATOR,
        MUMU_SIMULATOR,
        HAIMAWAN_SIMULATOR,
        TIANTIAN_SIMULATOR,
        BLUESTACKS_SIMULATOR,
        XYAZ_SIMULATOR
    )

    /**
     * Get Device Type
     *
     * @return device type
     */
    val deviceType: String
        @SuppressLint("SdCardPath")
        get() {
            var result = UNKNOWN_SIMULATOR.name
            if (isFileExists("/dev/virtpipe-common")
                && isFileExists("/dev/virtpipe-render")
                && isFileExists("/data/data/com.tencent.tinput")
                && (isFileContainsContent(
                    "/proc/version",
                    "Linux version 3.10.0-tencent (ychuang@ubuntu)"
                )
                        || isFileContainsContent(
                    "/proc/version",
                    "Linux version 4.4.163+ (mqq@10-51-151-156)"
                )
                        || isFileContainsContent(
                    "/proc/version",
                    "Linux version 3.10.0-tencent (mqq@10-51-151-156)"
                )
                        || isFileContainsContent(
                    "/proc/version",
                    "Linux version 4.4.163-tencent (cibuilder@VM_0_97_centos)"
                ))
            ) {
                result = SYZS_SIMULATOR.name // 腾讯手游助手
            }
            if (isFileExists("/mnt/shared/TxGameAssistant")
                && isFileExists("/system/lib/vboxpcism.ko")
                && isFileExists("/system/priv-app/ZKOP/ZKOP.apk")
                && isFileExists("/system/priv-app/TiantianIME/TiantianIME.apk")
                && isFileExists("/system/etc/init.tiantian.sh")
                && isFileExists("/init.ttVM_x86.rc")
                && isFileExists("/etc/init.tiantian.sh")
            ) {
                result = SYZS_SIMULATOR.name // 腾讯手游助手
            }
            if (result == SYZS_SIMULATOR.name) {
                // 容错，特判
                if (isFileExists("/data/./data/com.bignox.app.store.hd") && isFileExists("/data/./data/com.bignox.google.installer")) {
                    return YESHEN_SIMULATOR.name
                } else if (isFileExists("/system/bin/nemuVM-nemu-control") && isFileExists("/system/bin/nemuVM-prop") && isFileExists(
                        "/system/bin/nemuVM-nemu-sf"
                    ) && isFileExists("/system/bin/nemuVM-nemu-service")
                ) {
                    return MUMU_SIMULATOR.name
                }
                return SYZS_SIMULATOR.name
            }
            if (isFileExists("/system/bin/ldinit")
                || isFileExists("/system/app/ldAppStore")
                || isFileExists("/system/priv-app/ldAppStore/ldAppStore.apk")
            ) {
                return LDMNQ_SIMULATOR.name // 雷电
            }
            if (isFileExists("/data/./data/com.bignox.app.store.hd")
                && isFileExists("/data/./data/com.bignox.google.installer")
            ) {
                return YESHEN_SIMULATOR.name // 夜神
            }
            if (isFileExists("/system/bin/nemuVM-nemu-control")
                && isFileExists("/system/bin/nemuVM-prop")
                && isFileExists("/system/bin/nemuVM-nemu-sf")
                && isFileExists("/system/bin/nemuVM-nemu-service")
            ) {
                return MUMU_SIMULATOR.name //  MuMu
            }
            if (isFileExists("/system/bin/droid4x")
                && isFileExists("/system/bin/droid4x-prop")
                && isFileExists("/system/bin/droid4x-vbox-sf")
                && isFileExists("/system/bin/droid4x_setprop")
            ) {
                return HAIMAWAN_SIMULATOR.name // 海马玩
            }
            if (isFileExists("/ueventd.ttVM_x86.rc")
                && isFileExists("/init.ttVM_x86.rc")
                && isFileExists("/fstab.ttVM_x86")
                && isFileExists("/system/bin/ttVM-prop")
                && isFileExists("/system/bin/ttVM-vbox-sf")
                && isFileExists("/system/bin/ttVM_setprop")
                && isFileExists("/dev/ttipc_ime")
            ) {
                return TIANTIAN_SIMULATOR.name // 天天
            }
            if ( /*(Build.CPU_ABI.contains("x86") || Build.CPU_ABI2.contains("x86"))
                &&*/isFileExists("/data/data/com.bluestacks.settings")
                || isFileExists("/data/user/0/com.bluestacks.settings/")
                || isFileExists("/sdcard/DCIM/SharedFolder/bs_avatar.png")
                || isFileExists("/dev/bst_gps")
                || isFileExists("/dev/bst_ime")
            ) {
                return BLUESTACKS_SIMULATOR.name // 蓝叠
            }
            if (isFileExists("/data/data/com.microvirt.installer")
                || isFileExists("/sdcard/Android/data/com.microvirt.launcher2")
                || isFileExists("/sdcard/Android/data/com.microvirt.guide")
            ) {
                return XYAZ_SIMULATOR.name // 逍遥
            }
            return if (isFileExists("/dev/vboxguest")
                || isFileExists("/dev/vboxuser")
                || isFileExists("/system/lib/vboxguest.ko")
                || isFileExists("/system/lib/vboxsf.ko")
                || isFileExists("/sys/class/misc/tboxguest")
                || isFileExists("/sys/class/misc/tboxuser")
                || isFileExists("/init.x86.rc")
                || isFileExists("/ueventd.android_x86.rc")
                || isFileContainsContent("/proc/self/maps", "lib_renderControl_enc.so")
            ) {
                UNKNOWN_SIMULATOR.name // 其他模拟器
            } else {
                UNKNOWN_SIMULATOR.name
            }
        }

    private fun isFileExists(filename: String): Boolean {
        try {
            return File(filename).exists()
        } catch (ignore: Throwable) {
        }
        return false
    }

    private fun isFileContainsContent(filename: String, content: String): Boolean {
        var found = false
        var reader: BufferedReader? = null
        try {
            val file = File(filename)
            if (file.exists() && !file.isDirectory && file.canRead()) {
                reader = BufferedReader(InputStreamReader(FileInputStream(file)))
                var line = reader.readLine()
                while (line != null) {
                    if (line.contains(content)) {
                        found = true
                        break
                    }
                    line = reader.readLine()
                }
            }
        } catch (ignore: Throwable) {
            // ignore
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (ignore: IOException) {
                    // ignore
                }
            }
        }
        return found
    }
}