package com.example.financeapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

    LinearLayout linLayout;
    LayoutInflater ltInflater;
    View item;
    ImageView labelOfTicker;
    TextView ticker;
    TextView price;
    TextView dividend;
    TextView changing;
    ImageView upOrDown;
    Button addToFavorites;
    Intent getIntent;
    String getExtra;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Получение строки, содержащий имя тикера из searchStock.class
        getIntent = getIntent();
        getExtra = getIntent.getStringExtra("enterTicker");
        getExtra = getExtra.toUpperCase();

        //Создание потока под запрос на Yahoo finance, для получения акции.
        new Thread(){
            @Override
            public void run() {
                Stock stock = null;
                try {

                    //Запрос
                    stock = YahooFinance.get(getExtra);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Возврат цены
                BigDecimal price3 = stock.getQuote().getPrice();

                //Возврат изменения за сутки
                BigDecimal change = stock.getQuote().getChange();

                //Возврат дивидендов в год
                BigDecimal dividends = stock.getDividend().getAnnualYieldPercent();

                //Возвращение к UI потоку для взаимодействия с имеющимися View.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linLayout = (LinearLayout) findViewById(R.id.linLayout);
                        ltInflater = getLayoutInflater();
                        item = ltInflater.inflate(R.layout.find_stock_activity, linLayout, false);

                        for (int i = 0; i < tickerPlusLabel.tickers.length; i++) {
                            tickerPlusLabel.stockStack.put(tickerPlusLabel.tickers[i], tickerPlusLabel.drawable[i]);
                        }

                        labelOfTicker = (ImageView) item.findViewById(R.id.imageTesla);
                        if(tickerPlusLabel.stockStack.get(getExtra) != null) {
                            labelOfTicker.setImageResource(tickerPlusLabel.stockStack.get(getExtra));
                        }else{
                            labelOfTicker.setImageResource(R.drawable.clear);
                        }

                        ticker = (TextView) item.findViewById(R.id.ticker);
                        ticker.setText(getExtra);

                        price = (TextView) item.findViewById(R.id.showPrice);
                        price.setText(String.valueOf(price3));

                        dividend = (TextView) item.findViewById(R.id.showDivident);
                        if(dividends != null) {
                            dividend.setText(String.valueOf(dividends));
                        }else{
                            dividend.setText("-");
                        }

                        changing = (TextView) item.findViewById(R.id.showChange);
                        changing.setText(String.valueOf(change));

                        //Вывод зеленой\красной стрелки, в зависимости от знака перед значением.
                        upOrDown = (ImageView) item.findViewById(R.id.upDown);
                        double converter = Double.parseDouble(String.valueOf(change));
                        if (converter > 0) {
                            upOrDown.setScaleY(1);
                            upOrDown.setScaleX(1);
                            upOrDown.setImageResource(R.drawable.gr);
                        } else {
                            upOrDown.setScaleY(1);
                            upOrDown.setScaleX(1);
                            upOrDown.setImageResource(R.drawable.r);
                        }

                        addToFavorites = (Button) item.findViewById(R.id.getSearch);
                        addToFavorites.setText(R.string.addToFavorites);
                        addToFavorites.setTag(getExtra);

                        linLayout.addView(item);
                    }
                });
            }
        }.start();
    }

    public void onClick(View v){

        //Добавление в избранное.
        new Thread(){
            @Override
            public void run() {
                Stock stock = null;
                try {
                    stock = YahooFinance.get((String) v.getTag());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BigDecimal price3 = stock.getQuote().getPrice();
                BigDecimal change = stock.getQuote().getChange();
                BigDecimal dividends = stock.getDividend().getAnnualYieldPercent();


                Stock finalStock = stock;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Добавление в кеш.
                        if(!tickerPlusLabel.tickerList.contains((String) v.getTag())){
                            tickerPlusLabel.tickerList.add((String) v.getTag());
                            tickerPlusLabel.priceList.add(String.valueOf((price3)));
                            tickerPlusLabel.changeList.add(String.valueOf((change)));
                            tickerPlusLabel.dividentList.add(String.valueOf((dividends)));
                        }
                    };
                });
            }
        }.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Сохранение избранного.
        saveText();
    }

    //Функция сохранение избранного.
    public void saveText() {
        for(int i = 0; i < tickerPlusLabel.tickersForFavorites.size(); i++) {
            SharedPreferences sPref = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(String.valueOf(i), tickerPlusLabel.tickersForFavorites.get(i));
            ed.apply();
        }
    }
}
