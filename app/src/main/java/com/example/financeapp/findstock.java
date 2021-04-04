package com.example.financeapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.math.BigDecimal;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

//Активити найденной акции.
public class findstock extends Activity {

    ImageView labelOfTicker;
    TextView showTicker;
    TextView showIndex;
    TextView showPrice;
    TextView showDivident;
    TextView showChanging;
    ImageView upOrDown;
    Button addToFavorites;
    Button showHistory;
    Intent getIntent;
    String getExtra;
    Animation animAlpha;
    Boolean flag;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_stock_activity);
        flag = true;

        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);

        //Получение строки, содержащий имя тикера.
        getIntent = getIntent();
        getExtra = getIntent.getStringExtra("enter");
        getExtra = getExtra.toUpperCase();

        //Создание потока под запрос на Yahoo finance, для получения акции.
        new Thread(){
            @Override
            public void run() {

                //Запуск цикла, для переодического обновления показателей.
                while (flag) {
                    //Запрос.
                    Stock stockFind = null;
                    try {
                        stockFind = YahooFinance.get(getExtra);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Возврат цены.
                    BigDecimal price = null;
                    try {
                        price = stockFind.getQuote(true).getPrice();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Возврат изменения за сутки.
                    BigDecimal change = null;
                    try {
                        change = stockFind.getQuote(true).getChangeInPercent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Получение изменения за сутки.
                    BigDecimal changeIndex = null;
                    try {
                        changeIndex = stockFind.getQuote(true).getChange();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Возврат дивидендов в год.
                    BigDecimal dividends = null;
                    try {
                        dividends = stockFind.getDividend(true).getAnnualYieldPercent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Возвращение к UI потоку для взаимодействия с имеющимися View.
                    BigDecimal finalPrice = price;
                    BigDecimal finalChange = change;
                    BigDecimal finalChangeIndex = changeIndex;
                    BigDecimal finalDividents = dividends;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            labelOfTicker = (ImageView) findViewById(R.id.imageTesla);

                            //Вставить заглушку в виде стандартной картинки, если подходящая не найдена.
                            if (tickerPlusLabel.stockStack.get(getExtra) != null) {
                                labelOfTicker.setImageResource(tickerPlusLabel.stockStack.get(getExtra));
                            } else {
                                labelOfTicker.setImageResource(R.drawable.clear);
                            }

                            showTicker = (TextView) findViewById(R.id.ticker);
                            showTicker.setText(getExtra);

                            showPrice = (TextView) findViewById(R.id.showPrice);
                            showPrice.setText("$ " + String.valueOf(finalPrice));

                            showDivident = (TextView) findViewById(R.id.showDivident);
                            if (finalDividents != null) {
                                showDivident.setText(String.valueOf(finalDividents) + "%");
                            } else {
                                showDivident.setText("-");
                            }

                            showIndex = (TextView) findViewById(R.id.indexFind);
                            showIndex.setText(String.valueOf(finalChangeIndex));

                            showChanging = (TextView) findViewById(R.id.showChange);
                            showChanging.setText("(" + String.valueOf(finalChange) + "%)");

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

                            //Кнопка добавления в избранное.
                            addToFavorites = (Button) findViewById(R.id.getSearch);
                            addToFavorites.setText(R.string.addToFavorites);
                            addToFavorites.setTag(getExtra);

                            //Выовод исторических данных.
                            showHistory = (Button) findViewById(R.id.showHistory);
                            showHistory.setText(R.string.showHistory);
                            showHistory.setTag(getExtra);
                        }
                    });

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Завершение обновления данных по акции.
        flag = false;
    }

    public void onClickAddToFavorites(View v){
        v.startAnimation(animAlpha);

        //Добавление в избранное.
        new Thread(){
            @Override
            public void run() {
                Stock stockFind2 = null;
                try {
                    stockFind2 = YahooFinance.get((String) v.getTag());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BigDecimal price2 = null;
                try {
                    price2 = stockFind2.getQuote(true).getPrice();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BigDecimal change2 = null;
                try {
                    change2 = stockFind2.getQuote(true).getChangeInPercent();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Получение изменения за сутки.
                BigDecimal changeIndex2 = null;
                try {
                    changeIndex2 = stockFind2.getQuote(true).getChange();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BigDecimal dividends = null;
                try {
                    dividends = stockFind2.getDividend(true).getAnnualYieldPercent();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BigDecimal finalPrice2 = price2;
                BigDecimal finalChange2 = change2;
                BigDecimal finalChangeIndex2 = changeIndex2;
                BigDecimal finalDividends2 = dividends;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //Добавление в кеш.
                        if(!tickerPlusLabel.tickerList.contains((String) v.getTag())){

                            tickerPlusLabel.tickerList.add((String) v.getTag());
                            tickerPlusLabel.priceList.add(String.valueOf((finalPrice2)));
                            tickerPlusLabel.changeList.add(String.valueOf((finalChange2)));
                            tickerPlusLabel.changeIndexList.add(String.valueOf((finalChangeIndex2)));
                            tickerPlusLabel.dividentList.add(String.valueOf((finalDividends2)));
                        }

                    };
                });
            }
        }.start();
    }

    public void onClickShowHistory(View v){
        v.startAnimation(animAlpha);

        Intent intentFindStock = new Intent("historyList");
        intentFindStock.putExtra("getHistory", (String) v.getTag());
        startActivity(intentFindStock);
    }
}
