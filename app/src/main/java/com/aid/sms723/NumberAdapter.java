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
    NumberModel[] numberModel;

    Context context;
    public NumberAdapter(NumberModel[] numberModel) {
        this.numberModel = numberModel;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
        return new NumberAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final NumberModel number = numberModel[position];
        holder.textView.setText(numberModel[position].getNum());
        Log.d("Xay", String.valueOf(number.getNum()));
    }

    @Override
    public int getItemCount() {
        return numberModel.length;
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
