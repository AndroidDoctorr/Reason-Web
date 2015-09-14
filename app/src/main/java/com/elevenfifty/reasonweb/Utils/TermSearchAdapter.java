package com.elevenfifty.reasonweb.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.elevenfifty.reasonweb.Models.Term;
import com.elevenfifty.reasonweb.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Andrew on 9/3/2015.
 *
 */

public class TermSearchAdapter extends ArrayAdapter<Term> {
    private int resource;
    private ArrayList<Term> terms;
    private LayoutInflater inflater;
    View view;

    public TermSearchAdapter(Context context, int resource, ArrayList<Term> objects) {
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

        holder.search_item_term.setText(term.getTerm());
        holder.search_item_definition.setText(term.getDefinition());

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.search_item_term)
        TextView search_item_term;
        @Bind(R.id.search_item_definition)
        TextView search_item_definition;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void updateAdapter(ArrayList<Term> terms) {
        this.terms = terms;
        super.notifyDataSetChanged();
    }
}
