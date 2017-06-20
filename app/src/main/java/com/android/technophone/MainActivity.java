package com.android.technophone;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by User on 15.06.2017.
 */

public class MainActivity extends AppCompatActivity {

    private EmployeeRecycleAdapter mEmployeeRecycleAdapter;

    //@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
    @RequiresPermission(Manifest.permission.CALL_PHONE)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<Employee> arr_employees = createArray();


        /*ListView linearLayout = (ListView) findViewById(R.id.employee_list);
        EmployeeAdapter adapter = new EmployeeAdapter(this, arr_employees);

        linearLayout.setAdapter(adapter);
        linearLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee word = arr_employees.get(position);

                Intent intentCall = new Intent(Intent.ACTION_CALL);
                intentCall.setData(Uri.parse("tel:"+word.getmPhoneNumber()));

                try {
                    startActivity(intentCall);
                }catch (ActivityNotFoundException activityException){
                    Toast.makeText(getBaseContext(), "Не удалось позвонить", Toast.LENGTH_LONG);
                }

            }
        });*/

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.employee_recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mEmployeeRecycleAdapter = new EmployeeRecycleAdapter(this, arr_employees);
        mEmployeeRecycleAdapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = recyclerView.indexOfChild(v);
                //Toast.makeText(getBaseContext(), ""+mEmployeeRecycleAdapter.mFilteredList.get(pos), Toast.LENGTH_SHORT).show();
                Intent intentCall = new Intent(Intent.ACTION_CALL);
                intentCall.setData(Uri.parse("tel:"+mEmployeeRecycleAdapter.mFilteredList.get(pos).getmPhoneNumber()));

                try {
                    startActivity(intentCall);
                }catch (ActivityNotFoundException activityException){
                    Toast.makeText(getBaseContext(), "Не удалось позвонить", Toast.LENGTH_LONG);
                }
            }
        });

        recyclerView.setAdapter(mEmployeeRecycleAdapter);

        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}

            @Override
            public boolean onQueryTextChange(String newText) {
                mEmployeeRecycleAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    private void initData(){

        final ArrayList<Employee> arr_employees = createArray();

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
