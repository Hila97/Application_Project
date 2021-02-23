package com.hilali.finalproject.ui.userPostList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.Post;

import java.util.List;

public class UserPostListViewModel extends ViewModel {
    private LiveData<List<Post>> postList;
    public UserPostListViewModel(){
        String uid=Model.instance.getUserID();
        postList = Model.instance.getAllUserPosts(uid);
    }
    LiveData<List<Post>> getList(){
        return postList;
    }
}
