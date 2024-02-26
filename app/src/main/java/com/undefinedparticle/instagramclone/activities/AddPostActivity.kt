package com.undefinedparticle.instagramclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.undefinedparticle.instagramclone.databinding.ActivityAddPostBinding
import com.undefinedparticle.instagramclone.models.Posts
import com.undefinedparticle.instagramclone.models.User
import com.undefinedparticle.instagramclone.utils.POST_NODE
import com.undefinedparticle.instagramclone.utils.USER_NODE
import com.undefinedparticle.instagramclone.utils.USER_POST_FOLDER
import com.undefinedparticle.instagramclone.utils.uploadImage

class AddPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPostBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: User
    val db = Firebase.firestore
    private var imageURL = "imageURL"
    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

        uri?.let {

            uploadImage(uri, USER_POST_FOLDER) {
                if (it != null) {
                    imageURL = it

                    binding.imageView1.setImageURI(uri)
                    binding.imageView1.visibility = View.VISIBLE
                    binding.selectImage.visibility = View.GONE
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_post)
        binding.lifecycleOwner = this

        binding.handler = ClickHandler()

        auth = Firebase.auth
        user = User()

        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {

                user = it.toObject<User>()!!

            }

    }


    inner class ClickHandler(){

        fun goToBack(view: View){
            finish()
        }

        fun uploadImage(view: View){
            galleryLauncher.launch("image/*")
        }

        fun sharePost(view: View){

            if (imageURL == "imageURL"){
                Toast.makeText(this@AddPostActivity, "Please upload an image!", Toast.LENGTH_SHORT).show()
                return
            }

            var posts = Posts(user.profilePic,
                user.name,
                user.userName,
                imageURL,
                binding.postCaption.text.toString(),
                binding.postLocation.text.toString())

            Firebase.firestore.collection(POST_NODE).document().set(posts)
                .addOnSuccessListener {

                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(posts)
                        .addOnSuccessListener{

                            finish()

                        }

                }

        }

    }

    override fun onBackPressed() {
        finish()
    }


}