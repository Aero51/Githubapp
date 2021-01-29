package com.aero51.githubapp.retrofit;

import com.aero51.githubapp.db.model.RepoSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubApi {

    @GET("search/repositories?sort=stars")
    Call<RepoSearchResponse> searchRepos(@Query("q") String query,
                                         @Query("page") int page,
                                         @Query("per_page") int itemsPerPage);

}
