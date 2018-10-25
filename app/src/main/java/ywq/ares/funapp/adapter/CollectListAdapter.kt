package ywq.ares.funapp.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

import ywq.ares.funapp.R
import ywq.ares.funapp.bean.CollectionBean
import ywq.ares.funapp.bean.MovieSearchItem

class CollectListAdapter(data: List<CollectionBean<*>>) : BaseMultiItemQuickAdapter<CollectionBean<*>, BaseViewHolder>(data) {

    init {
        addItemType(1, R.layout.item_artress_card)
        addItemType(2, R.layout.item_art_work)
    }

    override fun convert(helper: BaseViewHolder, item: CollectionBean<*>) {

        val type = getItemViewType(helper.adapterPosition)

        when (type) {
            1 -> {

                val movieItem = item.dataBean as MovieSearchItem


            }
            else -> {
                val actressItem = item.dataBean as ArrayList<Pair<String, String>>


            }


        }
    }
}
