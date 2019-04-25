package com.example.tests4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FieldOfficerAdapter extends ArrayAdapter<FieldOfficer> {
    public FieldOfficerAdapter(Context context, List<FieldOfficer> fieldOfficers) {
        super(context, 0, fieldOfficers);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.fieldofficer_item, parent, false);
        }

        FieldOfficer currentOfficer = getItem(position);

        TextView uIDTextView = listItemView.findViewById(R.id.FOid);

        uIDTextView.setText(currentOfficer.getName());

        return listItemView;
    }
}
