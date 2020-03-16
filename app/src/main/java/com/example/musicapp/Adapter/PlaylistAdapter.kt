package com.example.musicapp.Adapter
import android.app.Activity
import com.example.musicapp.Model.Playlist
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicapp.R


class PlaylistAdapter(private val getcontext:Context,private val layout: Int,private val list_playlist: ArrayList<Playlist>):
    ArrayAdapter<Playlist>(getcontext, layout, list_playlist)
{

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        val Holder: ViewHolder
        if(row == null) {
            val inflater = (getcontext as Activity).layoutInflater
            row = inflater!!.inflate(layout, parent, false)

            Holder = ViewHolder()

            Holder.img = row!!.findViewById(R.id.imv_playlist) as ImageView
            Holder.txtName = row!!.findViewById(R.id.txt_nameplaylist) as TextView

            row.setTag(Holder)
        }else{
            Holder = row.getTag() as ViewHolder
        }

        val item = list_playlist[position]
        //Holder.img!!.setImageResource(item.url_image)
        Glide.with(context).load(item.url_image)
            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background)).into(
                Holder.img!!)

        Holder.txtName!!.text = item.name
        return row
    }

    class ViewHolder {
        var img: ImageView? = null
        var txtName: TextView? = null

    }

}