package com.undefinedparticle.instagramclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.FragmentHomeBinding
import com.undefinedparticle.instagramclone.fragments.myprofile.MyPostAdapter
import com.undefinedparticle.instagramclone.fragments.myprofile.ViewProfilePhotoDialogFragment
import com.undefinedparticle.instagramclone.fragments.story.StoryAdapter
import com.undefinedparticle.instagramclone.models.Posts
import com.undefinedparticle.instagramclone.models.User
import com.undefinedparticle.instagramclone.utils.FOLLOWING_NODE
import com.undefinedparticle.instagramclone.utils.POST_NODE
import com.undefinedparticle.instagramclone.utils.STORY_NODE
import com.undefinedparticle.instagramclone.utils.USER_NODE
import com.undefinedparticle.instagramclone.utils.USER_PROFILE_FOLDER
import com.undefinedparticle.instagramclone.utils.uploadImage

class HomeFragment : Fragment() {
    lateinit var binding:FragmentHomeBinding

    private lateinit var postList:ArrayList<Posts>
    private lateinit var userList:ArrayList<User>
    private lateinit var myPostAdapter: MyPostAdapter
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var user: User
    private val db = Firebase.firestore

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

        uri?.let {

            uploadImage(uri, USER_PROFILE_FOLDER) {
                if (it != null) {
                    user.storyImage = it
                    binding.storyImage.setImageURI(uri)
                    binding.storyImage.borderColor = resources.getColor(R.color.post_blue)

                    db.collection(STORY_NODE)
                        .document(Firebase.auth.currentUser!!.uid)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Story uploaded successfully!", Toast.LENGTH_SHORT).show()
                        }
                }
            }

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this

        user = User()
        getCurrentUser()

        postList = ArrayList()
        myPostAdapter = MyPostAdapter(requireContext(), postList, user)
        binding.recyclerView.adapter = myPostAdapter

        userList = ArrayList()
        storyAdapter = StoryAdapter(requireContext(), userList)
        binding.storyRecyclerView.adapter = storyAdapter

        loadStory()
        loadData()

        binding.uploadStory.setOnClickListener {

            galleryLauncher.launch("image/*")

        }

        binding.notificationButton.setOnClickListener {

            Toast.makeText(context, "This is under development!", Toast.LENGTH_SHORT).show()


        }
        binding.chatButton.setOnClickListener {

            Toast.makeText(context, "This is under development!", Toast.LENGTH_SHORT).show()


        }

        binding.storyImage.setOnClickListener {

            if(user.storyImage != null){

                val dialogFragment = ViewProfilePhotoDialogFragment(user.storyImage.toString())

                dialogFragment.show(requireActivity().supportFragmentManager, "fullScreenDialog")


            }

        }



        return binding.root
    }

    private fun getCurrentUser(){

        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {

                user = it.toObject<User>()!!
                myPostAdapter.notifyDataSetChanged()
            }

    }

    private fun loadData(){

        Firebase.firestore.collection(POST_NODE).get().addOnSuccessListener {

            val tempList = ArrayList<Posts>()

            for(document in it.documents){
                val post = document.toObject(Posts::class.java)
                if (post != null) {
                    post.postId = document.id
                }
                post?.let {
                    tempList.add(post)
                }
            }

            postList.clear()
            postList.addAll(tempList)
            myPostAdapter.notifyDataSetChanged()

            binding.isThereAnyPosts = postList.isNotEmpty()

        }


    }

    private fun loadStory(){

        /*db.collection(STORY_NODE)
            .document(Firebase.auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                val story = it.toObject(User::class.java)
                if(story!!.storyImage != null) {
                    *//*Glide.with(requireContext())
                        .load(story.profilePic)
                        .into(binding.storyImage)*//*
                }else{
                    binding.storyImage.setImageResource(R.drawable.default_user_profile)
                }
            }*/

        Firebase.firestore.collection(STORY_NODE).get().addOnSuccessListener {

            val tempList = ArrayList<User>()

            for(document in it.documents){
                val user = document.toObject(User::class.java)
                user?.let {
                    tempList.add(user)
                }
            }

            userList.clear()
            userList.addAll(tempList)
            storyAdapter.notifyDataSetChanged()

        }


    }


    override fun onStart() {
        super.onStart()

        loadStory()
        loadData()
    }

    override fun onResume() {
        super.onResume()

        loadStory()
        loadData()
    }

}