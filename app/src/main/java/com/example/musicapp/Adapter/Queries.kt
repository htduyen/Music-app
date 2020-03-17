package com.example.musicapp.Adapter

import android.util.Log
import com.example.musicapp.Model.Song
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

object Queries {

    var db = FirebaseFirestore.getInstance()
    var currentUId = FirebaseAuth.getInstance().currentUser!!.uid
    var HavePlaylist: Boolean = false

    var array_songMyList:ArrayList<Song> = getSongInMyPlayList()

    fun IsHavePlaylist():Boolean{
        var name:String
        val docRef = db.collection("USER").document(currentUId)
        docRef.collection("USER_PLAYLIST").document("list").get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    name = document.data?.get("name_playlist").toString()
                    HavePlaylist = if(name == "") false else true
                }
            }
        return HavePlaylist
    }

    fun getSongInMyPlayList():ArrayList<Song> {
        var list = arrayListOf<Song>()
        val docRef = db.collection("USER").document(currentUId)
        docRef.collection("USER_PLAYLIST").document("list").get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    var array_id:ArrayList<String> = document.data?.get("id_songs") as ArrayList<String>

                    array_id.forEach {id ->
                        db.collection("SONGS").document(id).get()
                            .addOnSuccessListener { document2 ->
                                if(document2 != null){
                                   // Log.d("AAA", "DocumentSnapshot data: ${document2.data}")
                                    list.add(
                                        Song(
                                            document2.get("id_song") as String,
                                            document2.get("song_img") as String,
                                            document2.get("name") as String,
                                            document2.get("url_song") as String
                                        )
                                    )
                                }
                                array_songMyList = list
                            }
                            .addOnFailureListener { exception ->
                                Log.d("AAA", "get failed with ", exception)
                            }
                    }
                }
            }
        return array_songMyList
    }
}