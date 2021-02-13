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

import java.util.Map;


public class MainViewModel extends AndroidViewModel {
    private MainRepository mainRepository;


    public MainViewModel(@NonNull Application application) {
        super(application);
        AppExecutors executors = new AppExecutors();
        mainRepository =new MainRepository(executors,application);
    }

    private MutableLiveData<Map<Integer,String>> queryLiveData = new MutableLiveData<>();
    //Applying transformation to get RepoSearchResult for the given Search Query
    private LiveData<RepoSearchResult> repoResult = Transformations.map(queryLiveData, new Function<Map<Integer, String>, RepoSearchResult>() {
                @Override
                public RepoSearchResult apply(Map<Integer, String> input) {
                    //String query =input.get(STARS_SORT_FLAG);
                    String query="";
                    Integer sortFlag = null;
                    //return mainRepository.search(inputQuery,sortFlag);
                    for (Map.Entry<Integer, String> me : input.entrySet()) {
                        query=me.getValue();
                        sortFlag=me.getKey();

                    }
                    return mainRepository.search(query,sortFlag);
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
    void searchRepo(Map<Integer,String>queryMap) {
        queryLiveData.postValue(queryMap);
    }

    /**
     * Get the last query value.
     */
    @Nullable
    Map<Integer,String> lastQueryValue() {
        return queryLiveData.getValue();
    }

}
