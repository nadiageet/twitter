package com.nadia.twitter.services;

import java.util.List;

public class Page<T> {
    private Integer pageNumber;
    private Integer resultsPerPage;
    private Integer totalResults;
    private List<T> items;


    public Page(Integer pageNumber, Integer resultsPerPage, Integer totalResults, List<T> items) {
        this.pageNumber = pageNumber;
        this.resultsPerPage = resultsPerPage;
        this.totalResults = totalResults;
        this.items = items;
    }
}
