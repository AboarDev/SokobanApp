package com.example.gridview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class PopulateList extends RecyclerView.Adapter<PopulateList.MyViewHolder>{
    private String[] mDataset;
    private Payload mPayload;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView thePosition;
        public TextView theName;
        public MyViewHolder(View v) {
            super(v);
            thePosition = v.findViewById(R.id.item);
            theName = v.findViewById(R.id.name);
        }
    }

    public PopulateList(String[] myDataset, Payload myListener) {
        mDataset = myDataset;
        mPayload = myListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PopulateList.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.level_view, parent, false);
        //...
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - replace the contents of the view with that element
        holder.thePosition.setText(String.valueOf(position + 1));
        holder.theName.setText(mDataset[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPayload.setPayload(position);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
