package ywq.ares.funapp.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.ares.datacontentlayout.DataContentLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_actress_detail.*
import ywq.ares.funapp.R
import ywq.ares.funapp.adapter.InfoItemAdapter
import ywq.ares.funapp.adapter.SearchItemAdapter
import ywq.ares.funapp.base.BaseActivity
import ywq.ares.funapp.bean.ArtWorkItem
import ywq.ares.funapp.bean.BaseSearchItem
import ywq.ares.funapp.http.DataSource

class ActressInfoActivity : BaseActivity() {
    override fun getLayoutId(): Int {

        return R.layout.activity_actress_detail
    }
    private lateinit var adapter:SearchItemAdapter

    private var list:ArrayList<ArtWorkItem> = ArrayList()


    private var currentPage = 1
    override fun doMain() {


        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")

        adapter =  SearchItemAdapter(list)
        rvArtwork.adapter =adapter
        initToolbarSetting(toolbar,name)
        loadActressInfo(id)
        loadWorkList(id,currentPage)

        btnNext.visibility =View.VISIBLE
        btnNext.setOnClickListener {

            loadWorkList(id,++currentPage)
        }
        btnPre.setOnClickListener {
            loadWorkList(id,--currentPage)

        }
        adapter.setOnItemClickEx(object :SearchItemAdapter.OnItemClickEx{


            override fun onClick(item: BaseSearchItem, position: Int) {

                if(item is ArtWorkItem){

                    ArtworkDetailActivity.start(this@ActressInfoActivity, item.code!!, item.title!!, item.photoUrl!!)
                }

            }
        })

        rvInfo.isNestedScrollingEnabled = false
        rvArtwork.isNestedScrollingEnabled = false

    }


    private fun getLayoutManager():StaggeredGridLayoutManager{

        val layoutManager = StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL)
        rvArtwork.addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)


                outRect?.bottom = 20
                outRect?.left = 10
                outRect?.right = 10
            }
        })
        return layoutManager
    }
    private fun loadWorkList(id: String,page :Int){
        dataLayout2.showLoading()

        currentPage = page
        if(page>1){

            btnPre.visibility =View.VISIBLE
        }else{
            btnPre.visibility =View.INVISIBLE
        }
        DataSource.getArtworkListOfActress(id,page).subscribe({


            println("size = ${it.size}")

            if(it.isEmpty()){

                dataLayout2.showEmptyContent()
                btnNext.visibility=View.GONE
            }else{
                btnNext.visibility=View.VISIBLE

                list.clear()
                list.addAll(it)
                adapter.notifyDataSetChanged()


                rvArtwork.layoutManager =getLayoutManager()

                dataLayout2.showContent()

            }
            if(it.size<30){
                btnNext.visibility=View.GONE
            }

        },{

            dataLayout2.showError(object :DataContentLayout.ErrorListener{

                override fun showError(view: View) {

                    loadWorkList(id,page)
                }
            })

        })

    }

    fun loadActressInfo(id: String) {
        cardView.visibility = View.INVISIBLE

        dataLayout.showLoading()
        DataSource.getActressDetail(id)
                .subscribe({



                    val list = ArrayList<Pair<String, String>>()


                    if(DataSource.isOpenPhoto()){

                        Glide.with(this).load(it.avatar).into(ivCover)
                    }else{
                        Glide.with(this).load(R.drawable.avatar).into(ivCover)

                    }
                    cardView.visibility = View.VISIBLE

                    list.add(Pair("name", it.name))
                    list.add(Pair("birthday", it.birthday))
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
                    rvInfo.layoutManager = LinearLayoutManager(this@ActressInfoActivity)
                    rvInfo.adapter =adapter
                    rvInfo.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))
                    dataLayout.showContent()
                }, {


                    dataLayout.showError(object : DataContentLayout.ErrorListener {

                        override fun showError(view: View) {

                            loadActressInfo(id)
                        }
                    })
                })

    }

    companion object {

        fun start(id: String,name:String, context: Context) {

            val intent = Intent(context, ActressInfoActivity::class.java)

            intent.putExtra("id", id)
            intent.putExtra("name", name)

            context.startActivity(intent)
        }
    }
}