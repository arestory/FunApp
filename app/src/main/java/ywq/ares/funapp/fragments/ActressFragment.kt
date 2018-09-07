package ywq.ares.funapp.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_actresses.*
import ywq.ares.funapp.R
import ywq.ares.funapp.adapter.ActressesAdapter
import ywq.ares.funapp.bean.Actress
import ywq.ares.funapp.bean.MovieSearchItem

class ActressFragment : BaseFragment() {
    override fun getLayoutId(): Int {

        return R.layout.fragment_actresses
    }

    override fun initData(arguments: Bundle?, rootView: View?) {

        val rv = rootView!!.findViewById<RecyclerView>(R.id.rv)


        val detail = arguments!!.getSerializable("movie") as MovieSearchItem


        val adapter = ActressesAdapter()

        val actresses = ArrayList<Actress>()
        detail.actresses.forEach {

            val actress = Actress(it.name, it.avatar, it.workListUrl)
            actresses.add(actress)
        }
        adapter.setNewData(actresses)
        rv.addItemDecoration(DividerItemDecoration(activity,VERTICAL))
        rv.layoutManager = LinearLayoutManager(rv.context)
        rv.adapter = adapter
        if(actresses.isEmpty()){

            val tvEmpty = rootView.findViewById<TextView>(R.id.tvEmpty)
            tvEmpty.visibility =View.VISIBLE
        }
    }


    override fun whenDestroy() {
    }

    companion object {


        fun newInstance(item: MovieSearchItem): ActressFragment {

            val args = Bundle()
            args.putSerializable("movie", item)
            val fragment = ActressFragment()
            fragment.arguments = args
            return fragment
        }
    }
}