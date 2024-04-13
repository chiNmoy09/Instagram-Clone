package com.undefinedparticle.instagramclone.models

class Posts {

    var profilePic: String? = null
    var name: String? = null
    var userName: String? = null
    var imageUrl: String? = null
    var postCaption: String? = null
    var postLocation: String? = null
    var postId: String? = null
    var isLiked: Boolean = false
    var postTime: Long? =null
    var likeCount: Int? = null
    var commentsCount: Int? = null

    constructor()
    constructor(
        profilePic: String?,
        name: String?,
        userName: String?,
        imageUrl: String?,
        postCaption: String?,
        postLocation: String?
    ) {
        this.profilePic = profilePic
        this.name = name
        this.userName = userName
        this.imageUrl = imageUrl
        this.postCaption = postCaption
        this.postLocation = postLocation
    }
}