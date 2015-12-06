package com.andrewtorr.reasonweb;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.andrewtorr.reasonweb.Utils.EvidArrayAdapter;
import com.andrewtorr.reasonweb.Components.Globals;
import com.andrewtorr.reasonweb.Models.Evid;
import com.andrewtorr.reasonweb.Models.Prop;
import com.andrewtorr.reasonweb.Models.Syll;
import com.andrewtorr.reasonweb.Models.Term;
import com.elevenfifty.reasonweb.R;
import com.andrewtorr.reasonweb.Utils.PropArrayAdapter;
import com.andrewtorr.reasonweb.Utils.SyllArrayAdapter;
import com.andrewtorr.reasonweb.Utils.TermSearchAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andrew on 7/28/2015.
 *
 */

public class SearchActivity extends ActionBarActivity {

    @Bind(R.id.term_search)
    Button term_search;
    @Bind(R.id.prop_search)
    Button prop_search;
    @Bind(R.id.syll_search)
    Button syll_search;
    @Bind(R.id.evid_search)
    Button evid_search;

    @Bind(R.id.search)
    SearchView search;

    @Bind(R.id.search_results)
    ListView search_results;

    private String TAG = "Search Activity: ";

    private String category;
    private String searchTermQuery;

    public static final ArrayList<Term> terms = new ArrayList<>();
    public static final ArrayList<Prop> props = new ArrayList<>();
    public static final ArrayList<Syll> sylls = new ArrayList<>();
    public static final ArrayList<Evid> evids = new ArrayList<>();

    private TermSearchAdapter termArrayAdapter;
    private PropArrayAdapter propArrayAdapter;
    private SyllArrayAdapter syllArrayAdapter;
    private EvidArrayAdapter evidArrayAdapter;

    ParseQuery<Term> termQuery;
    ParseQuery<Prop> propQuery;
    ParseQuery<Syll> syllQuery;
    ParseQuery<Evid> evidQuery;

    SearchManager searchManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Log.d(TAG, "OnCreate");

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);

        searchManager = (SearchManager) SearchActivity.this.getSystemService(Context.SEARCH_SERVICE);

        searchProp();
    }

    // Buttons to switch between Search Categories

    @OnClick(R.id.term_search)
    public void searchTerm() {
        Log.d(TAG, "Term Search");
        category = "term";

        search.setQuery("", false);
        search_results.clearChoices();
        if (termQuery != null) {
            termQuery.cancel();
        }
        if (propQuery != null) {
            propQuery.cancel();
        }
        if (syllQuery != null) {
            syllQuery.cancel();
        }
        if (evidQuery != null) {
            evidQuery.cancel();
        }

        terms.clear();
        props.clear();
        sylls.clear();
        evids.clear();

        term_search.setTextColor(this.getResources().getColor(R.color.header));
        prop_search.setTextColor(this.getResources().getColor(R.color.text_main));
        syll_search.setTextColor(this.getResources().getColor(R.color.text_main));
        evid_search.setTextColor(this.getResources().getColor(R.color.text_main));
        search_results.setBackgroundResource(R.drawable.rounded_green);
        search_results.removeAllViewsInLayout();


        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(final String newText) {
                Log.d("TEST","on query text change");
                searchTermQuery = newText;

                if (newText.length() > 1) {
                    termQuery = ParseQuery.getQuery(Term.class);
                    termQuery.whereContains("term", newText);
                    termQuery.orderByDescending("createdAt");

                    termQuery.findInBackground(new FindCallback<Term>() {
                        @Override
                        public void done(List<Term> list, ParseException e) {
                            terms.clear();
                            for (Term term : list) {
                                terms.add(term);
                            }
                            Log.d("TEST", "populating term list");
                            termArrayAdapter = new TermSearchAdapter(getApplicationContext(),
                                    R.layout.search_item_term, terms);
                            termArrayAdapter.updateAdapter(terms);
                            search_results.setAdapter(termArrayAdapter);

                            View foot = getLayoutInflater().inflate(R.layout.item_add_term, null);
                            search_results.addFooterView(foot);
                        }
                    });
                }
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                return true;
            }
        };
        search.setOnQueryTextListener(queryTextListener);


        search_results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < terms.size()-1) {
                    Intent intent = new Intent(SearchActivity.this, ViewTermActivity.class);
                    intent.putExtra("termID", terms.get(position).getObjectId());
                    startActivity(intent);
                } else {
                    newTerm(searchTermQuery, "search");
                }
            }
        });
    }

    @OnClick(R.id.prop_search)
    public void searchProp() {
        Log.d(TAG, "Prop Search");
        category = "prop";

        search.setQuery("", false);
        search_results.clearChoices();
        if (termQuery != null) {
            termQuery.cancel();
        }
        if (propQuery != null) {
            propQuery.cancel();
        }
        if (syllQuery != null) {
            syllQuery.cancel();
        }
        if (evidQuery != null) {
            evidQuery.cancel();
        }

        terms.clear();
        props.clear();
        sylls.clear();
        evids.clear();

        term_search.setTextColor(this.getResources().getColor(R.color.text_main));
        prop_search.setTextColor(this.getResources().getColor(R.color.header));
        syll_search.setTextColor(this.getResources().getColor(R.color.text_main));
        evid_search.setTextColor(this.getResources().getColor(R.color.text_main));
        search_results.setBackgroundResource(R.drawable.rounded_purple);
        search_results.removeAllViewsInLayout();

        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                Log.d("TEST","on query text change");

                if (newText.length() > 1) {
                    propQuery = ParseQuery.getQuery(Prop.class);
                    propQuery.whereContains("searchStr", newText);
                    propQuery.orderByDescending("createdAt");

                    propQuery.findInBackground(new FindCallback<Prop>() {
                        @Override
                        public void done(List<Prop> list, ParseException e) {
                            props.clear();
                            for (Prop prop : list) {
                                props.add(prop);
                            }
                            Log.d("TEST", "populating prop list");
                            propArrayAdapter = new PropArrayAdapter(getApplicationContext(),
                                    R.layout.search_item_prop, props);
                            propArrayAdapter.updateAdapter(props);
                            search_results.setAdapter(propArrayAdapter);

                            View foot = getLayoutInflater().inflate(R.layout.item_add_prop, null);
                            search_results.addFooterView(foot);
                        }
                    });
                }
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                return true;
            }
        };
        search.setOnQueryTextListener(queryTextListener);

        search_results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < terms.size()-1) {
                    Intent intent = new Intent(SearchActivity.this, ViewPropActivity.class);
                    Globals.propID = props.get(position).getObjectId();
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SearchActivity.this, SubmitPropActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick(R.id.syll_search)
    public void searchSyll() {
        Log.d(TAG, "Syll Search");
        category = "syll";

        search.setQuery("", false);
        search_results.clearChoices();
        if (termQuery != null) {
            termQuery.cancel();
        }
        if (propQuery != null) {
            propQuery.cancel();
        }
        if (syllQuery != null) {
            syllQuery.cancel();
        }
        if (evidQuery != null) {
            evidQuery.cancel();
        }

        terms.clear();
        props.clear();
        sylls.clear();
        evids.clear();

        term_search.setTextColor(this.getResources().getColor(R.color.text_main));
        prop_search.setTextColor(this.getResources().getColor(R.color.text_main));
        syll_search.setTextColor(this.getResources().getColor(R.color.header));
        evid_search.setTextColor(this.getResources().getColor(R.color.text_main));
        search_results.setBackgroundResource(R.drawable.rounded_blue);
        search_results.removeAllViewsInLayout();

        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                Log.d("TEST","on query text change");

                if (newText.length() > 1) {
                    syllQuery = ParseQuery.getQuery(Syll.class);
                    syllQuery.whereContains("term", newText);
                    syllQuery.orderByDescending("createdAt");

                    syllQuery.findInBackground(new FindCallback<Syll>() {
                        @Override
                        public void done(List<Syll> list, ParseException e) {
                            sylls.clear();
                            for (Syll syll : list) {
                                sylls.add(syll);
                            }
                            Log.d("TEST", "populating syll list");
                            syllArrayAdapter = new SyllArrayAdapter(getApplicationContext(),
                                    R.layout.list_item_syll, sylls);
                            syllArrayAdapter.updateAdapter(sylls);
                            search_results.setAdapter(syllArrayAdapter);

                            View foot = getLayoutInflater().inflate(R.layout.item_add_syll, null);
                            search_results.addFooterView(foot);
                        }
                    });
                }
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                return true;
            }
        };
        search.setOnQueryTextListener(queryTextListener);

        search_results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < terms.size()-1) {

                } else {
                    Intent intent = new Intent(SearchActivity.this, SubmitSyllActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick(R.id.evid_search)
    public void searchEvid() {
        Log.d(TAG, "Evid Search");
        category = "evid";

        search.setQuery("", false);
        search_results.clearChoices();
        if (termQuery != null) {
            termQuery.cancel();
        }
        if (propQuery != null) {
            propQuery.cancel();
        }
        if (syllQuery != null) {
            syllQuery.cancel();
        }
        if (evidQuery != null) {
            evidQuery.cancel();
        }

        terms.clear();
        props.clear();
        sylls.clear();
        evids.clear();

        term_search.setTextColor(this.getResources().getColor(R.color.text_main));
        prop_search.setTextColor(this.getResources().getColor(R.color.text_main));
        syll_search.setTextColor(this.getResources().getColor(R.color.text_main));
        evid_search.setTextColor(this.getResources().getColor(R.color.header));
        search_results.setBackgroundResource(R.drawable.rounded_orange);
        search_results.removeAllViewsInLayout();

        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                Log.d("TEST","on query text change");

                if (newText.length() > 1) {
                    evidQuery = ParseQuery.getQuery(Evid.class);
                    evidQuery.whereContains("evid", newText);
                    evidQuery.orderByDescending("createdAt");

                    evidQuery.findInBackground(new FindCallback<Evid>() {
                        @Override
                        public void done(List<Evid> list, ParseException e) {
                            evids.clear();
                            for (Evid evid : list) {
                                evids.add(evid);
                            }
                            Log.d("TEST", "populating list");
                            ArrayAdapter termsArrayAdapter = new EvidArrayAdapter(getApplicationContext(),
                                    R.layout.list_item_evid, evids);
                            evidArrayAdapter.updateAdapter(evids);
                            search_results.setAdapter(termsArrayAdapter);

                            View foot = getLayoutInflater().inflate(R.layout.item_add_term, null);
                            search_results.addFooterView(foot);
                        }
                    });
                }
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                return true;
            }
        };
        search.setOnQueryTextListener(queryTextListener);

        search_results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < terms.size() - 1) {

                } else {
                    Intent intent = new Intent(SearchActivity.this, SubmitEvidActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == com.elevenfifty.reasonweb.R.id.prop_menu_item) {
            Intent intent = new Intent(SearchActivity.this, ViewPropActivity.class);
            startActivity(intent);
            return true;
        } else if (id == com.elevenfifty.reasonweb.R.id.profile_menu_item) {
            Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
            startActivityForResult(intent, 1);
            return true;
        } else if (id == com.elevenfifty.reasonweb.R.id.logout_menu_item) {
            ParseUser.logOut();
            Intent intent = new Intent(SearchActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void newTerm(String term, String where) {
        Intent intent = new Intent(SearchActivity.this, SubmitTermActivity.class);
        intent.putExtra("term", term);
        intent.putExtra("where", where);
        startActivityForResult(intent, 1);
    }
}
