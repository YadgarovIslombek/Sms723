package com.aid.sms723.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aid.sms723.R;
import com.aid.sms723.model.Number;

import java.util.ArrayList;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.MyViewHolder> {
    ArrayList<String> stringArrayList = new ArrayList<>();
    Context context;

    public PhoneAdapter(Context context,ArrayList<String> stringArrayList) {
        this.context = context;
        this.stringArrayList=stringArrayList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_phone,parent,false);
        return new PhoneAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(stringArrayList.get(position));

//        Log.d("Xay", stringArrayList.get(position) + "");
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView textView;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.number1);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}
