package com.example.musicapp.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.musicapp.Common.common
import com.example.musicapp.Common.common.toast
import com.example.musicapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    lateinit var Username:String
    lateinit var Email:String
    lateinit var Password:String
    var auth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        Username = edtUserName.text.toString()


        btnRegister.setOnClickListener{
            Email = edtEmailRegister.text.toString()
            Password = edtPasswordRegister.text.toString()
            registerUser(Email, Password)


        }
        txtLoginNow.setOnClickListener {
            var intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                var data = hashMapOf(
                    "id_songs" to arrayListOf<String>(),
                    "name_playlist" to "",
                    "image_playlist" to "https://firebasestorage.googleapis.com/v0/b/musicapp-780fe.appspot.com/o/Image_song%2Fimages.jpg?alt=media&token=2dfdaa19-b88b-46ff-b011-b364aa27b7c6"
                )
                db.collection("USER").document(auth.currentUser!!.uid).collection("USER_PLAYLIST").document("list").set(data)
                    .addOnCompleteListener{toast("add empty playlist success")}
                Log.d("AAA", "1")
            }
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    toast("Register successfull!")
                    var intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    toast("CreateUserWithEmail:failure ${task.exception}")
                }
                Log.d("AAA", "2")
            }
    }
}
