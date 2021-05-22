package com.morozov.songs.ui.main.songs.details

import android.os.Bundle

import com.morozov.songs.R
import com.morozov.songs.ui.Activity

class SongsDetailsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.details)
    }
}