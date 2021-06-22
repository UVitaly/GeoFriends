package com.example.luf

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity()
{
    private var emailEditText: EditText? = null
    private var resetPasswordButton: Button? = null
    private var progressBar: ProgressBar? = null
    var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        emailEditText = findViewById<View>(R.id.email) as EditText
        resetPasswordButton = findViewById<View>(R.id.resetPassword) as Button
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        auth = FirebaseAuth.getInstance()
        resetPasswordButton!!.setOnClickListener { resetPassword() }
    }

    private fun resetPassword()
    {
        val email = emailEditText!!.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
            emailEditText!!.error = "Неверный логин!"
            emailEditText!!.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEditText!!.error = "Пожалуйста введите действующий email!"
            emailEditText!!.requestFocus()
            return
        }
        progressBar!!.visibility = View.VISIBLE
        auth!!.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                Toast.makeText(this@ForgotPassword, "проверьте вашу почту и поменяйте пароль", Toast.LENGTH_LONG).show()
            }
            else
            {
                Toast.makeText(this@ForgotPassword, "Попытайтесь снова, возможно произошла ошибка", Toast.LENGTH_LONG).show()
            }
        }
    }
}