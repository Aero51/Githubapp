package com.aero51.githubapp.db.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RepoSearchResponse {


        @SerializedName("total_count")
        public int total;
        public List<Repo> items;
        public int nextPage;

}
