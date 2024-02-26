package com.undefinedparticle.instagramclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.ActivitySignUpBinding
import com.undefinedparticle.instagramclone.models.User
import com.undefinedparticle.instagramclone.utils.USER_NODE
import com.undefinedparticle.instagramclone.utils.USER_PROFILE_FOLDER
import com.undefinedparticle.instagramclone.utils.uploadImage

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    lateinit var user: User
    val db = Firebase.firestore
    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

        uri?.let {

            uploadImage(uri, USER_PROFILE_FOLDER) {
                if (it != null) {
                    user.profilePic = it
                    binding.profileImage.setImageURI(uri)
                }
            }

        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.lifecycleOwner = this

        binding.handler = ClickHandler()

        // Initialize Firebase Auth
        auth = Firebase.auth
        user = User()


    }

    inner class ClickHandler() {

        fun uploadProfile(view: View){

            galleryLauncher.launch("image/*")

        }

        fun onRegister(view: View) {

            if (binding.name.text.toString() == "" || binding.userName.text.toString() == "" || binding.emailId.text.toString() == "" || binding.password.text.toString() == "") {

                if (binding.name.text.toString() == "") {
                    binding.name.error = "Required!"
                }
                if (binding.userName.text.toString() == "") {
                    binding.userName.error = "Required!"
                }
                if (binding.emailId.text.toString() == "") {
                    binding.emailId.error = "Required!"
                }
                if (binding.password.text.toString() == "") {
                    binding.password.error = "Required!"
                }

                return
            }

            val name = binding.name.text.toString().trim()
            val userName = binding.userName.text.toString().trim()
            val email = binding.emailId.text.toString().trim()
            val password = binding.password.text.toString().trim()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("onRegister", "createUserWithEmail: success")
                        user.name = name
                        user.userName = userName
                        user.email = email
                        user.password = password
                        // Add a new document with a generated ID
                        db.collection(USER_NODE)
                            .document(Firebase.auth.currentUser!!.uid)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this@SignUpActivity, "User created successfully!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                                finish()
                            }

                        //updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("onRegister", "createUserWithEmail: ${task.exception?.localizedMessage}");
                        Toast.makeText(this@SignUpActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }


        }

        fun onLogIn(view: View) {

            startActivity(Intent(this@SignUpActivity, LogInActivity::class.java))
            finish()

        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) go to main screen
        if (auth.currentUser != null) {
            //go to main screen
        }
    }
}