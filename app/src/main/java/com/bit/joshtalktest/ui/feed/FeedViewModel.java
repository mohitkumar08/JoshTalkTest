package com.bit.joshtalktest.ui.feed;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.bit.joshtalktest.BuildConfig;
import com.bit.joshtalktest.data.local.DatabaseHelper;
import com.bit.joshtalktest.data.model.api.FeedResponse;
import com.bit.joshtalktest.data.model.db.Post;
import com.bit.joshtalktest.data.service.JoshService;
import com.bit.joshtalktest.data.service.NetworkClient;
import com.bit.joshtalktest.utils.Constants;

import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FeedViewModel extends AndroidViewModel {

    private Integer pageNumber = 1;
    private MutableLiveData<List<Post>> postMutableLiveData = new MutableLiveData();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private JoshService joshService;
    private DatabaseHelper databaseHelper;


    public FeedViewModel(@NonNull final Application application) {
        super(application);
        joshService = new NetworkClient().getRetrofitClientInstance(BuildConfig.HOST_URL).create(JoshService.class);
        databaseHelper = new DatabaseHelper();

    }

    public void getFeed() {
        String pageUrl = Constants.mapPage.get(pageNumber);
        if (pageUrl == null) {
            getPostMutableLiveData().postValue(null);
            return;
        }
        joshService.getFeed(pageUrl).subscribeOn(Schedulers.io()).subscribe(new SingleObserver<FeedResponse>() {
            @Override
            public void onSubscribe(final Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(final FeedResponse feedResponse) {
                getPostMutableLiveData().postValue(feedResponse.getPosts());
                databaseHelper.savePost(feedResponse.getPosts(), pageNumber);
                pageNumber++;

            }

            @Override
            public void onError(final Throwable e) {
                e.printStackTrace();
                getPostMutableLiveData().postValue(null);

            }
        });
    }

    public void getFeedOffline() {

        databaseHelper.getPostFromDb(pageNumber).subscribeOn(Schedulers.io()).subscribe(new MaybeObserver<List<Post>>() {
            @Override
            public void onSubscribe(final Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(final List<Post> posts) {
                getPostMutableLiveData().postValue(posts);
            }

            @Override
            public void onError(final Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    public MutableLiveData<List<Post>> getPostMutableLiveData() {
        return postMutableLiveData;
    }
}
