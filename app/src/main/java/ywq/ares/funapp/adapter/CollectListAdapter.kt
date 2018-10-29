package ywq.ares.funapp.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

import ywq.ares.funapp.R
import ywq.ares.funapp.bean.Actress
import ywq.ares.funapp.bean.ArtWorkItem
import ywq.ares.funapp.bean.CollectionBean
import ywq.ares.funapp.bean.MovieSearchItem
import ywq.ares.funapp.common.GlideApp
import ywq.ares.funapp.http.DataSource

class CollectListAdapter(data: List<CollectionBean<*>>) : BaseMultiItemQuickAdapter<CollectionBean<*>, BaseViewHolder>(data) {

    init {
        addItemType(2, R.layout.item_artress_card)
        addItemType(1, R.layout.item_art_work)
    }

    override fun convert(helper: BaseViewHolder, item: CollectionBean<*>) {

        val type = getItemViewType(helper.adapterPosition)
        val cover = helper.getView<ImageView>(R.id.ivCover)
        val tvTips = helper.getView<TextView>(R.id.tvTips)
        val obj = item.dataBean
        if (obj is Actress) {

            helper.setText(R.id.tvName, obj.name)

            if (DataSource.isOpenPhoto()) {

                GlideApp.with(cover).asBitmap().load(obj.avatar).placeholder(R.drawable.avatar)
                        .into(cover)


            } else {

                GlideApp.with(cover).asBitmap().load(R.drawable.avatar)
                        .into(cover)

            }

            helper.itemView.setOnClickListener {

                onItemClick?.onActressClick(obj)
            }

        } else {

            val it = obj as ArtWorkItem

            helper.itemView.setOnClickListener {

                _ ->
                onItemClick?.onArtworkClick(it)
            }
            helper.setText(R.id.tvContent, it.content)
            helper.setText(R.id.tvCode, it.code)
            helper.setText(R.id.tvDate, it.date)


            helper.getView<TextView>(R.id.tvDate).visibility = when (it.date) {

                null -> View.GONE
                else -> View.VISIBLE
            }

            helper.getView<TextView>(R.id.tvContent).visibility = when (it.content) {

                null -> View.GONE
                else -> View.VISIBLE
            }

            if (DataSource.isOpenPhoto()) {

                GlideApp.with(cover).asBitmap().load(it.photoUrl).placeholder(R.drawable.cover).into(cover)
                tvTips.visibility = View.GONE


            } else {
                GlideApp.with(cover).asBitmap().load(R.drawable.cover)
                        .into(cover)
                tvTips.visibility = View.VISIBLE
            }
        }
    }

    private var onItemClick: OnItemClick? = null

    fun setOnItemClick(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    public interface OnItemClick {

        fun onActressClick(actress: Actress)

        fun onArtworkClick(workItem: ArtWorkItem)
    }
}
