package com.elevenfifty.reasonweb;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class ReasonWebApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //ParseObject.registerSubclass(Note.class);

        Parse.initialize(this, "AFIipchqYqrhlQrBrP0DcaqoRBGI8RTthmS4CK7m", "ZGrUiv0JPP2K0A8ZR1Y3KmNpvZ2huLFXE2arH9q6");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

}