package com.hilali.finalproject.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserModelFireBase {
    public UserModelFireBase(){}

    public static void CreateUser(String email,String password,Model.CreateUserListener listener) {
        FirebaseAuth fauth=FirebaseAuth.getInstance();
        fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    listener.onComplete(true);
                else
                    listener.onComplete(false);
            }
        });
    }
    public static boolean isSign()
    {
        FirebaseAuth fauth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=fauth.getCurrentUser();
        if (currentUser==null)
            return false;
        return true;
    }
    public static void logOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public static String getUserID() {
        FirebaseAuth fauth=FirebaseAuth.getInstance();
        return fauth.getCurrentUser().getUid();
    }

    public static void getUserName(String id,Model.getUserNameListener listener ) {
        Model.instance.getUserById(id, new Model.GetUserByIDsListener() {
            @Override
            public void onComplete(User user) {
                final User user1=user;
                listener.onComplete(user1.getName());
            }
        });
    }





    public static FirebaseUser getCurrentUser() {
        FirebaseAuth fauth=FirebaseAuth.getInstance();
        return fauth.getCurrentUser();
    }

    public static void LoginUser(String email, String password, Model.LoginUserListener listener) {
        FirebaseAuth fauth=FirebaseAuth.getInstance();
        fauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    listener.onComplete(true);
                else
                    listener.onComplete(false);
            }
        });
    }
    public static void getAllUsers(Model.GetAllUsersListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<User> userList = new LinkedList<User>();
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        User user = doc.toObject(User.class);
                        userList.add(user);
                    }
                    listener.onComplete(userList);
                }else{
                    Log.d("TAG", "failed getting users from fb");
                    listener.onComplete(null);
                }
            }
        });
    }

    public static void getUserById(String id, Model.GetUserByIDsListener listener) {
        //FirebaseAuth fauth=FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            User user = task.getResult().toObject(User.class);
                            listener.onComplete(user);
                        }else{
                            listener.onComplete(null);
                        }
                    }
                });
    }

    public static void addUser(User user, Model.AddUserListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> data=new HashMap<String, Object>();
        data.put("uid",user.getUid());
        data.put("email",user.getEmail());
        data.put("password",user.getPassword());
        data.put("name",user.getName());
        data.put("phone",user.getPhone());

        db.collection("users").document(user.getUid()).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                        listener.onComplete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                        listener.onComplete(false);
                    }
                });
    }

    public static void UpdateUser(User user, Model.updateUserListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth fauth=FirebaseAuth.getInstance();
        FirebaseUser user1 = fauth.getCurrentUser();
        db.collection("users").document(user.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                        user1.updatePassword(user.getPassword());
                        listener.onComplete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                        listener.onComplete(false);
                    }
                });
    }

    public static void deleteUser(User user, Model.deleteUserListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth fauth=FirebaseAuth.getInstance();
        db.collection("users").document(fauth.getUid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
    }





    public static void uploadProfileImage(Bitmap imageBmp, String fileName, Model.UploadProfileImageListener listener) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child("UserProfileImage").child(fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        listener.onComplete(downloadUrl.toString());
                    }
                });
            }
        });
    }







}
