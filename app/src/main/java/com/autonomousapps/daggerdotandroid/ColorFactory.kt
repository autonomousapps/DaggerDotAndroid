package com.autonomousapps.daggerdotandroid

import android.support.annotation.ColorRes

interface ColorFactory {

    @ColorRes fun getColor(): Int
}