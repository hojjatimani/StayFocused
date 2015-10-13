package com.aspire.controleZaman.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.aspire.controleZaman.R;
import com.aspire.controleZaman.myUtils.PersianReshape;
import com.aspire.controleZaman.myUtils.Utils;

/**
 * Created by Hojjat on 3/13/2015.
 */
public class ActivityBlock extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        TextView blockText = (TextView) findViewById(R.id.block_text);
        blockText.setTypeface(Utils.getAppFontBold(this));
        blockText.setText(PersianReshape.reshape(Utils.getBlockText(this)));
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
//        super.onBackPressed();
    }
}
