package com.android.technophone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by User on 21.06.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_TABLE = "EmployeePhone";
    private SQLiteDatabase mDB;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Log.d("CreateDB", "---on Create BD Employee---");

        sqLiteDatabase.execSQL("create table "+DB_TABLE+" ("
                + "_id STRING PRIMARY KEY, "
                + "NameEmployee text, "
                + "PhoneNumber text" + ");");

        createArrayForDB(sqLiteDatabase, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(sqLiteDatabase);
    }

    public DBHelper(Context context) {
        super(context, "Employee.db", null, 1);
    }

    public void createArrayForDB(SQLiteDatabase db, ArrayList<Employee> arrayListEmployee) {

        ContentValues cv = new ContentValues();

        if (arrayListEmployee != null)
        {
            for (Employee employee: arrayListEmployee)
            {
                cv.put("_id", employee.getmPhoneNumber());
                cv.put("NameEmployee", employee.getmNameEmployee());
                cv.put("PhoneNumber", "+380"+employee.getmPhoneNumber());

                try {
                    db.insert(DB_TABLE, null, cv);
                }catch (Exception e){

                }

            }
        }

    }

    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

}
