package com.undefinedparticle.instagramclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.ActivityLogInBinding
import com.undefinedparticle.instagramclone.databinding.ActivitySignUpBinding
import com.undefinedparticle.instagramclone.models.User

class LogInActivity : AppCompatActivity() {


    lateinit var binding: ActivityLogInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)
        binding.lifecycleOwner = this

        binding.handler = ClickHandler()

        // Initialize Firebase Auth
        auth = Firebase.auth
        user = User()
    }

    inner class ClickHandler() {

        fun onLogIn(view: View) {

            if (binding.emailId.text.toString() == "" || binding.password.text.toString() == "") {

                if (binding.emailId.text.toString() == "") {
                    binding.emailId.error = "Required!"
                }
                if (binding.password.text.toString() == "") {
                    binding.password.error = "Required!"
                }

                return
            }

            val email = binding.emailId.text.toString().trim()
            val password = binding.password.text.toString().trim()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("onLogIn", "signInWithEmail:success")
                        val user = auth.currentUser
                        Toast.makeText(this@LogInActivity, "You are logged in!", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this@LogInActivity, MainActivity::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("onLogIn", "signInWithEmail: ${task.exception?.localizedMessage}")
                        Toast.makeText(this@LogInActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }


        }


        fun onRegister(view: View) {

            startActivity(Intent(this@LogInActivity, SignUpActivity::class.java))
            //finish()

        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) go to main screen
        if (auth.currentUser != null) {
            startActivity(Intent(this@LogInActivity, MainActivity::class.java))
            finish()
        }
    }

}