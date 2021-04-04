package com.example.financeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.math.BigDecimal;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

//Активити детализации акции из избранного.
public class showDetailsFromCache extends Activity {

    Boolean flag;
    TextView ticker;
    Intent getIntent;
    TextView dividend;
    TextView changing;
    TextView changingIndex;
    ImageView upOrDown;
    TextView priceText;
    String getExtraPrice;
    String getExtraTicker;
    String getExtraChange;
    String getExtraChangeIndex;
    String getExtraDivident;
    ImageView labelOfTicker;
    Animation animAlpha;
    Button showHistory;

    //Активити детализации акции при вызове из фаворитов.
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_details_activity);

        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);

        //Получение данных из Intent, которые содержат имя тикера, цену, изменение за день и дивиденды в год из favorites.class.
        getIntent = getIntent();
        getExtraTicker = getIntent.getStringExtra("ticker");
        getExtraPrice = getIntent.getStringExtra("price");
        getExtraChange = getIntent.getStringExtra("change");
        getExtraChangeIndex = getIntent.getStringExtra("changeIndex");
        getExtraDivident = getIntent.getStringExtra("divident");

        labelOfTicker = (ImageView) findViewById(R.id.imageTesla);

        //Вставить заглушку в виде стандартной картинки, если подходящая не найдена.
        if (tickerPlusLabel.stockStack.get(getExtraTicker) != null) {
            labelOfTicker.setImageResource(tickerPlusLabel.stockStack.get(getExtraTicker));
        } else {
            labelOfTicker.setImageResource(R.drawable.clear);
        }

        ticker = (TextView) findViewById(R.id.ticker);
        ticker.setText(getExtraTicker);

        priceText = (TextView) findViewById(R.id.showPrice);
        priceText.setText("$ " + String.valueOf(getExtraPrice));

        dividend = (TextView) findViewById(R.id.showDivident);
        dividend.setText(getExtraDivident + "%");

        changingIndex = (TextView) findViewById(R.id.showIndexFind);
        changingIndex.setText(getExtraChangeIndex);

        changing = (TextView) findViewById(R.id.showChange);
        changing.setText("(" + String.valueOf(getExtraChange) + "%)");

        //Вывод зеленой\красной стрелки, в зависимости от знака перед значением.
        upOrDown = (ImageView) findViewById(R.id.upDown);
        double converter = Double.parseDouble(String.valueOf(getExtraChange));
        if (converter > 0) {
            upOrDown.setScaleY(1);
            upOrDown.setScaleX(1);
            upOrDown.setImageResource(R.drawable.gr);
        } else {
            upOrDown.setScaleY(1);
            upOrDown.setScaleX(1);
            upOrDown.setImageResource(R.drawable.r);
        }

        //Выовод исторических данных.
        showHistory = (Button) findViewById(R.id.getSearch);
        showHistory.setText(R.string.showHistory);
        showHistory.setTag(getExtraTicker);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flag = true;

        //Создание потока под запрос на Yahoo finance, для получения акции.
        new Thread(){
            @Override
            public void run() {

                //Начало обновления данных по акции.
                while (flag) {

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //Запрос.
                    Stock stockFind = null;
                    try {
                        stockFind = YahooFinance.get(getExtraTicker);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Возврат цены.
                    BigDecimal price;
                    price = stockFind.getQuote().getPrice();

                    //Возврат изменения за сутки.
                    BigDecimal change;
                    change = stockFind.getQuote().getChangeInPercent();

                    //Получение изменения за сутки.
                    BigDecimal changeIndex;
                    changeIndex = stockFind.getQuote().getChange();

                    //Возврат дивидендов в год.
                    BigDecimal dividends;
                    dividends = stockFind.getDividend().getAnnualYieldPercent();

                    //Возвращение к UI потоку для взаимодействия с имеющимися View.
                    BigDecimal finalPrice = price;
                    BigDecimal finalChange = change;
                    BigDecimal finalChangeIndex = changeIndex;
                    BigDecimal finalDividents = dividends;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            ticker.setText(getExtraTicker);

                            priceText.setText("$ " + String.valueOf(finalPrice));

                            if (finalDividents != null) {
                                dividend.setText(String.valueOf(finalDividents) + "%");
                            } else {
                                dividend.setText("-");
                            }

                            changingIndex.setText(String.valueOf(finalChangeIndex));

                            changing.setText("(" + String.valueOf(finalChange) + "%)");

                            //Вывод зеленой\красной стрелки, в зависимости от знака перед значением.
                            upOrDown = (ImageView) findViewById(R.id.upDown);
                            double converter = Double.parseDouble(String.valueOf(finalChange));
                            if (converter > 0) {
                                upOrDown.setScaleY(1);
                                upOrDown.setScaleX(1);
                                upOrDown.setImageResource(R.drawable.gr);
                            } else {
                                upOrDown.setScaleY(1);
                                upOrDown.setScaleX(1);
                                upOrDown.setImageResource(R.drawable.r);
                            }
                        }
                    });
                }
            }
        }.start();
    }

    public void onClickShowHistory(View v){
        v.startAnimation(animAlpha);

        Intent intentFindStock = new Intent("historyList");
        intentFindStock.putExtra("getHistory", (String) v.getTag());
        startActivity(intentFindStock);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Завершение обновления данных по акции.
        flag = false;
    }
}
