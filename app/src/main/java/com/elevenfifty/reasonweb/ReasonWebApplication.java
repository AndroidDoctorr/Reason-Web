package com.elevenfifty.reasonweb;

import android.app.Application;

import com.elevenfifty.reasonweb.Models.Prop;
import com.elevenfifty.reasonweb.Models.User;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ReasonWebApplication extends Application {
    private static ReasonWebApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        ParseUser.registerSubclass(User.class);
        ParseObject.registerSubclass(Prop.class);

        Parse.initialize(this, "qZMi4h0YpVic4iXcRYq0RuTu3TiNeA0Gg3SkcAuS", "smVZd8YBKoCq8neN1xtaMMWc4dFHHpdauwAfnUyX");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

}