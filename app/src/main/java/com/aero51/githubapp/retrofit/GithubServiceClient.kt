package com.aero51.githubapp.retrofit

import com.aero51.githubapp.model.Repo
import com.aero51.githubapp.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * REST Client that makes API Calls
 *
 * @author Nikola Srdoc
 */
@Module
@InstallIn(SingletonComponent::class)
object GithubServiceClient {
    //Constant used for Logs
    private val LOG_TAG = GithubServiceClient::class.java.simpleName

    /**
     * Creates the Retrofit Service for Github API
     *
     * @return The [GithubApi] instance
     */
    @Provides
    @Singleton
    fun create(): GithubApi {
        //Initializing HttpLoggingInterceptor to receive the HTTP event logs
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)

        //Building the HTTPClient with the logger
        val httpClient: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

        //Returning the Retrofit service for the BASE_URL
        return Retrofit.Builder()
                .baseUrl(BASE_URL) //Using the HTTPClient setup
                .client(httpClient) //GSON converter to convert the JSON elements to a POJO
                .addConverterFactory(GsonConverterFactory.create())
                .build() //Creating the service for the defined API Interface
                .create<GithubApi>(GithubApi::class.java)
    }





    /*
    /**
     * Method that invokes the [GithubApi.searchRepos] API for
     * retrieving the Repositories using the Search query executed.
     *
     * @param service      The [GithubApi] instance for executing the API
     * @param query        The Search Query to execute
     * @param page         The Page index to show
     * @param itemsPerPage The Number of items to be shown per page
     * @param apiCallback  The [ApiCallback] interface for receiving the events of this
     * API Call
     */
    @Singleton
    suspend fun searchRepos(service: GithubApi, query: String, page: Int, itemsPerPage: Int, apiCallback: ApiCallback) {
        Log.d(LOG_TAG, String.format("searchRepos: query: %s, page: %s, itemsPerPage: %s", query, page, itemsPerPage))

        //Framing the query to be searched only in the Name and description fields of the
        //Github repositories
        val apiQuery = query + "in:name,description"

        //Executing the API asynchronously
        service.searchRepos(apiQuery, page, itemsPerPage).enqueue(object : Callback<RepoSearchResponse> {
            //Called when the response is received
            override fun onResponse(call: Call<RepoSearchResponse>, response: Response<RepoSearchResponse>) {
                Log.d(LOG_TAG, "onResponse: Got response: $response")
                if (response.isSuccessful) {
                    //Retrieving the Repo Items when the response is successful
                    val items: List<Repo>
                    items = if (response.body() != null) {
                        response.body()!!.items
                    } else {
                        ArrayList()
                    }
                    //Pass the result to the callback
                    apiCallback.onSuccess(items)
                } else {
                    //When the response is unsuccessful
                    try {
                        //Pass the error to the callback
                        apiCallback.onError(if (response.errorBody() != null) response.errorBody()!!.string() else "Unknown error")
                    } catch (e: IOException) {
                        Log.e(LOG_TAG, "onResponse: Failed while reading errorBody: ", e)
                    }
                }
            }

            //Called on Failure of the request
            override fun onFailure(call: Call<RepoSearchResponse?>?, t: Throwable) {
                Log.d(LOG_TAG, "onFailure: Failed to get data")
                //Pass the error to the callback
                apiCallback.onError(if (t.message != null) t.message else "Unknown error")
            }
        })
    }

     */

    interface ApiCallback {
        /**
         * Callback invoked when the Search Repo API Call
         * completed successfully
         *
         * @param items The List of Repos retrieved for the Search done
         */
        fun onSuccess(items: List<Repo>?)

        /**
         * Callback invoked when the Search Repo API Call failed
         *
         * @param errorMessage The Error message captured for the API Call failed
         */
        fun onError(errorMessage: String?)
    }
}