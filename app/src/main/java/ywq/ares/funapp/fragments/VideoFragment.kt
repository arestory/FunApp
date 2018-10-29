package ywq.ares.funapp.fragments

import android.os.Bundle
import android.view.View
import ywq.ares.funapp.R
import ywq.ares.funapp.bean.MovieSearchItem
import ywq.ares.funapp.bean.VideoSearchItem
import ywq.ares.funapp.http.DataSource

class VideoFragment : BaseFragment(){
    override fun getLayoutId(): Int = R.layout.fragment_actresses

    override fun initData(arguments: Bundle?, rootView: View?) {

        val code = arguments?.getString("code")
        DataSource.getVideoList(code!!,object :DataSource.DataListener<List<VideoSearchItem>>{
            override fun onSuccess(t: List<VideoSearchItem>) {

                println(t.size)
            }

            override fun onFail() {
             }


        })
     }

    override fun whenDestroy() {
     }

    companion object {
        fun newInstance(code: String): VideoFragment {

            val args = Bundle()
            args.putString("code",code)
            val fragment = VideoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}