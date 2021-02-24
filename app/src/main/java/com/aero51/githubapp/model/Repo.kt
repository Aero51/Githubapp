package com.aero51.githubapp.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "repos")
data class Repo(
        @PrimaryKey
        @SerializedName("id")
        val id: Long,

        @SerializedName("name")
        val name: String,

        @SerializedName("full_name")
        val fullName: String,

        @SerializedName("description")
        val description: String,


        @SerializedName("html_url")
        val url: String,


        @SerializedName("stargazers_count")
        val stars: Int,


        @SerializedName("forks_count")
        val forks: Int,


        @SerializedName("language")
        val language: String,


        @SerializedName("watchers_count")
        val watchers: Int,

        @SerializedName("open_issues_count")
        val openIssues: Int,


        @SerializedName("updated_at")
        val updated: String,

        @Embedded
        val owner: RepoOwner

) : Parcelable {

}