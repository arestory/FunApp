package ywq.ares.funapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.ares.datacontentlayout.DataContentLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_artwork_detail.*
import ywq.ares.funapp.adapter.ArtworkPageAdapter
import ywq.ares.funapp.base.BaseActivity
import ywq.ares.funapp.fragments.*
import ywq.ares.funapp.http.DataSource

class ArtworkDetailActivity : BaseActivity() {


    private lateinit var code: String
    private lateinit var title: String
    private lateinit var coverUrl: String


    override fun doMain() {

        code = intent.getStringExtra("code")
        title = intent.getStringExtra("title")
        coverUrl = intent.getStringExtra("coverUrl")


        initToolbarSetting(toolbar,"详情")

        dataLayout.showLoading()


        loadData(code)


    }


    fun loadData(code:String){

        DataSource.getArtworkDetail(code).subscribe( {

            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            val fragmentList = ArrayList<Fragment>()
            val titleList = ArrayList<String>()
            titleList.add("简介")
            titleList.add("艺术家")
            titleList.add("画廊")
            titleList.add("相关艺术品")
            fragmentList.add(ArtWorkInfoFragment.newInstance(it))
            fragmentList.add(ActressFragment.newInstance(it))
            fragmentList.add(SamplePhotoFragment.newInstance(it))
            fragmentList.add(RelateArtworkFragment.newInstance(it))
            val adapter = ArtworkPageAdapter(supportFragmentManager,fragmentList,titleList)

            viewPager.adapter =adapter

            tabLayout.setupWithViewPager(viewPager)
            tabLayout.visibility =View.VISIBLE
            dataLayout.showContent()
        },{
            dataLayout.showError(object :DataContentLayout.ErrorListener{
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


        fun start(context: Context, code: String,title:String,coverUrl:String) {

            val intent = Intent(context, ArtworkDetailActivity::class.java)

            intent.putExtra("code", code)
            intent.putExtra("title", title)
            intent.putExtra("coverUrl", coverUrl)
            context.startActivity(intent)
        }
    }
}