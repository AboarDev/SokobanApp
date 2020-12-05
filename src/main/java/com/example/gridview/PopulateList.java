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

    @Override
    public PopulateList.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.level_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.thePosition.setText(String.valueOf(position + 1));
        holder.theName.setText(mDataset[position]);
        holder.itemView.setOnClickListener(v -> mPayload.setPayload(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
