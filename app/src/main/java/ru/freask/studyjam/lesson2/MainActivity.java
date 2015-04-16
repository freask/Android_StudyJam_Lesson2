package ru.freask.studyjam.lesson2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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


public class MainActivity extends ActionBarActivity {
    private static final String JSON_RESULT = "JSON_RESULT";

    TextView title;
    WeatherTask weatherTask;

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

        if (savedInstanceState != null && savedInstanceState.containsKey(JSON_RESULT)) {
            jsonResult = savedInstanceState.getString(JSON_RESULT);
            updateListView();
        } else {
            weatherTask = new WeatherTask();
            weatherTask.execute();
            progress.setVisibility(View.VISIBLE);
        }

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
        updateListView();
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

    class WeatherTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                StringBuilder builder = new StringBuilder();
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q=Moscow,ru");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader buf_reader = new BufferedReader(reader);
                String line;
                while ((line = buf_reader.readLine()) != null) {
                    builder.append(line);
                }
                connection.disconnect();
                return builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            jsonResult = result;
            updateListView();
            progress.setVisibility(View.INVISIBLE);
        }
    }

    private void updateListView() {
        try {
            final JSONObject json = new JSONObject(jsonResult);
            JSONArray list = json.getJSONArray("list");
            ArrayList<JSONObject> itemsJSON = new ArrayList<>();
            JSONObject json_data;
            String prevDate = "";
            for(int i=0; i < list.length() ; i++) {
                json_data = list.getJSONObject(i);

                Timestamp stamp = new Timestamp(json_data.getLong("dt") * 1000);
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy.MM.dd");
                SimpleDateFormat dateFormatNoTime = new SimpleDateFormat("yyyy.MMMM.dd");
                Date date = new Date(stamp.getTime());
                String newDate = dateFormatNoTime.format(date);
                json_data.put("day_date", prevDate.equals(newDate) ? "" : dateFormatNoTime.format(date));
                json_data.put("day", dateFormatDay.format(date));
                json_data.put("date", dateFormat.format(date));
                prevDate = newDate;
                itemsJSON.add(json_data);
            }

            WeatherAdapter weatherAdapter = new WeatherAdapter(MainActivity.this, itemsJSON);
            ListView listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(weatherAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    JSONObject item = (JSONObject) parent.getItemAtPosition(position);

                    try {
                        Intent i = new Intent(context, WeatherInfoActivity.class);
                        i.putExtra("city_name", json.getJSONObject("city").getString("name"));
                        i.putExtra("item", item.toString());
                        context.startActivity(i);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            JSONObject city = json.getJSONObject("city");
            title.setText(city.getString("name"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
