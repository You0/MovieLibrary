package me.com.movielibrary.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Me on 2017/8/15.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    String CREATE_TABLE = "CREATE TABLE love(\n" +
            "\tid INTEGER primary key AUTOINCREMENT,\n" +
            "\tcover varchar(500),\n" +
            "\ttitle varchar(500),\n" +
            "\tactor varchar(500),\n" +
            "\tfh varchar(500) UNIQUE,\n" +
            "\tc_date varchar(500),\n" +
            "\trating float DEFAULT 0\n" +
            ")";


    private Context mContext;
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
