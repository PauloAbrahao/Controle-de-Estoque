package com.example.controleestoque;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Seta um timer de 3 segundos ao iniciar o aplicativo
        // para que seja demonstrada a tela inicial ou de apresentação
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i = new Intent(SplashActivity.this, ActivityMain.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}