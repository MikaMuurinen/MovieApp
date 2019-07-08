//https://www.dev2qa.com/passing-data-between-activities-android-tutorial/

package com.example.getapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;

public class CalendarActivity extends AppCompatActivity  {
    CalendarView calendarView;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ImageButton dateButton = (ImageButton) findViewById(R.id.backbutton_logo);
        calendarView = (CalendarView) findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                i1++;
                String day = Integer.toString(i2);
                String month= Integer.toString(i1);
                if (i2<10) day="0"+i2;
                if (i1<10) month="0"+i1;
                selectedDate = day+"."+month+"."+i;
           //     Toast.makeText(getApplicationContext(), "Selected Date:\n" + "Day = " + i2 + "\n" + "Month = " + i1 + "\n" + "Year = " + i, Toast.LENGTH_LONG).show();
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedDate != null) {
                Intent intent = new Intent();
                intent.putExtra("message_return", selectedDate);
                setResult(RESULT_OK, intent);
                finish();
            } else
                {Toast.makeText(getApplicationContext(), "Valitse päivä", Toast.LENGTH_LONG).show();}
            }
        });
    }
}
