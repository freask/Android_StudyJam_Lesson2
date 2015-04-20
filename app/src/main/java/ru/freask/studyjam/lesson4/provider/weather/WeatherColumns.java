package ru.freask.studyjam.lesson4.provider.weather;

import android.net.Uri;
import android.provider.BaseColumns;

import ru.freask.studyjam.lesson4.provider.WeatherContentProvider;
import ru.freask.studyjam.lesson4.provider.weather.WeatherColumns;

/**
 * the weather items
 */
public class WeatherColumns implements BaseColumns {
    public static final String TABLE_NAME = "weather";
    public static final Uri CONTENT_URI = Uri.parse(WeatherContentProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String DATE = "date";

    public static final String TEMP = "temp";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            DATE,
            TEMP
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(DATE) || c.contains("." + DATE)) return true;
            if (c.equals(TEMP) || c.contains("." + TEMP)) return true;
        }
        return false;
    }

}
