package com.android.installedapps.feature.app_detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.installedapps.core.domain.repository.AppsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppDetailViewModel(
    private val repository: AppsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val packageName: String = checkNotNull(savedStateHandle["packageName"])

    private val _state = MutableStateFlow(AppDetailContract.State())
    val state: StateFlow<AppDetailContract.State> = _state
        .onSubscription {
            // if info about app was changed while we were in background, then we updated it
            handleIntent(AppDetailContract.Intent.LoadAppDetail(packageName))
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            _state.value
        )

    private val _action = Channel<AppDetailContract.Action>(Channel.BUFFERED)
    val action = _action.receiveAsFlow()

    fun handleIntent(intent: AppDetailContract.Intent) {
        when (intent) {
            is AppDetailContract.Intent.LoadAppDetail -> loadAppDetail(intent.packageName)
            is AppDetailContract.Intent.LaunchApp -> launchApp()
            is AppDetailContract.Intent.NavigateBack -> navigateBack()
        }
    }

    private fun loadAppDetail(packageName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val detail = repository.getAppDetail(packageName)
                if (detail != null) {
                    _state.update { it.copy(appDetail = detail, isLoading = false) }
                } else {
                    _state.update { it.copy(error = "App not found", isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    private fun launchApp() {
        viewModelScope.launch {
            _action.send(AppDetailContract.Action.LaunchApp(packageName))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _action.send(AppDetailContract.Action.NavigateBack)
        }
    }
}
