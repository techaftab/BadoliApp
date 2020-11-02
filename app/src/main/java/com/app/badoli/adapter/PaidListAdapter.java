package com.app.badoli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.badoli.R;
import com.app.badoli.repositories.TransactionHistory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaidListAdapter extends RecyclerView.Adapter<PaidListAdapter.PaidListAdapterHolder> implements Filterable,
        TransItemAdapter.TransItemAdapterClickListner {
    //private String TAG=PaidListAdapter.class.getSimpleName();
    private List<TransactionHistory.WalletHistory> loadList;
    private List<TransactionHistory.WalletHistory> loadListFiltered;
    private List<TransactionHistory.WalletHistory.WalletHistoryInner> listItem=new ArrayList<>();
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

        try {
            holder.bindMessage(paidList);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    class PaidListAdapterHolder extends RecyclerView.ViewHolder implements TransItemAdapter.TransItemAdapterClickListner {
        TextView txtDate;
        RecyclerView recyclerViewPaidList;
        PaidListAdapterHolder(View view){
            super(view);
            txtDate=view.findViewById(R.id.txt_date_paidlist);
            recyclerViewPaidList=view.findViewById(R.id.recycler_paidlist);
        }

        void bindMessage(TransactionHistory.WalletHistory paidList) throws ParseException {
            SimpleDateFormat previousFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat newFormat=new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
            Date previousDate=previousFormat.parse(paidList.transction_date);
            String newDate= null;
            if (previousDate != null) {
                newDate = newFormat.format(previousDate);
            }
            TransItemAdapter transItemAdapter = new TransItemAdapter(context, listItem, this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerViewPaidList.setLayoutManager(linearLayoutManager);
            // request_list.setHasFixedSize(true);
            recyclerViewPaidList.setAdapter(transItemAdapter);
            txtDate.setText(newDate);
            listItem.clear();
            listItem.addAll(paidList.wallethistory);
            transItemAdapter.notifyDataSetChanged();
          /*  transItemAdapter.notifyDataSetChanged();
            for (int j=0;j<paidList.wallethistory.size();j++) {
                if (paidList.wallethistory.get(j).type.equals("Debit")) {
                    txtDate.setText(newDate);
                    listItem.clear();
                    listItem.add(paidList.wallethistory.get(j));
                    transItemAdapter.notifyDataSetChanged();
                    Log.e(TAG,"PAID WALLET HISTORY--->"+new Gson().toJson(paidList.wallethistory.get(j)));
                }else {
                    txtDate.setVisibility(View.GONE);
                }
            }*/
        }
    }
}
