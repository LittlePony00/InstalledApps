package com.android.installedapps

import android.app.Application
import com.android.installedapps.core.di.coreModule
import com.android.installedapps.feature.app_detail.di.appDetailModule
import com.android.installedapps.feature.apps_list.di.appsListModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class InstalledAppsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@InstalledAppsApplication)

            modules(
                coreModule,
                appsListModule,
                appDetailModule
            )
        }
    }
}
