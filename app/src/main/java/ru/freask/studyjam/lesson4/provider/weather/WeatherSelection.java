package ru.freask.studyjam.lesson4.provider.weather;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import ru.freask.studyjam.lesson4.provider.base.AbstractSelection;

/**
 * Selection for the {@code weather} table.
 */
public class WeatherSelection extends AbstractSelection<WeatherSelection> {
    @Override
    protected Uri baseUri() {
        return WeatherColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code WeatherCursor} object, which is positioned before the first entry, or null.
     */
    public WeatherCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new WeatherCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null)}.
     */
    public WeatherCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null)}.
     */
    public WeatherCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }


    public WeatherSelection id(long... value) {
        addEquals("weather." + WeatherColumns._ID, toObjectArray(value));
        return this;
    }

    public WeatherSelection date(Date... value) {
        addEquals(WeatherColumns.DATE, value);
        return this;
    }

    public WeatherSelection dateNot(Date... value) {
        addNotEquals(WeatherColumns.DATE, value);
        return this;
    }

    public WeatherSelection date(long... value) {
        addEquals(WeatherColumns.DATE, toObjectArray(value));
        return this;
    }

    public WeatherSelection dateAfter(Date value) {
        addGreaterThan(WeatherColumns.DATE, value);
        return this;
    }

    public WeatherSelection dateAfterEq(Date value) {
        addGreaterThanOrEquals(WeatherColumns.DATE, value);
        return this;
    }

    public WeatherSelection dateBefore(Date value) {
        addLessThan(WeatherColumns.DATE, value);
        return this;
    }

    public WeatherSelection dateBeforeEq(Date value) {
        addLessThanOrEquals(WeatherColumns.DATE, value);
        return this;
    }

    public WeatherSelection temp(String... value) {
        addEquals(WeatherColumns.TEMP, value);
        return this;
    }

    public WeatherSelection tempNot(String... value) {
        addNotEquals(WeatherColumns.TEMP, value);
        return this;
    }

    public WeatherSelection tempLike(String... value) {
        addLike(WeatherColumns.TEMP, value);
        return this;
    }

    public WeatherSelection tempContains(String... value) {
        addContains(WeatherColumns.TEMP, value);
        return this;
    }

    public WeatherSelection tempStartsWith(String... value) {
        addStartsWith(WeatherColumns.TEMP, value);
        return this;
    }

    public WeatherSelection tempEndsWith(String... value) {
        addEndsWith(WeatherColumns.TEMP, value);
        return this;
    }
}
