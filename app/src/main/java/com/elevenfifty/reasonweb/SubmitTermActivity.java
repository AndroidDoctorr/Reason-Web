package com.elevenfifty.reasonweb;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.elevenfifty.reasonweb.Models.Term;
import com.elevenfifty.reasonweb.Utils.TermArrayAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andrew on 8/24/2015.
 *
 * Nouns:
 * A:  0 - General, 1 - Particular
 * B:  0 - Plural, 1 - Singular
 *
 * Verbs:
 * A: 0 - Passive, 1 - Active/Intransitive, 2 - Active/Transitive
 * B: 0 - Past, 1 - Present (infinitive), 2 - Future
 *
 * Adjectives:
 * A: 0 - Normal, 1 - Comparative, 2 - Superlative
 * B: 0 - Qualitative, 1 - Quantitative/Intensive, 2 - Quantitative/Extensive
 *
 */

public class SubmitTermActivity extends ActionBarActivity {
    @Bind(R.id.term_text)
    EditText term_text;
    @Bind(R.id.type_spinner)
    Spinner type_spinner;

    @Bind(R.id.noun_radio_layout)
    LinearLayout noun_radio_layout;
    @Bind(R.id.verb_radio_layout)
    LinearLayout verb_radio_layout;
    @Bind(R.id.adjective_radio_layout)
    LinearLayout adjective_radio_layout;

    //Nouns A
    @Bind(R.id.general_radio)
    RadioButton general_radio;
    @Bind(R.id.particular_radio)
    RadioButton particular_radio;
    //Nouns B
    @Bind(R.id.singular_radio)
    RadioButton singular_radio;
    @Bind(R.id.plural_radio)
    RadioButton plural_radio;

    //Verbs A
    @Bind(R.id.passive_radio)
    RadioButton passive_radio;
    @Bind(R.id.intransitive_radio)
    RadioButton intransitive_radio;
    @Bind(R.id.transitive_radio)
    RadioButton transitive_radio;
    //Verbs B
    @Bind(R.id.past_radio)
    RadioButton past_radio;
    @Bind(R.id.present_radio)
    RadioButton present_radio;
    @Bind(R.id.future_radio)
    RadioButton future_radio;

    //Adjectives A
    @Bind(R.id.normal_radio)
    RadioButton normal_radio;
    @Bind(R.id.comparative_radio)
    RadioButton comparative_radio;
    @Bind(R.id.superlative_radio)
    RadioButton superlative_radio;
    //Adjectives B
    @Bind(R.id.qualitative_radio)
    RadioButton qualitative_radio;
    @Bind(R.id.extensive_radio)
    RadioButton extensive_radio;
    @Bind(R.id.intensive_radio)
    RadioButton intensive_radio ;

    @Bind(R.id.definition_text)
    EditText definition_text;
    @Bind(R.id.submit_term)
    Button submit_term;

    private String termStr;
    private String typeStr;
    private String definitionStr;

    private String type = "noun";
    private String typeA;
    private String typeB;

    private String where;

    private String TAG = "Term Submit: ";
    //private Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_term);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(com.elevenfifty.reasonweb.R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        where = intent.getStringExtra("where");
        termStr = intent.getStringExtra("term");
        if (!termStr.equals("")) {
            term_text.setText(termStr);
        }

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.dark_green));
        }

        //Set up Default Layout

        general_radio.setChecked(true);
        singular_radio.setChecked(true);
        present_radio.setChecked(true);
        intransitive_radio.setChecked(true);
        normal_radio.setChecked(true);
        qualitative_radio.setChecked(true);

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"item selected: " + position);
                switch (position) {
                    case 0:
                        //Noun Layout
                        type = "noun";
                        noun_radio_layout.setVisibility(View.VISIBLE);
                        verb_radio_layout.setVisibility(View.INVISIBLE);
                        adjective_radio_layout.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        //Verb Layout
                        type = "verb";
                        noun_radio_layout.setVisibility(View.INVISIBLE);
                        verb_radio_layout.setVisibility(View.VISIBLE);
                        adjective_radio_layout.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        //Adjective Layout
                        type = "adjective";
                        noun_radio_layout.setVisibility(View.INVISIBLE);
                        verb_radio_layout.setVisibility(View.INVISIBLE);
                        adjective_radio_layout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG,"nothing selected");
            }
        });
    }

    @OnClick(R.id.submit_term)
    public void submitTerm() {
        //Set term and definition from EditTexts
        Log.d(TAG, "Submit Button pressed");

        termStr = term_text.getText().toString();
        definitionStr = definition_text.getText().toString();

        if (termStr.isEmpty()) {
            Toast.makeText(this, "You forgot to name the term!", Toast.LENGTH_SHORT).show();
        } else if(definitionStr.isEmpty()) {
            Toast.makeText(this, "You forgot to define the term!", Toast.LENGTH_SHORT).show();
        } else {
            //TODO: Spell-check?? Check dictionary???
            //Set term types (see class description)
            switch (type) {
                case "noun":
                    typeA = general_radio.isChecked() ? "general" : "particular";
                    typeB = singular_radio.isChecked() ? "singular" : "plural";
                    break;
                case "verb":
                    typeA = passive_radio.isChecked() ? "passive" :
                            (intransitive_radio.isChecked() ? "intransitive" : "transitive");
                    typeB = past_radio.isChecked() ? "past" :
                            (present_radio.isChecked() ? "present" : "future");
                    break;
                case "adjective":
                    typeA = normal_radio.isChecked() ? "" :
                            (comparative_radio.isChecked() ? "comparative" : "superlative");
                    typeB = qualitative_radio.isChecked() ? "qualitative" :
                            (intensive_radio.isChecked() ? "intensive" : "extensive");
                    break;
            }

            typeStr = typeA + " " + typeB  + " " + type;

            ParseQuery<Term> termQuery = new ParseQuery<>(Term.class);
            //TODO: Come up with a better way to do this using regular expressions?
            termQuery.whereEqualTo("term", termStr);
            termQuery.orderByAscending("count");
            Log.d(TAG, "Running query...");
            termQuery.findInBackground(new FindCallback<Term>() {
                @Override
                public void done(List<Term> list, ParseException e) {
                    Log.d(TAG, "Query finished");
                    if (e != null) {
                        Log.e(TAG, e.getMessage());
                    }
                    //TODO: Check to make sure terms in definition are valid???
                    if (list.isEmpty()) {
                        //Term is new, make a new Term object

                        Term newTerm = new Term();

                        newTerm.setTerm(termStr);
                        newTerm.setDefinition(definitionStr);
                        newTerm.setType(type);
                        newTerm.setTypeA(typeA);
                        newTerm.setTypeB(typeB);
                        newTerm.setSearchStr(termStr.toLowerCase() + definitionStr.toLowerCase());
                        newTerm.setCount(0);

                        confirmTerm(newTerm);
                    } else {
                        //Term exists, throw Term Exists dialog

                        ArrayList<Term> terms = new ArrayList<>(list);

                        termExists(terms);
                    }
                }
            });
        }
    }

    public void termExists(final ArrayList<Term> terms) {
        //TODO: USE CARDS TO SHOW DEFINITIONS OF TERM!!!!

        final Dialog e_dialog = new Dialog(this, R.style.Dialog_No_Border);
        e_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater dialog_inflater = LayoutInflater.from(this);
        View e_dialog_view = dialog_inflater.inflate(R.layout.exists_term, null);

        TextView term_title = (TextView) e_dialog_view.findViewById(R.id.exists_term_title);
        ListView term_list = (ListView) e_dialog_view.findViewById(R.id.term_list);

        TermArrayAdapter arrayAdapter = new TermArrayAdapter(this, R.layout.list_item_term, terms);
        arrayAdapter.updateAdapter(terms);
        term_list.setAdapter(arrayAdapter);

        //TODO: Make sure term comes back to SubmitProp and to the right place
        term_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, terms.get(position).getTerm() + " selected");
                Term term = terms.get(position);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("termID", term.getObjectId());
                returnIntent.putExtra("where", where);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        Button newTerm = (Button) e_dialog_view.findViewById(R.id.new_term_button);
        Button back = (Button) e_dialog_view.findViewById(R.id.back_button);

        term_title.setText(termStr);

        View.OnClickListener m_clickListener = new View.OnClickListener(){
            @Override
            public void onClick(View p_v) {
                switch (p_v.getId()) {
                    case R.id.new_term_button:
                        //New Term (shown with a "(#)" at the end showing the definition #)

                        Term newTerm = new Term();

                        newTerm.setTerm(termStr);
                        newTerm.setCount(terms.size() + 1);
                        newTerm.setDefinition(definitionStr);
                        newTerm.setType(type);
                        newTerm.setTypeA(typeA);
                        newTerm.setTypeB(typeB);
                        newTerm.setSearchStr(termStr.toLowerCase() + definitionStr.toLowerCase());

                        confirmTerm(newTerm);

                        e_dialog.dismiss();
                        break;
                    case R.id.back_button:

                        e_dialog.cancel();
                        break;
                    default:
                        e_dialog.dismiss();
                        break;
                }
            }
        };

        newTerm.setOnClickListener(m_clickListener);
        back.setOnClickListener(m_clickListener);

        e_dialog.setContentView(e_dialog_view);
        e_dialog.show();
    }

    public void confirmTerm(final Term term) {
        final Dialog c_dialog = new Dialog(this, R.style.Dialog_No_Border);
        c_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater dialog_inflater = LayoutInflater.from(this);
        View c_dialog_view = dialog_inflater.inflate(R.layout.confirm_term, null);

        TextView confirm_term_text = (TextView) c_dialog_view.findViewById(R.id.confirm_term_text);
        TextView confirm_term_type = (TextView) c_dialog_view.findViewById(R.id.confirm_term_type);
        TextView confirm_definition_text = (TextView) c_dialog_view.findViewById(R.id.confirm_definition_text);

        Button new_term_button = (Button) c_dialog_view.findViewById(R.id.new_term_button);
        Button back_button = (Button) c_dialog_view.findViewById(R.id.back_button);

        confirm_term_text.setText(termStr);
        confirm_term_type.setText(typeStr);
        confirm_definition_text.setText(definitionStr);

        View.OnClickListener m_clickListener = new View.OnClickListener(){
            @Override
            public void onClick(View p_v) {
                switch (p_v.getId()) {
                    case R.id.new_term_button:
                        term.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(SubmitTermActivity.this, "New Term Saved!", Toast.LENGTH_SHORT).show();

                                    //Go back to Prop Activity with term

                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("termID", term.getObjectId());
                                    returnIntent.putExtra("where", where);
                                    setResult(RESULT_OK, returnIntent);
                                    c_dialog.dismiss();
                                    finish();
                                }
                            }
                        });
                    case R.id.back_button:

                        c_dialog.cancel();
                        break;
                    default:
                        c_dialog.dismiss();
                        break;
                }
            }
        };

        new_term_button.setOnClickListener(m_clickListener);
        back_button.setOnClickListener(m_clickListener);

        c_dialog.setContentView(c_dialog_view);
        c_dialog.show();
    }
}
