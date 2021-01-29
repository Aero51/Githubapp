package com.aero51.githubapp.utils;

public class Constants {


    public static final String DATABASE_NAME = "Github.db";
    public static final String LOG = "GithubApp";
    public static final String BASE_URL = "https://api.github.com/";
    public static final int NETWORK_PAGE_SIZE = 50;
    public static final int DATABASE_PAGE_SIZE = 20;
    public static final String DEFAULT_QUERY = "Android";
    public static final String LAST_SEARCH_QUERY = "last_search_query";
    public static final String LAST_SORT_FLAG = "last_sort_flag";
    public static final int STARS_SORT_FLAG = 0;
    public static final int FORKS_SORT_FLAG = 1;
    public static final int UPDATED_SORT_FLAG = 2;
    public static final int DEFAULT_SORT_FLAG = STARS_SORT_FLAG;
}
