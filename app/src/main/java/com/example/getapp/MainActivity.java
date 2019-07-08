//https://www.tutlane.com/tutorial/android/android-xml-parsing-using-sax-parser
//https://androidexample.com/Create_A_Simple_Listview_-_Android_Example/index.php?view=article_discription&aid=65
//https://abhiandroid.com/ui/arrayadapter-tutorial-example.html

package com.example.getapp;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.lang.String;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Response";
    private final static int REQUEST_CODE = 1;
    private DataStorage storage;
    RestAndParse parse = new RestAndParse();
    Button date;
    ListView listView;
    Spinner spinner;

    ArrayAdapter<String> adapter;
    private int listposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        spinner=(Spinner)findViewById(R.id.spinner);
        date = (Button) findViewById(R.id.button_date);

        storage = new DataStorage();;

        parse.RequestTheatres(storage);
        parse.requestData(storage);

        date.setText(storage.currentdate);

        // ------------------Adapters ----------------

        adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_listview, R.id.textView, storage.movies);
        listView.setAdapter(adapter);

        final ArrayAdapter<String>arrayAdapter=new ArrayAdapter<String>(this,R.layout.spinner_item, storage.theatreName);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(arrayAdapter);

        // ------------------LISTENERS ----------------

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);
                    storage.cityposition = position;
                    storage.clearContent();
                    parse.requestData(storage);
                    adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchCalendar();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, storage.movies.get(position)+"", Toast.LENGTH_SHORT).show();
                listposition = position;
                storage.movietitle=storage.movies.get(listposition);
                launchActivity();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);

        // The returned result data is identified by requestCode.
        // The request code is specified in startActivityForResult(intent, REQUEST_CODE_1); method.
        switch (requestCode)
        {
            // This request code is set by startActivityForResult(intent, REQUEST_CODE_1) method.
            case REQUEST_CODE:
                if(resultCode == RESULT_OK)
                {
                    String messageReturn = dataIntent.getStringExtra("message_return");
                  //  Toast.makeText(MainActivity.this, messageReturn+"", Toast.LENGTH_SHORT).show();
                    date.setText(messageReturn);
                    storage.currentdate = messageReturn;
                    storage.clearContent();
                    parse.requestData(storage);
                    adapter.notifyDataSetChanged();
                }
        }
    }

    private void launchCalendar() {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void launchActivity() {
        Intent intent = new Intent(this, MovieActivity.class);
      //  intent.putExtra("message", storage.movies.get(listposition));
        intent.putExtra("datastorageobject", storage);
        startActivity(intent);
    }

}



