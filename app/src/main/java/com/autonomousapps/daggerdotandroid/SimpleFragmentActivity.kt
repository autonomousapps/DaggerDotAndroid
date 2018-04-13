package com.autonomousapps.daggerdotandroid

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_simple.textView
import javax.inject.Inject

@Module abstract class SimpleFragmentActivityModule {

    @ContributesAndroidInjector abstract fun simpleFragment(): SimpleFragment

    @Module companion object {
        @Provides @JvmStatic fun provideText() = "Custom argument provided by dagger"
    }
}

class SimpleFragmentActivity : AppCompatActivity(), HasSupportFragmentInjector {

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, SimpleFragmentActivity::class.java)
        }
    }

    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    @Inject lateinit var viewModelFactory: SimpleFragmentActivityViewModelFactory
    private val viewModel: SimpleFragmentActivityViewModel by lazy(mode = LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, viewModelFactory).get(SimpleFragmentActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_fragment)

        viewModel.getBoolean().observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SimpleFragment.newInstance(R.color.green))
                    .commit()
            }
        })
    }
}

class SimpleFragment : Fragment() {

    companion object {

        private const val ARG_COLOR = "arg_color"

        fun newInstance(@ColorRes color: Int): SimpleFragment {
            return SimpleFragment().apply {
                // bundleOf() from android-ktx
                arguments = bundleOf(ARG_COLOR to color)
            }
        }
    }

    private val color: Int by lazy(mode = LazyThreadSafetyMode.NONE) {
        val color = arguments!!.getInt(ARG_COLOR)
        ContextCompat.getColor(context!!, color)
    }

    @Inject lateinit var viewModelFactory: SimpleFragmentActivityViewModelFactory
    private val viewModel: SimpleFragmentActivityViewModel by lazy(mode = LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(activity!!, viewModelFactory).get(SimpleFragmentActivityViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getString().observe(this, Observer {
            textView.text = SpannableString(it).apply {
                setSpan(
                    ForegroundColorSpan(color),
                    0, length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simple, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        @ColorRes val color = arguments?.getInt(ARG_COLOR)
            ?: throw IllegalStateException("You must supply an argument!")

        val spannable = SpannableString(textView.text)
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(view.context, color)),
            0, spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannable
    }
}

class SimpleFragmentActivityViewModelFactory @Inject constructor(
    private val text: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SimpleFragmentActivityViewModel::class.java) -> {
                SimpleFragmentActivityViewModel(text) as T
            }
            else -> throw IllegalArgumentException("${modelClass.simpleName} is an unknown type of view model")
        }
    }
}

class SimpleFragmentActivityViewModel @Inject constructor(text: String) : ViewModel() {

    private val boolean: MutableLiveData<Boolean> = MutableLiveData()
    private val string: MutableLiveData<String> = MutableLiveData()

    init {
        boolean.value = true
        string.value = text
    }

    fun getBoolean(): LiveData<Boolean> = boolean
    fun getString(): LiveData<String> = string
}
