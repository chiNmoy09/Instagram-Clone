package com.undefinedparticle.instagramclone.fragments.myprofile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.undefinedparticle.instagramclone.models.User

class ViewPagerAdapter(fragmentActivity: FragmentActivity, val user: User): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }


    override fun createFragment(position: Int): Fragment {
        return when(position){

            0 -> {
                MyPostFragment(user)
            }

            1 -> {
                MyReelsFragment()
            }

            else -> {
                MyPostFragment(user)
            }

        }
    }

}