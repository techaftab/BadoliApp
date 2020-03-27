package com.app.badoli.adapter;

import android.annotation.SuppressLint;
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
import com.app.badoli.model.CountryResponse;
import com.app.badoli.model.CountryResult;
import com.app.badoli.utilities.GetMyItem;

import java.util.ArrayList;
import java.util.List;


public class Country_Adapter extends RecyclerView.Adapter<Country_Adapter.MyViewHolder> implements Filterable {


  //  private Context context;
    private List<CountryResponse.CountryResult> loadList;
    private List<CountryResponse.CountryResult> loadListFiltered;
    private DMTPayHistoryAdapterListener  listener;
    private GetMyItem getMyItem;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sort_name, phone_code, country_name;
        MyViewHolder(View view) {
            super(view);
            sort_name = itemView.findViewById(R.id.country_sort_name);
            phone_code = itemView.findViewById(R.id.phone_code);
            country_name = itemView.findViewById(R.id.country_name);
            view.setOnClickListener(view1 -> {
                // send selected contact in callback
                listener.onRecyclerViewClickListenerSelected(loadListFiltered.get(getAdapterPosition()));
                listener.onClick(view1, getAdapterPosition());
            });
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onClick(v, pos);
                }
            });*/
        }
    }

    public Country_Adapter(Context context, List<CountryResponse.CountryResult> rechargeList,
                           DMTPayHistoryAdapterListener listener,GetMyItem getMyItem) {
       // this.context = context;
        this.listener = listener;
        this.loadList = rechargeList;
        this.loadListFiltered = rechargeList;
        this.getMyItem = getMyItem;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final CountryResponse.CountryResult country = loadListFiltered.get(position);
        holder.sort_name.setText(country.sortname);
        holder.phone_code.setText(String.valueOf(country.phonecode));
        holder.country_name.setText(country.name);

        holder.itemView.setOnClickListener(view -> getMyItem.GetClickedItem(country.id, country.phonecode));
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
                    List<CountryResponse.CountryResult> filteredList = new ArrayList<>();
                    Log.e("WORD","WORD--->"+charString);
               //     final String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (CountryResponse.CountryResult row : loadList) {
                        String phone= String.valueOf(row.phonecode);
                        if (row.name.toLowerCase().contains(charString.toLowerCase())
                           ||phone.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    loadListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = loadListFiltered;
                filterResults.count = loadListFiltered.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                loadListFiltered = (ArrayList<CountryResponse.CountryResult>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public interface DMTPayHistoryAdapterListener {
        void onRecyclerViewClickListenerSelected(CountryResponse.CountryResult countryResult);

        void onClick(View v, int pos);
    }
}
