package com.aero51.githubapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aero51.githubapp.model.Repo
import com.aero51.githubapp.retrofit.GithubApi
import com.aero51.githubapp.utils.Constants.GITHUB_FIRST_PAGE
import retrofit2.HttpException
import java.io.IOException

class GithubPagingSource(
        private val githubApi: GithubApi,
        private val query: String
) : PagingSource<Int, Repo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val position = params.key ?: GITHUB_FIRST_PAGE
        return try {
            val response = githubApi.searchRepos(query, position, params.loadSize)
            val repos = response.items
            LoadResult.Page(
                    data = repos,
                    prevKey = if (position == GITHUB_FIRST_PAGE) null else position - 1,
                    nextKey = if (repos.isEmpty()) null else position + 1

            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }


override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
    TODO("Not yet implemented")
    return state.anchorPosition
}
}