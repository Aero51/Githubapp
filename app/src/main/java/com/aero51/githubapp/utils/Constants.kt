package com.aero51.githubapp.utils

object Constants {

    const val DATABASE_NAME = "Github.db"
    const val LOG = "GithubApp"
    const val BASE_URL = "https://api.github.com/"
    const val NETWORK_PAGE_SIZE = 50
    const val DATABASE_PAGE_SIZE = 20
    const val DEFAULT_QUERY = "Android"
    const val LAST_SEARCH_QUERY = "last_search_query"
    const val LAST_SORT_FLAG = "last_sort_flag"
    const val STARS_SORT_FLAG = 0
    const val FORKS_SORT_FLAG = 1
    const val UPDATED_SORT_FLAG = 2
    const val DEFAULT_SORT_FLAG = STARS_SORT_FLAG
    const val GITHUB_FIRST_PAGE = STARS_SORT_FLAG
}

enum class SortOrder (val sort:String) { BY_STARS("stars"), BY_FORKS("forks"),BY_UPDATED("updated") }