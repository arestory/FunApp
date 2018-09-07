package ywq.ares.funapp.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import ywq.ares.funapp.R
import ywq.ares.funapp.bean.MovieSearchItem

class ArtWorkInfoFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_artwork_info
    }

    override fun initData(arguments: Bundle, rootView: View) {


        val detail = arguments.getSerializable("movie") as MovieSearchItem



        val ivCover = rootView.findViewById<ImageView>(R.id.ivCover)
        Glide.with(this).asBitmap().load(detail.coverPhotoUrl).into(ivCover)
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
