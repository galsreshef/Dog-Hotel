package galos.thegalos.doghotel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

public class Booking extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivPicture;
    private EditText etAge;
    private EditText etName;
    private EditText etPhone;
    private EditText etEmail;
    private final int CAMERA_REQUEST = 1;
    private TextView tvClock;
    private TextView tvDate;
    private Bitmap bitmap = null;
    int hourSelected, minuteSelected, daySelected, monthSelected, yearSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_activity);

        tvClock = findViewById(R.id.tvClock);
        ImageView ivClock = findViewById(R.id.ivClock);
        ImageView ivCalendar = findViewById(R.id.ivCalendar);
        ivPicture = findViewById(R.id.ivPicture);
        Button btnFinish = findViewById(R.id.btnFinish);
        ImageView ivCamera = findViewById(R.id.ivCamera);
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        tvDate = findViewById(R.id.tvCalendar);
        TextView tvDetails = findViewById(R.id.tvDetails);

        ivCalendar.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        ivClock.setOnClickListener(this);
        ivCalendar.setOnClickListener(this);
        ivCamera.setOnClickListener(this);

        String str = getIntent().getStringExtra("owner name") + " ," + getResources().getString(R.string.register_your_dog);
        tvDetails.setText(str);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            ivPicture.setImageBitmap(bitmap);

            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                FileOutputStream fo = openFileOutput("dogPhoto", Context.MODE_PRIVATE);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Booking.this);
        builder.setTitle(R.string.sure);
        builder.setMessage(R.string.are_you_sure);
        builder.setCancelable(true);
        builder.setNegativeButton("כן", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Booking.this, MainMenu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        builder.setPositiveButton("לא", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnFinish:
                if (tvClock.getText().toString().equals("") || tvDate.getText().toString().equals("") ||
                        etName.getText().toString().equals("") || etAge.getText().toString().equals("") ||
                        etPhone.getText().toString().equals("") || etEmail.getText().toString().equals("") || bitmap == null) {
                    Toast.makeText(Booking.this, getResources().getString(R.string.missing_input), Toast.LENGTH_SHORT).show();
                    break;
                }
                intent = new Intent(Booking.this, SummaryActivity.class);
                intent.putExtra("dog name", etName.getText().toString());
                intent.putExtra("dog age", etAge.getText().toString());
                intent.putExtra("owner phone", etPhone.getText().toString());
                intent.putExtra("owner email", etEmail.getText().toString());
                intent.putExtra("hour", hourSelected);
                intent.putExtra("minute", minuteSelected);
                intent.putExtra("day", minuteSelected);
                intent.putExtra("month", minuteSelected);
                intent.putExtra("year", minuteSelected);
                startActivity(intent);
                break;

            case R.id.ivCamera:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
                break;

            case R.id.ivClock:
                Calendar calendar1 = Calendar.getInstance();
                int currentHour = calendar1.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(Booking.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hourSelected = hourOfDay;
                        minuteSelected = minute;
                        if (minute > 9) {
                            if (hourOfDay > 9) {
                                String time = hourOfDay + ":" + minute;
                                tvClock.setText(time);
                            } else {
                                String time = "0" + hourOfDay + ":" + minute;
                                tvClock.setText(time);
                            }
                        } else {
                            if (hourOfDay > 9) {
                                String time = hourOfDay + ":0" + minute;
                                tvClock.setText(time);
                            } else {
                                String time = "0" + hourOfDay + ":0" + minute;
                                tvClock.setText(time);
                            }
                        }
                    }
                }, currentHour, currentMinute, true);
                timePickerDialog.setTitle("Select time:");
                timePickerDialog.show();
                break;

            case R.id.ivCalendar:
                final Calendar calendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        daySelected = dayOfMonth;
                        monthSelected = month;
                        yearSelected = year;

                        tvDate.setText((new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())).format(calendar.getTime()));
                    }
                };

                DatePickerDialog dp = new DatePickerDialog(Booking.this, datePickerDialog,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dp.getDatePicker().setMinDate(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
                dp.show();
                break;
        }
    }
}