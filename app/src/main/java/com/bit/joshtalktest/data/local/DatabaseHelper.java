package com.bit.joshtalktest.data.local;

import android.content.Context;

import com.bit.joshtalktest.BaseApplication;
import com.bit.joshtalktest.data.local.database.AppDatabase;
import com.bit.joshtalktest.data.model.db.Post;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DatabaseHelper {
    private AppDatabase appDatabase;

    public DatabaseHelper() {
        this.appDatabase = BaseApplication.getAppDatabaseContext();
    }


    public void savePost(List<Post> postList, int pageNumber) {
        Observable.just(postList).subscribeOn(Schedulers.computation())
                .flatMap((Function<List<Post>, ObservableSource<Post>>) posts -> Observable.fromIterable(posts))
                .map(post -> {
            post.setPageNumber(pageNumber);
            return post;
        }).toList().subscribe(new SingleObserver<List<Post>>() {
            @Override
            public void onSubscribe(final Disposable d) {

            }

            @Override
            public void onSuccess(final List<Post> posts) {
                appDatabase.getFeedDao().insertFeedInDb(posts);
            }

            @Override
            public void onError(final Throwable e) {

            }
        });
    }

    public Maybe<List<Post>> getPostFromDb(int pageNumber) {
        return appDatabase.getFeedDao().getSavedInvoices(pageNumber);
    }


}
