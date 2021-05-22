package com.morozov.songs.ui.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.morozov.songs.R
import com.morozov.songs.ui.Activity
import com.morozov.songs.services.SessionService

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SessionService.getSongsUser()?.isBlocked == true) {
            setContentView(R.layout.activity_blocked)
        } else {
            var bottomNavigationView: BottomNavigationView
            var navController: NavController
            if (SessionService.getSongsUser()?.isAdmin == true) {
                setContentView(R.layout.activity_main_admin)
                bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view_admin)
                navController = findNavController(R.id.activity_main_nav_host_fragment_admin)
            } else {
                setContentView(R.layout.activity_main)
                bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view)
                navController = findNavController(R.id.activity_main_nav_host_fragment)
            }

            val appBarConfiguration = AppBarConfiguration(
                setOf (
                    R.id.googleMapsFragment,
                    R.id.songsFragment,
                    R.id.settingsFragment,
                    R.id.adminPanelFragment
                )
            )

            setupActionBarWithNavController(navController, appBarConfiguration)
            bottomNavigationView.setupWithNavController(navController)
        }
    }

}