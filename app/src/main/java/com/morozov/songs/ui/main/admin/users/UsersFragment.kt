package com.morozov.songs.ui.main.admin.users

import android.os.Bundle
import android.view.*
import android.widget.ListView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.morozov.songs.R
import com.morozov.songs.services.SessionService


class UsersFragment : Fragment() {
    private lateinit var list: ListView

    override fun onResume() {
        super.onResume()
        SessionService.selectedUser = null
        syncItems()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)
        initViewObjects(view)
        setupViewObjects()
        return view
    }

    private fun initViewObjects(view: View) {
        list = view.findViewById(R.id.fragment_users_list)
    }

    private fun setupViewObjects() {
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }
    }

    private fun syncItems() {
        val adapter = UsersAdapter(requireContext(), SessionService.getSongsUsers()?: ArrayList())
        list.adapter = adapter
        list.setOnItemClickListener { parent, view, position, id ->
            SessionService.selectedUser = SessionService.getSongsUsers()?.get(position)
            findNavController().navigate(R.id.action_usersFragment_to_usersDetailsActivity)
        }
    }
}