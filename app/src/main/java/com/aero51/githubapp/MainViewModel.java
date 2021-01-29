package com.aero51.githubapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;

import com.aero51.githubapp.db.model.Repo;
import com.aero51.githubapp.utils.AppExecutors;

public class MainViewModel extends AndroidViewModel {
    private MainRepository mainRepository;


    public MainViewModel(@NonNull Application application) {
        super(application);
        AppExecutors executors = new AppExecutors();
        mainRepository =new MainRepository(executors,application);
    }

    private MutableLiveData<String> queryLiveData = new MutableLiveData<>();
    //Applying transformation to get RepoSearchResult for the given Search Query
    private LiveData<RepoSearchResult> repoResult = Transformations.map(queryLiveData,
            new Function<String, RepoSearchResult>() {
                @Override
                public RepoSearchResult apply(String inputQuery) {
                    return mainRepository.search(inputQuery);
                }
            }
    );
    //Applying transformation to get Live PagedList<Repo> from the RepoSearchResult
    private LiveData<PagedList<Repo>> repos = Transformations.switchMap(repoResult,
            new Function<RepoSearchResult, LiveData<PagedList<Repo>>>() {
                @Override
                public LiveData<PagedList<Repo>> apply(RepoSearchResult repoSearchResult) {
                    return repoSearchResult.getData();
                }
            }
    );
    //Applying transformation to get Live Network Errors from the RepoSearchResult
    private LiveData<String> networkErrors = Transformations.switchMap(repoResult,
            RepoSearchResult::getNetworkErrors
    );


    LiveData<PagedList<Repo>> getRepos() {
        return repos;
    }

    LiveData<String> getNetworkErrors() {
        return networkErrors;
    }

    /**
     * Search a repository based on a query string.
     */
    void searchRepo(String queryString) {
        queryLiveData.postValue(queryString);
    }

    /**
     * Get the last query value.
     */
    @Nullable
    String lastQueryValue() {
        return queryLiveData.getValue();
    }

}
