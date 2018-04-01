package com.sfzd5.amtbtv.page;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.SearchSupportFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridPresenter;
import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;
import com.sfzd5.amtbtv.TVApplication;
import com.sfzd5.amtbtv.model.Card;
import com.sfzd5.amtbtv.model.Channel;
import com.sfzd5.amtbtv.model.Program;

import java.util.ArrayList;
import java.util.List;
public class SearchFragment extends android.support.v17.leanback.app.SearchSupportFragment
        implements android.support.v17.leanback.app.SearchSupportFragment.SearchResultProvider {

    List<String> names;
    List<Program> programs;
    List<Card> queryPrograms;
    TVApplication app;
    ArrayObjectAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int COLUMNS = 6;
        int ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_SMALL;
        VerticalGridPresenter presenter = new VerticalGridPresenter(ZOOM_FACTOR);
        presenter.setNumberOfColumns(COLUMNS);
        mAdapter = new ArrayObjectAdapter(presenter);

        setSearchResultProvider(this);

        setTitle("搜索");
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                Card card = (Card) item;
                TVApplication app = TVApplication.getInstance();
                app.curChannel = card.channel;
                app.curCardId = card.id;

                if(item instanceof Program){
                    Intent intent = new Intent(getActivity().getBaseContext(), DetailActivity.class);
                    startActivity(intent);
                }
            }
        });

        programs = new ArrayList<>();
        queryPrograms = new ArrayList<>();
        names = new ArrayList<>();
        app = TVApplication.getInstance();
        for(Channel c : app.data.channels){
            for(Program p : c.programs){
                programs.add(p);
                char[] chs = p.name.toCharArray();
                StringBuilder sb = new StringBuilder(chs.length);
                for(char ch : chs){
                    String py = Pinyin.toPinyin(ch);
                    sb.append(py.charAt(0));
                }
                names.add(sb.toString());
            }
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        //mRowsAdapter.add(new ListRow(header, listRowAdapter));
        return mAdapter;
    }

    void filter(String query){
        String uQuery  = query.toUpperCase();
        if (!TextUtils.isEmpty(query) && !query.equals("nil")) {
            queryPrograms.clear();
            for(int i = 0; i < names.size(); i++){
                String s = names.get(i);
                if(s.startsWith(uQuery)) {
                    queryPrograms.add(programs.get(i));
                }
            }
        }

        mAdapter.clear();
        mAdapter.addAll(0, queryPrograms);
        mAdapter.notifyArrayItemRangeChanged(0, queryPrograms.size());
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        filter(newQuery);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        filter(query);
        return true;
    }
}