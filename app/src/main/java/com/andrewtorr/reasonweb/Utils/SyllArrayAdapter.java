package com.andrewtorr.reasonweb.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andrewtorr.reasonweb.Models.Syll;
import com.elevenfifty.reasonweb.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Andrew on 9/3/2015.
 *
 */

public class SyllArrayAdapter extends ArrayAdapter<Syll> {
    private int resource;
    private ArrayList<Syll> sylls;
    private LayoutInflater inflater;
    View view;

    public SyllArrayAdapter(Context context, int resource, ArrayList<Syll> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.sylls = objects;
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

        Syll syll = sylls.get(position);

        holder.conclusion.setText(syll.getConcStr());
        holder.premise_1.setText(syll.getPrem1Str());
        holder.premise_2.setText(syll.getPrem2Str());

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.conclusion)
        TextView conclusion;
        @Bind(R.id.premise_1)
        TextView premise_1;
        @Bind(R.id.premise_2)
        TextView premise_2;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void updateAdapter(ArrayList<Syll> sylls) {
        this.sylls = sylls;
        super.notifyDataSetChanged();
    }
}
