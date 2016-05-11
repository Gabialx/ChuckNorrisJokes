package eu.universes.android.chucknorrisjokes;

import java.util.List;

/**
 * Created by Gabriela on 11.5.2016
 */
public class Joke {

    private Long id;
    private String joke;
    private List<String> categories;

    public Joke(Long id, String joke, List<String> categories) {
        this.id = id;
        this.joke = joke;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public String getJoke() {
        return joke;
    }

    public List<String> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Long.toString(id)).append(", ").append(joke).append(", [");
        for (String c : categories) {
            builder.append(c).append(",");
        }
        builder.append("]");
        return builder.toString();
    }
}
