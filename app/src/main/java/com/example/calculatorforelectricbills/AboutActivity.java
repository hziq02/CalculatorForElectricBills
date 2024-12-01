package com.example.calculatorforelectricbills;

import android.os.Bundle;
import android.view.MenuItem;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutActivity extends AppCompatActivity {


    Toolbar aboutToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);

        aboutToolbar = findViewById(R.id.about_toolbar);
        setSupportActionBar(aboutToolbar);
        getSupportActionBar().setTitle("About");

        // Find the ImageView by its ID
        ImageView imageView = findViewById(R.id.github);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set an OnClickListener
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // URL to open
                String url = "https://github.com/hziq02/CalculatorForElectricBills";

                // Create an Intent to open the URL in a browser
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));

                // Start the activity
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == android.R.id.home){
            super.onBackPressed();

        }
        return true;

    }

}