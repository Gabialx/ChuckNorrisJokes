package eu.universes.android.chucknorrisjokes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gabriela on 12.5.2016
 */
public class DatabaseService extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "jokes.db";

    public static String SQL_CREATE = "CREATE TABLE JOKES (ID INTEGER PRIMARY KEY, JOKE TEXT NOT NULL);" +
            "CREATE TABLE CATEGORIES (JOKE_ID INTEGER NOT NULL, CATEGORY TEXT NOT NULL);";

    public static String SQL_READ_ALL = "SELECT J.ID, J.JOKE, C.CATEGORY FROM JOKES J JOIN CATEGORY C ON C.JOKE_ID = J.ID ORDER BY J.ID, C.CATEGORY";

    public DatabaseService(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void persist(JokesResult jokesResult) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Joke joke : jokesResult.getJokes()) {
            ContentValues jokeValues = new ContentValues();
            jokeValues.put("ID", joke.getId());
            jokeValues.put("JOKE", joke.getJoke());
            long joke_id = db.insert("JOKES", null, jokeValues);

            for (String category : joke.getCategories()) {
                ContentValues categoryValues = new ContentValues();
                categoryValues.put("JOKE_ID", joke_id);
                categoryValues.put("CATEGORY", category);
                long category_id = db.insert("CATEGORIES", null, categoryValues);
            }
        }
    }

    public Collection<Joke> readAllJokes() {
        Map<Long, Joke> jokes = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(SQL_READ_ALL, null);
        try {
            if (c.moveToFirst()) {
                do {
                    Long id = c.getLong(0);
                    String joke = c.getString(1);
                    String category = c.getString(2);

                    Joke j = jokes.get(id);
                    if (j != null) {
                        j.getCategories().add(category);
                    } else {
                        List<String> categories = new ArrayList<>();
                        categories.add(category);
                        j = new Joke(id, joke, categories);
                        jokes.put(id, j);
                    }
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return jokes.values();
    }
}