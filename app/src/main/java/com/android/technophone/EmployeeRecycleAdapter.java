package com.android.technophone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 16.06.2017.
 */

public class EmployeeRecycleAdapter extends RecyclerView.Adapter<EmployeeRecycleAdapter.ViewHolder> implements Filterable{

    private ArrayList<Employee> mArrayList;
    private ArrayList<Employee> mFilteredList;

    public EmployeeRecycleAdapter(Context context, ArrayList<Employee> records) {
        mArrayList = records;
        mFilteredList = records;
    }

    /**
     * Создание новых View и ViewHolder элемента списка, которые впоследствии могут переиспользоваться.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employee_view, viewGroup, false);
        return new ViewHolder(v);
    }

    /**
     * Заполнение виджетов View данными из элемента списка с номером i
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.tv_name.setText(mFilteredList.get(i).toString());
    }

    @Override
    public int getItemCount() {
        //Log.i("DAtaTest", ""+mFilteredList.size());
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                    Log.i("FilterTest", "empty filter. "+mArrayList.size());
                } else {

                    Log.i("FilterTest", "charString "+charString);
                    ArrayList<Employee> filteredList = new ArrayList<>();

                    for (Employee employee : mArrayList) {

                        if (employee.getmPhoneNumber().toLowerCase().contains(charString) || employee.getmNameEmployee().toLowerCase().contains(charString) ) {

                            filteredList.add(employee);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Employee>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView)view.findViewById(R.id.textViewEmployee);
        }
    }
}
