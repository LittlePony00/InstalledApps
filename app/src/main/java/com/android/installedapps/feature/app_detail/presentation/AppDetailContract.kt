package com.android.installedapps.feature.app_detail.presentation

import com.android.installedapps.core.domain.model.AppDetailInfo

object AppDetailContract {

    data class State(
        val appDetail: AppDetailInfo? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed class Intent {
        data class LoadAppDetail(val packageName: String) : Intent()
        data object LaunchApp : Intent()
        data object NavigateBack : Intent()
    }

    sealed class Action {
        data class LaunchApp(val packageName: String) : Action()
        data object NavigateBack : Action()
    }
}
