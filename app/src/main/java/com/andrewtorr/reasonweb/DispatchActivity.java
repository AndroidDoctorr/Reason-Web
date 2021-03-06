package com.andrewtorr.reasonweb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.andrewtorr.reasonweb.Components.Globals;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

/**
 * Created by Andrew on 8/16/2015.
 *
 */

public class DispatchActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("MainActivity: ", "onCreate");

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        Globals.dpHeight = outMetrics.heightPixels / density;
        Globals.dpWidth  = outMetrics.widthPixels / density;
        Globals.width = (Globals.dpWidth + 50.f)*2;

        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            Intent intent = new Intent(DispatchActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(DispatchActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(DispatchActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
