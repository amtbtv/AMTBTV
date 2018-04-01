package com.sfzd5.amtbtv.card;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v17.leanback.widget.ImageCardView;
import android.view.ContextThemeWrapper;

import com.sfzd5.amtbtv.R;
import com.sfzd5.amtbtv.TVApplication;
import com.sfzd5.amtbtv.model.Card;
import com.sfzd5.amtbtv.util.CacheResult;


/**
 * A very basic {@link ImageCardView} {@link android.support.v17.leanback.widget.Presenter}.You can
 * pass a custom style for the ImageCardView in the constructor. Use the default constructor to
 * create a Presenter with a default ImageCardView style.
 */
public class ImageCardViewPresenter extends AbstractCardPresenter<ImageCardView> {

    TVApplication app;
    Handler handler;

    public ImageCardViewPresenter(Context context, int cardThemeResId) {
        super(new ContextThemeWrapper(context, cardThemeResId));
        app = TVApplication.getInstance();
        handler = new Handler();
    }

    public ImageCardViewPresenter(Context context) {
        this(context, R.style.DefaultCardTheme);
    }

    @Override
    protected ImageCardView onCreateView() {
        ImageCardView imageCardView = new ImageCardView(getContext());
//        imageCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "Clicked on ImageCardView", Toast.LENGTH_SHORT).show();
//            }
//        });
        return imageCardView;
    }

    @Override
    public void onBindViewHolder(Card card, final ImageCardView cardView) {
        cardView.setTag(card);
        cardView.setTitleText(card.name);
        //cardView.setContentText(card.getDescription());
        app.http.asyncTakeFile(card.cardPic, new CacheResult() {
            @Override
            public void tackFile(String txt, final Bitmap bmp, boolean isTxt) {
                if(bmp!=null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            cardView.setMainImage(new BitmapDrawable(bmp));
                        }
                    });
                }
            }
        }, false);
    }
}
