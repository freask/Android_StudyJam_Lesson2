package ru.freask.studyjam.lesson4.provider.weather;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.freask.studyjam.lesson4.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code weather} table.
 */
public class WeatherCursor extends AbstractCursor implements WeatherModel {
    public WeatherCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(WeatherColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code date} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public Date getDate() {
        Date res = getDateOrNull(WeatherColumns.DATE);
        if (res == null)
            throw new NullPointerException("The value of 'date' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code temp} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getTemp() {
        String res = getStringOrNull(WeatherColumns.TEMP);
        if (res == null)
            throw new NullPointerException("The value of 'temp' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code icon_code} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getIconCode() {
        String res = getStringOrNull(WeatherColumns.ICON_CODE);
        if (res == null)
            throw new NullPointerException("The value of 'icon_code' in the database was null, which is not allowed according to the model definition");
        return res;
    }
}
