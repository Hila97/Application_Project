package com.hilali.finalproject.Model;

import android.graphics.Bitmap;

public class UserModelFireBase {

    public UserModelFireBase(){}

    public static void getAllUsers(Model.GetAllUsersListener listener) {
    }

    public static void getUserById(String id, Model.GetUserByIDsListener listener) {
    }

    public static void addUser(User user, Model.AddUserListener listener) {
    }

    public static void UpdateUser(User user, Model.updateUserListener listener) {
    }

    public static void deleteUser(User user, Model.deleteUserListener listener) {
    }
    public interface UploadImageListener{
        public void onComplete(String url);
    }
    public static void uploadImage(Bitmap imageBmp, String fileName, UserModelFireBase.UploadImageListener listener) {
    }


    //public static void addUser(User user)






}
