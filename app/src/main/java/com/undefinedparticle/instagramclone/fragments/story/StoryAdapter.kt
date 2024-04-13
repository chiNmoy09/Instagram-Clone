package com.undefinedparticle.instagramclone.fragments.story

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.StoryItemBinding
import com.undefinedparticle.instagramclone.fragments.myprofile.ViewProfilePhotoDialogFragment
import com.undefinedparticle.instagramclone.models.User

class StoryAdapter(private var myContext: Context, private var list: ArrayList<User>): RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder(var binding: StoryItemBinding): RecyclerView.ViewHolder(binding.root){



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding: StoryItemBinding = DataBindingUtil.inflate(LayoutInflater.from(myContext), R.layout.story_item, parent, false)
        return StoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {

        val user = list[position]

        if(user.profilePic != null){
            Glide.with(myContext)
                .load(user.profilePic)
                .into(holder.binding.profileImage)
        }else{
            holder.binding.profileImage.setImageResource(R.drawable.default_profile_photo)
        }

        holder.binding.profileImage.setOnClickListener {

            if(user.profilePic != null){

                val dialogFragment = ViewProfilePhotoDialogFragment(user!!.profilePic.toString())
                val fragmentManager = (myContext as FragmentActivity).supportFragmentManager
                dialogFragment.show(fragmentManager, "fullScreenDialog")


            }

        }

    }

}