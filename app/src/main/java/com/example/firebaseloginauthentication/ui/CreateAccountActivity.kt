package com.example.firebaseloginauthentication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseloginauthentication.R
import com.example.firebaseloginauthentication.utils.Extensions.toast
import com.example.firebaseloginauthentication.utils.FirebaseUtils.firebaseAuth
import com.example.firebaseloginauthentication.utils.FirebaseUtils.firebaseUser
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class CreateAccountActivity : AppCompatActivity() {
    lateinit var edtEmail: EditText
    lateinit var edtPassword: EditText
    lateinit var edtConfirmPassword: EditText
    lateinit var btnCreateAccount: Button
    lateinit var btnSignIn: Button
    lateinit var btnGoogleSignIn: Button
    lateinit var btnCrash: Button
    val reqCode: Int = 100
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var userEmail: String
    lateinit var userPassword: String
    lateinit var createAccountInputsArray: Array<EditText>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)


        initView()
        createAccountInputsArray = arrayOf(edtEmail, edtPassword, edtConfirmPassword)
        clickListener()


        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun initView() {
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        edtConfirmPassword = findViewById(R.id.edt_confirm_password)
        btnCreateAccount = findViewById(R.id.btn_create_account)
        btnSignIn = findViewById(R.id.btn_sign_in)
        btnGoogleSignIn = findViewById(R.id.btn_sign_in_google)
        btnCrash = findViewById(R.id.btn_crash)
    }


    private fun clickListener() {
        btnCreateAccount.setOnClickListener {
            signIn()
        }

        btnSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            toast("please sign into your account")
            startActivity(intent)
        }

        btnGoogleSignIn.setOnClickListener {
            toast("Logging In")
            val intent: Intent = googleSignInClient.signInIntent
            startActivityForResult(intent, 100)
        }

        btnCrash.setOnClickListener {
            throw RuntimeException()
        }


    }


    override fun onStart() {
        super.onStart()
        //when user already sign-in  open homeActivity
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            toast("welcome back")
        }

        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(
                Intent(
                    this, HomeActivity
                    ::class.java
                )
            )
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == reqCode) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }

    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException) {
            toast(e.toString())
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun notEmpty(): Boolean =
        edtEmail.text.toString().trim().isNotEmpty() && edtPassword.text.toString().trim()
            .isNotEmpty() && edtConfirmPassword.text.toString().trim().isNotEmpty()

    private fun identicalPassword(): Boolean {

        var identical = false
        if (notEmpty() && edtPassword.text.toString()
                .trim() == edtConfirmPassword.text.toString()
                .trim()
        ) {
            identical = true
        } else if (!notEmpty()) {
            createAccountInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = " ${input.hint} is required"
                }
            }
        } else {
            toast("Password are not matching!")
        }
        return identical
    }

    private fun signIn() {
        if (identicalPassword()) {
            userEmail = edtEmail.text.toString().trim()
            userPassword = edtPassword.text.toString().trim()

            // create user
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        toast("Create Account Successfully")
                        sendEmailVerification()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)

                    } else {
                        toast("failed to Authenticate ! ")
                    }
                }
        }
    }

    /* send verification email to the new user. This will only
      work if the firebase user is not null.
    */

    private fun sendEmailVerification() {
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("email sent to $userEmail")
                }
            }
        }
    }


}