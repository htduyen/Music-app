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
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    toast("createUserWithEmail:success")
                    var intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    toast("CreateUserWithEmail:failure ${task.exception}")
                }
            }
    }
}
