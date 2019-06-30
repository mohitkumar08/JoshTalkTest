package com.bit.joshtalktest.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.bit.joshtalktest.data.local.database.AppDatabase.DateConverter;
import com.bit.joshtalktest.utils.Constants;
import com.bit.joshtalktest.utils.Utility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "thumbnail_image",
        "event_name",
        "event_date",
        "views",
        "likes",
        "shares"
})

@Entity(tableName = "Post", indices = {@Index(value = {"postId", "thumbnailImage", "eventDate"}, unique = true)})
@TypeConverters(DateConverter.class)
public final class Post {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    private long id;


    @ColumnInfo
    @JsonProperty("id")
    private String postId;

    @ColumnInfo
    private int pageNumber;

    @ColumnInfo
    @JsonProperty("thumbnail_image")
    private String thumbnailImage;

    @ColumnInfo
    @JsonProperty("event_name")
    private String eventName;

    @ColumnInfo
    @JsonProperty("event_date")
    private Integer eventDate;

    @ColumnInfo
    @JsonProperty("views")
    private Integer views = 0;

    @ColumnInfo
    @JsonProperty("likes")
    private Integer likes = 0;

    @ColumnInfo
    @JsonProperty("shares")
    private Integer shares = 0;

    @ColumnInfo
    private String eventTime;


    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @JsonProperty("id")
    public String getPostId() {
        return postId;
    }

    @JsonProperty("id")
    public void setPostId(final String postId) {
        this.postId = postId;
    }

    @JsonProperty("thumbnail_image")
    public String getThumbnailImage() {
        return thumbnailImage;
    }

    @JsonProperty("thumbnail_image")
    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    @JsonProperty("event_name")
    public String getEventName() {
        return eventName;
    }

    @JsonProperty("event_name")
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @JsonProperty("event_date")
    public Integer getEventDate() {
        return eventDate;
    }

    @JsonProperty("event_date")
    public void setEventDate(Integer eventDate) {
        this.eventDate = eventDate;
        setEventTime(eventDate);
    }

    @JsonProperty("views")
    public Integer getViews() {
        return views;
    }

    @JsonProperty("views")
    public void setViews(Integer views) {
        this.views = views;
    }

    @JsonProperty("likes")
    public Integer getLikes() {
        return likes;
    }

    @JsonProperty("likes")
    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    @JsonProperty("shares")
    public Integer getShares() {
        return shares;
    }

    @JsonProperty("shares")
    public void setShares(Integer shares) {
        this.shares = shares;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(final String eventTime) {
        this.eventTime = eventTime;
    }

    public void setEventTime(final Integer eventTime) {
        this.eventTime = Utility.convertStringToDate(Constants.FEED_TIME_FORMAT, eventTime);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (! (o instanceof Post)) return false;

        final Post post = (Post) o;

        if (getId() != post.getId()) return false;
        if (getThumbnailImage() != null ? ! getThumbnailImage().equals(post.getThumbnailImage()) : post.getThumbnailImage() != null)
            return false;
        if (getEventName() != null ? ! getEventName().equals(post.getEventName()) : post.getEventName() != null)
            return false;
        if (getEventDate() != null ? ! getEventDate().equals(post.getEventDate()) : post.getEventDate() != null)
            return false;
        if (getViews() != null ? ! getViews().equals(post.getViews()) : post.getViews() != null)
            return false;
        if (getLikes() != null ? ! getLikes().equals(post.getLikes()) : post.getLikes() != null)
            return false;
        return getShares() != null ? getShares().equals(post.getShares()) : post.getShares() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getThumbnailImage() != null ? getThumbnailImage().hashCode() : 0);
        result = 31 * result + (getEventName() != null ? getEventName().hashCode() : 0);
        result = 31 * result + (getEventDate() != null ? getEventDate().hashCode() : 0);
        result = 31 * result + (getViews() != null ? getViews().hashCode() : 0);
        result = 31 * result + (getLikes() != null ? getLikes().hashCode() : 0);
        result = 31 * result + (getShares() != null ? getShares().hashCode() : 0);
        return result;
    }

    static class DateCompare implements Comparator<Post> {
        DateFormat f = new SimpleDateFormat(Constants.FEED_TIME_FORMAT);

        @Override
        public int compare(final Post o1, final Post o2) {
            try {
                return f.parse(o1.getEventTime()).compareTo(f.parse(o2.getEventTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    static class LikesCompare implements Comparator<Post> {
        @Override
        public int compare(final Post o1, final Post o2) {
            return o1.getLikes().compareTo(o2.getLikes());
        }
    }

    static class ViewsCompare implements Comparator<Post> {

        @Override
        public int compare(final Post o1, final Post o2) {
            return o1.getViews().compareTo(o2.getViews());
        }
    }

    static class ShareCompare implements Comparator<Post> {

        @Override
        public int compare(final Post o1, final Post o2) {
            return o1.getShares().compareTo(o2.getShares());
        }
    }
}