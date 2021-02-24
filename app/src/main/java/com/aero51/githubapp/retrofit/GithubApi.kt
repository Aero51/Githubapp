package com.aero51.githubapp.retrofit

import retrofit2.http.GET
import com.aero51.githubapp.model.RepoSearchResponse
import retrofit2.http.Query

interface GithubApi {
    @GET("search/repositories?sort=stars")
    suspend fun searchRepos(@Query("q") query: String,
                    @Query("page") page: Int,
                    @Query("per_page") itemsPerPage: Int): RepoSearchResponse
}