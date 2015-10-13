package com.aspire.controleZaman.main;

/**
 * Created by Hojjat on 3/15/2015.
 */

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.aspire.controleZaman.R;
import com.aspire.controleZaman.myUtils.Constants;
import com.aspire.controleZaman.myUtils.PersianReshape;
import com.aspire.controleZaman.myUtils.Utils;

import java.util.ArrayList;

public class AdapterAppsList extends RecyclerView.Adapter<AdapterAppsList.AppInfoViewHolder> {
    String tag = "AdapterAppsList";
    Context context;
    ArrayList<PkgInfo> installedApps;
    ArrayList<String> toBeBlocked;

    public AdapterAppsList(ArrayList<PkgInfo> installedApps, Context context) {
        this.installedApps = installedApps;
        this.context = context;
        toBeBlocked = new ArrayList<>();
    }

    @Override
    public AppInfoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_app_list_item, null);

        return new AppInfoViewHolder(view, new ItemClickListener() {
            @Override
            public void onClick(View clickedView, final int position) {
                final PkgInfo clickedApp = installedApps.get(position);
                Log.d(tag , "clicked on : " + clickedApp.packageName);
                if (!clickedApp.isBlockedByapp) {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_choose_category);
                    dialog.setCancelable(false);
                    final RadioButton option1 = (RadioButton) dialog.findViewById(R.id.option1);
                    final RadioButton option2 = (RadioButton) dialog.findViewById(R.id.option2);
                    final RadioButton option3 = (RadioButton) dialog.findViewById(R.id.option3);
                    TextView title = (TextView) dialog.findViewById(R.id.title);
                    TextView option1Text = (TextView) dialog.findViewById(R.id.option1_text);
                    TextView option2Text = (TextView) dialog.findViewById(R.id.option2_text);
                    TextView option3Text = (TextView) dialog.findViewById(R.id.option3_text);
                    ButtonFlat okBTN = (ButtonFlat) dialog.findViewById(R.id.ok);
                    ButtonFlat cancelBTN = (ButtonFlat) dialog.findViewById(R.id.cancel);
                    option1Text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            option1.performClick();
                        }
                    });
                    option2Text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            option2.performClick();
                        }
                    });
                    option3Text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            option3.performClick();
                        }
                    });
                    option1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            option2.setChecked(false);
                            option3.setChecked(false);
                        }
                    });
                    option2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            option1.setChecked(false);
                            option3.setChecked(false);
                        }
                    });
                    option3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            option1.setChecked(false);
                            option2.setChecked(false);
                        }
                    });
                    if (Utils.getCategoryOfApp(clickedApp.packageName).equals(Constants.SOCIAL_MEDIAS)) {
                        option1.setChecked(true);
                    } else if (Utils.getCategoryOfApp(clickedApp.packageName).equals(Constants.GAMES)) {
                        option2.setChecked(true);
                    } else {
                        option3.setChecked(true);
                    }
                    option1Text.setText(PersianReshape.reshape("شبکه های اجتماعی و پیامرسان ها"));
                    option2Text.setText(PersianReshape.reshape("بازی ها"));
                    option3Text.setText(PersianReshape.reshape("دیگر"));
                    title.setText(PersianReshape.reshape("در کدام دسته قرار می گیرد؟"));
                    okBTN.setText(PersianReshape.reshape("ادامه"));
                    cancelBTN.setText(PersianReshape.reshape("لغو"));
                    title.setTypeface(Utils.getAppFontBold(context));
                    option1Text.setTypeface(Utils.getAppFontRegular(context));
                    option2Text.setTypeface(Utils.getAppFontRegular(context));
                    option3Text.setTypeface(Utils.getAppFontRegular(context));
                    dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
                    dialog.show();
                    okBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.blockApp(context, clickedApp.packageName, option1.isChecked() ? Constants.SOCIAL_MEDIAS : (option2.isChecked() ? Constants.GAMES : Constants.OTHERS));
                            Utils.restartService(context);
                            clickedApp.isBlockedByapp = true;
                            notifyItemChanged(position);
                            dialog.dismiss();
                        }
                    });
                    cancelBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(AppInfoViewHolder appInfoViewHolder, int i) {
        PkgInfo info = installedApps.get(i);
        appInfoViewHolder.name.setText(PersianReshape.reshape(info.appName));
        appInfoViewHolder.icon.setImageDrawable(info.icon);
        if (installedApps.get(i).isBlockedByapp) {
            appInfoViewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
            appInfoViewHolder.itemView.findViewById(R.id.is_blocked).setVisibility(View.VISIBLE);
        } else {
            appInfoViewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.gray_light));
            appInfoViewHolder.itemView.findViewById(R.id.is_blocked).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return (null != installedApps ? installedApps.size() : 0);
    }

    class AppInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemView;
        ImageView icon;
        TextView name;
        ItemClickListener clickListener;

        public AppInfoViewHolder(View itemView, ItemClickListener clickListener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.app_name);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            this.clickListener = clickListener;
            this.itemView = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(itemView, this.getPosition());
        }
    }

    static interface ItemClickListener {
        public void onClick(View clickedView, int position);
    }
}