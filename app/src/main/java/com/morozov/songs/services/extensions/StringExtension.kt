package com.morozov.songs.services.extensions

import android.text.TextUtils
import android.util.Patterns

fun String.isEmail(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}