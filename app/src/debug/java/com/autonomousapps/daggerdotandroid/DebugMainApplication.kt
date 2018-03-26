package com.autonomousapps.daggerdotandroid

class DebugMainApplication : MainApplication() {

    fun setTestComponent(component: MainApplicationComponent) {
        mainApplicationComponent = component
        mainApplicationComponent.inject(this)
    }
}