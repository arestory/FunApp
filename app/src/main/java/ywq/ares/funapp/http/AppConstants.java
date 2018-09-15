package ywq.ares.funapp.http;

import android.os.Environment;

import java.io.File;

/**
 * Created by ares on 2017/12/20.
 */
public  final class AppConstants {


    public static class HTTP{


        public static final int TIME_OUT=20000;
    }

    public static class URL{


        public static final String ARTWORK_URL = "http://ares-space.com:9595/";
    }

    public static class Constants{

        public static final int ART_WORK=0;
        public static final int UN_ART_WORK=1;
        public static final int ACTRESS =2;

    }
    public static class LOCAL{

        public static final String ROOT = Environment.getExternalStorageDirectory() + File.separator +"FunArtWork"+File.separator;
        public static final String NETWORK_CACHE =ROOT+"Cache";
        public static final String IMAGE_CACHE =ROOT+"Image";
    }
}
