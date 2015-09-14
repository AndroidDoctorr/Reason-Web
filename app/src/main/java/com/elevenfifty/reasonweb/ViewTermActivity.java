package com.elevenfifty.reasonweb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.elevenfifty.reasonweb.Models.Term;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andrew on 8/24/2015.
 *
 */

public class ViewTermActivity extends ActionBarActivity {
    @Bind(R.id.term_text)
    TextView term_text;
    @Bind(R.id.definition_text)
    TextView definition_text;
    @Bind(R.id.term_type)
    TextView type_text;

    @Bind(R.id.submit_term)
    Button submit_term;

    private String TAG = "Term View: ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_term);
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
            window.setStatusBarColor(this.getResources().getColor(R.color.dark_green));
        }

        Intent intent = getIntent();
        String termID = intent.getStringExtra("termID");
        ParseQuery<Term> termQuery = ParseQuery.getQuery(Term.class);
        termQuery.getInBackground(termID, new GetCallback<Term>() {
            @Override
            public void done(Term term, ParseException e) {
                if (e == null) {
                    term_text.setText(term.getTerm());
                    type_text.setText(term.getTypeA() + " " + term.getTypeB() + " " + term.getType());
                    definition_text.setText(term.getDefinition());
                } else {
                    Log.e(TAG, e.getCode() + ": " + e.getMessage());
                }
            }
        });
    }

    @OnClick(R.id.submit_term)
    public void submitTerm() {
        Intent intent = new Intent(ViewTermActivity.this, SubmitTermActivity.class);
        intent.putExtra("term", "");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Term resultTerm = (Term) data.getSerializableExtra("term");
                if (resultTerm != null) {
                    term_text.setText(resultTerm.getTerm());
                    type_text.setText(resultTerm.getTypeA() + " " + resultTerm.getTypeB() + " " + resultTerm.getType());
                    definition_text.setText(resultTerm.getDefinition());
                }
            }
        }
    }
}