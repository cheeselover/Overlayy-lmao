package solutions.overlayylmao.overlayylmao;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private Preset mPreset;

    @Bind(R.id.start_button)
    Button mStartButton;

    @Bind(R.id.stop_button)
    Button mStopButton;

    @Bind(R.id.open_effects_button)
    Button mOpenEffectsButton;

    Notification mOverlayNotification;
    NotificationManager mNotificationManager;
    final int OVERLAY_NOTIFICATION_ID = 1337;
    MediaProjectionManager mMediaProjectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Notification.Builder mBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.rainbow_tile)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setAutoCancel(true)
                .setOngoing(true);

        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        mOverlayNotification = mBuilder.build();
        mPreset = new Preset();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, OverlayService.class));
        mNotificationManager.cancel(OVERLAY_NOTIFICATION_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            Intent intent = new Intent(this, OverlayService.class);
            intent.putExtra(OverlayService.EXTRA_RESULT_CODE, resultCode);
            intent.putExtra(OverlayService.EXTRA_DATA, data);
            intent.putExtra(OverlayService.EXTRA_PRESET, mPreset);
            startService(intent);
            mNotificationManager.notify(OVERLAY_NOTIFICATION_ID, mOverlayNotification);
            return;
        } else if(requestCode == 52) {
            if(data != null) {
                mPreset = data.getParcelableExtra(OverlayService.EXTRA_PRESET);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.start_button)
    void startService() {
        Log.d("activity", "starting service");
        startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), 10);
    }

    @OnClick(R.id.stop_button)
    void stopService() {
        Log.d("activity", "stopping service");
        stopService(new Intent(this, OverlayService.class));
        mNotificationManager.cancel(OVERLAY_NOTIFICATION_ID);
    }

    @OnClick(R.id.open_effects_button)
    void openPreferences() {
        Intent myIntent = new Intent(MainActivity.this, EffectsActivity.class);
        MainActivity.this.startActivityForResult(myIntent, 52);
    }
}

