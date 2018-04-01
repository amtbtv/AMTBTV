package com.sfzd5.amtbtv.page;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v17.leanback.media.PlaybackBaseControlGlue;
import android.support.v17.leanback.media.PlaybackTransportControlGlue;
import android.support.v17.leanback.media.PlayerAdapter;
import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;
import android.support.v17.leanback.widget.PlaybackRowPresenter;
import android.support.v17.leanback.widget.PlaybackTransportRowPresenter;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/3/10.
 */

public class VideoMediaPlayerGlue<T extends PlayerAdapter> extends PlaybackTransportControlGlue<T> {

    public VideoMediaPlayerGlue(Activity context, T impl) {
        super(context, impl);
    }

    @Override
    protected PlaybackRowPresenter onCreateRowPresenter() {
        final AbstractDetailsDescriptionPresenter detailsPresenter =
                new AbstractDetailsDescriptionPresenter() {
                    @Override
                    protected void onBindDescription(ViewHolder
                                                             viewHolder, Object obj) {
                        PlaybackBaseControlGlue glue = (PlaybackBaseControlGlue) obj;
                        TextView title = viewHolder.getTitle();
                        title.setTypeface(Typeface.DEFAULT);
                        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 43);
                        title.setText(glue.getTitle());
                        viewHolder.getSubtitle().setText(glue.getSubtitle());
                    }
                };

        PlaybackTransportRowPresenter rowPresenter = new PlaybackTransportRowPresenter() {
            @Override
            protected void onBindRowViewHolder(RowPresenter.ViewHolder vh, Object item) {
                super.onBindRowViewHolder(vh, item);
                vh.setOnKeyListener(VideoMediaPlayerGlue.this);
            }
            @Override
            protected void onUnbindRowViewHolder(RowPresenter.ViewHolder vh) {
                super.onUnbindRowViewHolder(vh);
                vh.setOnKeyListener(null);
            }
        };
        rowPresenter.setDescriptionPresenter(detailsPresenter);
        return rowPresenter;
    }
}