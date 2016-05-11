package eu.universes.android.chucknorrisjokes;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Gabriela on 11.5.2016
 */
public class InternetService {

    private Activity activity;

    public InternetService(Activity activity) {
        this.activity = activity;
    }

    public class JokesResult {
        private boolean success;
        private String message;
        private String error;

        public JokesResult(boolean success, String message, String error) {
            this.success = success;
            this.message = message;
            this.error = error;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public String getError() {
            return error;
        }
    }

    public JokesResult jokes() {

        try {
            if (isNetworkAvailable()) {
                URL url = new URL("http://api.icndb.com/jokes");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();

                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line).append('\n');
                }

                String result = total.toString();

                return new JokesResult(true, result, null);
            } else {
                return new JokesResult(false, null, "There is no available network.");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new JokesResult(false, e.getMessage(), null);
        } catch (IOException e) {
            e.printStackTrace();
            return new JokesResult(false, e.getMessage(), null);
        }
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
