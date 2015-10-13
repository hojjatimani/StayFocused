package com.aspire.controleZaman.main;

import android.app.ProgressDialog;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aspire.controleZaman.R;
import com.aspire.controleZaman.myUtils.PersianReshape;
import com.aspire.controleZaman.myUtils.Utils;

import java.util.ArrayList;

/**
 * Created by Hojjat on 3/14/2015.
 */
public class ActivityAddApp extends ActionBarActivity {
    String tag = "ActivityAddApp";
    Toolbar toolbar;
    ArrayList<ResolveInfo> appsInfo;
    RecyclerView appsList;
    AdapterAppsList listAdapter;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_app);
        initialConstViews();
        initViews();
        prepareToolbar();
//        setData();
        new SetData().execute();
        setListeners();
        Log.d(tag, "ActivityAddApp on create");
    }

    private void initialConstViews() {
        Typeface bold = Utils.getAppFontBold(this);
        TextView title = (TextView) findViewById(R.id.activity_title);
        title.setText(PersianReshape.reshape("اضافه کردن برنامه"));
        title.setTypeface(bold);
    }

    private void setData() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        appsList.setLayoutManager(mLayoutManager);
        ArrayList<PkgInfo> installedApps = new InstalledPackagesInfo(this).getInstalledApps();
        listAdapter = new AdapterAppsList(installedApps, this);
    }

    private void setListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void prepareToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
    }

    private void initViews() {
        appsList = (RecyclerView) findViewById(R.id.app_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        back = (ImageButton)findViewById(R.id.back);
    }

    class SetData extends AsyncTask{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ActivityAddApp.this);
            progressDialog.setMessage(PersianReshape.reshape("کمی صبر کنید"));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            setData();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    appsList.setAdapter(listAdapter);
                }
            });
            return  null;
        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
        }
    }
}