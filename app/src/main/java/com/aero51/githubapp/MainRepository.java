package com.aero51.githubapp;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.aero51.githubapp.db.Database;
import com.aero51.githubapp.db.GithubLocalCache;
import com.aero51.githubapp.db.RepoDao;
import com.aero51.githubapp.db.model.Repo;
import com.aero51.githubapp.retrofit.GithubApi;
import com.aero51.githubapp.retrofit.GithubServiceClient;
import com.aero51.githubapp.utils.AppExecutors;

import static com.aero51.githubapp.utils.Constants.DATABASE_PAGE_SIZE;
import static com.aero51.githubapp.utils.Constants.LOG;

/**
 * Repository class that works with local and remote data sources.
 *
 * @author Nikola Srdoc
 */
public class MainRepository {

    private AppExecutors executors;
    private Application application;
    private RepoBoundaryCallback repoBoundaryCallback;

    private GithubLocalCache localCache;
    private GithubApi githubApi;

    public MainRepository(AppExecutors executors, Application application) {
        this.executors = executors;
        this.application = application;
        Database database = Database.getInstance(application);
        RepoDao repoDao = database.getRepoDao();
        localCache = new GithubLocalCache(repoDao, executors.networkIO());
        githubApi = GithubServiceClient.create();
        //repoBoundaryCallback = new RepoBoundaryCallback()
    }

    /**
     * Search repositories whose names match the query.
     */
    public RepoSearchResult search(String query) {
        Log.d(LOG, "search: New query: " + query);

        // Get data source factory from the local cache
        DataSource.Factory<Integer, Repo> reposByName = localCache.reposByName(query);

        // Construct the boundary callback
        RepoBoundaryCallback boundaryCallback = new RepoBoundaryCallback(query, githubApi, localCache);
        LiveData<String> networkErrors = boundaryCallback.getNetworkErrors();

        // Set the Page size for the Paged list
        PagedList.Config pagedConfig = new PagedList.Config.Builder()
                .setPageSize(DATABASE_PAGE_SIZE)
                .setPrefetchDistance(20)
                .build();

        // Get the Live Paged list
        LiveData<PagedList<Repo>> data = new LivePagedListBuilder<>(reposByName, pagedConfig)
                .setBoundaryCallback(boundaryCallback)
                .build();

        // Get the Search result with the network errors exposed by the boundary callback
        return new RepoSearchResult(data, networkErrors);
    }
}
