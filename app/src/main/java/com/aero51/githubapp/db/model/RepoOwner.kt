package com.aero51.githubapp.db.model

import com.google.gson.annotations.SerializedName

data class RepoOwner(

        val login: String,
        @SerializedName("avatar_url")
        var avatarUrl: String
) {}