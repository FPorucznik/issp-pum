package com.example.studentcrimeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CrimeActivity extends AppCompatActivity {

    private UUID crimeId;
    private String test;
    private EditText editText;
    private Button dateButton;
    private Button removeButton;
    private CheckBox solvedCheckBox;
    private Crime current;
    private DateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
    private Date editedDate;
    private int mMonth;
    private int mYear;
    private int mDay;
    final Calendar c = Calendar.getInstance();
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);

        editText = findViewById(R.id.editText);
        dateButton = findViewById(R.id.dateButton);
        removeButton = findViewById(R.id.removeCrimeButton);
        solvedCheckBox = findViewById(R.id.solvedCheckBox);

        crimeId = UUID.fromString(getIntent().getStringExtra("id"));
        current = CrimeLab.get(this).getCrime(crimeId);
        index = CrimeLab.get(this).getCrimes().indexOf(current);
        editText.setText(current.getTitle());
        dateButton.setText(df.format(current.getDate()));
        solvedCheckBox.setChecked(current.getSolved());

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                current.setTitle(editText.getText().toString());
                MainActivity.crimeToUpdate = current;
            }
        });

        solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                current.setSolved(solvedCheckBox.isChecked());
                MainActivity.crimeToUpdate = current;
            }
        });

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }

    public void showDateDialog(View view) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                showTimeDialog(year, monthOfYear, dayOfMonth);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void showTimeDialog(int yearParam, int monthParam, int dayParam){
        final Calendar time = Calendar.getInstance();
        int mHour = time.get(Calendar.HOUR_OF_DAY);
        int mMinute = time.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                c.set(yearParam, monthParam, dayParam, hourOfDay, minute);
                editedDate = c.getTime();
                dateButton.setText(df.format(editedDate));
                current.setDate(editedDate);
                MainActivity.crimeToUpdate = current;
            }
            }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    public void removeCrime(View view) {
        MainActivity.dbHandler.deleteCrime(current);
        finish();
    }
}
