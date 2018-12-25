package ywq.ares.funapp.fragments

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.ares.datacontentlayout.DataContentLayout
import ywq.ares.funapp.R
import ywq.ares.funapp.activity.ActressInfoActivity
import ywq.ares.funapp.activity.ArtworkDetailActivity
import ywq.ares.funapp.adapter.CollectListAdapter
import ywq.ares.funapp.bean.*
import ywq.ares.funapp.http.DataSource

class ActressCollectFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_collect_actresses
    private var adapter: CollectListAdapter? = null

    override fun initData(arguments: Bundle?, rootView: View?) {
        val rv = rootView!!.findViewById<RecyclerView>(R.id.rvInfo)
        val dataLayout = rootView.findViewById<DataContentLayout>(R.id.dataLayout)
        dataLayout.showLoading()
        DataSource.getCollectionBean(CollectType.Actress,object : DataSource.DataListener<List<CollectionBean<*>>>{
            override fun onSuccess(t: List<CollectionBean<*>>) {
                if(t.isEmpty()){
                    dataLayout.showEmptyContent()
                }else{
                    dataLayout.showContent()
                    adapter = CollectListAdapter(t)
                    rv.adapter =adapter
                    rv.addItemDecoration(object : RecyclerView.ItemDecoration() {

                        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                            super.getItemOffsets(outRect, view, parent, state)


                            outRect.bottom = 20
                            outRect.top = 20
                            outRect.left = 10
                            outRect.right = 10
                        }
                    })
                    rv.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

                    adapter?.setOnItemClick(object :CollectListAdapter.OnItemClick{
                        override fun onActressClick(actress: Actress) {
                            val id = actress.artworkListUrl.split("/").last()
                            ActressInfoActivity.start(id, actress.name, actress, context!!)
                        }

                        override fun onArtworkClick(workItem: ArtWorkItem) {
                            ArtworkDetailActivity.start(context!!, workItem.code!!, workItem.title!!, workItem.photoUrl!!)
                        }

                    })
                }

            }

            override fun onFail() {

                dataLayout.showEmptyContent("暂无收藏内容")
            }

        })

    }

    companion object {

        fun newInstance(): ActressCollectFragment {

            return ActressCollectFragment()
        }
    }

    override fun whenDestroy() {
     }
}