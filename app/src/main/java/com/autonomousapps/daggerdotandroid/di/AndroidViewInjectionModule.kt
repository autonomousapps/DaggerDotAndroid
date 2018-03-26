package com.autonomousapps.daggerdotandroid.di

import android.view.View
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.Multibinds

/**
 * Analogous to [dagger.android.support.AndroidSupportInjectionModule]. Dagger magic.
 */
@Module
abstract class AndroidViewInjectionModule {
    @Multibinds
    abstract fun viewInjectorFactories(): Map<Class<out View>, AndroidInjector.Factory<out View>>
}
