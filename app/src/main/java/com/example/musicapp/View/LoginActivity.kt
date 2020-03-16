package com.example.musicapp.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

import android.content.Intent
import android.view.View
import android.widget.Toast
import com.example.musicapp.Common.common.toast
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    val mAuth :FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.musicapp.R.layout.activity_login)

        var email :String
        var password :String

        btnLogin.setOnClickListener {
           email = edtEmailLogin.text.toString()
           password = edtPasswordLogin.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                checkLogin(email, password)
            }else{
                toast("Email & Password not empty!")
            }
        }
        txtRegister.setOnClickListener{
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkLogin( email:String,  password:String) {

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(it.isSuccessful) {
                    Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show()

                    var intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    toast("Wrong email or Password!")
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Login Fail, Check again email and Passowrd!", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.getCurrentUser()
        if (currentUser == null) {
        } else {
            var intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
