package ywq.ares.funapp.activity;

import java.util.Timer;
import java.util.TimerTask;

import ywq.ares.funapp.R;
import ywq.ares.funapp.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void doMain() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                SearchActivity.Companion.start(WelcomeActivity.this);

                finish();
            }
        },500);
    }
}
