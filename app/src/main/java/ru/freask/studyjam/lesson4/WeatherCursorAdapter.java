package ru.freask.studyjam.lesson4;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ru.freask.studyjam.lesson4.provider.weather.WeatherColumns;

/**
 * Created by Alexander.Kashin01 on 13.04.2015.
 */
public class WeatherCursorAdapter extends CursorAdapter {
    public WeatherCursorAdapter(Context context) {
        super(context, null, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.weather_item, viewGroup, false);
        WeatherViewHolder h = new WeatherViewHolder(v);
        v.setTag(h);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        WeatherViewHolder h = (WeatherViewHolder) view.getTag();

        Picasso.with(context).load("http://openweathermap.org/img/w/" +cursor.getString(cursor.getColumnIndex(WeatherColumns.ICON_CODE))+ ".png").into(h.icon);
        h.populateItem(cursor);
    }

    static class WeatherViewHolder{
        TextView text;
        TextView date_title;
        ImageView icon;

        public WeatherViewHolder(View v) {
            text = (TextView) v.findViewById(R.id.text);
            date_title = (TextView) v.findViewById(R.id.date_title);
            icon = (ImageView) v.findViewById(R.id.icon);
        }

        void populateItem(Cursor cursor) {
            text.setText(cursor.getString(cursor.getColumnIndex(WeatherColumns.TEMP)));
            date_title.setText(cursor.getString(cursor.getColumnIndex(WeatherColumns.DATE)));

            /*try {
                JSONObject main = json.getJSONObject("main");
                String day_date = json.getString("day_date");
                String date = json.getString("date");
                JSONArray weather = json.getJSONArray("weather");
                JSONObject weather0 = weather.getJSONObject(0);
                Long temp = Math.round(main.getDouble("temp") - 273.15);

                if (day_date.equals("")) {
                    date_title.setVisibility(View.GONE);
                } else {
                    date_title.setVisibility(View.VISIBLE);
                    date_title.setText(day_date);
                }

                text.setText(date + ": " + ((temp > 0) ? "+" : "") + temp.toString());
                String iconCode = weather0.getString("icon");
                Picasso.with(context).load("http://openweathermap.org/img/w/" +iconCode+ ".png").into(icon);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
    }
}
