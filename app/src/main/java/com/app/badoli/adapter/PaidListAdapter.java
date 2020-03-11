package com.app.badoli.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.badoli.R;
import com.app.badoli.repositories.TransactionHistory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class PaidListAdapter extends RecyclerView.Adapter<PaidListAdapter.PaidListAdapterHolder> implements Filterable {

    private String TAG=PaidListAdapter.class.getSimpleName();
    private List<TransactionHistory.WalletHistory> loadList;
    private List<TransactionHistory.WalletHistory> loadListFiltered;
    private Context context;
    private PaidListClickListner paidListClickListner;


    public PaidListAdapter(Context context, List<TransactionHistory.WalletHistory> paidList,PaidListClickListner paidListClickListner) {
        this.loadList = paidList;
        this.loadListFiltered = paidList;
        this.paidListClickListner = paidListClickListner;
        this.context = context;
    }

    public interface PaidListClickListner {

    }

    @NonNull
    @Override
    public PaidListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_wallet, parent, false);
        return new PaidListAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PaidListAdapterHolder holder, int position) {

        final TransactionHistory.WalletHistory paidList = loadListFiltered.get(position);

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
                    List<TransactionHistory.WalletHistory> filteredList = new ArrayList<>();
                    for (TransactionHistory.WalletHistory row : loadList) {

                        if (row.transction_date.toLowerCase().contains(charString.toLowerCase())) {
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
                loadListFiltered = (ArrayList<TransactionHistory.WalletHistory>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class PaidListAdapterHolder extends RecyclerView.ViewHolder {
        TextView txtDate;
        RecyclerView recyclerViewPaidList;
        PaidListAdapterHolder(View view){
            super(view);
            txtDate=view.findViewById(R.id.txt_date_paidlist);
            recyclerViewPaidList=view.findViewById(R.id.recycler_paidlist);
        }

        void bindMessage(TransactionHistory.WalletHistory paidList) {
            for (int j=0;j<paidList.wallethistory.size();j++) {
                if (paidList.wallethistory.get(j).type.equals("Debit")) {
                    txtDate.setText(paidList.transction_date);
                    Log.e(TAG,"PAID WALLET HISTORY--->"+new Gson().toJson(paidList.wallethistory.get(j)));
                }
            }
        }
    }
}
