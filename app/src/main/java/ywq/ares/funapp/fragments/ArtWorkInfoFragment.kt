package ywq.ares.funapp.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ywq.ares.funapp.App

import ywq.ares.funapp.R
import ywq.ares.funapp.activity.ShowImageActivity
import ywq.ares.funapp.bean.MovieSearchItem
import ywq.ares.funapp.common.GlideApp
import ywq.ares.funapp.http.DataSource

class ArtWorkInfoFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_artwork_info
    }

    override fun initData(arguments: Bundle, rootView: View) {


        val detail = arguments.getSerializable("movie") as MovieSearchItem



        val ivCover = rootView.findViewById<ImageView>(R.id.ivCover)
        if(DataSource.isOpenPhoto()){




            if(App.isZhLanguage()){
                GlideApp.with(this).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.cover_placeholder_zh).load(detail.coverPhotoUrl).into(ivCover)
            }else{
                GlideApp.with(this).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.cover_placeholder).load(detail.coverPhotoUrl).into(ivCover)
            }
            ivCover.setOnClickListener {

                val list = ArrayList<String>()
                list.add(detail.coverPhotoUrl!!)
                ShowImageActivity.start(context!!, list,0)
            }

        }else{
            if(App.isZhLanguage()){
                GlideApp.with(this).load(R.drawable.cover_placeholder_invisible_zh).into(ivCover)

            }else{

                GlideApp.with(this).load(R.drawable.cover_placeholder_invisible).into(ivCover)
            }

        }

        val tvTitle = rootView.findViewById<TextView>(R.id.tvTitle)

        tvTitle.text = detail.title

        val tvCode = rootView.findViewById<TextView>(R.id.tvCode)
        tvCode.text = detail.code
        val tvDate = rootView.findViewById<TextView>(R.id.tvDate)
        tvDate.text = detail.date
        val tvDuration = rootView.findViewById<TextView>(R.id.tvDuration)

        tvDuration.text = detail.duration


    }

    override fun whenDestroy() {

    }

    companion object {

        fun newInstance(item: MovieSearchItem): ArtWorkInfoFragment {

            val args = Bundle()
            args.putSerializable("movie", item)
            val fragment = ArtWorkInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
