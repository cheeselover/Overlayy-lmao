package solutions.overlayylmao.overlayylmao;

import android.accessibilityservice.AccessibilityService;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class OverlayService extends AccessibilityService {

    private static String TAG = OverlayService.class.getSimpleName();

    private WindowManager windowManager;
    private ImageView chatHead;
    private Timer mTimer;

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

    @Override public void onCreate() {
        mTimer = new Timer();
        super.onCreate();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
//                Log.d("service", "task is running");
//                File scr = new File(Environment.getExternalStorageDirectory(), "scr.png");
//                try {
//                    Process sh = Runtime.getRuntime().exec("su", null, null);
//                    OutputStream os = sh.getOutputStream();
//                    os.write(("/system/bin/screencap -p " + scr.getAbsolutePath()).getBytes("ASCII"));
//                    os.flush();
//                    os.close();
//                    sh.waitFor();
//                    Log.d("service", "image created");
//                } catch (IOException | InterruptedException e) {
//                    e.printStackTrace();
//                }
                imageHandler.sendEmptyMessage(0);

            }
        }, 0, 10000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null) windowManager.removeView(chatHead);
        mTimer.cancel();
    }

    private final Handler imageHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
//            File scr = new File(Environment.getExternalStorageDirectory(), "scr.png");
//            Log.d("service", "handling stuff: " + scr.getAbsolutePath());

//            try {
//                Process sh = Runtime.getRuntime().exec("su", null, null);
//                OutputStream os = sh.getOutputStream();
//                os.write(("/system/bin/screencap -p " + scr.getAbsolutePath()).getBytes("ASCII"));
//                os.flush();
//                os.close();
//                sh.waitFor();
//                Log.d("service", "image created");
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            }

//            Log.d("service", "checking file");
//            if (scr.exists()) {
//                Log.d("service", "file exists");
                windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

                boolean firstTime = false;
                if(chatHead == null) {
                    firstTime = true;
                    Log.d("service", "first time");
                    chatHead = new ImageView(OverlayService.this);
                }
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                Bitmap bitmap = BitmapFactory.decodeFile(scr.getAbsolutePath(), options);
//                chatHead.setImageBitmap(bitmap);


                chatHead.setImageResource(R.drawable.rainbow);

                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);

                params.gravity = Gravity.TOP | Gravity.LEFT;
                params.x = 0;
                params.y = 0;

                chatHead.setAlpha(0.5f);
                if (firstTime) windowManager.addView(chatHead, params);
//            }
        }
    };
}
