package com.example.luf

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), View.OnClickListener
{
    private var register: TextView? = null
    private var forgotPassword: TextView? = null
    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var signIn: Button? = null
    private var mAuth: FirebaseAuth? = null
    private var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        register = findViewById<View>(R.id.register) as TextView
        register!!.setOnClickListener(this)
        signIn = findViewById<View>(R.id.signIn) as Button
        signIn!!.setOnClickListener(this)
        editTextEmail = findViewById<View>(R.id.email) as EditText
        editTextPassword = findViewById<View>(R.id.password) as EditText
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        mAuth = FirebaseAuth.getInstance()
        forgotPassword = findViewById<View>(R.id.forgotPassword) as TextView
        forgotPassword!!.setOnClickListener(this)
    }

    override fun onClick(v: View)
    {
        when (v.id)
        {
            R.id.register -> startActivity(Intent(this, RegisterUser::class.java))
            R.id.signIn -> userLogin()
            R.id.forgotPassword -> startActivity(Intent(this, ForgotPassword::class.java))
        }
    }

    private fun userLogin()
    {
        val email = editTextEmail!!.text.toString().trim { it <= ' ' }
        val password = editTextPassword!!.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
            editTextEmail!!.error = "Введите свой email - адрес!"
            editTextEmail!!.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail!!.error = "Пожалуйста введите существующий email - адрес"
            editTextEmail!!.requestFocus()
            return
        }
        if (password.isEmpty())
        {
            editTextPassword!!.error = "Введите действующий пароль!"
            editTextPassword!!.requestFocus()
            return
        }
        if (password.length < 6)
        {
            editTextPassword!!.error = "Пароль должен состоять из 6 символов и более, напишите пароль снова!"
            editTextPassword!!.requestFocus()
            return
        }
        progressBar!!.visibility = View.VISIBLE
        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                val user = FirebaseAuth.getInstance().currentUser
                if (user!!.isEmailVerified)
                {
                    startActivity(Intent(this@MainActivity, MenuAct::class.java))
                }
                else
                {
                    user.sendEmailVerification()
                    Toast.makeText(this@MainActivity, "Вам на почту пришло письмо проверьте его!", Toast.LENGTH_LONG).show()
                }
            }
            else
            {
                Toast.makeText(this@MainActivity, "Ошибка ввода логина или пароля попытайтесь снова или проверьте есть ли у вас аккаунт", Toast.LENGTH_LONG).show()
            }
        }
    }
}