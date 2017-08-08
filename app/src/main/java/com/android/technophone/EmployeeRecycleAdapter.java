package com.android.technophone;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by User on 16.06.2017.
 */

public class EmployeeRecycleAdapter extends RecyclerViewCursorAdapter<EmployeeRecycleAdapter.ViewHolder>{

    private static View.OnClickListener mClickListener;
    private static View.OnLongClickListener mOnLongClickListener;
    private Cursor mCursor;
    private Context mContext;

    public EmployeeRecycleAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        Log.d("EmployeeRecycleAdapter", ""+cursor);
        mCursor = cursor;
        mContext = context;
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint)
    {

        try {
            DBHelper dbHelper = new DBHelper(mContext);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM EmployeePhone WHERE (PhoneNumber Like '%"+constraint+"%' ) or (NameEmployeeLowerCase Like '%"+constraint+"%' ) or (NameEmployee Like '%"+constraint+"%' )", null);

            return cursor;
        }catch (NullPointerException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Создание новых View и ViewHolder элемента списка, которые впоследствии могут переиспользоваться.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employee_view, viewGroup, false);
        EmployeeRecycleAdapter.ViewHolder holder = new EmployeeRecycleAdapter.ViewHolder(v);
        //Log.d("onCreateViewHolder", ""+holder.getPosition());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onClick(view);
                //Log.i("TestClick", ""+mClickListener.onClick(view);toString());
            }
        });

        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Log.i("LongClick_Holder", "just text");
                mOnLongClickListener.onLongClick(view);
                return true;
            }
        });

        return holder;
    }

    /**
     * Заполнение виджетов View данными из элемента списка с номером i
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

        //Log.d("onBindViewHolder", ""+cursor);
        viewHolder.tv_name.setText(cursor.getString(1));
        viewHolder.tv_phone.setText(cursor.getString(2));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_phone;

        public ViewHolder(View view) {
            super(view);
            Log.i("ViewHolder", "");
            tv_name = (TextView)view.findViewById(R.id.textViewEmployee);
            tv_phone = (TextView)view.findViewById(R.id.textViewPhoneNumber);
            /*tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onClick(view);
                    //Log.i("TestClick", ""+mClickListener.onClick(view);toString());
                }
            });*/
        }
    }

    public void setClickListener(View.OnClickListener callback) {
        mClickListener = callback;
    }

    public void setOnLongClickListener(View.OnLongClickListener callback) {
        mOnLongClickListener = callback;
    }

}
