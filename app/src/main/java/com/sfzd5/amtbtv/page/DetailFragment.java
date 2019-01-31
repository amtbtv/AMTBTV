package com.sfzd5.amtbtv.page;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.app.DetailsFragmentBackgroundController;
import android.support.v17.leanback.app.DetailsSupportFragment;
import android.support.v17.leanback.app.DetailsSupportFragmentBackgroundController;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sfzd5.amtbtv.util.AmtbApi;
import com.sfzd5.amtbtv.util.AmtbApiCallBack;
import com.sfzd5.amtbtv.R;
import com.sfzd5.amtbtv.TVApplication;
import com.sfzd5.amtbtv.card.DetailsDescriptionPresenter;
import com.sfzd5.amtbtv.model.Channel;
import com.sfzd5.amtbtv.model.FileListResult;
import com.sfzd5.amtbtv.model.History;
import com.sfzd5.amtbtv.model.Program;
import com.sfzd5.amtbtv.util.CacheResult;

import java.util.List;

/**
 * Created by Administrator on 2018/3/1.
 */

public class DetailFragment extends DetailsSupportFragment implements OnItemViewClickedListener,
        OnItemViewSelectedListener {

    public static final String TRANSITION_NAME = "t_for_transition";
    public static final String EXTRA_CARD = "card";

    private static final long ACTION_PLAY = 1;
    private static final long ACTION_SELECT_MOVIE = 2;

    private Action mActionPlay;
    private Action mActionSelectMovie;

    private ArrayObjectAdapter mRowsAdapter;
    private final DetailsSupportFragmentBackgroundController mDetailsBackground =
            new DetailsSupportFragmentBackgroundController(this);

    DetailsOverviewRow detailsOverview;

    Handler handler;
    TVApplication app;
    private Program program;
    int curFileIdx = 0;
    int amtbid = -1;
    String identifier;
    String tp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();
        app = TVApplication.getInstance();

        Intent intent = getActivity().getIntent();
        tp = intent.getStringExtra("tp");

        //从tp来区分两种来源，一种是历史记录，一种是节目
        if(tp.equals("History")){
            identifier = intent.getStringExtra("identifier");
            History history = app.historyManager.findHistory(identifier);
            if(history==null){
                Toast.makeText(DetailFragment.this.getActivity(), R.string.history_remove, Toast.LENGTH_LONG).show();
                getActivity().finish();
            } else {
                program = history;
                for (Channel c : app.data.channels) {
                    if (c.name.equals(program.channel)) {
                        amtbid = c.amtbid;
                        break;
                    }
                }
                if (amtbid == -1) {
                    app.historyManager.removeHistory(history);
                    Toast.makeText(DetailFragment.this.getActivity(), R.string.program_change, Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
            }
        } else { // tp == Program
            amtbid = intent.getIntExtra("amtbid", 0);
            identifier = intent.getStringExtra("identifier");
            program = app.findProgram(amtbid, identifier);
        }

        if(app.filesHashMap.containsKey(amtbid)) {
            setupUi();
            setupEventListeners();
        } else {
            getProgressBarManager().show();
            AmtbApi<FileListResult> api = new AmtbApi<>(AmtbApi.takeFilesUrl(identifier), new AmtbApiCallBack<FileListResult>() {
                @Override
                public void callBack(FileListResult obj) {
                    getProgressBarManager().hide();
                    if(obj!=null){
                        app.filesHashMap.put(identifier, obj.files);
                        setupUi();
                        setupEventListeners();
                    } else {
                        Toast.makeText(DetailFragment.this.getActivity(), R.string.down_data_err, Toast.LENGTH_LONG).show();
                        getProgressBarManager().hide();
                    }
                }
            });
            api.execute(FileListResult.class);
        }
    }

    private void setupUi() {
        // Setup fragment
        setTitle(program.name);

        FullWidthDetailsOverviewRowPresenter rowPresenter = new FullWidthDetailsOverviewRowPresenter(
                new DetailsDescriptionPresenter(getActivity())) {

            @Override
            protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
                // Customize Actionbar and Content by using custom colors.
                RowPresenter.ViewHolder viewHolder = super.createRowViewHolder(parent);

                View actionsView = viewHolder.view.
                        findViewById(R.id.details_overview_actions_background);
                actionsView.setBackgroundColor(getActivity().getResources().
                        getColor(R.color.detail_view_actionbar_background));

                View detailsView = viewHolder.view.findViewById(R.id.details_frame);
                detailsView.setBackgroundColor(
                        getResources().getColor(R.color.detail_view_background));
                return viewHolder;
            }
        };

        FullWidthDetailsOverviewSharedElementHelper mHelper = new FullWidthDetailsOverviewSharedElementHelper();
        mHelper.setSharedElementEnterTransition(getActivity(), TRANSITION_NAME);
        rowPresenter.setListener(mHelper);
        rowPresenter.setParticipatingEntranceTransition(false);
        prepareEntranceTransition();

        // Setup PresenterSelector to distinguish between the different rows.
        ClassPresenterSelector rowPresenterSelector = new ClassPresenterSelector();
        rowPresenterSelector.addClassPresenter(DetailsOverviewRow.class, rowPresenter);
        rowPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
        mRowsAdapter = new ArrayObjectAdapter(rowPresenterSelector);

        // Setup action and detail row.
        detailsOverview = new DetailsOverviewRow(program);
        Drawable drawable = getResources().getDrawable(R.drawable.cardbg2);
        detailsOverview.setImageDrawable(drawable);

        ArrayObjectAdapter actionAdapter = new ArrayObjectAdapter();

        List<String> files = app.filesHashMap.get(identifier);
        String fileName = files.get(0);
        //查看是否存在播放记录
        History history = app.historyManager.findProgramHistory(program);
        if(history!=null) {
            fileName = files.get(history.fileIdx);
            curFileIdx = history.fileIdx;
        }

        mActionPlay = new Action(ACTION_PLAY, getString(R.string.bofang)+fileName);
        actionAdapter.add(mActionPlay);

        if(files.size()>1) {
            mActionSelectMovie = new Action(ACTION_SELECT_MOVIE, getString(R.string.xuanji));
            actionAdapter.add(mActionSelectMovie);
        }

        detailsOverview.setActionsAdapter(actionAdapter);
        mRowsAdapter.add(detailsOverview);

        setAdapter(mRowsAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startEntranceTransition();
            }
        }, 500);

        detailsOverview.setImageDrawable(getResources().getDrawable(R.drawable.cardbg2));
        mDetailsBackground.enableParallax();
        mDetailsBackground.setCoverBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.cardbg));
        //有图片，载入图片
        if(program.picCreated==1){
            app.http.asyncTakeFile(program.getCardPic(), new CacheResult() {
                @Override
                public void tackFile(final Bitmap bmp) {
                    if(bmp!=null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                detailsOverview.setImageDrawable(new BitmapDrawable(null, bmp));
                            }
                        });
                    }
                }
            });
        }
        //initializeBackground(data);
    }

    private void setupEventListeners() {
        setOnItemViewSelectedListener(this);
        setOnItemViewClickedListener(this);
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                              RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (!(item instanceof Action)) return;
        Action action = (Action) item;
        Intent intent;
        if (action.getId() == ACTION_PLAY) {
            intent = new Intent(getActivity().getBaseContext(), VideoPlayerActivity.class);
        } else { // if (action.getId() == ACTION_SELECT_MOVIE)
            intent = new Intent(getActivity().getBaseContext(), SelectMovieActivity.class);
        }
        intent.putExtra("channel", program.channel);
        intent.putExtra("amtbid", amtbid);
        intent.putExtra("identifier", program.identifier);
        intent.putExtra("tp", tp);
        intent.putExtra("curFileIdx", curFileIdx);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                               RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (mRowsAdapter.indexOf(row) > 0) {
            int backgroundColor = getResources().getColor(R.color.detail_view_related_background);
            getView().setBackgroundColor(backgroundColor);
        } else {
            getView().setBackground(null);
        }
    }
}
