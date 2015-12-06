package com.andrewtorr.reasonweb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andrewtorr.reasonweb.Models.User;
import com.elevenfifty.reasonweb.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andrew on 7/28/2015.
 *
 */

public class ProfileEditActivity extends ActionBarActivity {
    @Bind(R.id.avatar_image)
    ParseImageView avatar_image;

    @Bind(R.id.username_text)
    TextView username_text;
    @Bind(R.id.email_edit)
    EditText email_edit;
    @Bind(R.id.firstname_edit)
    EditText firstname_edit;
    @Bind(R.id.lastname_edit)
    EditText lastname_edit;

    @Bind(R.id.save_profile)
    Button save_profile;
    @Bind(R.id.cancel)
    Button cancel;

    private String TAG = "Edit Profile: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);

        final User user = (User) ParseUser.getCurrentUser();

        username_text.setText(user.getUsername());
        email_edit.setText(user.getEmail());
        firstname_edit.setText(user.getFirstName());
        lastname_edit.setText(user.getLastName());

        ParseFile edit_image = user.getAvatarImage();
        if (edit_image != null) {
            avatar_image.setParseFile(edit_image);
            avatar_image.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    Log.d(TAG, "avatar image loaded");
                }
            });
        }


        //TODO: make sure the email address is valid for password resetting purposes
    }

    @OnClick(R.id.avatar_image)
    public void openGallery() {
        Log.d(TAG, "avatar button");

        //TODO: RESIZE IMAGES!!!!!!!!!!!!

        //Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(galleryIntent, 1);
    }

    @OnClick(R.id.save_profile)
    public void saveProfile() {
        //TODO: Change avatar image
        //TODO: Save image (and other info????) to Parse
        //TODO: Send password change email

        User user = (User) ParseUser.getCurrentUser();
        user.setEmail(email_edit.getText().toString());
        user.setFirstName(firstname_edit.getText().toString());
        user.setLastName(lastname_edit.getText().toString());
        //user.setAvatarImage();

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getApplicationContext(), "Changes Saved", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ProfileEditActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.cancel)
    public void cancel() {
        Intent intent = new Intent(ProfileEditActivity.this, ProfileActivity.class);
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
            Intent intent = new Intent(ProfileEditActivity.this, SearchActivity.class);
            startActivity(intent);
            return true;
        } else if (id == com.elevenfifty.reasonweb.R.id.prop_menu_item) {
            Intent intent = new Intent(ProfileEditActivity.this, ViewPropActivity.class);
            startActivityForResult(intent, 1);
            return true;
        } else if (id == com.elevenfifty.reasonweb.R.id.logout_menu_item) {
            ParseUser.logOut();
            Intent intent = new Intent(ProfileEditActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}