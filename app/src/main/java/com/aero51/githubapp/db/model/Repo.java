package com.aero51.githubapp.db.model;

import androidx.annotation.Nullable;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "repos")
public class Repo {
    @PrimaryKey
    @SerializedName("id")
    public long id;

    @SerializedName("name")
    public String name;

    @SerializedName("full_name")
    public String fullName;

    @Nullable
    @SerializedName("description")
    public String description;

    @SerializedName("html_url")
    public String url;

    @SerializedName("stargazers_count")
    public int stars;

    @SerializedName("forks_count")
    public int forks;

    @Nullable
    @SerializedName("language")
    public String language;

    @SerializedName("watchers_count")
    public int watchers;

    @SerializedName("open_issues_count")
    public int openIssues;

    @Embedded
    public RepoOwner owner;




}