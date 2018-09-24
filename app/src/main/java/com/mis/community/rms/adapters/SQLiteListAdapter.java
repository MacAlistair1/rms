package com.mis.community.rms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mis.community.rms.R;

import java.util.ArrayList;

public class SQLiteListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Integer> id;
    ArrayList<String> name;
    ArrayList<String> price;
    ArrayList<String> quantity;

    public SQLiteListAdapter(Context context, ArrayList<Integer> id, ArrayList<String> name, ArrayList<String> price, ArrayList<String> quantity) {

        this.context = context;
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public int getCount() {
        return id.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        LayoutInflater layoutInflater;
        if (convertView == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.order_items, null);
            holder = new Holder();
            holder.foodItemName = convertView.findViewById(R.id.foodItemName);
            holder.foodItemPrice = convertView.findViewById(R.id.foodItemPrice);
            holder.foodQt = convertView.findViewById(R.id.foodQt);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.foodItemName.setText(name.get(position));
        holder.foodItemPrice.setText("Rs." + price.get(position));
        holder.foodQt.setText(quantity.get(position));

        return convertView;
    }

    public class Holder {
        TextView foodItemName;
        TextView foodItemPrice;
        TextView foodQt;
    }
}
