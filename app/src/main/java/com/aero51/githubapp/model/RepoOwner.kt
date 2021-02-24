package com.aero51.githubapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RepoOwner(

        val login: String,
        @SerializedName("avatar_url")
        var avatarUrl: String
) : Parcelable {}