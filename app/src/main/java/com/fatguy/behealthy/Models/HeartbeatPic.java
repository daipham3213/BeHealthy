package com.fatguy.behealthy.Models;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.fatguy.behealthy.Activities.HeartRateMonitor;
import com.fatguy.behealthy.R;


/**
 * This class extends the View class and is designed draw the heartbeat image.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class HeartbeatPic extends View {

    private static final Matrix matrix = new Matrix();
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static Bitmap greenBitmap = null;
    private static Bitmap redBitmap = null;

    private static int parentWidth = 0;
    private static int parentHeight = 0;
    private static final String TAG = "HeartbeatPic";

    public HeartbeatPic(Context context, AttributeSet attr) {
        super(context, attr);

        greenBitmap = getBitmap(context,R.drawable.ic_heart);
        redBitmap = getBitmap(context,R.drawable.ic_heart);
    }

    public HeartbeatPic(Context context) {
        super(context);

        greenBitmap = getBitmap(context,R.drawable.ic_heart);
        redBitmap = getBitmap(context,R.drawable.ic_heart);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        Log.e(TAG, "getBitmap: 1");
        return bitmap;
    }

    private static Bitmap getBitmap(Context context, int drawableId) {
        Log.e(TAG, "getBitmap: 2");
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(parentWidth, parentHeight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null) throw new NullPointerException();
        int sizeWidth ;
        Bitmap bitmap = null;
        if (HeartRateMonitor.getCurrent() == HeartRateMonitor.TYPE.GREEN) {
            bitmap = greenBitmap;
            sizeWidth = 190;
        }
        else {
            bitmap = redBitmap;
            sizeWidth = 200;
        }


        int bitmapX = bitmap.getWidth();
        int bitmapY = bitmap.getHeight();
        float scaleWidth = ((float) sizeWidth) / bitmapX;
        float scaleHeight = ((float) sizeWidth) / bitmapY;
        int parentX = parentWidth / 2;
        int parentY = parentHeight / 2;

        int centerX = parentX - (int)sizeWidth/2;
        int centerY = parentY - (int)sizeWidth/2;


        matrix.reset();
        matrix.postScale(scaleWidth,scaleHeight);
        matrix.postTranslate(centerX, centerY);
        canvas.drawBitmap(bitmap, matrix, paint);
    }
}
