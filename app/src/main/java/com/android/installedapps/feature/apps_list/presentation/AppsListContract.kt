package com.android.installedapps.feature.apps_list.presentation

import com.android.installedapps.core.domain.model.AppInfo

object AppsListContract {

    data class State(
        val apps: List<AppInfo> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed class Intent {
        data object LoadApps : Intent()
        data class OpenAppDetail(val packageName: String) : Intent()
    }

    sealed class Action {
        data class NavigateToDetail(val packageName: String) : Action()
    }
}

