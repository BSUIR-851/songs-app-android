package com.morozov.songs.ui.main.songs.details

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso

import com.morozov.songs.R
import com.morozov.songs.services.SessionService
import com.morozov.songs.services.extensions.format


class SongsDetailsFragment : Fragment() {

    private lateinit var photoView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var videoView: VideoView
    private lateinit var videoStackView: LinearLayout
    private lateinit var releaseLatitudeTextView: TextView
    private lateinit var releaseLongitudeTextView: TextView
    private lateinit var releaseNoteTextView: TextView
    private lateinit var releaseStackView: LinearLayout

    override fun onResume() {
        super.onResume()
        syncWithSelected()

        if (SessionService.selectedAsset == null) {
            requireActivity().onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_songs_details, container, false)
        initViewObjects(view)
        setupViewObjects()
        return view
    }

    private fun initViewObjects(view: View) {
        photoView = view.findViewById(R.id.fragment_songs_details_photo)
        nameTextView = view.findViewById(R.id.fragment_songs_details_text_name)
        authorTextView = view.findViewById(R.id.fragment_songs_details_text_author)
        descriptionTextView = view.findViewById(R.id.fragment_songs_details_text_description)
        yearTextView = view.findViewById(R.id.fragment_songs_details_text_year)
        videoView = view.findViewById(R.id.fragment_songs_details_video)
        videoStackView = view.findViewById(R.id.fragment_songs_details_video_stack)
        releaseLatitudeTextView = view.findViewById(R.id.fragment_songs_details_text_release_latitude)
        releaseLongitudeTextView = view.findViewById(R.id.fragment_songs_details_text_release_longitude)
        releaseNoteTextView = view.findViewById(R.id.fragment_songs_details_text_release_note)
        releaseStackView = view.findViewById(R.id.fragment_songs_details_release_stack)
    }

    private fun setupViewObjects() {
        videoView.setMediaController(MediaController(requireContext()))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_songs_details_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true
            }
            R.id.menu_songs_details_edit -> {
                findNavController().navigate(R.id.action_songsDetailsFragment_to_formActivity2)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun syncWithSelected() {
        val asset = SessionService.selectedAsset
        if (asset != null) {
            if (asset.photoFileData != null) {
                Picasso.get()
                    .load(asset.photoFileData!!.downloadURL)
                    .placeholder(R.drawable.ic_baseline_downloading_24dp)
                    .error(R.drawable.ic_baseline_grade_24dp)
                    .into(photoView)
                photoView.visibility = View.VISIBLE
            } else {
                photoView.visibility = View.GONE
            }

            nameTextView.text = asset.name
            authorTextView.text = asset.author
            descriptionTextView.text = asset.description
            yearTextView.text = asset.year.toString()

            if (asset.videoFileData != null) {
                videoView.setVideoPath(asset.videoFileData!!.downloadURL)
                videoView.seekTo(100)
                videoStackView.visibility = View.VISIBLE
            } else {
                videoStackView.visibility = View.GONE
            }

            if (asset.release != null) {
                releaseLatitudeTextView.text = asset.release!!.latitude.format(4)
                releaseLongitudeTextView.text = asset.release!!.longitude.format(4)
                releaseNoteTextView.text = asset.release!!.note
                releaseStackView.visibility = View.VISIBLE
            } else {
                releaseStackView.visibility = View.GONE
            }
        }
    }
}