package com.autonomousapps.daggerdotandroid.di

import android.view.View
import dagger.android.DispatchingAndroidInjector

interface HasViewInjector {
    fun viewInjector(): DispatchingAndroidInjector<View>
}
