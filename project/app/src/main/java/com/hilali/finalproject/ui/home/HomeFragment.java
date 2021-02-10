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
import androidx.navigation.Navigation;

import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.Post;
import com.hilali.finalproject.Model.User;
import com.hilali.finalproject.R;

import java.util.LinkedList;
import java.util.List;

public class HomeFragment extends Fragment {
    List<Post> allData=new LinkedList<>();
    List<String> allNames=new LinkedList<String>();
    ListView list;
    MyAdapter adapter;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        list=view.findViewById(R.id.mainlistfragment_listview);
        adapter=new MyAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Log.d("TAG","post id"+i);
                HomeFragmentDirections.ActionNavHomeToPostDetailsFragment action = HomeFragmentDirections.actionNavHomeToPostDetailsFragment(allData.get(i).getPid());
                Navigation.findNavController(view).navigate(action);
            }
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        Model.instance.getAllPosts(new Model.GetAllPostsListener() {
            @Override
            public void onComplete(List<Post> data) {
                allData=data;
                adapter.notifyDataSetChanged();
            }
        });
    }
    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() { return allData.size(); }

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
            Post post=allData.get(i);
            TextView titleTV=view.findViewById(R.id.mainlist_title);
            titleTV.setText(post.getTitle());
            TextView usernameTV=view.findViewById(R.id.mainlist_username);
            usernameTV.setText("user");
            ImageView imageView=view.findViewById(R.id.mainlist_image);

            return view;
        }
    }
}