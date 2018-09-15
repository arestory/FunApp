package ywq.ares.funapp.adapter

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_sample_photo.view.*
import ywq.ares.funapp.App
import ywq.ares.funapp.R
import ywq.ares.funapp.common.GlideApp
import ywq.ares.funapp.http.DataSource

class SamplePhotoAdapter(layoutId:Int,list: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(layoutId,list) {


    override fun convert(helper: BaseViewHolder?, item: String?) {

        val ivCover = helper!!.getView<ImageView>(R.id.ivCover)


        if (DataSource.isOpenPhoto()) {
            if(App.isZhLanguage()){
                GlideApp.with(helper.itemView.context).load(item).error(R.drawable.load_fail)
                        .placeholder(R.drawable.cover_placeholder_zh)
                        .into(ivCover)
            }else{
                GlideApp.with(helper.itemView.context).load(item).error(R.drawable.load_fail)
                        .placeholder(R.drawable.cover_placeholder)
                        .into(ivCover)
            }


         }else{

            if(App.isZhLanguage()){

                GlideApp.with(helper.itemView.context).load(R.drawable.cover_placeholder_invisible_zh).into(ivCover)
            }else{
                GlideApp.with(helper.itemView.context).load(R.drawable.cover_placeholder_invisible).into(ivCover)

            }


        }
    }
}