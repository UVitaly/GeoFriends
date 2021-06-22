package com.example.luf

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity()
{
    private var logout: Button? = null
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        logout = findViewById<View>(R.id.signOut) as Button
        logout!!.setOnClickListener()
        {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
        }
    }
}