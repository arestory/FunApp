package ywq.ares.funapp

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.ares.datacontentlayout.DataContentLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_actress_detail.*
import ywq.ares.funapp.adapter.InfoItemAdapter
import ywq.ares.funapp.base.BaseActivity
import ywq.ares.funapp.http.DataSource

class ActressInfoActivity : BaseActivity() {
    override fun getLayoutId(): Int {

        return R.layout.activity_actress_detail
    }

    override fun doMain() {


        val id = intent.getStringExtra("id")


        initToolbarSetting(toolbar,"艺术家")
        loadData(id)

    }

    fun loadData(id: String) {
        dataLayout.showLoading()
        DataSource.getActressDetail(id)
                .subscribe({



                    val list = ArrayList<Pair<String, String>>()


                    Glide.with(this).load(it.avatar).into(ivCover)
                    list.add(Pair("birthday", it.birthday))
                    list.add(Pair("name", it.name))
                    list.add(Pair("age", it.age.toString()))
                    list.add(Pair("cup", it.cup))
                    list.add(Pair("stature", it.stature))
                    list.add(Pair("chestWidth", it.chestWidth))
                    list.add(Pair("waistline", it.waistline))
                    list.add(Pair("hipline", it.hipline))
                    list.add(Pair("home", it.waistline))
                    list.add(Pair("hobby", it.hobby))

                    val adapter = InfoItemAdapter(R.layout.item_actress_info)
                    adapter.setNewData(list)
                    rv.layoutManager = LinearLayoutManager(this@ActressInfoActivity)
                    rv.adapter =adapter
                    dataLayout.showContent()
                }, {


                    dataLayout.showError(object : DataContentLayout.ErrorListener {

                        override fun showError(view: View) {

                            loadData(id)
                        }
                    })
                })

    }

    companion object {

        fun start(id: String, context: Context) {

            val intent = Intent(context, ActressInfoActivity::class.java)

            intent.putExtra("id", id)

            context.startActivity(intent)
        }
    }
}