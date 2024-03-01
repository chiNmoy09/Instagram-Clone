package com.undefinedparticle.instagramclone.fragments.myprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.FragmentMyPostBinding
import com.undefinedparticle.instagramclone.models.Posts

class MyPostFragment : Fragment() {
    lateinit var binding: FragmentMyPostBinding
    private lateinit var postList:ArrayList<Posts>
    private lateinit var myPostAdapter: MyPostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_post, container, false)
        binding.lifecycleOwner = this

        postList = ArrayList()
        myPostAdapter = MyPostAdapter(requireContext(), postList)
        binding.recyclerView.adapter = myPostAdapter

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {

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


        return binding.root
    }


}