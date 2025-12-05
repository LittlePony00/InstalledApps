package com.android.installedapps.feature.apps_list.di

import com.android.installedapps.feature.apps_list.presentation.AppsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appsListModule = module {
    viewModel { AppsListViewModel(get()) }
}

