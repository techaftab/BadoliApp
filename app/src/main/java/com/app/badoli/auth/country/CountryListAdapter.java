package com.app.badoli.auth.country;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.badoli.R;
import com.app.badoli.model.CountryResponse;
import com.app.badoli.model.CountryResult;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.CountryListAdapterHolder> implements Filterable {

    private String TAG=CountryListAdapter.class.getSimpleName();
    private List<CountryResponse.CountryResult> loadList;
    private List<CountryResponse.CountryResult> loadListFiltered;
    private Context context;

    private CountryListAdapterListener countryListAdapterListener;

    public CountryListAdapter(Context context, List<CountryResponse.CountryResult> receivedList,
                              CountryListAdapterListener countryListAdapterListener) {
        this.loadList = receivedList;
        this.loadListFiltered = receivedList;
        this.countryListAdapterListener = countryListAdapterListener;
        this.context = context;
    }

    public interface CountryListAdapterListener {

        void onCountryClick(int phonecode, String name, int id);
    }

    @NonNull
    @Override
    public CountryListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country_list, parent, false);
        return new CountryListAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CountryListAdapterHolder holder, int position) {
        final CountryResponse.CountryResult countryResult = loadListFiltered.get(position);
        holder.bindMessage(countryResult);
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
                    for (CountryResponse.CountryResult row : loadList) {
                        String code= String.valueOf(row.phonecode);
                        if (row.name.toLowerCase().contains(charString.toLowerCase())
                        ||code.toLowerCase().contains(charString.toLowerCase())
                        ||row.sortname.contains(charString.toLowerCase())) {
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
                loadListFiltered = (ArrayList<CountryResponse.CountryResult>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class CountryListAdapterHolder extends RecyclerView.ViewHolder {
        TextView txtPhoneCode,txtCountrySort,txtCountryName;

        CountryListAdapterHolder(View view) {
            super(view);
            txtPhoneCode=view.findViewById(R.id.txtPhoneCode);
            txtCountrySort=view.findViewById(R.id.txtCountrySort);
            txtCountryName=view.findViewById(R.id.txtCountryName);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    countryListAdapterListener.onCountryClick(loadListFiltered.get(pos).phonecode,loadListFiltered.get(pos).name,
                            loadListFiltered.get(pos).id);
                }
            });
        }

        @SuppressLint({"SetTextI18n", "ResourceType"})
        void bindMessage(CountryResponse.CountryResult countryResult) {
           txtPhoneCode.setText(""+countryResult.phonecode);
           txtCountrySort.setText(countryResult.sortname);
           txtCountryName.setText(countryResult.name);
        }
    }
}
