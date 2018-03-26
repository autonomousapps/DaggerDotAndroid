package com.autonomousapps.daggerdotandroid

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.button
import kotlinx.android.synthetic.main.activity_main.counterButton
import kotlinx.android.synthetic.main.activity_main.counterText
import kotlinx.android.synthetic.main.activity_main.textView
import javax.inject.Inject

@Module
object MainActivityModule {
    @Provides @JvmStatic fun provideText() = "Why hello there!"
    @Provides @JvmStatic fun provideMutableObject() = MutableObject()
}

class MainActivity : AppCompatActivity() {

    @Inject lateinit var text: String
    @Inject lateinit var viewModelFactory: MainActivityViewModelFactory

    private val viewModel: MainActivityViewModel by lazy(mode = LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.text = text
        button.setOnClickListener {
            startActivity(RetainedFragmentActivity.getLaunchIntent(this))
        }

        viewModel.getCounter().observe(this, Observer {
            counterText.text = it.toString()
        })
        counterButton.setOnClickListener {
            viewModel.onClickCounter()
        }
    }
}

class MutableObject(var counter: Int = 0)

class MainActivityViewModel(private val mutableObject: MutableObject) : ViewModel() {

    private val counter: MutableLiveData<Int> = MutableLiveData()

    init {
        counter.value = mutableObject.counter
    }

    fun getCounter(): LiveData<Int> = counter

    fun onClickCounter() {
        counter.value = ++mutableObject.counter
    }
}

class MainActivityViewModelFactory @Inject constructor(
    private val mutableObject: MutableObject
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainActivityViewModel::class.java) -> MainActivityViewModel(mutableObject) as T
            else -> throw IllegalArgumentException("${modelClass.simpleName} is an unknown type of view model")
        }
    }
}
