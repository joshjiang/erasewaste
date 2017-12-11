package com.example.jiang18j.erasewaste;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database_name";

    private static final String DB_POSTS = "Posts";
    private static final String P_ID = "id";
    private static final String P_DESCRIPTION = "description";
    private static final String P_LOCATION = "location";
    private static final String P_TIME = "time";
    private static final String P_USERNAME = "username";

    private static final String DB_PHOTOS = "Photos"; //DB_IMAGES
    private static final String PH_ID = "id";
    private static final String PH_IMAGE = "image";

    private static final String DB_TAGS = "Tags";
    private static final String T_ID = "id";
    private static final String T_TAG = "tag";

    private static final String CREATE_TABLE_POSTS = "CREATE TABLE " + DB_POSTS + "("+
            P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            P_DESCRIPTION + " TEXT," +
            P_LOCATION + " TEXT," +
            P_TIME + " TEXT," +
            P_USERNAME + " TEXT);";//location and time change datatype? -- timestamp?

    private static final String CREATE_TABLE_PHOTOS = "CREATE TABLE " + DB_PHOTOS + "("+
            PH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PH_IMAGE + " BLOB);";

    private static final String CREATE_TABLE_TAGS = "CREATE TABLE " + DB_TAGS + "("+
            T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            T_TAG + " TEXT);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // drops older tables
        db.execSQL("DROP TABLE IF EXISTS " + DB_POSTS);
        db.execSQL("DROP TABLE IF EXISTS " + DB_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TAGS);

        // create new table
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create tables
        db.execSQL(CREATE_TABLE_POSTS);
        db.execSQL(CREATE_TABLE_PHOTOS);
        db.execSQL(CREATE_TABLE_TAGS);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!");
    }

    //INSERT POST INTO DB
    public void addEntryPosts(String description, String location, String time, String username) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteStatement q = database.compileStatement("INSERT INTO POSTS(description, location, time, username) values('" + description + "','" + location + "','" + time + "','" + username + "')");
        q.execute();
    }

    //INSERT PHOTO INTO DB
    public void addEntryPhotos(byte[] image) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteStatement q = database.compileStatement("INSERT INTO PHOTOS(image) values(?)");
        q.bindBlob(1, image);
        q.execute();
    }

    //INSERT TAGS INTO DB
    public void addEntryTags(String tag) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteStatement q = database.compileStatement("INSERT INTO TAGS(tag) values('" + tag + "')");
        q.execute();
    }

    public Cursor getPosts(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + DB_POSTS, null);
        return cursor;
    }

    public Cursor getPhotos(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + DB_PHOTOS, null);
        return cursor;
    }

    public Cursor getTags(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + DB_TAGS, null);
        return cursor;
    }

    public Cursor getPostsPhotos(){

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + DB_POSTS + ", " + DB_PHOTOS + " WHERE "
                + DB_POSTS + "." + P_ID + " = " + DB_PHOTOS + "." + PH_ID , null);
        return cursor;
    }

    public Cursor getPostsPhotosTags(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + DB_POSTS + ", " + DB_PHOTOS + ", " + DB_TAGS + " WHERE "
                + DB_POSTS + "." + P_ID + " = " + DB_PHOTOS + "." + PH_ID
                + " AND " + DB_POSTS + "." + P_ID + " = " + DB_TAGS + "." + T_ID , null);
        return cursor;
    }

    public Cursor getSinglePost(String id){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + DB_POSTS + ", " + DB_PHOTOS + ", " + DB_TAGS + " WHERE "
                + DB_POSTS + "." + P_ID + " = " + DB_PHOTOS + "." + PH_ID
                + " AND " + DB_POSTS + "." + P_ID + " = " + DB_TAGS + "." + T_ID + " AND " + DB_POSTS + "." + P_ID + " = " + id, null);        return cursor;
    }

}