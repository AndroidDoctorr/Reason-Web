package com.andrewtorr.reasonweb.Models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Andrew on 8/23/2015.
 *
 * http://www.writingsimplified.com/2009/10/4-types-of-evidence.html
 * http://depts.washington.edu/methods/evidencetypes.html
 */
@ParseClassName("Evid")
public class Evid extends ParseObject {
    private String title;
    private String description;
    private ParseFile imageFile;
    private ParseFile thumbFile;
    private ArrayList<String> links;
    private ArrayList<Prop> supports;
    private Double cert;
    private Integer votes;
    private Double voteSum;
    private String searchStr;

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title",title);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public ArrayList<Prop> getSupports() {
        JSONArray supportsJSON =  getJSONArray("supports");
        supports.clear();
        for (int i=0; i<supportsJSON.length(); i++) {
            try {
                supports.add((Prop) supportsJSON.get(i));
            } catch (JSONException e) {
                Log.e("Evid", e.getMessage());
            }
        }
        return supports;
    }

    public void setSupports(ArrayList<Prop> supports) {
        JSONArray supportsJSON = new JSONArray();
        for (int i=0; i<supports.size(); i++) {
            supportsJSON.put(supports.get(i));
        }
        put("supports",supportsJSON);
    }

    public ParseFile getImageFile() {
        return getParseFile("imageFile");
    }

    public void setImageFile(ParseFile imageFile) {
        put("imageFile",imageFile);
    }

    public ParseFile getThumbFile() {
        return getParseFile("thumbFile");
    }

    public void setThumbFile(ParseFile thumbFile) {
        put("thumbFile",thumbFile);
    }

    public ArrayList<String> getLinks() {
        JSONArray linksJSON =  getJSONArray("links");
        links.clear();
        for (int i=0; i<linksJSON.length(); i++) {
            try {
                links.add((String) linksJSON.get(i));
            } catch (JSONException e) {
                Log.e("Evid", e.getMessage());
            }
        }
        return links;
    }

    public void setLinks(ArrayList<String> links) {
        JSONArray linksJSON = new JSONArray();
        for (int i=0; i<links.size(); i++) {
            linksJSON.put(links.get(i));
        }
        put("links",linksJSON);
    }

    public Double getCert() {
        return getDouble("cert");
    }

    public void setCert(Double cert) {
        put("cert",cert);
    }

    public Integer getVotes() {
        return getInt("votes");
    }

    public void setVotes(Integer votes) {
        put("votes",votes);
    }

    public Double getVoteSum() {
        return getDouble("voteSum");
    }

    public void setVoteSum(Double voteSum) {
        put("voteSum",voteSum);
    }

    public String getSearchStr() {
        return getString("searchStr");
    }

    public void setSearchStr(String searchStr) {
        put("searchStr", searchStr);
    }
}
