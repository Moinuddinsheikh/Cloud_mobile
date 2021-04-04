package com.macstudio.mobilecloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import com.macstudio.mobilecloud.MainActivity;

public class SplashActivity
extends Activity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().setFlags(512, 512);
        this.setContentView(2131427401);
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent intent = new Intent((Context)SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
                SplashActivity.this.overridePendingTransition(2130771984, 2130771985);
            }
        }, 1000L);
    }

}

