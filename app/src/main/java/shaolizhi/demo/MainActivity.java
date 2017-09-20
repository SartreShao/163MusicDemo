package shaolizhi.demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;

import shaolizhi.demo.utils.FastBlurUtil;
import shaolizhi.demo.utils.LoadBitmapUtil;


public class MainActivity extends AppCompatActivity {

    final String TAG = getClass().getSimpleName();

    //图片的URL,更改后调用setBackGroundImage与setAlubmCoverImage应用更改
    String imageUrl;

    //高斯模糊的背景
    ImageView backGround;

    //自定义播放转盘
    VinylView vinylView;

    //转盘的播放按钮
    Button playButton;

    private final MyHandler myHandler = new MyHandler(this);

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
                setBitmapToUI(msg, mainActivity);
            }
        }
    }

    //call by MyHandler.handleMessage(Message msg)
    private static void setBitmapToUI(Message msg, MainActivity mainActivity) {
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
                    mainActivity.vinylView.setAlubmCoverImage(bitmap);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //控件绑定
        backGround = (ImageView) findViewById(R.id.iv_background);
        vinylView = (VinylView) findViewById(R.id.my_view);
        playButton = (Button) findViewById(R.id.bt_play);

        //点击事件
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vinylView.clickPlayButton();
            }
        });

        //设置图标URL
        imageUrl = "http://images2015.cnblogs.com/blog/852227/201608/852227-20160803202633403-940757137.jpg";
        setBackGroundImage(imageUrl);
        setAlubmCoverImage(imageUrl);
    }

    //if success, call MyHandler's handleMessage(Message msg), pass Bitmap in msg.obj
    private void setBackGroundImage(final String imageUrl) {
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

    //if success, call MyHandler's handleMessage(Message msg), pass Bitmap in msg.obj
    private void setAlubmCoverImage(final String imageUrl) {
        if (imageUrl != null && imageUrl.length() != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //关键代码：将传入URL对应图片进行高斯模糊处理后返回
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

}
