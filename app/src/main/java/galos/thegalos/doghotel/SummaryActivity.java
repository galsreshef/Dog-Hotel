package galos.thegalos.doghotel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.Calendar;


public class SummaryActivity extends Activity {
    int hourSelected, minuteSelected, daySelected, monthSelected, yearSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);

        hourSelected = getIntent().getIntExtra("hour", 0);
        minuteSelected = getIntent().getIntExtra("minute", 0);
        daySelected = getIntent().getIntExtra("day", 1);
        monthSelected = getIntent().getIntExtra("month", 1);
        yearSelected = getIntent().getIntExtra("year", 2020);

        TextView tvDogName = findViewById(R.id.tvDogName);
        TextView tvDogAge = findViewById(R.id.tvDogAge);
        TextView tvOwnerPhone = findViewById(R.id.tvOwnerPhone);
        TextView tvOwnerEmail = findViewById(R.id.tvOwnerEmail);
        ImageView ivDogPicture = findViewById(R.id.ivDogPicture);
        Button btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryActivity.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(openFileInput("dogPhoto"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ivDogPicture.setImageBitmap(bitmap);

        String str = getResources().getString(R.string.dog_name) + ": " + getIntent().getStringExtra("dog name");
        tvDogName.setText(str);

        str = getResources().getString(R.string.dog_age) + ": " + getIntent().getStringExtra("dog age");
        tvDogAge.setText(str);

        str = getIntent().getStringExtra("owner phone");
        tvOwnerPhone.setText(str);

        str = getIntent().getStringExtra("owner email");
        tvOwnerEmail.setText(str);

        final Button add_event = findViewById(R.id.btnAddCalendar);
        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });
    }

    private void openCalendar() {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(yearSelected, monthSelected - 1, daySelected, hourSelected, minuteSelected);
        Calendar endTime = Calendar.getInstance();
        endTime.set(yearSelected, monthSelected - 1, daySelected, hourSelected + 1, minuteSelected);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, getString(R.string.app_name))
                .putExtra(CalendarContract.Events.DESCRIPTION, getString(R.string.meeting))
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Dog Hotel,+Holon+Israel")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(CalendarContract.Events.CALENDAR_COLOR, getResources().getColor(android.R.color.holo_red_light));//"#FF0000");
        startActivity(intent);
    }
}
