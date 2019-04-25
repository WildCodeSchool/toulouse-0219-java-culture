package fr.wildcodeschool.culture;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class ScreenSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_splash);




        EasySplashScreen config = new EasySplashScreen(ScreenSplashActivity.this)
                .withFullScreen()
                .withTargetActivity(SignIn.class)
                .withSplashTimeOut(5000)
                .withBackgroundColor(Color.parseColor("#1a1b29"))
                .withHeaderText("")
                .withFooterText("")
                .withAfterLogoText("")
                .withBeforeLogoText("~ Welcome ~")
                .withLogo(R.drawable.splashscreen);

        config.getHeaderTextView().setTextColor(Color.WHITE);
        config.getFooterTextView().setTextColor(Color.WHITE);
        config.getBeforeLogoTextView().setTextColor(Color.WHITE);
        config.getAfterLogoTextView().setTextColor(Color.WHITE);

        View easySplashScreen = config.create();
        setContentView(easySplashScreen);

    }

}