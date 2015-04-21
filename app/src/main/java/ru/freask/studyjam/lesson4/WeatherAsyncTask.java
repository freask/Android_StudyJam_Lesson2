package ru.freask.studyjam.lesson4;

/**
 * Created by Alexander.Kashin01 on 21.04.2015.
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
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

import ru.freask.studyjam.lesson4.provider.WeatherContentProvider;
import ru.freask.studyjam.lesson4.provider.weather.WeatherColumns;

public class WeatherAsyncTask extends AsyncTask<Void, Void, String> {
    private final ContentResolver resolver;

    public WeatherAsyncTask(Context context) {
        resolver = context.getContentResolver();
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

            final JSONObject json = new JSONObject(builder.toString());
            JSONArray list = json.getJSONArray("list");
            ArrayList<JSONObject> itemsJSON = new ArrayList<>();
            JSONObject json_data;
            String prevDate = "";

            ContentValues[] values = new ContentValues[list.length()];

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
                //itemsJSON.add(json_data);

                values[i] = new ContentValues();
                values[i].put("date", dateFormatDay.format(date));
                values[i].put("temp", json_data.getJSONObject("main").getString("temp"));


                values[i].put("icon_code", json_data.getJSONArray("weather").getJSONObject(0).getString("icon"));
            }
            refreshContentProvider(values);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void refreshContentProvider(ContentValues[] values) {
        Log.v("", "refreshContentProvider");

        resolver.delete(Uri.parse("content://" + WeatherContentProvider.AUTHORITY + "/" + WeatherColumns.TABLE_NAME), null, null);
        resolver.bulkInsert(Uri.parse("content://" + WeatherContentProvider.AUTHORITY + "/" + WeatherColumns.TABLE_NAME), values);
    }
}