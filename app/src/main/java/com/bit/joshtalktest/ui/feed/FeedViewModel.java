package com.bit.joshtalktest.ui.feed;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.bit.joshtalktest.BuildConfig;
import com.bit.joshtalktest.data.local.DatabaseHelper;
import com.bit.joshtalktest.data.model.api.FeedResponse;
import com.bit.joshtalktest.data.model.db.Post;
import com.bit.joshtalktest.data.service.JoshService;
import com.bit.joshtalktest.data.service.NetworkClient;
import com.bit.joshtalktest.utils.Constants;
import com.bit.joshtalktest.utils.NetworkUtils;

import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FeedViewModel extends AndroidViewModel {

    private Integer pageNumber = 1;
    private MutableLiveData<List<Post>> postMutableLiveData = new MutableLiveData();
    private MutableLiveData<List<Post>> localDbFeedMutableLiveData = new MutableLiveData();
    private MutableLiveData<Boolean> networkStateMutableLiveData = new MutableLiveData();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private JoshService joshService;
    private DatabaseHelper databaseHelper;
    private Boolean offline = false;


    public FeedViewModel(@NonNull final Application application) {
        super(application);
        joshService = new NetworkClient().getRetrofitClientInstance(BuildConfig.HOST_URL).create(JoshService.class);
        databaseHelper = new DatabaseHelper();
    }


    public final BroadcastReceiver mAppBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    if (NetworkUtils.isNetworkAvailable(context)) {
                        offline = false;
                        networkStateMutableLiveData.postValue(true);
                    } else {
                        networkStateMutableLiveData.postValue(false);
                    }
                }
            }
        }
    };


    public void getFeed() {


        if (NetworkUtils.isNetworkAvailable(getApplication())) {
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
        } else {
            networkStateMutableLiveData.postValue(false);
            if (offline) {
                getFeedOffline();
            }

        }
    }


    public void getFeedOffline() {

        databaseHelper.getPostFromDb(pageNumber).subscribeOn(Schedulers.io()).subscribe(new MaybeObserver<List<Post>>() {
            @Override
            public void onSubscribe(final Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(final List<Post> posts) {
                localDbFeedMutableLiveData.postValue(posts);
                pageNumber++;
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

    public MutableLiveData<Boolean> getNetworkStateMutableLiveData() {
        return networkStateMutableLiveData;
    }

    public MutableLiveData<List<Post>> getLocalDbFeedMutableLiveData() {
        return localDbFeedMutableLiveData;
    }

    public void setOffline(final Boolean offline) {
        this.offline = offline;
    }

    public Boolean isOffline() {
        return offline;
    }
}
