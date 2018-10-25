package com.ares.http

import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ywq.ares.funapp.bean.*

interface ArtworkApi {



    @GET("/artwork/actress")
    fun getArtworkListOfActress( @Query(value = "id") id: String,@Query("page") page:Int): Observable<List<ArtWorkItem>>

    @GET("/artwork/actresses/{page}")
    fun getActressList(@Path("page") page: Int): Observable<List<Actress>>

    @GET("/artwork/actressInfo")
    fun getActressDetail(@Query("id") id: String): Observable<ActressDetail>

    @GET("/artwork/search/{keyword}/{page}?type=2")
    fun getSearchList(@Path("keyword") keyword: String, @Path("page") page: Int): Observable<List<ActressSearchItem>>

    @GET("/artwork/search/{keyword}/{page}")
    fun getSearchList(@Path("keyword") keyword: String, @Path("page") page: Int, @Query(value = "type") type: Int): Observable<List<ArtWorkItem>>

    @GET("/artwork/{code}")
    fun searchArtWork(@Path("code") code: String): Observable<MovieSearchItem>

    @GET("/artwork/video/{code}")
    fun searchArtWorkVideo(@Path("code") code: String): Observable<List<VideoSearchItem>>

}
