package com.hilali.finalproject.ui.addPost;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.Post;
import com.hilali.finalproject.Model.PostCategory;
import com.hilali.finalproject.R;

public class AddDesignFragment extends Fragment {

    EditText titleET;
    EditText descriptionET;
    Button addBtn;
    ProgressBar progBar_addDes;
    Spinner categorySpinner;
    PostCategory postCategory;
    ImageView postImage;
    ImageButton addImg;
    private static final int RESULT_CANCELED =0 ;
    private static final int RESULT_OK =-1 ;
    boolean flagAddImage=false;

    static String[] categories = new String[]{PostCategory.LIVING_ROOM.toString(),
            PostCategory.BEDROOM.toString(),
            PostCategory.KITCHEN.toString(),
            PostCategory.CHILDREN_ROOM.toString(),
            PostCategory.BATHROOM.toString(),
            PostCategory.GARDEN.toString(),
            PostCategory.OTHER.toString()
           };
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_design, container, false);
        progBar_addDes=view.findViewById(R.id.progBar_addDes);
        progBar_addDes.setVisibility(View.INVISIBLE);
        postImage=view.findViewById(R.id.addpost_img);
        addImg=view.findViewById(R.id.addpost_imgBtn);
        titleET=view.findViewById(R.id.addpost_title);
        descriptionET=view.findViewById(R.id.addpost_text);
        addBtn=view.findViewById(R.id.addpost_addBtn);
        categorySpinner=view.findViewById(R.id.addpost_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                postCategory=categoryPick(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                postCategory=PostCategory.OTHER;
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verifyFields())
                {
                    progBar_addDes.setVisibility(View.VISIBLE);
                    savePost(v);
                }
            }
        });
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage(v);
            }
        });
        return view;
    }



    // isEmpty FUNC
    public boolean isETEmpty(EditText et) {
        if (et.getText().toString().equals("") || et.getText().toString().equals(" ")) {
            et.setError("required");
            return true;
        }
        return false;
    }

    //verifyFields FUNC
    private boolean verifyFields() {
        EditText title,description;
        title= titleET;
        description=descriptionET;

        if (!isETEmpty(title) && !isETEmpty(description)) {
            return true;
        }
        return false;
    }

    private void savePost(View v) {
        String title,description;
        final String uid= Model.instance.getUserID();
        addBtn.setEnabled(false);
        addImg.setEnabled(false);
        title=titleET.getText().toString();
        description=descriptionET.getText().toString();
        Post post=new Post(uid,title,description,postCategory.toString());
        Bitmap bitmap = ((BitmapDrawable)postImage.getDrawable()).getBitmap();
        Model.instance.uploadPostImage(bitmap, uid + " " +title, new Model.UploadPostImageListener() {
            @Override
            public void onComplete(String url) {
                if (url == null){
                    progBar_addDes.setVisibility(View.INVISIBLE);
                    Toast.makeText(v.getContext(), "FAILED SAVING IMAGE", Toast.LENGTH_SHORT).show();
                }
                else {
                    post.setImageUrl(url);
                    Model.instance.addPostWithID(post, new Model.AddPostWithIDListener() {
                        @Override
                        public void onComplete(boolean success) {
                            if(success)
                            {
                                Toast.makeText(v.getContext(),"post created",Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(v).navigate(R.id.action_nav_add_design_to_userPostList);
                            }
                            else
                                Toast.makeText(v.getContext(),"error",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void editImage(View v) {
        final CharSequence[] options = {"Take a photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose a picture for your new design");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take a photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto=new Intent(Intent.ACTION_GET_CONTENT);
                    pickPhoto.setType("image/*");
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
                        //imageUri=data.getData();
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        postImage.setImageBitmap(selectedImage);
                        flagAddImage=true;
                    }
                    break;
                case 1: //return from gallery
                    if( resultCode==RESULT_OK && data!=null){
                        //imageUri=data.getData();
                        postImage.setImageURI(data.getData());
                        flagAddImage=true;
                    }
                    break;
            }
        }
    }

    private PostCategory categoryPick(int position) {
        switch (position){
            case 0: return PostCategory.LIVING_ROOM;
            case 1: return  PostCategory.BEDROOM;
            case 2: return PostCategory.KITCHEN;
            case 3: return PostCategory.CHILDREN_ROOM;
            case 4: return PostCategory.BATHROOM;
            case 5: return PostCategory.GARDEN;
        }
        return PostCategory.OTHER;
    }
}