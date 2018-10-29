package ywq.ares.funapp.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import com.ares.datacontentlayout.DataContentLayout
import com.google.gson.Gson
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_artwork_detail.*
import ywq.ares.funapp.R
import ywq.ares.funapp.adapter.ArtworkPageAdapter
import ywq.ares.funapp.base.BaseActivity
import ywq.ares.funapp.bean.ArtWorkItem
import ywq.ares.funapp.bean.VideoSearchItem
import ywq.ares.funapp.fragments.*
import ywq.ares.funapp.http.DataSource
import ywq.ares.funapp.util.CacheDataManager
import ywq.ares.funapp.util.SPUtils
import java.io.File

class ArtworkDetailActivity : BaseActivity() {


    private lateinit var code: String
    private lateinit var title: String
    private lateinit var coverUrl: String


    override fun doMain() {

        code = intent.getStringExtra("code")
        title = intent.getStringExtra("title")
        coverUrl = intent.getStringExtra("coverUrl")
        initToolbarSetting(toolbar, "")
        tvTitle.text = code
        dataLayout.showLoading()
        loadData(code)
    }


    private var artDetailDisposable: Disposable? = null
    fun loadData(code: String) {

        DataSource.getVideoList(code, object : DataSource.DataListener<List<VideoSearchItem>> {

            override fun onSuccess(t: List<VideoSearchItem>) {

                println("success")
                println(t)
            }

            override fun onFail() {

                println("onFail")

            }

        })

        this.artDetailDisposable = DataSource.getArtworkDetail(code).subscribe({

            item ->
            val fragmentList = ArrayList<Fragment>()
            val titleList = ArrayList<String>()
            titleList.add(getString(R.string.tab_title_intro))
            titleList.add(getString(R.string.tab_title_artist))
            titleList.add(getString(R.string.tab_title_gallery))
            titleList.add(getString(R.string.tab_title_relate))
            titleList.add(getString(R.string.tab_title_video))
            fragmentList.add(ArtWorkInfoFragment.newInstance(item))
            fragmentList.add(ActressFragment.newInstance(item))
            fragmentList.add(SamplePhotoFragment.newInstance(item))
            fragmentList.add(RelateArtworkFragment.newInstance(item))
            fragmentList.add(VideoFragment.newInstance(item.code!!))
            val adapter = ArtworkPageAdapter(supportFragmentManager, fragmentList, titleList)

            viewPager.adapter = adapter
            //保存缓存
            if (!CacheDataManager.getInstance().cacheExist("artwork-".plus(code))) {

                CacheDataManager.getInstance().saveCache(Gson().toJson(item), "artwork-".plus(code), {})
            }
            tabLayout.setupWithViewPager(viewPager)
            tabLayout.visibility = View.VISIBLE

            dataLayout.showContent()

            btnCollect.visibility = View.VISIBLE
            btnCollect.isSelected = DataSource.isArtWorkCollected(code)
            btnCollect.setOnClickListener {

                val collected = btnCollect.isSelected
                btnCollect.isSelected = when (collected) {
                    false -> {
                        DataSource.collectedArtWork(code, item)
                        toast("已收藏")
                        true
                    }
                    true -> {
                        DataSource.removeCollectedArtWork(code)
                        toast("已取消收藏")

                        false
                    }
                }
            }
        }, {
            dataLayout.showError(object : DataContentLayout.ErrorListener {
                override fun showError(view: View) {
                    loadData(code)
                }
            })
        })
    }

    fun toast(msg: String) {

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
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

        fun start(context: Context, code: String, title: String, coverUrl: String, item: ArtWorkItem) {

            val intent = Intent(context, ArtworkDetailActivity::class.java)

            intent.putExtra("code", code)
            intent.putExtra("title", title)
            intent.putExtra("coverUrl", coverUrl)
            context.startActivity(intent)
        }
    }
}