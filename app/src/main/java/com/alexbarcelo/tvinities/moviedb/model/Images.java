package com.alexbarcelo.tvinities.moviedb.model;

import java.util.List;

/**
 * TODO: Add header
 *
 * @author Alex Barceló
 * @date 16/03/2018
 */

public class Images {

    private String mBaseUrl;
    private String mSecureBaseUrl;
    private List<String> mBackdropSizes;
    private List<String> mLogoSizes;
    private List<String> mPosterSizes;
    private List<String> mProfileSizes;
    private List<String> mStillSizes;

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    public String getSecureBaseUrl() {
        return mSecureBaseUrl;
    }

    public void setSecureBaseUrl(String secureBaseUrl) {
        this.mSecureBaseUrl = secureBaseUrl;
    }

    public List<String> getBackdropSizes() {
        return mBackdropSizes;
    }

    public void setBackdropSizes(List<String> backdropSizes) {
        this.mBackdropSizes = backdropSizes;
    }

    public List<String> getLogoSizes() {
        return mLogoSizes;
    }

    public void setLogoSizes(List<String> logoSizes) {
        this.mLogoSizes = logoSizes;
    }

    public List<String> getPosterSizes() {
        return mPosterSizes;
    }

    public void setPosterSizes(List<String> posterSizes) {
        this.mPosterSizes = posterSizes;
    }

    public List<String> getProfileSizes() {
        return mProfileSizes;
    }

    public void setProfileSizes(List<String> profileSizes) {
        this.mProfileSizes = profileSizes;
    }

    public List<String> getStillSizes() {
        return mStillSizes;
    }

    public void setStillSizes(List<String> stillSizes) {
        this.mStillSizes = stillSizes;
    }

}