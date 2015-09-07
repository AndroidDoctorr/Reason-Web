package com.elevenfifty.reasonweb;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.elevenfifty.reasonweb.Components.Globals;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andrew on 9/1/2015.
 *
 */

public class SubmitPropActivity extends ActionBarActivity {
    @Bind(R.id.q1_seekbar)
    SeekBar q1_seekbar;
    @Bind(R.id.q2_seekbar)
    SeekBar q2_seekbar;

    @Bind(R.id.verb)
    TextView verb;
    @Bind(R.id.qualifier_1)
    TextView qualifier_1;
    @Bind(R.id.qualifier_2)
    TextView qualifier_2;

    @Bind(R.id.term1_search)
    SearchView term1_search;
    @Bind(R.id.term2_search)
    SearchView term2_search;

    @Bind(R.id.new_term)
    Button new_term;
    @Bind(R.id.submit_prop)
    Button submit_prop;

    private String TAG = "Proposition Submit: ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_prop);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);

        float width = (Globals.dpWidth - 50.f)*2;
        Log.d(TAG, ((Float) width).toString());

        LinearGradient gradient = new LinearGradient(0.f, 0.f, width, 0.0f,
                new int[] {0xFFFF0000, 0xFFFFFFFF, 0xFF00FF00},
                null, Shader.TileMode.CLAMP);
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(gradient);
        q1_seekbar.setProgressDrawable(shape);
        q1_seekbar.setThumb(getResources().getDrawable(R.drawable.cert_bar_thumb));

        q1_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, ((Integer) progress).toString());
                if (progress < 5) {
                    qualifier_1.setText("No");
                } else if (progress < 20) {
                    qualifier_1.setText("Few");
                } else if (progress < 40) {
                    qualifier_1.setText("Some");
                } else if (progress < 60) {
                    qualifier_1.setText("Many");
                } else if (progress < 80) {
                    qualifier_1.setText("Most");
                } else if (progress < 95) {
                    qualifier_1.setText("Most");
                } else {
                    qualifier_1.setText("All");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        q2_seekbar.setProgressDrawable(shape);
        q2_seekbar.setThumb(getResources().getDrawable(R.drawable.cert_bar_thumb));

        q2_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, ((Integer) progress).toString());
                if (progress < 5) {
                    qualifier_2.setText("Never");
                } else if (progress < 20) {
                    qualifier_2.setText("Rarely");
                } else if (progress < 40) {
                    qualifier_2.setText("Occasionally");
                } else if (progress < 60) {
                    qualifier_2.setText("Often");
                } else if (progress < 80) {
                    qualifier_2.setText("Usually");
                } else if (progress < 95) {
                    qualifier_2.setText("Almost Always");
                } else {
                    qualifier_2.setText("Always");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @OnClick(R.id.new_term)
    public void newTerm() {
        Intent intent = new Intent(SubmitPropActivity.this, SubmitTermActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.submit_prop)
    public void testnav() {
        Intent intent = new Intent(SubmitPropActivity.this, SubmitTermActivity.class);
        startActivity(intent);
    }
}