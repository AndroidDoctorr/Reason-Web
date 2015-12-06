package com.andrewtorr.reasonweb.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Andrew on 8/31/2015.
 *
 * Nouns:
 * A:  0 - General,     1 - Particular
 * B:  0 - Plural,      1 - Singular
 *
 * Verbs:
 * A: 0 - Passive,      1 - Active/Intransitive,    2 - Active/Transitive
 * B: 0 - Past,         1 - Present (infinitive),   2 - Future
 *
 * Adjectives:
 * A: 0 - Normal,       1 - Comparative,            2 - Superlative
 * B: 0 - Qualitative,  1 - Quantitative/Intensive, 2 - Quantitative/Extensive
 *
 */
@ParseClassName("Term")
public class Term extends ParseObject {
    private String term;
    private String definition;
    private String type;
    private String typeA;
    private String typeB;
    private Integer count;
    private String searchStr;

    public String getType() {
        return getString("type");
    }

    public void setType(String type) {
        put("type", type);
    }

    public String getTerm() {
        return getString("term");
    }

    public void setTerm(String term) {
        put("term", term);
    }

    public String getDefinition() {
        return getString("definition");
    }

    public void setDefinition(String definition) {
        put("definition", definition);
    }

    public String getTypeA() {
        return getString("typeA");
    }

    public void setTypeA(String typeA) {
        put("typeA", typeA);
    }

    public String getTypeB() {
        return getString("typeB");
    }

    public void setTypeB(String typeB) {
        put("typeB", typeB);
    }

    public Integer getCount() {
        return getInt("count");
    }

    public void setCount(Integer count) {
        put("count", count);
    }

    public String getSearchStr() {
        return getString("searchStr");
    }

    public void setSearchStr() {
        put("searchStr", term.toLowerCase() + definition.toLowerCase());
    }
}