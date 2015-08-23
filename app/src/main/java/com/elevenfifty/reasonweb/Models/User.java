package com.elevenfifty.reasonweb.Models;

import com.parse.ParseFile;
import com.parse.ParseUser;

/**
 * Created by Andrew on 8/22/2015.
 *
 */

public class User extends ParseUser {
    private ParseFile avatarImage;
    private String firstName;
    private String lastName;

    public ParseFile getAvatarImage() {
        return getParseFile("avatarImage");
    }

    public void setAvatarImage(ParseFile avatarImage) {
        put("avatarImage",avatarImage);
    }

    public String getFirstName() {
        return getString("firstName");
    }

    public void setFirstName(String firstName) {
        put("firstName",firstName);
    }

    public String getLastName() {
        return getString("lastName");
    }

    public void setLastName(String lastName) {
        put("lastName",lastName);
    }
}
