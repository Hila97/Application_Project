package com.hilali.finalproject.ui.login;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
    ProgressBar progressBar;
    //FirebaseAuth fauth;
    

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
        progressBar=view.findViewById(R.id.signup_progressBar);
        signUpBtn=view.findViewById(R.id.signup_btn);


        /*
        fauth=FirebaseAuth.getInstance();

        if(fauth.getCurrentUser()!=null)
            Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_nav_home);


         */

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyFields())
                {
                    if(verifyPassword(view))
                        saveUser(view);
                }
            }
        });
        return view;
    }
    public boolean isETEmpty(EditText et){
        if( et.getText().toString().equals("") || et.getText().toString().equals(" "))
        {
            et.setError("required");
            return true;
        }
        return false;
    }
    private boolean verifyFields() {
        if(!isETEmpty(mailET) && !isETEmpty(passwordET) && !isETEmpty(verifyPasswordET) && !isETEmpty(nameET) && !isETEmpty(phoneET)  )
        {
            return true;
        }
        return false;
    }

    private boolean verifyPassword(View view) {
        String p=passwordET.getText().toString();
        if(p.length()<6) {
            Toast toast = Toast.makeText(view.getContext(), "the password is at least 6 character", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(!p.equals(verifyPasswordET.getText().toString()))
        {
            Toast toast = Toast.makeText(view.getContext(), "the password is not the same", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    private void saveUser(View view) {
        String email,password,name,phone;
        //final String id;

        email=mailET.getText().toString();
        password=passwordET.getText().toString();
        name=nameET.getText().toString();
        phone=phoneET.getText().toString();
        //User user=new User(email,password,name,phone);
        signUpBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        Model.instance.CreateUser(email, password, new Model.CreateUserListener() {
            @Override
            public void onComplete(boolean success) {
                if(success==true)
                {
                    Toast.makeText(view.getContext(),"user created",Toast.LENGTH_SHORT).show();
                    final String id=Model.instance.getUserID();
                    User user=new User(id,email,password,name,phone);
                    Model.instance.addUser(user, new Model.AddUserListener() {
                        @Override
                        public void onComplete(boolean success) {
                            Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_nav_home);
                        }
                    });
                }
                else {
                    Toast.makeText(view.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    signUpBtn.setEnabled(true);
                }
            }
        });
    }
}