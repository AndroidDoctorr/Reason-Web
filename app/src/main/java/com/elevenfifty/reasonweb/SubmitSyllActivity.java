package com.elevenfifty.reasonweb;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andrew on 9/1/2015.
 *
 */

public class SubmitSyllActivity extends ActionBarActivity {
    @Bind(R.id.submit_syll)
    Button submit_syll;

    private String TAG = "Proposition Submit: ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_syll);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @OnClick(R.id.submit_syll)
    public void submitSyll() {

    }
}