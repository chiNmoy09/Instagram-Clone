package com.undefinedparticle.instagramclone.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.ActivityAddReelsBinding
import com.undefinedparticle.instagramclone.models.Posts
import com.undefinedparticle.instagramclone.models.Reels
import com.undefinedparticle.instagramclone.models.User
import com.undefinedparticle.instagramclone.utils.POST_NODE
import com.undefinedparticle.instagramclone.utils.REELS_NODE
import com.undefinedparticle.instagramclone.utils.USER_NODE
import com.undefinedparticle.instagramclone.utils.USER_POST_FOLDER
import com.undefinedparticle.instagramclone.utils.USER_REELS_FOLDER
import com.undefinedparticle.instagramclone.utils.uploadImage
import com.undefinedparticle.instagramclone.utils.uploadVideo

class AddReelsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddReelsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: User
    private var videoURL = "imageURL"
    private lateinit var progressDialog: ProgressDialog
    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

        uri?.let {

            uploadVideo(uri, USER_REELS_FOLDER, progressDialog) {
                if (it != null) {
                    videoURL = it

                    binding.videoView1.setVideoPath(it)
                    binding.videoView1.setOnPreparedListener { video ->

                        video.start()

                    }
                    binding.videoView1.visibility = View.VISIBLE
                    binding.selectImage.visibility = View.GONE
                }
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_reels)
        binding.lifecycleOwner = this

        binding.handler = ClickHandler()
        progressDialog = ProgressDialog(this@AddReelsActivity)

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
            galleryLauncher.launch("video/*")
        }

        fun sharePost(view: View){

            if (videoURL == "imageURL"){
                Toast.makeText(this@AddReelsActivity, "Please upload an image!", Toast.LENGTH_SHORT).show()
                return
            }

            var reels = Reels(user.profilePic,
                user.name,
                user.userName,
                videoURL,
                binding.postCaption.text.toString(),
                binding.postLocation.text.toString())

            Firebase.firestore.collection(REELS_NODE).document().set(reels)
                .addOnSuccessListener {

                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REELS_NODE).document().set(reels)
                        .addOnSuccessListener{

                            restartApplication()

                        }

                }

        }


    }

    override fun onBackPressed() {
        finish()
    }

    private fun restartApplication() {
        finish()
        val intent = Intent(this@AddReelsActivity, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}