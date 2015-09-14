package com.elevenfifty.reasonweb.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Andrew on 8/23/2015.
 *
 * http://www.butte.edu/~wmwu/iLogic/2.4/iLogic_2_4.html
 * https://www.fibonicci.com/logical-reasoning/syllogisms/examples-types/
 * http://www.philosophypages.com/lg/e08a.htm
 */
@ParseClassName("Syll")
public class Syll extends ParseObject {
    //TODO: Mark as valid or invalid based on category + type of prop??
    private Prop prem1;
    private Prop prem2;
    private Prop conc;
    private String prem1Str;
    private String prem2Str;
    private String concStr;
    private Integer type;
    private String expl;
    private Double cert;
    private Integer votes;
    private Double voteSum;
    private String searchStr;

    public Prop getPrem1() {
        return (Prop) getParseObject("prem1");
    }

    public void setPrem1(Prop prem1) {
        put("prem1",prem1);
    }

    public Prop getPrem2() {
        return (Prop) getParseObject("prem2");
    }

    public void setPrem2(Prop prem2) {
        put("prem1",prem2);
    }

    public Prop getConc() {
        return (Prop) getParseObject("conc");
    }

    public void setConc(Prop conc) {
        put("conc",conc);
    }

    public Integer getType() {
        return getInt("type");
    }

    public void setType(Integer type) {
        put("type",type);
    }

    public String getPrem1Str() {
        return getString("premlStr");
    }

    public void setPrem1Str(String prem1Str) {
        put("premlStr",prem1Str);
    }

    public String getPrem2Str() {
        return getString("prem2Str");
    }

    public void setPrem2Str(String prem2Str) {
        put("prem2Str",prem2Str);
    }

    public String getConcStr() {
        return getString("concStr");
    }

    public void setConcStr(String concStr) {
        put("concStr",concStr);
    }

    public String getExpl() {
        return getString("expl");
    }

    public void setExpl(String expl) {
        put("expl",expl);
    }

    public String getSearchStr() {
        return getString("searchStr");
    }

    public void setSearchStr(String searchStr) {
        put("searchStr", searchStr);
    }
}
