package ywq.ares.funapp.http

import com.ares.http.ArtworkApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ywq.ares.funapp.bean.ActressDetail
import ywq.ares.funapp.bean.ActressSearchItem
import ywq.ares.funapp.bean.ArtWorkItem
import ywq.ares.funapp.bean.MovieSearchItem
import ywq.ares.funapp.util.SPUtils

object DataSource {


    private val api =  RetrofitServiceManager.getManager().create(AppConstants.URL.ARTWORK_URL, ArtworkApi::class.java)


    fun getArtworkListOfActress(id:String,page: Int):Observable<List<ArtWorkItem>>{



        return api.getArtworkListOfActress(id,page).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())

    }

    fun getActressDetail(id: String): Observable<ActressDetail> {


        return api.getActressDetail(id).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
    }

    fun getActressList(keyword:String ,page :Int):Observable<List<ActressSearchItem>>{


        return api.getSearchList(keyword,page).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())

    }

    fun getArtworkDetail(code:String): Observable<MovieSearchItem>{

        return  api.searchArtWork(code).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun getArtworkList(keyword:String ,page :Int,type:Int): Observable<List<ArtWorkItem>>{


        return api.getSearchList(keyword,page,type).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
    }

    fun isOpenPhoto():Boolean{


        return SPUtils("user").getBoolean("openPhoto",false)


    }
}
