package com.example.firebaseloginauthentication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.firebaseloginauthentication.R
import com.example.firebaseloginauthentication.utils.Extensions.toast
import com.example.firebaseloginauthentication.utils.FirebaseUtils.firebaseAuth

class SignInActivity : AppCompatActivity() {
    lateinit var edtSignInEmail: EditText
    lateinit var edtSignInPassword: EditText
    lateinit var btnSignIn: Button
    lateinit var btnCreateAccount: Button

    lateinit var signInEmail: String
    lateinit var signInPassword: String
    lateinit var signInInputArray: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initView()
        signInInputArray = arrayOf(edtSignInEmail, edtSignInPassword)
        clickListener()
    }


    private fun initView() {
        edtSignInEmail = findViewById(R.id.edt_sign_in_email)
        edtSignInPassword = findViewById(R.id.edt_sign_in_password)
        btnSignIn = findViewById(R.id.btn_signIn)
        btnCreateAccount = findViewById(R.id.btn_createAccount)

    }

    private fun clickListener() {
        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSignIn.setOnClickListener {
            signInUser()
        }
    }

    private fun notEmpty(): Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()

    private fun signInUser() {
        signInEmail = edtSignInEmail.text.toString().trim()
        signInPassword = edtSignInPassword.text.toString().trim()


        if (notEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        toast("signed in successfully")
                        finish()
                    } else {
                        toast("sign in failed")
                    }
                }
        } else {
            signInInputArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        }

    }
}