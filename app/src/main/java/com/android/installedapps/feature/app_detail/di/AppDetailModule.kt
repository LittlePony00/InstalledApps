package com.android.installedapps.feature.app_detail.di

import com.android.installedapps.feature.app_detail.presentation.AppDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appDetailModule = module {

    viewModel { AppDetailViewModel(get(), get()) }
}
