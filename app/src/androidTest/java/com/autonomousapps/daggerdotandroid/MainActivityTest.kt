package com.autonomousapps.daggerdotandroid

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Singleton

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule private var activityTestRule = ActivityTestRule(
        MainActivity::class.java,
        false,
        false
    )

    @Singleton
    @Component(modules = [
        AndroidSupportInjectionModule::class,
        TestMainActivityModule::class
    ])
    interface TestMainApplicationComponent : MainApplication.MainApplicationComponent {

        @Component.Builder
        interface Builder {
            @BindsInstance fun app(app: MainApplication): Builder
            @BindsInstance fun text(text: String): Builder
            fun build(): TestMainApplicationComponent
        }
    }

    @Module abstract class TestMainActivityModule {
        @ContributesAndroidInjector abstract fun mainActivity(): MainActivity
    }

    @Before
    fun setUp() {
        val app = InstrumentationRegistry.getTargetContext().applicationContext as DebugMainApplication
        val mainComponent = DaggerMainActivityTest_TestMainApplicationComponent.builder()
            .app(app)
            .text("I'm a test!")
            .build()
        app.setTestComponent(mainComponent)

        activityTestRule.launchActivity(null)
    }

    @Test fun verifyText() {
        onView(withText("I'm a test!")).check(matches(isDisplayed()))
    }
}