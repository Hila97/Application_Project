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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.User;
import com.hilali.finalproject.R;

public class EditProfileFragment extends Fragment {
    TextView textViewEditTittle;
    EditText EditProfile_NameEt;
    EditText EditProfile_PhoneEt;
    EditText EditProfile_PasswordEt;
    Button EditProfile_saveBtn;
    Button EditProfile_cancelBtn;
    ProgressBar progBar_editProfile;
    User userNow;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        textViewEditTittle = view.findViewById(R.id.edit_profile_text);
        EditProfile_NameEt = view.findViewById(R.id.edit_post_titlepost_ET);
        EditProfile_PhoneEt = view.findViewById(R.id.edit_post_descrip_ET);
        EditProfile_PasswordEt = view.findViewById(R.id.edit_profile_password_ET);
        EditProfile_saveBtn = view.findViewById(R.id.edit_post_save_button);
        EditProfile_cancelBtn = view.findViewById(R.id.edit_post_cancel_button);
        progBar_editProfile=view.findViewById(R.id.progBar_editProfile);
        progBar_editProfile.setVisibility(View.INVISIBLE);


        final String uid = Model.instance.getUserID();
        Model.instance.getUserById(uid, new Model.GetUserByIDsListener() {
            @Override
            public void onComplete(User user) {
                userNow = user;
                EditProfile_NameEt.setText(userNow.getName());
                EditProfile_PhoneEt.setText(userNow.getPhone());
                EditProfile_PasswordEt.setText(userNow.getPassword());

            }
        });
        EditProfile_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });


        EditProfile_saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //בדיקת קלט
                if(verifyFields())
                {
                    if(verifyPassword(view))
                        UpdateProfile(view);
                }
            }
        });



        return view;
    }





    public boolean isETEmpty(EditText et) {
        if (et.getText().toString().equals("") || et.getText().toString().equals(" ")) {
            et.setError("required");
            return true;
        }
        return false;
    }

    private boolean verifyFields() {
        EditText password,name,phone;
        password= EditProfile_PasswordEt;
        name=EditProfile_NameEt;
        phone=EditProfile_PhoneEt;

        if (!isETEmpty(password) && !isETEmpty(name) && !isETEmpty(phone)) {
            return true;
        }
        return false;
    }

    private boolean verifyPassword(View view) {
        String p = EditProfile_PasswordEt.getText().toString();
        if (p.length() < 6) {
            Toast toast = Toast.makeText(view.getContext(), "the password is at least 6 character", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }


    private void UpdateProfile(View view) {
        progBar_editProfile.setVisibility(View.VISIBLE);
        userNow.setName(EditProfile_NameEt.getText().toString());
        userNow.setPhone(EditProfile_PhoneEt.getText().toString());
        userNow.setPassword(EditProfile_PasswordEt.getText().toString());
        Model.instance.UpdateUser(userNow, new Model.updateUserListener() {
            @Override
            public void onComplete(Boolean success) {
                if (success)
                    Navigation.findNavController(view).popBackStack();
                else
                    Toast.makeText(view.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

    }


}