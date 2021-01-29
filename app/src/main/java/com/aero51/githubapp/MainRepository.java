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
import static com.aero51.githubapp.utils.Constants.FORKS_SORT_FLAG;
import static com.aero51.githubapp.utils.Constants.LOG;
import static com.aero51.githubapp.utils.Constants.STARS_SORT_FLAG;
import static com.aero51.githubapp.utils.Constants.UPDATED_SORT_FLAG;

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
    public RepoSearchResult search(String query, Integer sortFlag) {
        Log.d(LOG, "search: New query: " + query);

        DataSource.Factory<Integer, Repo> repos;
        // Get data source factory from the local cache

        switch (sortFlag) {
            case STARS_SORT_FLAG:
                repos = localCache.reposByStars(query);
                break;
            case FORKS_SORT_FLAG:
                repos = localCache.reposByForks(query);
                break;

            case UPDATED_SORT_FLAG:
                repos = localCache.reposByUpdated(query);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + sortFlag);
        }


        // Construct the boundary callback
        RepoBoundaryCallback boundaryCallback = new RepoBoundaryCallback(query, githubApi, localCache);
        LiveData<String> networkErrors = boundaryCallback.getNetworkErrors();

        // Set the Page size for the Paged list
        PagedList.Config pagedConfig = new PagedList.Config.Builder()
                .setPageSize(DATABASE_PAGE_SIZE)
                .setPrefetchDistance(20)
                .build();

        // Get the Live Paged list
        LiveData<PagedList<Repo>> data = new LivePagedListBuilder<>(repos, pagedConfig)
                .setBoundaryCallback(boundaryCallback)
                .build();

        // Get the Search result with the network errors exposed by the boundary callback
        return new RepoSearchResult(data, networkErrors);
    }
}
