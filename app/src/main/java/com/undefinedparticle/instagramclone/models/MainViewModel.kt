package com.undefinedparticle.instagramclone.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application): AndroidViewModel(application) {

    var searchText:MutableLiveData<String> = MutableLiveData("")
    var loggedOut: MutableLiveData<Boolean> = MutableLiveData(false)

}