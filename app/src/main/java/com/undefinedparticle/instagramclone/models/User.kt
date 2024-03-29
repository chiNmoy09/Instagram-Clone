package com.undefinedparticle.instagramclone.models

class User {

    var profilePic: String? = null
    var name: String? = null
    var userName: String? = null
    var bio: String? = null
    var email: String? = null
    var password: String? = null
    var postCaption: String? = null
    var postLocation: String? = null
    var following: Boolean? = false


    constructor(name: String?, userName: String?, email: String?, password: String?) {
        this.name = name
        this.userName = userName
        this.email = email
        this.password = password
    }

    constructor(profilePic: String?, name: String?, userName:String?, bio:String?, email: String?, password: String?) {
        this.profilePic = profilePic
        this.name = name
        this.userName = userName
        this.bio = bio
        this.email = email
        this.password = password
    }

    constructor()

    constructor(email: String?, password: String?) {
        this.email = email
        this.password = password
    }
}