package com.bit.joshtalktest.data.service;

import com.bit.joshtalktest.data.model.api.FeedResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface JoshService {

    @GET(".")
    Single<FeedResponse> getFeed();

}
