package com.andrewtorr.reasonweb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andrewtorr.reasonweb.Models.User;
import com.elevenfifty.reasonweb.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends Activity {
    //@SuppressLint("NewApi")

    @Bind(R.id.txtusername)
    EditText txtusername;
    @Bind(R.id.txtpassword)
    EditText txtpassword;
    @Bind(R.id.txtpassword_check)
    EditText txtpassword_check;
    @Bind(R.id.login)
    Button login;
    @Bind(R.id.signup)
    Button signup;

    private String username;
    private String password;
    private String password_check;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.elevenfifty.reasonweb.R.layout.activity_login);
        ButterKnife.bind(this);
        Log.d("LoginActivity: ", "onCreate");

        //TODO: Add First Name and Last Name to signup
        //TODO: add Avatar Image option

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.dark_red));
        }
    }

    @OnClick(R.id.login)
    public void doLogin() {
        username = txtusername.getText().toString();
        password = txtpassword.getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // If user exist and authenticated, send user to Welcome.class
                    Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),
                            "Successfully Logged in!",
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Invalid username or password.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @OnClick(R.id.signup)
    public void doSignup() {
        username = txtusername.getText().toString();
        password = txtpassword.getText().toString();
        password_check = txtpassword_check.getText().toString();

        txtpassword_check.setVisibility(View.VISIBLE);

        if (username.equals("") || password.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please complete the sign up form",
                    Toast.LENGTH_LONG).show();

        } else {
            // Save new user data into Parse.com Data Storage
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Show a simple Toast message upon successful registration
                        if (password.equals(password_check)) {
                            Toast.makeText(getApplicationContext(),
                                    "Successfully Signed up!",
                                    Toast.LENGTH_LONG).show();
                            ParseUser.logInInBackground(username, password, new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    // If user exist and authenticated, send user to Welcome.class
                                    Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Confirm your password",
                                    Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Log.d("TEST", e.toString());
                        Toast.makeText(getApplicationContext(),
                                "Sign up Error", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
