package com.webmobril.badoli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webmobril.badoli.model.PaidModel;
import com.webmobril.badoli.R;

import java.util.List;

public class PaidAdapter extends RecyclerView.Adapter<PaidAdapter.PaidHolder> {

    List<PaidModel> list;
    Context context;

    public PaidAdapter(List<PaidModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public PaidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paid_item_list, parent, false);
        return new PaidHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaidHolder holder, int position) {
        PaidModel model = list.get(position);
        holder.name.setText(model.getName());
        holder.amount.setText(model.getAmount());
        holder.transfer.setText(model.getTransfer_text());
        holder.date.setText(model.getTime());
        //holder.paid_image.setBackgroundResource();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaidHolder extends RecyclerView.ViewHolder {
        TextView transfer, amount, name, date;
        ImageView image;

        public PaidHolder(@NonNull View itemView) {
            super(itemView);
            transfer= itemView.findViewById(R.id.txt_transfer);
            amount= itemView.findViewById(R.id.txt_amount);
            name= itemView.findViewById(R.id.txt_name);
            date= itemView.findViewById(R.id.txt_date);
            image = itemView.findViewById(R.id.paid_image);
        }
    }
}
