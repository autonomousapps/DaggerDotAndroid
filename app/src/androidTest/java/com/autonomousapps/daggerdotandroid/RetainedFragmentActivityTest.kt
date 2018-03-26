package com.autonomousapps.daggerdotandroid

import android.support.annotation.ColorRes
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.hasTextColor
import android.support.test.espresso.matcher.ViewMatchers.withId
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
class RetainedFragmentActivityTest {

    @get:Rule private var activityTestRule = ActivityTestRule(
        RetainedFragmentActivity::class.java,
        false,
        false
    )

    @Singleton
    @Component(modules = [
        AndroidSupportInjectionModule::class,
        TestRetainedFragmentActivityModule::class
    ])
    interface TestRetainedFragmentActivityApplicationComponent : MainApplication.MainApplicationComponent {

        @Component.Builder
        interface Builder {
            @BindsInstance fun app(app: MainApplication): Builder
            @BindsInstance fun colorFactory(colorFactory: ColorFactory): Builder
            fun build(): TestRetainedFragmentActivityApplicationComponent
        }
    }

    @Module abstract class TestRetainedFragmentActivityModule {
        @ContributesAndroidInjector abstract fun retainedFragmentActivity(): RetainedFragmentActivity
        @ContributesAndroidInjector(modules = [ColoredTextViewModule::class]) abstract fun retainedFragment(): RetainedFragment
    }

    @ColorRes private val green = R.color.green

    @Before fun setUp() {
        val app = InstrumentationRegistry.getTargetContext().applicationContext as DebugMainApplication
        val mainComponent = DaggerRetainedFragmentActivityTest_TestRetainedFragmentActivityApplicationComponent.builder()
            .app(app)
            .colorFactory(FakeColorFactory(green))
            .build()
        app.setTestComponent(mainComponent)

        activityTestRule.launchActivity(null)
    }

    @Test fun verifyColor() {
        onView(withId(R.id.coloredTextView)).check(matches(hasTextColor(green)))
    }
}

class FakeColorFactory(private val colorRes: Int) : ColorFactory {
    @ColorRes override fun getColor() = colorRes
}
