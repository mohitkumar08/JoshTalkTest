package com.bit.joshtalktest.ui.feed;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProvider.AndroidViewModelFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bit.joshtalktest.R;
import com.bit.joshtalktest.data.model.db.Post;
import com.bit.joshtalktest.utils.RecyclerViewItemDecoration;

import java.util.List;

public class FeedActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private FeedViewModel feedViewModel;
    private RecyclerView feedRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Boolean isLoading = false;
    private FeedAdapter feedAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        feedViewModel = new ViewModelProvider(this, new AndroidViewModelFactory(getApplication())).get(FeedViewModel.class);
        initView();
        addObserver();
        fetchFeed();

    }


    private void addObserver() {
        feedViewModel.getPostMutableLiveData().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable final List<Post> posts) {
                if (posts != null && posts.size() > 0) {
                    feedAdapter.addPost(posts);
                } else {

                }
                hideProgressView();

            }
        });

    }

    private void initView() {
        feedRecyclerView = findViewById(R.id.feed_recycle_view);
        feedRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(getApplication()));
        feedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        feedRecyclerView.setHasFixedSize(true);
        feedAdapter = new FeedAdapter();
        feedRecyclerView.setAdapter(feedAdapter);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        feedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (! isLoading) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == feedAdapter.getItemCount() - 1) {
                        showProgressView();
                        feedViewModel.getFeed();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void fetchFeed() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                feedViewModel.getFeed();
            }
        });
    }

    @Override
    public void onRefresh() {
        fetchFeed();
    }

    private void showProgressView() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void hideProgressView() {
        mSwipeRefreshLayout.setRefreshing(false);
        isLoading = false;

    }
}
