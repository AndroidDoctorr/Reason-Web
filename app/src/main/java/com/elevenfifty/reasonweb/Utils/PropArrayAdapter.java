package com.elevenfifty.reasonweb.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.elevenfifty.reasonweb.Models.Prop;
import com.elevenfifty.reasonweb.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Andrew on 9/3/2015.
 *
 */

public class PropArrayAdapter extends ArrayAdapter<Prop> {
    private int resource;
    private ArrayList<Prop> props;
    private LayoutInflater inflater;
    View view;

    public PropArrayAdapter(Context context, int resource, ArrayList<Prop> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.props = objects;
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

        Prop prop = props.get(position);

        holder.subject.setText(prop.getSubject());
        holder.predicate.setText(prop.getPredicate());

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.subject)
        TextView subject;
        @Bind(R.id.predicate)
        TextView predicate;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void updateAdapter(ArrayList<Prop> props) {
        this.props = props;
        super.notifyDataSetChanged();
    }
}
