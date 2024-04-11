package com.undefinedparticle.instagramclone.fragments.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.activities.MainActivity
import com.undefinedparticle.instagramclone.databinding.FragmentExploreBinding
import com.undefinedparticle.instagramclone.fragments.myprofile.MyPostAdapter
import com.undefinedparticle.instagramclone.models.MainViewModel
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
    private lateinit var mainViewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false)
        binding.lifecycleOwner = this

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.viewModel = mainViewModel
        auth = Firebase.auth
        user = User()

        userList = ArrayList()
        searchAdapter = SearchAdapter(requireContext(), userList)
        binding.recyclerView.adapter = searchAdapter

        loadData()
        observeLiveData()

        return binding.root
    }

    private fun observeLiveData(){
        mainViewModel.searchText.observe(requireActivity(), Observer {

            if(it.isNullOrEmpty()){
                loadData()
            }else{
                searchData(it)
            }

        })
    }

    private fun searchData(searchText: String){

        Firebase.firestore.collection(USER_NODE).whereEqualTo("name", searchText).get().addOnSuccessListener {

            val tempList = ArrayList<User>()

            for(document in it.documents){
                val user = document.toObject(User::class.java)

                if(document.id != Firebase.auth.currentUser!!.uid) {
                    user?.let {
                        tempList.add(user)
                    }
                }
            }

            userList.clear()
            userList.addAll(tempList)
            searchAdapter.notifyDataSetChanged()

            binding.noData = userList.size == 0

        }

    }


    private fun loadData(){

        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {

            val tempList = ArrayList<User>()

            for(document in it.documents){
                val user = document.toObject(User::class.java)

                if(document.id != Firebase.auth.currentUser!!.uid) {
                    user?.let {
                        tempList.add(user)
                    }
                }
            }

            userList.clear()
            userList.addAll(tempList)
            searchAdapter.notifyDataSetChanged()

            binding.noData = userList.size == 0
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