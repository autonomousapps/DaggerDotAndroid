package com.autonomousapps.daggerdotandroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.autonomousapps.daggerdotandroid.di.HasViewInjector
import dagger.android.DispatchingAndroidInjector

class RetainedFragmentActivity : AppCompatActivity(), HasViewInjector {

    override fun viewInjector(): DispatchingAndroidInjector<View> {
        // HasViewInjector has some limitations when implemented by a fragment, so we have the activity
        // redirect to the fragment
        return (supportFragmentManager.findFragmentById(R.id.container) as RetainedFragment).viewInjector
    }

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, RetainedFragmentActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retained_fragment)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RetainedFragment.newInstance())
                .commit()
        }
    }
}
