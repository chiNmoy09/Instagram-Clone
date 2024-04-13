package com.undefinedparticle.instagramclone.fragments.myprofile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.SamplePostItemBinding
import com.undefinedparticle.instagramclone.models.Posts
import com.undefinedparticle.instagramclone.models.User
import com.undefinedparticle.instagramclone.utils.LIKES_NODE

class MyPostAdapter(private val context: Context, private var list: ArrayList<Posts>, private val user: User): RecyclerView.Adapter<MyPostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding: SamplePostItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.sample_post_item, parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val posts = list[position]

        holder.binding.model = posts

        Glide.with(context)
            .load(posts.profilePic)
            .into(holder.binding.profileImage)

        Glide.with(context)
            .load(posts.imageUrl)
            .into(holder.binding.postImage)

        Firebase.firestore.collection(LIKES_NODE).document(posts.postId + LIKES_NODE).get().addOnSuccessListener {

            if(it.exists()){
                holder.binding.likeButton.setImageResource(R.drawable.liked_icon)
            }else{
                holder.binding.likeButton.setImageResource(R.drawable.like_icon)
            }

        }

        holder.binding.likeButton.setOnClickListener {

            if(!posts.isLiked) {
                Firebase.firestore.collection(LIKES_NODE)
                    .document(posts.postId + LIKES_NODE)
                    .set(user).addOnSuccessListener {

                    holder.binding.likeButton.setImageResource(R.drawable.liked_icon)

                }
            }else{
                holder.binding.likeButton.setImageResource(R.drawable.like_icon)
                Firebase.firestore.collection(LIKES_NODE)
                    .document(posts.postId + LIKES_NODE)
                    .delete()
            }

            posts.isLiked = !posts.isLiked
        }

    }

    inner class PostViewHolder(var binding: SamplePostItemBinding): RecyclerView.ViewHolder(binding.root){



    }

}