package shaolizhi.demo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 由邵励治于2017/9/20创造.
 */

public class MyView extends RelativeLayout {

    LayoutInflater layoutInflater;

    ImageView alubmCoverBack;

    ImageView alubmCover;

    ObjectAnimator alubmCoverAnimator;

    ObjectAnimator alubmCoverBackAnimator;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.my_view, this);

        alubmCover = (ImageView) findViewById(R.id.iv_alubmCover);
        alubmCoverBack = (ImageView) findViewById(R.id.iv_alubmCoverBack);

        alubmCoverAnimator = getDiscObjectAnimator(alubmCover);
        alubmCoverBackAnimator = getDiscObjectAnimator(alubmCoverBack);
    }

    private ObjectAnimator getDiscObjectAnimator(ImageView imageView) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, View.ROTATION, 0, 360);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setDuration(20 * 1000);
        objectAnimator.setInterpolator(new LinearInterpolator());

        return objectAnimator;
    }

    public void setAlubmCoverImage(Bitmap bitmap) {
        alubmCover.setImageBitmap(bitmap);
        postInvalidate();
    }

    public void startRotating(){
        if (alubmCoverAnimator.isPaused()) {
            alubmCoverAnimator.resume();
            alubmCoverBackAnimator.resume();
        } else {
            alubmCoverAnimator.start();
            alubmCoverBackAnimator.start();
        }
    }

    public void stopRotating(){
        alubmCoverAnimator.pause();
        alubmCoverBackAnimator.pause();
    }
}
