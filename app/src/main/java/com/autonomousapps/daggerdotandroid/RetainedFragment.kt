package com.autonomousapps.daggerdotandroid

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autonomousapps.daggerdotandroid.di.FragmentScoped
import com.autonomousapps.daggerdotandroid.di.HasViewInjector
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RetainedFragment : Fragment(), HasViewInjector {

    companion object {
        fun newInstance(): RetainedFragment {
            return RetainedFragment()
        }
    }

    @Inject lateinit var viewInjector: DispatchingAndroidInjector<View>
    override fun viewInjector() = viewInjector

    @Inject lateinit var colorFactory: ColorFactory

    override fun onAttach(context: Context) {
        if (!::colorFactory.isInitialized) {
            // It's very important to only inject this object once
            AndroidSupportInjection.inject(this)
        }
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_retained, container, false)
    }

    @Module
    abstract class RetainedFragmentModule {

        @Binds @FragmentScoped abstract fun bindColorFactory(factory: RealColorFactory): ColorFactory

        @Module
        companion object {
            @Provides @JvmStatic @FragmentScoped fun provideShouldDoThing(): Boolean {
                // Yes, do the thing!
                return true
            }
        }
    }
}
