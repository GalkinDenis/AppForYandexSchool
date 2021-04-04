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

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

public class historyList extends Activity {

    View item;
    Intent getIntent;
    String getExtra;

    LinearLayout linLayout;
    LayoutInflater ltInflater;

    TextView symbolPrice;
    TextView showSymbolPrice;

    TextView datePrice;
    TextView showDatePrice;

    TextView highPrice;
    TextView showHighPrice;

    TextView lowPrice;
    TextView showLowPrice;

    TextView closePrice;
    TextView showClosePrice;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Получение строки, содержащий имя тикера.
        getIntent = getIntent();
        getExtra = getIntent.getStringExtra("getHistory");
        getExtra = getExtra.toUpperCase();

        //Создание потока под запрос на Yahoo finance, для получения акции.

        new Thread() {
            @Override
            public void run() {

                //Запрос.
                Stock stock = null;
                try {
                    stock = YahooFinance.get(getExtra);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                List<HistoricalQuote> history = null;
                try {
                    assert stock != null;
                    history = stock.getHistory();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                    //Возвращение к UI потоку для взаимодействия с имеющимися View.
                List<HistoricalQuote> finalHistory = history;
                runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (HistoricalQuote quote : finalHistory) {

                                String symbol = quote.getSymbol();
                                String date = convertDate(quote.getDate());
                                BigDecimal high = quote.getHigh();
                                BigDecimal low = quote.getLow();
                                BigDecimal close = quote.getClose();

                            linLayout = (LinearLayout) findViewById(R.id.linLayout);
                            ltInflater = getLayoutInflater();
                            item = ltInflater.inflate(R.layout.history_item, linLayout, false);

                            symbolPrice = (TextView) item.findViewById(R.id.symbolPrice);
                            showSymbolPrice = (TextView) item.findViewById(R.id.showSymbolPrice);
                            showSymbolPrice.setText(symbol);

                            datePrice = (TextView) item.findViewById(R.id.datePrice);
                            showDatePrice = (TextView) item.findViewById(R.id.showDatePrice);
                            showDatePrice.setText(date);

                            highPrice = (TextView) item.findViewById(R.id.highPrice);
                            showHighPrice = (TextView) item.findViewById(R.id.showHighPrice);
                            showHighPrice.setText(String.valueOf(high));

                            lowPrice = (TextView) item.findViewById(R.id.lowPrice);
                            showLowPrice = (TextView) item.findViewById(R.id.showLowPrice);
                            showLowPrice.setText(String.valueOf(low));

                            closePrice = (TextView) item.findViewById(R.id.changing);
                            showClosePrice = (TextView) item.findViewById(R.id.showClosePrice);
                            showClosePrice.setText(String.valueOf(close));

                            //Добавление элемента в список.
                            linLayout.addView(item);
                        }
                    }
                });
            }
        }.start();
    }

    private String convertDate(Calendar cal){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddd");
        return format.format(cal.getTime());
    }
}
