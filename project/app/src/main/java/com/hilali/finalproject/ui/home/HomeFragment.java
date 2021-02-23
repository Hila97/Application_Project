package com.hilali.finalproject.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hilali.finalproject.MainActivity;
import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.Post;
import com.hilali.finalproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeFragment extends Fragment {
    PostListViewModel viewModel;
   // List<Post> allData=new LinkedList<>();
    //List<String> allNames=new LinkedList<String>();
    ListView list;
    MyAdapter adapter;
    TextView mail;
    SwipeRefreshLayout swipeRefresh;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity)getActivity()).setUserDetails();

        list=view.findViewById(R.id.mainlistfragment_listview);
        swipeRefresh=view.findViewById(R.id.PostList_swipe);

        viewModel=new ViewModelProvider(this).get(PostListViewModel.class);
        adapter=new MyAdapter();
        list.setAdapter(adapter);
        swipeRefresh.setOnRefreshListener(()->
        {
            swipeRefresh.setRefreshing(true);
            reloadData();
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String pid=viewModel.getList().getValue().get(i).getPid();
                Log.d("TAG","post id"+i);
                HomeFragmentDirections.ActionNavHomeToPostDetailsFragment action = HomeFragmentDirections.actionNavHomeToPostDetailsFragment(pid);
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
        Model.instance.refreshAllPost(()->{
            swipeRefresh.setRefreshing(false);
        });
    }

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
        public Object getItem(int position) { return null; }

        @Override
        public long getItemId(int position) { return 0; }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null)
            {
                LayoutInflater inflater=getLayoutInflater();
                view=inflater.inflate(R.layout.main_list_row,null);

            }
            Post post=viewModel.getList().getValue().get(i);
            TextView titleTV=view.findViewById(R.id.mainlist_title);
            titleTV.setText(post.getTitle());
            ImageView postImg=view.findViewById(R.id.mainlist_image);
            if(post.getImageUrl()!=null&& post.getImageUrl()!="")
                Picasso.get().load(post.getImageUrl()).into(postImg);
            else
                postImg.setImageResource(R.drawable.home_icon);

            return view;
        }
    }
}