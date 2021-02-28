package com.aero51.githubapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.aero51.githubapp.model.QueryAndSortWrapper
import com.aero51.githubapp.utils.Constants.DEFAULT_QUERY
import com.aero51.githubapp.utils.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private val currentQuery= MutableLiveData(QueryAndSortWrapper(DEFAULT_QUERY, SortOrder.BY_STARS))

    val repos = currentQuery.switchMap { queryAndSortWrapper ->
        repository.getSearchResults(queryAndSortWrapper.query,queryAndSortWrapper.sortFlag.sort).cachedIn(viewModelScope)
    }

    fun searchRepos(query:String, orderBy:SortOrder) {
        var queryAndSortWrapper: QueryAndSortWrapper=(QueryAndSortWrapper(query, orderBy))
        currentQuery.value=queryAndSortWrapper

    }
}

