package com.android.technophone;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

/**
 * Created by User on 20.06.2017.
 */

public class EmployeeLoader extends AsyncTaskLoader<Cursor> {

    DBHelper dbHelper;
    String mArgs;

    public EmployeeLoader(Context context, String args) {
        super(context);
        mArgs = args;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }

    @Override
    public Cursor loadInBackground() {
        return createArray();
    }

    private Cursor createArray(){

        final ArrayList<Employee> arr_employees = new ArrayList<Employee>();

        String selection = "NameEmployee Like ? ";
        String[] selectionArgs = new String[]{"%"+mArgs+"%"};

        dbHelper = new DBHelper(getContext());
        //Log.d("loadInBack", ""+dbHelper);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Log.d("loadInBack", ""+db);

        Cursor cursor = db.query("EmployeePhone", null, selection, selectionArgs, null , null, null);
        //Log.d("loadInBack", ""+cursor.getCount());
        /*if(cursor.moveToFirst()){

            // определяем номера столбцов по имени в выборке
            int nameColIndex = cursor.getColumnIndex("NameEmployee");
            int phoneColIndex = cursor.getColumnIndex("PhoneNumber");

            do {
                arr_employees.add(new Employee(cursor.getString(nameColIndex), cursor.getString(phoneColIndex)));
            }while (cursor.moveToNext());
        }
        dbHelper.close();
        return arr_employees;*/
        dbHelper.close();
        return cursor;
    }

}

