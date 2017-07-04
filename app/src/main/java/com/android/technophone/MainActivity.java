package com.android.technophone;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

/**
 * Created by User on 15.06.2017.
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public int numberLoader = 0;
    private EmployeeRecycleAdapter mEmployeeRecycleAdapter;
    private RecyclerView recyclerView;

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

        mEmployeeRecycleAdapter.setClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int pos = recyclerView.getChildPosition(v);
                Intent intentCall = new Intent(Intent.ACTION_CALL);

                Cursor cursor = mEmployeeRecycleAdapter.getCursor();
                cursor.moveToPosition(pos);
                intentCall.setData(Uri.parse("tel:"+cursor.getString(2)));

                try {
                    startActivity(intentCall);
                }catch (ActivityNotFoundException activityException){
                    Toast.makeText(getBaseContext(), R.string.textCantCall, Toast.LENGTH_LONG);
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //Log.i("Test Scroll", ""+newState);

                if(newState == 1) {
                    if(mEmployeeRecycleAdapter.getItemCount() == -1 || mEmployeeRecycleAdapter.getItemCount() == 0 ){
                        getSupportLoaderManager().restartLoader(numberLoader, null, MainActivity.this).forceLoad();
                    }
                }
            }
        } );

        recyclerView.setAdapter(mEmployeeRecycleAdapter);

        getSupportLoaderManager().restartLoader(numberLoader, null, MainActivity.this).forceLoad();

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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Log.i("TestLoaderCreate", ""+mEmployeeRecycleAdapter.getItemCount());

        return new EmployeeLoader(this, "");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Cursor cursor = ((EmployeeRecycleAdapter) recyclerView.getAdapter()).getCursor();
        mEmployeeRecycleAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}

