package com.ldp.reader.widget.animation;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

/**
 * Created by ldp on 17-7-24.
 */

public class NonePageAnim extends HorizonPageAnim{
    private final static String TAG = "NonePageAnim";

    public NonePageAnim(int w, int h, View view, OnPageChangeListener listener) {
        super(w, h, view, listener);
    }

    @Override
    public void drawStatic(Canvas canvas) {
        if (isCancel){
            canvas.drawBitmap(mCurBitmap, 0, 0, null);
        }else {
            Log.d(TAG,"+drawStaticmNextBitmap");
            canvas.drawBitmap(mNextBitmap, 0, 0, null);
        }
    }

    @Override
    public void drawMove(Canvas canvas) {
        if (isCancel){
            canvas.drawBitmap(mCurBitmap, 0, 0, null);
        }else {
            Log.d(TAG,"+drawMovemNextBitmap");
            canvas.drawBitmap(mNextBitmap, 0, 0, null);
        }
    }

    @Override
    public void startAnim() {
    }
}
