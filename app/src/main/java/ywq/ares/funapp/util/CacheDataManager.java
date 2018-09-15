package ywq.ares.funapp.util;


import android.annotation.SuppressLint;
import android.util.ArrayMap;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ywq.ares.funapp.App;
import ywq.ares.funapp.http.AppConstants;
import ywq.ares.funapp.util.rxjava2.DataConsumer;
import ywq.ares.funapp.util.rxjava2.SchedulersSwitcher;
import ywq.ares.funapp.util.rxjava2.ThrowableConsumer;

/**
 * 缓存数据管理器，用本地文件取替 sharepreference
 * Created by ares on 2017/6/5.
 */

public class CacheDataManager {


    private static volatile CacheDataManager instance = null;

    public static final String CACHE_TIME = "cacheTime";

    //双重锁式
    public static CacheDataManager getInstance() {
        if (instance == null) {
            synchronized (CacheDataManager.class) {
                if (instance == null) {
                    instance = new CacheDataManager();
                }
            }
        }
        return instance;
    }


    /**
     * 缓存到本地，已运行在 io 线程
     *
     * @param content
     * @param key
     * @param dataListener
     */
    public  Disposable  saveCache(String content, final String key, final SaveCacheListener dataListener) {


       return Observable.just(content).compose(SchedulersSwitcher.io2IoAndHandleError())
                .subscribe(new DataConsumer<String>() {
                    @Override
                    public void acceptData(@NonNull String content) {

                        BufferedWriter writer = null;

                        File rootPath = new File(AppConstants.LOCAL.NETWORK_CACHE);
                        if (!rootPath.exists()) {
                            rootPath.mkdirs();
                        }


                        String fileName = AppConstants.LOCAL.NETWORK_CACHE + File.separator + key + ".json";

                        File file = new File(fileName);

                        if (file.exists()) {
                            file.delete();
                        }


                        System.out.println("保存缓存！"+fileName);
                        //写入
                        try {
                            writer = new BufferedWriter(new FileWriter(file));
                            writer.write(content);
                            dataListener.onSaveSuccess();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Map<String, String> map = new ArrayMap<>();
                            map.put("path", file.getPath());
                        } finally {
                            try {
                                if (writer != null) {
                                    writer.close();
                                }
                            } catch (IOException e) {


                            }
                        }

                    }
                }, new ThrowableConsumer() {
                    @Override
                    public void handle(ExceptionHandle.ResponseThrowable e) {

                    }
                });


    }

    public synchronized boolean cacheExist(String key){


        return new File( AppConstants.LOCAL.NETWORK_CACHE + File.separator +key+".json").exists();
    }

    /**
     * 保存成功的回调
     */
    public interface SaveCacheListener {

        void onSaveSuccess();

    }


    /**
     * 缓存到本地，已运行在 io 线程
     *
     * @param content
     * @param key
     */
    public Disposable saveCache(String content, final String key) {

      return   Observable.just(content).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(new DataConsumer<String>() {
                    @Override
                    public void acceptData(@NonNull String content) {

                        BufferedWriter writer = null;

                        File rootPath = new File(AppConstants.LOCAL.NETWORK_CACHE);
                        if (!rootPath.exists()) {
                            rootPath.mkdirs();
                        }


                        File file = new File(AppConstants.LOCAL.NETWORK_CACHE + File.separator + key + ".json");

                        if (file.exists()) {
                            file.delete();

                            //写入
                            try {
                                writer = new BufferedWriter(new FileWriter(file));
                                writer.write(content);
                            } catch (IOException e) {
                                e.printStackTrace();

                                Map<String, String> map = new ArrayMap<>();
                                map.put("path", file.getPath());
                            } finally {
                                try {
                                    if (writer != null) {
                                        writer.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();

                                }
                            }
                        }
                    }
                });


    }


    /**
     * 获取缓存，需运行在 io 线程
     *
     * @param key
     * @return
     */
    public String getCacheFromCacheDir(final String key) {

        FileInputStream fis = null;
        StringBuffer sBuf = new StringBuffer();
        try {

            fis = App.getContext().openFileInput(key + ".json");
             int len = 0;
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) != -1) {
                sBuf.append(new String(buf, 0, len));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (sBuf != null) {
            return sBuf.toString();
        }
        return null;


    }

    /**
     * 获取缓存，需运行在 io 线程
     *
     * @param key
     * @return
     */
    public String getCache(final String key){


        final String fileName = AppConstants.LOCAL.NETWORK_CACHE + File.separator + key + ".json";


        File file = new File(fileName);
        if (!file.exists()) {

            return null;
        }
        BufferedReader reader = null;
        //返回值,使用StringBuffer
        StringBuffer data = new StringBuffer();
        //
        try {
            reader = new BufferedReader(new FileReader(file));
            //每次读取文件的缓存
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                data.append(temp);
            }
        } catch (IOException fileE) {
            fileE.printStackTrace();
            Map<String, String> map = new ArrayMap<>();
            map.put("path", fileName);


        } finally {
            //关闭文件流
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioE) {
                    ioE.printStackTrace();

                }
            }
        }


//         return getCacheFromCacheDir(key);
        return data.toString();



    }


    /**
     * 自动将 json 缓存解析成对应的类
     *
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T getCacheBean(String key, Class<T> tClass)  {

        String json = getCache(key);
        Gson gson = new Gson();

        return gson.fromJson(json, tClass);

    }

    /**
     * 清理目标缓存
     *
     * @param key
     * @return
     */
    public boolean clearCache(String key) {
        final String path = AppConstants.LOCAL.NETWORK_CACHE + File.separator + key + ".json";

        return new File(path).delete();


    }


    /**
     * 清理所有缓存
     */
    public boolean clearAllCache() {
        final String path = AppConstants.LOCAL.NETWORK_CACHE;

        File file = new File(path);

        return file.delete() & file.mkdirs();
    }

}

