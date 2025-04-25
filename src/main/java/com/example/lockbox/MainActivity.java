package com.example.passwordmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText etService, etPassword;
    Button btnSave, btnRetrieve;
    TextView tvResult;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create LinearLayout
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Convert dp to px for consistent padding across devices
        int paddingInDp = 24;
        int paddingInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                paddingInDp,
                getResources().getDisplayMetrics()
        );
        layout.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);

        // LayoutParams with bottom margin
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, paddingInPx / 2); // margin bottom

        // Service input
        etService = new EditText(this);
        etService.setHint("Service (e.g., Gmail)");
        etService.setLayoutParams(params);
        layout.addView(etService);

        // Password input
        etPassword = new EditText(this);
        etPassword.setHint("Password");
        etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPassword.setLayoutParams(params);
        layout.addView(etPassword);

        // Save Button
        btnSave = new Button(this);
        btnSave.setText("Save Password");
        btnSave.setLayoutParams(params);
        layout.addView(btnSave);

        // Retrieve Button
        btnRetrieve = new Button(this);
        btnRetrieve.setText("Retrieve Password");
        btnRetrieve.setLayoutParams(params);
        layout.addView(btnRetrieve);

        // Result TextView
        tvResult = new TextView(this);
        tvResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tvResult.setLayoutParams(params);
        layout.addView(tvResult);

        // Set content view
        setContentView(layout);

        // SharedPreferences
        sharedPreferences = getSharedPreferences("PasswordManagerPrefs", Context.MODE_PRIVATE);

        // Save logic
        btnSave.setOnClickListener(v -> {
            String service = etService.getText().toString();
            String password = etPassword.getText().toString();
            if (!service.isEmpty() && !password.isEmpty()) {
                String encodedPassword = Base64.encodeToString(password.getBytes(), Base64.DEFAULT);
                sharedPreferences.edit().putString(service, encodedPassword).apply();
                Toast.makeText(this, "Password saved.", Toast.LENGTH_SHORT).show();
                etPassword.setText("");
            } else {
                Toast.makeText(this, "Please enter service and password.", Toast.LENGTH_SHORT).show();
            }
        });

        // Retrieve logic
        btnRetrieve.setOnClickListener(v -> {
            String service = etService.getText().toString();
            if (!service.isEmpty()) {
                String encodedPassword = sharedPreferences.getString(service, null);
                if (encodedPassword != null) {
                    String password = new String(Base64.decode(encodedPassword, Base64.DEFAULT));
                    tvResult.setText("Password for " + service + ": " + password);
                } else {
                    tvResult.setText("No password found for this service.");
                }
            } else {
                Toast.makeText(this, "Enter a service name.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
