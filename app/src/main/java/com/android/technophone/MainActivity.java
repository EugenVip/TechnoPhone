package com.android.technophone;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by User on 15.06.2017.
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Employee>> {

    public int numberLoader = 0;
    private EmployeeRecycleAdapter mEmployeeRecycleAdapter;
    private RecyclerView recyclerView;

    //@SdkConstant(SdkConstantType.ACTIVITY_INTENT_ACTION)
    @RequiresPermission(Manifest.permission.CALL_PHONE)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final ArrayList<Employee> arr_employees = createArray();

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

        recyclerView = (RecyclerView) findViewById(R.id.employee_recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mEmployeeRecycleAdapter = new EmployeeRecycleAdapter(this, null);
        recyclerView.setAdapter(mEmployeeRecycleAdapter);

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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Log.i("Test Scroll", ""+newState);

                if(newState == 1) {
                    if(mEmployeeRecycleAdapter.getItemCount() == -1 || mEmployeeRecycleAdapter.getItemCount() == 0 ){
                        getSupportLoaderManager().restartLoader(numberLoader, null, MainActivity.this).forceLoad();
                    }
                }
            }
        } );

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

    @Override
    public Loader<ArrayList<Employee>> onCreateLoader(int id, Bundle args) {
        Log.i("TestLoaderCreate", ""+mEmployeeRecycleAdapter.getItemCount());

        return new EmployeeLoader(getBaseContext());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Employee>> loader, ArrayList<Employee> data) {

        mEmployeeRecycleAdapter.mFilteredList=(data);
        mEmployeeRecycleAdapter.notifyDataSetChanged();
        Log.i("TestLoaderFinished", ""+mEmployeeRecycleAdapter.getItemCount());
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Employee>> loader) {
        mEmployeeRecycleAdapter.mFilteredList=(new ArrayList<Employee>());
        Log.i("TestLoaderReset", ""+mEmployeeRecycleAdapter.getItemCount());

    }
}
