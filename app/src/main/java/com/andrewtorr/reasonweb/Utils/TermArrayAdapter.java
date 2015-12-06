package com.andrewtorr.reasonweb.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andrewtorr.reasonweb.Models.Term;
import com.elevenfifty.reasonweb.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Andrew on 9/3/2015.
 *
 */

public class TermArrayAdapter extends ArrayAdapter<Term> {
    private int resource;
    private ArrayList<Term> terms;
    private LayoutInflater inflater;
    View view;

    public TermArrayAdapter(Context context, int resource, ArrayList<Term> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.terms = objects;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        view = convertView;
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Term term = terms.get(position);

        if (term.getCount() != 0) {
            holder.term_text.setText(term.getTerm() + " (" + term.getCount() + ")");
        } else {
            holder.term_text.setText(term.getTerm());
        }
        holder.types_text.setText(term.getTypeA() + " " + term.getTypeB() + " " + term.getType());
        holder.definition_text.setText(term.getDefinition());

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.term_text)
        TextView term_text;
        @Bind(R.id.types_text)
        TextView types_text;
        @Bind(R.id.definition_text)
        TextView definition_text;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void updateAdapter(ArrayList<Term> terms) {
        this.terms = terms;
        super.notifyDataSetChanged();
    }
}
