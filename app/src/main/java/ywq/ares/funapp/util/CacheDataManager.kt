package ywq.ares.funapp.util


import android.annotation.SuppressLint
import android.util.ArrayMap

import com.google.gson.Gson

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileFilter
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.io.FilenameFilter
import java.io.IOException
import java.util.ArrayList

import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ywq.ares.funapp.App
import ywq.ares.funapp.http.AppConstants
import ywq.ares.funapp.util.rxjava2.DataConsumer
import ywq.ares.funapp.util.rxjava2.SchedulersSwitcher
import ywq.ares.funapp.util.rxjava2.ThrowableConsumer
import java.nio.file.Paths

/**
 * 缓存数据管理器，用本地文件取替 sharepreference
 * Created by ares on 2017/6/5.
 */

object CacheDataManager {


    /**
     * 缓存到本地，已运行在 io 线程
     *
     * @param content
     * @param key
     * @param dataListener
     */
    fun saveCache(content: String, key: String, dataListener: SaveCacheListener?): Disposable {


        return Observable.just(content).compose(SchedulersSwitcher.io2IoAndHandleError())
                .subscribe(object : DataConsumer<String>() {
                    override fun acceptData(@NonNull content: String) {

                        var writer: BufferedWriter? = null

                        val rootPath = File(AppConstants.LOCAL.NETWORK_CACHE)
                        if (!rootPath.exists()) {
                            rootPath.mkdirs()
                        }


                        val fileName = AppConstants.LOCAL.NETWORK_CACHE + File.separator + key + ".json"

                        val file = File(fileName)

                        if (file.exists()) {
                            file.delete()
                        }


                        println("保存缓存！$fileName")
                        //写入
                        try {
                            writer = BufferedWriter(FileWriter(file))
                            writer.write(content)
                            dataListener?.onSaveSuccess()
                        } catch (e: IOException) {
                            e.printStackTrace()
                            val map = ArrayMap<String, String>()
                            map["path"] = file.path
                        } finally {
                            try {
                                writer?.close()
                            } catch (e: IOException) {


                            }

                        }

                    }
                }, object : ThrowableConsumer() {
                    override fun handle(e: ExceptionHandle.ResponseThrowable) {

                    }
                })


    }

    @Synchronized
    fun cacheExist(key: String): Boolean {


        return File(AppConstants.LOCAL.NETWORK_CACHE + File.separator + key + ".json").exists()
    }

    /**
     * 保存成功的回调
     */
    interface SaveCacheListener {

        fun onSaveSuccess()

    }


    /**
     * 缓存到本地，已运行在 io 线程
     *
     * @param content
     * @param key
     */
    fun saveCache(content: String, key: String): Disposable {

        return Observable.just(content).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(object : DataConsumer<String>() {
                    override fun acceptData(@NonNull content: String) {

                        var writer: BufferedWriter? = null

                        val rootPath = File(AppConstants.LOCAL.NETWORK_CACHE)
                        if (!rootPath.exists()) {
                            rootPath.mkdirs()
                        }


                        val file = File(AppConstants.LOCAL.NETWORK_CACHE + File.separator + key + ".json")

                        if (file.exists()) {
                            file.delete()
                        }
                        //写入
                        try {
                            writer = BufferedWriter(FileWriter(file))
                            writer.write(content)
                        } catch (e: IOException) {
                            e.printStackTrace()

                            val map = ArrayMap<String, String>()
                            map["path"] = file.path
                        } finally {
                            try {
                                writer?.close()
                            } catch (e: IOException) {
                                e.printStackTrace()

                            }

                        }
                    }
                })
    }


    /**
     * 获取缓存，需运行在 io 线程
     *
     * @param key
     * @return
     */
    fun getCacheFromCacheDir(key: String): String {

        var fis: FileInputStream? = null
        val sBuf = StringBuffer()
        try {

            fis = App.getContext().openFileInput("$key.json")
            val buf = ByteArray(1024)
            var len =  fis!!.read(buf)
            while ((len) != -1) {
                len =  fis.read(buf)
                sBuf.append(String(buf, 0, len))
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return sBuf?.toString()


    }


    /**
     * 获取缓存，需运行在 io 线程
     *
     * @param key
     * @return
     */
    fun getCache(key: String): String? {


        val fileName = AppConstants.LOCAL.NETWORK_CACHE + File.separator + key + ".json"


        val file = File(fileName)
        if (!file.exists()) {

            return null
        }
        var reader: BufferedReader? = null
        //返回值,使用StringBuffer
        val data = StringBuffer()
        //
        try {
            reader = BufferedReader(FileReader(file))
            //每次读取文件的缓存
            var temp :String ?=null
            while ({ temp = reader.readLine();temp }() != null) {
//                temp = temp + read + "\r\n"
                data.append(temp)
            }
        } catch (fileE: IOException) {
            fileE.printStackTrace()
            val map = ArrayMap<String, String>()
            map["path"] = fileName
        } finally {
            //关闭文件流
            if (reader != null) {
                try {
                    reader.close()
                } catch (ioE: IOException) {
                    ioE.printStackTrace()

                }

            }
        }


        //         return getCacheFromCacheDir(key);
        return data.toString()


    }

    fun <T> getContainCacheBean(key: String, tClass: Class<T>): List<T> {

        val list = ArrayList<T>()
        val fileName = AppConstants.LOCAL.NETWORK_CACHE + File.separator + key + ".json"

        val rootFile = File(AppConstants.LOCAL.NETWORK_CACHE)
        val files = rootFile.listFiles { dir, name -> name.contains(key) }

        files.reverse()
        for (file in files) {

            var reader: BufferedReader? = null
            //返回值,使用StringBuffer
            val data = StringBuffer()
            //
            try {
                reader = BufferedReader(FileReader(file))
                //每次读取文件的缓存
                var temp :String ?=null
                while ({ temp = reader.readLine();temp }() != null) {
//                    temp = reader.readLine()
                    data.append(temp)
                }
            } catch (fileE: IOException) {
                fileE.printStackTrace()
                val map = ArrayMap<String, String>()
                map["path"] = fileName
            } finally {
                //关闭文件流
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (ioE: IOException) {
                        ioE.printStackTrace()

                    }

                }
            }

            list.add(Gson().fromJson(data.toString(), tClass))

        }



        return list


    }

    /**
     * 自动将 json 缓存解析成对应的类
     *
     * @param key
     * @param tClass
     * @param <T>
     * @return
    </T> */
    fun <T> getCacheBean(key: String, tClass: Class<T>): T {

        val json = getCache(key)
        val gson = Gson()

        return gson.fromJson(json, tClass)

    }

    /**
     * 清理目标缓存
     *
     * @param key
     * @return
     */
    fun clearCache(key: String): Boolean {
        val path = AppConstants.LOCAL.NETWORK_CACHE + File.separator + key + ".json"

        return File(path).delete()


    }


    /**
     * 清理所有缓存
     */
    fun clearAllCache(): Boolean {
        val path = AppConstants.LOCAL.NETWORK_CACHE

        val file = File(path)

        return file.delete() and file.mkdirs()
    }



}

