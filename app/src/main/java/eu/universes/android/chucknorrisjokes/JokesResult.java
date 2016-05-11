package eu.universes.android.chucknorrisjokes;

import java.util.List;

/**
 * Created by Gabriela on 11.5.2016
 */
public class JokesResult {
    private boolean success;
    private List<Joke> jokes;
    private String error;

    public JokesResult(boolean success, List<Joke> jokes, String error) {
        this.success = success;
        this.jokes = jokes;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<Joke> getJokes() {
        return jokes;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("success: ").append(Boolean.toString(success)).append("\n");
        if (success) {
            builder.append("jokes: ").append("\n");
            for (Joke j : jokes) {
                builder.append("\t").append(j.toString()).append("\n");
            }
        }else {
            builder.append("error: ").append(error).append("\n");
        }
        return builder.toString();
    }
}
