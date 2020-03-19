package com.kaycloud.framework.util

import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import com.kaycloud.framework.R
import com.kaycloud.framework.util.util.ObjectUtils
import java.util.function.LongFunction
import java.util.jar.Attributes

/**
 * author: jiangyunkai
 * Created_at: 2020/3/17
 */
class ViewHelper {

    companion object {
        private const val RADIUS_ALL = 0
        private const val RADIUS_LEFT = 1
        private const val RADIUS_RIGHT = 2
        private const val RADIUS_TOP = 3
        private const val RADIUS_BOTTOM = 4

        fun setViewOutline(
            view: View,
            attributes: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) {
            val typeArray = view.context.obtainStyledAttributes(
                attributes,
                R.styleable.viewOutLineStrategy,
                defStyleAttr,
                defStyleRes
            )
            val radius =
                typeArray.getDimensionPixelSize(R.styleable.viewOutLineStrategy_clip_radius, 0)
            val hideSide = typeArray.getInt(R.styleable.viewOutLineStrategy_clip_side, 0)
            typeArray.recycle()
            setViewOutline(view, radius, hideSide)
        }

        fun setViewOutline(owner: View, radius: Int, radiusSide: Int) {
            owner.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    view?.let {
                        if (it.width == 0 || it.height == 0) {
                            return
                        }

                        if (radiusSide != RADIUS_ALL) {
                            var left = 0
                            var top = 0
                            var right = 0
                            var bottom = 0

                            when (radiusSide) {
                                RADIUS_LEFT -> right += radius
                                RADIUS_TOP -> bottom += radius
                                RADIUS_RIGHT -> left += radius
                                RADIUS_BOTTOM -> top += radius
                            }

                            outline?.setRoundRect(left, top, right, bottom, radius.toFloat())
                            return
                        }

                        var top = 0
                        var bottom = it.height
                        var left = 0
                        var right = it.width
                        if (radius < 0) {
                            outline?.setRect(left, top, right, bottom)
                        } else {
                            outline?.setRoundRect(left, top, right, bottom, radius.toFloat())
                        }
                    }
                }

            }
            owner.clipToOutline = radius > 0
            owner.invalidate()
        }
    }
}