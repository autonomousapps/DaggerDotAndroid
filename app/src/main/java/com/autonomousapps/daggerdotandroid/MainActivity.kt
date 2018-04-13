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
import kotlinx.android.synthetic.main.activity_main.counterButton
import kotlinx.android.synthetic.main.activity_main.counterText
import kotlinx.android.synthetic.main.activity_main.launchFragmentButton
import kotlinx.android.synthetic.main.activity_main.launchRetainedFragmentButton
import kotlinx.android.synthetic.main.activity_main.textView
import javax.inject.Inject

@Module
object MainActivityModule {
    @Provides @JvmStatic fun provideText() = "Why hello there!"
    @Provides @JvmStatic fun provideMutableObjectFactory() = MutableObjectFactory()
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

        launchFragmentButton.setOnClickListener {
            startActivity(SimpleFragmentActivity.getLaunchIntent(this))
        }

        launchRetainedFragmentButton.setOnClickListener {
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

/**
 * Let's assume that [MutableObject] is very heavy, and we don't want to create a new one unless we really have to.
 * If we provided one directly via Dagger, then on each rotation a new instance would be created, even though it
 * would never get used.
 */
class MutableObjectFactory {
    fun newMutableObject() = MutableObject()
}

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
    private val mutableObjectFactory: MutableObjectFactory
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainActivityViewModel::class.java) -> {
                val mutableObject = mutableObjectFactory.newMutableObject()
                MainActivityViewModel(mutableObject) as T
            }
            else -> throw IllegalArgumentException("${modelClass.simpleName} is an unknown type of view model")
        }
    }
}
