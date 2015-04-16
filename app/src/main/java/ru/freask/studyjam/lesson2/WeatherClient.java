package ru.freask.studyjam.lesson2;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Alexander.Kashin01 on 13.04.2015.
 */
public class WeatherClient {
    private static final String API_URL = "http://api.openweathermap.org/data/2.5";

    static class Weather {
        String weather;
        Object coord;
    }

    interface WeatherApi {
        @GET("/weather?q={city}")
        List<Weather> weathers(
                @Path("city") String city
        );
    }

    public static void main() {
        // Create a very simple REST adapter which points the GitHub API endpoint.
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();

        // Create an instance of our GitHub API interface.
        WeatherApi weather_api = restAdapter.create(WeatherApi.class);

        // Fetch and print a list of the contributors to this library.
        List<Weather> weathers = weather_api.weathers("Moscow,ru");
        for (Weather weather : weathers) {
            System.out.println(weather.weather + " (" + weather.coord + ")");
        }
    }
}
