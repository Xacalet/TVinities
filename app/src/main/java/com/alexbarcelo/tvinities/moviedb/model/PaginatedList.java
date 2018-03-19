package com.alexbarcelo.tvinities.moviedb.model;

import java.util.List;

/**
 * Class that may represent data retrieved from The Movie Database API whose request returns a paginated list.
 *
 * @author Alex Barceló
 */

public class PaginatedList<T> {

    private int page;
    private int totalResults;
    private int totalPages;
    private List<T> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
