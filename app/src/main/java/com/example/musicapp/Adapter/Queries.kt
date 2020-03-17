package com.example.musicapp.Adapter

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

object Queries {

    var db = FirebaseFirestore.getInstance()
    var currentUId = FirebaseAuth.getInstance().currentUser!!.uid
    var HavePlaylist: Boolean = false

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
}