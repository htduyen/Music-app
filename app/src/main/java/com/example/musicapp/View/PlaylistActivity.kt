package com.example.musicapp.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicapp.Adapter.RecyclerViewAdapter
import com.example.musicapp.Model.Song
import com.example.musicapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_playlist.*
import kotlinx.android.synthetic.main.playlist_top.*

class PlaylistActivity : AppCompatActivity() {

    companion object {
        var myPlayListSongs:ArrayList<Song> = arrayListOf()
        var isPlayAll: Boolean = false
        var list_song_ofPlaylist = arrayListOf<Song>()
    }
    lateinit var recyclerviewPlaylist: RecyclerView
    lateinit var adapter:RecyclerViewAdapter

    var db:FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var btn_playAll: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        setSupportActionBar(toolbar_playlist)

        btn_playAll = findViewById(R.id.btn_playall)
        recyclerviewPlaylist = findViewById(R.id.rv_plylistsong)

        var title = intent.getStringExtra("NAME_PLAYLIST")

        if(title != null){
            supportActionBar?.setTitle(title)
        }else {
            supportActionBar?.setTitle("My playlist")
        }

        Glide.with(applicationContext).load(intent.getStringExtra("URL_IMAGE"))
            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background)).into(imv_topplaylist)
        var isMyList = intent.getBooleanExtra("IS_MY_LIST", false)  //MainActivity

        var list_songID:ArrayList<String>

        if(!isMyList) {
            list_songID = intent.getSerializableExtra("LIST_SONG") as ArrayList<String>  // MainActivity
            getSongById(list_songID)
        }else {
            adapter = RecyclerViewAdapter(myPlayListSongs)
            recyclerviewPlaylist.layoutManager = LinearLayoutManager(this)
            recyclerviewPlaylist.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        btn_playAll.setOnClickListener{
            isPlayAll = true
            var intent = Intent(applicationContext, SongDetailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getSongById(list_songID: ArrayList<String>) {
        list_song_ofPlaylist.clear()
        for(id in list_songID){
            val docRef = db.collection("SONGS").document(id)
            docRef.get()
                .addOnSuccessListener { document ->

                    if (document != null) {
                        list_song_ofPlaylist.add(Song(document.data?.get("id_song").toString(),document.data?.get("song_img").toString(), document.data?.get("name").toString(), document.data?.get("url_song").toString()))
                    } else {
                        Log.d("AAA", "No such document")
                    }
                    adapter = RecyclerViewAdapter(list_song_ofPlaylist)
                    recyclerviewPlaylist.layoutManager = LinearLayoutManager(this)
                    recyclerviewPlaylist.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.d("AAA", "get failed with ", exception)
                }
        }
    }
}
