package ywq.ares.funapp.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.Toast
import com.ares.datacontentlayout.DataContentLayout
import com.bumptech.glide.Glide
import com.google.gson.Gson
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_actress_detail.*
import ywq.ares.funapp.R
import ywq.ares.funapp.adapter.InfoItemAdapter
import ywq.ares.funapp.adapter.SearchItemAdapter
import ywq.ares.funapp.base.BaseActivity
import ywq.ares.funapp.bean.Actress
import ywq.ares.funapp.bean.ArtWorkItem
import ywq.ares.funapp.bean.BaseSearchItem
import ywq.ares.funapp.http.DataSource
import ywq.ares.funapp.util.CacheDataManager

class ActressInfoActivity : BaseActivity() {
    override fun getLayoutId(): Int {

        return R.layout.activity_actress_detail
    }

    private lateinit var adapter: SearchItemAdapter

    private var list: ArrayList<ArtWorkItem> = ArrayList()


    private  var actress: Actress?=null
    private var currentPage = 1
    override fun doMain() {
        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        val obj = intent.getSerializableExtra("item")
        this.actress = if(obj!=null){
           obj as Actress
        }else{
            null
        }

        adapter = SearchItemAdapter(list)
        rvArtwork.adapter = adapter
        tvTitle.text = name
        rvArtwork.layoutManager = getLayoutManager()
        initToolbarSetting(toolbar, "")
        loadActressInfo(id)
        loadWorkList(id, currentPage)
        btnNext.visibility = View.VISIBLE
        btnNext.setOnClickListener {
            loadWorkList(id, ++currentPage)
        }
        btnPre.setOnClickListener {
            loadWorkList(id, --currentPage)

        }
        adapter.setOnItemClickEx(object : SearchItemAdapter.OnItemClickEx {


            override fun onClick(item: BaseSearchItem, position: Int) {

                if (item is ArtWorkItem) {

                    ArtworkDetailActivity.start(this@ActressInfoActivity, item.code!!, item.title!!, item.photoUrl!!)
                }

            }
        })

        rvInfo.isNestedScrollingEnabled = false
        rvArtwork.isNestedScrollingEnabled = false

    }

    private var layoutManager: StaggeredGridLayoutManager? = null


    private fun getLayoutManager(): StaggeredGridLayoutManager {

        if (layoutManager == null) {

            layoutManager = StaggeredGridLayoutManager(3,
                    StaggeredGridLayoutManager.VERTICAL)
            rvArtwork.addItemDecoration(object : RecyclerView.ItemDecoration() {

                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)

                    outRect.bottom = 20
                    outRect.left = 10
                    outRect.right = 10
                }
            })
        }

        return layoutManager!!
    }

    private var lastRequest: Disposable? = null
    private fun loadWorkList(id: String, page: Int) {
        dataLayout2.showLoading()

        currentPage = page
        if (page > 1) {

            btnPre.visibility = View.VISIBLE
        } else {
            btnPre.visibility = View.INVISIBLE
        }


        //取消上一次请求
        if (lastRequest != null) {

            lastRequest?.dispose()
        }
        lastRequest = DataSource.getArtworkListOfActress(id, page).subscribe({


            println("size = ${it.size}")

            if (it.isEmpty()) {

                dataLayout2.showEmptyContent()
                btnNext.visibility = View.GONE
            } else {
                btnNext.visibility = View.VISIBLE

                list.clear()
                list.addAll(it)
                adapter.notifyDataSetChanged()

                dataLayout2.showContent()
                val key = "artist-".plus(id).plus("-artworkList-$page")
                if (!CacheDataManager.getInstance().cacheExist(key)) {
                    CacheDataManager.getInstance().saveCache(Gson().toJson(it), key, {})

                }
            }
            if (it.size < 30) {
                btnNext.visibility = View.GONE
            }

        }, {

            dataLayout2.showError(object : DataContentLayout.ErrorListener {

                override fun showError(view: View) {

                    loadWorkList(id, page)
                }
            })

        })

    }

    fun loadActressInfo(id: String) {
        cardView.visibility = View.INVISIBLE
        dataLayout.showLoading()
        val dis = DataSource.getActressDetail(id)
                .subscribe({ it ->
                    val list = ArrayList<Pair<String, String>>()
                    val url = it.avatar
                    if (DataSource.isOpenPhoto()) {
                        Glide.with(this).load(url).into(ivCover)
                    } else {
                        Glide.with(this).load(R.drawable.avatar).into(ivCover)
                    }
                    ivCover.setOnClickListener {
                        ShowImageActivity.start(this@ActressInfoActivity, arrayListOf(url), 0)
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

                    btnCollect.visibility = View.VISIBLE
                    btnCollect.isSelected =DataSource.isActressCollected(id)
                    btnCollect.setOnClickListener {
                        btnCollect.isSelected = when(btnCollect.isSelected){

                            false -> {
                                list.add(Pair("avatar",url))
                                if(actress!=null){

                                    DataSource.collectedActress(id,actress!!)
                                }
                                toast("已收藏")
                                true
                            }
                            true -> {
                                DataSource.removeCollectedActress(id)
                                toast("已取消收藏")

                                false
                            }


                        }

                    }

                    val adapter = InfoItemAdapter(R.layout.item_actress_info)
                    adapter.setNewData(list)
                    rvInfo.layoutManager = LinearLayoutManager(this@ActressInfoActivity)
                    rvInfo.adapter = adapter
                    rvInfo.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
                    dataLayout.showContent()

                    val key = "artist-info-".plus(id)
                    if (!CacheDataManager.getInstance().cacheExist(key)) {

                        CacheDataManager.getInstance().saveCache(Gson().toJson(it), key, {})
                    }
                }, {


                    dataLayout.showError(object : DataContentLayout.ErrorListener {

                        override fun showError(view: View) {

                            loadActressInfo(id)
                        }
                    })
                })

    }

  private  fun toast(msg:String){

        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
    }


    companion object {


        fun start(id: String, name: String,item:Actress, context: Context) {

            val intent = Intent(context, ActressInfoActivity::class.java)

            intent.putExtra("id", id)
            intent.putExtra("name", name)
            intent.putExtra("item", item)

            context.startActivity(intent)
        }
    }
}