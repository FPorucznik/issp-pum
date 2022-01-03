package com.example.studentcrimeapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.studentcrimeapp.databinding.ActivityCrimeBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

    private ActivityCrimeBinding binding;
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int CAMERA_INTENT = 2;

    private Uri savePicturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrimeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        //setContentView(R.layout.activity_crime);
        setContentView(view);

        binding.addPhotoButton.setOnClickListener(v -> {
            activateCamera();
        });

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
        if(current.getPicture() != null){
            binding.crimeImageView.setImageURI(Uri.parse(current.getPicture()));
        }

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

    private void activateCamera() {
        Dexter.withContext(this).withPermission(
                Manifest.permission.CAMERA
        ).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_INTENT);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                showRationaleDialog();
            }
        }).onSameThread().check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA_INTENT){
                Bitmap image = (Bitmap)data.getExtras().get("data");
                binding.crimeImageView.setImageBitmap(image);
                savePicturePath = savePicture(image);
                MainActivity.dbHandler.updateCrimeImage(current.getId(), savePicturePath.toString());
            }
        }
    }

    private void showRationaleDialog(){
        new AlertDialog.Builder(this)
                .setMessage("This feature requires camera permission")
                .setPositiveButton("Ask me", (dialog, which) -> {
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                    catch (ActivityNotFoundException e){
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private Uri savePicture(Bitmap bitmap){
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir("myGallery", Context.MODE_PRIVATE);
        file = new File(file, UUID.randomUUID().toString() + ".jpg");

        try {
            OutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return Uri.parse(file.getAbsolutePath());
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
