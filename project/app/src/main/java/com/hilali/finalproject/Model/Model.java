package com.hilali.finalproject.Model;

import android.graphics.Bitmap;

import java.util.List;

public class Model {
    public final static Model instance = new Model();
    UserModelFireBase userModelFireBase=new UserModelFireBase();
    private Model(){}

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
        void onComplete();
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
    public void uploadImage(Bitmap imageBmp, String fileName, final UserModelFireBase.UploadImageListener listener){
        UserModelFireBase.uploadImage(imageBmp,fileName,listener);
    }

}
