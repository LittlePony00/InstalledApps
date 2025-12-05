package com.android.installedapps.feature.apps_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.installedapps.core.domain.repository.AppsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppsListViewModel(
    private val repository: AppsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AppsListContract.State())
    val state: StateFlow<AppsListContract.State> = _state
        .onSubscription {
            // if app was in background and user installed new app, then we updated our list.
            handleIntent(AppsListContract.Intent.LoadApps)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            _state.value
        )

    private val _action = Channel<AppsListContract.Action>(Channel.BUFFERED)
    val action = _action.receiveAsFlow()

    init {

    }

    fun handleIntent(intent: AppsListContract.Intent) {
        when (intent) {
            is AppsListContract.Intent.LoadApps -> loadApps()
            is AppsListContract.Intent.OpenAppDetail -> openAppDetail(intent.packageName)
        }
    }

    private fun loadApps() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val apps = repository.getInstalledApps()
                _state.update { it.copy(apps = apps, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    private fun openAppDetail(packageName: String) {
        viewModelScope.launch {
            _action.send(AppsListContract.Action.NavigateToDetail(packageName))
        }
    }
}

