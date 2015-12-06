package com.andrewtorr.reasonweb;

import android.app.Application;

import com.andrewtorr.reasonweb.Models.Syll;
import com.andrewtorr.reasonweb.Models.Term;
import com.andrewtorr.reasonweb.Models.Evid;
import com.andrewtorr.reasonweb.Models.Prop;
import com.andrewtorr.reasonweb.Models.User;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Calculemus
 *
 * The only way to rectify our reasonings is to make them as tangible as those of the
 * Mathematicians, so that we can find our error at a glance, and when there are disputes among
 * persons, we can simply say: Let us calculate, without further ado, to see who is right.
 **/

public class ReasonWebApplication extends Application {
    private static ReasonWebApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        ParseUser.registerSubclass(User.class);
        ParseObject.registerSubclass(Term.class);
        ParseObject.registerSubclass(Prop.class);
        ParseObject.registerSubclass(Evid.class);
        ParseObject.registerSubclass(Syll.class);

        Parse.initialize(this, "qZMi4h0YpVic4iXcRYq0RuTu3TiNeA0Gg3SkcAuS", "smVZd8YBKoCq8neN1xtaMMWc4dFHHpdauwAfnUyX");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}