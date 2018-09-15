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
import android.view.View
import android.widget.TextView
import ywq.ares.funapp.common.GlideApp
import ywq.ares.funapp.http.DataSource


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
        val tvTips = helper.getView<TextView>(R.id.tvTips)
        helper.itemView.setOnClickListener {

            itemClick?.onClick(item,helper.adapterPosition)
        }
        if (item is ArtWorkItem) {

            helper.setText(R.id.tvContent,item.content)
            helper.setText(R.id.tvCode,item.code)
            helper.setText(R.id.tvDate,item.date)


            helper.getView<TextView>(R.id.tvDate).visibility = when(item.date){

                null -> View.GONE
                else -> View.VISIBLE
            }

            helper.getView<TextView>(R.id.tvContent).visibility = when(item.content){

                null -> View.GONE
                else -> View.VISIBLE
            }

            if (DataSource.isOpenPhoto()) {

                GlideApp.with(cover).asBitmap().load(item.photoUrl).placeholder(R.drawable.cover).into(cover)
                tvTips.visibility = View.GONE


            }else{
                GlideApp.with(cover).asBitmap().load(R.drawable.cover)
                        .into(cover)
                tvTips.visibility = View.VISIBLE
            }


//            GlideApp.with(cover).load(item.photoUrl).placeholder(R.drawable.ic_launcher_background).into(cover)

        } else {



            val obj  = (item as ActressSearchItem)

            helper.setText(R.id.tvName,obj.name)

            if (DataSource.isOpenPhoto()) {

                GlideApp.with(cover).asBitmap().load(obj.avatar).placeholder(R.drawable.avatar)
                        .into(cover)


            }else{

                GlideApp.with(cover).asBitmap().load(R.drawable.avatar)
                        .into(cover)

            }

        }
    }


}
