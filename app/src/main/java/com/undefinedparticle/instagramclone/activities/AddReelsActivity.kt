package com.undefinedparticle.instagramclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.ActivityAddReelsBinding

class AddReelsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddReelsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_reels)
        binding.lifecycleOwner = this

        binding.handler = ClickHandler()


    }

    inner class ClickHandler(){

        fun goToBack(view: View){
            finish()
        }

    }

    override fun onBackPressed() {
        finish()
    }


}