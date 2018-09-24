package com.mis.community.rms.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mis.community.rms.Common.CommonPos;
import com.mis.community.rms.R;
import com.mis.community.rms.model.MenuItemsModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class MenuItemsAdapter extends ArrayAdapter {

    public LayoutInflater inflater;
    private List<MenuItemsModel> menuItemsModelList;
    private int resource;

    public MenuItemsAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);

        menuItemsModelList = objects;
        this.resource = resource;

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        ViewHolder viewHolder = null;


        if (convertView == null) {
            viewHolder = new MenuItemsAdapter.ViewHolder();
            convertView = inflater.inflate(resource, null);
            viewHolder.itemImage = convertView.findViewById(R.id.itemImage);
            viewHolder.itemName = convertView.findViewById(R.id.itemName);
            viewHolder.itemPrice = convertView.findViewById(R.id.itemPrice);
            viewHolder.countText = convertView.findViewById(R.id.count);
            viewHolder.plusBtn = convertView.findViewById(R.id.plus);
            viewHolder.minusBtn = convertView.findViewById(R.id.minus);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MenuItemsAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.itemName.setText(menuItemsModelList.get(position).getName());
        viewHolder.itemPrice.setText("Rs."+menuItemsModelList.get(position).getPrice());
        viewHolder.countText.setText(String.valueOf(CommonPos.getCount()));




        final ProgressBar progressBar = convertView.findViewById(R.id.progressBar);

        ImageLoader.getInstance().displayImage(menuItemsModelList.get(position).getImage(), viewHolder.itemImage, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.GONE);

            }
        });


        return convertView;
    }

   class ViewHolder {
        private ImageView itemImage;
        private TextView itemName;
        private TextView itemPrice;
        private TextView countText;
        private ImageView plusBtn, minusBtn;
    }
}
