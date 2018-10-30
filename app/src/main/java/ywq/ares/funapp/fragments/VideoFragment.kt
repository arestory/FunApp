package ywq.ares.funapp.fragments

import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ares.datacontentlayout.DataContentLayout
import ywq.ares.funapp.R
import ywq.ares.funapp.adapter.VideoLinkAdapter
import ywq.ares.funapp.bean.MovieSearchItem
import ywq.ares.funapp.bean.VideoSearchItem
import ywq.ares.funapp.http.DataSource
import android.content.Context.CLIPBOARD_SERVICE
import android.content.ClipboardManager


class VideoFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_video

    lateinit var rv: RecyclerView
    lateinit var dataLayout: DataContentLayout
    override fun initData(arguments: Bundle?, rootView: View?) {

        val code = arguments?.getString("code")
        rv = rootView!!.findViewById(R.id.rv)
        dataLayout = rootView.findViewById(R.id.dataLayout)
        getVideo(code)

    }

    private fun getVideo(code: String?) {
        dataLayout.showLoading()
        DataSource.getVideoList(code!!, object : DataSource.DataListener<List<VideoSearchItem>> {
            override fun onSuccess(t: List<VideoSearchItem>) {

                println(t.size)
                if (t.isNotEmpty()) {
                    val adapter = VideoLinkAdapter()
                    adapter.setNewData(t)
                    rv.adapter = adapter
                    rv.layoutManager = LinearLayoutManager(activity)
                    adapter.setOnItemClick { _, item ->

                        val cm = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cm.text = item.magnet
                        showShortToast("已复制链接到粘贴板")

                    }

                    dataLayout.showContent()
                } else {
                    dataLayout.showEmptyContent()
                }
            }

            override fun onFail() {

                dataLayout.showError(object : DataContentLayout.ErrorListener {
                    override fun showError(view: View) {

                        getVideo(code)
                    }

                })
            }


        })

    }

    override fun whenDestroy() {
    }

    companion object {
        fun newInstance(code: String): VideoFragment {

            val args = Bundle()
            args.putString("code", code)
            val fragment = VideoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}