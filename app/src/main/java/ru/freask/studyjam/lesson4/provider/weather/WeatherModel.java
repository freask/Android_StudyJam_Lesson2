package ru.freask.studyjam.lesson4.provider.weather;

import ru.freask.studyjam.lesson4.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * the weather items
 */
public interface WeatherModel extends BaseModel {

    /**
     * Get the {@code date} value.
     * Cannot be {@code null}.
     */
    @NonNull
    Date getDate();

    /**
     * Get the {@code temp} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getTemp();
}
