package com.elevenfifty.reasonweb.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.elevenfifty.reasonweb.Models.Evid;
import com.elevenfifty.reasonweb.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Andrew on 9/3/2015.
 *
 */

public class EvidArrayAdapter extends ArrayAdapter<Evid> {
    private int resource;
    private ArrayList<Evid> evidence;
    private LayoutInflater inflater;
    View view;

    public EvidArrayAdapter(Context context, int resource, ArrayList<Evid> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.evidence = objects;
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

        Evid evid = evidence.get(position);

        holder.evid_image.setParseFile(evid.getThumbFile());
        holder.evid_title.setText(evid.getTitle());
        holder.evid_description.setText(evid.getText());

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.evid_title)
        TextView evid_title;
        @Bind(R.id.evid_description)
        TextView evid_description;
        @Bind(R.id.evid_image)
        com.parse.ParseImageView evid_image;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void updateAdapter(ArrayList<Evid> evidence) {
        this.evidence = evidence;
        super.notifyDataSetChanged();
    }
}
