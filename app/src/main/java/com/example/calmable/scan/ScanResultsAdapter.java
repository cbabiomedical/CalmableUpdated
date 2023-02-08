package com.example.calmable.scan;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crrepa.ble.scan.bean.CRPScanDevice;
import com.example.calmable.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanResultsAdapter extends RecyclerView.Adapter<ScanResultsAdapter.ViewHolder>{


    interface OnAdapterItemClickListener {

        void onAdapterViewClick(View view);
    }

    private static final Comparator<CRPScanDevice> SORTING_COMPARATOR = new Comparator<CRPScanDevice>() {
        @Override
        public int compare(CRPScanDevice scanDevice, CRPScanDevice t1) {
            return scanDevice.getDevice().getAddress().compareTo(t1.getDevice().getAddress());
        }
    };

    private final List<CRPScanDevice> data = new ArrayList<>();
    private OnAdapterItemClickListener onAdapterItemClickListener;
    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (onAdapterItemClickListener != null) {
                onAdapterItemClickListener.onAdapterViewClick(v);
            }
        }
    };

    void addScanResult(CRPScanDevice bleScanResult) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getDevice().equals(bleScanResult.getDevice())) {
                data.set(i, bleScanResult);
                notifyItemChanged(i);
                return;
            }
        }

        data.add(bleScanResult);
        Collections.sort(data, SORTING_COMPARATOR);
        notifyDataSetChanged();
    }

    void clearScanResults() {
        data.clear();
        notifyDataSetChanged();
    }

    public CRPScanDevice getItemAtPosition(int childAdapterPosition) {
        return data.get(childAdapterPosition);
    }


    @Override
    public ScanResultsAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        //final View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.two_line_list_item, parent, false);
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.scan_device_name, parent, false);
        itemView.setOnClickListener(onClickListener);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ScanResultsAdapter.ViewHolder holder, int position) {

        final CRPScanDevice scanDevice = data.get(position);
        final BluetoothDevice bleDevice = scanDevice.getDevice();
        holder.line1.setText(String.valueOf(bleDevice.getName()));
        //holder.line1.setText(String.valueOf(bleDevice.getAddress()));
        holder.line2.setText(String.valueOf(bleDevice.getAddress()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    void setOnAdapterItemClickListener(OnAdapterItemClickListener onAdapterItemClickListener) {
        this.onAdapterItemClickListener = onAdapterItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(android.R.id.text1)
        TextView line1;
        @BindView(android.R.id.text2)
        TextView line2;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}