package com.app.badoli.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.badoli.R;
import com.app.badoli.model.StaffList;

import java.util.ArrayList;
import java.util.List;

public class SwitchStaffListAdapter extends RecyclerView.Adapter<SwitchStaffListAdapter.SwitchStaffListAdapterHolder> implements Filterable {

    //  private String TAG=SearchListAdapter.class.getSimpleName();
    private List<StaffList.Result> loadList;
    private List<StaffList.Result> loadListFiltered;
    private Context context;
    private SwitchStaffListAdapterListner staffListAdapterListner;

    public SwitchStaffListAdapter(Context context, List<StaffList.Result> receivedList, SwitchStaffListAdapterListner staffListAdapterListner) {
        this.loadList = receivedList;
        this.loadListFiltered = receivedList;
        this.staffListAdapterListner = staffListAdapterListner;
        this.context = context;
    }

    public interface SwitchStaffListAdapterListner {
        void onStaffClick(String agent_code, int agent_pin, int merchant_id, String staff_name, int id);
    }

    @NonNull
    @Override
    public SwitchStaffListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff, parent, false);
        return new SwitchStaffListAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SwitchStaffListAdapterHolder holder, int position) {
        final StaffList.Result data = loadListFiltered.get(position);
        holder.bindMessage(data);
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
                    List<StaffList.Result> filteredList = new ArrayList<>();
                    for (StaffList.Result row : loadList) {

                        if (row.getStaff_name().toLowerCase().contains(charString.toLowerCase())) {
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
                loadListFiltered = (ArrayList<StaffList.Result>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class SwitchStaffListAdapterHolder extends RecyclerView.ViewHolder {

        TextView txtName;

        SwitchStaffListAdapterHolder(View view) {
            super(view);
            txtName=view.findViewById(R.id.txtName);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    staffListAdapterListner.onStaffClick(loadListFiltered.get(pos).getAgent_code(),
                            loadListFiltered.get(pos).getAgent_pin(),loadListFiltered.get(pos).getMerchant_id(),
                            loadListFiltered.get(pos).getStaff_name(),loadListFiltered.get(pos).getId());
                }
            });
        }

        @SuppressLint("SetTextI18n")
        void bindMessage(StaffList.Result data)  {
            txtName.setText(data.getStaff_name());
          /*
            txtEmail.setText(data.getEmail());*/
        }
    }
}
