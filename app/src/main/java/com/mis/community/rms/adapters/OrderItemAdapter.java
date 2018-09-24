package com.mis.community.rms.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mis.community.rms.R;
import com.mis.community.rms.model.OrderModel;

import java.util.List;

public class OrderItemAdapter extends ArrayAdapter {

    public LayoutInflater inflater;
    private List<OrderModel> orderList;
    private int resource;

    public OrderItemAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);

        orderList = objects;
        this.resource = resource;

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder = null;


        if (convertView == null) {
            viewHolder = new OrderItemAdapter.ViewHolder();
            convertView = inflater.inflate(resource, null);
            viewHolder.itemName = convertView.findViewById(R.id.foodItemName);
            viewHolder.itemPrice = convertView.findViewById(R.id.foodItemPrice);
            viewHolder.countText = convertView.findViewById(R.id.foodQt);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OrderItemAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.itemName.setText(orderList.get(position).getName());
        viewHolder.itemPrice.setText(orderList.get(position).getPrice());
        viewHolder.countText.setText(orderList.get(position).getQuantity());
        Log.d("OnOrders", orderList.get(position).getName());


        return convertView;
    }

    class ViewHolder {
        private TextView itemName;
        private TextView itemPrice;
        private TextView countText;
    }
}
