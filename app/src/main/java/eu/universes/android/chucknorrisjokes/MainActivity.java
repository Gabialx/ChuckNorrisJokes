package eu.universes.android.chucknorrisjokes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    private InternetService service = new InternetService(this);

    private DatabaseService databaseService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseService = new DatabaseService(getApplicationContext());

        textView = (TextView) findViewById(R.id.textView);

        service.init();
        JokesResult jokesResult = service.jokes();
        databaseService.persist(jokesResult);

        if (jokesResult.isSuccess()) {
            textView.setText(jokesResult.toString());
        } else {
            textView.setText(jokesResult.getError());
        }
    }
}
