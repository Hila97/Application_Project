package com.hilali.finalproject.Model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PostModelSQL {

    public LiveData<List<Post>> getAllPosts(){
        return AppLocalDB.db.postDao().getAllPosts();
    }
    public LiveData<List<Post>> getAllUserPosts(String uid ) {
        return AppLocalDB.db.postDao().getAllUserPosts(uid);
    }



    public static void addPost(Post post,final Model.AddPostWithIDListener listener) {
        class MyAsynchTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDB.db.postDao().insertAll(post);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete(true);
                }
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();

    }


    public static void deletePost(final Post post, final Model.deletePostListener listener) {

        class MyAsynchTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDB.db.postDao().delete(post);
               return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete(true);
                }
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }




    public void getPostByID(final String pid, final Model.GetPostByIDsListener listener){
        class MyAsynchTask extends AsyncTask<String,String,String>{
            Post post;
            //will execute on the new thread
            @Override
            protected String doInBackground(String... strings) {
                post = AppLocalDB.db.postDao().getPostByID(pid);
                return null;
            }

            //will execute on the main thread
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                listener.onComplete(post);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute("");
    }
    public void updatePost(final Post post, final Model.updatePostListener listener) {
        class MyAsynchTask extends AsyncTask{
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDB.db.postDao().updatePost(post);
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) {
                    listener.onComplete(true);
                }
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }
}

