package com.hilali.finalproject.ui.addPost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.hilali.finalproject.Model.PostCategory;
import com.hilali.finalproject.R;

public class AddDesignFragment extends Fragment {
    ImageButton addImg;
    EditText titleET;
    EditText descriptionET;
    Button addBtn;
    Spinner categorySpinner;
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_design, container, false);
        addImg=view.findViewById(R.id.addpost_imgBtn);
        titleET=view.findViewById(R.id.addpost_title);
        descriptionET=view.findViewById(R.id.addpost_text);
        categorySpinner=view.findViewById(R.id.addpost_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                postCategory=categoryPick(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                postCategory=PostCategory.OTHER;
            }
        });

        addBtn=view.findViewById(R.id.addpost_addBtn);

        return view;
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