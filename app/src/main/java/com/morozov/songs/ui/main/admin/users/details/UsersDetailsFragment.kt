package com.morozov.songs.ui.main.admin.users.details

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.morozov.songs.R
import com.morozov.songs.models.songs.auth.SongsUser
import com.morozov.songs.services.SessionService
import com.morozov.songs.ui.main.MainActivity
import java.lang.Exception

class UsersDetailsFragment: Fragment() {
    private lateinit var idTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var roleTextView: TextView

    private lateinit var activeButton: Button
    private lateinit var blockButton: Button

    private lateinit var makeUserButton: Button
    private lateinit var makeAdminButton: Button

    override fun onResume() {
        super.onResume()
        syncWithSelected()

        if (SessionService.selectedUser == null) {
            requireActivity().onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_users_details, container, false)
        initViewObjects(view)
        setupViewObjects()
        return view
    }

    private fun initViewObjects(view: View) {
        idTextView = view.findViewById(R.id.fragment_users_details_textview_id_value)
        emailTextView = view.findViewById(R.id.fragment_users_details_textview_email_value)
        statusTextView = view.findViewById(R.id.fragment_users_details_textview_status_value)
        roleTextView = view.findViewById(R.id.fragment_users_details_textview_role_value)

        activeButton = view.findViewById(R.id.fragment_users_details_button_active)
        blockButton = view.findViewById(R.id.fragment_users_details_button_blocked)

        makeUserButton = view.findViewById(R.id.fragment_users_details_button_user)
        makeAdminButton = view.findViewById(R.id.fragment_users_details_button_admin)
    }

    private fun setupViewObjects() {
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        activeButton.setOnClickListener {
            if (SessionService.selectedUser != null) {
                SessionService.selectedUser?.isBlocked = false
                SessionService.updateRemoteUser(SessionService.selectedUser!!) { error ->
                    handleChangeResult(error)
                }
            }
        }
        blockButton.setOnClickListener {
            if (SessionService.selectedUser != null) {
                SessionService.selectedUser?.isBlocked = true
                SessionService.updateRemoteUser(SessionService.selectedUser!!) { error ->
                    handleChangeResult(error)
                }
            }
        }
        makeUserButton.setOnClickListener {
            if (SessionService.selectedUser != null) {
                SessionService.selectedUser?.isAdmin = false
                SessionService.updateRemoteUser(SessionService.selectedUser!!) { error ->
                    handleChangeResult(error)
                }
            }
        }
        makeAdminButton.setOnClickListener {
            if (SessionService.selectedUser != null) {
                SessionService.selectedUser?.isAdmin = true
                SessionService.updateRemoteUser(SessionService.selectedUser!!) { error ->
                    handleChangeResult(error)
                }
            }
        }
    }

    private fun handleChangeResult(error: Exception?) {
        var title: Int = R.string.success
        var msg: Int = R.string.success_change
        if (error != null) {
            println(error)
            title = R.string.error
            msg = R.string.something_went_wrong
        }
        showAlert(title, msg)
        syncWithSelected()
    }

    private fun showAlert(title: Int, msg: Int) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(title)
        alertDialog.setMessage(msg)
        alertDialog.setPositiveButton("OK") { dialog, id ->
            dialog.dismiss()
        }
        val alert = alertDialog.create()
        alert.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun syncWithSelected() {
        val user = SessionService.selectedUser
        if (user != null) {
            idTextView.setText(user.id)
            emailTextView.setText(user.email)
            if (user.isBlocked) {
                statusTextView.setText(R.string.blocked)
            } else {
                statusTextView.setText(R.string.active)
            }
            if (user.isAdmin) {
                roleTextView.setText(R.string.admin_role)
            } else {
                roleTextView.setText(R.string.user_role)
            }
        }
    }
}