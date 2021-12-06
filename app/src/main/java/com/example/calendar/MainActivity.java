package com.example.calendar;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    EditText date,time,dateTime;
    Button submit;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        date =findViewById(R.id.date);
        time =findViewById(R.id.time);
        dateTime =findViewById(R.id.datetime);
        submit = findViewById(R.id.submit);

        date.setInputType(InputType.TYPE_NULL);
        time.setInputType(InputType.TYPE_NULL);
        dateTime.setInputType(InputType.TYPE_NULL);

        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDateDialog(date);
            }
        });

        time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showTimeDialog(time);
            }
        });

        dateTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDateTimeDialog(dateTime);
            }
        });

        createNotificationChannnel();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Reminder Set!",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this,ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                long timeAtButtonClick =System.currentTimeMillis();
                long tenSecondInMills= 1000 * 10;

                System.out.println("Calendar Time====================================="+calendar.getTimeInMillis());
                alarmManager.set(AlarmManager.RTC_WAKEUP, /*timeAtButtonClick+tenSecondInMills*/calendar.getTimeInMillis() , pendingIntent);
            }
        });

    }

    private void createNotificationChannnel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "Pintu Kumar Channel";
            String description = "Just a demo ";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Notify Pintu", name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showDateTimeDialog(EditText dateTime) {
        calendar= Calendar.getInstance();
        DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay-12);
                        calendar.set(Calendar.MINUTE,minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        dateTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(MainActivity.this,timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),false).show();
            }
        };
        new DatePickerDialog(MainActivity.this,datePickerDialog,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.HOUR_OF_DAY)).show();
    }

    private void showTimeDialog(EditText time) {
        final Calendar calendar= Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay-12);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                time.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new TimePickerDialog(MainActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false).show();

    }

    private void showDateDialog(EditText date) {
        final Calendar calendar= Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH,month);
                    calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    date.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new DatePickerDialog(MainActivity.this,dateSetListener,calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}