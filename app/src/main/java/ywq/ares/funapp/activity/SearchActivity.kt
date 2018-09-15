package ywq.ares.funapp.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.ares.datacontentlayout.DataContentLayout
import com.ares.http.ArtworkApi
import io.reactivex.disposables.Disposable

import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*
import ywq.ares.funapp.R
import ywq.ares.funapp.adapter.SearchItemAdapter
import ywq.ares.funapp.http.AppConstants
import ywq.ares.funapp.http.RetrofitServiceManager
import ywq.ares.funapp.bean.ActressSearchItem
import ywq.ares.funapp.bean.ArtWorkItem
import ywq.ares.funapp.bean.BaseSearchItem
import ywq.ares.funapp.http.DataSource
import ywq.ares.funapp.util.KeyboradUtils


class SearchActivity : AppCompatActivity() {

    private val adapter = SearchItemAdapter(ArrayList())

    private var disposable:Disposable?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        rvInfo.adapter = adapter
        rvInfo.addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)


                outRect?.bottom = 20
                outRect?.top = 20
                outRect?.left = 10
                outRect?.right = 10
            }
        })



        adapter.setOnItemClickEx(object : SearchItemAdapter.OnItemClickEx {

            override fun onClick(item: BaseSearchItem, position: Int) {


                if (item is ActressSearchItem) {


                    ActressInfoActivity.start(item.id!!, item.name!!,this@SearchActivity)


                } else if (item is ArtWorkItem) {

                    ArtworkDetailActivity.start(this@SearchActivity, item.code!!, item.title!!, item.photoUrl!!)


                }
            }
        })

        rvInfo.isNestedScrollingEnabled = false
        contentLayout.nonShow()
        btnSearch.setOnClickListener {

            disposable?.dispose()
            val keyword = etKeyword.text.toString()



            if(keyword == ""){

                Toast.makeText(this,"请输入关键词",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            KeyboradUtils.hide(it)
            val type = when (rg.checkedRadioButtonId) {

                R.id.rbArtwork1 -> {
                    val layoutManager = StaggeredGridLayoutManager(3,
                            StaggeredGridLayoutManager.VERTICAL)
//        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE//不设置的话，图片闪烁错位，有可能有整列错位的情况。



                    rvInfo.layoutManager = layoutManager
                    1
                }
                R.id.rbArtwork2 -> {

                    val layoutManager = StaggeredGridLayoutManager(3,
                            StaggeredGridLayoutManager.VERTICAL)
//        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE//不设置的话，图片闪烁错位，有可能有整列错位的情况。



                    rvInfo.layoutManager = layoutManager
                    0
                }
                R.id.rbActress -> {

                    val layoutManager = StaggeredGridLayoutManager(4,
                            StaggeredGridLayoutManager.VERTICAL)
//        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE//不设置的话，图片闪烁错位，有可能有整列错位的情况。


                    rvInfo.addItemDecoration(object : RecyclerView.ItemDecoration() {

                        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                            super.getItemOffsets(outRect, view, parent, state)


                            outRect?.bottom = 5
                            outRect?.left = 2
                            outRect?.right = 2
                        }
                    })
                    rvInfo.layoutManager = layoutManager
                    2
                }
                else -> -1

            }
            contentLayout.showLoading()
            val page = when(etPage.text.toString()){

                "" ,"0"->
                {
                    etPage.setText("1")
                    1
                }
                else -> etPage.text.toString().toInt()

            }
            if (type == 2) {

                disposable=   DataSource.getActressList(keyword, page)
                        .subscribe({


                            if (!it.isEmpty()) {

                                contentLayout.showEmptyContent(getString(R.string.tips_empty))

                                adapter.setNewData(it)
                                contentLayout.showContent()
                            } else {
                                contentLayout.showEmptyContent()

                            }
                        }, {

                            it.printStackTrace()
                            contentLayout.showError(object : DataContentLayout.ErrorListener {
                                override fun showError(view: View) {


                                }


                            })
                        })

            } else {


                disposable=    DataSource.getArtworkList(keyword, page, type)
                        .subscribe({


                            if (!it.isEmpty()) {

                                contentLayout.showEmptyContent(getString(R.string.tips_empty))

                                adapter.setNewData(it)
                                contentLayout.showContent()
                            } else {
                                contentLayout.showEmptyContent()

                            }
                        }, {

                            it.printStackTrace()
                            contentLayout.showError(object : DataContentLayout.ErrorListener {
                                override fun showError(view: View) {


                                }


                            })
                        })

            }


        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return when (id) {

            R.id.actionAbout -> {
                AboutActivity.start(this)
                true

            }
            else -> false
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
    companion object {


        fun start(context: Context) {
            val starter = Intent(context, SearchActivity::class.java)
            context.startActivity(starter)
        }
    }

}
