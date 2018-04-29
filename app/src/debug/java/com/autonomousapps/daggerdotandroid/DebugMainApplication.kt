package com.autonomousapps.daggerdotandroid

class DebugMainApplication : MainApplication() {

    fun setTestComponent(component: MainApplicationComponent) {
        component.inject(this)
    }
}