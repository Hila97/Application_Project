package com.hilali.finalproject.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.Post;
import com.hilali.finalproject.R;


public class PostDetailsFragment extends Fragment {
    TextView userPost;
    TextView titleOfPost;
    TextView descripOfPost;
    Button editPost_btn ;
    ImageView imageOnPost;
    Post post;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_post_details, container, false);
        String pid=PostDetailsFragmentArgs.fromBundle(getArguments()).getPid();
        Log.d("TAG","post id is "+pid);
        userPost=view.findViewById(R.id.postDetails_username_text);
        titleOfPost=view.findViewById(R.id.postDetails_title_text);
        descripOfPost=view.findViewById(R.id.postDetails_Description_text);
        editPost_btn=view.findViewById(R.id.postDetails_editpost_btn);
        editPost_btn.setEnabled(false);
        imageOnPost=view.findViewById(R.id.postdetalis_image);

        Model.instance.getPostById(pid, new Model.GetPostByIDsListener() {
            @Override
            public void onComplete(Post post1) {
                post=post1;
                userPost.setText(post.getUid());
                titleOfPost.setText(post.getTitle());
                descripOfPost.setText(post.getDescription());
                final String uid=Model.instance.getUserID();
                String uid2=post.getUid();
                if(uid.equals(uid2))
                {
                    editPost_btn.setEnabled(true);
                }
            }
        });


        return view;
    }
}