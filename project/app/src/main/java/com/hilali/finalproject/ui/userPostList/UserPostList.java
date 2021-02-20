package com.hilali.finalproject.ui.userPostList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.Post;
import com.hilali.finalproject.R;
import com.hilali.finalproject.ui.home.HomeFragment;
import com.hilali.finalproject.ui.userDetails.UserProfileFragmentDirections;

import java.util.LinkedList;
import java.util.List;

public class UserPostList extends Fragment {
    List<Post> myData = new LinkedList<>();
    ListView list;
    MyAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_post_list, container, false);
        list = view.findViewById(R.id.mainlistfragment_listview);
        adapter = new MyAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Log.d("TAG","post id"+i);
                UserPostListDirections.ActionUserPostListToPostDetailsFragment action= UserPostListDirections.actionUserPostListToPostDetailsFragment(myData.get(i).getPid());
                Navigation.findNavController(view).navigate(action);

            }
        });
        return view;
    }

        @Override
        public void onResume() {
            super.onResume();
            final String uid=Model.instance.getUserID();
            Model.instance.getAllUserPosts(uid,new Model.GetAllUserPostsListener() {
                @Override
                public void onComplete(List<Post> data) {
                    myData=data;
                    adapter.notifyDataSetChanged();
                }
            });
        }




        class MyAdapter extends BaseAdapter {

            @Override
            public int getCount() {
                return myData.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    view = inflater.inflate(R.layout.main_list_row, null);

                }
                Post post = myData.get(i);
                TextView titleTV = view.findViewById(R.id.mainlist_title);
                titleTV.setText(post.getTitle());
                ImageView imageView = view.findViewById(R.id.mainlist_image);

                return view;
            }
        }
    }
