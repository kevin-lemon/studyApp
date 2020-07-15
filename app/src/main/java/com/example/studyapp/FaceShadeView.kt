package com.example.studyapp

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.graphics.drawable.toBitmap


/**
 * Created by wxk on 2020/7/10.
 */
class FaceShadeView : View {
    private var shadeColor: Int = 0
    private var countDownTime = 10
    private var sweepAngle = 0f
    private var mWidth = 0f
    private var mHeight = 0f
    private var iconWidth = 20
    private var iconHeight = 20
    private var progressMargin = 20f
    private var showProgress = false
    private var isShowState = true
    private var isSuccess = true
    private var animator: ValueAnimator? = null
    private var mPaint = Paint()
    private var iconSuccess: Bitmap? = null
    private var iconFail: Bitmap? = null
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)
    private var src = Rect()
    private var dst = Rect()
    private var stateShapeColor = 0
    private var stateTextColor = 0
    private var progressColor = 0
    private var stateTextSize = 20f
    private var stateTextSuccess = ""
    private var stateTextFail = ""
    constructor(context: Context) : super(context) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FaceShadeView)
        shadeColor = ta.getColor(R.styleable.FaceShadeView_shade_color, Color.WHITE)
        countDownTime = ta.getInt(R.styleable.FaceShadeView_count_down_time, 10)
        progressMargin = ta.getDimension(R.styleable.FaceShadeView_progress_margin, 20f)
        showProgress = ta.getBoolean(R.styleable.FaceShadeView_show_progress, true)
        iconHeight = ta.getDimension(R.styleable.FaceShadeView_icon_height,20f).toInt()
        iconWidth = ta.getDimension(R.styleable.FaceShadeView_icon_width,20f).toInt()
        stateShapeColor = ta.getColor(R.styleable.FaceShadeView_state_circle_shape_color,Color.TRANSPARENT)
        stateTextColor = ta.getColor(R.styleable.FaceShadeView_state_text_color,Color.WHITE)
        progressColor = ta.getColor(R.styleable.FaceShadeView_progress_color,Color.GREEN)
        stateTextSize = ta.getDimension(R.styleable.FaceShadeView_state_text_size,20f)
        stateTextFail = ta.getString(R.styleable.FaceShadeView_state_text_fail)?:""
        stateTextSuccess = ta.getString(R.styleable.FaceShadeView_state_text_success)?:""
        iconSuccess = context.getDrawable(
            ta.getResourceId(
                R.styleable.FaceShadeView_success_icon,
                R.drawable.icon_ok
            )
        )?.toBitmap(iconWidth,iconHeight)
        iconFail = context.getDrawable(
            ta.getResourceId(
                R.styleable.FaceShadeView_fail_icon,
                R.drawable.icon_fail
            )
        )?.toBitmap(iconWidth,iconHeight)

        ta.recycle()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mHeight = measuredHeight.toFloat()
        mWidth = measuredWidth.toFloat()
        canvas?.run {
            mPaint.isAntiAlias = true
            mPaint.style = Paint.Style.FILL
            mPaint.color = shadeColor
            drawRect(0f, mHeight, mWidth, 0f, mPaint)
            mPaint.style = Paint.Style.FILL
            mPaint.strokeWidth = 15F
            mPaint.xfermode = xfermode
            mPaint.color = Color.WHITE
            drawCircle(
                mWidth / 2f,
                mHeight / 2f,
                mWidth / 2f,
                mPaint
            )
            mPaint.xfermode = null
            if (isShowState) {
                mPaint.color = stateShapeColor
                drawCircle(
                    mWidth / 2f,
                    mHeight / 2f,
                    mWidth / 2f,
                    mPaint
                )
                src.set(0,0,iconWidth,iconHeight)
                dst.set(((mWidth-iconWidth)/2).toInt(),((mHeight-iconHeight)/2).toInt(),((mWidth+iconWidth)/2).toInt(),((mWidth+iconWidth)/2).toInt())
                (if (isSuccess) iconSuccess else iconFail)?.let {
                    drawBitmap(it,src,dst,mPaint)
                }
                mPaint.textSize = stateTextSize
                mPaint.color = stateTextColor
                mPaint.textAlign = Paint.Align.CENTER

                val fontMetrics: Paint.FontMetrics = mPaint.fontMetrics
                val distance =
                    (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
                val baseline: Float =mHeight/2+iconHeight + distance
                drawText(if (isSuccess) stateTextSuccess else stateTextFail,mWidth/2,baseline,mPaint)
            }
            if (showProgress) {
                mPaint.color = Color.WHITE
                mPaint.style = Paint.Style.STROKE
                drawCircle(
                    mWidth / 2f,
                    mHeight / 2f,
                    mWidth / 2f - progressMargin,
                    mPaint
                )
                drawProgress(this)
            }
        }
    }

    private fun drawProgress(canvas: Canvas?) {
        mPaint.color = progressColor
        mPaint.style = Paint.Style.STROKE
        val oval = RectF(
            0f + progressMargin,
            0f + progressMargin,
            mWidth - progressMargin,
            mHeight - progressMargin
        ) //用于定义的圆弧的形状和大小的界限
        canvas?.drawArc(oval, 270f, sweepAngle, false, mPaint)
    }

    fun startCountDownAnimation() {
        if (animator?.isRunning == true) {
            return
        }
        isShowState = false
        showProgress = true
        sweepAngle = 0f
        invalidate()
        animator = ObjectAnimator.ofFloat(0f, 360f)
        animator?.run {
            addUpdateListener(AnimatorUpdateListener { animation ->
                sweepAngle = animation.animatedValue as Float
                postInvalidate()
            })
            startDelay = 50
            duration = countDownTime * 1000L
            interpolator = LinearInterpolator() //动画匀速
            start()
        }
    }

    fun stopCountDownAnimation() {
        animator?.cancel()
    }

    fun showState(isSuccess: Boolean) {
        showProgress = false
        isShowState = true
        this.isSuccess = isSuccess
        invalidate()
    }

    fun initView(){
        showProgress = false
        isShowState = false
        isSuccess = false
        invalidate()
    }
}