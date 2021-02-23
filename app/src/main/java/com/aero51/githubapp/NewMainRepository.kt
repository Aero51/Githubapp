package com.aero51.githubapp

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.aero51.githubapp.data.GithubPagingSource
import com.aero51.githubapp.retrofit.GithubApi
import javax.inject.Inject

class NewMainRepository @Inject constructor(private val githubApi: GithubApi) {

    fun getSearchResults(query: String) =
            Pager(
                    config = PagingConfig(
                            pageSize = 20,
                            maxSize = 100,
                            enablePlaceholders = false
                    ),
                    pagingSourceFactory = {GithubPagingSource(githubApi,query)}
            ).liveData
}