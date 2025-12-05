package com.android.installedapps.core.data.repository

import android.content.Context
import android.content.pm.PackageManager
import com.android.installedapps.core.domain.model.AppDetailInfo
import com.android.installedapps.core.domain.model.AppInfo
import com.android.installedapps.core.domain.repository.AppsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.security.MessageDigest

class AppsRepositoryImpl(
    private val context: Context
) : AppsRepository {

    override suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        packages.mapNotNull { appInfo ->
            try {
                val packageInfo = packageManager.getPackageInfo(appInfo.packageName, 0)
                AppInfo(
                    name = appInfo.loadLabel(packageManager).toString(),
                    packageName = appInfo.packageName,
                    version = packageInfo.versionName ?: "N/A",
                    icon = appInfo.loadIcon(packageManager),
                    apkPath = appInfo.sourceDir
                )
            } catch (e: Exception) {
                null
            }
        }.sortedBy { it.name.lowercase() }
    }

    override suspend fun getAppDetail(packageName: String): AppDetailInfo? = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        try {
            val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val checksum = calculateMd5(appInfo.sourceDir)

            AppDetailInfo(
                name = appInfo.loadLabel(packageManager).toString(),
                packageName = appInfo.packageName,
                version = packageInfo.versionName ?: "N/A",
                icon = appInfo.loadIcon(packageManager),
                checksum = checksum
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun calculateMd5(filePath: String): String {
        val file = File(filePath)
        val digest = MessageDigest.getInstance("MD5")
        file.inputStream().use { fis ->
            val buffer = ByteArray(8192)
            var bytesRead: Int
            while (fis.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }
}
