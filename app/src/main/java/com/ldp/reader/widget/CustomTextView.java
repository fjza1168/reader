package com.ldp.reader.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by ldp on 17-4-29.
 * 1. 找到改写TextView内容的方法:是哪个 0 0，个人猜测是setText()
 * 2. 找到文章中存在《》的位置:
 * 3. 设置ForeSpan
 * 4. 添加点击事件
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;

public class CustomTextView extends AppCompatTextView {

    private final static String TAG = CustomTextView.class.getSimpleName();
    private Paint paint1;
    private Paint paint2;

    private int mWidth;
    private LinearGradient gradient;
    private Matrix matrix;
    //渐变的速度
    private int deltaX;

    public CustomTextView(Context context) {
        super(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        paint1 = new Paint();
        paint1.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        paint1.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG,"*********************");

        if(mWidth == 0){
            Log.e(TAG,"*********************");
            mWidth = getMeasuredWidth();
            paint2 = getPaint();
            //颜色渐变器
            gradient = new LinearGradient(0, 0, mWidth, 0, new int[]{Color.BLUE,Color.RED,Color.YELLOW}, new float[]{
                    0.3f,0.5f,1.0f
            }, Shader.TileMode.CLAMP);
            paint2.setShader(gradient);

            matrix = new Matrix();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: ");
        if(matrix !=null){
            deltaX += mWidth / 5;
            if(deltaX > 2 * mWidth){
                deltaX = -mWidth;
            }
        }
        //关键代码通过矩阵的平移实现
        matrix.setTranslate(deltaX, 0);
        gradient.setLocalMatrix(matrix);
//        postInvalidateDelayed(100);
    }
}