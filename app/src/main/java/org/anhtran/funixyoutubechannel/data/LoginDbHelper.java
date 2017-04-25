package org.anhtran.funixyoutubechannel.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static org.anhtran.funixyoutubechannel.data.DbContract.LoginEntry;

/**
 * Created by anhtran on 4/23/17.
 */

public class LoginDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "xtube.db";
    private static final int DB_VERSION = 1;

    public LoginDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_LOGIN_TABLE = "CREATE TABLE " + LoginEntry.LOGIN_TABLE_NAME + " ("
                + LoginEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LoginEntry.COLUMN_LOGIN_USER + " TEXT NOT NULL, "
                + LoginEntry.COLUMN_LOGIN_PASSWORD + " TEXT NOT NULL, "
                + LoginEntry.COLUMN_LOGIN_EMAIL + " TEXT)";

        db.execSQL(SQL_CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
