package com.autonomousapps.daggerdotandroid.di

import com.autonomousapps.daggerdotandroid.ColoredTextViewModule
import com.autonomousapps.daggerdotandroid.MainActivity
import com.autonomousapps.daggerdotandroid.MainActivityModule
import com.autonomousapps.daggerdotandroid.RetainedFragment
import com.autonomousapps.daggerdotandroid.SimpleFragmentActivity
import com.autonomousapps.daggerdotandroid.SimpleFragmentActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ScreenBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainActivityModule::class]) abstract fun mainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [
        SimpleFragmentActivityModule::class
    ]) abstract fun simpleFragmentActivity(): SimpleFragmentActivity

    // RetainedFragment is declared here because it has no dependencies on its host Activity, and is
    // retained, meaning it survives activity death.
    @FragmentScoped
    @ContributesAndroidInjector(modules = [
        RetainedFragment.RetainedFragmentModule::class,
        ColoredTextViewModule::class
    ]) abstract fun upgradeRetainedFragment(): RetainedFragment
}