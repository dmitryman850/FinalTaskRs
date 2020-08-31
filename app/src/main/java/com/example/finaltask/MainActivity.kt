package com.example.finaltask

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val REQUEST_CODE = 1
    companion object {
        lateinit var musicFiles: ArrayList<MusicFiles>
    }

    private lateinit var btnRegistration: Button
    private lateinit var btnLogin: Button
    private lateinit var editTxtEmail: EditText
    private lateinit var editTxtPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var firebaseAut: FirebaseAuth
    private lateinit var backgroundProgressLine: LinearLayout
    private lateinit var textProgress: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permission()

        btnRegistration = findViewById(R.id.btn_registration)
        btnLogin = findViewById(R.id.btn_login)
        editTxtEmail = findViewById(R.id.edit_txt_email)
        editTxtPassword = findViewById(R.id.edit_txt_password)
        progressBar = findViewById(R.id.progress_bar_load_login)
        backgroundProgressLine = findViewById(R.id.background_progredd_bar_line)
        textProgress = findViewById(R.id.txt_progress)

        firebaseAut = FirebaseAuth.getInstance()

        btnRegistration.setOnClickListener(this)
        btnLogin.setOnClickListener(this)

        if(firebaseAut.currentUser != null) {
            startActivity(Intent(applicationContext, MainPageActivity::class.java))
        }
    }

     fun registerUser(){
        val email = editTxtEmail.text.toString().trim()
        val password = editTxtPassword.text.toString().trim()
        textProgress.text = "Выполняется регистрация"

        when {
            TextUtils.isEmpty(email) -> {
                Toast.makeText(this, "Введите Email", Toast.LENGTH_SHORT).show()
                return
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show()
                return
            }
            password.length < 5 -> {
                Toast.makeText(this, "Пароль должен состоять минимум из 5 символов",
                    Toast.LENGTH_SHORT).show()
                return
            }
        }
        backgroundProgressLine.visibility = LinearLayout.VISIBLE

        firebaseAut.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Регистрация выполнена", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                    startActivity(Intent(applicationContext, MainPageActivity::class.java))
                } else {
                    Toast.makeText(this, "Регистрация провалена, повторите попытку",
                        Toast.LENGTH_SHORT).show()
                }
                backgroundProgressLine.visibility = LinearLayout.INVISIBLE
            }
    }

    private fun loginUser() {
        val email = editTxtEmail.text.toString().trim()
        val password = editTxtPassword.text.toString().trim()
        textProgress.text = "Выполняется вход"
        when {
            TextUtils.isEmpty(email) -> {
                Toast.makeText(this, "Введите Email", Toast.LENGTH_SHORT).show()
                return
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show()
                return
            }
        }
        backgroundProgressLine.visibility = LinearLayout.VISIBLE

        firebaseAut.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    finish()
                    startActivity(Intent(applicationContext, MainPageActivity::class.java))
                } else {
                    Toast.makeText(this, "Неверный Email или пароль",
                        Toast.LENGTH_SHORT).show()
                }
                backgroundProgressLine.visibility = LinearLayout.INVISIBLE
            }
    }

    override fun onClick(view: View?) {
        if (view == btnRegistration) {
            registerUser()
        }
        if (view == btnLogin) {
            loginUser()
        }
    }


    private fun permission() {
        if(ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE)
        } else {
            musicFiles = getAllAudio(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                musicFiles = getAllAudio(this)
            }
            else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE)
            }
        }
    }
    private fun getAllAudio(context: Context): ArrayList<MusicFiles> {
        val tempAudioList : ArrayList<MusicFiles> = ArrayList()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA, //For Path
            MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.AlbumColumns.ALBUM)
        val cursor: Cursor? = context.contentResolver.query(uri, projection, null,
            null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val albumArt:String = if (cursor.getString(0) == null) {
                    "unknown album art"
                } else {
                    cursor.getString(0)
                }
                val title: String = if (cursor.getString(1) == null) {
                    "unknown title"
                } else {
                    cursor.getString(1)
                }
                val duration: String = if (cursor.getString(2) == null) {
                    "unknown duration"
                } else {
                    cursor.getString(2)
                }
                val path: String = if (cursor.getString(3) == null) {
                    "unknown path"
                } else {
                    cursor.getString(3)
                }
                val artist: String = if (cursor.getString(4) == null) {
                    "unknown artist"
                } else {
                    cursor.getString(4)
                }
                val albumName: String = if(cursor.getString(5) == null) {
                    "unknown album"
                } else {
                    cursor.getString(5)
                }

                val musicFiles: MusicFiles = MusicFiles(path, title, artist, albumArt, duration, albumName)
                Log.e("Path : $path", "Album : $albumArt")
                tempAudioList.add(musicFiles)
            }
            cursor.close()
        }
        return tempAudioList
    }
}