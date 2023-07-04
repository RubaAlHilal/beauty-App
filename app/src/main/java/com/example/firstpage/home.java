package com.example.firstpage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        VideoView videoView=findViewById(R.id.video_view);
        String videoPath="android.resource://"+getPackageName()+"/"+R.raw.beutyvideo;
        Uri uri=Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController=new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

    }
}
