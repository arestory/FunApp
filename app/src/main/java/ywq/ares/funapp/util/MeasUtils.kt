package ywq.ares.funapp.util

import android.content.Context
import android.util.TypedValue



class MeasUtils {

    companion object {
        fun pxToDp(px: Int, context: Context): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px.toFloat(),
                    context.resources.displayMetrics).toInt()
        }

        fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                    context.resources.displayMetrics).toInt()
        }

    }

}