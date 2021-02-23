package com.hilali.finalproject.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.Post;

import java.util.List;

public class PostListViewModel extends ViewModel {
    private LiveData<List<Post>> postList;
    public PostListViewModel(){
        postList = Model.instance.getAllPosts();
    }
    LiveData<List<Post>> getList(){
        return postList;
    }
}
