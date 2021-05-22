package com.morozov.songs.ui.main.settings

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.zeugmasolutions.localehelper.Locales

import com.morozov.songs.App
import com.morozov.songs.R
import com.morozov.songs.services.Constants
import com.morozov.songs.services.SessionService
import com.morozov.songs.services.SharedPreferencesService
import com.morozov.songs.services.TypefaceService
import com.morozov.songs.ui.Activity


class SettingsFragment : Fragment() {
    private lateinit var roleValueTextView: TextView

    private lateinit var themeSystemButton: Button
    private lateinit var themeLightButton: Button
    private lateinit var themeDarkButton: Button

    private lateinit var languageSystemButton: Button
    private lateinit var languageEnglishButton: Button
    private lateinit var languageRussianButton: Button

    private lateinit var fontSpinner: Spinner
    private lateinit var availableFonts: Array<String?>

    private lateinit var logOutButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        initViewObjects(view)
        setupViewObjects()
        return view
    }

    private fun initViewObjects(view: View) {
        initRoleTextView(view)

        themeSystemButton = view.findViewById(R.id.fragment_settings_button_theme_system)
        themeLightButton = view.findViewById(R.id.fragment_settings_button_theme_light)
        themeDarkButton = view.findViewById(R.id.fragment_settings_button_theme_dark)

        languageSystemButton = view.findViewById(R.id.fragment_settings_button_language_system)
        languageEnglishButton = view.findViewById(R.id.fragment_settings_button_language_english)
        languageRussianButton = view.findViewById(R.id.fragment_settings_button_language_russian)

        initSpinnerObject(view)

        logOutButton = view.findViewById(R.id.fragment_settings_button_log_out)
    }

    private fun initRoleTextView(view: View) {
        roleValueTextView = view.findViewById(R.id.fragment_settings_textview_role_value)
        if (SessionService.getSongsUser()?.isAdmin == true) {
            roleValueTextView.setText(R.string.admin_role)
        } else {
            roleValueTextView.setText(R.string.user_role)
        }
    }

    private fun initSpinnerObject(view: View) {
        fontSpinner = view.findViewById(R.id.fragment_settings_spinner_font)
        availableFonts = TypefaceService.getAvailableFonts(requireContext().assets, "fonts")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, availableFonts)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        fontSpinner.adapter = adapter
        val fontName: String? = SharedPreferencesService.standard.getString(Constants.SharedPreferences.Standard.FONT_KEY, "")
        if (!fontName.isNullOrBlank()) {
            fontSpinner.setSelection(availableFonts.indexOf(fontName))
        }
    }

    private fun setupViewObjects() {
        setupButtonListeners()
        setupSpinnerListeners()
    }

    private fun setupButtonListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }

        themeSystemButton.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            with(SharedPreferencesService.standard.edit()) {
                remove(Constants.SharedPreferences.Standard.FORCED_THEME_KEY)
                apply()
            }
        }

        themeLightButton.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            with(SharedPreferencesService.standard.edit()) {
                putString(Constants.SharedPreferences.Standard.FORCED_THEME_KEY, Constants.SharedPreferences.Standard.FORCED_THEME_LIGHT_VALUE)
                apply()
            }
        }

        themeDarkButton.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            with(SharedPreferencesService.standard.edit()) {
                putString(Constants.SharedPreferences.Standard.FORCED_THEME_KEY, Constants.SharedPreferences.Standard.FORCED_THEME_DARK_VALUE)
                apply()
            }
        }

        languageSystemButton.setOnClickListener {
            (requireActivity() as Activity).updateLocale(App.systemLocale())
        }

        languageEnglishButton.setOnClickListener {
            (requireActivity() as Activity).updateLocale(Locales.English)
        }

        languageRussianButton.setOnClickListener {
            (requireActivity() as Activity).updateLocale(Locales.Russian)
        }

        logOutButton.setOnClickListener {
            SessionService.destroy()
            requireActivity().finish()
        }
    }

    private fun setupSpinnerListeners() {
        fontSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val fontName: String = parent.getItemAtPosition(position).toString()
                TypefaceService.overrideAllFontFields(requireContext(), "fonts/" + fontName)
                with(SharedPreferencesService.standard.edit()) {
                    putString(Constants.SharedPreferences.Standard.FONT_KEY, fontName)
                    apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }
}