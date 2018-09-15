package ywq.ares.funapp.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View
import com.ares.datacontentlayout.DataContentLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_artwork_detail.*
import ywq.ares.funapp.R
import ywq.ares.funapp.adapter.ArtworkPageAdapter
import ywq.ares.funapp.base.BaseActivity
import ywq.ares.funapp.fragments.*
import ywq.ares.funapp.http.DataSource
import ywq.ares.funapp.util.CacheDataManager
import java.io.File

class ArtworkDetailActivity : BaseActivity() {


    private lateinit var code: String
    private lateinit var title: String
    private lateinit var coverUrl: String


    override fun doMain() {

        code = intent.getStringExtra("code")
        title = intent.getStringExtra("title")
        coverUrl = intent.getStringExtra("coverUrl")


        initToolbarSetting(toolbar, code)

        dataLayout.showLoading()


        loadData(code)


    }


    fun loadData(code: String) {

        DataSource.getArtworkDetail(code).subscribe({

            val fragmentList = ArrayList<Fragment>()
            val titleList = ArrayList<String>()
            titleList.add(getString(R.string.tab_title_intro))
            titleList.add(getString(R.string.tab_title_artist))
            titleList.add(getString(R.string.tab_title_gallery))
            titleList.add(getString(R.string.tab_title_relate))
            fragmentList.add(ArtWorkInfoFragment.newInstance(it))
            fragmentList.add(ActressFragment.newInstance(it))
            fragmentList.add(SamplePhotoFragment.newInstance(it))
            fragmentList.add(RelateArtworkFragment.newInstance(it))
            val adapter = ArtworkPageAdapter(supportFragmentManager, fragmentList, titleList)

            viewPager.adapter = adapter
            //保存缓存
            if (!CacheDataManager.getInstance().cacheExist("artwork-".plus(code))) {

                CacheDataManager.getInstance().saveCache(Gson().toJson(it), "artwork-".plus(code), {})
            }
            tabLayout.setupWithViewPager(viewPager)
            tabLayout.visibility = View.VISIBLE

            dataLayout.showContent()
        }, {
            dataLayout.showError(object : DataContentLayout.ErrorListener {
                override fun showError(view: View) {


                    loadData(code)

                }


            })


        })
    }


    override fun getLayoutId(): Int {

        return R.layout.activity_artwork_detail
    }

    companion object {


        fun start(context: Context, code: String, title: String, coverUrl: String) {

            val intent = Intent(context, ArtworkDetailActivity::class.java)

            intent.putExtra("code", code)
            intent.putExtra("title", title)
            intent.putExtra("coverUrl", coverUrl)
            context.startActivity(intent)
        }
    }
}