package com.chinshry.base.util

import android.Manifest
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.chinshry.base.R
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.dialog.MyDialog
import com.chinshry.base.util.PermissionMap.Companion.permissions2String

/**
 * Created by chinshry on 2022/1/3.
 * Describe：PermissionHelper
 */
enum class PermissionMap(
    val permission: String,
    val permissionName: String,
) {
    WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储"),
    READ_EXTERNAL_STORAGE(Manifest.permission.READ_EXTERNAL_STORAGE, "存储"),
    CAMERA(Manifest.permission.CAMERA, "相机"),
    LOCATION(Manifest.permission.LOCATION_HARDWARE, "定位");

    companion object {
        fun permissions2String(permissions: List<String>): String {
            val permissionStringList = mutableListOf<String>()

            permissions.forEach { permission ->
                values().forEach {
                    if (permission == it.permission && !permissionStringList.contains(it.permissionName)) {
                        permissionStringList.add(it.permissionName)
                    }
                }
            }

            return permissionStringList.joinToString("、")
        }

    }
}

object PermissionHelper {

    fun request(
        function: () -> Unit = {},
        vararg permissions: String
    ) {
        PermissionUtils
            .permission(*permissions)
            .explain { _, denied, shouldRequest -> showExplainDialog(denied, shouldRequest) }
            .rationale { _, shouldRequest ->  showRationaleDialog(shouldRequest)}
            .callback { isAllGranted, granted, deniedForever, denied ->
                LogUtils.d(isAllGranted, granted, deniedForever, denied)

                if (isAllGranted) {
                    function()
                } else {
                    if (deniedForever.isNotEmpty()) {
                        showOpenAppSettingDialog(deniedForever)
                    } else {
                        showOpenAppSettingDialog(denied)
                    }
                }
            }
            .request()
    }

    @BuryPoint(pageName = "权限申请-申请前提示弹框")
    private fun showExplainDialog(denied: List<String>, shouldRequest: PermissionUtils.OnExplainListener.ShouldRequest) {
        MyDialog()
            .setTitle(getStringById(android.R.string.dialog_alert_title))
            .setMsg(String.format(getStringById(R.string.permission_request_message), permissions2String(denied)))
            .setConfirmBtn("打开权限") { shouldRequest.start(true) }
            .setCancelBtn { shouldRequest.start(false) }
            .show()
    }

    @BuryPoint(pageName = "权限申请-再次申请弹框")
    private fun showRationaleDialog(shouldRequest: PermissionUtils.OnRationaleListener.ShouldRequest) {
        MyDialog()
            .setTitle(getStringById(android.R.string.dialog_alert_title))
            .setMsg(getStringById(R.string.permission_rationale_message))
            .setConfirmBtn("再次授权") { shouldRequest.again(true) }
            .setCancelBtn { shouldRequest.again(false) }
            .show()
    }

    @BuryPoint(pageName = "权限申请-申请被拒提示去设置弹框")
    private fun showOpenAppSettingDialog(deniedForever: List<String>) {
        MyDialog()
            .setTitle(getStringById(android.R.string.dialog_alert_title))
            .setMsg(String.format(getStringById(R.string.permission_denied), permissions2String(deniedForever)))
            .setConfirmBtn("去设置") { PermissionUtils.launchAppDetailsSettings() }
            .show()
    }

}