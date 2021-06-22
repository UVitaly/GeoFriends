package com.example.luf

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterUser : AppCompatActivity(), View.OnClickListener {
    private var banner: TextView? = null
    private var registerUser: TextView? = null
    private var editTextFullName: EditText? = null
    private var editTextAge: EditText? = null
    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var progressBar: ProgressBar? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        mAuth = FirebaseAuth.getInstance()
        banner = findViewById<View>(R.id.banner) as TextView
        banner!!.setOnClickListener(this)
        registerUser = findViewById<View>(R.id.registerUser) as Button
        registerUser!!.setOnClickListener(this)
        editTextFullName = findViewById<View>(R.id.fullName) as EditText
        editTextAge = findViewById<View>(R.id.age) as EditText
        editTextEmail = findViewById<View>(R.id.email) as EditText
        editTextPassword = findViewById<View>(R.id.password) as EditText
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.banner -> startActivity(Intent(this, MainActivity::class.java))
            R.id.registerUser -> registerUser()
        }
    }

    private fun registerUser() {
        val email = editTextEmail!!.text.toString().trim { it <= ' ' }
        val password = editTextPassword!!.text.toString().trim { it <= ' ' }
        val fullName = editTextFullName!!.text.toString().trim { it <= ' ' }
        val age = editTextAge!!.text.toString().trim { it <= ' ' }
        if (fullName.isEmpty()) {
            editTextFullName!!.error = "Не введены Ф.И.О"
            editTextFullName!!.requestFocus()
            return
        }

        if (age.isEmpty()) {
            editTextAge!!.error = "Вы не указали свой возраст"
            editTextAge!!.requestFocus()
            return
        }

        if (email.isEmpty()) {
            editTextEmail!!.error = "Вы не указали свой email"
            editTextEmail!!.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail!!.error = "Неверный email - адрес"
            editTextEmail!!.requestFocus()
            return
        }

        if (password.isEmpty()) {
            editTextPassword!!.error = "Вы не ввели пароль"
            editTextPassword!!.requestFocus()
            return
        }

        if (password.length < 6) {
            editTextPassword!!.error = "Пароль должен состоять из 6 и более символов"
            editTextPassword!!.requestFocus()
            return
        }

        progressBar!!.visibility = View.VISIBLE
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = User(fullName, age, email)
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@RegisterUser, "Пользователь был зарегистрирован успешно.", Toast.LENGTH_LONG).show()
                        progressBar!!.visibility = View.GONE
                        startActivity(Intent(this@RegisterUser, MainActivity::class.java))
                    } else {
                        Toast.makeText(this@RegisterUser, "Ошибка регистрации! Попытайтесь снова.", Toast.LENGTH_LONG).show()
                        progressBar!!.visibility = View.GONE
                    }
                }
            } else {
                Toast.makeText(this@RegisterUser, "Ошибка регистрации! Попытайтесь снова.", Toast.LENGTH_LONG).show()
                progressBar!!.visibility = View.GONE
            }
        }
    }
}
