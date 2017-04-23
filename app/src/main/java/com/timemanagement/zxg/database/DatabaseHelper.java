package com.timemanagement.zxg.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.timemanagement.zxg.utils.LogUtils;

/**
 * Created by zxg on 17/3/14.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "TimerManagement.db";
    public static final String TABLE_NAME = "Event";
    private static final int version = 1;
    private static final String CREATE_EVENT = "create table "+ TABLE_NAME
            + "(id integer primary key autoincrement, "
            + "title text, "
            + "date text default (date('now', 'localtime')), "
            + "remind text, "
            + "remindAgain text, "
            + "emergency integer, "
            + "importance integer, "
            + "repeat integer, "
            + "repeatEnd text, "
            + "comment text, "
            + "isFinished integer)";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtils.i("DatabaseHelper", "new Database succeed!");
        db.execSQL(CREATE_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
