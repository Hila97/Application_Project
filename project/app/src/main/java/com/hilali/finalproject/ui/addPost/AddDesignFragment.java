package com.hilali.finalproject.ui.addPost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.hilali.finalproject.R;

public class AddDesignFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_design, container, false);
        final TextView textView = root.findViewById(R.id.text_add_design);
        textView.setText("This is add design fragment");

        return root;
    }
}