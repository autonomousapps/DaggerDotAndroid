package com.autonomousapps.daggerdotandroid.di

import android.app.Activity
import android.content.ContextWrapper
import android.view.View
import com.autonomousapps.daggerdotandroid.checkNotNull
import java.util.Locale

object ViewInjection {
    fun inject(view: View) {
        view.checkNotNull("view must not be null")
        val hasViewInjector = findHasViewInjector(view)
        val viewInjector = findHasViewInjector(view).viewInjector()
        viewInjector.checkNotNull("${hasViewInjector.javaClass}.viewInjector returned null")
        viewInjector.inject(view)
    }

    private fun findHasViewInjector(view: View): HasViewInjector {
        val activity = view.getActivity()
        if (activity !is HasViewInjector) {
            throw RuntimeException(
                String.format(Locale.US,
                    "%s does not implement %s",
                    activity.javaClass.canonicalName,
                    HasViewInjector::class.java.canonicalName
                )
            )
        }
        return activity
    }

    private fun View.getActivity(): Activity {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        throw IllegalStateException("Context does not stem from an activity: $context")
    }
}
