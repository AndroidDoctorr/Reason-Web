package com.elevenfifty.reasonweb;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andrew on 8/24/2015.
 *
 */

public class ViewSyllActivity extends ActionBarActivity {
    @Bind(R.id.submit_syll)
    Button submit_syll;

    private String TAG = "Evidence Submit";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_evid);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.dark_orange));
        }


    }

    @OnClick(R.id.submit_syll)
    public void submitSyll() {

    }
}