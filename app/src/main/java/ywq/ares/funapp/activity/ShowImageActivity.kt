package ywq.ares.funapp.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import kotlinx.android.synthetic.main.activity_show_image_list.*

import java.util.ArrayList

import ywq.ares.funapp.R
import ywq.ares.funapp.adapter.PicturePagerAdapter
import ywq.ares.funapp.base.BaseActivity
import ywq.ares.funapp.fragments.PictureFragment

class ShowImageActivity : BaseActivity() {

    private var imagesUrl: List<String>? = null
    private var imageFragments: List<Fragment>? = null
    private var imagePosition: Int = 0

    override fun getLayoutId(): Int {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        return R.layout.activity_show_image_list
    }

    override fun doMain() {

        imagesUrl = intent.getStringArrayListExtra(IMAGES)

        if (imagesUrl == null) {
            finish()
            return
        }
        if (imagesUrl!!.size == 0) {

            finish()
            return
        }
        imagePosition = intent.getIntExtra(POSITION, 0)
        imageFragments = createImageFragments()


        val adapter = PicturePagerAdapter(supportFragmentManager, imageFragments)
        vpImage.setAdapter(adapter)

        vpImage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                tvPictureNum.setText((position + 1).toString() + "/" + imageFragments!!.size)

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })


        vpImage.setCurrentItem(imagePosition)
        tvPictureNum.setText((imagePosition + 1).toString() + "/" + imageFragments!!.size)
    }

    /**
     * 创建多个ImageViewFragment
     * @return
     */
    private fun createImageFragments(): List<Fragment> {
        val fragments = ArrayList<Fragment>()
        if (imagesUrl == null || imagesUrl!!.size == 0) {
            throw RuntimeException("当前图片列表为空，请检查")
        }
        for (url in imagesUrl!!) {

            val pictureFragment = PictureFragment.newInstance(url)
            fragments.add(pictureFragment)
        }

        return fragments
    }

    companion object {


        val IMAGES = "images"
        val POSITION = "position"
        fun start(context: Context, imagesUrl: ArrayList<String>, position: Int) {
            val starter = Intent(context, ShowImageActivity::class.java)
            starter.putStringArrayListExtra(IMAGES, imagesUrl)
            starter.putExtra(POSITION, position)
            context.startActivity(starter)
        }
    }

}
