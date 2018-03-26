package com.autonomousapps.daggerdotandroid.di

import android.view.View
import dagger.MapKey
import kotlin.reflect.KClass

/** [MapKey] annotation to key bindings by a type of a [View]. */
@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class ViewKey(val value: KClass<out View>)