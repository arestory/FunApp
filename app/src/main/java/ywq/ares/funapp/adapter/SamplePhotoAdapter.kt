package ywq.ares.funapp.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_sample_photo.view.*
import ywq.ares.funapp.R

class SamplePhotoAdapter(layoutId:Int,list: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(layoutId,list) {


    override fun convert(helper: BaseViewHolder?, item: String?) {

        val ivCover = helper!!.getView<ImageView>(R.id.ivCover)


        Glide.with(helper.itemView.context).load(item).into(ivCover)
    }
}