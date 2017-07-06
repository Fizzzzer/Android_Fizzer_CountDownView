package com.fizzer.doraemon.countdownview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Fizzer on 2017/6/20.
 * Email: doraemonmqq@sina.com
 */

public class CountDownView extends View {

    private final int TASK_RUNNING = 0x004;

    private Rect mBaseRect; //矩形区域

    private int mBaseRadius = 50;  //内圈背景圆的半径

    private int mOutCircleStroke = 4;   //外圈进度条的宽度

    private Paint mCirclePaint; //基础的圆画笔
    private Paint mTextPaint;   //文字画笔
    private Paint mCircleStrokePaint;   //外圈圆的画笔

    private float mDrawAngle = 0; //绘制进度条角度

    private float mTime = 5000;  //时间
    private float mTimeStep = 50;    //绘制的时间间隔

    private CountDownListener mListener;

    public CountDownView(Context context) {
        super(context);

        init();
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.e("Fizzer", "init");
        mTextPaint = new Paint();
        mTextPaint.setTextSize(36);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.parseColor("#000000"));

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.parseColor("#A07F7F7F"));
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mCircleStrokePaint = new Paint();
        mCircleStrokePaint.setAntiAlias(true);
        mCircleStrokePaint.setColor(Color.parseColor("#FF0000"));
        mCircleStrokePaint.setStyle(Paint.Style.STROKE);
        mCircleStrokePaint.setStrokeWidth(mOutCircleStroke);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = mBaseRadius * 2 + mOutCircleStroke * 2;
        }

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = mBaseRadius * 2 + mOutCircleStroke * 2;
        }

        int size = Math.max(width, height);

        mBaseRadius = (size - mOutCircleStroke * 2) / 2;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mBaseRect = new Rect();
        getDrawingRect(mBaseRect);
        canvas.drawText("跳过", mBaseRect.centerX(), mBaseRect.centerY() - (mTextPaint.descent() + mTextPaint.ascent()) / 2, mTextPaint);
        canvas.drawCircle(mBaseRect.centerX(), mBaseRect.centerY(), mBaseRadius, mCirclePaint);
        RectF rectF = new RectF(mBaseRect.left + mOutCircleStroke / 2, mBaseRect.top + mOutCircleStroke / 2,
                mBaseRect.right - mOutCircleStroke / 2, mBaseRect.bottom - mOutCircleStroke / 2);
        canvas.drawArc(rectF, -90, mDrawAngle, false, mCircleStrokePaint);
    }

    private float changeTime;
    private float mAngleStep;

    public void start() {
        changeTime = mTime;
        mAngleStep = 360 / (mTime / mTimeStep);
        mHandler.sendMessageDelayed(Message.obtain(mHandler, TASK_RUNNING), (int) mTimeStep);
    }

    private int count = 0;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            count++;
            changeTime -= mTimeStep;
            if (mListener != null) {
                mListener.countDown(changeTime);
            }
            if (changeTime > 0) {
                mDrawAngle = mAngleStep * (count + 1);
                invalidate();
                mHandler.sendMessageDelayed(Message.obtain(mHandler, TASK_RUNNING), (int) mTimeStep);
            } else {
                mHandler.removeMessages(TASK_RUNNING);
                setClickable(true);
            }
        }
    };



    public void setCountDownListener(CountDownListener listener) {
        mListener = listener;
    }

    public interface CountDownListener {
        void countDown(float progress);
    }
}
