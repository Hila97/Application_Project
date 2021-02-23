package com.hilali.finalproject;
//Get inspired to design your home
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.User;
import com.squareup.picasso.Picasso;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    public Toolbar toolbar;
    NavigationView navigationView;
    TextView mail,name;
    ImageView profileImage;
    User userNow;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View v=navigationView.inflateHeaderView(R.layout.nav_header_main);
        mail=v.findViewById(R.id.menu_user_mail);
        name=v.findViewById(R.id.menu_user_name);
        profileImage=v.findViewById(R.id.menu_user_image);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_add_design, R.id.nav_user_profile,R.id.userPostList)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public void setUserDetails() {
        String uid=Model.instance.getUserID();
       userNow=Model.instance.getUserById(uid, new Model.GetUserByIDsListener() {
            @Override
            public void onComplete(User user) {
                userNow=user;
                mail.setText(userNow.getEmail());
                name.setText(userNow.getName());

                if(userNow.getImageUrl()!=null &&userNow.getImageUrl()!="")
                {
                    Picasso.get().load(userNow.getImageUrl()).into(profileImage);
                }
                else
                {
                    profileImage.setImageResource(R.drawable.user_profile_picture);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    protected void onStart() {
        super.onStart();
        Log.d("TAG","main activity onStart");
    }
}