package com.autonomousapps.daggerdotandroid

import android.support.annotation.ColorRes
import javax.inject.Inject

class RealColorFactory @Inject constructor() : ColorFactory {

    @ColorRes
    override fun getColor(): Int {
        return R.color.red
    }
}