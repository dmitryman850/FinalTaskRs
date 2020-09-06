package com.example.finaltask.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finaltask.MainActivity.Companion.musicFiles
import com.example.finaltask.OnItemClickListener
import com.example.finaltask.R
import com.example.finaltask.RecyclerMusicAdapter

class RandomFragment(private val itemClickListener: OnCurrentFragmentClickListener) : Fragment(), OnItemClickListener {


    private lateinit var recyclerView: RecyclerView
    private lateinit var musicAdapter: RecyclerMusicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_random, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_random)
        recyclerView.setHasFixedSize(true)
        if (!(musicFiles.size < 1)) {
            musicAdapter = RecyclerMusicAdapter(context, musicFiles, this)
            recyclerView.adapter = musicAdapter
            recyclerView.layoutManager = LinearLayoutManager(
                context, RecyclerView.VERTICAL,
                false
            )
        }
        return view
    }

    override fun onItemClicked(
        trackNameBind: String, groupNameBind: String, albumNameBind: String,
        durationTrackBind: String, imageAlbumBind: ByteArray?, pathBind: String
    ) {

        itemClickListener.onItemClick()
        setFragmentResult(
            "requestKey", bundleOf(
                "trackNameKey" to trackNameBind,
                "groupNameKey" to groupNameBind,
                "albumNameKey" to albumNameBind,
                "durationTrackKey" to durationTrackBind,
                "albumImageKey" to imageAlbumBind,
                "pathKey" to pathBind
            )
        )
    }

    interface OnCurrentFragmentClickListener{
        fun onItemClick()
    }
}

