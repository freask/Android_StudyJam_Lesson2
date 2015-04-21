package ru.freask.studyjam.lesson4.controller;

import android.content.Context;

import ru.freask.studyjam.lesson4.WeatherAsyncTask;

/**
 * Created by Alexander.Kashin01 on 21.04.2015.
 */
public class ForecastController {
    private final Context context;

    public ForecastController(Context c) {
        this.context = c;
    }

    public void refreshForecast() {
        new WeatherAsyncTask(context).execute();
    }
}
