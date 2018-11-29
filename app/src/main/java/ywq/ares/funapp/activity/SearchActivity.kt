package ywq.ares.funapp.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.ares.datacontentlayout.DataContentLayout
import io.reactivex.disposables.Disposable

import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*
import ywq.ares.funapp.R
import ywq.ares.funapp.adapter.SearchItemAdapter
import ywq.ares.funapp.bean.Actress
import ywq.ares.funapp.bean.ActressSearchItem
import ywq.ares.funapp.bean.ArtWorkItem
import ywq.ares.funapp.bean.BaseSearchItem
import ywq.ares.funapp.http.DataSource
import ywq.ares.funapp.util.KeyboradUtils
import ywq.ares.funapp.util.PermissionUtils


class SearchActivity : AppCompatActivity() {

    private val adapter = SearchItemAdapter(ArrayList())

    private var disposable: Disposable? = null

    private fun checkPermission() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val isPermissionPass = grantResults.any { it == -1 }

        println("权限申请通过 ${!isPermissionPass}")

        if (isPermissionPass) {

            val ignorePer = permissions.map {


                //检测用户是否点击了不再询问
                val flag = ActivityCompat.shouldShowRequestPermissionRationale(this, it)

                println(" it per = $flag")
                flag
            }.any {

                false
            }
            if (!ignorePer) {

                showAlertDialog()


            } else {
                showRequestPermissionDialog()

            }
        }


    }

    private var clickJump = false

    private fun showAlertDialog() {

        val dialog = AlertDialog.Builder(this).setMessage(getString(R.string.title_alert_dialog)).setPositiveButton(getString(R.string.action_jump)) { _, _ ->
            clickJump = true
            PermissionUtils(this@SearchActivity).startPermissionSetting()
        }.setNegativeButton(R.string.action_cancel) { _, _ -> finish() }.create()

        dialog.setOnCancelListener {


            Toast.makeText(this@SearchActivity, getString(R.string.miss_permission_tips), Toast.LENGTH_SHORT).show()
            finish()
        }

        dialog.show()
    }


    private fun showRequestPermissionDialog() {

        val dialog = AlertDialog.Builder(this).setTitle(getString(R.string.miss_permission)).setPositiveButton(getString(R.string.str_retry_apply)) { _, _ -> checkPermission() }.setMessage(getString(R.string.tips_authority)).setNegativeButton(R.string.action_cancel) { _, _ -> finish() }.create()

        dialog.setOnCancelListener {

            Toast.makeText(this@SearchActivity, getString(R.string.miss_permission_tips), Toast.LENGTH_SHORT).show()
            finish()
        }

        dialog.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

         checkPermission()
        toolbar.setOnClickListener {
            nestedSv.scrollTo(0, -100)
        }
        rvInfo.adapter = adapter
        rvInfo.addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect!!, view!!, parent!!, state!!)


                outRect?.bottom = 20
                outRect?.top = 20
                outRect?.left = 10
                outRect?.right = 10
            }
        })



        adapter.setOnItemClickEx(object : SearchItemAdapter.OnItemClickEx {

            override fun onClick(item: BaseSearchItem, position: Int) {


                if (item is ActressSearchItem) {


                    val actress = Actress()
                    actress.name = item.name
                    actress.avatar = item.avatar
                    actress.artworkListUrl = item.workListUrl
                    ActressInfoActivity.start(item.id!!, item.name!!, actress, this@SearchActivity)


                } else if (item is ArtWorkItem) {

                    ArtworkDetailActivity.start(this@SearchActivity, item.code!!, item.title!!, item.photoUrl!!)


                }
            }
        })

        rvInfo.isNestedScrollingEnabled = false
        contentLayout.nonShow()
        rg.setOnCheckedChangeListener { group, checkedId ->

            if(checkedId == R.id.rbMainPage){
                if(inputLayout.visibility==View.VISIBLE){
                    inputLayout.visibility =View.GONE
                }

            }else{
                if(inputLayout.visibility==View.GONE){
                    inputLayout.visibility =View.VISIBLE
                }
            }
        }
        btnSearch.setOnClickListener {

            disposable?.dispose()
            val keyword = etKeyword.text.toString()

            if (keyword == "" && rg.checkedRadioButtonId != R.id.rbMainPage) {

                Toast.makeText(this, getString(R.string.input_keyword_tips), Toast.LENGTH_SHORT).show()
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

                        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                            super.getItemOffsets(outRect!!, view!!, parent!!, state!!)


                            outRect?.bottom = 5
                            outRect?.left = 2
                            outRect?.right = 2
                        }
                    })
                    rvInfo.layoutManager = layoutManager
                    2
                }
                else -> {
                    val layoutManager = StaggeredGridLayoutManager(3,
                            StaggeredGridLayoutManager.VERTICAL)
//        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE//不设置的话，图片闪烁错位，有可能有整列错位的情况。


                    rvInfo.layoutManager = layoutManager
                    3
                }

            }
            contentLayout.showLoading()
            val page = when (etPage.text.toString()) {

                "", "0" -> {
                    etPage.setText("1")
                    1
                }
                else -> etPage.text.toString().toInt()

            }
            if (type == 2) {

                disposable = DataSource.getActressList(keyword, page)
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

                                    btnSearch.performClick()
                                }


                            })
                        })

            }else if(type==3){

                disposable = DataSource.getArtworkListMain(page).subscribe({

                    if(!it.isEmpty()){
                        contentLayout.showEmptyContent(getString(R.string.tips_empty))

                        adapter.setNewData(it)
                        contentLayout.showContent()
                    }else{

                        contentLayout.showEmptyContent()

                    }


                },{

                    it.printStackTrace()
                    contentLayout.showError(object : DataContentLayout.ErrorListener {
                        override fun showError(view: View) {


                            btnSearch.performClick()


                        }
                    })

                })
            }

            else {


                disposable = DataSource.getArtworkList(keyword, page, type)
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

                                    btnSearch.performClick()

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
            R.id.actionCollect -> {
                CollectActivity.start(this)
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

        const val REQUEST_PERMISSION = 1000

        fun start(context: Context) {
            val starter = Intent(context, SearchActivity::class.java)
            context.startActivity(starter)
        }
    }

}
