package com.hilali.finalproject.ui.userDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.User;
import com.hilali.finalproject.R;
import com.squareup.picasso.Picasso;

public class UserProfileFragment extends Fragment {
    TextView mailTV;
    TextView passwordTV;
    TextView nameTV;
    TextView phoneTV;
    Button editBtn;
    ImageButton logOutBtn;
    User userNow;
    ImageView userImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mailTV=view.findViewById(R.id.user_profile_mail);
        passwordTV=view.findViewById(R.id.user_profile_password);
        nameTV=view.findViewById(R.id.user_profile_name);
        phoneTV=view.findViewById(R.id.user_profile_phone);
        editBtn=view.findViewById(R.id.user_profile_edit_btn);
        logOutBtn=view.findViewById(R.id.user_profile_logoutBtn);
        userImage=view.findViewById(R.id.user_profile_image);
        final String uid=Model.instance.getUserID();
        Model.instance.getUserById(uid, new Model.GetUserByIDsListener() {
            @Override
            public void onComplete(User user) {
                userNow=user;
                mailTV.setText(userNow.getEmail());
                passwordTV.setText(userNow.getPassword());
                nameTV.setText(userNow.getName());
                phoneTV.setText(userNow.getPhone());
                if(userNow.getImageUrl()!=null &&userNow.getImageUrl()!="")
                {
                    Picasso.get().load(userNow.getImageUrl()).into(userImage);
                }
                else
                {
                    userImage.setImageResource(R.drawable.user_profile_picture);
                }
            }
        });
        editBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_user_profile_to_editProfileFragment));
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.instance.logOut();
                Navigation.findNavController(v).navigate(R.id.action_nav_user_profile_to_loginFragment);
            }
        });

        return view;
    }
}