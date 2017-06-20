package com.android.technophone;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

/**
 * Created by User on 20.06.2017.
 */

public class EmployeeLoader extends AsyncTaskLoader<ArrayList<Employee>> {

    public EmployeeLoader(Context context) {
        super(context);
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
    public ArrayList<Employee> loadInBackground() {
        return createArray();
    }

    private ArrayList<Employee> createArray(){

        final ArrayList<Employee> arr_employees = new ArrayList<Employee>();

        for (int i=1; i<200; i++) {
            arr_employees.add(new Employee(""+i, "+38050"+i));
            /*arr_employees.add(new Employee("two", "+38067"));
            arr_employees.add(new Employee("three", "+38073"));
            arr_employees.add(new Employee("one1", "+38050"));
            arr_employees.add(new Employee("two2", "+38067"));
            arr_employees.add(new Employee("three3", "+38073"));
            arr_employees.add(new Employee("one4", "+38050"));
            arr_employees.add(new Employee("two5", "+38067"));
            arr_employees.add(new Employee("three6", "+38073"));
            arr_employees.add(new Employee("one7", "+38050"));
            arr_employees.add(new Employee("two8", "+38067"));
            arr_employees.add(new Employee("three9", "+38073"));*/
        }
        return arr_employees;
    }
}
