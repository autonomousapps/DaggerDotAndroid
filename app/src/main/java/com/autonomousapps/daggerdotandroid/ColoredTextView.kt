package com.autonomousapps.daggerdotandroid

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.View
import com.autonomousapps.daggerdotandroid.di.ViewInjection
import com.autonomousapps.daggerdotandroid.di.ViewKey
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Inject

class ColoredTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    @Inject lateinit var colorFactory: ColorFactory

    init {
        initColor()
    }

    private fun initColor() {
        if (isInEditMode) return

        ViewInjection.inject(this)
        @ColorRes val colorRes = colorFactory.getColor()
        setTextColor(ContextCompat.getColor(context, colorRes))
    }
}

@Module(subcomponents = [ColoredTextViewModule.ColoredTextViewComponent::class])
abstract class ColoredTextViewModule {

    // Creates a new subcomponent for each view
    @Binds @IntoMap @ViewKey(ColoredTextView::class)
    abstract fun bindAndroidInjectorFactoryForColoredTextView(
        builder: ColoredTextViewComponent.Builder
    ): AndroidInjector.Factory<out View>

    @Subcomponent
    interface ColoredTextViewComponent : AndroidInjector<ColoredTextView> {
        @Subcomponent.Builder
        abstract class Builder : AndroidInjector.Builder<ColoredTextView>()
    }
}