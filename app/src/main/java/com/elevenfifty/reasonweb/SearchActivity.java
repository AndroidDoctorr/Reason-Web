package com.elevenfifty.reasonweb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

//import android.view.GestureDetector;

/**
 * Created by Andrew on 7/28/2015.
 *
 */

public class SearchActivity extends ActionBarActivity {
    private String TAG = "Search Activity: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //Butterknife.bind(this);

        Log.d(TAG,"OnCreate");

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == com.elevenfifty.reasonweb.R.id.prop_menu_item) {
            Intent intent = new Intent(SearchActivity.this, ViewPropActivity.class);
            startActivity(intent);
            return true;
        } else if (id == com.elevenfifty.reasonweb.R.id.profile_menu_item) {
            Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
            startActivityForResult(intent, 1);
            return true;
        } else if (id == com.elevenfifty.reasonweb.R.id.logout_menu_item) {
            ParseUser.logOut();
            Intent intent = new Intent(SearchActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
