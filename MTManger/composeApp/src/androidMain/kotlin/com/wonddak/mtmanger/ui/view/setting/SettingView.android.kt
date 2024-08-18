package com.wonddak.mtmanger.ui.view.setting

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.wonddak.mtmanger.ui.view.home.main.AppContext

actual fun getVersion(): String {
    val context: Context = AppContext.get()
    val packageInfo: PackageInfo =
        with(context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(
                    packageName,
                    PackageManager.PackageInfoFlags.of(PackageManager.GET_SIGNING_CERTIFICATES.toLong())
                )
            } else {
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            }
        }
    return packageInfo.versionName
}