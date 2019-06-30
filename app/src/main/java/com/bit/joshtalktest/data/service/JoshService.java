package com.bit.joshtalktest.data.service;

import com.bit.joshtalktest.data.model.api.FeedResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface JoshService {

    @GET
    Single<FeedResponse> getFeed(@Url String pageUrl);

}
