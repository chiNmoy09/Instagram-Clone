package com.undefinedparticle.instagramclone.utils

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun uploadImage(uri: Uri, folderName: String, callback:(String?)->Unit){

    var imageUrl: String? = null
    
    FirebaseStorage.getInstance().getReference(folderName)
        .child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener { itUri ->

            itUri.storage.downloadUrl.addOnSuccessListener {
                imageUrl = it.toString()
                callback(imageUrl)
            }

        }
}