package com.example.ntumobile;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class booking_adapter extends RecyclerView.Adapter<booking_adapter.MyViewHolder> {

    Context context;
    ArrayList<booking_display> list;
    private OnItemClickListener mListener;

    public booking_adapter(Context context, ArrayList<booking_display> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_booking_display,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        booking_display user = list.get(position);
        holder.tr.setText(user.getTR());
        holder.date.setText(user.getDate());
        holder.time.setText(user.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener() {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tr,date,time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tr = itemView.findViewById(R.id.booking_tr);
            date = itemView.findViewById(R.id.booking_date);
            time = itemView.findViewById(R.id.booking_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mListener.onItemClick(position);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

}
