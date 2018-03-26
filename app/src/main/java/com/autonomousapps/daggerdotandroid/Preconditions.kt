package com.autonomousapps.daggerdotandroid

fun <T> T?.checkNotNull(): T {
    return checkNotNull(null)
}

fun <T> T?.checkNotNull(errorMessage: Any?): T {
    return this ?: throw NullPointerException(errorMessage.toString())
}