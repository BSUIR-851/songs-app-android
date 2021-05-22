package com.morozov.songs.ui.main.songs

import android.os.Bundle
import android.view.*
import android.widget.ListView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.morozov.songs.R
import com.morozov.songs.services.SessionService


class SongsFragment : Fragment() {

    private lateinit var list: ListView

    override fun onResume() {
        super.onResume()
        SessionService.selectedAsset = null
        syncItems()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_songs, container, false)
        initViewObjects(view)
        setupViewObjects()
        return view
    }

    private fun initViewObjects(view: View) {
        list = view.findViewById(R.id.fragment_songs_list)
    }

    private fun setupViewObjects() {
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main_songs_action_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add_songs) {
            findNavController().navigate(R.id.action_songsFragment_to_formActivity)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun syncItems() {
        val adapter = SongsAdapter(requireContext(), SessionService.getLocalAssets() ?: ArrayList())
        list.adapter = adapter
        list.setOnItemClickListener { parent, view, position, id ->
            SessionService.selectedAsset = SessionService.getLocalAssets()?.get(position)
            findNavController().navigate(R.id.action_songsFragment_to_songsDetailsActivity)
        }
    }

}