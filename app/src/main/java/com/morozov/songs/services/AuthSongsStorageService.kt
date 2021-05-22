package com.morozov.songs.services

import com.morozov.songs.models.songs.auth.SongsAuth
import com.morozov.songs.services.Constants
import com.morozov.songs.services.SharedPreferencesService

object AuthSongsStorageService {
    private val preferences = SharedPreferencesService.encrypted

    fun save(songsAuth: SongsAuth) {
        with(preferences.edit()) {
            putString(Constants.SharedPreferences.Encrypted.EMAIL_KEY, songsAuth.email)
            putString(Constants.SharedPreferences.Encrypted.PASSWORD_KEY, songsAuth.password)
            commit()
        }
    }

    fun delete() {
        with(preferences.edit()) {
            remove(Constants.SharedPreferences.Encrypted.EMAIL_KEY)
            remove(Constants.SharedPreferences.Encrypted.PASSWORD_KEY)
            commit()
        }
    }

    fun restore(): SongsAuth? {
        val email = preferences.getString(Constants.SharedPreferences.Encrypted.EMAIL_KEY, null)
        val password = preferences.getString(Constants.SharedPreferences.Encrypted.PASSWORD_KEY, null)
        if (email != null && password != null) {
            return SongsAuth(email, password)
        }
        return null
    }
}