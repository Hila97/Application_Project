package com.hilali.finalproject.ui.userDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.User;
import com.hilali.finalproject.R;

public class UserProfileFragment extends Fragment {
    TextView mailTV;
    TextView passwordTV;
    TextView nameTV;
    TextView phoneTV;
    Button editBtn;
    User userNow;
   // String uid;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mailTV=view.findViewById(R.id.user_profile_mail);
        passwordTV=view.findViewById(R.id.user_profile_password);
        nameTV=view.findViewById(R.id.user_profile_name);
        phoneTV=view.findViewById(R.id.user_profile_phone);
        editBtn=view.findViewById(R.id.user_profile_edit_btn);
        final String uid=Model.instance.getUserID();
        TextView textView=view.findViewById(R.id.text_user_post_list);
        textView.setText(uid);

        Model.instance.getUserById(uid, new Model.GetUserByIDsListener() {
            @Override
            public void onComplete(User user) {
                userNow=user;
                mailTV.setText(userNow.getEmail());
                passwordTV.setText(userNow.getPassword());
                nameTV.setText(userNow.getName());
                phoneTV.setText(userNow.getPhone());
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_user_profile_to_editProfileFragment);
            }
        });


        return view;
    }
}