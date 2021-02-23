package com.hilali.finalproject.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;
import com.hilali.finalproject.MyApplication;

import java.util.List;

public class Model {
    public final static Model instance = new Model();
    UserModelFireBase userModelFireBase=new UserModelFireBase();
    PostModelFireBase postModelFireBase=new PostModelFireBase();
    PostModelSQL postModelSQL = new PostModelSQL();
    LiveData<List<Post>> allPostList;
    LiveData<List<Post>> userPostList;
    private Model(){}

//////////////////////////////////////////////////////////////////////////////
///////////////////////user related acts//////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
    public interface CreateUserListener{
        void onComplete(boolean success);
    }
    public void CreateUser(final String email,final String password,final CreateUserListener listener)
    {
        userModelFireBase.CreateUser(email,password,listener);
    }
    public interface LoginUserListener{
        void onComplete(boolean success);
    }
    public void LoginUser(final String email,final String password,final LoginUserListener listener)
    {
        userModelFireBase.LoginUser(email,password,listener);
    }
    public String getUserID()
    {
        return userModelFireBase.getUserID();
    }
    public boolean isSign()
    {
        return userModelFireBase.isSign();
    }
    public void logOut()
    {
        userModelFireBase.logOut();
    }
    public  interface getUserNameListener{
        void  onComplete(String name);
    }
    public String getUserName(String id, final getUserNameListener listener ){
        userModelFireBase.getUserName(id,listener);
         return null;
    }
    public FirebaseUser getCurrentUser()
    {
        return userModelFireBase.getCurrentUser();
    }

    public interface GetAllUsersListener{
        void onComplete(List<User> data);
    }
    public void getAllUsers(final GetAllUsersListener listener) {
        userModelFireBase.getAllUsers(listener);
    }
    public interface GetUserByIDsListener{
        void onComplete(User user);
    }
    public User getUserById(final String id, final GetUserByIDsListener listener){
        userModelFireBase.getUserById(id,listener);
        return null;

    }

    public interface AddUserListener{
        void onComplete(boolean success);
    }

    public void addUser(final User user, final AddUserListener listener){
        userModelFireBase.addUser(user,listener);

    }
    public interface updateUserListener{
        void onComplete(Boolean success);
    }

    public void UpdateUser(final User user, final updateUserListener listener) {
        userModelFireBase.UpdateUser( user,listener);

    }

    public interface deleteUserListener{
        void onComplete();
    }
    public void deleteUser(final User user, final deleteUserListener listener){
        userModelFireBase.deleteUser(user,listener);

    }
    public interface UploadProfileImageListener{
        public void onComplete(String url);
    }
    public void uploadUserImage(Bitmap imageBmp, String fileName, final UploadProfileImageListener listener){
        userModelFireBase.uploadProfileImage(imageBmp,fileName,listener);
    }


    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////
    /////////////////////////////post related acts////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    public LiveData<List<Post>> getAllPosts() {
        if (allPostList == null){
            allPostList = postModelSQL.getAllPosts();
            refreshAllPost(null);
        }
        return allPostList;
    }
    public interface refreshAllPostListener{
        void onComplete();
    }
    public void refreshAllPost(final refreshAllPostListener listener) {
        //1. get local last update date
        final SharedPreferences sp = MyApplication.getAppContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong("lastUpdated",0);
        //2. get all updated record from firebase from the last update date
        postModelFireBase.getAllPosts(lastUpdated, new PostModelFireBase.getAllPostsListener() {
            @Override
            public void onComplete(List<Post> list) {
                long lastUpdate = 0;
                for (Post p: list)
                {
                    if(p.getIsDeleted()=="true")
                        PostModelSQL.deletePost(p, new deletePostListener() {
                            @Override
                            public void onComplete(Boolean success) {

                            }
                        });
                    else
                    {
                        PostModelSQL.addPost(p,null);
                        if (p.getLastUpdated()>lastUpdate)
                            lastUpdate=p.getLastUpdated();
                    }

                }
                sp.edit().putLong("lastUpdated",lastUpdate).commit();
                if(listener!=null)
                    listener.onComplete();
            }
        });
    }

    /*
    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }
    public void getAllPosts(final GetAllPostsListener listener) {
        postModelFireBase.getAllPosts(listener);
    }

     */
    public LiveData<List<Post>> getAllUserPosts(final String userId) {
        if (userPostList == null){
            userPostList = postModelSQL.getAllUserPosts(userId);
            refreshUserPosts(userId,null);
        }
        return userPostList;
    }
    public interface refreshUserPostsListener{
        void onComplete();
    }
    public void refreshUserPosts(String userId,final refreshUserPostsListener listener) {
        //1. get local last update date
        final SharedPreferences sp = MyApplication.getAppContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong("lastUpdated",0);
        //2. get all updated record from firebase from the last update date
       // String uid=Model.instance.getUserID();
        postModelFireBase.getAllUserPosts(lastUpdated, userId, new PostModelFireBase.GetAllUserPostsListener() {
            @Override
            public void onComplete(List<Post> list) {
                long lastUpdate = 0;
                for (Post p: list)
                {
                    if(p.getIsDeleted()=="true")
                        PostModelSQL.deletePost(p, new deletePostListener() {
                            @Override
                            public void onComplete(Boolean success) {

                            }
                        });
                    else
                    {
                        if(p.getUid()==userId)
                            PostModelSQL.addPost(p,null);
                        if (p.getLastUpdated()>lastUpdate)
                            lastUpdate=p.getLastUpdated();
                    }
                }
                sp.edit().putLong("lastUpdated",lastUpdate).commit();
                if(listener!=null)
                    listener.onComplete();

            }
        });
    }

    public interface GetPostByIDsListener{
        void onComplete(Post post);
    }
    public Post getPostById(final String id, final GetPostByIDsListener listener){
        postModelFireBase.getPost(id,listener);
        return null;
    }

    public interface AddPostWithIDListener{
        void onComplete(boolean success);
    }
    public void addPostWithID(final Post post,final AddPostWithIDListener listener){
        postModelFireBase.addPostWithID(post, new AddPostWithIDListener() {
            @Override
            public void onComplete(boolean success) {
                refreshAllPost(new refreshAllPostListener() {
                    @Override
                    public void onComplete() {
                        listener.onComplete(true);
                    }
                });
            }
        });

    }

    public interface updatePostListener{
        void onComplete(Boolean success);
    }

    public void UpdatePost(final Post post, final updatePostListener listener) {
        postModelFireBase.updatePost(post,listener);
    }

    public interface deletePostListener{
        void onComplete(Boolean success);
    }
    public void deletePost(final Post post, final deletePostListener listener){
        //postModelSQL.deletePost(post, listener);
        postModelFireBase.deletePost(post, new deletePostListener() {
            @Override
            public void onComplete(Boolean success) {
                listener.onComplete(true);
            }
        });
    }
    public interface UploadPostImageListener{
        public void onComplete(String url);
    }
    public void uploadPostImage(Bitmap imageBmp, String fileName, final UploadPostImageListener listener){
        postModelFireBase.uploadPostImage(imageBmp,fileName,listener);
    }
    public interface deletePostImageListener{
        void onComplete(Boolean success);
    }
    public void deletePostImage(String fileName, final deletePostImageListener listener ) {
        postModelFireBase.deletePostImage(fileName,listener);
    }


}
