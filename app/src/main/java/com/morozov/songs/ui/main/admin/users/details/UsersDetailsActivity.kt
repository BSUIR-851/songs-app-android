package com.morozov.songs.ui.main.admin.users.details

import android.os.Bundle
import com.morozov.songs.R
import com.morozov.songs.ui.Activity

class UsersDetailsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.details)
    }
}