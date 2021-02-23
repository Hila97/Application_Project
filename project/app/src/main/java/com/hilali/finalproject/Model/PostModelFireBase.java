package com.hilali.finalproject.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
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
public class PostModelFireBase {
    public PostModelFireBase(){}

    interface getAllPostsListener{
        void onComplete(List<Post> list);
    }
    public static void getAllPosts(Long lastUpdated,final getAllPostsListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(lastUpdated,0);
        db.collection("posts").whereGreaterThanOrEqualTo("lastUpdated",ts).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<Post> postList = new LinkedList<Post>();
                    for (DocumentSnapshot doc:task.getResult()) {
                        Post post =new Post();
                        post.fromMap(doc.getData());
                        postList.add(post);
                    }
                    listener.onComplete(postList);
                }
                //else{
                //    listener.onComplete(null);
                //}
            }
        });
    }

     interface GetAllUserPostsListener{
        void onComplete(List<Post> data);
    }
    public static void getAllUserPosts(Long lastUpdated,String userId,final GetAllUserPostsListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(lastUpdated,0);
        db.collection("posts").whereGreaterThanOrEqualTo("lastUpdated",ts).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<Post> postList = new LinkedList<Post>();
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        Log.d("TAG","post id: " + doc.get("pid"));
                       // Post post = doc.toObject(Post.class);
                        Post post =new Post();
                        post.fromMap(doc.getData());
                        if(post.getUid().equals(userId))
                            postList.add(post);
                    }
                    listener.onComplete(postList);
                }else{
                    Log.d("TAG", "failed getting posts from fb");
                    listener.onComplete(null);
                }
            }
        });
    }

    public static void getPost(String pid, Model.GetPostByIDsListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(pid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Post post=new Post();
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null) {
                        post.fromMap(task.getResult().getData());
                    }
                    //Post post  = task.getResult().toObject(Post.class);
                    listener.onComplete(post);
                }else{
                    listener.onComplete(null);
                }
            }
        });
    }

    public static void updatePost(Post post, Model.updatePostListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(post.getPid())
                .set(post.toMap())
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

    public static void deletePost(Post post, Model.deletePostListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(post.getPid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                        listener.onComplete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                        listener.onComplete(false);
                    }
                });
    }

    public static void addPostWithID(Post post, Model.AddPostWithIDListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("posts").document();
        String pid=ref.getId();
        post.setPid(pid);
        db.collection("posts").document(pid).set(post.toMap())
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



    public static void uploadPostImage(Bitmap imageBmp, String fileName, Model.UploadPostImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child("PostsImage").child(fileName);
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

    public static void deletePostImage(String fileName, Model.deletePostImageListener listener) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        /*StorageReference storageRef = storage.getReference();
        String deleteImg="PostsImage/"+" "+fileName;
        StorageReference desertRef = storageRef.child(fileName);
        desertRef.delete().addOnSuccessListener
         */
        StorageReference photoRef = storage.getReferenceFromUrl(fileName);
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onComplete(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                listener.onComplete(false);
            }
        });
    }
}
