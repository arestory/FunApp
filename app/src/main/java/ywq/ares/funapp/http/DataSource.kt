package ywq.ares.funapp.http

import android.os.Build
import com.ares.http.ArtworkApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ywq.ares.funapp.bean.*
import ywq.ares.funapp.util.CacheDataManager
import ywq.ares.funapp.util.SPUtils
import ywq.ares.funapp.util.rxjava2.SchedulersSwitcher
import java.io.File
import java.io.FileFilter
import java.util.function.BiFunction

object DataSource {

    private val cacheDataManager = CacheDataManager.getInstance()

    private val api = RetrofitServiceManager.getManager().create(AppConstants.URL.ARTWORK_URL, ArtworkApi::class.java)


    interface DataListener<T> {

        fun onSuccess(t: T)
        fun onFail()
    }
    fun getVideoList(code:String,dataListener: DataListener<List<VideoSearchItem>>){

        val dis = api.searchArtWorkVideo(code).compose(SchedulersSwitcher.io2UiThread())
                .subscribe({

                    dataListener.onSuccess(it)

                },{
                    dataListener.onFail()

                })
    }



    fun getArtworkListOfActress(id: String, page: Int, dataListener: DataListener<List<ArtWorkItem>>) {

        val dis = getArtworkListOfActress(id, page).subscribe({

            dataListener.onSuccess(it)
        }, {

            dataListener.onFail()
        })
    }

    fun getArtworkListOfActress(id: String, page: Int): Observable<List<ArtWorkItem>> {


        val sourceCache = Observable.create<List<ArtWorkItem>> {

            val cache = cacheDataManager.getCache("artist-".plus(id).plus("-artworkList-$page"))

            val list = Gson().fromJson<List<ArtWorkItem>>(cache, object : TypeToken<List<ArtWorkItem>>() {}.type)
            if (list != null && !list.isEmpty()) {

                it.onNext(list)
            }
            it.onComplete()


        }.subscribeOn(Schedulers.io())

        val sourceNet = api.getArtworkListOfActress(id, page)

        return Observable.concat(sourceCache, sourceNet).compose(SchedulersSwitcher.io2UiThread())

    }

    fun getActressDetail(id: String, dataListener: DataListener<ActressDetail>) {

        val dis = getActressDetail(id).subscribe({

            dataListener.onSuccess(it)
        }, {
            dataListener.onFail()

        })

    }

    fun getActressDetail(id: String): Observable<ActressDetail> {


        val sourceCache = Observable.create<ActressDetail> {

            val item = cacheDataManager.getCacheBean("artist-info-".plus(id), ActressDetail::class.java)
            if (item != null) {

                it.onNext(item)
            }
            it.onComplete()

        }.subscribeOn(Schedulers.io())

        val sourceNet = api.getActressDetail(id)


        return Observable.concat(sourceCache, sourceNet).compose(SchedulersSwitcher.io2UiThread())

    }

    fun getActressList(keyword: String, page: Int): Observable<List<ActressSearchItem>> {


        return api.getSearchList(keyword, page).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())

    }

    fun getArtworkDetail(code: String, dataListener: DataListener<MovieSearchItem>) {

       val dis =  getArtworkDetail(code).subscribe({

            dataListener.onSuccess(it)
        }, {

            dataListener.onFail()
        })

    }

    fun getArtworkDetail(code: String): Observable<MovieSearchItem> {

        val sourceCache = Observable.create<MovieSearchItem> {

            val item = cacheDataManager.getCacheBean("artwork-".plus(code), MovieSearchItem::class.java)

            if (item != null) {
                it.onNext(item)
            }
            it.onComplete()
        }.subscribeOn(Schedulers.io())
        val sourceNet = api.searchArtWork(code)
        return Observable.concat(sourceCache, sourceNet).compose(SchedulersSwitcher.io2UiThread())
    }

    fun collectedArtWork(code: String, item: MovieSearchItem) {
        val artWorkItem =ArtWorkItem()
        artWorkItem.code = code
        artWorkItem.content = item.title
        artWorkItem.title = item.title
        artWorkItem.date = item.date
        artWorkItem.movieUrl  = item.coverPhotoUrl
        artWorkItem.photoUrl = item.coverPhotoUrl
        val col  = CollectionBean<ArtWorkItem>()
        col.isCollected =true
        col.dataBean =artWorkItem
        cacheDataManager.saveCache(Gson().toJson(artWorkItem),"collect-artwork-$code")
    }

    fun getCollectedArtworkList():List<ArtWorkItem>{

        return cacheDataManager.getContainCacheBean("collect-artwork-",ArtWorkItem::class.java)
    }
    fun getCollectedActresses():List<Actress>{

        return cacheDataManager.getContainCacheBean("collect-actress-",Actress::class.java)
    }


    fun isArtWorkCollected(code: String):Boolean{
        return cacheDataManager.cacheExist("collect-artwork-$code")
    }
    fun collectedActress(actressId: String, actress:Actress) {
        val col  = CollectionBean<Actress>()
        col.isCollected =true
        col.dataBean =actress
        cacheDataManager.saveCache(Gson().toJson(actress),"collect-actress-$actressId")
    }
    fun collectedActress(actressId: String, info: ArrayList<Pair<String, String>>) {
        val col  = CollectionBean<ArrayList<Pair<String, String>>>()
        col.isCollected =true
        col.dataBean =info
        cacheDataManager.saveCache(Gson().toJson(col),"collect-actress-$actressId")
    }
    fun isActressCollected(actressId: String):Boolean{
        return cacheDataManager.cacheExist("collect-actress-$actressId")
    }
    fun removeCollectedArtWork(code: String) {
         cacheDataManager.clearCache("collect-artwork-$code")
    }
    fun removeCollectedActress(id: String) {
        cacheDataManager.clearCache("collect-actress-$id")
    }
    fun getArtworkList(keyword: String, page: Int, type: Int): Observable<List<ArtWorkItem>> {
        return api.getSearchList(keyword, page, type).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
    }

    fun isOpenPhoto(): Boolean {


        return SPUtils("user").getBoolean("openPhoto", false)


    }

    fun hadOpenShowPhoto(): Boolean {


        return SPUtils("user").getBoolean("showPhotoTipsFirstTime", true)

    }
}
