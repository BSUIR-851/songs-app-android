package com.morozov.songs.services

import android.R.attr.path
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Typeface
import java.io.File
import java.io.IOException
import java.nio.file.Paths


object TypefaceService {
    fun overrideFont(context: Context, staticTypefaceFieldName: String, fontAssetName: String) {
        val newTypeface: Typeface = Typeface.createFromAsset(context.assets, fontAssetName)
        replaceFont(staticTypefaceFieldName, newTypeface)
    }

    fun replaceFont(staticTypefaceFieldName: String, newTypeface: Typeface) {
        try {
            var staticField = Typeface::class.java.getDeclaredField(staticTypefaceFieldName)
            staticField.isAccessible = true
            staticField.set(null, newTypeface)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    fun overrideAllFontFields(context: Context, fontAssetName: String) {
        overrideFont(context, "DEFAULT", fontAssetName)
        overrideFont(context, "MONOSPACE", fontAssetName)
        overrideFont(context, "SERIF", fontAssetName)
        overrideFont(context, "SANS_SERIF", fontAssetName)
    }

    fun getAvailableFonts(assets: AssetManager, fontsFolder: String): Array<String?> {
        var fileNames = arrayOfNulls<String>(0)
        try {
            val files = assets.list(fontsFolder) as Array<String>
            if (files.isNotEmpty()) {
                fileNames = arrayOfNulls<String>(files.size)
                files.mapIndexed { index, file ->
                    fileNames[index] = file
                }
            }
        } catch (e: IOException) {
            return fileNames
        }

        return fileNames
    }
}