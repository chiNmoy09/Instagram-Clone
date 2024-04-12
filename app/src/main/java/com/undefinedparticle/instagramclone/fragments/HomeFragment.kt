package com.undefinedparticle.instagramclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.FragmentHomeBinding
import com.undefinedparticle.instagramclone.fragments.myprofile.MyPostAdapter
import com.undefinedparticle.instagramclone.fragments.story.StoryAdapter
import com.undefinedparticle.instagramclone.models.Posts
import com.undefinedparticle.instagramclone.models.User
import com.undefinedparticle.instagramclone.utils.FOLLOWING_NODE
import com.undefinedparticle.instagramclone.utils.POST_NODE

class HomeFragment : Fragment() {
    lateinit var binding:FragmentHomeBinding

    private lateinit var postList:ArrayList<Posts>
    private lateinit var userList:ArrayList<User>
    private lateinit var myPostAdapter: MyPostAdapter
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this

        postList = ArrayList()
        myPostAdapter = MyPostAdapter(requireContext(), postList)
        binding.recyclerView.adapter = myPostAdapter

        userList = ArrayList()
        storyAdapter = StoryAdapter(requireContext(), userList)
        binding.storyRecyclerView.adapter = storyAdapter

        loadStory()
        loadData()

        return binding.root
    }

    private fun loadData(){

        Firebase.firestore.collection(POST_NODE).get().addOnSuccessListener {

            val tempList = ArrayList<Posts>()

            for(document in it.documents){
                val post = document.toObject(Posts::class.java)
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

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOWING_NODE).get().addOnSuccessListener {

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