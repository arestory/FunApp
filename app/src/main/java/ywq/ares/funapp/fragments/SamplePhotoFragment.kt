package ywq.ares.funapp.fragments

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import ywq.ares.funapp.R
import ywq.ares.funapp.adapter.SamplePhotoAdapter
import ywq.ares.funapp.bean.MovieSearchItem

class SamplePhotoFragment: BaseFragment() {
    override fun getLayoutId(): Int {

        return R.layout.fragment_photo
    }

    override fun initData(arguments: Bundle?, rootView: View?) {

        val obj = arguments!!.getSerializable("movie") as MovieSearchItem
        val rv = rootView!!.findViewById<RecyclerView>(R.id.rvInfo)
        val list = ArrayList<String>()

        list.addAll(obj.samplePhotos)
        val adapter=SamplePhotoAdapter(R.layout.item_sample_photo,list)
        rv.layoutManager = LinearLayoutManager(activity)

        rv.addItemDecoration(DividerItemDecoration(activity,VERTICAL))
        rv.adapter =adapter

        if(list.isEmpty()){

            val tv = rootView.findViewById<TextView>(R.id.tvEmpty)
            tv.visibility = View.VISIBLE
        }

    }
    companion object {


        fun newInstance(item: MovieSearchItem): SamplePhotoFragment {

            val args = Bundle()
            args.putSerializable("movie", item)
            val fragment = SamplePhotoFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun whenDestroy() {
     }
}