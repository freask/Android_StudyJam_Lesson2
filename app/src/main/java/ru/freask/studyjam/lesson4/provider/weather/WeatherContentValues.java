package ru.freask.studyjam.lesson4.provider.weather;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.freask.studyjam.lesson4.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code weather} table.
 */
public class WeatherContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return WeatherColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable WeatherSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public WeatherContentValues putDate(@NonNull Date value) {
        if (value == null) throw new IllegalArgumentException("date must not be null");
        mContentValues.put(WeatherColumns.DATE, value.getTime());
        return this;
    }


    public WeatherContentValues putDate(long value) {
        mContentValues.put(WeatherColumns.DATE, value);
        return this;
    }

    public WeatherContentValues putTemp(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("temp must not be null");
        mContentValues.put(WeatherColumns.TEMP, value);
        return this;
    }

}
