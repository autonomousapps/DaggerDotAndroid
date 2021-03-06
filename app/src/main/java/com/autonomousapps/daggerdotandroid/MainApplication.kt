package com.autonomousapps.daggerdotandroid

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.v4.app.Fragment
import com.autonomousapps.daggerdotandroid.di.AndroidViewInjectionModule
import com.autonomousapps.daggerdotandroid.di.ScreenBindingModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import javax.inject.Singleton

// AS seems to have trouble with application classes written in Kotlin. Rest assured, this works fine. We're just
// suppressing an invalid warning.
@SuppressLint("Registered")
open class MainApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        DaggerMainApplication_MainApplicationComponent.builder()
            .app(this)
            .build()
            .inject(this)
    }

    @Inject protected lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    override fun supportFragmentInjector() = fragmentInjector

    @Singleton
    @Component(modules = [
        AndroidSupportInjectionModule::class,
        ScreenBindingModule::class,
        AndroidViewInjectionModule::class
    ])
    interface MainApplicationComponent {

        fun inject(app: MainApplication)

        @Component.Builder
        interface Builder {
            fun build(): MainApplicationComponent
            @BindsInstance fun app(app: Context): Builder
        }
    }
}