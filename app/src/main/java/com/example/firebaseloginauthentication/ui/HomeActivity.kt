package com.example.firebaseloginauthentication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.firebaseloginauthentication.R
import com.example.firebaseloginauthentication.utils.Extensions.toast
import com.example.firebaseloginauthentication.utils.FirebaseUtils.firebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging


class HomeActivity : AppCompatActivity() {
    lateinit var btnSignOut: Button
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("TAG", "Token failed to receive!")
                return@addOnCompleteListener
            }
            val token: String = task.result
            Log.d("Token", token)

        }


        btnSignOut = findViewById(R.id.btn_sign_out)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        btnSignOut.setOnClickListener {
            firebaseAuth.signOut()
            mGoogleSignInClient.signOut().addOnCompleteListener {
                val intent = Intent(this, CreateAccountActivity::class.java)
                startActivity(intent)
                toast("signed out")
                finish()
            }

        }

    }


}


