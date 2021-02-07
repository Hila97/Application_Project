package com.hilali.finalproject.ui.userPostList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hilali.finalproject.Model.Post;
import com.hilali.finalproject.R;

import java.util.LinkedList;
import java.util.List;

public class UserPostList extends Fragment {
    List<Post> data=new LinkedList<>();
    ListView list;
   // MyAdapter adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_post_list, container, false);
        return view;
    }
}