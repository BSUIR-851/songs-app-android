package com.morozov.songs.ui.main.admin.users

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.morozov.songs.R
import com.morozov.songs.models.songs.auth.SongsUser

class UsersAdapter (private val context: Context, private val users: ArrayList<SongsUser>): BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return users.count()
    }

    override fun getItem(position: Int): Any {
        return users[position]
    }

    override fun getItemId(position: Int): Long {
        return users[position].hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if (convertView == null) {
            view = inflater.inflate(R.layout.element_users_cell, parent, false)
        } else {
            view = convertView
        }

        val user: SongsUser = users[position]

        updateCellView(view, user)

        return view
    }

    private fun updateCellView(view: View, user: SongsUser) {
        val email: TextView = view.findViewById(R.id.element_users_cell_text_email)
        val id: TextView = view.findViewById(R.id.element_users_cell_text_id)
        val role: TextView = view.findViewById(R.id.element_users_cell_text_role)
        val status: TextView = view.findViewById(R.id.element_users_cell_text_status)

        email.setText(user.email)
        id.setText(user.id)
        if (user.isAdmin) {
            role.setText(R.string.admin_role)
        } else {
            role.setText(R.string.user_role)
        }
        if (user.isBlocked) {
            status.setText(R.string.blocked)
        } else {
            status.setText(R.string.active)
        }

    }
}