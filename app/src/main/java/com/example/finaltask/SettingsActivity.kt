package com.example.finaltask

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btnLogout: Button
    private lateinit var txtWelcomeUser: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser == null) {
            finish()
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        btnLogout = findViewById(R.id.btn_logout)
        btnLogout.setOnClickListener(this)
        txtWelcomeUser = findViewById(R.id.txt_welcome_userName)
        val user = firebaseAuth.currentUser
        txtWelcomeUser.text = "Привет ${user?.email}"
    }

    override fun onClick(view: View?) {
        if (view == btnLogout) {
            firebaseAuth.signOut()
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}