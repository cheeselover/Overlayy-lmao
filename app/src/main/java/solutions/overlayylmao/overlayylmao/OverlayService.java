package solutions.overlayylmao.overlayylmao;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

public class OverlayService extends AccessibilityService {

    public static final String EXTRA_RESULT_CODE = "result_code";
    public static final String EXTRA_DATA = "data";

    private static String TAG = OverlayService.class.getSimpleName();

    private WindowManager windowManager;
//    private SurfaceView chatHead;
    private ImageView chatHead;
    private Timer mTimer;
    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private ImageReader mImageReader;
    private DisplayMetrics mMetrics = new DisplayMetrics();


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d("got event", event.toString());
        AccessibilityNodeInfo source = event.getSource();
        if (source == null) {
            Log.d(TAG, "no source");
            return;
        }
        Log.d(TAG, "res: " + source.getViewIdResourceName());
//        try {
//            Process sh = Runtime.getRuntime().exec("su", null, null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        int resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, 0);
        Intent data = intent.getParcelableExtra(EXTRA_DATA);
        mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(mMetrics);

        setupImageWatching();

        mTimer = new Timer();
        super.onCreate();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                imageHandler.sendEmptyMessage(0);
            }
        }, 0, 1000);
//        imageHandler.sendEmptyMessage(0);
//        imageHandler.sendEmptyMessage(0);
//        mTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                imageHandler.sendEmptyMessage(0);
//            }
//        }, 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null) windowManager.removeView(chatHead);
        if (mMediaProjection != null) mMediaProjection.stop();
        if (mVirtualDisplay != null) mVirtualDisplay.release();
        mTimer.cancel();
    }

    private void setupImageWatching() {
        if (mImageReader != null) mImageReader.close();
        if (mVirtualDisplay != null) mVirtualDisplay.release();
        mImageReader = ImageReader.newInstance(mMetrics.widthPixels, mMetrics.heightPixels, PixelFormat.RGBA_8888, 2);

        mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
                mMetrics.widthPixels, mMetrics.heightPixels, mMetrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
//                    chatHead.getHolder().getSurface(), null, null);
    }

    private final Handler imageHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            Log.d("OVERLAYY", "imagehandler");
            if(chatHead == null) {
                Log.d("service", "first time");
                chatHead = new ImageView(OverlayService.this);
                chatHead.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                chatHead.setBackgroundColor(Color.CYAN);
//                chatHead = new SurfaceView(OverlayService.this);
            }
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                Bitmap bitmap = BitmapFactory.decodeFile(scr.getAbsolutePath(), options);
//                chatHead.setImageBitmap(bitmap);

            if (chatHead.isAttachedToWindow()) windowManager.removeView(chatHead);

            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    imageHandler2.sendEmptyMessage(0);
                }
            }, 50);

        }
    };

    private final Handler imageHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("OVERLAYY", "imagehandlerTWO");
            Image image = mImageReader.acquireLatestImage();
            if (image != null) {
                Log.d("OVROSRYUTNSTR", "w" + image.getWidth() + ", h" + image.getHeight());
//                Image.Plane[] planes = image.getPlanes();
//                ByteBuffer buffer = planes[0].getBuffer();
//                int pixelStride = planes[0].getPixelStride();
//                int rowStride = planes[0].getRowStride();
//                int rowPadding = rowStride - pixelStride * mMetrics.widthPixels;
//
//                Bitmap bitmap = Bitmap.createBitmap(mMetrics.widthPixels + rowPadding / pixelStride, mMetrics.heightPixels, Bitmap.Config.ARGB_8888);
//                bitmap.copyPixelsFromBuffer(buffer);
                int width = mMetrics.widthPixels;
//                int height = mMetrics.heightPixels;
                int height = image.getHeight();
                final Image.Plane[] planes = image.getPlanes();
                final ByteBuffer buffer = planes[0].getBuffer();
                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * width;
// create bitmap
                Bitmap bitmap = Bitmap.createBitmap(width+rowPadding/pixelStride, height, Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(buffer);
                chatHead.setImageBitmap(bitmap);
                image.close();
                setupImageWatching();

                try {
                    FileOutputStream out = new FileOutputStream(getApplicationContext().getFilesDir().getAbsolutePath() + "/derp.jpg");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

//                chatHead.setImageResource(R.drawable.rainbow);

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

            params.x = 0;
            params.y = 0;
            params.width = mMetrics.widthPixels;
            params.height = mMetrics.heightPixels;
//            params.width = metrics.widthPixels - metrics.widthPixels/50;
//            params.width = 250;
//            params.height = 250;

//            chatHead.setAlpha(0.5f);
//            chatHead.setVisibility(View.VISIBLE);
//            if (firstTime) windowManager.addView(chatHead, params);
            if(!chatHead.isAttachedToWindow()) windowManager.addView(chatHead, params);
//            }
        }
    };
}
