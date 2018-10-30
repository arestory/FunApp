package ywq.ares.funapp.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.ares.datacontentlayout.DataContentLayout
import kotlinx.android.synthetic.main.activity_collect.*
import ywq.ares.funapp.R
import ywq.ares.funapp.adapter.CollectListAdapter
import ywq.ares.funapp.base.BaseActivity
import ywq.ares.funapp.bean.Actress
import ywq.ares.funapp.bean.ArtWorkItem
import ywq.ares.funapp.bean.CollectionBean
import ywq.ares.funapp.http.DataSource

class CollectActivity: BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_collect

    private var adapter:CollectListAdapter? = null
    override fun doMain() {
        initToolbarSetting(toolbar)
        dataLayout.showLoading()
        DataSource.getCollectionBean(object : DataSource.DataListener<List<CollectionBean<*>>>{
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
                    rv.layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)

                    adapter?.setOnItemClick(object :CollectListAdapter.OnItemClick{
                        override fun onActressClick(actress: Actress) {
                            val id = actress.artworkListUrl.split("/").last()
                            ActressInfoActivity.start(id, actress.name, actress, this@CollectActivity)
                        }

                        override fun onArtworkClick(workItem: ArtWorkItem) {
                            ArtworkDetailActivity.start(this@CollectActivity, workItem.code!!, workItem.title!!, workItem.photoUrl!!)
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

        fun start(context: Context){

            context.startActivity(Intent(context,CollectActivity::class.java))
        }
    }
}