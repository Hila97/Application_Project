package com.hilali.finalproject.ui.userPostList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.Post;
import com.hilali.finalproject.Model.PostCategory;
import com.hilali.finalproject.Model.User;
import com.hilali.finalproject.R;
import com.hilali.finalproject.ui.home.PostDetailsFragmentArgs;


public class EditPostFragment extends Fragment {
    EditText title_ET;
    EditText descrip_ET;
    ImageButton addImg_btn ;
    Button save_btn;
    Button cancel_btn;
    Post postNow;
    Spinner categorySpinner_EP;
    PostCategory postCategory;
    static String[] categories = new String[]{PostCategory.LIVING_ROOM.toString(),
            PostCategory.BEDROOM.toString(),
            PostCategory.KITCHEN.toString(),
            PostCategory.CHILDREN_ROOM.toString(),
            PostCategory.BATHROOM.toString(),
            PostCategory.GARDEN.toString(),
            PostCategory.OTHER.toString()
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);
        title_ET=view.findViewById(R.id.editPost_title);
        descrip_ET=view.findViewById(R.id.editPost_descrip);
        addImg_btn=view.findViewById(R.id.editPost_imgBtn);
        save_btn=view.findViewById(R.id.editPost_save_btn);
        cancel_btn=view.findViewById(R.id.editPost_cancel_btn);

        categorySpinner_EP=view.findViewById(R.id.editPost_category); ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner_EP.setAdapter(adapter);
        categorySpinner_EP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                postCategory=categoryPick(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                postCategory=PostCategory.OTHER;
            }
        });



        String pid= PostDetailsFragmentArgs.fromBundle(getArguments()).getPid();
        Model.instance.getPostById(pid, new Model.GetPostByIDsListener() {
            @Override
            public void onComplete(Post post) {
                postNow=post;
                title_ET.setText(postNow.getTitle());
                descrip_ET.setText(postNow.getDescription());

            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postNow.setTitle(title_ET.getText().toString());
                postNow.setDescription(descrip_ET.getText().toString());
                Model.instance.UpdatePost(postNow, new Model.updatePostListener() {
                    @Override
                    public void onComplete(Boolean success) {
                        if(success){
                            Toast.makeText(view.getContext(),"post updated",Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).popBackStack();
                        }
                        else
                            Toast.makeText(view.getContext(), "ERROR", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

       cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        return  view;

    }

    private PostCategory categoryPick(int position) {
        switch (position){
            case 0: return PostCategory.LIVING_ROOM;
            case 1: return  PostCategory.BEDROOM;
            case 3: return PostCategory.KITCHEN;
            case 4: return PostCategory.CHILDREN_ROOM;
            case 5: return PostCategory.BATHROOM;
            case 6: return PostCategory.GARDEN;
        }
        return PostCategory.OTHER;
    }
}