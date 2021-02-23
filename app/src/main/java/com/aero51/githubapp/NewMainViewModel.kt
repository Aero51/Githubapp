package com.aero51.githubapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.aero51.githubapp.utils.Constants.DEFAULT_QUERY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewMainViewModel @Inject constructor(private val repository: NewMainRepository) : ViewModel() {
    private val currentQuery = MutableLiveData(DEFAULT_QUERY)
    val repos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }
    fun searchRepos(query: String) {
        currentQuery.value = query
    }
}