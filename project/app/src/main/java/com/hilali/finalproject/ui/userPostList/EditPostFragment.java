package com.hilali.finalproject.ui.userPostList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
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
import android.widget.Toast;

import com.hilali.finalproject.Model.Model;
import com.hilali.finalproject.Model.Post;
import com.hilali.finalproject.Model.PostCategory;
import com.hilali.finalproject.R;
import com.hilali.finalproject.ui.home.HomeFragment;
import com.hilali.finalproject.ui.home.PostDetailsFragmentArgs;
import com.squareup.picasso.Picasso;


public class EditPostFragment extends Fragment {
    EditText title_ET;
    EditText describe_ET;
    ImageButton addImg_btn ;
    Button save_btn;
    Button cancel_btn;
    ImageButton delete_btn;
    ImageView postImage;
    ProgressBar progBar_editPost;
    Post postNow;
    Spinner categorySpinner_EP;
    private static final int RESULT_CANCELED =0 ;
    private static final int RESULT_OK =-1 ;
    boolean flagAddImage=false;

    String postCategory;
    static String[] categories = new String[]{PostCategory.LIVING_ROOM.toString(),
            PostCategory.BEDROOM.toString(),
            PostCategory.KITCHEN.toString(),
            PostCategory.CHILDREN_ROOM.toString(),
            PostCategory.BATHROOM.toString(),
            PostCategory.GARDEN.toString(),
            PostCategory.OTHER.toString()
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);
        String pid= PostDetailsFragmentArgs.fromBundle(getArguments()).getPid();

        title_ET=view.findViewById(R.id.editPost_title);
        describe_ET =view.findViewById(R.id.editPost_descrip);
        postImage=view.findViewById(R.id.editPost_img);
        addImg_btn=view.findViewById(R.id.editPost_imgBtn);
        save_btn=view.findViewById(R.id.editPost_save_btn);
        cancel_btn=view.findViewById(R.id.editPost_cancel_btn);
        delete_btn=view.findViewById(R.id.editPost_delete_btn);
        categorySpinner_EP=view.findViewById(R.id.editPost_category);
        progBar_editPost=view.findViewById(R.id.progBar_editPost);
        progBar_editPost.setVisibility(View.INVISIBLE);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner_EP.setAdapter(adapter);
        categorySpinner_EP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                postCategory=categoryPick(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("TAG","take the previous choise");
            }
        });

        Model.instance.getPostById(pid, new Model.GetPostByIDsListener() {
            @Override
            public void onComplete(Post post) {
                postNow=post;
                title_ET.setText(postNow.getTitle());
                describe_ET.setText(postNow.getDescription());
                postCategory=postNow.getCategory();
                if(postNow.getImageUrl()!=null &&postNow.getImageUrl()!="")
                    Picasso.get().load(postNow.getImageUrl()).into(postImage);
                else
                    postImage.setImageResource(R.drawable.user_profile_picture);
            }
        });

        //SAVE BUTTON
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyFields())
                    updatePost(view);
            }
        });

        //CANCEL BUTTON
       cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

       delete_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               deletePost(view);
           }
       });
        addImg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage(v);
            }
        });

        return  view;
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
        if (!isETEmpty(title_ET) && !isETEmpty(describe_ET)) {
            return true;
        }
        return false;
    }

    //updatePost FUNC
    private void updatePost(View view) {
        progBar_editPost.setVisibility(View.VISIBLE);
        postNow.setTitle(title_ET.getText().toString());
        postNow.setDescription(describe_ET.getText().toString());
        postNow.setCategory(postCategory);
        Bitmap bitmap = ((BitmapDrawable)postImage.getDrawable()).getBitmap();
        Model.instance.uploadPostImage(bitmap, postNow.getUid() + " " +postNow.getTitle(),new  Model.UploadPostImageListener() {
            @Override
            public void onComplete(String url) {
                if (url == null) {
                    progBar_editPost.setVisibility(View.INVISIBLE);
                    Toast.makeText(view.getContext(), "FAILED SAVING IMAGE", Toast.LENGTH_SHORT).show();
                } else {
                    postNow.setImageUrl(url);
                    Model.instance.UpdatePost(postNow, new Model.updatePostListener() {
                        @Override
                        public void onComplete(Boolean success) {
                            if (success) {
                                Toast.makeText(view.getContext(), "post updated", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).popBackStack();
                            } else
                                Toast.makeText(view.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //delete Post FUNC
    private void deletePost(View view) {
        postNow.setIsDeleted("true");
        progBar_editPost.setVisibility(View.VISIBLE);
        Model.instance.deletePostImage(postNow.getImageUrl(), new Model.deletePostImageListener() {
            @Override
            public void onComplete(Boolean success) {
                if(success)
                {
                    Model.instance.UpdatePost(postNow, new Model.updatePostListener() {
                        @Override
                        public void onComplete(Boolean success) {
                            Model.instance.refreshUserPosts(Model.instance.getUserID(),()->{ });
                            Model.instance.refreshAllPost(()->{ });
                        }
                    });
                    Model.instance.deletePost(postNow, new Model.deletePostListener() {
                        @Override
                        public void onComplete(Boolean success) {
                            if(success){
                                Toast.makeText(view.getContext(),"post deleted",Toast.LENGTH_SHORT).show();
                                Model.instance.refreshUserPosts(Model.instance.getUserID(),()->{ });
                                Model.instance.refreshAllPost(()->{ });
                                Navigation.findNavController(view).navigate(R.id.action_editPostFragment_to_userPostList);
                            }
                            else
                                Toast.makeText(view.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText(view.getContext(), "FAILED TO DELETE", Toast.LENGTH_SHORT).show();
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
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        postImage.setImageBitmap(selectedImage);
                        flagAddImage=true;
                    }
                    break;
                case 1: //return from gallery
                    if( resultCode==RESULT_OK && data!=null){
                        Uri imageUri=data.getData();
                        postImage.setImageURI(imageUri);
                        flagAddImage=true;
                    }
                    break;
            }
        }
    }

    //categoryPick FUNC
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