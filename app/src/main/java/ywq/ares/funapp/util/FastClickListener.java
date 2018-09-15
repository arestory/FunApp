package ywq.ares.funapp.util;

import android.view.View;

public  abstract  class FastClickListener implements View.OnClickListener {

    private long lastClickTime =0;


    public abstract void onFastClick(View view);

    @Override
    public void onClick(View v) {

        if(System.currentTimeMillis()-lastClickTime<getQuickInterval()){

            onFastClick(v);
        }


        lastClickTime=System.currentTimeMillis();

    }

    //时间间隔
    private long getQuickInterval() {
        return 400;
    }
}
