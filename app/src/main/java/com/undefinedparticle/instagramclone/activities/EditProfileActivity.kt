package com.undefinedparticle.instagramclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.ActivityEditProfileBinding
import com.undefinedparticle.instagramclone.models.User
import com.undefinedparticle.instagramclone.utils.USER_NODE
import com.undefinedparticle.instagramclone.utils.USER_PROFILE_FOLDER
import com.undefinedparticle.instagramclone.utils.uploadImage

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        binding.lifecycleOwner = this

        binding.handler = ClickHandler()

        // Initialize Firebase Auth
        auth = Firebase.auth
        user = User()

        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {

                val user:User = it.toObject<User>()!!
                binding.name.setText(user.name)
                binding.userName.setText(user.userName)

                if(!user.bio.isNullOrEmpty()){
                    binding.bio.setText(user.bio)
                }

                if(!user.profilePic.isNullOrEmpty()){
                    // Load the image into the ImageView using Glide
                    Glide.with(this)
                        .load(user.profilePic)
                        .into(binding.profileImage);
                }

            }

    }


    inner class ClickHandler(){

        fun goToBack(view: View){
            finish()
        }

        fun uploadProfile(view: View){

            galleryLauncher.launch("image/*")

        }

        fun onUpdate(view: View) {

            if (binding.name.text.toString() == "" || binding.userName.text.toString() == "") {

                if (binding.name.text.toString() == "") {
                    binding.name.error = "Required!"
                }
                if (binding.userName.text.toString() == "") {
                    binding.userName.error = "Required!"
                }

                return
            }

            val name = binding.name.text.toString().trim()
            val userName = binding.userName.text.toString().trim()
            val bio = binding.bio.text.toString().trim()

            user.name = name
            user.userName = userName
            user.bio = bio

            // Add a new document with a generated ID
            db.collection(USER_NODE)
                .document(Firebase.auth.currentUser!!.uid)
                .set(user)
                .addOnSuccessListener {
                    Toast.makeText(this@EditProfileActivity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    //startActivity(Intent(this@EditProfileActivity, MainActivity::class.java))
                    finish()
                }

        }


    }

    override fun onBackPressed() {
        finish()
    }
}