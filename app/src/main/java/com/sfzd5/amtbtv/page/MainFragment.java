package com.sfzd5.amtbtv.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BrowseSupportFragment;
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
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.sfzd5.amtbtv.R;
import com.sfzd5.amtbtv.TVApplication;
import com.sfzd5.amtbtv.card.CardPresenterSelector;
import com.sfzd5.amtbtv.model.Card;
import com.sfzd5.amtbtv.model.Channel;
import com.sfzd5.amtbtv.model.History;
import com.sfzd5.amtbtv.model.Live;
import com.sfzd5.amtbtv.model.LiveChannelListResult;
import com.sfzd5.amtbtv.model.Program;
import com.sfzd5.amtbtv.model.ProgramListResult;
import com.sfzd5.amtbtv.util.AmtbApi;
import com.sfzd5.amtbtv.util.AmtbApiCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动界面
 */

public class MainFragment extends BrowseSupportFragment {

    // 左侧大类列表适配器
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
        //准备入口过渡动画
        prepareEntranceTransition();

        //注册右侧框架的内容生成工厂，内容用CardGridFragment显示，均在下面
        getMainFragmentRegistry().registerFragment(PageRow.class,
                new PageRowFragmentFactory());

        //载入数据
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
                    getProgressBarManager().hide();
                }
            }
        });
        api.execute(LiveChannelListResult.class);
    }

    //创建左侧大类菜单
    private void createRows() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        //检测是否存在播放记录，若存在则加入播放记录
        if(app.historyManager.historyList.size()>0){
            HeaderItem headerItem = new HeaderItem(-1, getString(R.string.history));
            PageRow historyPageRow = new PageRow(headerItem);
            mRowsAdapter.add(historyPageRow);
        }

        //直播节目
        HeaderItem headerItem1 = new HeaderItem(0, getString(R.string.live));
        PageRow livePageRow = new PageRow(headerItem1);
        mRowsAdapter.add(livePageRow);

        //点播节目
        for(Channel c : app.data.channels) {
            HeaderItem headerItem = new HeaderItem(c.amtbid, c.name);
            PageRow pageRow = new PageRow(headerItem);
            mRowsAdapter.add(pageRow);
        }
        setAdapter(mRowsAdapter);
    }

    //右侧内容生成工厂类
    private static class PageRowFragmentFactory extends BrowseSupportFragment.FragmentFactory {
        @Override
        public Fragment createFragment(Object rowObj) {
            Row row = (Row)rowObj; //在上面传入的 PageRow
            HeaderItem headerItem = row.getHeaderItem();
            Fragment fragment = new CardGridFragment();
            //传入数据： channel  id ，在下面的类中接改此参数
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
        //右侧内容的适配器
        private ArrayObjectAdapter mAdapter;
        private String channel;
        private int heaerItemId;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle bundle = this.getArguments();
            //传入数据： channel  id
            this.channel = bundle.getString("channel");
            this.heaerItemId = bundle.getInt("id");
            //设置适配器 显示列数、显示模式、点击事件
            setupAdapter();
            //显示数据
            loadData();
        }

        /**
         * 设置适配器 显示列数、显示模式、点击事件
         */
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
                        TVApplication app = TVApplication.getInstance();
                        int idx = 0;
                        for (Live lv : app.data.lives) {
                            if (lv==live) {
                                break;
                            }
                            idx++;
                        }
                        //Intent intent = new Intent(getActivity().getBaseContext(), PlayActivity.class);
                        //intent.putExtra("name", live.name);
                        Intent intent = new Intent(getActivity().getBaseContext(), VideoPlayerActivity.class);
                        intent.putExtra("tp", "Live");
                        intent.putExtra("curFileIdx", idx);
                        startActivity(intent);
                        //Toast.makeText(CardGridFragment.this.getActivity(),card.name, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CardGridFragment.this.getActivity(),getString(R.string.unknow)+card.name, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        //更新界面
        private void notifyDataReady(){
            MainFragmentAdapter mainFragmentAdapter = this.getMainFragmentAdapter();
            if(mainFragmentAdapter!=null){
                FragmentHost fragmentHost = mainFragmentAdapter.getFragmentHost();
                if(fragmentHost!=null){
                    fragmentHost.notifyDataReady(mainFragmentAdapter);
                }
            }
        }

        private void loadData() {
            TVApplication app = TVApplication.getInstance();
            if (heaerItemId == -1) {//历史
                List<Card> cards = new ArrayList<>();
                for (History history : app.historyManager.historyList) {
                    cards.add(history);
                }
                mAdapter.addAll(0, cards);
                notifyDataReady();
            } else if (heaerItemId == 0) { //直播
                mAdapter.addAll(0, app.data.lives);
                notifyDataReady();
            } else { //点播
                //final int amtbid = heaerItemId;
                if (app.programListResultHashMap.containsKey(heaerItemId)) {
                    mAdapter.addAll(0, app.programListResultHashMap.get(heaerItemId).programs);
                    notifyDataReady();
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
                                notifyDataReady();
                            } else {
                                Activity activity = getActivity();
                                if(activity!=null)
                                    Toast.makeText(activity, R.string.down_data_err, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    api.execute(ProgramListResult.class);
                }
            }
        }
    }
}
