package com.crazy.kotlin_mvvm.listener

/**
 * 权限回调接口
 */
interface PermissionListener {
    fun onGranted()

    fun onDenied(deniedPermissions: List<String>)
}
