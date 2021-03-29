package com.example.financeapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

//Активити стартового списка трендовых акций.
public class originalList extends Activity {

    LinearLayout linLayout;
    LayoutInflater ltInflater;
    View item;
    TextView ticker;
    TextView price;
    TextView changing;
    ImageView label;
    ImageView upOrDown;
    Button addToFavorites;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Создание потока под запрос на Yahoo finance, для получения акции.
        new Thread(){
                @Override
                public void run() {
                    Stock stock = null;
                    for (int i = 0; i < tickerPlusLabel.tickers.length; i++) {
                        try {

                            //Запрос
                            stock = YahooFinance.get(tickerPlusLabel.tickers[i]);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Получение цены
                        BigDecimal price3 = stock.getQuote().getPrice();

                        //Получение изменения за сутки
                        BigDecimal change = stock.getQuote().getChange();

                        //Возвращение к UI потоку для взаимодействия с имеющимися View.
                        int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                linLayout = (LinearLayout) findViewById(R.id.linLayout);
                                ltInflater = getLayoutInflater();
                                item = ltInflater.inflate(R.layout.item, linLayout, false);

                                label = (ImageView) item.findViewById(R.id.imageTesla);
                                label.setImageResource(tickerPlusLabel.drawable[finalI]);

                                ticker = (TextView) item.findViewById(R.id.ticker);
                                ticker.setText(tickerPlusLabel.tickers2[finalI]);

                                price = (TextView) item.findViewById(R.id.price);
                                price.setText("$ " + String.valueOf(price3));

                                changing = (TextView) item.findViewById(R.id.changing);
                                changing.setText(String.valueOf(change));

                                //Вывод зеленой\красной стрелки, в зависимости от знака перед значением.
                                upOrDown = (ImageView) item.findViewById(R.id.upOrDown);
                                double converter =  Double.parseDouble(String.valueOf(change));
                                if(converter > 0) {
                                    upOrDown.setScaleY(1);
                                    upOrDown.setScaleX(1);
                                    upOrDown.setImageResource(R.drawable.gr);
                                }else{
                                    upOrDown.setScaleY(1);
                                    upOrDown.setScaleX(1);
                                    upOrDown.setImageResource(R.drawable.r);
                                }

                                addToFavorites = (Button) item.findViewById(R.id.favorits);
                                addToFavorites.setText(R.string.addToFavorites);
                                addToFavorites.setTag(tickerPlusLabel.tickers[finalI]);

                                linLayout.addView(item);
                            }
                        });
                    }
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
                }
                });
        }
    }.start();

    }
}