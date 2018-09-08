package ywq.ares.funapp.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.ares.datacontentlayout.DataContentLayout
import kotlinx.android.synthetic.main.activity_actress_works.*
import ywq.ares.funapp.R
import ywq.ares.funapp.adapter.SearchItemAdapter

import ywq.ares.funapp.base.BaseActivity
import ywq.ares.funapp.http.DataSource

class ArtworkListActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_actress_works
    }

    override fun doMain() {

        etPage.setText("1")

        val id = intent.getStringExtra("id")




        loadData(id, 1)

        btnSearch.setOnClickListener {
            val pageStr = etPage.text.toString()
            val page = if (pageStr.isEmpty()) {

                1
            } else {
                pageStr.toInt()
            }


            loadData(id, page)
        }
    }

   private fun loadData(id: String, page: Int) {
        dataLayout.showLoading()
        btnSearch.isEnabled = false
        DataSource.getArtworkListOfActress(id, page).subscribe({

            btnSearch.isEnabled = true

            if(it.isEmpty()){

                dataLayout.showEmptyContent()
            }else{
                val adapter = SearchItemAdapter(it)
                rvInfo.adapter =adapter


                val layoutManager = StaggeredGridLayoutManager(3,
                        StaggeredGridLayoutManager.VERTICAL)
                rvInfo.addItemDecoration(object : RecyclerView.ItemDecoration() {

                    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                        super.getItemOffsets(outRect, view, parent, state)


                        outRect?.bottom = 20
                        outRect?.left = 10
                        outRect?.right = 10
                    }
                })

                rvInfo.layoutManager =layoutManager

                dataLayout.showContent()

            }


        }, {

            dataLayout.showError(object :DataContentLayout.ErrorListener{

                override fun showError(view: View) {
                 }
            })

        })

    }

    companion object {


        fun start(context: Context, id: String) {
            val starter = Intent(context, ArtworkListActivity::class.java)
            starter.putExtra("id", id)
            context.startActivity(starter)
        }
    }
}
