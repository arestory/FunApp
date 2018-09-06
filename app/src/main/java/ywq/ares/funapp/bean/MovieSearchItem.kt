package ywq.ares.funapp.bean

class MovieSearchItem {

    var title:String?=null
    var coverPhotoUrl:String?=null
    var code:String?=null
    var date:String?=null
    var duration:String?=null

    var actresses:ArrayList<ActressSearchItem>  =ArrayList()
    var samplePhotos:ArrayList<String>  = ArrayList()
    var relateArtWorkList:ArrayList<SimpleMovieItem>  = ArrayList()
}