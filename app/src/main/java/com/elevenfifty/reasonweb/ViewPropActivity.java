package com.elevenfifty.reasonweb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

import com.elevenfifty.reasonweb.Components.Globals;
import com.elevenfifty.reasonweb.Models.Prop;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andrew on 7/28/2015.
 *
 */

public class ViewPropActivity extends ActionBarActivity {
    @Bind(R.id.subject_text)
    TextView subject_text;
    @Bind(R.id.predicate_text)
    TextView predicate_text;
    @Bind(R.id.prop_type)
    TextView prop_type_text;

    @Bind(R.id.submit_prop)
    Button submit_prop;

    private ParseQuery<Prop> propQuery = ParseQuery.getQuery(Prop.class);

    private final String TAG = "Prop View: ";
    int xi;
    int yi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prop);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(TAG, Globals.propID);

        try {
            if (Globals.propID != null) {
                propQuery.getInBackground(Globals.propID, new GetCallback<Prop>() {
                    @Override
                    public void done(Prop prop, ParseException e) {
                        if (e == null) {
                            subject_text.setText(prop.getSubject());
                            predicate_text.setText(prop.getPredicate());

                            switch (prop.getPropType()) {
                                case "A":
                                    prop_type_text.setText(prop.getPropType() + " - Universal Affirmative");
                                    break;
                                case "E":
                                    prop_type_text.setText(prop.getPropType() + " - Universal Negative");
                                    break;
                                case "I":
                                    prop_type_text.setText(prop.getPropType() + " - Particular Affirmative");
                                    break;
                                case "O":
                                    prop_type_text.setText(prop.getPropType() + " - Particular Negative");
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            Log.e(TAG, e.getCode() + ": " + e.getMessage());
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        //TODO: Make terms clickable - use Linky() thing, to to Term View
        //TODO: Add confidence/certainty
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = MotionEventCompat.getActionMasked(event);
        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d(TAG, "Action was DOWN");
                xi = (int) event.getRawX();
                yi = (int) event.getRawY();
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.d(TAG,"Action was UP");
                int xf = (int) event.getX();
                int yf = (int) event.getY();

                int vx = (xf - xi);
                int vy = (yf - yi);

                if (vx > 0 || vy > 0) {
                    if (Math.abs(vx) > Math.abs(vy)) {
                        Log.d(TAG, "horizontal");
                        if (vx > 0) {
                            Log.d(TAG, "right");
                            Intent intent = new Intent(ViewPropActivity.this, ListEvidActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_right, R.anim.out_right);
                        } else {
                            Log.d(TAG, "left");
                            Intent intent = new Intent(ViewPropActivity.this, ListSyllActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_left, R.anim.out_left);
                        }
                    } else {
                        Log.d(TAG, "vertical");
                        if (vy > 0) {
                            Log.d(TAG, "down");
                            Intent intent = new Intent(ViewPropActivity.this, ListConcActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_down, R.anim.out_down);
                        } else {
                            Log.d(TAG, "up");
                        }
                    }
                }
                return true;
            //case (MotionEvent.ACTION_CANCEL) :
                //Log.d(TAG,"Action was CANCEL");
                //return true;
            //case (MotionEvent.ACTION_OUTSIDE) :
                //Log.d(TAG,"Movement occurred outside bounds " +
                //        "of current screen element");
                //return true;
            default :
                return super.onTouchEvent(event);
        }
    }

    @OnClick(R.id.submit_prop)
    public void submitProp() {
        Intent intent = new Intent(ViewPropActivity.this, SubmitPropActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_proposition, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == com.elevenfifty.reasonweb.R.id.search_menu_item) {
            Intent intent = new Intent(ViewPropActivity.this, SearchActivity.class);
            //intent.putExtra(PROPOSITION, "");
            startActivity(intent);
            return true;
        } else if (id == com.elevenfifty.reasonweb.R.id.profile_menu_item) {
            Intent intent = new Intent(ViewPropActivity.this, ProfileActivity.class);
            startActivityForResult(intent, 1);
            return true;
        } else if (id == com.elevenfifty.reasonweb.R.id.logout_menu_item) {
            ParseUser.logOut();
            Intent intent = new Intent(ViewPropActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
