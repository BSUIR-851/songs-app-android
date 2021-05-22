package com.morozov.songs.models.songs.auth

import com.morozov.songs.models.songs.auth.SongsUser

data class UsersDashboard (
    var users: ArrayList<SongsUser> = ArrayList()
)