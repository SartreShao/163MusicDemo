package shaolizhi.demo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;

import shaolizhi.demo.utils.FastBlurUtil;
import shaolizhi.demo.utils.LoadBitmapUtil;


public class MainActivity extends AppCompatActivity {

    final String TAG = getClass().getSimpleName();

    String imageUrl;

    ImageView backGround;

    ImageView alubmCoverBack;

    ImageView alubmCover;

    ObjectAnimator alubmCoverAnimator;

    ObjectAnimator alubmCoverBackAnimator;

    Button startButton;

    Button stopButton;

    private static final int SET_BACKGROUND_IMAGE = 0x00000001;

    private static final int SET_ALUBCOVER_IMAGE = 0x00000002;

    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> mainActivityWeakReference;

        private MyHandler(MainActivity activity) {
            this.mainActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity != null) {
                setBitmapToImageView(msg, mainActivity);
            }
        }
    }

    private static void setBitmapToImageView(Message msg, MainActivity mainActivity) {
        switch (msg.what) {
            case SET_BACKGROUND_IMAGE:
                if (msg.obj instanceof Bitmap) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    mainActivity.backGround.setImageBitmap(bitmap);
                }
                break;
            case SET_ALUBCOVER_IMAGE:
                if (msg.obj instanceof Bitmap) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    mainActivity.alubmCover.setImageBitmap(bitmap);
                }
                break;
            default:
                break;
        }
    }

    private final MyHandler myHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backGround = (ImageView) findViewById(R.id.iv_background);
        alubmCover = (ImageView) findViewById(R.id.iv_alubmCover);
        alubmCoverBack = (ImageView) findViewById(R.id.iv_alubmCoverBack);
        startButton = (Button) findViewById(R.id.bt_start);
        stopButton = (Button) findViewById(R.id.bt_Stop);

        imageUrl = "http://images2015.cnblogs.com/blog/852227/201608/852227-20160803202633403-940757137.jpg";
        setBackGroundImage(imageUrl);
        setAlubmCoverImage(imageUrl);

        alubmCoverAnimator = getDiscObjectAnimator(alubmCover);
        alubmCoverBackAnimator = getDiscObjectAnimator(alubmCoverBack);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alubmCoverAnimator.isPaused()) {
                    alubmCoverAnimator.resume();
                    alubmCoverBackAnimator.resume();
                } else {
                    alubmCoverAnimator.start();
                    alubmCoverBackAnimator.start();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alubmCoverAnimator.pause();
                alubmCoverBackAnimator.pause();
            }
        });
    }

    public void setBackGroundImage(final String imageUrl) {
        if (imageUrl != null && imageUrl.length() != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = FastBlurUtil.GetUrlBitmap(imageUrl, 8);
                    if (bitmap == null) {
                        Log.e(TAG, "网络请求失败");
                        return;
                    }
                    Message message = new Message();
                    message.what = SET_BACKGROUND_IMAGE;
                    message.obj = bitmap;
                    myHandler.sendMessage(message);
                }
            }).start();
        }
    }

    public void setAlubmCoverImage(final String imageUrl) {
        if (imageUrl != null && imageUrl.length() != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap bitmap = LoadBitmapUtil.getBitmap(imageUrl);
                        if (bitmap == null) {
                            return;
                        }
                        Bitmap roundBitmap = LoadBitmapUtil.toRoundBitmap(bitmap);
                        Message message = new Message();
                        message.what = SET_ALUBCOVER_IMAGE;
                        message.obj = roundBitmap;
                        myHandler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private ObjectAnimator getDiscObjectAnimator(ImageView imageView) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, View.ROTATION, 0, 360);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setDuration(20 * 1000);
        objectAnimator.setInterpolator(new LinearInterpolator());

        return objectAnimator;
    }
}
