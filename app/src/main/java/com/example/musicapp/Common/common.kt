package com.example.musicapp.Common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast

object common {
    fun Context.toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
//    fun Context.intent(activity:Activity){
//        var intent = Intent(this, activity::class.java)
//        startActivity(intent)
//    }
}