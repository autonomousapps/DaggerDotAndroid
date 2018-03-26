package com.autonomousapps.daggerdotandroid.di

import com.autonomousapps.daggerdotandroid.MainActivity
import com.autonomousapps.daggerdotandroid.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ScreenBindingModule {
    @ContributesAndroidInjector(modules = [MainActivityModule::class]) abstract fun mainActivity(): MainActivity
}