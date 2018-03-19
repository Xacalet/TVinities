
package com.alexbarcelo.tvinities.moviedb.model;

public class Season {

    private String airDate;
    private long episodeCount;
    private long id;
    private String posterPath;
    private long seasonNumber;

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public long getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(long episodeCount) {
        this.episodeCount = episodeCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public long getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(long seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

}
