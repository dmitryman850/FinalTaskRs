package com.example.finaltask.fragments

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.example.finaltask.R

class CurrentTrackFragment : Fragment() {

    private lateinit var nameTrackView: TextView
    private lateinit var groupNameView: TextView
    private lateinit var albumNameView: TextView
    private lateinit var durationTrackView: TextView
    private lateinit var albumImageView: ImageView
    private var mediaPlayer: MediaPlayer? = null
    private var pathTrack: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_current_track, null)

        var flag = true
        val btnPlayCurrent = view.findViewById<ImageView>(R.id.btn_play_current)
        btnPlayCurrent.setOnClickListener(View.OnClickListener {
            if (flag) {
                btnPlayCurrent.setImageResource(R.drawable.ic_pause_current)
                mediaPlayer = MediaPlayer()
                mediaPlayer!!.setDataSource(pathTrack)
                mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()
            } else {
                btnPlayCurrent.setImageResource(R.drawable.ic_play_current)
                mediaPlayer!!.pause()
            }
            flag = !flag
        })

        nameTrackView = view.findViewById(R.id.text_name_track_current)
        groupNameView = view.findViewById(R.id.text_name_group_current)
        albumNameView = view.findViewById(R.id.text_name_album_current)
        durationTrackView = view.findViewById(R.id.text_duration_end_current)
        albumImageView = view.findViewById(R.id.image_current_track)



        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("requestKey") { key, bundle ->
            nameTrackView.text = bundle.getString("trackNameKey")
            groupNameView.text = bundle.getString("groupNameKey")
            durationTrackView.text = bundle.getString("durationTrackKey")
            albumNameView.text = bundle.getString("albumNameKey")
            pathTrack = bundle.getString("pathKey")
            val album = bundle.getByteArray("albumImageKey")
            if (album != null) {
                context?.let {
                    Glide.with(it).asBitmap()
                        .load(album)
                        .into(albumImageView)
                }
            } else {
                context?.let {
                    Glide.with(it)
                        .load(R.drawable.default_album_image)
                        .into(albumImageView)
                }
            }
        }
    }
}


