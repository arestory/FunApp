package ywq.ares.funapp;

import android.app.Application;
import android.content.Context;

import java.util.Locale;

public class App extends Application {

    private  static  App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

    }

    public static boolean isZhLanguage() {

        Locale locale = app.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language.endsWith("zh");

    }

    public static Context getContext(){

        return app;
    }
}
