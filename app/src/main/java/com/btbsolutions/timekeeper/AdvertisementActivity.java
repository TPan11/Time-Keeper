package com.btbsolutions.timekeeper;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

public class AdvertisementActivity extends AppCompatActivity {

    private String path[] = {"http://tpandedeveloper.000webhostapp.com/videos/app_development_v2.mp4",
            "http://tpandedeveloper.000webhostapp.com/videos/jmpantCoach_v2.mp4",
            "http://tpandedeveloper.000webhostapp.com/videos/web_application_v2.mp4"};
    ProgressDialog pd;

    ImageButton imgbtn_close;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        imgbtn_close = findViewById(R.id.imgbtn_close);

        int adNumber = getIntent().getIntExtra("adnumber", 0);

        pd = new ProgressDialog(this);
        pd.setMessage("Buffering video please wait...");
        pd.show();

        Uri uri=Uri.parse(path[adNumber]);

        final VideoView video = findViewById(R.id.videoView);

        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(video);

        video.setVideoURI(uri);
        video.setMediaController(controller);
        video.start();

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //close the progress dialog when buffering is done
                pd.dismiss();
                duration = video.getDuration();
            }
        });

        video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                finish();
                return false;
            }
        });

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });

        imgbtn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
