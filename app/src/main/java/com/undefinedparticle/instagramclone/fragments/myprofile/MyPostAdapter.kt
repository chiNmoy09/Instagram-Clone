package com.undefinedparticle.instagramclone.fragments.myprofile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.SamplePostItemBinding
import com.undefinedparticle.instagramclone.models.Posts

class MyPostAdapter(private val context: Context, private var list: ArrayList<Posts>): RecyclerView.Adapter<MyPostAdapter.PostViewHolder>() {

    var posts: Posts? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding: SamplePostItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.sample_post_item, parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        posts = list[position]

        holder.binding.model = posts

        Glide.with(context)
            .load(posts!!.profilePic)
            .into(holder.binding.profileImage)

        Glide.with(context)
            .load(posts!!.imageUrl)
            .into(holder.binding.postImage)

    }

    inner class PostViewHolder(var binding: SamplePostItemBinding): RecyclerView.ViewHolder(binding.root){



    }

}