package com.example.musicapp.Model

import android.os.Parcelable
import java.io.Serializable

data class Playlist(
     var name: String,
     var url_image: String,
    var list_song: ArrayList<Song>
):Serializable
