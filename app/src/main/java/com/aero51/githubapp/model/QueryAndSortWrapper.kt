package com.aero51.githubapp.model

import com.aero51.githubapp.utils.SortOrder

data class QueryAndSortWrapper(
        var query: String,
        var sortFlag: SortOrder
)