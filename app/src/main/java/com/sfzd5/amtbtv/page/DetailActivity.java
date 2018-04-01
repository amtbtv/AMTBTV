package com.sfzd5.amtbtv.page;

import android.app.Activity;
import android.os.Bundle;

import com.sfzd5.amtbtv.R;

public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            DetailFragment fragment = new DetailFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.details_fragment, fragment)
                    .commit();
        }
    }
}
