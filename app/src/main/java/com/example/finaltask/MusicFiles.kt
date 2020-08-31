package com.example.finaltask

class MusicFiles(
    private var path: String,
    private var title: String,
    private var artist: String,
    private var albumArt: String,
    private var duration: String,
    private var albumName: String
) {
    fun getPath(): String {
            return path
        }
    fun  setPath(path: String) {
            this.path = path
        }
    fun getTitle():String {
            return title
        }
    fun setTitle(title: String) {
            this.title = title
        }
    fun getArtist():String {
            return artist
        }
    fun setArtist(artist: String) {
            this.artist = artist
        }
    fun getAlbumArt() :String {
            return  albumArt
        }
    fun setAlbumArt(albumArt: String) {
            this.albumArt
        }
    fun getDuration():String {
            return duration
        }
    fun setDuration(duration: String) {
            this.duration = duration
        }
    fun getAlbumName(): String {
        return albumName
    }
    fun setAlbumName(albumName: String){
        this.albumName
    }
}