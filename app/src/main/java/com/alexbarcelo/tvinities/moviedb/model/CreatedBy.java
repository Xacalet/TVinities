package com.alexbarcelo.tvinities.moviedb.model;

public class CreatedBy {

    private long id;
    private String name;
    private long gender;
    private String profilePath;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getGender() {
        return gender;
    }

    public void setGender(long gender) {
        this.gender = gender;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

}
