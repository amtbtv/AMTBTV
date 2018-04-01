package com.sfzd5.amtbtv.card;


import android.content.Context;
import android.support.v17.leanback.widget.BaseCardView;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.sfzd5.amtbtv.model.Card;
import com.sfzd5.amtbtv.R;

public class TextCardView extends BaseCardView {

    public TextCardView(Context context) {
        super(context, null, R.style.TextCardStyle);
        LayoutInflater.from(getContext()).inflate(R.layout.text_card, this);
        setFocusable(true);
    }

    public void updateUi(Card card) {
        //TextView extraText = (TextView) findViewById(R.id.extra_text);
        TextView primaryText = (TextView) findViewById(R.id.primary_text);
        //final ImageView imageView = (ImageView) findViewById(R.id.main_image);

        primaryText.setText(card.name);
        //extraText.setText(card.getDescription());

        //String iconStr = card.getIcon();
        //byte[] decodedString = Base64.decode(iconStr.substring(iconStr.indexOf(",")  + 1), Base64.DEFAULT);
        //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        //imageView.setImageBitmap(decodedByte);
        /*
        extraText.setText(card.getExtraText());
        primaryText.setText(card.getTitle());


        // Create a rounded drawable.
        int resourceId = card.getLocalImageResourceId(getContext());
        Bitmap bitmap = BitmapFactory
                .decodeResource(getContext().getResources(), resourceId);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), bitmap);
        drawable.setAntiAlias(true);
        drawable.setCornerRadius(
                Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
        imageView.setImageDrawable(drawable);
        */
    }
}