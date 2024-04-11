package com.undefinedparticle.instagramclone.fragments.search

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
import com.undefinedparticle.instagramclone.databinding.SearchItemBinding
import com.undefinedparticle.instagramclone.models.User
import com.undefinedparticle.instagramclone.utils.FOLLOWING_NODE

class SearchAdapter(private val context: Context, private var list: ArrayList<User>): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var user: User? = null

    inner class SearchViewHolder (val binding:SearchItemBinding): RecyclerView.ViewHolder(binding.root){



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding: SearchItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.search_item, parent, false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        user = list[position]

        Glide.with(context)
            .load(user!!.profilePic)
            .into(holder.binding.profileImage)

        holder.binding.name.text = user!!.name.toString()
        holder.binding.userName.text = user!!.userName.toString()

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOWING_NODE).whereEqualTo("email", user!!.email)
            .get().addOnSuccessListener {

                user!!.following = !it.isEmpty
                holder.binding.following = user!!.following

            }


        holder.binding.followButton.setOnClickListener {

            if(user!!.following == true){
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOWING_NODE).whereEqualTo("email", user!!.email)
                    .get().addOnSuccessListener {

                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOWING_NODE).document(
                            it.documents[0].id
                        ).delete()

                    }
            }else{
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOWING_NODE).document().set(user!!)
            }

            user!!.following = !user!!.following!!
            holder.binding.following = user!!.following

        }


    }

}