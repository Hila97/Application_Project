package com.hilali.finalproject.Model;

import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Model {
    public final static Model instance = new Model();
    UserModelFireBase userModelFireBase=new UserModelFireBase();
    private Model(){}

//////////////////////////////////////////////////////////////////////////////
    //////user related acts/////
//////////////////////////////////////////////////////////////////////////////
    public interface CreateUserListener{
        void onComplete(boolean success);
    }
    public void CreateUser(final String email,final String password,final CreateUserListener listener)
    {
        UserModelFireBase.CreateUser(email,password,listener);
    }
    public interface LoginUserListener{
        void onComplete(boolean success);
    }
    public void LoginUser(final String email,final String password,final LoginUserListener listener)
    {
        UserModelFireBase.LoginUser(email,password,listener);
    }
    public String getUserID()
    {
        return UserModelFireBase.getUserID();
    }
    public  interface getUserNameListener{
        void  onComplete(String name);
    }
    public String getUserName(String id, final getUserNameListener listener ){
         UserModelFireBase.getUserName(id,listener);
         return null;
    }
    public FirebaseUser getCurrentUser()
    {
        return UserModelFireBase.getCurrentUser();
    }

    public interface GetAllUsersListener{
        void onComplete(List<User> data);
    }
    public void getAllUsers(final GetAllUsersListener listener) {
        UserModelFireBase.getAllUsers(listener);
    }
    public interface GetUserByIDsListener{
        void onComplete(User user);
    }
    public User getUserById(final String id, final GetUserByIDsListener listener){
        UserModelFireBase.getUserById(id,listener);
        return null;

    }

    public interface AddUserListener{
        void onComplete(boolean success);
    }

    public void addUser(final User user, final AddUserListener listener){
        UserModelFireBase.addUser(user,listener);

    }
    public interface updateUserListener{
        void onComplete(Boolean success);
    }

    public void UpdateUser(final User user, final updateUserListener listener) {
        UserModelFireBase.UpdateUser( user,listener);

    }

    public interface deleteUserListener{
        void onComplete();
    }
    public void deleteUser(final User user, final deleteUserListener listener){
        UserModelFireBase.deleteUser(user,listener);

    }
    public void uploadImage(Bitmap imageBmp, String fileName, final UserModelFireBase.UploadProfileImageListener listener){
        UserModelFireBase.uploadProfileImage(imageBmp,fileName,listener);
    }


    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////
    /////////////////////////////post related acts////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }
    public void getAllPosts(final GetAllPostsListener listener) {
        PostModelFireBase.getAllPosts(listener);
    }
    public interface GetAllUserPostsListener{
        void onComplete(List<Post> data);
    }
    public void getAllUserPosts(final String userId,final GetAllUserPostsListener listener) {
        PostModelFireBase.getAllUserPosts(userId,listener);
    }

    public interface GetPostByIDsListener{
        void onComplete(Post post);
    }
    public Post getPostById(final String id, final GetPostByIDsListener listener){
        PostModelFireBase.getPost(id,listener);
        return null;

    }

    public interface AddPostWithIDListener{
        void onComplete(boolean success);
    }
    public void addPostWithID(final Post post,final AddPostWithIDListener listener){
        PostModelFireBase.addPostWithID(post,listener);

    }
    public interface AddPostListener{
        void onComplete(boolean success);
    }
    public void addPost(final Post post, final AddPostListener listener){
        PostModelFireBase.addPost(post,listener);

    }
    public interface updatePostListener{
        void onComplete(Boolean success); //לבדוק
    }

    public void UpdatePost(final Post post, final updatePostListener listener) {
        PostModelFireBase.updatePost(post,listener);

    }

    public interface deletePostListener{
        void onComplete();
    }
    public void deletePost(final Post post, final deletePostListener listener){
        PostModelFireBase.deletePost(post,listener);

    }
    public void uploadPostImage(Bitmap imageBmp, String fileName, final PostModelFireBase.UploadPostImageListener listener){
        PostModelFireBase.uploadPostImage(imageBmp,fileName,listener);
    }


}
