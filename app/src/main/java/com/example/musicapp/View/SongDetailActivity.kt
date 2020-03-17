package com.example.musicapp.View

import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.musicapp.Model.Song
import com.example.musicapp.R
import android.media.MediaPlayer
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_song_detail.*
import java.util.concurrent.TimeUnit



class SongDetailActivity : AppCompatActivity() {

    lateinit var btn_previous: ImageButton
    lateinit var btn_play: ImageButton
    lateinit var btn_next:ImageButton
    lateinit var btn_pause:ImageButton
    lateinit var startTime: TextView
    lateinit var songTime: TextView
    lateinit var songPrgs: SeekBar
    private var oTime = 0
    var sTime = 0
    var eTime = 0
    var fTime = 5000
    var bTime = 5000
    private val hdlr = Handler()
    var mPlayer = MediaPlayer()
    lateinit var txtName:TextView
    lateinit var wheelRotation: RotateAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_detail)
        setSupportActionBar(toolbar_songdetail)
        supportActionBar?.setTitle("Detail")

        var list:ArrayList<Song> = PlaylistActivity.list_song_ofPlaylist
        //var position = intent.getIntExtra("POSITION", 0)

        txtName = findViewById(R.id.txt_songName)
        btn_previous = findViewById(R.id.btn_previous)
        btn_play = findViewById(R.id.btn_play)
        btn_next = findViewById(R.id.btn_next)
        btn_pause = findViewById(R.id.btn_pause)
        startTime = findViewById(R.id.startTime)
        songTime = findViewById(R.id.songTime)

        songPrgs = findViewById(R.id.songPrgs)
        songPrgs.setClickable(false)
        btn_pause.setEnabled(false)


        wheelRotation = RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        wheelRotation.setInterpolator(LinearInterpolator())
        wheelRotation.setRepeatCount(Integer.MAX_VALUE)
        wheelRotation.setDuration(6000)
        wheelRotation.setInterpolator(this, android.R.interpolator.accelerate_decelerate)

        var position = 0
        var song:Song

        if(!PlaylistActivity.isPlayAll) {
            song = intent.getSerializableExtra("SONG") as Song
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mPlayer.setDataSource(song.url_song)
            mPlayer.prepare()

            txtName.text = song.name
            Glide.with(applicationContext).load(song.image)
                .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background))
                .into(imv_Song)

            btn_next.isEnabled = false
            btn_previous.isEnabled = false
        }else {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mPlayer.setDataSource(list[position].url_song)
            mPlayer.prepare()

            txtName.text = list[position].name
            Glide.with(applicationContext).load(list[position].image)
                .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background))
                .into(imv_Song)
            if (position < list.size) {
                btn_next.setOnClickListener {
                    mPlayer.stop()
                    mPlayer.reset()
                    btn_next.isEnabled = true
                    position = position + 1
                    if (position == list.size - 1) {
                        btn_next.isEnabled = false
                    }
                    txtName.text = list.get(position).name
                    val url = list.get(position).url_song
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                    mPlayer.setDataSource(url)
                    mPlayer.prepare()
                    Glide.with(applicationContext).load(list[position].image)
                        .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background))
                        .into(imv_Song)

                    PlayMusic()

                }
            }

            if (position > 0) {
                btn_previous.setOnClickListener {
                    mPlayer.stop()
                    mPlayer.reset()
                    position = position - 1

                    txtName.text = list.get(position).name
                    val url = list.get(position).url_song
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                    mPlayer.setDataSource(url)
                    mPlayer.prepare()
                    Glide.with(applicationContext).load(list[position].image)
                        .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background))
                        .into(imv_Song)
                    if (position == 0) {
                        btn_previous.isEnabled = false
                    } else {
                        btn_previous.isEnabled = true
                    }
                    PlayMusic()
                }
            }
        }
        btn_play.setOnClickListener {
            //val url = song.url_song
            PlayMusic()
        }

        btn_pause.setOnClickListener {
            mPlayer.pause()
            btn_pause.setEnabled(false)
            btn_play.setEnabled(true)

        }


    }

    private fun PlayMusic() {
        try {
            mPlayer.start()
        }catch (ex:Exception){
            Log.d("AAA","Ex: "+ex.message.toString())
        }

        imv_cd.startAnimation(wheelRotation)
        eTime = mPlayer.getDuration()
        sTime = mPlayer.getCurrentPosition()
        if (oTime == 0) {
            songPrgs.setMax(eTime)
            oTime = 1
        }
        songTime.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(eTime.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(eTime.toLong()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(
                eTime.toLong()
            ))) )
        startTime.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(sTime.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(sTime.toLong()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(
                sTime.toLong()
            ))) )
        songPrgs.setProgress(sTime)
        hdlr.postDelayed(UpdateSongTime, 100)
        btn_pause.isEnabled = true
        btn_play.isEnabled = false
    }

    private val UpdateSongTime = object : Runnable {
        override fun run() {
            sTime = mPlayer.getCurrentPosition()
            startTime.text = String.format(
                "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(sTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(sTime.toLong()) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(sTime.toLong())
                )
            )
            songPrgs.progress = sTime
            hdlr.postDelayed(this, 100)
        }
    }
}
