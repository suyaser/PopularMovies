package com.training.yasser.popularmovies.utils;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by yasser on 16/09/2016.
 */
public class MovieProvider extends ContentProvider {

    public static final Uri MOVIE_URI =
            Uri.parse("content://com.yasser.popularmovies/movies");

    private static final int ALL_ROWS = 1;
    private static final int SINGLE_ROW = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.yasser.popularmovies", "movies", ALL_ROWS);
        uriMatcher.addURI("com.yasser.popularmovies", "movies/#", SINGLE_ROW);
    }

    public static final String KEY_ID = "_id";
    public static final String KEY_COLUMN_IMG = "image";
    public static final String KEY_COLUMN_TITLE = "title";
    public static final String KEY_COLUMN_PLOT = "plot";
    public static final String KEY_COLUMN_RATING = "rating";
    public static final String KEY_COLUMN_RELDATE = "release";
    public static final String KEY_COLUMN_MOVIE_ID = "movie_id";
    public static final String KEY_COLUMN_BACKDROP = "backdrop";
    public static final String KEY_COLUMN_GENRE = "genre";

    private MovieDBOpenHelper dbOpenHelper;

    @Override
    public boolean onCreate() {
        dbOpenHelper = new MovieDBOpenHelper(getContext()
                , MovieDBOpenHelper.DATABASE_NAME,
                null,
                MovieDBOpenHelper.DATABASE_VERSION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        String groupBy = null;
        String having = null;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MovieDBOpenHelper.DATABASE_TABLE);

        switch (uriMatcher.match(uri)){
            case ALL_ROWS:
                break;
            case SINGLE_ROW :
                String rowID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(KEY_ID + "=" + rowID);
                break;
            default:
                throw new IllegalArgumentException( "illegal uri: " + uri);
        }
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_ROWS:
                return "vnd.android.cursor.dir/vnd.yasser.elemental";
            case SINGLE_ROW:
                return "vnd.android.cursor.item/vnd.yasser.elemental";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        Long id = db.insert(MovieDBOpenHelper.DATABASE_TABLE,
                null, contentValues);

        if(id > -1){
            Uri insertedId = ContentUris.withAppendedId(MOVIE_URI, id);
            getContext().getContentResolver().notifyChange(insertedId, null);
            return insertedId;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case ALL_ROWS:
                break;
            case SINGLE_ROW :
                String rowID = uri.getPathSegments().get(1);
                selection = KEY_ID + "=" + rowID +
                        (!TextUtils.isEmpty(selection) ?
                        "AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException( "illegal uri: " + uri);
        }

        if(selection == null)
            selection = "1";

        int deleteCount = db.delete(MovieDBOpenHelper.DATABASE_TABLE, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case SINGLE_ROW :
                String rowID = uri.getPathSegments().get(1);
                selection = KEY_ID + "=" + rowID +
                        (!TextUtils.isEmpty(selection) ?
                                "AND (" + selection + ')' : "");
                break;
            default: break;
        }
        int updateCounnt = db.delete(MovieDBOpenHelper.DATABASE_TABLE, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return updateCounnt;
    }

    private static class MovieDBOpenHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "movieDatabase.db";
        private static final String DATABASE_TABLE = "Favorites";
        private static final int DATABASE_VERSION = 6;

        private static final String DATABASE_CREATE = "CREATE TABLE " +
                DATABASE_TABLE + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_COLUMN_IMG + " TEXT NOT NULL, " +
                KEY_COLUMN_TITLE + " TEXT NOT NULL, " +
                KEY_COLUMN_PLOT + " TEXT NOT NULL, " +
                KEY_COLUMN_RATING + " REAL, " +
                KEY_COLUMN_RELDATE + " TEXT NOT NULL, " +
                KEY_COLUMN_MOVIE_ID + " INTEGER UNIQUE, " +
                KEY_COLUMN_BACKDROP + " TEXT NOT NULL, " +
                KEY_COLUMN_GENRE + " TEXT NOT NULL)";



        public MovieDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(sqLiteDatabase);
        }
    }
}
