package com.app.badoli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.badoli.R;
import com.app.badoli.repositories.TransactionHistory;

import java.util.ArrayList;
import java.util.List;

public class TransItemAdapter extends RecyclerView.Adapter<TransItemAdapter.TransItemAdapterHolder> implements Filterable {

    private String TAG=TransItemAdapter.class.getSimpleName();
    private List<TransactionHistory.WalletHistory.WalletHistoryInner> loadList;
    private List<TransactionHistory.WalletHistory.WalletHistoryInner> loadListFiltered;
    private Context context;
    private TransItemAdapterClickListner transListClickListner;


    public TransItemAdapter(Context context, List<TransactionHistory.WalletHistory.WalletHistoryInner> paidList, TransItemAdapterClickListner transListClickListner) {
        this.loadList = paidList;
        this.loadListFiltered = paidList;
        this.transListClickListner = transListClickListner;
        this.context = context;
    }

    public interface TransItemAdapterClickListner {

    }

    @NonNull
    @Override
    public TransItemAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_adapter, parent, false);
        return new TransItemAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TransItemAdapterHolder holder, int position) {

        final TransactionHistory.WalletHistory.WalletHistoryInner paidList = loadListFiltered.get(position);
        holder.bindMessage(paidList);
    }

    @Override
    public int getItemCount() {
        return loadListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    loadListFiltered = loadList;
                } else {
                    List<TransactionHistory.WalletHistory.WalletHistoryInner> filteredList = new ArrayList<>();
                    for (TransactionHistory.WalletHistory.WalletHistoryInner row : loadList) {

                        if (row.created_at.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    loadListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = loadListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                loadListFiltered = (ArrayList<TransactionHistory.WalletHistory.WalletHistoryInner>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class TransItemAdapterHolder extends RecyclerView.ViewHolder {

        ImageView imgHistory;
        TextView txtMessage,txtAmount,txtDate;

        TransItemAdapterHolder(View view){
            super(view);
            txtDate=view.findViewById(R.id.txt_date_history);
            txtAmount=view.findViewById(R.id.txt_amt_history);
            txtMessage=view.findViewById(R.id.txt_msg_history);
            imgHistory=view.findViewById(R.id.img_history);
        }

        void bindMessage(TransactionHistory.WalletHistory.WalletHistoryInner paidList) {
            txtMessage.setText(paidList.message);
            txtAmount.setText(paidList.amount);
            txtDate.setText(paidList.created_at);
            if (paidList.type.equals("Credit")){
                imgHistory.setImageDrawable(context.getResources().getDrawable(R.drawable.received));
            }else {
                imgHistory.setImageDrawable(context.getResources().getDrawable(R.drawable.paid));
            }
        }
    }

}
