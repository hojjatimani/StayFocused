package com.aspire.controleZaman.main;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.SnackBar;
import com.aspire.controleZaman.R;
import com.aspire.controleZaman.myUtils.Constants;
import com.aspire.controleZaman.myUtils.PersianReshape;
import com.aspire.controleZaman.myUtils.Utils;

/**
 * Created by Hojjat on 3/21/2015.
 */
public class ActivitySettings extends ActionBarActivity implements View.OnClickListener {
    public static final String tag = "ActivitySettings";
    LinearLayout setupTime;
    LinearLayout blockText;
    LinearLayout resetTime;
    LinearLayout extend;
    LinearLayout aboutApp;
    LinearLayout contactUs;
    LinearLayout donate;
    LinearLayout rate;
    LinearLayout tellFriends;
    LinearLayout aboutUs;
    TextView switchStatus;
    SwitchCompat appSwitch;


    ImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initializeConstViews();
        initializeViews();
        setInitSettingsState();
        setListeners();
    }

    private void setInitSettingsState() {
        switchStatus.setTypeface(Utils.getAppFontRegular(this));
        switchStatus.setText(PersianReshape.reshape(Utils.isAppOn(this) ? "فعال" : "غیر فعال"));
        appSwitch.setChecked(Utils.isAppOn(this) ? true : false);
    }

    private void setListeners() {
        setupTime.setOnClickListener(this);
        blockText.setOnClickListener(this);
        resetTime.setOnClickListener(this);
        aboutApp.setOnClickListener(this);
        contactUs.setOnClickListener(this);
        donate.setOnClickListener(this);
        rate.setOnClickListener(this);
        tellFriends.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        back.setOnClickListener(this);
        appSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Utils.swithApp(ActivitySettings.this, true);
                    SnackBar message = new SnackBar(ActivitySettings.this, PersianReshape.reshape(Constants.APP_NAME + " فعال شد " + "\n"), null, null);
                    message.show();
//                    Toast.makeText(ActivitySettings.this, PersianReshape.reshape(Constants.APP_NAME + " فعال شد "), Toast.LENGTH_SHORT).show();
                    switchStatus.setText(PersianReshape.reshape("فعال"));
                } else {
                    if (Utils.isSetupTime(ActivitySettings.this)) {
                        Utils.swithApp(ActivitySettings.this, false);
                        SnackBar message = new SnackBar(ActivitySettings.this, PersianReshape.reshape(Constants.APP_NAME + " غیر فعال شد " + "\n"), null, null);
                        message.show();
//                        Toast.makeText(ActivitySettings.this, PersianReshape.reshape(Constants.APP_NAME + " غیر فعال شد "), Toast.LENGTH_SHORT).show();
                        switchStatus.setText(PersianReshape.reshape("غیر فعال"));
                    } else {
                        SnackBar message = new SnackBar(ActivitySettings.this, PersianReshape.reshape("غیر فعال کردن برنامه تنها در زمان های آزاد ایجاد تغییر ممکن است"), null, null);
                        message.show();
//                        Toast.makeText(ActivitySettings.this, PersianReshape.reshape("غیر فعال کردن برنامه تنها در زمان های آزاد ایجاد تغییر ممکن است"), Toast.LENGTH_SHORT).show();
                        appSwitch.setChecked(true);
                    }
                }
            }
        });
    }

    private void initializeConstViews() {
        Typeface regular = Utils.getAppFontRegular(this);
        Typeface bold = Utils.getAppFontBold(this);
        TextView title = (TextView) findViewById(R.id.activity_title);
        title.setText(PersianReshape.reshape("تنظیمات"));
        title.setTypeface(bold);
        TextView appName = (TextView) findViewById(R.id.app_name);
        appName.setText(PersianReshape.reshape(Constants.APP_NAME));
        appName.setTypeface(bold);
        appName.setText(PersianReshape.reshape(Constants.APP_NAME));
        TextView mainSettings = (TextView) findViewById(R.id.main_settings);
        mainSettings.setTypeface(bold);
        mainSettings.setText(PersianReshape.reshape("تنظیمات اصلی"));
        TextView setupTime = (TextView) findViewById(R.id.change_setup_time);
        setupTime.setTypeface(bold);
        setupTime.setText(PersianReshape.reshape("زمان آزاد ایجاد تغییرات"));
        TextView blockText = (TextView) findViewById(R.id.change_block_text);
        blockText.setTypeface(bold);
        blockText.setText(PersianReshape.reshape("متن اعلام اتمام وقت"));
        TextView resetTime = (TextView) findViewById(R.id.change_reset_time);
        resetTime.setTypeface(bold);
        resetTime.setText(PersianReshape.reshape("زمان ریست روزانه"));
        TextView about = (TextView) findViewById(R.id.about);
        about.setTypeface(bold);
        about.setText(PersianReshape.reshape("درباره"));
        TextView aboutApp = (TextView) findViewById(R.id.about_app);
        aboutApp.setTypeface(bold);
        aboutApp.setText(PersianReshape.reshape("راهنمای استفاده"));
        TextView contactUs = (TextView) findViewById(R.id.contact_us);
        contactUs.setTypeface(bold);
        contactUs.setText(PersianReshape.reshape("تماس با ما"));
        TextView support = (TextView) findViewById(R.id.support);
        support.setTypeface(bold);
        support.setText(PersianReshape.reshape("حمایت"));
        TextView donate = (TextView) findViewById(R.id.donate);
        donate.setTypeface(bold);
        donate.setText(PersianReshape.reshape("پرداخت هزینه برنامه (donate)"));
        TextView rate = (TextView) findViewById(R.id.rate_comment);
        rate.setTypeface(bold);
        rate.setText(PersianReshape.reshape("امتیاز دادن به برنامه"));
        TextView tellFriends = (TextView) findViewById(R.id.tell_friends);
        tellFriends.setTypeface(bold);
        tellFriends.setText(PersianReshape.reshape("معرفی به دیگران"));
        TextView aboutUs = (TextView) findViewById(R.id.about_us);
        aboutUs.setTypeface(bold);
        aboutUs.setText(PersianReshape.reshape("درباره ما"));
        TextView setupTimeDesc = (TextView) findViewById(R.id.setup_time_desc);
        setupTimeDesc.setTypeface(regular);
        if (Utils.isSetupTimeAlways(this)) {
            setupTimeDesc.setText(PersianReshape.reshape("با تعیین زمانی مشخص در شبانه روز برای ایجاد تغییرات، جلوی تقلب کردن را بگیرید."));
        } else {
            setupTimeDesc.setText(PersianReshape.reshape(Utils.getSetupTime_(this)));
        }
        TextView blockTextDesc = (TextView) findViewById(R.id.block_text_desc);
        blockTextDesc.setTypeface(regular);
        blockTextDesc.setText(PersianReshape.reshape("جمله ای که در صفحه بلاک برنامه ها نمایش داده می شود."));
        TextView resetTimeDesc = (TextView) findViewById(R.id.reset_time_desc);
        resetTimeDesc.setTypeface(regular);
        resetTimeDesc.setText(PersianReshape.reshape("23:59 (غیرقابل تغییر)"));
        TextView aboutAppDesc = (TextView) findViewById(R.id.about_app_desc);
        aboutAppDesc.setTypeface(regular);
        aboutAppDesc.setText(PersianReshape.reshape("این اپ چیست و چکار می کند؟"));
        TextView contactUsDesc = (TextView) findViewById(R.id.contact_us_desc);
        contactUsDesc.setTypeface(regular);
        contactUsDesc.setText(PersianReshape.reshape("مشکلی هست؟ سوالی دارید؟ خوشحال میشویم کمکی کنیم."));
        TextView donateDesc = (TextView) findViewById(R.id.donate_desc);
        donateDesc.setTypeface(regular);
        donateDesc.setText(PersianReshape.reshape("فروشی مبتنی بر اعتماد برای ایرانیان"));
        TextView rateDesc = (TextView) findViewById(R.id.rate_comment_desc);
        rateDesc.setTypeface(regular);
        rateDesc.setText(PersianReshape.reshape("بازخورد شما برای ما ارزشمند  است."));
        TextView tellFriendsDesc = (TextView) findViewById(R.id.tell_friends_desc);
        tellFriendsDesc.setTypeface(regular);
        tellFriendsDesc.setText(PersianReshape.reshape("برنامه مفیدی است؟ آن را به دوستانتان توصیه کنید!"));
    }

    private void initializeViews() {
        setupTime = (LinearLayout) findViewById(R.id.setup_time_);
        blockText = (LinearLayout) findViewById(R.id.block_text_);
        resetTime = (LinearLayout) findViewById(R.id.reset_time_);
        aboutApp = (LinearLayout) findViewById(R.id.about_app_);
        contactUs = (LinearLayout) findViewById(R.id.contact_us_);
        extend = (LinearLayout) findViewById(R.id.extend_possiblility);
                donate = (LinearLayout) findViewById(R.id.donate_);
        rate = (LinearLayout) findViewById(R.id.rate_);
        tellFriends = (LinearLayout) findViewById(R.id.tell_friends_);
        aboutUs = (LinearLayout) findViewById(R.id.about_us_);
        switchStatus = (TextView) findViewById(R.id.switch_status);
        appSwitch = (SwitchCompat) findViewById(R.id.app_switch);
        back = (ImageButton) findViewById(R.id.back);

        // hide donate
        donate.setVisibility(View.GONE);
        findViewById(R.id.line_after_donate).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == setupTime.getId()) {
            if(Utils.isSetupTime(this)) {
                startActivity(new Intent(ActivitySettings.this, ActivitySetupTime.class));
            }else{
                SnackBar message = new SnackBar(this, PersianReshape.reshape("در زمان آزاد ایجاد تغییرات نیستیم!" + "\n"), null, null);
                message.show();
            }
        } else if (id == blockText.getId()) {
            showChangeBlockTextDialog();
        } else if (id == aboutApp.getId()) {
            startActivity(new Intent(this, ActivityHowToUse.class));
        } else if (id == contactUs.getId()) {
            sendMail();
        } else if (id == donate.getId()) {
            startActivity(new Intent(this, ActivityDonate.class));
        } else if (id == rate.getId()) {
            rate();
        } else if (id == tellFriends.getId()) {
            tellFriends();
        } else if (id == aboutUs.getId()) {

        } else if (id == back.getId()) {
            onBackPressed();
        } else {
            Log.d(tag, "click event not handled!");
        }
    }

    private void showChangeBlockTextDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_block_text);
        dialog.setCancelable(false);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        final EditText editText = (EditText) dialog.findViewById(R.id.edit_text);
        ButtonFlat okBTN = (ButtonFlat) dialog.findViewById(R.id.ok);
        ButtonFlat cancelBTN = (ButtonFlat) dialog.findViewById(R.id.cancel);
        ButtonFlat setDefault = (ButtonFlat) dialog.findViewById(R.id.set_default);
        title.setText(PersianReshape.reshape("متن اعلام اتمام وقت"));
        editText.setText(PersianReshape.reshape(Utils.getBlockText(this)));
        okBTN.setText(PersianReshape.reshape("تایید"));
        cancelBTN.setText(PersianReshape.reshape("لغو"));
        setDefault.setText(PersianReshape.reshape("متن پیش فرض"));
        title.setTypeface(Utils.getAppFontBold(this));
        editText.setTypeface(Utils.getAppFontRegular(this));
        editText.getBackground().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setBlockText(ActivitySettings.this, editText.getText().toString());
                dialog.dismiss();
            }
        });
        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        setDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(PersianReshape.reshape(Constants.BLOCK_TEXT));
            }
        });
    }

    private void sendMail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.MAIL_ADDRESS});
        try {
            startActivity(Intent.createChooser(i, PersianReshape.reshape("ارسال از طریق")));
        } catch (android.content.ActivityNotFoundException ex) {
            final SnackBar snackBar = new SnackBar(this,PersianReshape.reshape( "هیچ برنامه ارسال ایمیلی یافت نشد. به این آدرس ایمیل بزنید: " )+ Constants.MAIL_ADDRESS, null, null);
            snackBar.show();
        }
    }

    private void rate() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_EDIT, Uri
                    .parse("bazaar://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse("http://cafebazaar.ir/app/?id="
                            + appPackageName)));
        }
    }

    private void tellFriends(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("sms:"));
        i.putExtra("sms_body", Constants.APP_NAME + " یه اپلیکیشن عالی برای مدیریت زمانهاییه که از برنامه های گوشیت استفاده میکنی. پیشنهاد میکنم نصبش کنی!"+"\n" + Constants.getDownloadAddress(this));
        try {
            startActivity(i);
        } catch (android.content.ActivityNotFoundException ex) {
            final SnackBar snackBar = new SnackBar(this, PersianReshape.reshape("خطا!" + "\n"), null, null);
            snackBar.show();
        }
    }

    @Override
    protected void onResume() {
        initializeConstViews();
        super.onResume();
    }
}