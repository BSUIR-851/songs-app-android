package com.morozov.songs.services.extensions

import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.fragment.app.Fragment

import com.morozov.songs.ui.Activity

fun Fragment.startAnimation(progressBar: ProgressBar) {
    val activity: Activity = activity as Activity
    activity.blockBackButton = true
    activity.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    progressBar.visibility = View.VISIBLE
}

fun Fragment.stopAnimation(progressBar: ProgressBar) {
    val activity: Activity = activity as Activity
    activity.blockBackButton = false
    activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    progressBar.visibility = View.INVISIBLE
}
