package ru.freask.studyjam.lesson4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Alexander.Kashin01 on 13.04.2015.
 */
public class WeatherAdapter extends ArrayAdapter<JSONObject> {
    Context context;
    static String prevDate = "";
    static int[] withDate;

    public WeatherAdapter(Context context, List<JSONObject> photos) {
        super(context, -1, -1, photos);
        this.context = context;
    }

    static class ViewHolder{
        TextView text;
        TextView date_title;
        ImageView icon;

        void populateItem(JSONObject json, Context context, int position) {
            try {
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
            }
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.weather_item, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.date_title = (TextView) convertView.findViewById(R.id.date_title);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        JSONObject json = getItem(position);
        holder.populateItem(json, context, position);
        return convertView;
    }
}
