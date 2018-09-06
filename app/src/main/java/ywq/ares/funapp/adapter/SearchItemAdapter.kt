package ywq.ares.funapp.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fivehundredpx.greedolayout.GreedoLayoutSizeCalculator

import ywq.ares.funapp.R
import ywq.ares.funapp.bean.ActressSearchItem
import ywq.ares.funapp.bean.ArtWorkItem
import ywq.ares.funapp.bean.BaseSearchItem
import ywq.ares.funapp.http.AppConstants
import android.graphics.BitmapFactory
import android.support.v7.widget.StaggeredGridLayoutManager


class SearchItemAdapter
/**
 * Same as QuickAdapter#QuickAdapter(Context,int) but with
 * some initialization data.
 *
 * @param data A new list is created out of this one to avoid mutable list
 */
(data: List<BaseSearchItem>) : BaseMultiItemQuickAdapter<BaseSearchItem, BaseViewHolder>(data)  {
    init {

        addItemType(AppConstants.Constants.ART_WORK, R.layout.item_art_work)
        addItemType(AppConstants.Constants.ACTRESS, R.layout.item_artress_card)
    }




    private var itemClick:OnItemClickEx?=null
    fun setOnItemClickEx(click:OnItemClickEx){

        this.itemClick = click

    }

    interface OnItemClickEx{

        fun onClick(item:BaseSearchItem,position:Int)
    }

    override fun convert(helper: BaseViewHolder, item: BaseSearchItem) {

        val cover = helper.getView<ImageView>(R.id.ivCover)
        helper.itemView.setOnClickListener {

            itemClick?.onClick(item,helper.adapterPosition)
        }
        if (item is ArtWorkItem) {

            helper.setText(R.id.tvContent,item.content)
            helper.setText(R.id.tvCode,item.code)
            helper.setText(R.id.tvDate,item.date)



            Glide.with(cover).asBitmap().load(item.photoUrl).into(cover)
//            GlideApp.with(cover).load(item.photoUrl).placeholder(R.drawable.ic_launcher_background).into(cover)

        } else {



            val obj  = (item as ActressSearchItem)

            helper.setText(R.id.tvName,obj.name)
            Glide.with(cover).asBitmap().load(obj.avatar)
                    .into(cover)


        }
    }


}
