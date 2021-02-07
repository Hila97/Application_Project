package com.hilali.finalproject.ui.userDetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.User;
import com.hilali.finalproject.R;

public class EditProfileFragment extends Fragment {
    TextView textViewEditTittle ;
    EditText EditProfile_NameEt;
    EditText EditProfile_EmailEt ;
    EditText EditProfile_PhoneEt;
    EditText EditProfile_PasswordEt ;
    Button EditProfile_saveBtn;
    Button EditProfile_cancelBtn;
    User userNow;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_profile, container, false);
        textViewEditTittle=view.findViewById(R.id.edit_profile_text);
        EditProfile_NameEt = view.findViewById(R.id.edit_profile_name_ET);
        EditProfile_EmailEt = view.findViewById(R.id.edit_profile_email_ET);
        EditProfile_PhoneEt = view.findViewById(R.id.edit_profile_phone_ET);
        EditProfile_PasswordEt = view.findViewById(R.id.edit_profile_password_ET);

        EditProfile_saveBtn = view.findViewById(R.id.edit_profile_save_button);
        EditProfile_cancelBtn = view.findViewById(R.id.edit_profile_cancel_button);


        final String uid=Model.instance.getUserID();
        Model.instance.getUserById(uid, new Model.GetUserByIDsListener() {
            @Override
            public void onComplete(User user) {
                userNow=user;
                // userNow.setId(user.getId());
                EditProfile_NameEt.setText(userNow.getName());
                EditProfile_EmailEt.setText(userNow.getEmail());
                EditProfile_PhoneEt.setText(userNow.getPhone());
                EditProfile_PasswordEt.setText(userNow.getPassword());

            }
        });


        EditProfile_saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userNow.setName(EditProfile_NameEt.getText().toString());
                userNow.setEmail(EditProfile_EmailEt.getText().toString());
                userNow.setPhone(EditProfile_PhoneEt.getText().toString());
                userNow.setPassword(EditProfile_PasswordEt.getText().toString());
                Model.instance.UpdateUser(userNow, new Model.updateUserListener() {
                    @Override
                    public void onComplete(Boolean success) {
                        if(success)
                            Navigation.findNavController(view).popBackStack();
                        else
                            Toast.makeText(view.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        EditProfile_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();

            }
        });


        return view;
    }
}