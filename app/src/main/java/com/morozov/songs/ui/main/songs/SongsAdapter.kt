package com.morozov.songs.ui.main.songs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.morozov.songs.R
import com.morozov.songs.models.songs.SongsAsset
import de.hdodenhof.circleimageview.CircleImageView

class SongsAdapter (private val context: Context, private val assets: ArrayList<SongsAsset>): BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return assets.count()
    }

    override fun getItem(position: Int): Any {
        return assets[position]
    }

    override fun getItemId(position: Int): Long {
        return assets[position].hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if (convertView == null) {
            view = inflater.inflate(R.layout.element_songs_cell, parent, false)
        } else {
            view = convertView
        }

        val asset: SongsAsset = assets[position]

        updateCellView(view, asset)

        return view
    }

    private fun updateCellView(view: View, p: SongsAsset) {
        val photo: CircleImageView = view.findViewById(R.id.element_songs_cell_photo)
        val name: TextView = view.findViewById(R.id.element_songs_cell_text_name)
        val author: TextView = view.findViewById(R.id.element_songs_cell_text_author)
        val year: TextView = view.findViewById(R.id.element_songs_cell_text_year)

        if (p.photoFileData != null) {
            Picasso.get()
                .load(p.photoFileData!!.downloadURL)
                .placeholder(R.drawable.ic_songs_cell)
                .error(R.drawable.ic_songs_cell)
                .into(photo)
        }

        name.text = p.name
        author.text = p.author
        year.text = p.year.toString()
    }
}