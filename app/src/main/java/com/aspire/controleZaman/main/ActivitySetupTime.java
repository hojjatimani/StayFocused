package com.aspire.controleZaman.main;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.SnackBar;
import com.aspire.controleZaman.R;
import com.aspire.controleZaman.myUtils.PersianReshape;
import com.aspire.controleZaman.myUtils.Utils;

/**
 * Created by Hojjat on 3/23/2015.
 */
public class ActivitySetupTime extends ActionBarActivity {
    ImageButton ok;
    ImageButton cancel;
    RadioButton always;
    RadioButton specificTime;
    TextView from;
    TextView to;
    LinearLayout setSpecificTime;

    Time setupTimeStart;
    Time setupTimeEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_time);
        intitializeConstViews();
        initializeViews();
        setListeners();
        setInitSettingsState();
    }

    private void setInitSettingsState() {
        if (Utils.isSetupTimeAlways(this)) {
            always.performClick();
        }else{
            specificTime.performClick();
        }
        Time[] setupTime = Utils.getSetupTime(this);
        setupTimeStart = setupTime[0];
        setupTimeEnd = setupTime[1];
        updateTimeTexts();
    }

    private void updateTimeTexts() {
        from.setText(String.format("%d:%02d", setupTimeStart.hour, setupTimeStart.minute));
        to.setText(String.format("%d:%02d", setupTimeEnd.hour, setupTimeEnd.minute));
    }

    private void setListeners() {
        always.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specificTime.setChecked(false);
                setSpecificTime.setVisibility(View.GONE);
            }
        });
        specificTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                always.setChecked(false);
                setSpecificTime.setVisibility(View.VISIBLE);
            }
        });

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromClicked();
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toClicked();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okClicked();
            }
        });
    }

    private void intitializeConstViews() {
        Typeface regular = Utils.getAppFontRegular(this);
        Typeface bold = Utils.getAppFontBold(this);
        TextView title = (TextView) findViewById(R.id.activity_title);
        title.setText(PersianReshape.reshape("زمان آزاد ایجاد تغییرات"));
        title.setTypeface(bold);
        TextView text = (TextView) findViewById(R.id.textView);
        text.setText(PersianReshape.reshape("با تعیین یک بازه (مثلا یک ساعته) به عنوان بازه زمانی آزاد ایجاد تغییرات، جلوی خود را زمانی که بعد از اتمام وقت وسوسه می شوید زمان را اضافه کرده یا برنامه را غیر فعال کنید، بگیرید."));
        text.setTypeface(regular);
        TextView from = (TextView) findViewById(R.id.from);
        from.setText(PersianReshape.reshape("از:"));
        from.setTypeface(regular);
        TextView to = (TextView) findViewById(R.id.to);
        to.setText(PersianReshape.reshape("تا:"));
        to.setTypeface(regular);
        final TextView always = (TextView) findViewById(R.id.always_text);
        always.setText(PersianReshape.reshape("همیشه"));
        always.setTypeface(regular);
        final TextView specificTime = (TextView) findViewById(R.id.specific_time_text);
        specificTime.setText(PersianReshape.reshape("بازه زمانی مشخص"));
        specificTime.setTypeface(regular);
        always.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySetupTime.this.always.performClick();
            }
        });
        specificTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySetupTime.this.specificTime.performClick();
            }
        });
    }

    private void initializeViews() {
        ok = (ImageButton) findViewById(R.id.ok);
        cancel = (ImageButton) findViewById(R.id.cancel);
        setSpecificTime = (LinearLayout) findViewById(R.id.set_specific_time);
        always = (RadioButton) findViewById(R.id.always);
        specificTime = (RadioButton) findViewById(R.id.specific_time);
        from = (TextView) findViewById(R.id.from_time);
        to = (TextView) findViewById(R.id.to_time);
    }

    private void okClicked() {
        if (always.isChecked()) {
            Utils.setSetupTimeAlways(this, true);
            onBackPressed();
        }else {
            int startTime = setupTimeStart.hour * 60 + setupTimeStart.minute;
            int endTime = setupTimeEnd.hour * 60 + setupTimeEnd.minute;
            if (Math.abs(endTime - startTime) < 10) {
                SnackBar message = new SnackBar(this, PersianReshape.reshape("اختلاف بین زمان شروع و پایان نمی تواند کمتر از 10 دقیقه باشد."), null, null);
                message.show();
            } else {
                Time[] setupTime = new Time[2];
                setupTime[0] = setupTimeStart;
                setupTime[1] = setupTimeEnd;
                Utils.setSetupTime(this, setupTime);
                onBackPressed();
            }
        }
    }

    private void fromClicked() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_time);
        dialog.setCancelable(false);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(setupTimeStart.hour);
        timePicker.setCurrentMinute(setupTimeStart.minute);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setVisibility(View.GONE);
        TextView minute = (TextView) dialog.findViewById(R.id.minute);
        TextView hour = (TextView) dialog.findViewById(R.id.hour);
        minute.setTypeface(Utils.getAppFontRegular(this));
        hour.setTypeface(Utils.getAppFontRegular(this));
        minute.setText(PersianReshape.reshape("دقیقه"));
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
                setupTimeStart.hour = timePicker.getCurrentHour();
                setupTimeStart.minute = timePicker.getCurrentMinute();
                updateTimeTexts();
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

    private void toClicked(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_time);
        dialog.setCancelable(false);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(setupTimeEnd.hour);
        timePicker.setCurrentMinute(setupTimeEnd.minute);
        TextView minute = (TextView) dialog.findViewById(R.id.minute);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setVisibility(View.GONE);
        TextView hour = (TextView) dialog.findViewById(R.id.hour);
        minute.setTypeface(Utils.getAppFontRegular(this));
        hour.setTypeface(Utils.getAppFontRegular(this));
        minute.setText(PersianReshape.reshape("دقیقه"));
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
                setupTimeEnd.hour = timePicker.getCurrentHour();
                setupTimeEnd.minute = timePicker.getCurrentMinute();
                updateTimeTexts();
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