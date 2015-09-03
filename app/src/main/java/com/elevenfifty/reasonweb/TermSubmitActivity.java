package com.elevenfifty.reasonweb;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Andrew on 8/24/2015.
 *
 */

public class TermSubmitActivity extends ActionBarActivity {
    @Bind(R.id.termtext)
    EditText termtext;
    @Bind(R.id.type_spinner)
    Spinner type_spinner;
    @Bind(R.id.general_radio)
    RadioButton general_radio;
    @Bind(R.id.particular_radio)
    RadioButton particular_radio;
    @Bind(R.id.singular_radio)
    RadioButton singular_radio;
    @Bind(R.id.plural_radio)
    RadioButton plural_radio;
    @Bind(R.id.definition)
    EditText definition;
    @Bind(R.id.submit_term)
    Button submit_term;

    @Bind(R.id.noun_radio_layout)
    LinearLayout noun_radio_layout;
    @Bind(R.id.verb_radio_layout)
    LinearLayout verb_radio_layout;
    @Bind(R.id.adjective_radio_layout)
    LinearLayout adjective_radio_layout;

    private String TAG = "Term Submit: ";
    private Integer type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_term);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);

        general_radio.setChecked(true);
        singular_radio.setChecked(true);

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"item selected: " + position);
                type = position;
                switch (position) {
                    case 0:
                        //Noun Layout
                        noun_radio_layout.setVisibility(View.VISIBLE);
                        verb_radio_layout.setVisibility(View.INVISIBLE);
                        adjective_radio_layout.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        //Verb Layout
                        noun_radio_layout.setVisibility(View.INVISIBLE);
                        verb_radio_layout.setVisibility(View.VISIBLE);
                        adjective_radio_layout.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        //Adjective Layout
                        noun_radio_layout.setVisibility(View.INVISIBLE);
                        verb_radio_layout.setVisibility(View.INVISIBLE);
                        adjective_radio_layout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG,"nothing selected");
            }
        });
    }
}
