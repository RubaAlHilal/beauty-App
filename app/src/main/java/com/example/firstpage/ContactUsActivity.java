package com.example.firstpage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ContactUsActivity extends AppCompatActivity {

    ImageView ivFacebook,ivInsta,ivTwitter, arrowBack;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

        ivFacebook=findViewById(R.id.facebook);
        ivInsta=findViewById(R.id.insta);
        ivTwitter=findViewById(R.id.twitter);
        arrowBack = (ImageView) findViewById(R.id.arrow);

        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sAppLink="fb://page/237564710351658";
                String sPackage="com.facebook.katana";
                String sWebLink="https://www.facebook.com/12H1rDeveloper";


                openLink(sAppLink,sPackage,sWebLink);
            }
        });

        ivInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sAppLink="https://www.instagram.com/imam_u1";
                String sPackage= "com.instagram.android";

                openLink(sAppLink,sPackage,sAppLink);
            }
        });

        ivTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sAppLink="twitter://users?screen_name=AndroidCoding_";
                String sPackage="com.twitter.android";
                String sWebLink="https://www.twitter.com/IMSIU_edu_sa";


                openLink(sAppLink,sPackage,sWebLink);
            }
        });


        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FirstFragment()).commit();
            }
        });

    }

    private void openLink(String sAppLink, String sPackage, String sWebLink) {

        try {
            Uri uri= Uri.parse(sAppLink);

            Intent intent=new Intent(Intent.ACTION_VIEW);

            intent.setData(uri);
            intent.setPackage(sPackage);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        }catch (ActivityNotFoundException activityNotFoundException){
            Uri uri=Uri.parse(sWebLink);
            Intent intent=new Intent(Intent.ACTION_VIEW);

            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }
}