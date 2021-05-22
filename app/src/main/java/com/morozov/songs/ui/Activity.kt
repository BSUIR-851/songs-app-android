package com.morozov.songs.ui

import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity

open class Activity: LocaleAwareCompatActivity() {

    var blockBackButton: Boolean = false

    override fun onBackPressed() {
        if (!blockBackButton) {
            super.onBackPressed()
        } else {
            println("Blocked back press due to running animation")
        }
    }

}