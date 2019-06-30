package com.bit.joshtalktest.ui.feed;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProvider.AndroidViewModelFactory;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bit.joshtalktest.R;
import com.bit.joshtalktest.data.model.db.Post;
import com.bit.joshtalktest.data.model.db.Post.DateCompare;
import com.bit.joshtalktest.data.model.db.Post.LikesCompare;
import com.bit.joshtalktest.data.model.db.Post.ShareCompare;
import com.bit.joshtalktest.data.model.db.Post.ViewsCompare;
import com.bit.joshtalktest.utils.AppDialogUtil;
import com.bit.joshtalktest.utils.Constants.SortSelection;
import com.bit.joshtalktest.utils.NetworkUtils;
import com.bit.joshtalktest.utils.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private FeedViewModel feedViewModel;
    private RecyclerView feedRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Boolean isLoading = false;
    private FeedAdapter feedAdapter;
    private Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        feedViewModel = new ViewModelProvider(this, new AndroidViewModelFactory(getApplication())).get(FeedViewModel.class);
        initView();
        addObserver();

        if (NetworkUtils.isNetworkAvailable(getApplicationContext())) {
            fetchFeed();
        } else {
            youAreNowOffline();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        getApplicationContext().registerReceiver(feedViewModel.mAppBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getApplicationContext().unregisterReceiver(feedViewModel.mAppBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_sort) {
            showSortDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void addObserver() {
        feedViewModel.getPostMutableLiveData().observe(this, posts -> {

            if (posts == null || posts.isEmpty()) {
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
            } else {
                feedAdapter.addPost(posts);
            }
            hideProgressView();

        });
        feedViewModel.getLocalDbFeedMutableLiveData().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable final List<Post> posts) {
                if (feedAdapter.getItemCount() == 0) {
                    if (posts == null || posts.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
                    } else {
                        feedAdapter.update(posts);
                    }
                } else {
                    if (posts.size() > 0) {
                        feedAdapter.addPost(posts);
                    }
                }

                hideProgressView();

            }
        });

        feedViewModel.getNetworkStateMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean aBoolean) {
                if (aBoolean) {
                    youAreNowOnline();
                } else {
                    youAreNowOffline();
                }
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
                    if (layoutManager != null && layoutManager.findLastVisibleItemPosition() == feedAdapter.getItemCount() - 1) {
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

    private void showSortDialog() {
        AppDialogUtil.getAppDialogUtilInstance().showCallingPopUp(this, choice -> {
            SortSelection sortSelection = SortSelection.valueOf(choice.toUpperCase());

            if (feedAdapter.getItemCount() <= 0) {
                return;
            }

            isLoading = true;
            List<Post> list = new ArrayList<>(feedAdapter.getPostList());

            switch (sortSelection) {
                case SHARE:
                    Collections.sort(list, new ShareCompare());
                    break;
                case VIEW:
                    Collections.sort(list, new ViewsCompare());
                    break;
                case DATE:
                    Collections.sort(list, new DateCompare());
                    break;
                case LIKE:
                    Collections.sort(list, new LikesCompare());
                    break;
            }
            feedAdapter.update(list);
            new Handler().postDelayed(() -> isLoading = false, 500);


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
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
            isLoading = false;
        }, 800);
    }

    private void youAreNowOffline() {
        if (feedViewModel.isOffline()) {
            return;
        }
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);
        snackbar = Snackbar
                .make(coordinatorLayout, "Go To offline", Snackbar.LENGTH_INDEFINITE)
                .setAction("Yes", view -> {
                    feedViewModel.setOffline(true);
                    feedViewModel.getFeedOffline();
                    snackbar.dismiss();
                });

        snackbar.show();
    }

    private void youAreNowOnline() {
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);
        snackbar = Snackbar
                .make(coordinatorLayout, "You are online", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
