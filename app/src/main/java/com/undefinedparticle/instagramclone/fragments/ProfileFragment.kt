package com.undefinedparticle.instagramclone.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.activities.EditProfileActivity
import com.undefinedparticle.instagramclone.activities.MainActivity
import com.undefinedparticle.instagramclone.databinding.FragmentProfileBinding
import com.undefinedparticle.instagramclone.databinding.ProfileMenuBinding
import com.undefinedparticle.instagramclone.fragments.myprofile.ViewPagerAdapter
import com.undefinedparticle.instagramclone.fragments.myprofile.ViewProfilePhotoDialogFragment
import com.undefinedparticle.instagramclone.models.MainViewModel
import com.undefinedparticle.instagramclone.models.User
import com.undefinedparticle.instagramclone.utils.FOLLOWING_NODE
import com.undefinedparticle.instagramclone.utils.REELS_NODE
import com.undefinedparticle.instagramclone.utils.STORY_NODE
import com.undefinedparticle.instagramclone.utils.USER_NODE
import com.undefinedparticle.instagramclone.utils.USER_PROFILE_FOLDER
import com.undefinedparticle.instagramclone.utils.uploadImage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private var imageURL = "imageURL"
    private lateinit var mainViewModel: MainViewModel


    private lateinit var auth: FirebaseAuth
    lateinit var user: User
    private val db = Firebase.firestore
    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

        uri?.let {

            uploadImage(uri, USER_PROFILE_FOLDER) {
                if (it != null) {
                    user.profilePic = it
                    binding.profileImage.setImageURI(uri)

                    db.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                        }
                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.lifecycleOwner = this

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.handler = ClickHandler()
        auth = Firebase.auth
        user = User()
        loadData()

        viewPagerAdapter = ViewPagerAdapter(requireActivity(), user)
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

        binding.menuIcon.setOnClickListener{

            showPopupMenu()

        }

        //observeLiveData()

        lifecycleScope.launch {

            binding.noOfFollowers.text = countFollowings().toString()

        }
        lifecycleScope.launch {

            binding.noOfFollowings.text = countFollowings().toString()

        }
        lifecycleScope.launch {

            binding.totalPosts.text = countPosts().toString()

        }

        return binding.root
    }

    private fun observeLiveData() {
        MainActivity.mainViewModel.totalPosts.observe(viewLifecycleOwner, Observer {

            binding.totalPosts.text = it.toString()

        })
    }

    private fun showPopupMenu() {
        val binding1: ProfileMenuBinding = DataBindingUtil.inflate(
            getLayoutInflater(),
            R.layout.profile_menu,
            view as ViewGroup?,
            false
        )
        val view1: View = binding1.root
        val builder = AlertDialog.Builder(context)
        builder.setView(view1)
        val alertDialog = builder.create()


        binding1.layoutMenu.setOnClickListener(View.OnClickListener {

            alertDialog.dismiss()

        })


        binding1.help.setOnClickListener(View.OnClickListener {

            Toast.makeText(context, "This is under development!", Toast.LENGTH_SHORT).show()

        })

        binding1.logoutButton.setOnClickListener(View.OnClickListener {

            MainActivity.mainViewModel.loggedOut.value = true
            alertDialog.dismiss()
            Log.d("loggedOut","logoutButton: clicked!")

        })


        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.setCancelable(true)
        alertDialog.show()
        alertDialog.window!!.setGravity(Gravity.TOP)
    }

    private suspend fun countFollowings(): Int{
        var totalFollowing = 0
        val followingTask = Firebase.firestore.collection(FOLLOWING_NODE).get()

        val followingSnapshot = followingTask.await()

        totalFollowing += followingSnapshot.size()

        return totalFollowing
    }

    private suspend fun countPosts(): Int {
        var totalPosts = 0
        val userPostsTask = Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).get()
        val reelsPostsTask = Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REELS_NODE).get()

        val userPostsSnapshot = userPostsTask.await()
        val reelsPostsSnapshot = reelsPostsTask.await()

        totalPosts += userPostsSnapshot.size() + reelsPostsSnapshot.size()

        return totalPosts
    }

    inner class ClickHandler(){

        fun viewProfilePhoto(view: View): Boolean{

            Log.d("imageURL", "imageURL: $imageURL")
            if(imageURL == "imageURL"){
                return false
            }

            val dialogFragment = ViewProfilePhotoDialogFragment(imageURL)
            dialogFragment.show(requireActivity().supportFragmentManager, "fullScreenDialog")

            return true
        }

        fun changeImage(view: View){
            galleryLauncher.launch("image/*")
        }

    }

    private fun loadData(){
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {

                user = it.toObject<User>()!!
                binding.name.text = user.name
                binding.userName.text = "@" + user.userName

                if(!user.bio.isNullOrEmpty()){
                    binding.bio.text = user.bio
                }

                if(!user.profilePic.isNullOrEmpty()){
                    // Load the image into the ImageView using Glide
                    imageURL = user.profilePic!!
                    Glide.with(this)
                        .load(user.profilePic)
                        .into(binding.profileImage);
                }

            }
    }
    override fun onStart() {
        super.onStart()

        loadData()

        //binding.totalPosts.text = countPosts().toString()

    }

}