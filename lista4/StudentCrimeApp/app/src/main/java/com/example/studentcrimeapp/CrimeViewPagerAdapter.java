package com.example.studentcrimeapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CrimeViewPagerAdapter extends RecyclerView.Adapter<CrimeViewPagerAdapter.ViewHolder> {

    private Context context;
    private List<Crime> crimes;
    private DateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
    private int mMonth;
    private int mYear;
    private int mDay;
    private Crime current;
    private Date editedDate;
    private Button dateButton;
    private int index;
    final Calendar c = Calendar.getInstance();

    public CrimeViewPagerAdapter(Context context){
        this.crimes = CrimeLab.get(context).getCrimes();
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private EditText editText;
        private Button removeButton;
        private CheckBox solvedCheckBox;
        private Button firstButton;
        private Button lastButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            editText = itemView.findViewById(R.id.editText);
            dateButton = itemView.findViewById(R.id.dateButton);
            removeButton = itemView.findViewById(R.id.removeCrimeButton);
            solvedCheckBox = itemView.findViewById(R.id.solvedCheckBox);
            firstButton = itemView.findViewById(R.id.firstButton);
            lastButton = itemView.findViewById(R.id.lastButton);
        }

        public void bind(Crime currentCrime){
            current = currentCrime;
            index = crimes.indexOf(current);

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
                    CrimeLab.get(context).updateCrime(index, current);
                }
            });

            solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    current.setSolved(solvedCheckBox.isChecked());
                    CrimeLab.get(context).updateCrime(index, current);
                }
            });
        }
    }

    public void showDateDialog(View view) {
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                c.set(yearParam, monthParam, dayParam, hourOfDay, minute);
                editedDate = c.getTime();
                dateButton.setText(df.format(editedDate));
                current.setDate(editedDate);
                CrimeLab.get(context).updateCrime(index, current);
            }
        }, mHour, mMinute, true);
        timePickerDialog.show();
    }


    public void removeCrime(View view) {
        CrimeLab.get(context).removeCrime(current);
        MainActivity.crimeListAdapter.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CrimeViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_crime, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CrimeViewPagerAdapter.ViewHolder holder, int position) {
        Crime activeCrime = crimes.get(position);
        holder.bind(activeCrime);
    }

    @Override
    public int getItemCount() {
        return crimes.size();
    }
}
