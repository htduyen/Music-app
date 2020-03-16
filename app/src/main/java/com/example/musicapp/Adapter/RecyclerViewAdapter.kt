package com.example.musicapp.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.Model.Song
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.View.PlaylistActivity
import com.example.musicapp.View.SongDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RecyclerViewAdapter()  : RecyclerView.Adapter<RecyclerViewAdapter.SongViewHolder>() {


    lateinit var listSongs: ArrayList<Song>

    constructor(listSong: ArrayList<Song>) : this(){
        this.listSongs = listSong
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listSongs.size
    }


    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bindData(listSongs[position], position)
    }

    class SongViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

        val image = itemView.findViewById<ImageView>(R.id.imv_song)
        val name = itemView.findViewById<TextView>(R.id.txt_songName)

        fun bindData(song: Song, position: Int){
            Glide.with(itemView.context).load(song.image)
                .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background)).into(image)
            Log.d("URL", song.image)
            name.text = song.name

            itemView.setOnClickListener {
                var intent = Intent(itemView.context, SongDetailActivity::class.java)
                intent.putExtra("SONG", song)
                //intent.putExtra("POSITION", position)
                itemView.context.startActivity(intent)
            }
            itemView.setOnLongClickListener {

                PlaylistActivity.myPlayListSongs.add(song)
//                var db = FirebaseFirestore.getInstance()
//                var user = FirebaseAuth.getInstance().currentUser
//                val docRef = db.collection("USER").document(user!!.uid)
//                docRef.collection("USER_PLAYLIST").document("list").update("id_song", song.)
                Toast.makeText(itemView.getContext(), "Added to my playlist", Toast.LENGTH_SHORT).show()
                return@setOnLongClickListener true
            }
        }

    }

    fun removeItem(position: Int) {
        listSongs.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listSongs.size)
    }

    fun restoreItem(song: Song, position: Int) {
        listSongs.add(position, song)
        // notify item added by position
        notifyItemInserted(position)
    }
}