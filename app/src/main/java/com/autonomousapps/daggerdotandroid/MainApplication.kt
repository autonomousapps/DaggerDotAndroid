package com.autonomousapps.daggerdotandroid

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.v4.app.Fragment
import com.autonomousapps.daggerdotandroid.di.AndroidViewInjectionModule
import com.autonomousapps.daggerdotandroid.di.ScreenBindingModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import javax.inject.Singleton

open class MainApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        mainApplicationComponent = DaggerMainApplication_MainApplicationComponent.builder()
            .app(this)
            .build()
        mainApplicationComponent.inject(this)
    }

    @Inject protected lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector() = dispatchingAndroidInjector

    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    override fun supportFragmentInjector() = fragmentInjector

    protected lateinit var mainApplicationComponent: MainApplicationComponent

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