package com.example.firstpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

public class AboutUsActivity extends AppCompatActivity {


    ImageView arrowback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        arrowback = (ImageView) findViewById(R.id.arrow);

       arrowback.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FirstFragment()).commit();
           }
       });
    }
}
