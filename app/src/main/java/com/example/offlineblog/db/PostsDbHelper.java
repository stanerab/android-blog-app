package com.example.offlineblog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PostsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "offline_blog.db";
    public static final int DATABASE_VERSION = 2; // 🔼 bump version

    public static final String TABLE_POSTS = "posts";

    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMAGE_URI = "image_uri";
    public static final String COL_CREATED_AT = "created_at";
    public static final String COL_UPDATED_AT = "updated_at";
    public static final String COL_UPLOAD_STATUS = "upload_status";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_POSTS + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TITLE + " TEXT NOT NULL, " +
                    COL_CONTENT + " TEXT NOT NULL, " +
                    COL_IMAGE_URI + " TEXT, " +
                    COL_CREATED_AT + " INTEGER NOT NULL, " +
                    COL_UPDATED_AT + " INTEGER NOT NULL, " +
                    COL_UPLOAD_STATUS + " INTEGER NOT NULL DEFAULT 0" +
                    ");";

    public PostsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Safe for coursework; recreates table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        onCreate(db);
    }
}
