package com.morozov.songs

import java.util.*
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

import com.zeugmasolutions.localehelper.LocaleAwareApplication
import com.zeugmasolutions.localehelper.Locales

import com.morozov.songs.services.Constants
import com.morozov.songs.services.SharedPreferencesService
import com.morozov.songs.services.TypefaceService

class App : LocaleAwareApplication() {

    override fun onCreate() {
        super.onCreate()
        when (SharedPreferencesService.standard.getString(Constants.SharedPreferences.Standard.FORCED_THEME_KEY, "null")) {
            Constants.SharedPreferences.Standard.FORCED_THEME_LIGHT_VALUE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            Constants.SharedPreferences.Standard.FORCED_THEME_DARK_VALUE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        val fontName: String? = SharedPreferencesService.standard.getString(Constants.SharedPreferences.Standard.FONT_KEY, "")
        if (!fontName.isNullOrBlank()) {
            TypefaceService.overrideAllFontFields(this, "fonts/" + fontName)
        }
    }

    init {
        instance = this
        systemLocale = Locale.getDefault()
    }

    companion object {
        private var systemLocale: Locale? = null
        private var instance: App? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun systemLocale(): Locale {
            return systemLocale ?: Locales.English
        }
    }

}