package ywq.ares.funapp.bean

import java.io.Serializable

class MovieSearchItem :Serializable{

    var title:String?=null
    var coverPhotoUrl:String?=null
    var code:String?=null
    var date:String?=null
    var duration:String?=null

    var actresses:ArrayList<ActressSearchItem>  =ArrayList()
    var samplePhotos:ArrayList<String>  = ArrayList()
    var relateArtWorkList:ArrayList<SimpleMovieItem>  = ArrayList()
}