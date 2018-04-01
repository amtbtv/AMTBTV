package com.sfzd5.amtbtv.page;

import android.app.Activity;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.*;
import android.support.v17.leanback.widget.SpeechRecognitionCallback;
import android.support.v4.app.FragmentActivity;
import android.arch.lifecycle.Lifecycle;
import android.util.Log;

import com.sfzd5.amtbtv.R;

public class SearchActivity extends FragmentActivity implements LifecycleRegistryOwner {

    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    private static final String TAG = "SearchActivity";
    private static final boolean DEBUG = false;
    private static final boolean USE_INTERNAL_SPEECH_RECOGNIZER = false;
    private static final int REQUEST_SPEECH = 1;

    // The fragment has to be retrived from the id which is the runtime information
    public android.support.v17.leanback.app.SearchFragment mFragment;

    public SpeechRecognitionCallback mSpeechRecognitionCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mFragment = (android.support.v17.leanback.app.SearchFragment) getFragmentManager().findFragmentById(R.id.search_fragment);
        if (USE_INTERNAL_SPEECH_RECOGNIZER) {
            mSpeechRecognitionCallback = new SpeechRecognitionCallback() {
                @Override
                public void recognizeSpeech() {
                    if (DEBUG) {
                        Log.v(TAG, "recognizeSpeech");
                    }
                    startActivityForResult(mFragment.getRecognizerIntent(), REQUEST_SPEECH);
                }
            };
            mFragment.setSpeechRecognitionCallback(mSpeechRecognitionCallback);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (DEBUG){
            Log.v(TAG, "onActivityResult requestCode="
                    + requestCode +
                    " resultCode="
                    + resultCode +
                    " data="
                    + data);
        }
        if (requestCode == REQUEST_SPEECH && resultCode == RESULT_OK) {
            mFragment.setSearchQuery(data, true);
        }
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }
}
