package com.webmobril.badoli.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.webmobril.badoli.adapter.PaidAdapter;
import com.webmobril.badoli.model.PaidModel;
import com.webmobril.badoli.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaidFragment extends Fragment {
    RecyclerView paidrecycler;
    PaidAdapter adapter;
    List<PaidModel> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paid, container, false);
        paidrecycler = view.findViewById(R.id.paid_recycler);
        paidrecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        try {
          /*  Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
          if (getActivity()!=null) {
              getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
              getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
          }
        }catch (Exception e){e.printStackTrace();}

        list = new ArrayList<>();

        list.add(new PaidModel("Transfer Amount", "50", "05-07-1,11.20am", "Alic"));
        list.add(new PaidModel("Transfer Amount", "100", "05-07-1,11.20am", "James"));
        list.add(new PaidModel("Transfer Amount", "150", "05-07-1,11.20am", "Lucas"));
        list.add(new PaidModel("Transfer Amount", "250", "05-07-1,11.20am", "Alic"));

        if (!list.isEmpty()) {
            adapter = new PaidAdapter(list, getActivity());
            paidrecycler.setAdapter(adapter);
            Log.e("list_count", ""+list.size());
        } else {
            Log.e("list_count", "" + list.size());
        }

        return view;
    }
}
