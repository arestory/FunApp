package ywq.ares.funapp

import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.support.v7.widget.LinearLayoutManager.HORIZONTAL
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.Toast
import com.ares.datacontentlayout.DataContentLayout
import com.ares.http.ArtworkApi
import com.fivehundredpx.greedolayout.GreedoLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*
import ywq.ares.funapp.adapter.SearchItemAdapter
import ywq.ares.funapp.http.AppConstants
import ywq.ares.funapp.http.RetrofitServiceManager
import com.fivehundredpx.greedolayout.GreedoSpacingItemDecoration
import ywq.ares.funapp.bean.ActressSearchItem
import ywq.ares.funapp.bean.ArtWorkItem
import ywq.ares.funapp.bean.BaseSearchItem
import ywq.ares.funapp.http.DataSource


class SearchActivity : AppCompatActivity() {

    private val adapter = SearchItemAdapter(ArrayList())
   private val api =  RetrofitServiceManager.getManager().create(AppConstants.URL.ARTWORK_URL,ArtworkApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        rv.adapter =adapter




        adapter.setOnItemClickEx(object :SearchItemAdapter.OnItemClickEx{

            override fun onClick(item: BaseSearchItem, position: Int) {


                if(item is ActressSearchItem){

                    Toast.makeText(this@SearchActivity,item.name,Toast.LENGTH_SHORT).show()
                    DataSource.getActressDetail(item.id!!)
                            .subscribe {

                                Toast.makeText(this@SearchActivity,it.toString(),Toast.LENGTH_SHORT).show()


                            }
                }else if(item is ArtWorkItem){
                    Toast.makeText(this@SearchActivity,item.title,Toast.LENGTH_SHORT).show()

                   DataSource.getArtworkDetail(item.code!!) .subscribe {

                                Toast.makeText(this@SearchActivity,it.toString(),Toast.LENGTH_SHORT).show()

                            }


                }
            }
        })


        contentLayout.nonShow()
        btnSearch.setOnClickListener {

            val keyword = etKeyword.text.toString()


            val type = when(rg.checkedRadioButtonId){

                R.id.rbArtwork1 ->{
                    val layoutManager = StaggeredGridLayoutManager(3,
                            StaggeredGridLayoutManager.VERTICAL)
//        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE//不设置的话，图片闪烁错位，有可能有整列错位的情况。


                    rv.addItemDecoration(object : RecyclerView.ItemDecoration() {

                        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                            super.getItemOffsets(outRect, view, parent, state)


                            outRect?.bottom = 20
                            outRect?.left = 10
                            outRect?.right = 10
                        }
                    })
                    rv.layoutManager=layoutManager
                    1
                }
                R.id.rbArtwork2 ->{

                    val layoutManager = StaggeredGridLayoutManager(3,
                            StaggeredGridLayoutManager.VERTICAL)
//        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE//不设置的话，图片闪烁错位，有可能有整列错位的情况。


                    rv.addItemDecoration(object : RecyclerView.ItemDecoration() {

                        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                            super.getItemOffsets(outRect, view, parent, state)


                            outRect?.bottom = 20
                            outRect?.left = 10
                            outRect?.right = 10
                        }
                    })
                    rv.layoutManager=layoutManager
                    0}
                R.id.rbActress -> {

                    val layoutManager = StaggeredGridLayoutManager(4,
                            StaggeredGridLayoutManager.VERTICAL)
//        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE//不设置的话，图片闪烁错位，有可能有整列错位的情况。


                    rv.addItemDecoration(object : RecyclerView.ItemDecoration() {

                        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                            super.getItemOffsets(outRect, view, parent, state)


                            outRect?.bottom = 5
                            outRect?.left = 2
                            outRect?.right = 2
                        }
                    })
                    rv.layoutManager=layoutManager
                    2
                }
                else -> -1

            }
            contentLayout.showLoading()
            val page = etPage.text.toString().toInt()
            if(type==2){

               DataSource.getActressList(keyword,page)
                        .subscribe({


                            if(!it.isEmpty()){

                                contentLayout.showEmptyContent("空内容")

                                adapter.setNewData(it)
                                contentLayout.showContent()
                            }else{
                                contentLayout.showEmptyContent()

                            }
                        },{

                            it.printStackTrace()
                            contentLayout.showError(object :DataContentLayout.ErrorListener{
                                override fun showError(view: View) {


                                }


                            })
                        })

            }else{


               DataSource.getArtworkList(keyword,page,type)
                        .subscribe({


                            if(!it.isEmpty()){

                                contentLayout.showEmptyContent("空内容")

                                adapter.setNewData(it)
                                contentLayout.showContent()
                            }else{
                                contentLayout.showEmptyContent()

                            }
                        },{

                            it.printStackTrace()
                            contentLayout.showError(object :DataContentLayout.ErrorListener{
                                override fun showError(view: View) {


                                }


                            })
                        })

            }




        }

    }

}
