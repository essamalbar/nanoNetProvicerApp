package com.crazyiter.nanonetprovicerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListPopupWindowAdapter extends BaseAdapter {
    LayoutInflater mLayoutInflater;
    List<CustomMenuItem> mItemList;

    public ListPopupWindowAdapter(Context context, List<CustomMenuItem> itemList) {
        mLayoutInflater = LayoutInflater.from(context);
        mItemList = itemList;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public CustomMenuItem getItem(int i) {
        return mItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_menu, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(getItem(position).getTitle());
        holder.ivImage.setImageResource(getItem(position).getImageRes());
        holder.ivImage.setColorFilter(convertView.getContext().getResources().getColor(getItem(position).getColorTint()));

        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;
        ImageView ivImage;

        ViewHolder(View view) {
            tvTitle = view.findViewById(R.id.menuItemIconTV);
            ivImage = view.findViewById(R.id.menuItemIconIV);
        }
    }
}