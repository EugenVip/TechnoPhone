package com.android.technophone;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.startapp.android.publish.ads.splash.SplashConfig;
import com.startapp.android.publish.adsCommon.AutoInterstitialPreferences;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import static com.android.technophone.LogInActivity.preferences;
import static com.android.technophone.R.id.itemClose;
import static com.android.technophone.R.id.itemRefresh;

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
        //ADs
        StartAppSDK.init(this, getString(R.string.ReturnADs), true);

        StartAppAd.showSplash(this, savedInstanceState,
                new SplashConfig()
                        .setTheme(SplashConfig.Theme.DEEP_BLUE)
                        .setAppName(getString(R.string.app_name))
                        .setLogo(R.mipmap.ic_launcher)   // resource ID
                        //.setOrientation(SplashConfig.Orientation.LANDSCAPE)
        );

        StartAppAd.setAutoInterstitialPreferences(
                new AutoInterstitialPreferences()
                        .setSecondsBetweenAds(60)
        );
        //end ADs

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

        mEmployeeRecycleAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                int pos = recyclerView.getChildPosition(view);

                Cursor cursor = mEmployeeRecycleAdapter.getCursor();
                cursor.moveToPosition(pos);
                String stringYouExtracted = cursor.getString(2);
                //Log.i("LongClick", stringYouExtracted);

                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(stringYouExtracted);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", stringYouExtracted);
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(getBaseContext(), "", Toast.LENGTH_LONG).show();
                return true;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case itemRefresh: {

                //ADs
                StartAppAd.showAd(this);
                //***
                new ReloadData().execute();

                preferences.edit().clear().apply();

                Intent intentMain = new Intent(this, LogInActivity.class);
                startActivity(intentMain);
                this.finish();

                break;
            }
            case itemClose:{
                this.finishAffinity();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i("TestLoaderCreate", "ItemCount = "+mEmployeeRecycleAdapter.getItemCount());

        return new EmployeeLoader(this, "");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.i("TestLoaderFinished", "ItemCount = "+mEmployeeRecycleAdapter.getItemCount());
        mEmployeeRecycleAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private class ReloadData extends AsyncTask{
        public ReloadData() {
            super();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            DBHelper dbHelper = new DBHelper(getBaseContext());
            dbHelper.onUpgrade(dbHelper.getWritableDatabase(),0,1);


            //SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(soapParam_timeout, soapParam_URL, soapParam_user, soapParam_pass, 3);
            //dispatcher.start();
            return new Object();
        }

        @Override
        protected void onCancelled() {
            //getSupportLoaderManager().restartLoader(numberLoader, null, MainActivity.this).forceLoad();
            super.onCancelled();
        }
    }
}

