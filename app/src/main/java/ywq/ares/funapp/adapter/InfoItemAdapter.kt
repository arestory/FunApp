package ywq.ares.funapp.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_actress_info.view.*
import ywq.ares.funapp.R

class InfoItemAdapter(var layoutId:Int) :BaseQuickAdapter<Pair<String,String>,BaseViewHolder>(layoutId){


    override fun convert(helper: BaseViewHolder?, item: Pair<String, String>?) {


        var value :String?=null
        if(item?.second==null){

            value = "不详"

        }else{
            value = item.second
        }
            val text =  when(item!!.first){

                "name" -> "名字："
                "birthday"->"生日："
                "age"->"年龄："
                "cup"->"罩杯："
                "stature"->"身高："
                "chestWidth"->"胸围："
                "waistline"->"腰围："
                "hipline"->"臀围："
                "home"->"家乡："
                "hobby"->"兴趣："
                else -> ""
            }.plus(value)

            helper!!.setText(R.id.tvInfo,text)




    }
}