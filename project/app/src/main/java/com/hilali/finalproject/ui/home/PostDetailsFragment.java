package com.hilali.finalproject.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.Post;
import com.hilali.finalproject.R;
import com.squareup.picasso.Picasso;


public class PostDetailsFragment extends Fragment {
    TextView userPost;
    TextView titleOfPost;
    TextView describeOfPost;
    TextView postCategory;
    ImageButton editPost_btn ;
    ImageView imageOnPost;
    Post post;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_post_details, container, false);
        String pid=PostDetailsFragmentArgs.fromBundle(getArguments()).getPid();

        userPost=view.findViewById(R.id.postDetails_username_text);
        titleOfPost=view.findViewById(R.id.postDetails_title_text);
        describeOfPost =view.findViewById(R.id.postDetails_Description_text);
        postCategory=view.findViewById(R.id.post_details_category);
        imageOnPost=view.findViewById(R.id.postdetalis_image);
        editPost_btn=view.findViewById(R.id.postDetails_editpost_btn);
        editPost_btn.setVisibility(View.GONE);

        Model.instance.getPostById(pid, new Model.GetPostByIDsListener() {
            @Override
            public void onComplete(Post post1) {
                post=post1;
                setOwnerName(post.getUid());
                titleOfPost.setText(post.getTitle());
                describeOfPost.setText(post.getDescription());
                postCategory.setText(post.getCategory().toString());
                if(post.getImageUrl()!=null&& post.getImageUrl()!="")
                    Picasso.get().load(post.getImageUrl()).into(imageOnPost);
                else
                    imageOnPost.setImageResource(R.drawable.home_icon);
                final String uid=Model.instance.getUserID();
                String uid2=post.getUid();
                if(uid.equals(uid2))
                {
                    editPost_btn.setVisibility(View.VISIBLE);
                }
            }
        });

        editPost_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostDetailsFragmentDirections.ActionPostDetailsFragmentToEditPostFragment action = PostDetailsFragmentDirections.actionPostDetailsFragmentToEditPostFragment(pid);
                Navigation.findNavController(view).navigate(action);
            }
        });

        return view;
    }

    private void setOwnerName(String uid) {
        Model.instance.getUserName(uid, new Model.getUserNameListener() {
            @Override
            public void onComplete(String name) {
                userPost.setText(name);
            }
        });
    }
}