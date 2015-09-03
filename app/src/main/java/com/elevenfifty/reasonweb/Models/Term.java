package com.elevenfifty.reasonweb.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Andrew on 8/31/2015.
 *
 */
@ParseClassName("Term")
public class Term extends ParseObject {
    private String term;
    private String definition;

}