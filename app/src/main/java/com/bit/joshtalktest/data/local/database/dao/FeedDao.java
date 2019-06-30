package com.bit.joshtalktest.data.local.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bit.joshtalktest.data.model.db.Post;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;

@Dao
public interface FeedDao {
    @Insert(onConflict =OnConflictStrategy.IGNORE)
    void insertFeedInDb(List<Post> list);

    @Query("SELECT * FROM Post where pageNumber =:pageNumber ORDER BY eventDate DESC ")
    Maybe<List<Post>> getSavedInvoices(int pageNumber);
}
