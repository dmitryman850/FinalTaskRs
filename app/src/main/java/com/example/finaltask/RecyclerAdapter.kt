package com.example.finaltask

import android.content.Context
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.*
import java.util.concurrent.TimeUnit

class RecyclerMusicAdapter(
    private var mContext: Context?,
    private var mFiles: ArrayList<MusicFiles>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_random, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mFiles.size
    }

    override fun onBindViewHolder(myHolder: MyViewHolder, position: Int) {
        val trackNameBind = mFiles[position].getTitle()
        val groupNameBind = mFiles[position].getArtist()
        val albumNameBind = mFiles[position].getAlbumName()
        val pathBind = mFiles[position].getPath()
        val milliseconds = mFiles[position].getDuration().toLong()
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60
        val minutesString = when {
            minutes.toString().isEmpty() -> {
                "0"
            }
            else -> minutes.toString()
        }
        val secondsString = when {
            seconds.toString().length == 1 -> {
                "0$seconds"
            }
            seconds.toString().isEmpty() -> {
                "00"
            }
            else -> seconds.toString()
        }

        val durationTrackBind = "$minutesString:$secondsString"
        val imageAlbumBind: ByteArray? = getAlbumArt(mFiles[position].getPath())

        myHolder.bind(
            trackNameBind,
            groupNameBind,
            albumNameBind,
            durationTrackBind,
            imageAlbumBind,
            mContext,
            itemClickListener,
            pathBind
        )
    }

    private fun getAlbumArt(uri: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(uri)
        val art: ByteArray? = retriever.embeddedPicture
        retriever.release()
        return art
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName = itemView.findViewById<TextView>(R.id.text_name_track_random)
    private val groupName = itemView.findViewById<TextView>(R.id.text_name_group_random)
    private val durationTrack = itemView.findViewById<TextView>(R.id.text_duration_random)
    private var albumImage = itemView.findViewById<ImageView>(R.id.image_random)
    private val albumName = itemView.findViewById<TextView>(R.id.text_name_album_random)

    fun bind(
        trackNameBind: String,
        groupNameBind: String,
        albumNameBind: String,
        durationTrackBind: String,
        imageAlbumBind: ByteArray?,
        mContext: Context?,
        clickListener: OnItemClickListener,
        pathBind: String
    ) {
        trackName.text = trackNameBind
        groupName.text = groupNameBind
        albumName.text = albumNameBind
        durationTrack.text = durationTrackBind
        if (imageAlbumBind != null) {
            mContext?.let {
                Glide.with(it).asBitmap()
                    .load(imageAlbumBind)
                    .into(albumImage)
            }
        } else {
            mContext?.let {
                Glide.with(it)
                    .load(R.drawable.default_album_image)
                    .into(albumImage)
            }
        }


        itemView.setOnClickListener {
            clickListener.onItemClicked(
                trackNameBind, groupNameBind, albumNameBind,
                durationTrackBind, imageAlbumBind, pathBind
            )
        }
    }
}


interface OnItemClickListener {
    fun onItemClicked(
        trackNameBind: String, groupNameBind: String, albumNameBind: String,
        durationTrackBind: String, imageAlbumBind: ByteArray?, pathBind: String
    )
}