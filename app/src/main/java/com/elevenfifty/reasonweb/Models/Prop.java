package com.elevenfifty.reasonweb.Models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Andrew on 8/22/2015.
 *
 */
@ParseClassName("Prop")
public class Prop extends ParseObject {
    private String prop;
    private String topP1;
    private String topP2;
    private String topReason;
    private ArrayList<Syll> reasons;
    private ArrayList<Evid> evidence;
    private ArrayList<Syll> conclusions;

    public String getProp() {
        return getString("prop");
    }

    public void setProp(String prop) {
        put("prop",prop);
    }

    public String getTopP1() {
        return getString("topP1");
    }

    public void setTopP1(String topP1) {
        put("topP1",topP1);
    }

    public String getTopP2() {
        return getString("topP2");
    }

    public void setTopP2(String topP2) {
        put("topP2",topP2);
    }

    public String getTopReason() {
        return getString("");
    }

    public void setTopReason(String topReason) {
        put("topReason",topReason);
    }

    public ArrayList<Syll> getReasons() {
        JSONArray reasonsJSON =  getJSONArray("reasons");
        reasons.clear();
        for (int i=0; i<reasonsJSON.length(); i++) {
            try {
                reasons.add((Syll) reasonsJSON.get(i));
            } catch (JSONException e) {
                Log.e("Prop", e.getMessage());
            }
        }
        return reasons;
    }

    public void setReasons(ArrayList<Syll> reasons) {
        JSONArray reasonsJSON = new JSONArray();
        for (int i=0; i<reasons.size(); i++) {
            reasonsJSON.put(reasons.get(i));
        }
        put("s",reasonsJSON);
    }

    public ArrayList<Evid> getEvidence() {
        JSONArray evidenceJSON =  getJSONArray("evidence");
        evidence.clear();
        for (int i=0; i<evidenceJSON.length(); i++) {
            try {
                evidence.add((Evid) evidenceJSON.get(i));
            } catch (JSONException e) {
                Log.e("Prop", e.getMessage());
            }
        }
        return evidence;
    }

    public void setEvidence(ArrayList<Evid> evidence) {
        JSONArray evidenceJSON = new JSONArray();
        for (int i=0; i<evidence.size(); i++) {
            evidenceJSON.put(evidence.get(i));
        }
        put("evidence",evidenceJSON);
    }

    public ArrayList<Syll> getConclusions() {
        JSONArray conclusionsJSON =  getJSONArray("conclusions");
        conclusions.clear();
        for (int i=0; i<conclusionsJSON.length(); i++) {
            try {
                conclusions.add((Syll) conclusionsJSON.get(i));
            } catch (JSONException e) {
                Log.e("Prop", e.getMessage());
            }
        }
        return conclusions;
    }

    public void setConclusions(ArrayList<Syll> conclusions) {
        JSONArray conclusionsJSON = new JSONArray();
        for (int i=0; i<conclusions.size(); i++) {
            conclusionsJSON.put(conclusions.get(i));
        }
        put("conclusions",conclusionsJSON);
    }
}