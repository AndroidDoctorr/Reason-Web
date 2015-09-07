package com.elevenfifty.reasonweb;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Andrew on 8/24/2015.
 *
 */

public class SubmitEvidActivity extends ActionBarActivity {
    @Bind(R.id.submit_evid)
    Button submit_evid;
    @Bind(R.id.evidence_image)
    ImageButton evidence_image;
    @Bind(R.id.evid_title)
    EditText evid_title;
    @Bind(R.id.evid_description)
    EditText evid_description;
    @Bind(R.id.evid_links)
    EditText evid_links;

    private String TAG = "Evidence Submit";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_evid);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);

        Integer randImage = (int) Math.ceil(3 * Math.random());
        Log.d("Evidence submit: ", randImage.toString());
        switch (randImage) {
            case 1:
                evidence_image.setBackgroundResource(R.mipmap.evidence_placeholder_image1);
                break;
            case 2:
                evidence_image.setBackgroundResource(R.mipmap.evidence_placeholder_image2);
                break;
            case 3:
                evidence_image.setBackgroundResource(R.mipmap.evidence_placeholder_image3);
        }

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
}
