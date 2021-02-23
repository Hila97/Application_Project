package com.hilali.finalproject.ui.userPostList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.hilali.finalproject.ui.home.PostListViewModel;
import com.hilali.finalproject.ui.userDetails.UserProfileFragmentDirections;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class UserPostList extends Fragment {
    UserPostListViewModel viewModel;
   // List<Post> myData = new LinkedList<>();
    ListView list;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_post_list, container, false);
        list = view.findViewById(R.id.mainlistfragment_listview);
        swipeRefresh=view.findViewById(R.id.PostList_swipe);
        viewModel=new ViewModelProvider(this).get(UserPostListViewModel.class);
        adapter = new MyAdapter();
        list.setAdapter(adapter);
        swipeRefresh.setOnRefreshListener(()->
        {
            swipeRefresh.setRefreshing(true);
            reloadData();
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Log.d("TAG","post id"+i);
                String pid=viewModel.getList().getValue().get(i).getPid();
                UserPostListDirections.ActionUserPostListToPostDetailsFragment action= UserPostListDirections.actionUserPostListToPostDetailsFragment(pid);
                Navigation.findNavController(view).navigate(action);
            }
        });
        viewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    private void reloadData() {
        final String uid=Model.instance.getUserID();
        Model.instance.refreshUserPosts(uid,()->{
            swipeRefresh.setRefreshing(false);
        });
    }

    @Override
        public void onResume() {
            super.onResume();
            reloadData();
        }


        class MyAdapter extends BaseAdapter {

            @Override
            public int getCount() {
                if (viewModel.getList().getValue() == null){
                    return 0;
                }
                return viewModel.getList().getValue().size();
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
                Post post = viewModel.getList().getValue().get(i);
                TextView titleTV = view.findViewById(R.id.mainlist_title);
                titleTV.setText(post.getTitle());
                ImageView imageOnPost = view.findViewById(R.id.mainlist_image);
                if(post.getImageUrl()!=null&& post.getImageUrl()!="")
                    Picasso.get().load(post.getImageUrl()).into(imageOnPost);
                else
                    imageOnPost.setImageResource(R.drawable.home_icon);
                return view;
            }
        }
    }
