package com.autonomousapps.daggerdotandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.textView
import javax.inject.Inject

@Module
object MainActivityModule {
    @Provides @JvmStatic fun provideText() = "Why hello there!"
}

class MainActivity : AppCompatActivity() {

    @Inject lateinit var text: String

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.text = text
    }
}
