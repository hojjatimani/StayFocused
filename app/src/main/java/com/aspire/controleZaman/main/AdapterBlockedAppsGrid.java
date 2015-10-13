package com.aspire.controleZaman.main;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspire.controleZaman.R;
import com.aspire.controleZaman.myUtils.Utils;

import java.util.ArrayList;

/**
 * Created by Hojjat on 3/18/2015.
 */
public class AdapterBlockedAppsGrid extends BaseAdapter{
    ArrayList<String> apps;
    Context context;
    String appsCategory;

    public AdapterBlockedAppsGrid(Context context, String appsCategory) {
        this.context = context;
        this.appsCategory = appsCategory;
        apps = new ArrayList<>();
    }

    public int setData() {
        apps = Utils.getBlockedApps(context, appsCategory);
        notifyDataSetChanged();
        return apps.size();
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_apps_grid_item, null);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            viewHolder.icon.setImageDrawable(context.getPackageManager().getApplicationIcon(apps.get(position)));
//            viewHolder.name.setText(context.getPackageManager().getApplicationLabel(context.getPackageManager().getApplicationInfo(apps.get(position), 0)));
            viewHolder.name.setText(context.getPackageManager().getApplicationInfo(apps.get(position), 0).loadLabel(context.getPackageManager()).toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            //TODO not handled
            //may be the app is uninstalled
        }
        return convertView;
    }

    class ViewHolder {
        ImageView icon;
        TextView name;
    }
}