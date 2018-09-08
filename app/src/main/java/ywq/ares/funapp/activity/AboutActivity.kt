package ywq.ares.funapp.activity

import android.content.Context
import android.content.Intent
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.activity_about.*
import ywq.ares.funapp.R
import ywq.ares.funapp.base.BaseActivity
import ywq.ares.funapp.util.SPUtils

class AboutActivity: BaseActivity() {
    override fun getLayoutId(): Int {

            return R.layout.activity_about
    }

    override fun doMain() {
        initToolbarSetting(toolbar)

        val spUtils = SPUtils("user")
        val flag =spUtils.getBoolean("openPhoto")
        cb.setOnCheckedChangeListener { _, isChecked ->

            spUtils.put("openPhoto",isChecked)
        }
        cb.isChecked = flag

    }


    companion object {


        fun start(context: Context){

            context.startActivity(Intent(context,AboutActivity::class.java))
        }
    }
}