package com.elevenfifty.reasonweb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.elevenfifty.reasonweb.Models.User;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andrew on 7/28/2015.
 *
 */

public class ProfileActivity extends ActionBarActivity {
    @Bind(R.id.edit_profile)
    Button edit_profile;
    @Bind(R.id.username_text)
    TextView username_text;
    @Bind(R.id.email_text)
    TextView email_text;
    @Bind(R.id.firstname_text)
    TextView firstname_text;
    @Bind(R.id.lastname_text)
    TextView lastname_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);

        User user = (User) ParseUser.getCurrentUser();

        username_text.setText(user.getUsername());
        email_text.setText(user.getEmail());
        firstname_text.setText(user.getFirstName());
        lastname_text.setText(user.getLastName());
    }

    @OnClick(R.id.edit_profile)
    public void editProfile() {
        Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == com.elevenfifty.reasonweb.R.id.search_menu_item) {
            Intent intent = new Intent(ProfileActivity.this, SearchActivity.class);
            startActivity(intent);
            return true;
        } else if (id == com.elevenfifty.reasonweb.R.id.prop_menu_item) {
            Intent intent = new Intent(ProfileActivity.this, ViewPropActivity.class);
            startActivityForResult(intent, 1);
            return true;
        } else if (id == com.elevenfifty.reasonweb.R.id.logout_menu_item) {
            ParseUser.logOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
