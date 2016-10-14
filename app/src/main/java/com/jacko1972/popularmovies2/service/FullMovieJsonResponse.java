package com.jacko1972.popularmovies2.service;

import com.google.gson.annotations.SerializedName;
import com.jacko1972.popularmovies2.model.MovieInfo;

import java.util.List;

public class FullMovieJsonResponse {

    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<MovieInfo> results;
    @SerializedName("total_results")
    private int total_results;
    @SerializedName("total_pages")
    private int total_pages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieInfo> getResults() {
        return results;
    }

    public void setResults(List<MovieInfo> results) {

        this.results = results;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int totalPages) {
        this.total_pages = totalPages;
    }
}

