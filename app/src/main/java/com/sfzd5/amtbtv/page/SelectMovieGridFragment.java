package com.sfzd5.amtbtv.page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.VerticalGridFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridPresenter;

import com.sfzd5.amtbtv.TVApplication;
import com.sfzd5.amtbtv.card.TextPresenterSelector;
import com.sfzd5.amtbtv.model.Card;
import com.sfzd5.amtbtv.model.Channel;
import com.sfzd5.amtbtv.model.Program;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/1.
 */

public class SelectMovieGridFragment extends VerticalGridFragment {

    private static final int COLUMNS = 6;
    private static final int ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_MEDIUM;

    private ArrayObjectAdapter mAdapter;
    TVApplication app;
    Program program;
    List<Card> cardList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = TVApplication.getInstance();

        //查找pgogram
        for(Channel c : app.data.channels){
            if(c.name.equals(app.curChannel)){
                for(Program p : c.programs){
                    if(p.id==app.curCardId){
                        program = p;
                        break;
                    }
                }
                break;
            }
        }

        if(program!=null) {
            setTitle(program.name);
            setupRowAdapter();
        }
    }

    private void setupRowAdapter() {
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter(ZOOM_FACTOR);
        gridPresenter.setNumberOfColumns(COLUMNS);
        gridPresenter.setKeepChildForeground(true);
        gridPresenter.setShadowEnabled(true);
        setGridPresenter(gridPresenter);
        gridPresenter.setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                Card card = (Card) item;
                app.curFileIdx = cardList.indexOf(card);
                Intent intent = new Intent(getActivity().getBaseContext(), VideoPlayerActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        PresenterSelector textPresenterSelector = new TextPresenterSelector(getActivity());
        mAdapter = new ArrayObjectAdapter(textPresenterSelector);
        setAdapter(mAdapter);

        prepareEntranceTransition();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                createRows();
                startEntranceTransition();
            }
        }, 200);
    }

    private void createRows() {
        cardList = new ArrayList<>();
        for(String file : program.files){
            Card card = new Card();
            String[] ss = file.split("-");
            String s = ss[2];
            int d = s.indexOf(".");
            if(d>0){
                s = s.substring(0, d);
            }
            card.name = s;
            cardList.add(card);
        }
        mAdapter.addAll(0, cardList);
    }
}
