package com.bit.joshtalktest.data.service;

import com.bit.joshtalktest.BuildConfig;
import com.bit.joshtalktest.utils.JacksonParserUtil;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class NetworkClient {

    private static final int TIME_OUT = 30;

    public OkHttpClient.Builder getOkHttpBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor bodyLogging = new HttpLoggingInterceptor();
            bodyLogging.setLevel(Level.BODY);
            builder.addInterceptor(bodyLogging);
            builder.addNetworkInterceptor(new StethoInterceptor());
        }
        return builder;
    }

    public Retrofit getRetrofitClientInstance(String url) {
        OkHttpClient.Builder builder = getOkHttpBuilder();
        OkHttpClient okHttpClientInstance = builder.build();
        Retrofit retrofitInstance = new Retrofit.Builder().baseUrl(url).client(okHttpClientInstance)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(JacksonConverterFactory.create(JacksonParserUtil.getObjectMapper())).build();
        return retrofitInstance;
    }

}
