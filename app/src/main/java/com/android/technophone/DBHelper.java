package com.android.technophone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by User on 21.06.2017.
 */

class DBHelper extends SQLiteOpenHelper {
    private static final String DB_TABLE = "EmployeePhone";
    private SQLiteDatabase mDB;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("CreateDB", "---on Create BD Employee---");

        sqLiteDatabase.execSQL("create table "+DB_TABLE+" ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NameEmployee text, "
                + "PhoneNumber text" + ");");

        createArrayForDB(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(sqLiteDatabase);
    }

    public DBHelper(Context context) {
        super(context, "Employee.db", null, 1);
    }

    private void createArrayForDB(SQLiteDatabase db){

        ContentValues cv = new ContentValues();

        for (int i=1; i<200; i++) {
            cv.put("NameEmployee", "Name_"+i);
            cv.put("PhoneNumber", "+38050"+i);

            db.insert(DB_TABLE, null, cv);
        }

    }

    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }
}
