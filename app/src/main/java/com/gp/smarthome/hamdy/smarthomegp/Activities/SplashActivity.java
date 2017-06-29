package com.gp.smarthome.hamdy.smarthomegp.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.SmartHomeApp;
import com.gp.smarthome.hamdy.smarthomegp.User;


/**
 * Created by AppleXIcon on 18/4/2017.
 */

public class SplashActivity extends AppCompatActivity {
    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        user = ((SmartHomeApp) getApplication()).user;

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                if(user.isLoggedIn(getApplicationContext())){
                    MainActivity.start(SplashActivity.this);
                }
                else{

                    LoginActivity.start(SplashActivity.this);

                }
            }
        }, 5000);
    }
}
