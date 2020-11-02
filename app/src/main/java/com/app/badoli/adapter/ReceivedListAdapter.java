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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.badoli.R;
import com.app.badoli.repositories.TransactionHistory;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReceivedListAdapter extends RecyclerView.Adapter<ReceivedListAdapter.ReceivedListAdapterHolder> implements Filterable,
        TransItemAdapter.TransItemAdapterClickListner{

    private String TAG=ReceivedListAdapter.class.getSimpleName();
    private List<TransactionHistory.WalletHistory> loadList;
    private List<TransactionHistory.WalletHistory> loadListFiltered;
    private Context context;
    private ReceivedListClickListner receivedListCliskListner;

    private List<TransactionHistory.WalletHistory.WalletHistoryInner> listItem=new ArrayList<>();

    public ReceivedListAdapter(Context context, List<TransactionHistory.WalletHistory> receivedList, ReceivedListClickListner receivedListCliskListner) {
        this.loadList = receivedList;
        this.loadListFiltered = receivedList;
        this.receivedListCliskListner = receivedListCliskListner;
        this.context = context;
    }

    public interface ReceivedListClickListner {

    }

    @NonNull
    @Override
    public ReceivedListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_wallet, parent, false);
        return new ReceivedListAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReceivedListAdapterHolder holder, int position) {
        final TransactionHistory.WalletHistory receivedList = loadListFiltered.get(position);

        try {
            holder.bindMessage(receivedList);
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

    class ReceivedListAdapterHolder extends RecyclerView.ViewHolder implements TransItemAdapter.TransItemAdapterClickListner {
        TextView txtDate;
        RecyclerView recyclerViewPaidList;

        ReceivedListAdapterHolder(View view) {
            super(view);
            txtDate = view.findViewById(R.id.txt_date_paidlist);
            recyclerViewPaidList = view.findViewById(R.id.recycler_paidlist);
        }

        void bindMessage(TransactionHistory.WalletHistory paidList) throws ParseException {
            SimpleDateFormat previousFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat newFormat=new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
            Date previousDate=previousFormat.parse(paidList.transction_date);
            String newDate=newFormat.format(previousDate);

            TransItemAdapter transItemAdapter = new TransItemAdapter(context, listItem, this);;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerViewPaidList.setLayoutManager(linearLayoutManager);
            // request_list.setHasFixedSize(true);
            recyclerViewPaidList.setAdapter(transItemAdapter);
            txtDate.setText(newDate);
            listItem.clear();
            listItem.addAll(paidList.wallethistory);
            transItemAdapter.notifyDataSetChanged();
          /*  for (int j = 0; j < paidList.wallethistory.size(); j++) {
                if (paidList.wallethistory.get(j).type.equals("Credit")) {
                    txtDate.setText(newDate);
                    listItem.clear();
                    listItem.add(paidList.wallethistory.get(j));
                    transItemAdapter.notifyDataSetChanged();
                    Log.e(TAG, "RECIEVED WALLET HISTORY--->" + new Gson().toJson(paidList.wallethistory.get(j)));
                }else {
                    txtDate.setVisibility(View.GONE);
                }
            }*/

        }
    }
}
