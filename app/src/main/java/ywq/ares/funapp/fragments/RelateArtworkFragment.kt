package ywq.ares.funapp.fragments

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.TextView
import ywq.ares.funapp.activity.ArtworkDetailActivity
import ywq.ares.funapp.R
import ywq.ares.funapp.adapter.SearchItemAdapter
import ywq.ares.funapp.bean.ArtWorkItem
import ywq.ares.funapp.bean.BaseSearchItem
import ywq.ares.funapp.bean.MovieSearchItem

class RelateArtworkFragment :BaseFragment(){
    override fun getLayoutId(): Int {


        return R.layout.fragment_relate_artwork

    }

    override fun initData(arguments: Bundle?, rootView: View?) {
        val detail = arguments!!.getSerializable("movie") as MovieSearchItem

        val rv = rootView!!.findViewById<RecyclerView>(R.id.rvInfo)

        val list = ArrayList<ArtWorkItem>()
        val adapter = SearchItemAdapter(list)

        adapter.setOnItemClickEx(object :SearchItemAdapter.OnItemClickEx{

            override fun onClick(item: BaseSearchItem, position: Int) {
                if(item is ArtWorkItem){

                    ArtworkDetailActivity.start(context!!,item.code!!,item.title!!,item.photoUrl!!)
                }
            }
        })
        detail.relateArtWorkList.forEach {

            val item =ArtWorkItem()

            item.code = it.code
            item.movieUrl = it.url
            item.photoUrl = it.coverPhotoUrl
            item.title = it.title

            list.add(item)


        }
        rv.adapter=adapter
        val layoutManager = StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL)
//        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE//不设置的话，图片闪烁错位，有可能有整列错位的情况。

        if(list.isEmpty()){

            val tvEmpty = rootView.findViewById<TextView>(R.id.tvEmpty)
            tvEmpty.visibility =View.VISIBLE
        }
        rv.addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect!!, view!!, parent!!, state!!)


                outRect?.bottom = 20
                outRect?.left = 10
                outRect?.right = 10
            }
        })
        rv.layoutManager = layoutManager



     }


    companion object {

        fun newInstance(item: MovieSearchItem): RelateArtworkFragment {

            val args = Bundle()
            args.putSerializable("movie", item)
            val fragment = RelateArtworkFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun whenDestroy() {
     }
}