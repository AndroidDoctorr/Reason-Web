package com.andrewtorr.reasonweb;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.andrewtorr.reasonweb.Components.Globals;
import com.andrewtorr.reasonweb.Models.Prop;
import com.andrewtorr.reasonweb.Models.Term;
import com.elevenfifty.reasonweb.R;
import com.andrewtorr.reasonweb.Utils.TermSearchAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andrew on 9/1/2015.
 *
 */

public class SubmitPropActivity extends ActionBarActivity {
    @Bind(R.id.q1_seekbar)
    SeekBar q1_seekbar;
    @Bind(R.id.q2_seekbar)
    SeekBar q2_seekbar;

    @Bind(R.id.verb_s)
    Spinner verb_s;
    @Bind(R.id.verb_p)
    Spinner verb_p;
    @Bind(R.id.helpers_1_s)
    Spinner helpers_1_s;
    @Bind(R.id.helpers_1_p)
    Spinner helpers_1_p;
    @Bind(R.id.helpers_2_s)
    Spinner helpers_2_s;
    @Bind(R.id.helpers_2_p)
    Spinner helpers_2_p;

    @Bind(R.id.qualifier_1)
    TextView qualifier_1;
    @Bind(R.id.qualifier_2)
    TextView qualifier_2;

    @Bind(R.id.subject_search)
    SearchView subject_search;
    @Bind(R.id.predicate_search)
    SearchView predicate_search;

    @Bind(R.id.adjective_switch)
    Switch adjective_switch;
    @Bind(R.id.prep)
    Spinner prep;
    @Bind(R.id.than)
    TextView than;
    @Bind(R.id.by)
    TextView by;
    @Bind(R.id.of)
    TextView of;

    @Bind(R.id.object_search)
    SearchView object_search;
    @Bind(R.id.adjective_search)
    SearchView adjective_search;

    @Bind(R.id.subject_list)
    ListView subject_list;
    @Bind(R.id.predicate_list)
    ListView predicate_list;
    @Bind(R.id.object_list)
    ListView object_list;
    @Bind(R.id.adjective_list)
    ListView adjective_list;

    @Bind(R.id.prop_type)
    TextView prop_type;
    @Bind(R.id.submit_prop)
    Button submit_prop;
    @Bind(R.id.cancel)
    Button cancel;

    private String TAG = "Proposition Submit: ";

    private static Integer cert1;
    private static Integer cert2;

    public static final ArrayList<Term> subjects = new ArrayList<>();
    public static final ArrayList<Term> predicates = new ArrayList<>();
    public static final ArrayList<Term> objects = new ArrayList<>();
    public static final ArrayList<Term> adjectives = new ArrayList<>();
    private TermSearchAdapter termsArrayAdapter;

    private static Term subject;
    private static Term predicate;
    private static Term object;
    private static Term adjective;

    private static String subjectStr = "";
    private static String predVerb;
    private static String qual1Str;
    private static String qual2Str;
    private static String predicateStr = "";
    private static String objectStr;
    private static String adjectiveStr;
    private static String prepStr;
    private static String propStr;

    ParseQuery<Term> subjectQuery;
    ParseQuery<Term> predicateQuery;
    ParseQuery<Term> objectQuery;
    ParseQuery<Term> adjectiveQuery;

    private Boolean subjectOK = false;
    private Boolean predicateOK = false;

    private String genPart = "general";
    private String propType = "";

    ShapeDrawable shape;

    //TODO: ADD NAVIGATION BACK TO SEARCH or PROP VIEW!!!

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_prop);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initial Layout:


        //Seekbar Drawable

        seekbarDrawable();

        //View default visibility:

        subject_list.setVisibility(View.INVISIBLE);
        predicate_list.setVisibility(View.INVISIBLE);
        object_list.setVisibility(View.INVISIBLE);
        adjective_list.setVisibility(View.INVISIBLE);

        adjective_search.setVisibility(View.INVISIBLE);
        object_search.setVisibility(View.INVISIBLE);

        prep.setVisibility(View.INVISIBLE);
        than.setVisibility(View.INVISIBLE);
        by.setVisibility(View.INVISIBLE);
        of.setVisibility(View.INVISIBLE);

        verb_s.setVisibility(View.INVISIBLE);
        verb_p.setVisibility(View.VISIBLE);
        helpers_1_s.setVisibility(View.INVISIBLE);
        helpers_1_p.setVisibility(View.INVISIBLE);
        helpers_2_s.setVisibility(View.INVISIBLE);
        helpers_2_p.setVisibility(View.INVISIBLE);

        qualifier_1.setVisibility(View.VISIBLE);
        q1_seekbar.setVisibility(View.VISIBLE);
        prop_type.setVisibility(View.INVISIBLE);

        pickVerb("verb p");

        //Disable Submit Button initially, enable when valid prop formed
        submit_prop.setEnabled(false);


        //Adjective Switch

        adjective_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adjective_search.setVisibility(View.VISIBLE);
                } else {
                    adjective_search.setVisibility(View.INVISIBLE);
                    adjective = null;
                }
            }
        });



        //Seekbar ChangeListeners:

        cert1 = 39;
        q1_seekbar.setProgress(39);
        q1_seekbar.setProgressDrawable(shape);
        q1_seekbar.setThumb(getResources().getDrawable(R.drawable.cert_bar_thumb));

        q1_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, ((Integer) progress).toString());
                cert1 = progress;

                if (progress < 5) {
                    qualifier_1.setText("No");
                    qual1Str = "No";
                } else if (progress < 20) {
                    qualifier_1.setText("Few");
                    qual1Str = "Few";
                } else if (progress < 40) {
                    qualifier_1.setText("Some");
                    qual1Str = "Some";
                } else if (progress < 60) {
                    qualifier_1.setText("Many");
                    qual1Str = "Many";
                } else if (progress < 80) {
                    qualifier_1.setText("Most");
                    qual1Str = "Most";
                } else if (progress < 95) {
                    qualifier_1.setText("Most");
                    qual1Str = "Most";
                } else {
                    qualifier_1.setText("All");
                    qual1Str = "All";
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        cert2 = 39;
        q2_seekbar.setProgress(39);
        q2_seekbar.setProgressDrawable(shape);
        q2_seekbar.setThumb(getResources().getDrawable(R.drawable.cert_bar_thumb));

        q2_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, ((Integer) progress).toString());
                cert2 = progress;

                if (progress < 5) {
                    qualifier_2.setText("never");
                    qual2Str = "never";
                } else if (progress < 20) {
                    qualifier_2.setText("rarely");
                    qual2Str = "rarely";
                } else if (progress < 40) {
                    qualifier_2.setText("occasionally");
                    qual2Str = "occasionally";
                } else if (progress < 60) {
                    qualifier_2.setText("often");
                    qual2Str = "often";
                } else if (progress < 80) {
                    qualifier_2.setText("usually");
                    qual2Str = "usually";
                } else if (progress < 95) {
                    qualifier_2.setText("almost always");
                    qual2Str = "almost always";
                } else {
                    qualifier_2.setText("always");
                    qual2Str = "always";
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });




        //SearchView Setup:

        SearchManager searchManager = (SearchManager) SubmitPropActivity.this.getSystemService(Context.SEARCH_SERVICE);

        View v1 = getLayoutInflater().inflate(R.layout.item_add_term, null);
        subject_list.addFooterView(v1);
        View v2 = getLayoutInflater().inflate(R.layout.item_add_term, null);
        predicate_list.addFooterView(v2);
        View v3 = getLayoutInflater().inflate(R.layout.item_add_term, null);
        object_list.addFooterView(v3);
        View v4 = getLayoutInflater().inflate(R.layout.item_add_term, null);
        adjective_list.addFooterView(v4);




        //SearchView TextListeners:

        if (subject_search != null) {
            subject_search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            subject_search.setIconifiedByDefault(false);

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    Log.d("TEST","on query text change");

                    if (newText.length() > 1) {
                        subject_list.setVisibility(View.VISIBLE);
                        subjectQuery = ParseQuery.getQuery(Term.class);
                        subjectQuery.whereEqualTo("type","noun");
                        subjectQuery.whereContains("term", newText);
                        subjectQuery.orderByDescending("createdAt");

                        subjectQuery.findInBackground(new FindCallback<Term>() {
                            @Override
                            public void done(List<Term> list, ParseException e) {
                                subjects.clear();
                                for (Term term : list) {
                                    subjects.add(term);
                                }
                                Log.d("TEST", "populating list");
                                termsArrayAdapter = new TermSearchAdapter(getApplicationContext(),
                                        R.layout.search_item_term, subjects);
                                termsArrayAdapter.updateAdapter(subjects);
                                subject_list.setAdapter(termsArrayAdapter);
                            }
                        });
                    } else {
                        subject_list.setVisibility(View.INVISIBLE);
                    }
                    return true;
                }

                public boolean onQueryTextSubmit(String query) {
                    return true;
                }
            };
            subject_search.setOnQueryTextListener(queryTextListener);
        }

        if (predicate_search != null) {
            predicate_search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            predicate_search.setIconifiedByDefault(false);

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    Log.d("TEST","on query text change");

                    if (newText.length() > 1) {
                        predicate_list.setVisibility(View.VISIBLE);
                        predicateQuery = ParseQuery.getQuery(Term.class);
                        predicateQuery.whereContains("term", newText);
                        predicateQuery.orderByDescending("createdAt");

                        predicateQuery.findInBackground(new FindCallback<Term>() {
                            @Override
                            public void done(List<Term> list, ParseException e) {
                                predicates.clear();
                                for (Term term : list) {
                                    predicates.add(term);
                                }
                                Log.d("TEST", "populating list");
                                termsArrayAdapter = new TermSearchAdapter(getApplicationContext(),
                                        R.layout.search_item_term, predicates);
                                termsArrayAdapter.updateAdapter(predicates);
                                predicate_list.setAdapter(termsArrayAdapter);
                            }
                        });
                    } else {
                        predicate_list.setVisibility(View.INVISIBLE);
                    }
                    return true;
                }

                public boolean onQueryTextSubmit(String query) {
                    return true;
                }
            };
            predicate_search.setOnQueryTextListener(queryTextListener);
        }

        if (adjective_search != null) {
            adjective_search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            adjective_search.setIconifiedByDefault(false);

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    Log.d("TEST","on query text change");

                    if (newText.length() > 1) {
                        adjective_list.setVisibility(View.VISIBLE);
                        adjectiveQuery = ParseQuery.getQuery(Term.class);
                        adjectiveQuery.whereEqualTo("type", "adjective");
                        adjectiveQuery.whereContains("term", newText);
                        adjectiveQuery.orderByDescending("createdAt");

                        adjectiveQuery.findInBackground(new FindCallback<Term>() {
                            @Override
                            public void done(List<Term> list, ParseException e) {
                                adjectives.clear();
                                for (Term term : list) {
                                    adjectives.add(term);
                                }
                                Log.d("TEST", "populating list");
                                termsArrayAdapter = new TermSearchAdapter(getApplicationContext(),
                                        R.layout.search_item_term, adjectives);
                                termsArrayAdapter.updateAdapter(adjectives);
                                adjective_list.setAdapter(termsArrayAdapter);
                            }
                        });
                    } else {
                        adjective_list.setVisibility(View.INVISIBLE);
                    }
                    return true;
                }

                public boolean onQueryTextSubmit(String query) {
                    return true;
                }
            };
            adjective_search.setOnQueryTextListener(queryTextListener);
        }

        if (object_search != null) {
            object_search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            object_search.setIconifiedByDefault(false);

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    Log.d("TEST","on query text change");

                    if (newText.length() > 1) {
                        object_list.setVisibility(View.VISIBLE);
                        objectQuery = ParseQuery.getQuery(Term.class);
                        objectQuery.whereEqualTo("type","noun");
                        objectQuery.whereContains("term", newText);
                        objectQuery.orderByDescending("createdAt");

                        objectQuery.findInBackground(new FindCallback<Term>() {
                            @Override
                            public void done(List<Term> list, ParseException e) {
                                objects.clear();
                                for (Term term : list) {
                                    objects.add(term);
                                }
                                Log.d("TEST", "populating list");
                                termsArrayAdapter = new TermSearchAdapter(getApplicationContext(),
                                        R.layout.search_item_term, objects);
                                termsArrayAdapter.updateAdapter(objects);
                                object_list.setAdapter(termsArrayAdapter);
                            }
                        });
                    } else {
                        object_list.setVisibility(View.INVISIBLE);
                    }
                    return true;
                }

                public boolean onQueryTextSubmit(String query) {
                    return true;
                }
            };
            object_search.setOnQueryTextListener(queryTextListener);
        }





        //Search Result List Item ClickListeners:

        subject_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == subjects.size()) {
                    Log.d(TAG, "new term");

                    newTerm(subject_search.getQuery().toString(), "subject");

                } else {
                    Term term = subjects.get(position);
                    Log.d(TAG, term.getTerm() + " selected");

                    subject = term;
                    String termStr;
                    if (term.getCount() != 0) {
                        termStr = term.getTerm() + " (" + term.getCount() + ")";
                    } else {
                        termStr = term.getTerm();
                    }
                    subject_search.setQuery(termStr, false);

                    //subjectQuery.cancel();
                    subjects.clear();
                    termsArrayAdapter.updateAdapter(subjects);
                    subject_list.setAdapter(termsArrayAdapter);
                    subject_list.setVisibility(View.INVISIBLE);

                    makeLayout();
                }
            }
        });

        predicate_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == predicates.size()) {
                    Log.d(TAG, "new term");

                    newTerm(predicate_search.getQuery().toString(), "predicate");

                } else {
                    Term term = predicates.get(position);
                    Log.d(TAG, term.getTerm() + " selected");

                    predicate = term;
                    String termStr;
                    if (term.getCount() != 0) {
                        termStr = term.getTerm() + " (" + term.getCount() + ")";
                    } else {
                        termStr = term.getTerm();
                    }
                    predicate_search.setQuery(termStr, false);

                    //predicateQuery.cancel();
                    predicates.clear();
                    termsArrayAdapter.updateAdapter(predicates);
                    predicate_list.setAdapter(termsArrayAdapter);
                    predicate_list.setVisibility(View.INVISIBLE);

                    makeLayout();
                }
            }
        });

        adjective_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == adjectives.size()) {
                    Log.d(TAG, "new term");

                    newTerm(adjective_search.getQuery().toString(), "adjective");

                } else {
                    Term term = adjectives.get(position);
                    Log.d(TAG, term.getTerm() + " selected");

                    adjective = term;
                    if (term.getCount() != 0) {
                        adjectiveStr = term.getTerm() + " (" + term.getCount() + ")";
                    } else {
                        adjectiveStr = term.getTerm();
                    }

                    adjective_search.setQuery(adjectiveStr, false);

                    adjectiveQuery.cancel();
                    adjectives.clear();
                    termsArrayAdapter.updateAdapter(adjectives);
                    adjective_list.setAdapter(termsArrayAdapter);
                    adjective_list.setVisibility(View.INVISIBLE);

                    makeLayout();
                }
            }
        });

        object_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == objects.size()) {
                    Log.d(TAG, "new term");

                    newTerm(object_search.getQuery().toString(), "object");

                } else {
                    Term term = objects.get(position);
                    Log.d(TAG, term.getTerm() + " selected");

                    object = term;
                    if (term.getCount() != 0) {
                        objectStr = term.getTerm() + " (" + term.getCount() + ")";
                    } else {
                        objectStr = term.getTerm();
                    }
                    object_search.setQuery(objectStr, false);

                    objectQuery.cancel();
                    objects.clear();
                    termsArrayAdapter.updateAdapter(objects);
                    object_list.setAdapter(termsArrayAdapter);
                    object_list.setVisibility(View.INVISIBLE);

                    makeLayout();
                }
            }
        });
    }

    //Cancel (Go back)
    @OnClick(R.id.cancel)
    public void goBack() {
        Intent intent = new Intent(SubmitPropActivity.this, ViewPropActivity.class);
        startActivity(intent);
    }

    //Submit Button

    @OnClick(R.id.submit_prop)
    public void submitProp() {
        //Confirmation dialog

        final Dialog dialog = new Dialog(this, R.style.Dialog_No_Border);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater dialog_inflater = LayoutInflater.from(this);
        View dialog_view = dialog_inflater.inflate(R.layout.confirm_prop, null);

        TextView confirm_subject = (TextView) dialog_view.findViewById(R.id.confirm_subject);
        TextView confirm_predicate = (TextView) dialog_view.findViewById(R.id.confirm_predicate);
        TextView confirm_proptype = (TextView) dialog_view.findViewById(R.id.confirm_proptype);

        confirm_subject.setText(subjectStr);
        confirm_predicate.setText(predicateStr);

        switch (propType) {
            case "A":
                confirm_proptype.setText(propType + " - Universal Affirmative");
                break;
            case "E":
                confirm_proptype.setText(propType + " - Universal Negative");
                break;
            case "I":
                confirm_proptype.setText(propType + " - Particular Affirmative");
                break;
            case "O":
                confirm_proptype.setText(propType + " - Particular Negative");
                break;
            default:
                Log.e(TAG, "Prop Type missing!!");
                confirm_proptype.setText("Prop Type missing...");
        }

        Button back_button = (Button) dialog_view.findViewById(R.id.back_button);
        Button new_prop_button = (Button) dialog_view.findViewById(R.id.new_prop_button);

        View.OnClickListener m_clickListener = new View.OnClickListener(){
            @Override
            public void onClick(View p_v) {
                switch (p_v.getId()) {
                    case R.id.back_button:
                        Log.d(TAG, "Back to edit");
                        dialog.dismiss();
                        break;
                    case R.id.new_prop_button:

                        Log.d(TAG, "Submit New Prop");
                        final Prop newProp = new Prop();

                        JSONArray terms = new JSONArray();
                        terms.put(subject);
                        terms.put(predicate);
                        if (object_search.getVisibility() == View.VISIBLE) {
                            terms.put(object);
                        }
                        if (adjective_search.getVisibility() == View.VISIBLE) {
                            terms.put(adjective);
                        }
                        //Upload image from file
                        Log.d(TAG, "prop: " + propStr);
                        Log.d(TAG, "type: " + propType);
                        Log.d(TAG, "subject: " + subjectStr);
                        Log.d(TAG, "predicate: " + predicateStr);

                        newProp.setTerms(terms);
                        newProp.setSubject(subjectStr);
                        newProp.setPredicate(predicateStr);
                        newProp.setProp(propStr);
                        newProp.setPropType(propType);
                        newProp.setSearchStr(propStr.toLowerCase());

                        newProp.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(SubmitPropActivity.this, "Proposition Saved: \"" + propStr + "\"", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(SubmitPropActivity.this, ViewPropActivity.class);
                                    Globals.propID = newProp.getObjectId();
                                    startActivity(intent);
                                } else {
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        });
                    default:
                        dialog.cancel();
                        break;
                }
            }
        };

        back_button.setOnClickListener(m_clickListener);
        new_prop_button.setOnClickListener(m_clickListener);

        dialog.setContentView(dialog_view);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SubmitPropActivity.this, ViewPropActivity.class);
        startActivity(intent);
    }

    //Functions

    public void newTerm(String term, String where) {
        Intent intent = new Intent(SubmitPropActivity.this, SubmitTermActivity.class);
        intent.putExtra("term", term);
        intent.putExtra("where", where);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {

            //TODO: Make sure selected/new terms come from new term activity!!

            if(resultCode == RESULT_OK){
                String termID = data.getStringExtra("termID");
                final String where = data.getStringExtra("where");
                ParseQuery<Term> termQuery = ParseQuery.getQuery(Term.class);
                termQuery.getInBackground(termID, new GetCallback<Term>() {
                    @Override
                    public void done(Term resultTerm, ParseException e) {
                        if (e == null) {
                            Log.d(TAG, "back from TermSubmit with " + resultTerm.getTerm() + " (" + resultTerm.getCount() + ")");
                            switch (where) {
                                case "subject":
                                    subject = resultTerm;
                                    subject_search.setQuery(subject.getTerm(), true);
                                    break;
                                case "predicate":
                                    predicate = resultTerm;
                                    predicate_search.setQuery(predicate.getTerm(), true);
                                    break;
                                case "object":
                                    object = resultTerm;
                                    object_search.setQuery(object.getTerm(), true);
                                    break;
                                case "adjective":
                                    adjective = resultTerm;
                                    adjective_search.setQuery(adjective.getTerm(), true);
                                    break;
                                default:
                                    Log.e(TAG, "It doesn't know where to put the term!!!");
                                    break;
                            }
                        } else {
                            Log.e(TAG, e.getCode() + ": " + e.getMessage());
                        }
                    }
                });
            }
            if (resultCode == RESULT_CANCELED) {
                //No term chosen (apparently)
                Log.d(TAG, "back from TermSubmit, no term selected");
            }
        }
        makeLayout();
    }

    public void makeLayout() {
        //Set predicate (helper/linking) verb:

        if (verb_s.getVisibility() == View.VISIBLE) {
            predVerb = verb_s.getSelectedItem().toString();
        } else if (verb_p.getVisibility() == View.VISIBLE) {
            predVerb = verb_p.getSelectedItem().toString();
        } else if (helpers_1_s.getVisibility() == View.VISIBLE) {
            predVerb = helpers_1_s.getSelectedItem().toString();
        } else if (helpers_1_p.getVisibility() == View.VISIBLE) {
            predVerb = helpers_1_p.getSelectedItem().toString();
        } else if (helpers_2_s.getVisibility() == View.VISIBLE) {
            predVerb = helpers_1_s.getSelectedItem().toString();
        } else if (helpers_2_p.getVisibility() == View.VISIBLE) {
            predVerb = helpers_1_p.getSelectedItem().toString();
        } else {
            predVerb = "";
        }

        //Check Subject
        if (subject != null) {
            Log.d(TAG, "Subject exists");
            subjectOK = true;

            //Set up for type A
            if (subject.getTypeA().equals("particular")) {
                Log.d(TAG,"particular subject");
                genPart = "particular";
                qualifier_1.setVisibility(View.INVISIBLE);
                q1_seekbar.setVisibility(View.INVISIBLE);
            } else {
                Log.d(TAG,"general (or no) subject");
                genPart = "general";
                qualifier_1.setVisibility(View.VISIBLE);
                q1_seekbar.setVisibility(View.VISIBLE);
            }

            //Set up for type B
            if (subject.getTypeB().equals("singular")) {
                Log.d(TAG,"singular subject");
                pickVerb("verb s");
            } else {
                Log.d(TAG,"plural (or no) subject");
                pickVerb("verb p");
            }
        } else {

            //Default Subject layout
            Log.d(TAG, "default subject layout");
            qualifier_1.setVisibility(View.VISIBLE);
            q1_seekbar.setVisibility(View.VISIBLE);
            pickVerb("verb p");
        }

        //Check Predicate

        if (predicate != null) {
            Log.d(TAG, "Predicate exists");
            //Set up for type
            if (predicate.getType().equals("noun")) {

                //Noun Layout
                Log.d(TAG,"Noun predicate");

                of.setVisibility(View.INVISIBLE);
                by.setVisibility(View.INVISIBLE);
                prep.setVisibility(View.INVISIBLE);
                than.setVisibility(View.INVISIBLE);
                object_search.setVisibility(View.INVISIBLE);
                adjective_switch.setVisibility(View.VISIBLE);
                if (adjective_switch.isChecked()) {
                    adjective_search.setVisibility(View.VISIBLE);
                } else {
                    adjective_search.setVisibility(View.INVISIBLE);
                }

                if ((subject != null) && (subject.getTypeB().equals("singular"))) {
                    Log.d(TAG,"singular noun");
                    pickVerb("verb s");
                    predVerb = "is";
                } else {
                    Log.d(TAG,"plural noun (or null)");
                    pickVerb("verb p");
                    predVerb = "are";
                }

                if (adjective_search.getVisibility() == View.VISIBLE) {
                    Log.d(TAG,"adjective");
                    predicateOK = (adjective != null);
                } else {
                    Log.d(TAG,"no adjective");
                    predicateOK = true;
                }

            } else if (predicate.getType().equals("verb")) {

                //Verb Layout
                Log.d(TAG, "Verb predicate");

                of.setVisibility(View.INVISIBLE);
                than.setVisibility(View.INVISIBLE);
                adjective_switch.setVisibility(View.INVISIBLE);

                if ((subject != null) && (subject.getTypeB().equals("singular"))) {
                    pickVerb("helper 1s");
                    predVerb = "";
                } else {
                    pickVerb("helper ip");
                    predVerb = "";
                }

                if (predicate.getTypeA().equals("passive")) {
                    Log.d(TAG, "passive verb");
                    prep.setVisibility(View.INVISIBLE);
                    by.setVisibility(View.VISIBLE);
                    object_search.setVisibility(View.VISIBLE);
                    predicateOK = (object != null);
                } else if (predicate.getTypeA().equals("transitive")) {
                    Log.d(TAG, "transitive verb");
                    prep.setVisibility(View.VISIBLE);
                    by.setVisibility(View.INVISIBLE);
                    object_search.setVisibility(View.VISIBLE);
                    predicateOK = (object != null);
                } else {
                    Log.d(TAG, "active verb");
                    prep.setVisibility(View.INVISIBLE);
                    by.setVisibility(View.INVISIBLE);
                    object_search.setVisibility(View.INVISIBLE);
                    predicateOK = true;
                }

                if (predicate.getTerm().equals(subject.getTerm()) && predicate.getCount().equals(subject.getCount())) {
                    predicateOK = false;
                    prop_type.setText("Invalid (Tautology)");
                }

            } else if (predicate.getType().equals("adjective")) {

                //Adjective Layout
                Log.d(TAG,"Adjective predicate");

                by.setVisibility(View.INVISIBLE);
                prep.setVisibility(View.INVISIBLE);
                adjective_switch.setVisibility(View.INVISIBLE);
                adjective_search.setVisibility(View.INVISIBLE);

                if ((subject != null) && (subject.getTypeB().equals("singular"))) {
                    pickVerb("helper 2s");
                    predVerb = "is";
                } else {
                    pickVerb("helper 2p");
                    predVerb = "are";
                }

                if (predicate.getTypeB().equals("comparative")) {
                    object_search.setVisibility(View.VISIBLE);
                    than.setVisibility(View.VISIBLE);
                    of.setVisibility(View.INVISIBLE);
                    predicateOK = (object != null);
                } else if (predicate.getTypeB().equals("superlative")) {
                    object_search.setVisibility(View.VISIBLE);
                    than.setVisibility(View.INVISIBLE);
                    of.setVisibility(View.VISIBLE);
                    predicateOK = (object != null);
                } else {
                    object_search.setVisibility(View.INVISIBLE);
                    than.setVisibility(View.INVISIBLE);
                    prep.setVisibility(View.INVISIBLE);
                    object = null;
                    predicateOK = true;
                }
            } else {
                Log.e(TAG, "Something went horribly wrong...");
            }
        } else {
            //Default Predicate View
            Log.d(TAG,"default predicate view");
            if ((subject != null) && (subject.getTypeB().equals("singular"))) {
                pickVerb("verb s");
                predVerb = "is";
            } else {
                pickVerb("verb p");
                predVerb = "are";
            }
            predicateOK = false;

            of.setVisibility(View.INVISIBLE);
            by.setVisibility(View.INVISIBLE);
            prep.setVisibility(View.INVISIBLE);
            than.setVisibility(View.INVISIBLE);
            object_search.setVisibility(View.INVISIBLE);
            adjective_switch.setVisibility(View.VISIBLE);
        }

        prop_type.setVisibility(View.VISIBLE);

        if (genPart.equals("general")) {
            if (cert1 < 6) {
                //E
                propType = "E";
                prop_type.setText("E - Universal Negative");
            } else {
                //A
                propType = "A";
                prop_type.setText("A - Universal Affirmative");
            }
        } else {
            if (cert2 < 6) {
                //O
                propType = "O";
                prop_type.setText("O - Particular Negative");
            } else {
                //I
                propType = "I";
                prop_type.setText("I - Particular Affirmative");
            }
        }

        if (subjectOK && predicateOK) {
            submit_prop.setEnabled(true);
            buildPropStr();
        } else {
            prop_type.setText("");
        }
    }

    public void pickVerb(String view) {
        Log.d(TAG,"pickVerb: " + view);
        verb_s.setVisibility(View.INVISIBLE);
        verb_p.setVisibility(View.INVISIBLE);
        helpers_1_s.setVisibility(View.INVISIBLE);
        helpers_1_p.setVisibility(View.INVISIBLE);
        helpers_2_s.setVisibility(View.INVISIBLE);
        helpers_2_p.setVisibility(View.INVISIBLE);

        switch (view) {
            case "verb s":
                verb_s.setVisibility(View.VISIBLE);
                break;
            case "verb p":
                verb_p.setVisibility(View.VISIBLE);
                break;
            case "helper 1s":
                helpers_1_s.setVisibility(View.VISIBLE);
                break;
            case "helper 1p":
                helpers_1_p.setVisibility(View.VISIBLE);
                break;
            case "helper 2s":
                helpers_2_s.setVisibility(View.VISIBLE);
                break;
            case "helper 2p":
                helpers_2_p.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public void buildPropStr() {
        Log.d(TAG, "Building Prop String...");
        //TODO: Experiment with different types, make sure this works right!!!!
        prepStr = prep.getSelectedItem().toString();

        if (qualifier_1.getVisibility() == View.VISIBLE) {
            subjectStr += qual1Str + " ";
        }

        subjectStr += subject.getTerm();
        if (subject.getCount() > 0) {
            subjectStr += " (" + subject.getCount() + ")";
        }

        predicateStr = " " + predVerb + " " + qual2Str + " ";

        if (predicate.getType().equals("noun")) {
            //Noun predicate

            if (adjective_search.getVisibility() == View.VISIBLE) {
                predicateStr += adjective.getTerm() + " " + predicate.getTerm() + ".";
            } else {
                predicateStr += predicate.getTerm() + ".";
            }
        } else if (predicate.getType().equals("verb")) {
            //Verb predicate

            if (predicate.getTypeA().equals("passive")) {
                //Passive

                predicateStr += predicate.getTerm() + " by " + object.getTerm();
                if (object.getCount() > 0) {
                    predicateStr += " (" + object.getCount() + ").";
                }
            } else if (predicate.getTypeA().equals("transitive")) {
                //Transitive

                predicateStr += predicate.getTerm() + " " + prepStr + " " + object.getTerm();
                if (object.getCount() > 0) {
                    predicateStr += " (" + object.getCount() + ").";
                }
            } else {
                //Active, intransitive

                predicateStr += predicate.getTerm();
                if (predicate.getCount() > 0) {
                    predicateStr += " (" + predicate.getCount() + ").";
                } else {
                    predicateStr += ".";
                }
            }
        } else if (predicate.getType().equals("adjective")) {
            predicateStr += predicate.getTerm();
            if (predicate.getCount() > 0) {
                predicateStr += " (" + predicate.getCount() + ").";
            }
        } else {
            Log.e(TAG, "String failed to build properly");
        }

        propStr = subjectStr + predicateStr;

        Log.d(TAG, propStr);
    }

    public void seekbarDrawable() {
        float width = (Globals.dpWidth + 50.f)*2;
        Log.d(TAG, ((Float) width).toString());

        LinearGradient gradient = new LinearGradient(0.f, 0.f, width, 0.0f,
                new int[] {0xFFFF0000, 0xFFFFFFFF, 0xFF00FF00},
                null, Shader.TileMode.CLAMP);
        shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(gradient);
    }
}