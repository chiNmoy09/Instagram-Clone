package com.undefinedparticle.instagramclone.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.databinding.ActivityMainBinding
import com.undefinedparticle.instagramclone.fragments.AddBottomSheetDialogFragment
import com.undefinedparticle.instagramclone.fragments.search.ExploreFragment
import com.undefinedparticle.instagramclone.fragments.HomeFragment
import com.undefinedparticle.instagramclone.fragments.ProfileFragment
import com.undefinedparticle.instagramclone.fragments.ReelsFragment
import com.undefinedparticle.instagramclone.models.MainViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    companion object{
        lateinit var mainViewModel: MainViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        auth = Firebase.auth


        binding.addButton.setOnClickListener {

            val addBottomSheetDialogFragment = AddBottomSheetDialogFragment()
            addBottomSheetDialogFragment.show(supportFragmentManager, "addBottomSheetDialogFragment")

        }

        replaceFragment(HomeFragment())
        binding.bottomNavView.setOnNavigationItemSelectedListener {item ->

            when(item.itemId){
                R.id.homeFragment ->{

                    replaceFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.exploreFragment ->{

                    replaceFragment(ExploreFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                /*R.id.addFragment ->{

                    replaceFragment(AddBottomSheetDialogFragment())
                    return@setOnNavigationItemSelectedListener true
                }*/
                R.id.reelsFragment ->{

                    replaceFragment(ReelsFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.profileFragment ->{

                    replaceFragment(ProfileFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }


        }

        observeLiveData()
    }

    private fun observeLiveData(){

        mainViewModel.loggedOut.observe(this, Observer {

            Log.d("loggedOut","loggedOut: $it")

            if(it == true){
                if(auth.currentUser != null){
                    Toast.makeText(this@MainActivity, "You're logged out!", Toast.LENGTH_SHORT).show()

                    Firebase.auth.signOut()
                    restartApplication()
                }

            }

        })

    }

    private fun restartApplication() {
        finish()
        val intent = Intent(this@MainActivity, SplashActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    // Function to replace fragments
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        if(currentFragment is HomeFragment){
            finish()
        }else{
            binding.bottomNavView.selectedItemId = R.id.homeFragment
        }

    }

}