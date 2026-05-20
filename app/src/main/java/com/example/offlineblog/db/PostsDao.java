package com.example.offlineblog.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.offlineblog.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostsDao {

    private SQLiteDatabase db;

    public PostsDao(Context context) {
        PostsDbHelper helper = new PostsDbHelper(context);
        db = helper.getWritableDatabase();
    }

    // INSERT
    public long insert(Post post) {
        ContentValues values = new ContentValues();
        values.put(PostsDbHelper.COL_TITLE, post.getTitle());
        values.put(PostsDbHelper.COL_CONTENT, post.getContent());
        values.put(PostsDbHelper.COL_IMAGE_URI, post.getImageUri());
        values.put(PostsDbHelper.COL_CREATED_AT, System.currentTimeMillis());
        values.put(PostsDbHelper.COL_UPDATED_AT, System.currentTimeMillis());
        values.put(PostsDbHelper.COL_UPLOAD_STATUS, post.getUploadStatus());

        return db.insert(PostsDbHelper.TABLE_POSTS, null, values);
    }

    // UPDATE
    public void update(Post post) {
        ContentValues values = new ContentValues();
        values.put(PostsDbHelper.COL_TITLE, post.getTitle());
        values.put(PostsDbHelper.COL_CONTENT, post.getContent());
        values.put(PostsDbHelper.COL_IMAGE_URI, post.getImageUri());
        values.put(PostsDbHelper.COL_UPDATED_AT, System.currentTimeMillis());

        db.update(
                PostsDbHelper.TABLE_POSTS,
                values,
                PostsDbHelper.COL_ID + "=?",
                new String[]{String.valueOf(post.getId())}
        );
    }

    // GET BY ID
    public Post getById(long id) {
        Cursor cursor = db.query(
                PostsDbHelper.TABLE_POSTS,
                null,
                PostsDbHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            Post post = cursorToPost(cursor);
            cursor.close();
            return post;
        }

        return null;
    }

    // GET ALL
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();

        Cursor cursor = db.query(
                PostsDbHelper.TABLE_POSTS,
                null,
                null,
                null,
                null,
                null,
                PostsDbHelper.COL_CREATED_AT + " DESC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                posts.add(cursorToPost(cursor));
            }
            cursor.close();
        }

        return posts;
    }

    // SEARCH
    public List<Post> search(String keyword) {
        List<Post> posts = new ArrayList<>();

        Cursor cursor = db.query(
                PostsDbHelper.TABLE_POSTS,
                null,
                PostsDbHelper.COL_TITLE + " LIKE ? OR " +
                        PostsDbHelper.COL_CONTENT + " LIKE ?",
                new String[]{"%" + keyword + "%", "%" + keyword + "%"},
                null,
                null,
                PostsDbHelper.COL_CREATED_AT + " DESC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                posts.add(cursorToPost(cursor));
            }
            cursor.close();
        }

        return posts;
    }

    // DELETE
    public void delete(long id) {
        db.delete(
                PostsDbHelper.TABLE_POSTS,
                PostsDbHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }

    // CURSOR → POST (IMPORTANT)
    private Post cursorToPost(Cursor cursor) {
        Post post = new Post();

        post.setId(cursor.getLong(
                cursor.getColumnIndexOrThrow(PostsDbHelper.COL_ID)));
        post.setTitle(cursor.getString(
                cursor.getColumnIndexOrThrow(PostsDbHelper.COL_TITLE)));
        post.setContent(cursor.getString(
                cursor.getColumnIndexOrThrow(PostsDbHelper.COL_CONTENT)));
        post.setImageUri(cursor.getString(
                cursor.getColumnIndexOrThrow(PostsDbHelper.COL_IMAGE_URI)));
        post.setCreatedAt(cursor.getLong(
                cursor.getColumnIndexOrThrow(PostsDbHelper.COL_CREATED_AT)));
        post.setUpdatedAt(cursor.getLong(
                cursor.getColumnIndexOrThrow(PostsDbHelper.COL_UPDATED_AT)));
        post.setUploadStatus(cursor.getInt(
                cursor.getColumnIndexOrThrow(PostsDbHelper.COL_UPLOAD_STATUS)));

        return post;
    }
}
