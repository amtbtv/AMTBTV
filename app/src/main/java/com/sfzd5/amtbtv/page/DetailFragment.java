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

import com.sfzd5.amtbtv.R;
import com.sfzd5.amtbtv.TVApplication;
import com.sfzd5.amtbtv.card.DetailsDescriptionPresenter;
import com.sfzd5.amtbtv.model.Channel;
import com.sfzd5.amtbtv.model.History;
import com.sfzd5.amtbtv.model.Program;
import com.sfzd5.amtbtv.util.CacheResult;

/**
 * Created by Administrator on 2018/3/1.
 */

public class DetailFragment extends DetailsFragment implements OnItemViewClickedListener,
        OnItemViewSelectedListener {

    public static final String TRANSITION_NAME = "t_for_transition";
    public static final String EXTRA_CARD = "card";

    private static final long ACTION_PLAY = 1;
    private static final long ACTION_SELECT_MOVIE = 2;

    private Action mActionPlay;
    private Action mActionSelectMovie;

    private ArrayObjectAdapter mRowsAdapter;
    private final DetailsFragmentBackgroundController mDetailsBackground =
            new DetailsFragmentBackgroundController(this);

    DetailsOverviewRow detailsOverview;

    Handler handler;
    TVApplication app;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        app = TVApplication.getInstance();
        setupUi();
        setupEventListeners();
    }

    private void setupUi() {
        // Load the card we want to display from a JSON resource. This JSON data could come from
        // anywhere in a real world app, e.g. a server.
        Program data = null;
        for(Channel c : app.data.channels){
            if(c.name.equals(app.curChannel)){
                for(Program p : c.programs){
                    if(p.id == app.curCardId){
                        data = p;
                        break;
                    }
                }
                break;
            }
        }
        if(data==null){
            return;
        }

        // Setup fragment
        setTitle(data.name);

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
        detailsOverview = new DetailsOverviewRow(data);
        Drawable drawable = getResources().getDrawable(R.drawable.cardbg2);
        detailsOverview.setImageDrawable(drawable);

        ArrayObjectAdapter actionAdapter = new ArrayObjectAdapter();

        app.curFileIdx=0;
        String fileName = data.files.get(0);
        //查看是否存在播放记录
        History history = app.historyManager.findProgramHistory(data);
        if(history!=null) {
            fileName = data.files.get(history.fileIdx);
            app.curFileIdx = history.fileIdx;
        }

        mActionPlay = new Action(ACTION_PLAY, "播放"+fileName);
        actionAdapter.add(mActionPlay);

        if(data.files.size()>1) {
            mActionSelectMovie = new Action(ACTION_SELECT_MOVIE, "选集");
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

        app.http.asyncTakeFile(data.cardPic, new CacheResult() {
            @Override
            public void tackFile(String txt, final Bitmap bmp, boolean isTxt) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        detailsOverview.setImageDrawable(new BitmapDrawable(bmp));
                    }
                });
            }
        }, false);
        app.http.asyncTakeFile(data.bgPic, new CacheResult() {
            @Override
            public void tackFile(String txt, final Bitmap bmp, boolean isTxt) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDetailsBackground.enableParallax();
                        mDetailsBackground.setCoverBitmap(bmp);
                    }
                });
            }
        }, false);

        //initializeBackground(data);
    }

    private void initializeBackground(Program data) {
        mDetailsBackground.enableParallax();
        mDetailsBackground.setCoverBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg));
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

        if (action.getId() == ACTION_PLAY) {
            Intent intent = new Intent(getActivity().getBaseContext(), VideoPlayerActivity.class);
            startActivity(intent);
        } else if (action.getId() == ACTION_SELECT_MOVIE) {
            Intent intent = new Intent(getActivity().getBaseContext(), SelectMovieActivity.class);
            startActivity(intent);
        }
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
