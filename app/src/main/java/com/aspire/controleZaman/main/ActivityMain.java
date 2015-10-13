package com.aspire.controleZaman.main;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.farsitel.bazaar.IUpdateCheckService;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.SnackBar;
import com.aspire.controleZaman.R;
import com.aspire.controleZaman.myUtils.Constants;
import com.aspire.controleZaman.myUtils.PersianReshape;
import com.aspire.controleZaman.myUtils.Utils;


public class ActivityMain extends ActionBarActivity {
    String tag = "ActivityMain";

    Toolbar toolbar;
    ImageButton add;
    ImageButton settings;

    ImageButton setTimeOutSocials;
    ImageButton setTimeOutGames;
    ImageButton setTimeOutOthers;

    ExpandableHeightGridView socialAppsGrid;
    ExpandableHeightGridView gamesGrid;
    ExpandableHeightGridView othersGrid;
    AdapterBlockedAppsGrid socialAppsGridAdapter;
    AdapterBlockedAppsGrid gamesGridAdapter;
    AdapterBlockedAppsGrid othersGridAdapter;

    TextView socialRemainedTime;
    TextView gamesRemainedTime;
    TextView othersRemainedTime;

    ProgressBar socialsTimeProgress;
    ProgressBar gamesTimeProgress;
    ProgressBar othersTimeProgress;

    TextView socialsSpentTimePercent;
    TextView gamesSpentTimePercent;
    TextView othersSpentTimePercent;



    TextView noAppLableSocials;
    TextView noAppLableGames;
    TextView noAppLableOthers;

    IUpdateCheckService serviceCheckUpdate;
    UpdateServiceConnection connectionCheckUpdate;
    private static final String tagUpdate = "ActivityMainUpdateCheck";
    private boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Utils.clearAllAppData(this);
        //stop the service if running
//        stopService(new Intent(this, ShowView.class));
        initViews();
        setConstantUiElements();
        prepareToolbar();
        setListeners();
        setData();
        if(Utils.isAppOn(this)) {
            startService(new Intent(this, ServiceMain.class));
        }
        startService(new Intent(this, ServiceController.class));
        initChekForUpdateService();
    }

    private void setConstantUiElements() {
        Typeface bold = Utils.getAppFontBold(this);
        Typeface regular = Utils.getAppFontRegular(this);
        TextView appName = (TextView) findViewById(R.id.app_name);
        appName.setText(PersianReshape.reshape(Constants.APP_NAME));
        appName.setTypeface(bold);
        TextView socialLable = (TextView) findViewById(R.id.card1_title);
        socialLable.setText(PersianReshape.reshape("شبکه های اجتماعی و پیام رسان ها"));
        socialLable.setTypeface(bold);
        TextView gamesLable = (TextView) findViewById(R.id.card2_title);
        gamesLable.setText(PersianReshape.reshape("بازی ها"));
        gamesLable.setTypeface(bold);
        TextView othersLable = (TextView) findViewById(R.id.card3_title);
        othersLable.setText(PersianReshape.reshape("دیگر برنامه ها"));
        othersLable.setTypeface(bold);

        noAppLableOthers.setText(PersianReshape.reshape("برنامه ای اضافه نشده"));
        noAppLableGames.setText(PersianReshape.reshape("برنامه ای اضافه نشده"));
        noAppLableSocials.setText(PersianReshape.reshape("برنامه ای اضافه نشده"));
        noAppLableOthers.setTypeface(regular);
        noAppLableSocials.setTypeface(regular);
        noAppLableGames.setTypeface(regular);

        socialRemainedTime.setTypeface(regular);
        gamesRemainedTime.setTypeface(regular);
        othersRemainedTime.setTypeface(regular);
    }

    private void setData() {
        socialAppsGridAdapter = new AdapterBlockedAppsGrid(this, Constants.SOCIAL_MEDIAS);
        socialAppsGrid.setAdapter(socialAppsGridAdapter);
        gamesGridAdapter = new AdapterBlockedAppsGrid(this, Constants.GAMES);
        gamesGrid.setAdapter(gamesGridAdapter);
        othersGridAdapter = new AdapterBlockedAppsGrid(this, Constants.OTHERS);
        othersGrid.setAdapter(othersGridAdapter);
        updateAppsState();
    }

    private void setListeners() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, ActivityAddApp.class);
//                startActivityForResult(intent, Constants.REQUEST_CODE_ACTIVITY_ADD_APP);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, ActivitySettings.class);
                startActivity(intent);
            }
        });

        socialAppsGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (Utils.isSetupTime(ActivityMain.this)) {
                    showAlertUnblockApp(Constants.SOCIAL_MEDIAS, (String) socialAppsGridAdapter.getItem(position));
                } else {
                    SnackBar message = new SnackBar(ActivityMain.this,PersianReshape.reshape( "تنها در زمان آزاد ایجاد تغییرات می توانید برنامه ای را از لیست تحت نظرها حذف کنید."  ), null, null);
                    message.show();
                }
                return true;
            }
        });

        gamesGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (Utils.isSetupTime(ActivityMain.this)) {
                    showAlertUnblockApp(Constants.GAMES, (String) gamesGridAdapter.getItem(position));
                } else {
                    SnackBar message = new SnackBar(ActivityMain.this,PersianReshape.reshape( "تنها در زمان آزاد ایجاد تغییرات می توانید برنامه ای را از لیست تحت نظرها حذف کنید."), null, null);
                    message.show();
                }
                return true;
            }
        });

        othersGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (Utils.isSetupTime(ActivityMain.this)) {
                    showAlertUnblockApp(Constants.OTHERS, (String) othersGridAdapter.getItem(position));
                } else {
                    SnackBar message = new SnackBar(ActivityMain.this,PersianReshape.reshape( "تنها در زمان آزاد ایجاد تغییرات می توانید برنامه ای را از لیست تحت نظرها حذف کنید."), null, null);
                    message.show();
                }
                return true;
            }
        });

        setTimeOutSocials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChangeTimeOut(Constants.SOCIAL_MEDIAS);
            }
        });

        setTimeOutGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChangeTimeOut(Constants.GAMES);
            }
        });

        setTimeOutOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChangeTimeOut(Constants.OTHERS);
            }
        });
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        add = (ImageButton) findViewById(R.id.add);
        settings = (ImageButton) findViewById(R.id.settings);
        socialAppsGrid = (ExpandableHeightGridView) findViewById(R.id.apps_grid_1);
        gamesGrid = (ExpandableHeightGridView) findViewById(R.id.apps_grid_2);
        othersGrid = (ExpandableHeightGridView) findViewById(R.id.apps_grid_3);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final float screenWidth = metrics.widthPixels / metrics.density;
        //getDimension retrurns the pixel converted value!
        int bestColNum = (int) (metrics.widthPixels / getResources().getDimension(R.dimen.apps_icon_size) * 0.65);
        socialAppsGrid.setNumColumns(bestColNum);
        gamesGrid.setNumColumns(bestColNum);
        othersGrid.setNumColumns(bestColNum);

        noAppLableSocials = (TextView) findViewById(R.id.card1_no_app);
        noAppLableGames = (TextView) findViewById(R.id.card2_no_app);
        noAppLableOthers = (TextView) findViewById(R.id.card3_no_app);

        socialRemainedTime = (TextView) findViewById(R.id.card1_remained_time);
        gamesRemainedTime = (TextView) findViewById(R.id.card2_remained_time);
        othersRemainedTime = (TextView) findViewById(R.id.card3_remained_time);

        socialsTimeProgress = (ProgressBar) findViewById(R.id.socials_time_progress);
        gamesTimeProgress = (ProgressBar) findViewById(R.id.games_time_progress);
        othersTimeProgress = (ProgressBar) findViewById(R.id.others_time_progress);

        socialsSpentTimePercent = (TextView) findViewById(R.id.socials_spent_time_percent);
        gamesSpentTimePercent = (TextView) findViewById(R.id.games_spent_time_percent);
        othersSpentTimePercent = (TextView) findViewById(R.id.others_spent_time_percent);

        setTimeOutSocials = (ImageButton) findViewById(R.id.card1_set_time);
        setTimeOutGames = (ImageButton) findViewById(R.id.card2_set_time);
        setTimeOutOthers = (ImageButton) findViewById(R.id.card3_set_time);
    }

    private void prepareToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO not handled
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!Utils.userHasPurchased(this)){
            showAlertExit();
        }else {
            super.onBackPressed();
        }
    }

    private void exit(){
        if(exit == true){
            super.onBackPressed();
        }else{
            exit = true;
            Toast.makeText(this, "برای خروج کلید بازگشت را دوباره فشار دهید", Toast.LENGTH_SHORT);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    exit = false;
                }
            });
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.isAppOn(this)) {
            startService(new Intent(this, ServiceMain.class));
        }
        startService(new Intent(this, ServiceController.class));
        updateAppsState();
    }

    private void updateAppsState() {
        if (socialAppsGridAdapter.setData() == 0) {
            noAppLableSocials.setVisibility(View.VISIBLE);
        } else {
            noAppLableSocials.setVisibility(View.GONE);
        }

        if (gamesGridAdapter.setData() == 0) {
            noAppLableGames.setVisibility(View.VISIBLE);
        } else {
            noAppLableGames.setVisibility(View.GONE);
        }

        if (othersGridAdapter.setData() == 0) {
            noAppLableOthers.setVisibility(View.VISIBLE);
        } else {
            noAppLableOthers.setVisibility(View.GONE);
        }
        updateRemainedTimes();
    }

    private void updateRemainedTimes(){
        socialRemainedTime.setText(Utils.getRemainedTime(Constants.SOCIAL_MEDIAS, this) + " باقیمانده از " + Utils.getTimeOut(Constants.SOCIAL_MEDIAS, this));
        gamesRemainedTime.setText(Utils.getRemainedTime(Constants.GAMES, this) + " باقیمانده از " + Utils.getTimeOut(Constants.GAMES, this));
        othersRemainedTime.setText(Utils.getRemainedTime(Constants.OTHERS, this) + " باقیمانده از " + Utils.getTimeOut(Constants.OTHERS, this));
        socialsTimeProgress.setProgress(Utils.getSpentTimePercent(Constants.SOCIAL_MEDIAS, this));
        gamesTimeProgress.setProgress(Utils.getSpentTimePercent(Constants.GAMES , this));
        othersTimeProgress.setProgress(Utils.getSpentTimePercent(Constants.OTHERS, this));
        socialsSpentTimePercent.setText("%" + Utils.getSpentTimePercent(Constants.SOCIAL_MEDIAS, this));
        gamesSpentTimePercent.setText("%" + Utils.getSpentTimePercent(Constants.GAMES, this));
        othersSpentTimePercent.setText("%" + Utils.getSpentTimePercent(Constants.OTHERS, this));
//        socialRemainedTime.setText("زمان باقی مانده   " + Utils.getRemainedTime(Constants.SOCIAL_MEDIAS, this));
//        gamesRemainedTime.setText("زمان باقی مانده   " + Utils.getRemainedTime(Constants.GAMES, this));
//        othersRemainedTime.setText("زمان باقی مانده   " + Utils.getRemainedTime(Constants.OTHERS, this));
    }



    private void showAlertUnblockApp(final String groupTag, final String pckName) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_alert);
        dialog.setCancelable(false);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView message = (TextView) dialog.findViewById(R.id.message);
        ButtonFlat okBTN = (ButtonFlat) dialog.findViewById(R.id.ok);
        ButtonFlat cancelBTN = (ButtonFlat) dialog.findViewById(R.id.cancel);
        dialog.findViewById(R.id.dont_show_again_text).setVisibility(View.GONE);
        dialog.findViewById(R.id.dont_show_again).setVisibility(View.GONE);
        title.setText(PersianReshape.reshape("حذف از تحت نظر ها؟"));
        message.setText(PersianReshape.reshape("با حذف برنامه از لیست تحت نظرها استفاده از آن آزاد بدون زمانبندی خواهد شد. (می توانید هرموقع که خواستید آن را دوباره اضافه کنید.)"));
        okBTN.setText(PersianReshape.reshape("حذف"));
        cancelBTN.setText(PersianReshape.reshape("لغو"));
        title.setTypeface(Utils.getAppFontBold(this));
        message.setTypeface(Utils.getAppFontRegular(this));
        dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.unblockApp(ActivityMain.this, pckName, groupTag);
                updateAppsState();
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

    private void showDialogChangeTimeOut(final String groupTag) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_time);
        dialog.setCancelable(false);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        timePicker.setCurrentHour((Utils.getTimeOutMillis(this, groupTag) / (1000 * 60 * 60)) % 24);
        timePicker.setCurrentMinute((Utils.getTimeOutMillis(this, groupTag) / (1000 * 60)) % 60);
        TextView minute = (TextView) dialog.findViewById(R.id.minute);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView hour = (TextView) dialog.findViewById(R.id.hour);
        minute.setTypeface(Utils.getAppFontRegular(this));
        hour.setTypeface(Utils.getAppFontRegular(this));
        title.setTypeface(Utils.getAppFontRegular(this));
        minute.setText(PersianReshape.reshape("دقیقه"));
        title.setText("حداکثر زمان استفاده از برنامه های این دسته");
        hour.setText(PersianReshape.reshape("ساعت"));
        ButtonFlat okBTN = (ButtonFlat) dialog.findViewById(R.id.ok);
        ButtonFlat cancelBTN = (ButtonFlat) dialog.findViewById(R.id.cancel);
        okBTN.setText(PersianReshape.reshape("تایید"));
        cancelBTN.setText(PersianReshape.reshape("لغو"));
        dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newTimeOutMillis = (timePicker.getCurrentHour() * 60 + timePicker.getCurrentMinute()) * 60 * 1000;
                if(newTimeOutMillis > Utils.getTimeOutMillis(ActivityMain.this, groupTag) && !Utils.isSetupTime(ActivityMain.this)){
                    SnackBar message = new SnackBar(ActivityMain.this,PersianReshape.reshape( "تنها در زمان های آزاد ایجاد تغییرات می توانید زمان را افزایش دهید."), null, null);
                    message.show();
                }else {
                    Utils.setTimeOutMillis(ActivityMain.this, groupTag, newTimeOutMillis);
                    updateRemainedTimes();
                    dialog.dismiss();
                }
            }
        });
        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showAlertExit() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView message = (TextView) dialog.findViewById(R.id.message);
        ButtonFlat okBTN = (ButtonFlat) dialog.findViewById(R.id.ok);
        ButtonFlat cancelBTN = (ButtonFlat) dialog.findViewById(R.id.cancel);
        ButtonFlat rateBtn = (ButtonFlat) dialog.findViewById(R.id.rate);
        ImageButton dismiss = (ImageButton) dialog.findViewById(R.id.dismiss);
        title.setText(PersianReshape.reshape("فروش بر مبنای اعتماد"));
        message.setText(PersianReshape.reshape("ما فروش این برنامه را بر مبنای اعتماد به کاربران گذاشتیم، تا شما تنها درصورت رضایت از برنامه هزینه کنید، چناچه از این برنامه استفاده می کنید و آن را مفید می دانید مبلغ کمی بابت استفاده از آن بپردازید"));
        okBTN.setText(PersianReshape.reshape("پرداخت هزینه استفاده از برنامه"));
        rateBtn.setText(PersianReshape.reshape("امتیاز دادن به برنامه"));
        cancelBTN.setText(PersianReshape.reshape("الان نه، خروج"));
        title.setTypeface(Utils.getAppFontBold(this));
        message.setTypeface(Utils.getAppFontRegular(this));
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityMain.this, ActivityDonate.class));
                dialog.dismiss();
            }
        });
        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_EDIT, Uri
                            .parse("bazaar://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse("http://cafebazaar.ir/app/?id="
                                    + appPackageName)));
                }
                dialog.dismiss();
                ActivityMain.super.onBackPressed();
            }
        });
        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ActivityMain.super.onBackPressed();
            }
        });
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }



    private void initChekForUpdateService() {
        Log.i(tagUpdate, "initService()");
        connectionCheckUpdate = new UpdateServiceConnection();
        Intent i = new Intent(
                "com.farsitel.bazaar.service.UpdateCheckService.BIND");
        i.setPackage("com.farsitel.bazaar");
        boolean ret = bindService(i, connectionCheckUpdate, this.BIND_AUTO_CREATE);
        Log.e(tagUpdate, "initService() bound value: " + ret);
    }

    /** This is our function to un-binds this activity from our service. */
    private void releaseService() {
        unbindService(connectionCheckUpdate);
        connectionCheckUpdate = null;
        Log.d(tagUpdate, "releaseService(): unbound.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseService();
    }

    private class UpdateServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder boundService) {
            serviceCheckUpdate = IUpdateCheckService.Stub
                    .asInterface((IBinder) boundService);
            try {
                long vCode = serviceCheckUpdate.getVersionCode("your.app.packagename");
                Log.d(tagUpdate, "Version Code:" + vCode);
                if(vCode == -1){
                    //no update available
                    Log.d(tagUpdate, "no update available");
                }else{
                    if(Utils.isVersionIgnored(ActivityMain.this, vCode)){
                     //version is ignored
                    }else {
                        showUpdateDialog(vCode);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(tagUpdate, "could not get version code!");
            }
            Log.e(tagUpdate, "onServiceConnected(): Connected");
        }

        public void onServiceDisconnected(ComponentName name) {
            serviceCheckUpdate = null;
            Log.e(tagUpdate, "onServiceDisconnected(): Disconnected");
        }
    }

    private void showUpdateDialog(final Long vCode){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_alert);
        dialog.setCancelable(false);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView message = (TextView) dialog.findViewById(R.id.message);
        ButtonFlat okBTN = (ButtonFlat) dialog.findViewById(R.id.ok);
        ButtonFlat cancelBTN = (ButtonFlat) dialog.findViewById(R.id.cancel);
        final CheckBox dontShowAgain = (CheckBox) dialog.findViewById(R.id.dont_show_again);
        TextView dontShowAgainText = (TextView) dialog.findViewById(R.id.dont_show_again_text);
        title.setText(PersianReshape.reshape("بروزرسانی"));
        message.setText(PersianReshape.reshape("نسخه " + vCode + " آماده دانلود است!" + "\n"+ "برای تجربه بهتر پیشنهاد می کنیم همواره برنامه را بروز نگه دارید. مایل به بروزرسانی هستید؟"));
        okBTN.setText(PersianReshape.reshape("بروزرسانی"));
        cancelBTN.setText(PersianReshape.reshape("الان نه"));
        dontShowAgainText.setText(PersianReshape.reshape("بیخیال این نسخه"));
        dontShowAgainText.setTypeface(Utils.getAppFontRegular(ActivityMain.this));
        title.setTypeface(Utils.getAppFontBold(this));
        message.setTypeface(Utils.getAppFontRegular(this));
        dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dontShowAgain.isChecked()){
                    Utils.ignoreVersionForUpdate(ActivityMain.this, vCode);
                }
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse("bazaar://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    Log.d(tagUpdate, "problem loading app page in bazar!");
                }
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