package ywq.ares.funapp.activity

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.ares.datacontentlayout.DataContentLayout
import com.ares.http.ArtworkApi

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


class SearchActivity : AppCompatActivity() {

    private val adapter = SearchItemAdapter(ArrayList())
    private val api = RetrofitServiceManager.getManager().create(AppConstants.URL.ARTWORK_URL, ArtworkApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        rvInfo.adapter = adapter




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

            val keyword = etKeyword.text.toString()


            val type = when (rg.checkedRadioButtonId) {

                R.id.rbArtwork1 -> {
                    val layoutManager = StaggeredGridLayoutManager(3,
                            StaggeredGridLayoutManager.VERTICAL)
//        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE//不设置的话，图片闪烁错位，有可能有整列错位的情况。


                    rvInfo.addItemDecoration(object : RecyclerView.ItemDecoration() {

                        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                            super.getItemOffsets(outRect, view, parent, state)


                            outRect?.bottom = 20
                            outRect?.left = 10
                            outRect?.right = 10
                        }
                    })
                    rvInfo.layoutManager = layoutManager
                    1
                }
                R.id.rbArtwork2 -> {

                    val layoutManager = StaggeredGridLayoutManager(3,
                            StaggeredGridLayoutManager.VERTICAL)
//        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE//不设置的话，图片闪烁错位，有可能有整列错位的情况。


                    rvInfo.addItemDecoration(object : RecyclerView.ItemDecoration() {

                        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                            super.getItemOffsets(outRect, view, parent, state)


                            outRect?.bottom = 20
                            outRect?.left = 10
                            outRect?.right = 10
                        }
                    })
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
            val page = etPage.text.toString().toInt()
            if (type == 2) {

                DataSource.getActressList(keyword, page)
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


                DataSource.getArtworkList(keyword, page, type)
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


}
