package galos.thegalos.doghotel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);
        final EditText etName = findViewById(R.id.etAge);
        final Button btnStart = findViewById(R.id.btnStart);
        final Button ivContact = findViewById(R.id.btn);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                if (etName.getText().toString().length() == 0)
                    btnStart.setEnabled(false);
                else
                    btnStart.setEnabled(true);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                Intent intent = new Intent(MainMenu.this, Booking.class);
                intent.putExtra("owner name", name);
                startActivity(intent);
                finish();
            }
        });

        ivContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Contact.class);
                startActivity(intent);
            }
        });
    }
}