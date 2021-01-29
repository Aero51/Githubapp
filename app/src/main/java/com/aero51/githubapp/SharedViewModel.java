package com.aero51.githubapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


/**
 * Used to communicate between activity and fragment
 *
 * @author Nikola Srdoc
 */
public class SharedViewModel extends ViewModel {


    private MutableLiveData<String> searchQuery;
    private MutableLiveData<Integer> sortFlag;

    public SharedViewModel() {
        searchQuery = new MutableLiveData<>();
        sortFlag=new MutableLiveData<>();
    }

    public void sendSearchQuery(String query){
        searchQuery.postValue(query);
    }

    public MutableLiveData<String> getSearchQuery() {
        return searchQuery;
    }

    public void sendSortFlag(Integer flag){
        sortFlag.postValue(flag);
    }

    public MutableLiveData<Integer> getSortFlag() {
        return sortFlag;
    }
}
