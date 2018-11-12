package com.homecredit.exam.cities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homecredit.exam.R;
import com.homecredit.exam.models.City;

import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesViewHolder> {
    private Context context;
    private List<City> data;

    public CitiesAdapter(Context context, List<City> data) {
        this.context = context;
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        CitiesViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.cities_listitem, parent, false);
        holder = new CitiesViewHolder(view, context);

        if (view != null) {
            view.setTag(holder);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesViewHolder holder, int position) {
        City city = data.get(holder.getAdapterPosition());
        holder.setData(city);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
