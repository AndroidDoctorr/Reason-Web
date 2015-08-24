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

import com.parse.ParseUser;

/**
 * Created by Andrew on 7/28/2015.
 *
 */

public class PropositionActivity extends ActionBarActivity {
    private final String TAG = "Proposition Activity";
    int xi;
    int yi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposition);

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO: Make premises clickable
        //TODO: Add type and confidence
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
            //case (MotionEvent.ACTION_MOVE) :
                //Log.d(TAG,"Action was MOVE");
                //return true;
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
                            Intent intent = new Intent(PropositionActivity.this, EvidListActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_right, R.anim.out_right);
                        } else {
                            Log.d(TAG, "left");
                            Intent intent = new Intent(PropositionActivity.this, SyllListActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_left, R.anim.out_left);
                        }
                    } else {
                        Log.d(TAG, "vertical");
                        if (vy > 0) {
                            Log.d(TAG, "down");
                            Intent intent = new Intent(PropositionActivity.this, ConclusionListActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_proposition, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == com.elevenfifty.reasonweb.R.id.search_menu_item) {
            Intent intent = new Intent(PropositionActivity.this, SearchActivity.class);
            //intent.putExtra(PROPOSITION, "");
            startActivity(intent);
            return true;
        } else if (id == com.elevenfifty.reasonweb.R.id.profile_menu_item) {
            Intent intent = new Intent(PropositionActivity.this, ProfileActivity.class);
            startActivityForResult(intent, 1);
            return true;
        } else if (id == com.elevenfifty.reasonweb.R.id.logout_menu_item) {
            ParseUser.logOut();
            Intent intent = new Intent(PropositionActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
