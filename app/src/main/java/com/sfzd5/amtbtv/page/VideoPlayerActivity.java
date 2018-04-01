package com.sfzd5.amtbtv.page;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.os.BuildCompat;

import com.sfzd5.amtbtv.R;

public class VideoPlayerActivity extends Activity {
    public static final String TAG = "VideoPlayerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

            if (savedInstanceState == null) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.videoFragment, new VideoPlayerFragment(),
                        VideoPlayerFragment.TAG);
                ft.commit();
            }
        }

        @Override
        protected void onNewIntent(Intent intent) {
            super.onNewIntent(intent);
            // This part is necessary to ensure that getIntent returns the latest intent when
            // VideoExampleActivity is started. By default, getIntent() returns the initial intent
            // that was set from another activity that started VideoExampleActivity. However, we need
            // to update this intent when for example, user clicks on another video when the currently
            // playing video is in PIP mode, and a new video needs to be started.
            setIntent(intent);
        }

    public static boolean supportsPictureInPicture(Context context) {
        return BuildCompat.isAtLeastN() &&
                context.getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_PICTURE_IN_PICTURE);
    }
}
