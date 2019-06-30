package com.bit.joshtalktest.ui.feed;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.joshtalktest.R;
import com.bit.joshtalktest.data.model.db.Post;
import com.bit.joshtalktest.ui.feed.FeedAdapter.FeedViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {


    private List<Post> postList = new ArrayList<>();
    private static final String EMPTY = "";


    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item_view, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedViewHolder viewHolder, final int position) {
        Post post = postList.get(position);
        viewHolder.event.setText(post.getEventName());
        viewHolder.share.setText(post.getShares() > 0 ? post.getShares().toString() : EMPTY);
        viewHolder.like.setText(post.getLikes() > 0 ? post.getLikes().toString() : EMPTY);
        viewHolder.viewEvent.setText(viewHolder.itemView.getContext().getString(R.string.view, post.getViews() > 0 ? post.getLikes().toString() : EMPTY));
        viewHolder.time.setText(post.getEventTime());

        Picasso.get().load(post.getThumbnailImage()).placeholder(R.drawable.image_placeholder).into(viewHolder.feedImageView);

    }

    public void addPost(List<Post> post) {
        int lastPos = postList.size();
        postList.addAll(post);
        //notifyItemRangeInserted(lastPos, post.size());
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (postList == null) {
            return 0;
        }
        return postList.size();
    }

    protected static class FeedViewHolder extends ViewHolder {
        private ImageView feedImageView;
        private TextView like;
        private TextView share;
        private TextView event;
        private TextView time;
        private TextView viewEvent;

        public FeedViewHolder(@NonNull final View itemView) {
            super(itemView);
            feedImageView = itemView.findViewById(R.id.image_view_event);
            like = itemView.findViewById(R.id.tv_event_likes);
            share = itemView.findViewById(R.id.tv_event_share);
            event = itemView.findViewById(R.id.tv_event_name);
            time = itemView.findViewById(R.id.tv_event_date);
            viewEvent = itemView.findViewById(R.id.tv_event_view);
        }
    }
}
