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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andrew on 7/28/2015.
 *
 */

public class ListEvidActivity extends ActionBarActivity {
    @Bind(R.id.submit_evid)
    Button submit_evid;
    @Bind(R.id.evid_list)
    ListView evid_list;

    private String TAG = "Evidence List";
    int xi;
    int yi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_evid);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO: Add Evidence page to make the list items clickable

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.dark_orange));

            evid_list.setFocusable(false);
        }
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
                        } else {
                            Log.d(TAG, "left");
                            Intent intent = new Intent(ListEvidActivity.this, ViewPropActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_left, R.anim.out_left);
                        }
                    } else {
                        Log.d(TAG, "vertical");
                        if (vy > 0) {
                            Log.d(TAG, "down");
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

    @OnClick(R.id.submit_evid)
    public void goToSubmiEvid() {
        Intent intent = new Intent(ListEvidActivity.this, SubmitEvidActivity.class);
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
            Intent intent = new Intent(ListEvidActivity.this, SearchActivity.class);
            startActivity(intent);
            return true;
        } else if (id == com.elevenfifty.reasonweb.R.id.profile_menu_item) {
            Intent intent = new Intent(ListEvidActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == com.elevenfifty.reasonweb.R.id.logout_menu_item) {
            ParseUser.logOut();
            Intent intent = new Intent(ListEvidActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
