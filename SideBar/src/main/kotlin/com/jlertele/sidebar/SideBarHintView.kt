package com.jlertele.sidebar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension

/**
 * <pre>
 * author : leo
 * time   : 2019/02/26
 * desc   : SideBar 提示View
 * </pre>
 */
class SideBarHintView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    /**
     * 提示文字颜色
     */
    @ColorInt
    private var hintTextColor = 0

    /**
     * 提示文字大小
     */
    @Dimension
    private var hintTextSize = 0

    /**
     * 画笔
     */
    private var paint: Paint? = null

    /**
     * 提示文字内容
     */
    private var hintValue: String? = null

    /**
     * X轴绘制起点偏移量
     */
    private var offsetX = 0

    /**
     * init View
     */
    private fun initView() {
        hideHint()
    }

    /**
     * init Paint
     */
    private fun initPaint() {
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.color = hintTextColor
        paint!!.isDither = true
        paint!!.textSize = hintTextSize.toFloat()
    }

    /**
     * 初始化自定义属性
     *
     * @param context
     * @param attributeSet
     */
    private fun initAttr(
        context: Context,
        attributeSet: AttributeSet?
    ) {
        val array =
            context.obtainStyledAttributes(attributeSet, R.styleable.SideBarHintView)
        hintTextColor = array.getColor(
            R.styleable.SideBarHintView_sideHintTextColor,
            Color.WHITE
        )
        hintTextSize = array.getDimensionPixelSize(R.styleable.SideBarHintView_sideHintTextSize, 20)
        offsetX = array.getDimensionPixelOffset(R.styleable.SideBarHintView_sideHintTextOffsetX, 0)
        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas) {
        if (TextUtils.isEmpty(hintValue)) {
            return
        }
        val textWidth = paint!!.measureText(hintValue)
        val x = (width - textWidth) / 2 + offsetX
        val y = height / 2 - (paint!!.ascent() + paint!!.descent()) / 2
        canvas.drawText(hintValue!!, x, y, paint!!)
    }

    /**
     * 显示提示View
     *
     * @param hintValue
     */
    fun showHint(hintValue: String?) {
        visibility = VISIBLE
        this.hintValue = hintValue
        invalidate()
    }

    /**
     * 隐藏提示View
     */
    fun hideHint() {
        visibility = GONE
    }

    /**
     * 测量控件宽度
     *
     * @param widthMeasureSpec
     */
    private fun measureWidth(widthMeasureSpec: Int): Int {
        var result: Int
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val size = MeasureSpec.getSize(widthMeasureSpec)
        if (mode == MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = 100
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size)
            }
        }
        return result
    }

    /**
     * 测量控件高度
     *
     * @param heightMeasureSpec
     */
    private fun measureHeight(heightMeasureSpec: Int): Int {
        var result: Int
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        val size = MeasureSpec.getSize(heightMeasureSpec)
        if (mode == MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = 100
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size)
            }
        }
        return result
    }

    init {
        initAttr(context, attrs)
        initPaint()
        initView()
    }
}
