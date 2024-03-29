package com.undefinedparticle.instagramclone.fragments.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.SearchItemBinding
import com.undefinedparticle.instagramclone.models.User

class SearchAdapter(private val context: Context, var list: ArrayList<User>): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

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
        holder.binding.following = user!!.following


        holder.binding.followButton.setOnClickListener {

            user!!.following = !user!!.following!!
            holder.binding.following = user!!.following

        }


    }

}