package com.hilali.finalproject.ui.login;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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


public class SignUpFragment extends Fragment {
    EditText mailET;
    EditText passwordET;
    EditText verifyPasswordET;
    EditText nameET;
    EditText phoneET;
    Button signUpBtn;
   // TextView  errorTV;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);
        mailET=view.findViewById(R.id.signup_email);
        passwordET=view.findViewById(R.id.signup_password);
        nameET=view.findViewById(R.id.signup_name);
        phoneET=view.findViewById(R.id.signup_phone);
        verifyPasswordET=view.findViewById(R.id.signup_verify_password);
        //errorTV=view.findViewById(R.id.signup_error_mssg);
        signUpBtn=view.findViewById(R.id.signup_btn);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyFields()==true)
                {
                    if(verifyPassword(view)==true)
                    {
                        //errorTV.setText("");
                        saveUser(view);
                    }
                    else
                    {
                        CharSequence text = "the password is not the same";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(view.getContext(), text, duration);
                        toast.show();
                        //errorTV.setText("the password is not the same");
                        //errorTV.setTextColor(Color.RED);
                    }
                }
                else
                {
                    CharSequence text = "All fields are required";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(view.getContext(), text, duration);
                    toast.show();
                }
            }
        });


        return view;
    }
    public boolean isETEmpty(EditText et){
        if( et.getText().toString().equals("") || et.getText().toString().equals(" "))
            return true;
        return false;
    }
    private boolean verifyFields() {
        if(isETEmpty(mailET)==true || isETEmpty(passwordET)==true ||isETEmpty(nameET)==true || isETEmpty(phoneET)==true ||isETEmpty(verifyPasswordET)==true)
            return false;
        return true;
    }

    private boolean verifyPassword(View view) {
        if(passwordET.getText().toString().equals(verifyPasswordET.getText().toString()))
            return true;
        return false;
    }

    private void saveUser(View view) {
        String email,password,name,phone;

        email=mailET.getText().toString();
        password=passwordET.getText().toString();
        name=nameET.getText().toString();
        phone=phoneET.getText().toString();
        User user=new User(email,password,name,phone);
        signUpBtn.setEnabled(false);
        Model.instance.addUser(user, new Model.AddUserListener() {
            @Override
            public void onComplete(boolean success) {
                Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_nav_home);
            }
        });


    }
}