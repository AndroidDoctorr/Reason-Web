package com.elevenfifty.reasonweb.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Andrew on 8/22/2015.
 *
 */
@ParseClassName("Prop")
public class Prop extends ParseObject {
    private String prop;
    private String c1;  //Will eventually be a prop??
    private String c2;  //""

    public String getProp() {
        return getString("prop");
    }

    public void setProp(String prop) {
        put("prop",prop);
    }

    public String getC1() {
        return getString("c1");
    }

    public void setC1(String c1) {
        put("c1",c1);
    }

    public String getC2() {
        return getString("c2");
    }

    public void setC2(String c2) {
        put("c2",c2);
    }
}
