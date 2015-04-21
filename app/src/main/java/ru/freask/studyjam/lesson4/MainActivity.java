package ru.freask.studyjam.lesson4;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.freask.studyjam.lesson4.controller.ForecastController;
import ru.freask.studyjam.lesson4.provider.WeatherContentProvider;
import ru.freask.studyjam.lesson4.provider.weather.WeatherColumns;
import ru.freask.studyjam.lesson4.provider.weather.WeatherContentValues;
import ru.freask.studyjam.lesson4.provider.weather.WeatherCursor;
import ru.freask.studyjam.lesson4.provider.weather.WeatherModel;
import ru.freask.studyjam.lesson4.provider.weather.WeatherSelection;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String JSON_RESULT = "JSON_RESULT";
    private static final int WEATHER_LOADER = 1;

    TextView title;
    private WeatherCursorAdapter weatherCursorAdapter;

    ProgressBar progress;
    Context context;
    String jsonResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        progress = (ProgressBar) findViewById(R.id.progress);
        title = (TextView) findViewById(R.id.title);
        final ForecastController forecastController = new ForecastController(context);

        weatherCursorAdapter = new WeatherCursorAdapter(context);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(weatherCursorAdapter);
        getLoaderManager().initLoader(WEATHER_LOADER, null, this);


        if (savedInstanceState != null && savedInstanceState.containsKey(JSON_RESULT)) {
            jsonResult = savedInstanceState.getString(JSON_RESULT);
            //updateListView();
        }

        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forecastController.refreshForecast();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        saveInstanceState.putString(JSON_RESULT, jsonResult);
        // всегда вызывайте суперкласс для сохранения состояний видов
        super.onSaveInstanceState(saveInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        jsonResult = savedInstanceState.getString(JSON_RESULT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.v("", "onCreateLoader");
        return new CursorLoader(this, Uri.parse("content://" + WeatherContentProvider.AUTHORITY + "/" + WeatherColumns.TABLE_NAME), null, null, null, BaseColumns._ID);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v("", "onLoadFinished");
        weatherCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v("", "onLoaderReset");
        weatherCursorAdapter.swapCursor(null);
    }
}
