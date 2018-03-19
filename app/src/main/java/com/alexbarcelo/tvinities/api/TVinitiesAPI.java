package com.alexbarcelo.tvinities.api;

import com.alexbarcelo.tvinities.moviedb.model.PaginatedList;
import com.alexbarcelo.tvinities.moviedb.model.TVShowDetail;
import com.alexbarcelo.tvinities.moviedb.model.TVShowSummary;
import com.alexbarcelo.tvinities.moviedb.MovieDBService;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * This class represents a simplified API for accessing The Movie Database service, so consumers
 * do not need to include the API key or the language for every call to the REST service.
 *
 * @author Alex Barceló
 */

public class TVinitiesAPI {

    public static final String MOVIE_DATABASE_API_KEY = "35ec220e24d0c24bfa50fedacbbabdaf";
    public static final String BACKDROP_IMAGES_BASE_URL = "https://image.tmdb.org/t/p/w500";
    public static final String POSTER_IMAGES_BASE_URL = "https://image.tmdb.org/t/p/w200";

    private MovieDBService mService;
    private String mLanguage = "en-US";


    public TVinitiesAPI(Retrofit retrofit) {
        mService = retrofit.create(MovieDBService.class);
    }

    public Call<PaginatedList<TVShowSummary>> getPopularTVShows(int page) {
        return mService.getPopularTVShows(MOVIE_DATABASE_API_KEY, this.mLanguage, page);
    }

    public Call<TVShowDetail> getTVShowDetail(long id) {
        return mService.getTVShowDetail(id, MOVIE_DATABASE_API_KEY, this.mLanguage);
    }

    public Call<PaginatedList<TVShowSummary>> getSimilarShows(long id, int page) {
        return mService.getSimilarShows(id, MOVIE_DATABASE_API_KEY, this.mLanguage, page);
    }
}
