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
    //private ArrayList<Syll> reasons; - query for Syllogisms with this as the conclusion instead.
    //private ArrayList<Evid> evidence; - query for Evid with supports() instead?
    //OR query propositions on the evidence page??
    //private ArrayList<Syll> conclusions; - query for

    public String getProp() {
        return getString("prop");
    }

    public void setProp(String prop) {
        put("prop",prop);
    }

    /*public ArrayList<Syll> getReasons() {
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
    }*/
}