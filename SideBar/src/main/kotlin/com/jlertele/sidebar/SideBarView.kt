package com.jlertele.sidebar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import java.util.*
import kotlin.math.abs

/**
 * <pre>
 * author : leo
 * time   : 2019/02/25
 * desc   : 快速侧滑栏
</pre> *
 */
class SideBarView : View {
    /**
     * 画笔
     */
    private var textPaint: Paint? = null

    /**
     * 背景画笔
     */
    private var bgPaint: Paint? = null

    /**
     * 选中时文字是背景颜色
     */
    @ColorInt
    private var pressedTextBgColor = 0

    /**
     * 选中时文字颜色
     */
    @ColorInt
    private var pressedTextColor = 0

    /**
     * 未选中时文字原生
     */
    @ColorInt
    private var sideTextColor = 0

    /**
     * 文字item高度
     */
    @Dimension
    private var itemHeight = 0

    /**
     * 文字间距
     */
    @Dimension
    private var itemSpacing = 10

    /**
     * 文字大小
     */
    @Dimension
    private var textSize = 0

    /**
     * 侧边栏菜单内容
     */
    private var letters: List<String> =
        ArrayList()

    /**
     * View宽度
     */
    @Dimension
    private var viewWidth = 0

    /**
     * View高度
     */
    @Dimension
    private var viewHeight = 0

    /**
     * 绘制起点x轴位置
     */
    @Dimension
    private var itemStartY = 0f

    /**
     * 选中位置
     */
    private var choosePosition = -1

    /**
     * 触摸回调
     */
    private var onSideBarListener: OnSideBarListener? = null

    constructor(context: Context?) : super(context, null) {}

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        initAttr(context, attrs)
        initPaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            measureSize(widthMeasureSpec, 75).also {
                viewWidth = it
            },
            measureSize(
                heightMeasureSpec, itemHeight * letters.size + itemSpacing
            ).also {
                viewHeight = it
            })
        itemStartY = top + (itemSpacing * 0.5f)
    }

    /**
     * 初始化自定义属性
     *
     * @param context
     * @param attributeSet
     */
    private fun initAttr(context: Context, attributeSet: AttributeSet?) {
        val array =
            context.obtainStyledAttributes(attributeSet, R.styleable.SideBarView)
        pressedTextColor = array.getColor(
            R.styleable.SideBarView_sidePressedTextColor,
            Color.GRAY
        )
        pressedTextBgColor = array.getColor(
            R.styleable.SideBarView_sidePressedTextBgColor,
            Color.TRANSPARENT
        )
        sideTextColor = array.getColor(R.styleable.SideBarView_sideTextColor, Color.BLACK)
        itemSpacing = array.getDimensionPixelOffset(R.styleable.SideBarView_sideItemSpacing, 10)
        textSize = array.getDimensionPixelOffset(R.styleable.SideBarView_sideTextSize, 13)
        itemHeight = array.getDimensionPixelOffset(R.styleable.SideBarView_sideItemHeight, textSize)
        itemHeight += itemSpacing
        array.recycle()
    }

    /**
     * 初始化sideBar菜单内容
     */
    private fun initPaint() {
        textPaint = Paint()
        textPaint!!.textSize = textSize.toFloat()
        textPaint!!.isDither = true
        textPaint!!.isAntiAlias = true
        textPaint!!.color = sideTextColor
        bgPaint = Paint()
        bgPaint!!.isAntiAlias = true
        bgPaint!!.isDither = true
        bgPaint!!.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        onDrawText(canvas)
    }

    /**
     * 绘制文字
     *
     * @param canvas
     */
    private fun onDrawText(canvas: Canvas) {
        for (i in letters.indices) {
            val text = letters[i]
            val textWidth = textPaint!!.measureText(text)
            val drawX = (viewWidth - textWidth) * 0.5f
            val textHeight = (abs(textPaint!!.ascent()) - textPaint!!.descent()) / 2
            val drawY =
                itemHeight * i + itemHeight * 0.5f + textHeight + itemStartY
            if (i == choosePosition) {
                textPaint!!.color = pressedTextColor
                bgPaint!!.color = pressedTextBgColor
                val cx = (drawX + textWidth * 0.5).toFloat()
                val cy =
                    drawY - textHeight
                val radius = textSize / 2 + textSize * 0.2f
                canvas.drawCircle(cx, cy, radius, bgPaint!!)
            } else {
                textPaint!!.color = sideTextColor
            }
            canvas.drawText(text, drawX, drawY, textPaint!!)
        }
    }

    @SuppressLint("LongLogTag")
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val y = event.y
        val action = event.action
        val oldChoosePosition = choosePosition
        val newChoosePosition = ((y - itemStartY) / itemHeight).toInt()
        when (action) {
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                choosePosition = -1
                if (onSideBarListener != null) {
                    onSideBarListener!!.onSideTouchState(this, false)
                }
                invalidate()
            }
            else -> {
                if (action == MotionEvent.ACTION_DOWN) {
                    if (onSideBarListener != null) {
                        onSideBarListener!!.onSideTouchState(this, true)
                    }
                }
                if (oldChoosePosition != newChoosePosition) {
                    if (newChoosePosition >= 0 && newChoosePosition < letters.size) {
                        setSelectPosition(newChoosePosition)
                    }
                    invalidate()
                }
            }
        }
        return true
    }


    /**
     * 设置选中位置
     * @param position Int
     */
    private fun setSelectPosition(position: Int) {
        choosePosition = position
        val currentY = itemStartY + choosePosition * itemHeight - itemHeight / 2 + getY()
        if (onSideBarListener != null) {
            val letter = letters[choosePosition]
            onSideBarListener!!.onSideSelected(
                this, choosePosition, currentY, letter
            )
        }
    }

    /**
     * 初始化sideBar菜单内容
     *
     * @param letters
     * @return
     */
    fun setLetters(letters: List<String>): SideBarView {
        this.letters = letters
        updateViewHeight()
        postInvalidate()
        return this
    }

    /**
     * 根据Latter数量动态更新控件高度
     */
    private fun updateViewHeight() {
        val layoutParams = layoutParams
        layoutParams.height = letters.size * itemHeight + itemHeight
        setLayoutParams(layoutParams)
    }

    /**
     * 测量尺寸
     *
     * @param measureSpec
     * @param defaultSize
     * @return
     */
    private fun measureSize(measureSpec: Int, defaultSize: Int): Int {
        var result: Int
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        if (mode == MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = defaultSize
            if (mode == MeasureSpec.AT_MOST) {
                result = result.coerceAtMost(size)
            }
        }
        return result
    }

    /**
     * SideBar 点击回调
     */
    interface OnSideBarListener {
        fun onSideTouchState(sideBarView: SideBarView?, isTouch: Boolean)
        fun onSideSelected(
            sideBarView: SideBarView?,
            position: Int,
            currentY: Float,
            selectedValue: String?
        )
    }

    /**
     * set OnSideBarListener
     *
     * @param onSideBarListener
     * @return SideBarView
     */
    fun setSideBarListener(onSideBarListener: OnSideBarListener?): SideBarView {
        this.onSideBarListener = onSideBarListener
        return this
    }
}
