package com.morozov.songs.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.findNavController

import com.morozov.songs.R
import com.morozov.songs.services.SessionService
import com.morozov.songs.services.extensions.isEmail
import com.morozov.songs.services.extensions.startAnimation
import com.morozov.songs.services.extensions.stopAnimation


class AuthFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var signUpButton: Button
    private lateinit var signInButton: Button

    private lateinit var errorText: TextView

    private lateinit var progressBar: ProgressBar

    override fun onStop() {
        super.onStop()
        emailEditText.setText("")
        passwordEditText.setText("")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!SessionService.isInitialized()) {
            restoreSession()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_auth, container, false)
        initViewObjects(view)
        setupViewObjects()
        return view
    }

    private fun initViewObjects(view: View) {
        emailEditText = view.findViewById(R.id.fragment_auth_tf_email)
        passwordEditText = view.findViewById(R.id.fragment_auth_tf_password)

        signInButton = view.findViewById(R.id.fragment_auth_button_sign_in)
        signUpButton = view.findViewById(R.id.fragment_auth_button_sign_up)

        errorText = view.findViewById(R.id.fragment_auth_text_error)

        progressBar = view.findViewById(R.id.fragment_auth_progress_bar)
    }

    private fun setupViewObjects() {
        progressBar.visibility = View.GONE
        errorText.visibility = View.GONE

        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        signInButton.setOnClickListener {
            if (!validateInput()) {
                return@setOnClickListener
            }

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            this.startAnimation(progressBar)
            SessionService.signInEmail(email, password) { error ->
                this.stopAnimation(progressBar)
                if (error != null) {
                    errorText.visibility = View.VISIBLE
                } else {
                    errorText.visibility = View.GONE
                    this.view?.findNavController()?.navigate(R.id.action_authFragment_to_mainActivity)
                }
            }
        }

        signUpButton.setOnClickListener {
            if (!validateInput()) {
                return@setOnClickListener
            }

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            this.startAnimation(progressBar)
            SessionService.signUpEmail(email, password) { error ->
                this.stopAnimation(progressBar)
                if (error != null) {
                    errorText.visibility = View.VISIBLE
                } else {
                    errorText.visibility = View.GONE
                    this.view?.findNavController()?.navigate(R.id.action_authFragment_to_mainActivity)
                }
            }
        }
    }

    private fun restoreSession() {
        this.startAnimation(progressBar)

        val restoredSongsAuth = SessionService.restore { e ->
            this.stopAnimation(progressBar)
            if (e != null) {
                errorText.visibility = View.GONE
            } else {
                errorText.visibility = View.GONE
                this.view?.findNavController()?.navigate(R.id.action_authFragment_to_mainActivity)
            }
        }

        if (restoredSongsAuth != null) {
            emailEditText.setText(restoredSongsAuth.email)
            passwordEditText.setText(restoredSongsAuth.password)
        }
    }

    private fun validateInput(): Boolean {
        var ret = true
        if (!emailEditText.text.toString().isEmail()) {
            emailEditText.error = requireContext().getString(R.string.invalid_email)
            ret = false
        }
        if (passwordEditText.text.toString().length < 6) {
            passwordEditText.error = requireContext().getString(R.string.password_length_error)
            ret = false
        }
        return ret
    }
}