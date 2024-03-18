package com.undefinedparticle.instagramclone.models

class Reels {

    var profilePic: String? = null
    var name: String? = null
    var userName: String? = null
    var videoUrl: String? = null
    var postCaption: String? = null
    var postLocation: String? = null
    var postTime: Long? =null
    var likeCount: Int? = null
    var commentsCount: Int? = null

    constructor(
        profilePic: String?,
        name: String?,
        userName: String?,
        videoUrl: String?,
        postCaption: String?,
        postLocation: String?
    ) {
        this.profilePic = profilePic
        this.name = name
        this.userName = userName
        this.videoUrl = videoUrl
        this.postCaption = postCaption
        this.postLocation = postLocation
    }

    constructor()
}