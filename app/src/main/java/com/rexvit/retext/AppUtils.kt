package com.rexvit.retext

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

object AppUtils {
    fun getInstalledApps(context: Context): List<AppInfo> {
        val pm = context.packageManager
        val apps = try {
            pm.getInstalledApplications(PackageManager.GET_META_DATA)
                .filter { context.packageManager.getLaunchIntentForPackage(it.packageName) != null }
        } catch (e: Exception) {
            emptyList()
        }

        return apps.filter {
            it.flags and ApplicationInfo.FLAG_SYSTEM == 0
        }.map {
            AppInfo(
                name = it.loadLabel(pm).toString(),
                packageName = it.packageName,
                icon = it.loadIcon(pm)
            )
        }.sortedBy { it.name }
    }

    fun getAppName(context: Context, packageName: String): String {
        return try {
            val pm = context.packageManager
            val appInfo = pm.getApplicationInfo(packageName, 0)
            appInfo.loadLabel(pm).toString()
        } catch (e: Exception) {
            packageName
        }
    }
}