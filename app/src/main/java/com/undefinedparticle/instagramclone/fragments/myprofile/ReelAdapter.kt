package com.undefinedparticle.instagramclone.fragments.myprofile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.SampleReelItemBinding
import com.undefinedparticle.instagramclone.models.Reels

class ReelAdapter(private val context: Context, var list: ArrayList<Reels>): RecyclerView.Adapter<ReelAdapter.ReelViewHolder>() {

    var reels: Reels? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReelViewHolder {
        val binding: SampleReelItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.sample_reel_item, parent, false)
        return ReelViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ReelViewHolder, position: Int) {
        reels = list[position]

        holder.binding.model = reels
        holder.binding.videoProgressBar.visibility = View.VISIBLE

        Glide.with(context)
            .load(reels!!.profilePic)
            .into(holder.binding.profileImage);

        holder.binding.postVideo.setVideoPath(reels!!.videoUrl)

        holder.binding.postVideo.setOnPreparedListener {

            holder.binding.videoProgressBar.visibility = View.GONE
            it.start()

        }

    }

    inner class ReelViewHolder(var binding: SampleReelItemBinding): RecyclerView.ViewHolder(binding.root){



    }

}