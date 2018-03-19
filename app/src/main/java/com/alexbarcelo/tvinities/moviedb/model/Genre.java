package com.alexbarcelo.tvinities.moviedb.model;

/**
 * Class that represents a genre from The Movie Database API
 *
 * @author Alex Barceló
 */

public class Genre {
    private long id;
    private String name;

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
}
