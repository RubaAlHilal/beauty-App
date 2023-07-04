package com.example.firstpage;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Discover extends AppCompatActivity {

    CardView skin, hair, makeup;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discover);

        skin = (CardView) findViewById(R.id.SkincareCard);
        hair = (CardView) findViewById(R.id.HairCard);
        makeup = (CardView) findViewById(R.id.MakeupCard);
        back = (ImageView) findViewById(R.id.arrow);


        skin.setOnClickListener(v -> {
            Intent intent = new Intent(Discover.this, Product.class);
            intent.putExtra("category","Skincare");
            startActivity(intent);
        });

//        skin.setOnClickListener(view ->{
//            startActivity(new Intent(this, Skincare.class));
//        });


        hair.setOnClickListener(v -> {
            Intent intent = new Intent(Discover.this, Product.class);
            intent.putExtra("category","Hair");
            startActivity(intent);
        });

        makeup.setOnClickListener(v -> {
            Intent intent = new Intent(Discover.this, Product.class);
            intent.putExtra("category","Makeup");
            startActivity(intent);
        });

        back.setOnClickListener(view ->{
            startActivity(new Intent(this, MainActivity.class));
        });
    }
}