package ru.freask.studyjam.lesson2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

/**
 * Created by Alexander.Kashin01 on 16.04.2015.
 */
public class WeatherInfoActivity extends ActionBarActivity implements View.OnClickListener {
    Context context;
    TextView title, date, temp, pressure, humidity, weather0, wind;
    ProgressBar progress;
    ImageView icon;
    Button sharebut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_info);
        progress = (ProgressBar) findViewById(R.id.progress);
        title = (TextView) findViewById(R.id.title);
        date = (TextView) findViewById(R.id.date);
        temp = (TextView) findViewById(R.id.temp);
        pressure = (TextView) findViewById(R.id.pressure);
        humidity = (TextView) findViewById(R.id.humidity);
        weather0 = (TextView) findViewById(R.id.weather0);
        wind = (TextView) findViewById(R.id.wind);
        icon = (ImageView) findViewById(R.id.icon);
        sharebut = (Button) findViewById(R.id.share);
        sharebut.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("city_name")!= null)
            title.setText(bundle.getString("city_name"));

        if(bundle.getString("item")!= null) {
            try {
                JSONObject item = new JSONObject(bundle.getString("item"));
                JSONObject main = item.getJSONObject("main");

                Timestamp stamp = new Timestamp(item.getLong("dt") * 1000);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MMMM.dd HH:mm");
                Date dateStr = new Date(stamp.getTime());
                date.setText(dateFormat.format(dateStr));

                Long tempVal = Math.round(main.getDouble("temp") - 273.15);
                temp.setText("Температура: " + ((tempVal > 0) ? "+" : "") + tempVal.toString());

                pressure.setText("Давление: " + main.getString("pressure") + " мм рт.ст.");
                humidity.setText("Влажность: " + main.getString("humidity") + "%");

                JSONArray weather = item.getJSONArray("weather");
                JSONObject weather0obj = weather.getJSONObject(0);
                String iconCode = weather0obj.getString("icon");
                Picasso.with(context).load("http://openweathermap.org/img/w/" +iconCode+ ".png").into(icon);
                weather0.setText(weather0obj.getString("main") + ", " + weather0obj.getString("description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.share:
                Intent It = new Intent();
                It.setAction(Intent.ACTION_SEND);
                It.setType("text/plain");

                String text = "Прогноз погоды в городе " + title.getText() + " на " + date.getText() + ": " + temp.getText() + ", " + pressure.getText() + ", " + humidity.getText() + ", " + weather0.getText();


                It.putExtra(android.content.Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(It, "Поделиться прогнозом"));
                break;
        }
    }
}
