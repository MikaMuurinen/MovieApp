//https://www.andruni.com/picasso/

package com.example.getapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent intent = getIntent();
        DataStorage storage = (DataStorage)intent.getSerializableExtra("datastorageobject");
        ArrayList<String> moviescenes = new ArrayList<>();

        final ListView list = findViewById(R.id.list);
        ImageButton settingsButton = (ImageButton) findViewById(R.id.backbutton_logo);
        ImageView movieImage = (ImageView)  findViewById(R.id.moviepicture);
        TextView movieInfo = (TextView) findViewById(R.id.movieinformation); 

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, moviescenes);
        list.setAdapter(arrayAdapter);

        String URL = null, moviedetails = null;
        
        for (int i=0;i<storage.moviesAll.size();i++) {

            if (storage.moviesAll.get(i).compareTo(storage.movietitle) == 0) {
                System.out.println("ok");
                moviescenes.add(storage.moviesAll.get(i)+"\n"+storage.moviestart.get(i)+"\n"+storage.movieends.get(i)+"\n"+storage.theatre.get(i));
                URL = storage.movieimage.get(i);
                moviedetails = storage.movieinfo.get(i);
            }
        }
        
        movieInfo.setText(moviedetails);

        if (URL!=null) {
            URL = URL.replace("http", "https");
            Picasso.get().load(URL).into(movieImage);
        }
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
