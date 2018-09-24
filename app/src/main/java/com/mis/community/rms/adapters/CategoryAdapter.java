package com.mis.community.rms.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mis.community.rms.R;
import com.mis.community.rms.model.CategoryModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter {

    public LayoutInflater inflater;
    private List<CategoryModel> categoryModelList;
    private int resource;

    public CategoryAdapter(@NonNull Context context, int resource, @NonNull List<CategoryModel> objects) {
        super(context, resource, objects);

        categoryModelList = objects;
        this.resource = resource;

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder = null;


        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(resource, null);
            viewHolder.cateImage = convertView.findViewById(R.id.cateImage);
            viewHolder.cateName = convertView.findViewById(R.id.cateName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        final ProgressBar progressBar = convertView.findViewById(R.id.progressBar);

        viewHolder.cateName.setText(categoryModelList.get(position).getName());

        ImageLoader.getInstance().displayImage(categoryModelList.get(position).getImage(), viewHolder.cateImage, new ImageLoadingListener() {
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
        private ImageView cateImage;
        private TextView cateName;
    }
}
