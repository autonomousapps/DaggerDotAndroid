package com.autonomousapps.daggerdotandroid.di

import javax.inject.Scope

/**
 * Identifies a type that the injector only instantiates once per Activity instance. Not inherited.
 *
 * @see [Scope]
 */
@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentScoped