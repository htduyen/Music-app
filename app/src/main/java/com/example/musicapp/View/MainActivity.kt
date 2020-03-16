package com.example.musicapp.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.GridView
import com.example.musicapp.Adapter.PlaylistAdapter
import com.example.musicapp.Common.common.toast
import com.example.musicapp.Model.Playlist
import com.example.musicapp.Model.Song
import com.example.musicapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mAuthor: FirebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    var list_playlist: ArrayList<Playlist>  = arrayListOf()
    lateinit var customAdapter: PlaylistAdapter
    lateinit var gridViewPlaylist: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Main")

        gridViewPlaylist = findViewById(R.id.gv_playlist)

        getAllPlaylist()

    }

    private fun getAllPlaylist() {
        val docRef = db.collection("PLAYLIST").orderBy("index")
        docRef.get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot>{ task ->
                if (task.isSuccessful()) {
                    for (documentSnapshot in task.getResult()!!) {
                        //list_playlist = arrayListOf()
                        list_playlist.add(
                            Playlist(
                                documentSnapshot.get("name_playlist").toString(),
                                documentSnapshot.get("url_image_playlist").toString(),
                                documentSnapshot.get("list_song") as ArrayList<Song>
                            )
                        )
                    }
                    //Luu vao adapter
                    customAdapter = PlaylistAdapter(this,R.layout.item_playlist, list_playlist)

                   gridViewPlaylist.adapter = customAdapter

                   gridViewPlaylist.setOnItemClickListener { parent, view, position, id ->
                       //toast("${list_playlist?.get(position)?.list_song_ofPlaylist}")
                       var intent  = Intent(this@MainActivity, PlaylistActivity::class.java)
                       var arg:ArrayList<Song> = list_playlist?.get(position)?.list_song

                       intent.putExtra("LIST_SONG", arg)
                       intent.putExtra("URL_IMAGE", list_playlist[position].url_image)
                       intent.putExtra("NAME_PLAYLIST", list_playlist[position].name)
                       startActivity(intent)
                   }
                   }

            })
            .addOnFailureListener { exception ->
                Log.w("AAA", "Error getting documents: ", exception)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var infalter :MenuInflater = menuInflater
        infalter.inflate(R.menu.option_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.mnu_allsong -> {
                var intent= Intent(this, ViewAllActivity::class.java)
                startActivity(intent)
            }
            R.id.mnu_playlist -> {
                    var intent= Intent(this, PlaylistActivity::class.java)
                    intent.putExtra("IS_MY_LIST", true)
                    startActivity(intent)
            }
            R.id.mnu_logout ->{
                mAuthor.signOut()
                var intent= Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
