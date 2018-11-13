package com.homecredit.exam;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {

    @BindView(R.id.buildname) TextView txtBuildName;
    @BindView(R.id.title) TextView txtTitle;
    @BindView(R.id.body) TextView txtBody;

    @BindString(R.string.open_weather_app) String strTitle;
    @BindString(R.string.splash_body_1) String strBody1;
    @BindString(R.string.splash_body_2) String strBody2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        txtBuildName.setText(BuildConfig.CONFIG_TYPE);
        if (StringUtils.equals(BuildConfig.CONFIG_TYPE, "Test Build"))
        {
            txtTitle.setText("");
            txtBody.setText("");
        }
        else if (StringUtils.equals(BuildConfig.CONFIG_TYPE, "Development Build"))
        {
            txtTitle.setText(strTitle);
            txtTitle.setTypeface(txtTitle.getTypeface(), Typeface.BOLD_ITALIC);
            txtBody.setText(strBody1);
        }
        else if (StringUtils.equals(BuildConfig.CONFIG_TYPE, "Production Build"))
        {
            txtTitle.setText(strTitle);
            txtTitle.setTypeface(txtTitle.getTypeface(), Typeface.BOLD);
            txtBody.setText(strBody2);
        }


        Thread thread = new Thread()
        {
            @Override
            public void run() {
                try
                {
                    sleep(2000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
