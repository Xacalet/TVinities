package com.alexbarcelo.tvinities.moviedb.model;

import java.util.List;

/**
 * Class that represents a TV Show item from The Movie Database API
 *
 * @author Alex Barceló
 * @date 13/03/2018
 */

public class TVShowSummary {

    private String originalName;
    private List<Long> genreIds;
    private String name;
    private Double popularity;
    private List<String> originCountry;
    private Double voteAverage;
    private String firstAirDate;
    private String backdropPath;
    private String originalLanguage;
    private Long id;
    private String overview;
    private String posterPath;

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}