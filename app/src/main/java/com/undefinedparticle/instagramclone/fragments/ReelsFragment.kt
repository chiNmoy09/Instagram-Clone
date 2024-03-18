package com.undefinedparticle.instagramclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.FragmentReelsBinding
import com.undefinedparticle.instagramclone.fragments.myprofile.ReelAdapter
import com.undefinedparticle.instagramclone.models.Reels
import com.undefinedparticle.instagramclone.utils.REELS_NODE

class ReelsFragment : Fragment() {
    lateinit var binding: FragmentReelsBinding
    lateinit var reelList: ArrayList<Reels>
    lateinit var reelAdapter: ReelAdapter
    lateinit var viewPager2: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reels, container, false)
        binding.lifecycleOwner = this

        viewPager2 = binding.viewPager2
        reelList = ArrayList()
        reelAdapter = ReelAdapter(requireContext(), reelList)
        viewPager2.adapter = reelAdapter


        Firebase.firestore.collection(REELS_NODE).get().addOnSuccessListener {

            val tempList = ArrayList<Reels>()

            for(document in it.documents){
                val reels = document.toObject(Reels::class.java)
                reels?.let {
                    tempList.add(reels)
                }
            }

            reelList.clear()
            reelList.addAll(tempList)
            reelAdapter.notifyDataSetChanged()

            binding.isThereAnyPosts = reelList.isNotEmpty()

        }

        return binding.root
    }


}