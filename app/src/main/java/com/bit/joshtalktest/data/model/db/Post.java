package com.bit.joshtalktest.data.model.db;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;

import com.bit.joshtalktest.data.local.database.AppDatabase.DateConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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

@Entity(tableName = "Post")
@TypeConverters(DateConverter.class)
public final class Post {

    @JsonProperty("id")
    private String id;
    @JsonProperty("thumbnail_image")
    private String thumbnailImage;
    @JsonProperty("event_name")
    private String eventName;
    @JsonProperty("event_date")
    private Integer eventDate;
    @JsonProperty("views")
    private Integer views;
    @JsonProperty("likes")
    private Integer likes;
    @JsonProperty("shares")
    private Integer shares;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
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


}