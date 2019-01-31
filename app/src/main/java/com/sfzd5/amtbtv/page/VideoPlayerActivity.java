package com.sfzd5.amtbtv.page;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.sfzd5.amtbtv.R;

public class VideoPlayerActivity extends FragmentActivity {
    public static final String TAG = "VideoPlayerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.videoFragment, new VideoPlayerFragment(),
                    VideoPlayerFragment.TAG).commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
