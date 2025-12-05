package com.android.installedapps.core.domain.model

import android.graphics.drawable.Drawable

data class AppInfo(
    val name: String,
    val packageName: String,
    val version: String,
    val icon: Drawable?,
    val apkPath: String
)
