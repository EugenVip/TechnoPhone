package com.android.technophone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 15.06.2017.
 */

public class EmployeeAdapter extends ArrayAdapter {

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

    public EmployeeAdapter(Context context, ArrayList<Employee> androidWords){
        super(context, 0 , androidWords);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.employee_view, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Employee currentEmployee = (Employee) getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.textViewEmployee);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        nameTextView.setText(currentEmployee.toString());

        return listItemView;
    }
}
