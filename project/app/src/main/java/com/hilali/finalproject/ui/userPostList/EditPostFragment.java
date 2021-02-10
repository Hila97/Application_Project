package com.hilali.finalproject.ui.userPostList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
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

import com.google.android.material.textfield.TextInputEditText;
import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.Post;
import com.hilali.finalproject.Model.PostCategory;
import com.hilali.finalproject.R;
import com.hilali.finalproject.ui.home.PostDetailsFragmentArgs;


public class EditPostFragment extends Fragment {
    EditText title_ET;
    EditText describe_ET;
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
        String pid= PostDetailsFragmentArgs.fromBundle(getArguments()).getPid();

        title_ET=view.findViewById(R.id.editPost_title);
        describe_ET =view.findViewById(R.id.editPost_descrip);
        addImg_btn=view.findViewById(R.id.editPost_imgBtn);
        save_btn=view.findViewById(R.id.editPost_save_btn);
        cancel_btn=view.findViewById(R.id.editPost_cancel_btn);
        categorySpinner_EP=view.findViewById(R.id.editPost_category);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner_EP.setAdapter(adapter);
        categorySpinner_EP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                postCategory=categoryPick(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("TAG","take the previous choise");
            }
        });

        Model.instance.getPostById(pid, new Model.GetPostByIDsListener() {
            @Override
            public void onComplete(Post post) {
                postNow=post;
                title_ET.setText(postNow.getTitle());
                describe_ET.setText(postNow.getDescription());
                postCategory=postNow.getCategory();
            }
        });


        //SAVE BUTTON
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyFields())
                    updatePost(view);
            }
        });


        //CANCEL BUTTON
       cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        return  view;

    }





    // isEmpty FUNC
    public boolean isETEmpty(EditText et) {
        if (et.getText().toString().equals("") || et.getText().toString().equals(" ")) {
            et.setError("required");
            return true;
        }
        return false;
    }

    //verifyFields FUNC
    private boolean verifyFields() {
        /*
        EditText title,description;
        title= title_ET;
        description=describe_ET;

         */

        if (!isETEmpty(title_ET) && !isETEmpty(describe_ET)) {
            return true;
        }
        return false;
    }

    //updatePost FUNC
    private void updatePost(View view) {
        postNow.setTitle(title_ET.getText().toString());
        postNow.setDescription(describe_ET.getText().toString());
        postNow.setCategory(postCategory);
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

    //categoryPick FUNC
    private PostCategory categoryPick(int position) {
        switch (position){
            case 0: return PostCategory.LIVING_ROOM;
            case 1: return  PostCategory.BEDROOM;
            case 2: return PostCategory.KITCHEN;
            case 3: return PostCategory.CHILDREN_ROOM;
            case 4: return PostCategory.BATHROOM;
            case 5: return PostCategory.GARDEN;
        }
        return PostCategory.OTHER;
    }
}