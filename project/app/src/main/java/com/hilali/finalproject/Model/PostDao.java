package com.hilali.finalproject.Model;
//for ROOM
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostDao {
    @Query("select * from Post")
    LiveData<List<Post>> getAllPosts();

    @Query("select * from Post where Post.uid==:uid")
    LiveData<List<Post>> getAllUserPosts(String uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... Posts);

    @Delete
    void delete(Post post);

    @Query("select * from Post where Post.pid== :pid")
    Post getPostByID(String pid);

    @Update
    void updatePost(Post post);
}
