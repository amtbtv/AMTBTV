package com.sfzd5.amtbtv.page;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.VideoFragment;
import android.support.v17.leanback.app.VideoFragmentGlueHost;
import android.support.v17.leanback.media.MediaPlayerAdapter;
import android.support.v17.leanback.media.PlaybackGlue;
import android.support.v17.leanback.media.PlaybackTransportControlGlue;
import android.util.Log;

import com.sfzd5.amtbtv.ServerConst;
import com.sfzd5.amtbtv.TVApplication;
import com.sfzd5.amtbtv.model.Channel;
import com.sfzd5.amtbtv.model.History;
import com.sfzd5.amtbtv.model.Program;

import java.util.List;

/**
 * Created by Administrator on 2018/3/10.
 */

public class VideoPlayerFragment  extends VideoFragment {
    int idx = 0;
    TVApplication app;
    History history;
    Handler handler;
    int amtbid;
    List<String> files;
    String tp;

    private static String URL = "";
    public static final String TAG = "VideoConsumption";
    private VideoMediaPlayerGlue<MediaPlayerAdapter> mMediaPlayerGlue;
    final VideoFragmentGlueHost mHost = new VideoFragmentGlueHost(this);

    static void playWhenReady(VideoMediaPlayerGlue<MediaPlayerAdapter> glue, final int pos) {
        if (glue.isPrepared()) {
            glue.play();
            if(pos>0)
                glue.seekTo(pos);
        } else {
            glue.addPlayerCallback(new PlaybackGlue.PlayerCallback() {
                @Override
                public void onPreparedStateChanged(PlaybackGlue glue1) {
                    VideoMediaPlayerGlue<MediaPlayerAdapter> glue = (VideoMediaPlayerGlue<MediaPlayerAdapter>) glue1;
                    if (glue.isPrepared()) {
                        glue.removePlayerCallback(this);
                        glue.play();
                        if(pos>0)
                            glue.seekTo(pos);
                    }
                }
            });
        }
    }

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener
            = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int state) {
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        history.currentPosition = (int)mMediaPlayerGlue.getCurrentPosition();
        app.historyManager.save();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();
        app = TVApplication.getInstance();

        Intent intent = getActivity().getIntent();
        String channel = intent.getStringExtra("channel");
        String identifier = intent.getStringExtra("identifier");
        idx = intent.getIntExtra("curFileIdx", 0);
        amtbid = intent.getIntExtra("amtbid", 0);
        tp = intent.getStringExtra("tp");
        files = app.filesHashMap.get(identifier);

        if (tp.equals("History")) {
            history = app.historyManager.findHistory(identifier);
            app.historyManager.historyList.remove(history);
            app.historyManager.historyList.add(0, history);
            app.historyManager.save();
        } else {
            Program program = app.findProgram(amtbid, identifier);
            //记录历史记录
            history = app.historyManager.findProgramHistory(program);
            if (history == null) {
                history = new History();
                history.channel = program.channel;
                history.identifier = program.identifier;
                history.fileIdx = idx;
                history.currentPosition = 0;
                history.name = program.name;
                history.picCreated = program.picCreated;
                history.recAddress = program.recAddress;
                history.recDate = program.recDate;
                app.historyManager.historyList.add(0, history);
                app.historyManager.save();
            } else {
                app.historyManager.historyList.remove(history);
                app.historyManager.historyList.add(0, history);
                if(history.fileIdx != idx)
                    history.currentPosition = 0;
                app.historyManager.save();
            }

        }

        mMediaPlayerGlue = new VideoMediaPlayerGlue(getActivity(), new MediaPlayerAdapter(getActivity()));
        mMediaPlayerGlue.setHost(mHost);
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN) != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.w(TAG, "video player cannot obtain audio focus!");
        }

        setSeekProvider(mMediaPlayerGlue);

        mMediaPlayerGlue.addPlayerCallback(new PlaybackGlue.PlayerCallback() {
            @Override
            public void onPlayCompleted(PlaybackGlue glue) {
                super.onPlayCompleted(glue);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String file = takeFile(1);
                        history.currentPosition = 0;
                        history.fileIdx = idx;
                        app.historyManager.save();
                        play(file);
                    }
                });
            }
        });

        String file = takeFile(0);
        play(file);
    }

    String takeFile(int p){
        if(p==-1){//前一个
            if(idx==0)
                idx=files.size()-1;
            else
                idx--;
        } else if (p==1){
            if(idx==files.size()-1)
                idx=0;
            else
                idx++;
        }
        String file = files.get(idx);
        return file;
    }

    void play(String file) {
        mMediaPlayerGlue.setTitle(history.name);
        mMediaPlayerGlue.setSubtitle(file);
        URL = ServerConst.getProgramVideoUrl(history.identifier, file);

        mMediaPlayerGlue.getPlayerAdapter().setDataSource(Uri.parse(URL));

        playWhenReady(mMediaPlayerGlue, history.currentPosition);
        setBackgroundType(BG_LIGHT);

    }

    @Override
    public void onPause() {
        if (mMediaPlayerGlue != null) {
            mMediaPlayerGlue.pause();
        }
        super.onPause();
    }

    public static void setSeekProvider(final PlaybackTransportControlGlue glue) {
        if (glue.isPrepared()) {
            glue.setSeekProvider(new PlaybackSeekDataProvider(
                    glue.getDuration(),
                    glue.getDuration() / 100));
        } else {
            glue.addPlayerCallback(new PlaybackGlue.PlayerCallback() {
                @Override
                public void onPreparedStateChanged(PlaybackGlue glue) {
                    if (glue.isPrepared()) {
                        glue.removePlayerCallback(this);
                        PlaybackTransportControlGlue transportControlGlue =
                                (PlaybackTransportControlGlue) glue;
                        transportControlGlue.setSeekProvider(new PlaybackSeekDataProvider(
                                transportControlGlue.getDuration(),
                                transportControlGlue.getDuration() / 100));
                    }
                }
            });
        }
    }
}