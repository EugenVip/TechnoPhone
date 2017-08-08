package com.android.technophone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import static com.android.technophone.LogInActivity.soapParam_Response;

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
                + "PhoneNumber text,"
                + "NameEmployeeLowerCase text " + ");");

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

        if (arrayListEmployee != null)
        {
            ContentValues cv = new ContentValues();

            for (Employee employee: arrayListEmployee)
            {
                cv.put("_id", employee.getmPhoneNumber());
                cv.put("NameEmployee", employee.getmNameEmployee());
                cv.put("PhoneNumber", "+380"+employee.getmPhoneNumber());
                cv.put("NameEmployeeLowerCase", employee.getmNameEmployee().toLowerCase());

                try {
                    db.insertOrThrow(DB_TABLE, null, cv);
                }catch (SQLiteException e){
                    e.printStackTrace();
                }

            }
        }

    }

    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

    public ArrayList<Employee> initialiseEmployeeData()
    {
        ArrayList<Employee> employeeArrayList = new ArrayList<>();

        int count = soapParam_Response.getPropertyCount();

        for (int i = 0; i < count; i++) {
            SoapObject property = (SoapObject) soapParam_Response.getProperty(i);
            //Log.i("initialiseEmploy", ""+property.toString());
            if (property instanceof SoapObject) {
                SoapObject info = (SoapObject) property;
                String name = info.getProperty("FullName").toString();
                String phone = info.getProperty("PhoneNumber").toString();
                Employee mEmployee = new Employee(name, phone);
                employeeArrayList.add(mEmployee);
            }
        }
        return employeeArrayList;
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
