package com.webmobril.badoli.adapter;

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

import com.webmobril.badoli.R;
import com.webmobril.badoli.activities.AccountActivities.SignUpActivity;
import com.webmobril.badoli.model.CountryResult;
import com.webmobril.badoli.utilities.GetMyItem;

import java.util.ArrayList;
import java.util.List;


public class Country_Adapter extends RecyclerView.Adapter<Country_Adapter.MyViewHolder> implements Filterable {


    private Context context;
    private List<CountryResult> loadList;
    private List<CountryResult> loadListFiltered;
    private DMTPayHistoryAdapterListener  listener;
    GetMyItem getMyItem;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sort_name, phone_code, country_name;
        MyViewHolder(View view) {
            super(view);
            sort_name = itemView.findViewById(R.id.country_sort_name);
            phone_code = itemView.findViewById(R.id.phone_code);
            country_name = itemView.findViewById(R.id.country_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onRecyclerViewClickListenerSelected(loadListFiltered.get(getAdapterPosition()));
                    listener.onClick(view, getAdapterPosition());
                }
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


    public Country_Adapter(Context context, List<CountryResult> rechargeList,
                           DMTPayHistoryAdapterListener listener,GetMyItem getMyItem) {
        this.context = context;
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
        final CountryResult country = loadListFiltered.get(position);
        holder.sort_name.setText(country.getSortname());
        holder.phone_code.setText(String.valueOf(country.getPhonecode()));
        holder.country_name.setText(country.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getMyItem.GetClickedItem(country.getId(), country.getPhonecode());
               SignUpActivity.slideClose();
            }
        });
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
                    List<CountryResult> filteredList = new ArrayList<>();
                    Log.e("WORD","WORD--->"+charString);
                    final String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (CountryResult row : loadList) {

                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
                           ||row.getPhonecode().toString().toLowerCase().contains(charString.toLowerCase())) {
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
                loadListFiltered = (ArrayList<CountryResult>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public interface DMTPayHistoryAdapterListener {
        void onRecyclerViewClickListenerSelected(CountryResult countryResult);

        void onClick(View v, int pos);
    }
}
