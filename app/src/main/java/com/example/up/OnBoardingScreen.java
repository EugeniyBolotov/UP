package com.example.up;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.example.up.common.NetworkStats;

public class OnBoardingScreen extends AppCompatActivity {

    Integer step = 0;
    Integer start_x = -1;
    Integer end_x = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start_x = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                end_x = (int) event.getX();
        }

        if(start_x != -1 && end_x != -1) {
            if(Math.abs(start_x - end_x) > 10) {
                if(start_x < end_x) {
                    if(step != 0) {
                        setContentView(R.layout.activity_on_boarding_screen);
                        step = 0;
                    }
                } else {
                    if(step != 1) {
                        setContentView(R.layout.activity_on_boarding_screen_2);
                        if(!NetworkStats.isOnline(this)) {
                            TextView skip = findViewById(R.id.textView3);
                            skip.setVisibility(View.VISIBLE);
                        }
                        step = 1;
                    }
                }
            }
            start_x = -1;
            end_x =-1;
        }

        return false;
    }

    public void OnSingIn(View view) {
        Intent intent = new Intent(OnBoardingScreen.this, SingInScreen.class);
        startActivity(intent);
    }
    public void OnSingUp(View view) {
        Intent intent = new Intent(OnBoardingScreen.this, SingUpScreen.class);
        startActivity(intent);
    }
    public void OnSkipSing(View view) {
        Intent intent = new Intent(OnBoardingScreen.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}