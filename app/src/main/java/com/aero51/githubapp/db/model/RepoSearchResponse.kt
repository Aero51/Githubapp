package com.aero51.githubapp.db.model

import com.google.gson.annotations.SerializedName

data class RepoSearchResponse (
        @SerializedName("total_count")
        val total: Int = 0,
        @JvmField
        val items: List<Repo>,
        val nextPage: Int
        ){


}