package com.example.calculatorforelectricbills;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    TextView answer;
    EditText editkwh;
    EditText editrebate;
    Button calculateButton;
    Button clearButton;
    Button saveButton;

    // Firebase database reference
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://calculator-for-electric-bills-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference("message");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        answer = findViewById(R.id.answer);
        editkwh = findViewById(R.id.editkwh);
        editrebate = findViewById(R.id.editrebate);
        calculateButton =  findViewById(R.id.calculateButton);
        clearButton = findViewById(R.id.clearButton);
        saveButton = findViewById(R.id.saveButton);

        //function for calculationButton to calculate the rebate
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Take input from editkwh and editrebate
                String inputkwh = editkwh.getText().toString();
                String inputrebate = editrebate.getText().toString();

                // Check if editkwh and editrebate is empty
                if (inputkwh.isEmpty() || inputrebate.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter BOTH KWh and Rebate values!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Convert the value to double
                double kwh = Double.parseDouble(inputkwh);
                double rebate = Double.parseDouble(inputrebate);

                // Calculate total charges
                double charge = 0.0;
                double totalCharges = 0.0;

                if (kwh <= 200) {
                    charge = kwh * 0.218;
                } else if (kwh > 200 && kwh <= 300) {
                    charge = ((kwh - 200) * 0.334) + (200 * 0.218);
                } else if (kwh > 300 && kwh <= 600) {
                    charge = ((kwh - 300) * 0.516) + (200 * 0.218) + (100 * 0.334);
                } else if (kwh > 600) {
                    charge = (kwh - 600) * 0.546 + (200 * 0.218) + (100 * 0.334) + (300 * 0.516);
                }
                totalCharges = charge - (charge * (rebate/100));

                // Display the result
                double roundedTotal = Math.round(totalCharges);
                answer.setText("RM" + Double.toString(roundedTotal));

            }
        });

        //clearButton is for reset the value inside
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                answer.setText("0.0");
                editkwh.setText("");
                editrebate.setText("");
            }

         });
        // Inside your onClickListener:
        saveButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
             // Capture user input
             String inputkwh = editkwh.getText().toString();
             String inputrebate = editrebate.getText().toString();
             String totalCharges = answer.getText().toString();

             if (inputkwh.isEmpty() || inputrebate.isEmpty() || totalCharges.isEmpty()) {
                 Toast.makeText(MainActivity.this, "Please calculate first!", Toast.LENGTH_SHORT).show();
                 return;
             }

             // Get the current month
             Calendar calendar = Calendar.getInstance();
             SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy"); // Example: "November 2024"
             String currentMonth = dateFormat.format(calendar.getTime());

             // Create a data object
             CalculationData data = new CalculationData(inputkwh, inputrebate, totalCharges, currentMonth);

             // Save to Firebase with logging and error handling
             myRef.push().setValue(data)
                    .addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       Toast.makeText(MainActivity.this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
                            Log.d("Firebase", "Data saved successfully!");
                   } else {
                            Toast.makeText(MainActivity.this, "Failed to save data!", Toast.LENGTH_SHORT).show();
                            Log.e("Firebase", "Failed to save data: " + task.getException());
                         }
                   });
             }

        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menuHome) {
            Toast.makeText(this,"about clicked", Toast.LENGTH_SHORT).show();
            return true;

        }if (selected == R.id.menuAbout) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_SHORT).show();
            Intent aboutIntent = new Intent (this,AboutActivity.class);
            startActivity(aboutIntent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // Inner class for data model
    public class CalculationData {
        private String kwh;
        private String rebate;
        private String totalCharges;
        private String month;

        // Default constructor for Firebase
        public CalculationData() {}

        // Constructor with all fields
        public CalculationData(String kwh, String rebate, String totalCharges, String month) {
            this.kwh = kwh;
            this.rebate = rebate;
            this.totalCharges = totalCharges;
            this.month = month;
        }

        // Getters and setters (if needed)
        public String getKwh() {
            return kwh;
        }

        public void setKwh(String kwh) {
            this.kwh = kwh;
        }

        public String getRebate() {
            return rebate;
        }

        public void setRebate(String rebate) {
            this.rebate = rebate;
        }

        public String getTotalCharges() {
            return totalCharges;
        }

        public void setTotalCharges(String totalCharges) {
            this.totalCharges = totalCharges;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }
    }
}