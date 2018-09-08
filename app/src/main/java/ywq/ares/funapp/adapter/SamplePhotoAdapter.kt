package ywq.ares.funapp.adapter

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_sample_photo.view.*
import ywq.ares.funapp.R
import ywq.ares.funapp.http.DataSource

class SamplePhotoAdapter(layoutId:Int,list: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(layoutId,list) {


    override fun convert(helper: BaseViewHolder?, item: String?) {

        val ivCover = helper!!.getView<ImageView>(R.id.ivCover)


        if (DataSource.isOpenPhoto()) {

            Glide.with(helper.itemView.context).load(item).into(ivCover)

         }else{
             Glide.with(helper.itemView.context).load(R.drawable.cover).into(ivCover)


        }
    }
}