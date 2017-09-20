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
 * two public method:
 * (1)void setAlubmCoverImage(Bitmap bitmap) - 设置专辑封面图片
 * (2)void clickPlayButton() - 点击播放，再次点击暂停
 *
 * 注：
 * Vinyl - 黑胶唱片
 */
public class VinylView extends RelativeLayout {

    //唱片外边的黑边,加载图片资源drawable/ic_disc
    ImageView alubmCoverBack;

    //唱片中间的专辑图片
    ImageView alubmCover;

    ObjectAnimator alubmCoverAnimator;

    ObjectAnimator alubmCoverBackAnimator;

    boolean isPlay;

    public VinylView(Context context) {
        this(context, null);
    }

    public VinylView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_vinyl, this);

        alubmCover = findViewById(R.id.iv_alubmCover);
        alubmCoverBack = findViewById(R.id.iv_alubmCoverBack);

        alubmCoverAnimator = getDiscObjectAnimator(alubmCover);
        alubmCoverBackAnimator = getDiscObjectAnimator(alubmCoverBack);

        isPlay = false;
    }

    //set Bitmap on ImageView-alubmCover
    public void setAlubmCoverImage(Bitmap bitmap) {
        alubmCover.setImageBitmap(bitmap);
        postInvalidate();
    }

    //call startRotating() or stopRotating()
    public void clickPlayButton() {
        if (isPlay) {
            stopRotating();
            isPlay = false;
        } else {
            startRotating();
            isPlay = true;
        }
    }

    private ObjectAnimator getDiscObjectAnimator(ImageView imageView) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, View.ROTATION, 0, 360);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setDuration(20 * 1000);
        objectAnimator.setInterpolator(new LinearInterpolator());

        return objectAnimator;
    }

    private void startRotating() {
        if (alubmCoverAnimator.isPaused()) {
            alubmCoverAnimator.resume();
            alubmCoverBackAnimator.resume();
        } else {
            alubmCoverAnimator.start();
            alubmCoverBackAnimator.start();
        }
    }

    private void stopRotating() {
        alubmCoverAnimator.pause();
        alubmCoverBackAnimator.pause();
    }
}
