package com.example.ianalonzo.tourup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class LearnMoreActivity extends AppCompatActivity {

    private String landmarkName;
    private String landmarkTrivia;
    private String landmarkHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_more);

        Bundle extras = getIntent().getExtras();
        landmarkName = extras.getString("Name");
        landmarkHistory = extras.getString("History");
        landmarkTrivia = extras.getString("Trivia");

        /*final ImageView image = (ImageView) findViewById(R.id.learnmore_image);
        image.setBackgroundResource(R.drawable.carabaopark);*/

        final TextView name = (TextView) findViewById(R.id.name_learnmore);
        name.setText(landmarkName);

        final TextView hist = (TextView) findViewById(R.id.history_learnmore);
        hist.setText(landmarkHistory);

        final TextView triv = (TextView) findViewById(R.id.trivia_learnmore);
        triv.setText(landmarkTrivia);

    }
}
