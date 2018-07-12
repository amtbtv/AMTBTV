package com.sfzd5.amtbtv.page;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.PageRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridPresenter;
import android.widget.Toast;

import com.sfzd5.amtbtv.util.AmtbApi;
import com.sfzd5.amtbtv.util.AmtbApiCallBack;
import com.sfzd5.amtbtv.R;
import com.sfzd5.amtbtv.TVApplication;
import com.sfzd5.amtbtv.card.CardPresenterSelector;
import com.sfzd5.amtbtv.model.Card;
import com.sfzd5.amtbtv.model.Channel;
import com.sfzd5.amtbtv.model.History;
import com.sfzd5.amtbtv.model.LiveChannelListResult;
import com.sfzd5.amtbtv.model.Live;
import com.sfzd5.amtbtv.model.Program;
import com.sfzd5.amtbtv.model.ProgramListResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

public class MainFragment extends BrowseFragment {

    private ArrayObjectAdapter mRowsAdapter;
    private TVApplication app;
    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        app = TVApplication.getInstance();

        //设置标题
        setTitle(getString(R.string.amtb_tv_title));
        prepareEntranceTransition();

        getMainFragmentRegistry().registerFragment(PageRow.class,
                new PageRowFragmentFactory());

        loadData();
    }

    private void loadData() {
        getProgressBarManager().show();
        AmtbApi<LiveChannelListResult> api = new AmtbApi<>(AmtbApi.takeLiveChannelsUrl(), new AmtbApiCallBack<LiveChannelListResult>() {
            @Override
            public void callBack(LiveChannelListResult obj) {
                if(obj!=null) {
                    getProgressBarManager().hide();
                    app.data = obj;
                    createRows();
                    startEntranceTransition();
                } else {
                    Toast.makeText(MainFragment.this.getActivity(), R.string.down_data_err, Toast.LENGTH_LONG).show();
                }
            }
        });
        api.execute(LiveChannelListResult.class);
    }

    private void createRows() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        //检测是否存在播放记录，若存在则加入播放记录
        if(app.historyManager.historyList.size()>0){
            HeaderItem headerItem = new HeaderItem(-1, getString(R.string.history));
            PageRow pageRow = new PageRow(headerItem);
            mRowsAdapter.add(pageRow);
        }

        //直播节目
        HeaderItem headerItem1 = new HeaderItem(0, getString(R.string.live));
        PageRow pageRow1 = new PageRow(headerItem1);
        mRowsAdapter.add(pageRow1);

        //点播节目
        for(Channel c : app.data.channels) {
            HeaderItem headerItem = new HeaderItem(c.amtbid, c.name);
            PageRow pageRow = new PageRow(headerItem);
            mRowsAdapter.add(pageRow);
        }
        setAdapter(mRowsAdapter);
    }

    private static class PageRowFragmentFactory extends BrowseFragment.FragmentFactory {
        @Override
        public Fragment createFragment(Object rowObj) {
            Row row = (Row)rowObj;
            HeaderItem headerItem = row.getHeaderItem();
            Fragment fragment = new CardGridFragment();
            Bundle bundle = new Bundle();
            bundle.putString("channel", headerItem.getName());
            bundle.putInt("id", (int)headerItem.getId());
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    public static class CardGridFragment extends GridFragment {
        private static final int COLUMNS = 6;
        private final int ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_SMALL;
        private ArrayObjectAdapter mAdapter;
        private String channel;
        private int heaerItemId;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle bundle = this.getArguments();
            this.channel = bundle.getString("channel");
            this.heaerItemId = bundle.getInt("id");
            setupAdapter();
            loadData();
        }

        private void setupAdapter() {
            VerticalGridPresenter presenter = new VerticalGridPresenter(ZOOM_FACTOR);
            presenter.setNumberOfColumns(COLUMNS);
            setGridPresenter(presenter);

            CardPresenterSelector cardPresenter = new CardPresenterSelector(getActivity());
            mAdapter = new ArrayObjectAdapter(cardPresenter);
            setAdapter(mAdapter);

            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                @Override
                public void onItemClicked(
                        Presenter.ViewHolder itemViewHolder,
                        Object item,
                        RowPresenter.ViewHolder rowViewHolder,
                        Row row) {
                    Card card = (Card) item;
                    if(item instanceof History){
                        History history = (History) card;
                        Intent intent = new Intent(getActivity().getBaseContext(), DetailActivity.class);
                        intent.putExtra("tp", "History");
                        intent.putExtra("identifier", history.identifier);
                        startActivity(intent);
                    } else if(item instanceof Program){
                        Program program = (Program) card;
                        Intent intent = new Intent(getActivity().getBaseContext(), DetailActivity.class);
                        intent.putExtra("tp", "Program");
                        intent.putExtra("amtbid", heaerItemId);
                        intent.putExtra("identifier", program.identifier);
                        startActivity(intent);
                    } else if(item instanceof Live){
                        Live live = (Live) card;
                        Intent intent = new Intent(getActivity().getBaseContext(), PlayActivity.class);
                        intent.putExtra("name", live.name);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(),getString(R.string.unknow)+card.name, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void loadData() {
            TVApplication app = TVApplication.getInstance();
            if (heaerItemId == -1) {//历史
                List<Card> cards = new ArrayList<>();
                for (History history : app.historyManager.historyList) {
                    cards.add(history);
                }
                mAdapter.addAll(0, cards);
                MainFragmentAdapter mainFragmentAdapter = this.getMainFragmentAdapter();
                mainFragmentAdapter.getFragmentHost().notifyDataReady(mainFragmentAdapter);
            } else if (heaerItemId == 0) { //直播
                mAdapter.addAll(0, app.data.lives);
                MainFragmentAdapter mainFragmentAdapter = this.getMainFragmentAdapter();
                mainFragmentAdapter.getFragmentHost().notifyDataReady(mainFragmentAdapter);
            } else { //点播
                //final int amtbid = heaerItemId;
                if (app.programListResultHashMap.containsKey(heaerItemId)) {
                    mAdapter.addAll(0, app.programListResultHashMap.get(heaerItemId).programs);
                    MainFragmentAdapter mainFragmentAdapter = this.getMainFragmentAdapter();
                    mainFragmentAdapter.getFragmentHost().notifyDataReady(mainFragmentAdapter);
                } else {
                    AmtbApi<ProgramListResult> api = new AmtbApi<>(AmtbApi.takeProgramsUrl(heaerItemId), new AmtbApiCallBack<ProgramListResult>() {
                        @Override
                        public void callBack(ProgramListResult obj) {
                            if(obj!=null) {
                                TVApplication app = TVApplication.getInstance();
                                app.programListResultHashMap.put(heaerItemId, obj);
                                for (Program p : obj.programs)
                                    p.channel = channel;
                                mAdapter.addAll(0, obj.programs);
                                MainFragmentAdapter mainFragmentAdapter = CardGridFragment.this.getMainFragmentAdapter();
                                mainFragmentAdapter.getFragmentHost().notifyDataReady(mainFragmentAdapter);
                            } else {
                                Toast.makeText(getActivity(), R.string.down_data_err, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    api.execute(ProgramListResult.class);
                }
            }
        }
    }
}
