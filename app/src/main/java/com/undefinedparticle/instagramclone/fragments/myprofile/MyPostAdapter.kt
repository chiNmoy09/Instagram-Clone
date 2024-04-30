package com.undefinedparticle.instagramclone.fragments.myprofile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
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
import com.undefinedparticle.instagramclone.utils.BOOKMARKS_NODE
import com.undefinedparticle.instagramclone.utils.LIKES_NODE

class MyPostAdapter(private val myContext: Context, private var list: ArrayList<Posts>, private val user: User): RecyclerView.Adapter<MyPostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding: SamplePostItemBinding = DataBindingUtil.inflate(LayoutInflater.from(myContext), R.layout.sample_post_item, parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val posts = list[position]

        holder.binding.model = posts

        Glide.with(myContext)
            .load(posts.profilePic)
            .into(holder.binding.profileImage)

        Glide.with(myContext)
            .load(posts.imageUrl)
            .into(holder.binding.postImage)

        Firebase.firestore.collection(LIKES_NODE).document(posts.postId + LIKES_NODE).get().addOnSuccessListener {

            if(it.exists()){
                holder.binding.likeButton.setImageResource(R.drawable.liked_icon)
            }else{
                holder.binding.likeButton.setImageResource(R.drawable.like_icon)
            }

        }

        Firebase.firestore.collection(BOOKMARKS_NODE).document(posts.postId + BOOKMARKS_NODE).get().addOnSuccessListener {

            if(it.exists()){
                holder.binding.bookmarkButton.setImageResource(R.drawable.bookmarked_icon)
            }else{
                holder.binding.bookmarkButton.setImageResource(R.drawable.bookmark_icon)
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

        holder.binding.commentsButton.setOnClickListener {

            Toast.makeText(myContext, "Comments are turned off!", Toast.LENGTH_SHORT).show()


        }

        holder.binding.shareButton.setOnClickListener {

            shareUrl(myContext,posts.imageUrl!!)

        }

        holder.binding.bookmarkButton.setOnClickListener {

            if(!posts.isBookmarked) {
                Firebase.firestore.collection(BOOKMARKS_NODE)
                    .document(posts.postId + BOOKMARKS_NODE)
                    .set(user).addOnSuccessListener {

                        holder.binding.bookmarkButton.setImageResource(R.drawable.bookmarked_icon)

                    }
            }else{
                holder.binding.bookmarkButton.setImageResource(R.drawable.bookmark_icon)
                Firebase.firestore.collection(BOOKMARKS_NODE)
                    .document(posts.postId + BOOKMARKS_NODE)
                    .delete()
            }

            posts.isBookmarked = !posts.isBookmarked
        }

    }

    inner class PostViewHolder(var binding: SamplePostItemBinding): RecyclerView.ViewHolder(binding.root){



    }

    private fun shareImage(context: Context, imagePath: String) {
        // Create an Intent with ACTION_SEND
        val shareIntent = Intent(Intent.ACTION_SEND)

        // Set the MIME type of the content to share
        shareIntent.type = "text/*"

        // Add the image URI to the Intent as EXTRA_STREAM
        val imageUri = Uri.parse(imagePath)
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)

        // Optionally, add a subject to the shared content
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared Post")

        // Launch the Intent to share the content
        context.startActivity(Intent.createChooser(shareIntent, "Share Post"))
    }
    private fun shareUrl(context: Context, text: String) {
        // Create an Intent with ACTION_SEND
        val shareIntent = Intent(Intent.ACTION_SEND)

        // Set the MIME type of the content to share
        shareIntent.type = "text/plain"

        // Add the text to the Intent as EXTRA_TEXT
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)

        // Optionally, add a subject to the shared content
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared Post")

        // Launch the Intent to share the content
        context.startActivity(Intent.createChooser(shareIntent, "Share Post"))
    }



}