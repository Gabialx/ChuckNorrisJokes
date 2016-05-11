package eu.universes.android.chucknorrisjokes;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriela on 11.5.2016
 */
public class InternetService {

    private Activity activity;

    public InternetService(Activity activity) {
        this.activity = activity;
    }

    public JokesResult jokes() {

        try {
            if (isNetworkAvailable()) {
                URL url = new URL("http://api.icndb.com/jokes");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();

                return jokes(inputStream);
            } else {
                return new JokesResult(false, null, "There is no available network.");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new JokesResult(false, null, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return new JokesResult(false, null, e.getMessage());
        }
    }

    public JokesResult jokes(InputStream inputStream) {
        try {
            JsonReader  reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

            return jokes(reader);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new JokesResult(false, null, "Bad result.");
        } catch (IOException e) {
            e.printStackTrace();
            return new JokesResult(false, null, "Bad result.");
        }
    }

    public JokesResult jokes(JsonReader reader) throws IOException {
        boolean success = true;
        List<Joke> jokes = new ArrayList<>();

        // Reading the result.
        reader.beginObject();
        while (reader.hasNext()) {
            String resultProperty = reader.nextName();
            if (resultProperty.equals("type")) {
                success = "success".equals(reader.nextString());
            } else if (resultProperty.equals("value") && reader.peek() != JsonToken.NULL) {
                // Reading the jokes list.
                reader.beginArray();
                while (reader.hasNext()) {
                    Long id = null;
                    String joke = null;
                    List<String> categories = null;

                    reader.beginObject();
                    while (reader.hasNext()) {
                        String jokeProperty = reader.nextName();
                        if (jokeProperty.equals("id")) {
                            id = reader.nextLong();
                        } else if (jokeProperty.equals("joke")) {
                            joke = reader.nextString();
                        } else if (jokeProperty.equals("categories") && reader.peek() != JsonToken.NULL) {
                            categories = new ArrayList<>();

                            reader.beginArray();
                            while (reader.hasNext()) {
                                categories.add(reader.nextString());
                            }
                            reader.endArray();
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();

                    Joke j = new Joke(id, joke, categories);
                    jokes.add(j);
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new JokesResult(success, jokes, null);
    }

    public void init() {
        setupPolicies();
    }

    private void setupPolicies() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
