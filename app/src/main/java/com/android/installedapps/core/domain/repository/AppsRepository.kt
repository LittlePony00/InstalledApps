package com.android.installedapps.core.domain.repository

import com.android.installedapps.core.domain.model.AppDetailInfo
import com.android.installedapps.core.domain.model.AppInfo

interface AppsRepository {
    suspend fun getInstalledApps(): List<AppInfo>
    suspend fun getAppDetail(packageName: String): AppDetailInfo?
}
