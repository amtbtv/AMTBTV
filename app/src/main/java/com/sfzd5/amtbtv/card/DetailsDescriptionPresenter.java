package com.sfzd5.amtbtv.card;

import android.content.Context;
import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sfzd5.amtbtv.R;
import com.sfzd5.amtbtv.model.Program;
import com.sfzd5.amtbtv.page.ResourceCache;

public class DetailsDescriptionPresenter extends Presenter {

    private ResourceCache mResourceCache = new ResourceCache();
    private Context mContext;

    public DetailsDescriptionPresenter(Context context) {
        mContext = context;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.detail_view_content, null);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        TextView primaryText = mResourceCache.getViewById(viewHolder.view, R.id.primary_text);
        TextView sndText1 = mResourceCache.getViewById(viewHolder.view, R.id.secondary_text_first);
        TextView sndText2 = mResourceCache.getViewById(viewHolder.view, R.id.secondary_text_second);
        TextView extraText = mResourceCache.getViewById(viewHolder.view, R.id.extra_text);

        Program card = (Program) item;
        primaryText.setText(card.name);
        sndText1.setText(card.recDate);
        sndText2.setText(card.recAddress);

        extraText.setText("共 "+String.valueOf(card.recCount)+" 集");

    }

    @Override public void onUnbindViewHolder(ViewHolder viewHolder) {
        // Nothing to do here.
    }
}
