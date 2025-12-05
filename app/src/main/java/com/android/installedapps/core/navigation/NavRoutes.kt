package com.android.installedapps.core.navigation

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object NavRoutes {
    const val APPS_LIST = "apps_list"
    const val APP_DETAIL = "app_detail/{packageName}"

    fun appDetail(packageName: String): String {
        val encoded = URLEncoder.encode(packageName, StandardCharsets.UTF_8.toString())
        return "app_detail/$encoded"
    }

    fun decodePackageName(packageName: String): String {
        return URLDecoder.decode(packageName, StandardCharsets.UTF_8.toString())
    }
}
