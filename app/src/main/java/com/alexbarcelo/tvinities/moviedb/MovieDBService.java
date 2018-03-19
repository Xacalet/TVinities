package com.alexbarcelo.tvinities.moviedb;

import com.alexbarcelo.tvinities.moviedb.model.PaginatedList;
import com.alexbarcelo.tvinities.moviedb.model.TVShowDetail;
import com.alexbarcelo.tvinities.moviedb.model.TVShowSummary;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that provides a set of methods to access data from The Movie Database through its REST API
 *
 * @author Alex Barceló
 */

public interface MovieDBService {
    @GET("/3/tv/popular")
    Call<PaginatedList<TVShowSummary>> getPopularTVShows(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

    @GET("/3/tv/{tv_id}")
    Call<TVShowDetail> getTVShowDetail(@Path("tv_id") long id, @Query("api_key") String apiKey, @Query("language") String language);

    @GET("/3/tv/{tv_id}/similar")
    Call<PaginatedList<TVShowSummary>> getSimilarShows(@Path("tv_id") long id, @Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);
}
