package com.sfzd5.amtbtv.page;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;
import android.widget.VideoView;

import com.sfzd5.amtbtv.R;
import com.sfzd5.amtbtv.TVApplication;
import com.sfzd5.amtbtv.model.Live;

import java.util.List;

public class PlayActivity extends Activity {

    public static final String TAG = "PlayActivity";

    List<Live> lives = null;
    int idx = 0;

    View bg;
    VideoView videoView;
    Handler handler;

    AlphaAnimation mHideAnimation;

    private TVApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        String name = getIntent().getStringExtra("name");

        app = (TVApplication) getApplication();
        lives = app.data.lives;
        this.videoView = (VideoView) findViewById(R.id.videoView);
        this.bg = findViewById(R.id.videobg);
        this.bg.setVisibility(View.VISIBLE);
        //videoView.setBackgroundResource(R.drawable.bg);
        handler = new Handler();

        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(1000);
        mHideAnimation.setFillAfter(false);
        mHideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                PlayActivity.this.bg.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        this.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                //视频播放出错
                showMsg(getString(R.string.video_play_err));
                return false;
            }
        });

        this.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //PlayActivity.this.videoView.setBackgroundResource(0);
                PlayActivity.this.bg.clearAnimation();
                PlayActivity.this.bg.startAnimation(mHideAnimation);
            }
        });

        this.videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                //信息
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    showMsg(getString(R.string.buffer));
                }
                return false;
            }
        });

        for (int i = 0; i < lives.size(); i++) {
            Live live = lives.get(i);
            if (live.name.equals(name)) {
                idx = i;
                break;
            }
        }
        play(0);
    }


    public  void setHideAnimation( View view, int duration)
    {
        if (null == view || duration < 0)
        {
            return;
        }

        if (null != mHideAnimation)
        {
            mHideAnimation.cancel();
        }
        // 监听动画结束的操作
        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);
        view.startAnimation(mHideAnimation);
    }

    Live takeLive(int p){
        if(p==-1){//前一个
            if(idx==0)
                idx=lives.size()-1;
            else
                idx--;
        } else if (p==1){
            if(idx==lives.size()-1)
                idx=0;
            else
                idx++;
        }
        Live live = lives.get(idx);
        return live;
    }

    void play(int p) {
        Live live = takeLive(p);
        String url = live.mediaUrl;
        Uri uri = Uri.parse(url);
        this.videoView.setVideoURI(uri);
        this.videoView.start();
        showMsg(live.name);// +" "+ server.getTitle()
    }

    void showMsg(final String msg){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PlayActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_UP) {
            this.videoView.stopPlayback();
            this.bg.setVisibility(View.VISIBLE);
            play(-1);
            return true;
        } else if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_DOWN) {
            this.videoView.stopPlayback();
            this.bg.setVisibility(View.VISIBLE);
            play(1);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
