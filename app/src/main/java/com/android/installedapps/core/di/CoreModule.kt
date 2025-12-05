package com.android.installedapps.core.di

import com.android.installedapps.core.data.repository.AppsRepositoryImpl
import com.android.installedapps.core.domain.repository.AppsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {

    single<AppsRepository> { AppsRepositoryImpl(androidContext()) }
}
