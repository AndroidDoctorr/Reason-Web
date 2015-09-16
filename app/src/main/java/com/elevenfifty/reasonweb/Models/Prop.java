package com.elevenfifty.reasonweb.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;

/**
 * Created by Andrew on 8/22/2015.
 *
 *  A -
 *
 *  A -
 *
 *  A -
 *
 *  A -
 *
 */

@ParseClassName("Prop")
public class Prop extends ParseObject {
    private String prop;
    private String subject;
    private String predicate;
    private String propType;
    private JSONArray terms;
    private String searchStr;

    public String getProp() {
        return getString("prop");
    }

    public void setProp(String prop) {
        put("prop",prop);
    }

    public String getSubject() {
        return getString("subject");
    }

    public void setSubject(String subject) {
        put("subject", subject);
    }

    public String getPredicate() {
        return getString("predicate");
    }

    public void setPredicate(String predicate) {
        put("predicate",predicate);
    }

    public JSONArray getTerms() {
        return getJSONArray("terms");
    }

    public void setTerms(JSONArray terms) {
        put("terms",terms);
    }

    public String getPropType() {
        return getString("propType");
    }

    public void setPropType(String propType) {
        put("propType", propType);
    }

    public String getSearchStr() {
        return getString("searchStr");
    }

    public void setSearchStr(String searchStr) {
        put("searchStr", searchStr);
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