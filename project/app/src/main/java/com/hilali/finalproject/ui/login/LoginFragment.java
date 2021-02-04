package com.hilali.finalproject.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.R;


public class LoginFragment extends Fragment {
    EditText mailET;
    EditText passwordET;
    Button loginBtn;
    Button signUpBtn;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        mailET=view.findViewById(R.id.login_email);
        passwordET=view.findViewById(R.id.login_password);
        progressBar=view.findViewById(R.id.login_progressBar);
        loginBtn=view.findViewById(R.id.login_loginbtn);
        signUpBtn=view.findViewById(R.id.login_sigup_btn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyFields())
                {
                    LoginUser(view);
                }
                //Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_nav_home);
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment);
            }
        });

        return view;
    }

    private void LoginUser(View view) {
        String email,password;
        email=mailET.getText().toString();
        password=passwordET.getText().toString();
        loginBtn.setEnabled(false);
        signUpBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        Model.instance.LoginUser(email, password, new Model.LoginUserListener() {
            @Override
            public void onComplete(boolean success) {
                if(success)
                {
                    Toast.makeText(view.getContext(),"user login",Toast.LENGTH_SHORT).show();
                    final String id=Model.instance.getUserID();
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_nav_home);
                }
                else {
                    Toast.makeText(view.getContext(),"ERROR",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    loginBtn.setEnabled(true);
                    signUpBtn.setEnabled(true);
                }
            }
        });
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
        if(!isETEmpty(mailET) && !isETEmpty(passwordET) )
        {
            return true;
        }
        return false;
    }

}