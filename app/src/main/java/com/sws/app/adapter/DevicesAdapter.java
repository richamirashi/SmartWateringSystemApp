package com.sws.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sws.app.R;
import com.sws.app.db.model.DeviceItem;
import com.sws.app.listener.ItemClickListener;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DevicesViewHolder> {

    private List<DeviceItem> deviceItemList;
    private ItemClickListener mClickListener;

    public DevicesAdapter(List<DeviceItem> deviceItemList) {
        this.deviceItemList = deviceItemList;
    }

    public class DevicesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView deviceView;

        public DevicesViewHolder(View deviceView){
            super(deviceView);
            this.deviceView = deviceView.findViewById(R.id.tv_recyclerView);
            deviceView.setOnClickListener(this);
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
    public DevicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_device, parent, false);
        return new DevicesViewHolder(view);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(DevicesViewHolder holder, int position) {
        holder.deviceView.setText(deviceItemList.get(position).getDeviceName());
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return deviceItemList.size();
    }

    public DeviceItem getDevice(int index) {
        return deviceItemList.get(index);
    }

    /**
     * Allows clicks events to be caught
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}