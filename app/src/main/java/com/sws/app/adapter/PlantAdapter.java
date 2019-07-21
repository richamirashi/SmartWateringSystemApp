package com.sws.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sws.app.R;
import com.sws.app.db.model.PlantItem;
import com.sws.app.listener.ItemClickListener;

import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantsViewHolder> {

    private List<PlantItem> plantItemList;

    private ItemClickListener mClickListener;

    public PlantAdapter(List<PlantItem> plantItemList) {
        this.plantItemList = plantItemList;
    }

    public class PlantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView plantView;

        public PlantsViewHolder(View plantView){
            super(plantView);
            this.plantView = plantView.findViewById(R.id.tv_recyclerView);
            plantView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    /**
     * Create new views (invoked by the layout manager)
     */
    @Override
    public PlantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return new PlantsViewHolder(view);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(PlantsViewHolder holder, int position) {
        holder.plantView.setText(plantItemList.get(position).getPlantName());
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return plantItemList.size();
    }

    public PlantItem getPlant(int index) {
        return plantItemList.get(index);
    }

    /**
     * Allows clicks events to be caught
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}