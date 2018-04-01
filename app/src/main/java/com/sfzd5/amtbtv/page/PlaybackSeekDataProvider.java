package com.sfzd5.amtbtv.page;

import android.os.AsyncTask;
import android.support.v17.leanback.media.PlaybackGlue;
import android.support.v17.leanback.media.PlaybackTransportControlGlue;

/**
 * Created by Administrator on 2018/3/10.
 */

public class PlaybackSeekDataProvider  extends android.support.v17.leanback.widget.PlaybackSeekDataProvider {

    static final String TAG = "SeekProvider";

    long[] pos;
    int mLastRequestedIndex = -1;

    public PlaybackSeekDataProvider(long duration, long interval) {
        int size = (int) (duration / interval) + 1;
        pos = new long[size];
        for (int i = 0; i < pos.length; i++) {
            pos[i] = i * duration / pos.length;
        }
    }

    public void setSeekPositions(long[] positions) {
        pos = positions;
    }

    @Override
    public long[] getSeekPositions() {
        return pos;
    }

    @Override
    public void reset() {
        mLastRequestedIndex = -1;
    }

}