package com.hilali.finalproject.ui.userDetails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.User;
import com.hilali.finalproject.R;
import com.squareup.picasso.Picasso;

public class EditProfileFragment extends Fragment {
    TextView textViewEditTittle;
    EditText EditProfile_NameEt;
    EditText EditProfile_PhoneEt;
    EditText EditProfile_PasswordEt;
    Button EditProfile_saveBtn;
    Button EditProfile_cancelBtn;
    ImageView userImage;
    ImageButton editImageBtn;
    User userNow;
    ProgressBar progressBar;
    private static final int RESULT_CANCELED =0 ;
    private static final int RESULT_OK =-1 ;
    boolean flagAddImage=false;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        textViewEditTittle = view.findViewById(R.id.edit_profile_title);
        EditProfile_NameEt = view.findViewById(R.id.edit_profile_name);
        EditProfile_PhoneEt = view.findViewById(R.id.edit_profile_phone);
        EditProfile_PasswordEt = view.findViewById(R.id.edit_profile_password_ET);
        userImage=view.findViewById(R.id.edit_profile_image);
        editImageBtn=view.findViewById(R.id.edit_profile_imageBtn);
        EditProfile_saveBtn = view.findViewById(R.id.edit_profile_save_button);
        EditProfile_cancelBtn = view.findViewById(R.id.edit_profile_cancel_button);

        progressBar=view.findViewById(R.id.progBar_editProfile);
        progressBar.setVisibility(View.INVISIBLE);

        final String uid = Model.instance.getUserID();
        Model.instance.getUserById(uid, new Model.GetUserByIDsListener() {
            @Override
            public void onComplete(User user) {
                userNow = user;
                EditProfile_NameEt.setText(userNow.getName());
                EditProfile_PhoneEt.setText(userNow.getPhone());
                EditProfile_PasswordEt.setText(userNow.getPassword());
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
        EditProfile_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Navigation.findNavController(view).popBackStack();
            }
        });

        EditProfile_saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                //בדיקת קלט
                if(verifyFields())
                {
                    if(verifyPassword(view))
                        UpdateProfile(view);
                }
            }
        });
        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage(v);
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
        userNow.setName(EditProfile_NameEt.getText().toString());
        userNow.setPhone(EditProfile_PhoneEt.getText().toString());
        userNow.setPassword(EditProfile_PasswordEt.getText().toString());
        EditProfile_saveBtn.setEnabled(false);
        EditProfile_cancelBtn.setEnabled(false);
        editImageBtn.setEnabled(false);
        Bitmap bitmap = ((BitmapDrawable)userImage.getDrawable()).getBitmap();
        Model.instance.uploadUserImage(bitmap, userNow.getUid(), new Model.UploadProfileImageListener() {
            @Override
            public void onComplete(String url) {
                if (url == null){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(view.getContext(), "FAILED SAVING IMAGE", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    userNow.setImageUrl(url);
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
        });
    }

    private void editImage(View v) {
        final CharSequence[] options = {"Take a photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take a photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("Choose from Gallery")) {
//                   Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent pickPhoto=new Intent(Intent.ACTION_GET_CONTENT)   ;
                    pickPhoto.setType("UserProfileImage/*");
                    startActivityForResult(pickPhoto, 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0: //return from camera
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        userImage.setImageBitmap(selectedImage);
                        flagAddImage=true;
                    }
                    break;
                case 1: //return from gallery
                    if( resultCode==RESULT_OK && data!=null){
                        userImage.setImageURI(data.getData());
                        flagAddImage=true;
                    }
                    break;
            }
        }
    }


}