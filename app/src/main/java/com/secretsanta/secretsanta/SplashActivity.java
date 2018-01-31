package com.secretsanta.secretsanta;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_splash);

        //Sound
        MediaPlayer mp = MediaPlayer.create(this, R.raw.sound_open);
        mp.start();

        changeActivityAfter(3500);
    }

    private void changeActivityAfter(int sec){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, ParticipantActivity.class);
                SplashActivity.this.startActivity( i );
                SplashActivity.this.finish();
            }
        }, sec);
    }
}
