package ywq.ares.funapp.bean;

import ywq.ares.funapp.http.AppConstants

class ActressSearchItem:BaseSearchItem() {

    var id:String?=null
    var name:String?=null
    var avatar:String?=null
    var workListUrl:String?=null
    override fun toString(): String {
        return "ActressSearchItem(id=$id,name=$name, avatar=$avatar, workListUrl=$workListUrl)"
    }

    override fun getItemType(): Int {

        return AppConstants.Constants.ACTRESS
    }

}