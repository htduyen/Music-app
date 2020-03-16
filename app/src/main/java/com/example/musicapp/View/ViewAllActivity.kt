package com.example.musicapp.View

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.*
import android.graphics.Color.parseColor
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.Adapter.RecyclerViewAdapter
import com.example.musicapp.Model.Song
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import android.graphics.Bitmap
import android.graphics.drawable.PictureDrawable
import android.text.Layout
import android.view.*
import androidx.appcompat.app.AlertDialog
import com.example.musicapp.Common.SharedPreference
import com.example.musicapp.Common.common.toast
import com.example.musicapp.R
import com.example.musicapp.View.ViewAllActivity.Companion.listSongs
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_view_all.*
import kotlinx.android.synthetic.main.dialog_create_playlist.*
import kotlinx.android.synthetic.main.dialog_create_playlist.view.*


class ViewAllActivity : AppCompatActivity() {



    var db:FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var recyclerviewSong: RecyclerView
    var user = FirebaseAuth.getInstance()

    companion object{
        var listSongs:ArrayList<Song> = arrayListOf()
        var isCreated: Boolean = false
    }
    private lateinit var adapter: RecyclerViewAdapter
    private val p = Paint()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.musicapp.R.layout.activity_view_all)
        setSupportActionBar(toobar_viewAll)

        supportActionBar?.setTitle("View all list")
        recyclerviewSong = findViewById(com.example.musicapp.R.id.rv_listSong)
        if(SharedPreference(this).getValueString("MY_LIST")!!.isNotEmpty()){
            isCreated =  true
        }
        listSongs.clear()
        getAllSong()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var infalter : MenuInflater = menuInflater
        infalter.inflate(R.menu.search_create_playlist, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.mnu_search -> {
                toast("Search")
            }
            R.id.mnu_playlist -> {
                if(isCreated){
                    var intent= Intent(this, PlaylistActivity::class.java)
                    startActivity(intent)
                }else{
                    toast("You don't have playlist!")
                }
            }
            R.id.mnu_create ->{
                if(isCreated){
                    toast("You aldreadly have a playlist")
                }else {
                    displayDialog()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_create_playlist, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Create Playlist")
        //show dialog
        val  mAlertDialog = mBuilder.show()
        //login button click of custom layout
        mDialogView.btn_create.setOnClickListener {
            mAlertDialog.dismiss()
            isCreated = true
            val name = mDialogView.edtNamePlaylistCreate.text.toString()
            SharedPreference(this).saveString("MY_LIST", name)

            val dataPlaylist = hashMapOf(
                "name_playlist" to name,
                "is_song" to arrayListOf<String>()
            )
            val docRef = db.collection("USER").document(user.currentUser!!.uid)
                docRef.collection("USER_PLAYLIST").document("list")
                .set(dataPlaylist)
                .addOnSuccessListener { toast("Playlist added successfully!") }
                .addOnFailureListener {toast("Error writing document") }
            toast("Added")
        }
        //cancel button click of custom layout
        mDialogView.btn_cancel.setOnClickListener {
            mAlertDialog.dismiss()
        }


    }

    private fun getAllSong() {

        val docRef = db.collection("SONGS").orderBy("index")
            docRef.get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot>{task ->
                    if (task.isSuccessful()) {
                        for (documentSnapshot in task.getResult()!!) {
                            listSongs.add(
                                Song(
                                    documentSnapshot.get("id_song") as String,
                                    documentSnapshot.get("song_img") as String,
                                    documentSnapshot.get("name") as String,
                                    documentSnapshot.get("url_song") as String
                                )
                            )
                        }
                        //Luu vao adapter
                        adapter = RecyclerViewAdapter(listSongs)
                        recyclerviewSong.layoutManager = LinearLayoutManager(this)
                        recyclerviewSong.adapter = adapter
                        adapter.notifyDataSetChanged()
                    } else {
                        val message = task.getException()?.message
                        Toast.makeText(applicationContext, "Error: $message", Toast.LENGTH_SHORT).show()
                    }
                })
                .addOnFailureListener { exception ->
                    Log.w("AAA", "Error getting documents: ", exception)
                }
    }
}
