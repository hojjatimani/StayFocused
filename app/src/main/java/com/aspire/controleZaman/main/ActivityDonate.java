package com.aspire.controleZaman.main;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.aspire.controleZaman.R;
import com.aspire.controleZaman.myUtils.Constants;
import com.aspire.controleZaman.myUtils.PersianReshape;
import com.aspire.controleZaman.myUtils.Utils;
import com.aspire.controleZaman.util.IabHelper;
import com.aspire.controleZaman.util.IabResult;
import com.aspire.controleZaman.util.Inventory;
import com.aspire.controleZaman.util.Purchase;
import com.gc.materialdesign.widgets.SnackBar;

/**
 * Created by Hojjat on 4/11/2015.
 */
public class ActivityDonate extends ActionBarActivity {

    // Debug tag, for logging
    static final String TAG = "ActivityDonate";

    // SKUs for our products: the premium upgrade (non-consumable)
    static final String SKU_PREMIUM_PRICE1 = "PAY1000";
    static final String SKU_PREMIUM_PRICE2 = "PAY2000";
    static final String SKU_PREMIUM_PRICE3 = "PAY5000";

    // Does the user have the premium upgrade?
    boolean mIsPremiumPaidPrice1 = false;
    boolean mIsPremiumPaidPrice2 = false;
    boolean mIsPremiumPaidPrice3 = false;

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST_PAY_PRICE1 = Constants.DONATE_PRICE1;
    static final int RC_REQUEST_PAY_PRICE2 = Constants.DONATE_PRICE2;
    static final int RC_REQUEST_PAY_PRICE3 = Constants.DONATE_PRICE3;

    // The helper object
    IabHelper mHelper;


    TextView status;
    TextView error;
    ButtonFlat pay_price1;
    ButtonFlat pay_price2;
    ButtonFlat pay_price3;
    ProgressBar progressBar;
    ImageButton refresh;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        Typeface regular = Utils.getAppFontRegular(this);
        Typeface bold = Utils.getAppFontBold(this);
        TextView title = (TextView) findViewById(R.id.activity_title);
        title.setText(PersianReshape.reshape("فروش بر مبنای اعتماد"));
        title.setTypeface(bold);

        TextView explanation = (TextView) findViewById(R.id.explanation);
        explanation.setText(PersianReshape.reshape("ما فروش این برنامه را بر مبنای اعتماد به کاربران گذاشتیم، تا شما تنها درصورت رضایت از برنامه هزینه کنید، چناچه از این برنامه استفاده می کنید و آن را مفید می دانید مبلغ کمی بابت استفاده از آن بپردازید"));
        explanation.setTypeface(regular);

        status = (TextView) findViewById(R.id.status);
        status.setTypeface(bold);

        error = (TextView) findViewById(R.id.error);
        error.setText(PersianReshape.reshape("- اتصال به اینترنت برقرار نیست" + "\n" + "- کافه بازار روی دستگاه نصب نیست" + "\n" + "- وارد حساب کاربریتان در کافه بازار نشده اید"));
        error.setTextColor(Color.RED);
        error.setTypeface(regular);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        pay_price1 = (ButtonFlat) findViewById(R.id.pay_price1);
        pay_price2 = (ButtonFlat) findViewById(R.id.pay_price2);
        pay_price3 = (ButtonFlat) findViewById(R.id.pay_price3);
        pay_price1.setText(PersianReshape.reshape("پرداخت " + Constants.DONATE_PRICE1 + " تومان"));
        pay_price2.setText(PersianReshape.reshape("پرداخت " + Constants.DONATE_PRICE2 + " تومان"));
        pay_price3.setText(PersianReshape.reshape("پرداخت " + Constants.DONATE_PRICE3 + " تومان"));

        refresh = (ImageButton) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChecking();
            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pay_price1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPaymentPrice1();
            }
        });
        pay_price2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPaymentPrice2();
            }
        });
        pay_price3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPaymentPrice3();
            }
        });
        startChecking();
    }

    private void startChecking() {
        setStatus(4);
        if (mHelper != null) mHelper.dispose();
        mHelper = null;

        String base64EncodedPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwChsa0hyzoY+6bXb0cCD8aTTez5HSxxiKfqdtdMYpWRgml3O3/F1Ar2t9Jmc3av1hHu/H13WXMx0hthTK6fwosztJOJfzcqHlWmhCXtjnmBiHVR5uv9uHwkHbzLEDSBBk6vcE9zUXETC3hucKWt5sZiSI0vewXU9jjzhhWNEIf+5PR+9rQ/U+iSuiv+eHuYsqNe/xbo7qFQqG8iKyAwOGpaAO7ZLsivX2AlG/NGlycCAwEAAQ==";
        // You can find it in your Bazaar console, in the Dealers section.
        // It is recommended to add more security than just pasting it in your source code;
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (mHelper == null) return;
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    setStatus(0);
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                    return;
                }

                // Hooray, IAB is fully set up!
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (mHelper == null) return;

            Log.d(TAG, "Query inventory finished.");


            if (result.isFailure()) {
                //there is an error
                setStatus(0);
                Log.d(TAG, "Failed to query inventory: " + result);
                return;
            } else {
                Log.d(TAG, "Query inventory was successful.");
                // does the user have the premium upgrade?
                mIsPremiumPaidPrice1 = inventory.hasPurchase(SKU_PREMIUM_PRICE1);
                mIsPremiumPaidPrice2 = inventory.hasPurchase(SKU_PREMIUM_PRICE2);
                mIsPremiumPaidPrice3 = inventory.hasPurchase(SKU_PREMIUM_PRICE3);

                if (mIsPremiumPaidPrice1 || mIsPremiumPaidPrice2 || mIsPremiumPaidPrice3) {
                    Utils.userPurchased(ActivityDonate.this);
                }
                // update UI accordingly
                if (mIsPremiumPaidPrice1) {
                    mHelper.consumeAsync(inventory.getPurchase(SKU_PREMIUM_PRICE1), new IabHelper.OnConsumeFinishedListener() {
                        @Override
                        public void onConsumeFinished(Purchase purchase, IabResult result) {
                            if (result.isSuccess()) {
//                                Toast.makeText(ActivityDonate.this, "ptice1 consumed", Toast.LENGTH_SHORT).show();
                                setStatus(3);
                            } else {
                                Toast.makeText(ActivityDonate.this, "ptice1 consumtion failture!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (mIsPremiumPaidPrice2) {
                    mHelper.consumeAsync(inventory.getPurchase(SKU_PREMIUM_PRICE2), new IabHelper.OnConsumeFinishedListener() {
                        @Override
                        public void onConsumeFinished(Purchase purchase, IabResult result) {
                            if (result.isSuccess()) {
//                                Toast.makeText(ActivityDonate.this, "ptice2 consumed", Toast.LENGTH_SHORT).show();
                                setStatus(3);
                            } else {
                                Toast.makeText(ActivityDonate.this, "ptice2 consumtion failture!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (mIsPremiumPaidPrice3) {
                    mHelper.consumeAsync(inventory.getPurchase(SKU_PREMIUM_PRICE3), new IabHelper.OnConsumeFinishedListener() {
                        @Override
                        public void onConsumeFinished(Purchase purchase, IabResult result) {
                            if (result.isSuccess()) {
//                                Toast.makeText(ActivityDonate.this, "ptice3 consumed", Toast.LENGTH_SHORT).show();
                                setStatus(3);
                            } else {
//                                Toast.makeText(ActivityDonate.this, "ptice3 consumtion failture!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                } else {
                    if (Utils.userHasPurchased(ActivityDonate.this)) {
                        setStatus(3);
                    } else {
                        setStatus(1);
                    }
                }
                Log.d(TAG, "User is " + (mIsPremiumPaidPrice1 ? "PREMIUM - paid price1 -" : (mIsPremiumPaidPrice2 ? "PREMIUM - paid price2 -" : (mIsPremiumPaidPrice3 ? "PREMIUM - paid price3" : "NOT PREMIUM"))));
            }

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };


    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Toast.makeText(ActivityDonate.this, "purchase finished", Toast.LENGTH_SHORT).show();
            if (mHelper == null) {
                return;
            }


            if (result.isFailure()) {
                setStatus(2);
                Log.d(TAG, "Error purchasing: " + result);
                Toast.makeText(ActivityDonate.this, "Error purchasing", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(ActivityDonate.this, "succesfull purchasing", Toast.LENGTH_SHORT).show();
                Utils.userPurchased(ActivityDonate.this);
                Log.d(TAG, "Successful purchase!");
                mHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
                    @Override
                    public void onConsumeFinished(Purchase purchase, IabResult result) {
                        if (result.isSuccess()) {
//                            Toast.makeText(ActivityDonate.this, "perchase consumed", Toast.LENGTH_SHORT).show();
                        } else {
//                            Toast.makeText(ActivityDonate.this, "perchase consumtion failture!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                if (purchase.getSku().equals(SKU_PREMIUM_PRICE1)) {
                    // give user access to premium content and update the UI
                    setStatus(5);
                } else if (purchase.getSku().equals(SKU_PREMIUM_PRICE2)) {
                    // give user access to premium content and update the UI
                    setStatus(5);
                } else if (purchase.getSku().equals(SKU_PREMIUM_PRICE3)) {
                    // give user access to premium content and update the UI
                    setStatus(5);
                }
            }
        }
    };


    private void startPaymentPrice1() {
        mHelper.launchPurchaseFlow(this, SKU_PREMIUM_PRICE1, RC_REQUEST_PAY_PRICE1, mPurchaseFinishedListener, "payload-string");
    }

    private void startPaymentPrice2() {
        mHelper.launchPurchaseFlow(this, SKU_PREMIUM_PRICE2, RC_REQUEST_PAY_PRICE2, mPurchaseFinishedListener, "payload-string");
    }

    private void startPaymentPrice3() {
        mHelper.launchPurchaseFlow(this, SKU_PREMIUM_PRICE3, RC_REQUEST_PAY_PRICE3, mPurchaseFinishedListener, "payload-string");
    }

    private void showAlert() {
        View.OnClickListener on = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        on.onClick(null);
    }

    private void enableButtons(boolean enable) {
        if (enable) {
//            pay_price1.setBackgroundColor(getResources().getColor(R.color.primary));
//            pay_price2.setBackgroundColor(getResources().getColor(R.color.primary));
//            pay_price3.setBackgroundColor(getResources().getColor(R.color.primary));
            pay_price1.setVisibility(View.VISIBLE);
            pay_price2.setVisibility(View.VISIBLE);
            pay_price3.setVisibility(View.VISIBLE);
        } else {
//            pay_price1.setBackgroundColor(getResources().getColor(R.color.gray));
//            pay_price2.setBackgroundColor(getResources().getColor(R.color.gray));
//            pay_price3.setBackgroundColor(getResources().getColor(R.color.gray));
            pay_price1.setVisibility(View.GONE);
            pay_price2.setVisibility(View.GONE);
            pay_price3.setVisibility(View.GONE);
        }
    }

    private void setStatus(int state) {
        if (state == 0) {
            status.setText(PersianReshape.reshape("خطا در روند بررسی!" + "\n" + "یکی از مشکلات زیر اتفاق افتاده. پس از رفع مشکل دوباره امتحان کنید!"));
            status.setTextColor(Color.RED);
            error.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            refresh.setVisibility(View.VISIBLE);
            enableButtons(false);
        } else if (state == 1) {
            status.setText(PersianReshape.reshape("شما تا کنون هزینه ای پرداخت نکرده اید."));
            status.setTextColor(getResources().getColor(R.color.black));
            error.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            refresh.setVisibility(View.GONE);
            enableButtons(true);
        } else if (state == 2) {
            status.setText(PersianReshape.reshape("پرداخت ناموفق!" + "\n" + "پرداخت ناموفق بود، دوباره امتحان کنید."));
            status.setTextColor(Color.RED);
            error.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            refresh.setVisibility(View.VISIBLE);
            enableButtons(true);
        } else if (state == 3) {
            status.setText(PersianReshape.reshape("شما پیش از این هزینه استفاده از برنامه را پرداخت کرده اید! با تشکر از شما (جهت تمایل به حمایت از ما میتوانید باز هم هزینه ای بپردازید.)"));
            status.setTextColor(getResources().getColor(R.color.primary));
            error.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            refresh.setVisibility(View.GONE);
            enableButtons(true);
        } else if (state == 4) {
            status.setText(PersianReshape.reshape("در حال بررسی ..."));
            status.setTextColor(getResources().getColor(R.color.black));
            error.setVisibility(View.GONE);
            refresh.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            enableButtons(false);
        } else if (state == 5) {
            status.setText(PersianReshape.reshape("پرداخت انجام شد!" + "\n" + "پاسخ شما به اعتماد ما مایه افتخار است! با تشکر."));
            status.setTextColor(getResources().getColor(R.color.primary));
            status.setTextColor(getResources().getColor(R.color.primary));
            error.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            refresh.setVisibility(View.GONE);
            enableButtons(true);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (refresh.getVisibility() == View.VISIBLE) {
            startChecking();
        }
    }

    @Override
    public void onBackPressed() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            SnackBar message = new SnackBar(this, PersianReshape.reshape("کمی صبر کنید! " + "\n" + "در حال بررسی ... "), null, null);
            message.show();
        } else {
            super.onBackPressed();
        }
    }
}
