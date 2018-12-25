package ywq.ares.funapp.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.ares.datacontentlayout.DataContentLayout
import kotlinx.android.synthetic.main.activity_collect.*
import ywq.ares.funapp.R
import ywq.ares.funapp.adapter.ArtworkPageAdapter
import ywq.ares.funapp.base.BaseActivity
import ywq.ares.funapp.fragments.ActressCollectFragment
import ywq.ares.funapp.fragments.ArtworkCollectFragment

class CollectActivity: BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_collect

    override fun doMain() {
        initToolbarSetting(toolbar)
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(ArtworkCollectFragment.newInstance())
        fragmentList.add(ActressCollectFragment.newInstance())
        val titleList = ArrayList<String>()
        titleList.add(resources.getString(R.string.collect_title_artwork))
        titleList.add(resources.getString(R.string.collect_title_actress))
        val adapter = ArtworkPageAdapter(supportFragmentManager, fragmentList, titleList)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

    }

    companion object {

        fun start(context: Context){

            context.startActivity(Intent(context,CollectActivity::class.java))
        }
    }
}