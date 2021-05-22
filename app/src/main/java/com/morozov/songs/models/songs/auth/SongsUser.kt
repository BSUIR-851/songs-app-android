package com.morozov.songs.models.songs.auth

data class SongsUser (
    val id: String,
    val email: String,
    var isAdmin: Boolean = false,
    var isBlocked: Boolean = false
)