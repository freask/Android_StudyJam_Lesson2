package ru.freask.studyjam.lesson4.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import ru.freask.studyjam.lesson4.BuildConfig;
import ru.freask.studyjam.lesson4.provider.weather.WeatherColumns;

public class SampleSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = SampleSQLiteOpenHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;
    private static SampleSQLiteOpenHelper sInstance;
    private final Context mContext;
    private final SampleSQLiteOpenHelperCallbacks mOpenHelperCallbacks;

    // @formatter:off
    public static final String SQL_CREATE_TABLE_WEATHER = "CREATE TABLE IF NOT EXISTS "
            + WeatherColumns.TABLE_NAME + " ( "
            + WeatherColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + WeatherColumns.DATE + " INTEGER NOT NULL, "
            + WeatherColumns.TEMP + " TEXT NOT NULL "
            + " );";

    public static final String SQL_CREATE_INDEX_WEATHER_TEMP = "CREATE INDEX IDX_WEATHER_TEMP "
            + " ON " + WeatherColumns.TABLE_NAME + " ( " + WeatherColumns.TEMP + " );";

    // @formatter:on

    public static SampleSQLiteOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static SampleSQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */
    private static SampleSQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new SampleSQLiteOpenHelper(context);
    }

    private SampleSQLiteOpenHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mOpenHelperCallbacks = new SampleSQLiteOpenHelperCallbacks();
    }


    /*
     * Post Honeycomb.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static SampleSQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new SampleSQLiteOpenHelper(context, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private SampleSQLiteOpenHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new SampleSQLiteOpenHelperCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_WEATHER);
        db.execSQL(SQL_CREATE_INDEX_WEATHER_TEMP);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
