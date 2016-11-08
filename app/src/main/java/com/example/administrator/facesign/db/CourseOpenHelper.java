package com.example.administrator.facesign.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/11/1.
 */
public class CourseOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_TABLE_COURSE ="create table Course(" +
            "id integer primary key autoincrement," +
            "studentId text," +
            "courseName text," +
            "courseId text," +
            "teacherName text," +
            "teacherId text," +
            "room text," +
            "day integer," +
            "startWeek integer," +
            "totalWeeks integer," +
            "singleOrDouble integer," +
            "startSection integer," +
            "totalSection integer)";



    public CourseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COURSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                //sqLiteDatabase.execSQL(CREATE_TABLE_COURSE);
            default:
                break;
        }
    }
}
