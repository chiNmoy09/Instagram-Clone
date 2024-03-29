package com.undefinedparticle.instagramclone.fragments.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.FragmentExploreBinding
import com.undefinedparticle.instagramclone.fragments.myprofile.MyPostAdapter
import com.undefinedparticle.instagramclone.models.Posts
import com.undefinedparticle.instagramclone.models.User
import com.undefinedparticle.instagramclone.utils.USER_NODE


class ExploreFragment : Fragment() {
    lateinit var binding:FragmentExploreBinding
    private lateinit var auth: FirebaseAuth
    lateinit var user: User
    val db = Firebase.firestore
    private lateinit var userList:ArrayList<User>
    private lateinit var searchAdapter: SearchAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false)
        binding.lifecycleOwner = this

        auth = Firebase.auth
        user = User()

        userList = ArrayList()
        searchAdapter = SearchAdapter(requireContext(), userList)
        binding.recyclerView.adapter = searchAdapter

        loadData()


        return binding.root
    }

    fun loadData(){

        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {

            val tempList = ArrayList<User>()

            for(document in it.documents){
                val user = document.toObject(User::class.java)

                user?.let {
                    tempList.add(user)
                }
            }

            userList.clear()
            userList.addAll(tempList)
            searchAdapter.notifyDataSetChanged()

        }

    }

    override fun onStart() {
        super.onStart()

        loadData()
    }

    override fun onResume() {
        super.onResume()

        loadData()
    }

}