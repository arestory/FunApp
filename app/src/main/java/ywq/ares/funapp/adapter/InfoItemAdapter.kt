package ywq.ares.funapp.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_actress_info.view.*
import ywq.ares.funapp.R

class InfoItemAdapter(var layoutId: Int) : BaseQuickAdapter<Pair<String, String>, BaseViewHolder>(layoutId) {


    override fun convert(helper: BaseViewHolder?, item: Pair<String, String>?) {


        val value = if (item?.second == null) {

            helper!!.itemView.context.getString(R.string.str_unknown)

        } else {
            item.second
        }
        val context = helper!!.itemView.context
        val text = when (item!!.first) {

            "name" -> context.getString(R.string.app_name)
            "birthday" -> context.getString(R.string.item_info_birthday)
            "age" -> context.getString(R.string.item_info_age)
            "cup" -> context.getString(R.string.item_info_cup)
            "stature" -> context.getString(R.string.item_info_stature)
            "chestWidth" -> context.getString(R.string.item_info_chestWidth)
            "waistline" -> context.getString(R.string.item_info_waistline)
            "hipline" -> context.getString(R.string.item_info_hipline)
            "home" -> context.getString(R.string.item_info_home)
            "hobby" -> context.getString(R.string.item_info_hobby)
            else -> ""
        }.plus(":").plus(value)

        helper.setText(R.id.tvInfo, text)


    }
}