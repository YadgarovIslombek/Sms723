package com.aid.sms723;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.MyViewHolder> {
    ArrayList<Number> modelList = new ArrayList<>();
    ArrayList<String> stringArrayList = new ArrayList<>();
    Context context;

    public NumberAdapter(ArrayList<Number> modelList, Context context) {
        this.modelList.addAll(modelList);
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
        return new NumberAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Number number = modelList.get(position);


        holder.textView.setText(number.getNum_list() + "");
       Log.d("Xay", String.valueOf(number.getNum_list()) + "");
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView textView;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.number);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}
