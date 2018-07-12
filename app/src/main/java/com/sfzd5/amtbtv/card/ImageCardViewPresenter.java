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
import com.sfzd5.amtbtv.model.Program;
import com.sfzd5.amtbtv.util.CacheResult;


/**
 *
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
        boolean bset = false;
        cardView.setMainImage(getContext().getResources().getDrawable(R.drawable.cardbg2));
        if(card instanceof Program){
            Program program = (Program) card;
            if(program.picCreated==0){
                bset = true;
            }
        }
        if(!bset) {
            app.http.asyncTakeFile(card.getCardPic(), new CacheResult() {
                @Override
                public void tackFile(final Bitmap bmp) {
                    if (bmp != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                cardView.setMainImage(new BitmapDrawable(bmp));
                            }
                        });
                    }
                }
            });
        }
    }
}
