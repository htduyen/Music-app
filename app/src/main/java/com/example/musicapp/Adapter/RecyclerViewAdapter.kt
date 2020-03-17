package com.example.musicapp.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.Model.Song
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import com.example.musicapp.View.PlaylistActivity
import com.example.musicapp.View.SongDetailActivity
import com.example.musicapp.View.ViewAllActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList


class RecyclerViewAdapter()  : RecyclerView.Adapter<RecyclerViewAdapter.SongViewHolder>(), Filterable{


    lateinit var listSongs: ArrayList<Song>
     var songFilterList: ArrayList<Song> = arrayListOf()


    constructor(listSong: ArrayList<Song>) : this(){
        this.listSongs = listSong
        songFilterList = ArrayList(listSongs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(com.example.musicapp.R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songFilterList.size
    }


    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bindData(songFilterList[position], position)
    }

    class SongViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

        val image = itemView.findViewById<ImageView>(com.example.musicapp.R.id.imv_song)
        val name = itemView.findViewById<TextView>(com.example.musicapp.R.id.txt_songName)

        fun bindData(song: Song, position: Int){
            Glide.with(itemView.context).load(song.image)
                .apply(RequestOptions().placeholder(com.example.musicapp.R.drawable.ic_launcher_background)).into(image)
            name.text = song.name

            itemView.setOnClickListener {
                var intent = Intent(itemView.context, SongDetailActivity::class.java)
                intent.putExtra("SONG", song)
                intent.putExtra("POSITION", position)

                itemView.context.startActivity(intent)
            }
            itemView.setOnLongClickListener {
                if(ViewAllActivity.isCreated) {
                    var db = FirebaseFirestore.getInstance()
                    var user = FirebaseAuth.getInstance().currentUser?.uid

                    val docRef = db.collection("USER").document(user.toString())
                    docRef.collection("USER_PLAYLIST").document("list")
                        .update("id_songs", FieldValue.arrayUnion(song.id))

                    Toast.makeText(itemView.getContext(), "Add my playlist", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(itemView.context, "You don't create playlist", Toast.LENGTH_SHORT).show()
                }
                return@setOnLongClickListener true
            }
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var charSearch = constraint.toString()
            if (charSearch.isEmpty()) {
                songFilterList = listSongs
            } else {
                val resultList = ArrayList<Song>()
                for (song:Song in listSongs) {
                    if (song.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                        resultList.add(song)
                    }
                }
                songFilterList = resultList
            }
            val filterResults = FilterResults()
            filterResults.values = songFilterList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            songFilterList = results?.values as ArrayList<Song>
            notifyDataSetChanged()
        }

    }
    }


}