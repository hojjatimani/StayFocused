package com.aspire.controleZaman.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aspire.controleZaman.R;
import com.aspire.controleZaman.myUtils.Constants;
import com.aspire.controleZaman.myUtils.PersianReshape;
import com.aspire.controleZaman.myUtils.Utils;

/**
 * Created by Hojjat on 3/24/2015.
 */
public class ActivityHowToUse extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);

        Typeface regular = Utils.getAppFontRegular(this);
        Typeface bold = Utils.getAppFontBold(this);
        TextView title = (TextView) findViewById(R.id.activity_title);
        title.setText(PersianReshape.reshape("راهنمای استفاده"));
        title.setTypeface(bold);

        TextView  text1= (TextView) findViewById(R.id.main_text_part1);
        text1.setText(PersianReshape.reshape("امروزه تکنولوژی چون شمشیری دولبه، درکنار خدماتی که به بشر می رساند مضرات بسیاری را نیز متوجه او می سازد که اغلب ریشه در عدم استفاده صحیح از آن دارد. شبکه های اجتماعی و پیام رسان ها که هدفشان نزدیک کردن انسان ها به یکدیگر بوده است در جهت عکس عمل کرده و با ایجاد انواع تنش های روانی و صرف زمان، گهرسان دارایی آدمی، نه تنها او را از نزدیکانش دور ساخته و فرصت درک بسیاری از لذات زندگی واقعی را با آراسته نشان دادن پوسته پوچ زندگی مجازی از او گرفته، که با کمرنگ کردن نظم زمانی در زندگی افراد، راه رسیدن به موفقیت و رؤیاها را نیز ناهموارتر کرده است. از این رو برآن شدیم تا با ایجاد این اپلیکیشن قدم کوچکی در راه احیای نظم زمانی در زندگی خواستارانش برداریم. در استفاده از این ابزار توصیه می شود:"));
        text1.setTypeface(regular);

        TextView  text2 = (TextView) findViewById(R.id.main_text_part2);
        text2.setText(PersianReshape.reshape("زمان استفاده از هر دسته را مقداری تعیین کنید که کمی چالش برانگیز باشد، از تنظیم زمانهایی که رعایت کردنشان برایتان خیلی ساده یا خیلی سخت است بپرهیزید. "));
        text2.setTypeface(bold);

        TextView  text3= (TextView) findViewById(R.id.main_text_part3);
        text3.setText(PersianReshape.reshape("و در آخر فراموش نکنید هیچ تغییری ناگهانی ایجاد نمی شود، برای تغییر عادت های مخرب در استفاده از تکنولوژی باید زمان و اراده ی زیادی صرف کنید."));
        text3.setTypeface(regular);

        TextView  sing= (TextView) findViewById(R.id.sign);
        sing.setText(PersianReshape.reshape("با آرزوی موفقیت\n" + Constants.DEVELOP_TEAM));
        sing.setTypeface(regular);


        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
