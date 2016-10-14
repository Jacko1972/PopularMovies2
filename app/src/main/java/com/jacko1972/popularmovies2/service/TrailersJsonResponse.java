package com.jacko1972.popularmovies2.service;

import com.google.gson.annotations.SerializedName;
import com.jacko1972.popularmovies2.model.TrailersInfo;

import java.util.List;

public class TrailersJsonResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<TrailersInfo> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TrailersInfo> getResults() {
        return results;
    }

    public void setResults(List<TrailersInfo> results) {
        this.results = results;
    }
}
