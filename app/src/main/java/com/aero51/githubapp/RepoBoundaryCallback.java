package com.aero51.githubapp;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.aero51.githubapp.db.GithubLocalCache;
import com.aero51.githubapp.db.model.Repo;
import com.aero51.githubapp.retrofit.GithubApi;
import com.aero51.githubapp.retrofit.GithubServiceClient;

import java.util.List;

import static com.aero51.githubapp.utils.Constants.NETWORK_PAGE_SIZE;

/**
 * PagedList.BoundaryCallback class to know when to trigger the Network request for more data
 *
 * @author Nikola Srdoc
 */
public class RepoBoundaryCallback extends PagedList.BoundaryCallback<Repo> implements GithubServiceClient.ApiCallback {
    //Constant used for logs
    private static final String LOG_TAG = RepoBoundaryCallback.class.getSimpleName();
    // Constant for the Number of items in a page to be requested from the Github API

    private String query;
    private GithubApi githubApi;
    private GithubLocalCache localCache;
    // Keep the last requested page. When the request is successful, increment the page number.
    private int lastRequestedPage = 1;
    // Avoid triggering multiple requests in the same time
    private boolean isRequestInProgress = false;
    // LiveData of network errors.
    private MutableLiveData<String> networkErrors = new MutableLiveData<>();

    RepoBoundaryCallback(String query, GithubApi githubApi, GithubLocalCache localCache) {
        this.query = query;
        this.githubApi = githubApi;
        this.localCache = localCache;
    }

    LiveData<String> getNetworkErrors() {
        return networkErrors;
    }

    /**
     * Method to request data from Github API for the given search query
     * and save the results.
     *
     * @param query The query to use for retrieving the repositories from API
     */
    private void requestAndSaveData(String query) {
        //Exiting if the request is in progress
        if (isRequestInProgress) return;

        //Set to true as we are starting the network request
        isRequestInProgress = true;

        //Calling the client API to retrieve the Repos for the given search query
        GithubServiceClient.searchRepos(githubApi, query, lastRequestedPage, NETWORK_PAGE_SIZE, this);
    }

    /**
     * Called when zero items are returned from an initial load of the PagedList's data source.
     */
    @Override
    public void onZeroItemsLoaded() {
        Log.d(LOG_TAG, "onZeroItemsLoaded: Started");
        requestAndSaveData(query);
    }

    /**
     * Called when the item at the end of the PagedList has been loaded, and access has
     * occurred within {@link PagedList.Config#prefetchDistance} of it.
     * <p>
     * No more data will be appended to the PagedList after this item.
     *
     * @param itemAtEnd The first item of PagedList
     */
    @Override
    public void onItemAtEndLoaded(@NonNull Repo itemAtEnd) {
        Log.d(LOG_TAG, "onItemAtEndLoaded: Started");
        requestAndSaveData(query);
    }

    /**
     * Callback invoked when the Search Repo API Call
     * completed successfully
     *
     * @param items The List of Repos retrieved for the Search done
     */
    @Override
    public void onSuccess(List<Repo> items) {
        //Inserting records in the database thread
        localCache.insert(items, new GithubLocalCache.InsertCallback() {
            @Override
            public void insertFinished() {
                //Updating the last requested page number when the request was successful
                //and the results were inserted successfully
                lastRequestedPage++;
                //Marking the request progress as completed
                isRequestInProgress = false;
            }
        });
    }

    /**
     * Callback invoked when the Search Repo API Call failed
     *
     * @param errorMessage The Error message captured for the API Call failed
     */
    @Override
    public void onError(String errorMessage) {
        //Update the Network error to be shown
        networkErrors.postValue(errorMessage);
        //Mark the request progress as completed
        isRequestInProgress = false;
    }
}