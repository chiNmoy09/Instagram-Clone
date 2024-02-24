package com.undefinedparticle.instagramclone.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.activities.EditProfileActivity
import com.undefinedparticle.instagramclone.databinding.FragmentProfileBinding
import com.undefinedparticle.instagramclone.fragments.myprofile.ViewPagerAdapter
import com.undefinedparticle.instagramclone.fragments.myprofile.ViewProfilePhotoDialogFragment
import com.undefinedparticle.instagramclone.models.User
import com.undefinedparticle.instagramclone.utils.USER_NODE


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var imageURL: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.lifecycleOwner = this

        binding.handler = ClickHandler()

        viewPagerAdapter = ViewPagerAdapter(requireActivity())
        viewPager2 = binding.viewPager2
        tabLayout = binding.tabLayout
        //viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        viewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Posts"
                1 -> tab.text = "Reels"
            }
        }.attach()


        binding.editProfileButton.setOnClickListener{

            startActivity(Intent(context, EditProfileActivity::class.java))

        }

        return binding.root
    }

    inner class ClickHandler(){

        fun viewProfilePhoto(view: View): Boolean{

            Log.d("imageURL", "imageURL: $imageURL")
            if(!imageURL.contains("https://")){
                return false
            }

            val dialogFragment = ViewProfilePhotoDialogFragment(imageURL)
            dialogFragment.show(requireActivity().supportFragmentManager, "fullScreenDialog")

            return true
        }

    }

    override fun onStart() {
        super.onStart()

        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {

                val user:User = it.toObject<User>()!!
                binding.name.text = user.name
                binding.userName.text = "@" + user.userName

                if(!user.bio.isNullOrEmpty()){
                    binding.bio.text = user.bio
                }

                if(!user.image.isNullOrEmpty()){
                    // Load the image into the ImageView using Glide
                    imageURL = user.image!!
                    Glide.with(this)
                        .load(user.image)
                        .into(binding.profileImage);
                }

            }

    }

}